package com.mif50.instagram.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mif50.instagram.R
import com.mif50.instagram.di.component.FragmentComponent
import com.mif50.instagram.ui.base.BaseFragment
import com.mif50.instagram.ui.home.posts.PostsAdapter
import com.mif50.instagram.ui.main.MainSharedViewModel
import com.mif50.instagram.utils.common.LayoutRes
import com.mif50.instagram.utils.view.rv.isLastItem
import com.mif50.instagram.utils.view.rv.setOnScrollListener
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@LayoutRes(layout = R.layout.fragment_home)
class HomeFragment : BaseFragment<HomeViewModel>() {

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel
    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager
    @Inject
    lateinit var postsAdapter: PostsAdapter

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        observerLoading()
        observerPosts()
        observerNewPost()
        observerRefreshPosts()
    }

    private fun observerLoading(){
        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun observerPosts(){
        viewModel.posts.observe(this, Observer {
            it.data?.run {
                postsAdapter.appendData(this) }
        })
    }

    private fun observerNewPost(){
        mainSharedViewModel.newPost.observe(this, Observer {
            it.getIfNotHandled()?.run { viewModel.onNewPost(this) }
        })

    }

    private fun observerRefreshPosts(){
        viewModel.refreshPosts.observe(this, Observer {
            it.data?.run {
                postsAdapter.updateList(this)
                rvPosts.scrollToPosition(0)
            }
        })
    }

    override fun setupView(view: View) {
        initAdapter()
    }

    private fun initAdapter(){
        rvPosts.apply {
            layoutManager = linearLayoutManager
            adapter = postsAdapter
            setOnScrollListener { _, _, _ ->
                if (linearLayoutManager.isLastItem()) {
                    viewModel.onLoadMore()
                }
            }
        }
    }

}
