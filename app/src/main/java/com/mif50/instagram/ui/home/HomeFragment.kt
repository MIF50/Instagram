package com.mif50.instagram.ui.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.mif50.instagram.R
import com.mif50.instagram.di.component.FragmentComponent
import com.mif50.instagram.ui.base.BaseFragment
import com.mif50.instagram.ui.home.posts.PostsAdapter
import com.mif50.instagram.ui.main.MainSharedViewModel
import com.mif50.instagram.utils.common.LayoutRes
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@LayoutRes(layout = R.layout.fragment_home)
class HomeFragment : BaseFragment<HomeViewModel>() {

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    companion object {
        const val TAG = "HomeFragment"

        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var postsAdapter: PostsAdapter


    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.posts.observe(this, Observer {
            it.data?.run { postsAdapter.appendData(this) }
        })

        mainSharedViewModel.newPost.observe(this, Observer {
            it.getIfNotHandled()?.run { viewModel.onNewPost(this) }
        })

        viewModel.refreshPosts.observe(this, Observer {
            it.data?.run {
                postsAdapter.updateList(this)
                rvPosts.scrollToPosition(0)
            }
        })
    }

    override fun setupView(view: View) {

        rvPosts.apply {
            layoutManager = linearLayoutManager
            adapter = postsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.run {
                        if (this is LinearLayoutManager && itemCount > 0 && itemCount == findLastCompletelyVisibleItemPosition() + 1) {
                            viewModel.onLoadMore()
                        }
                    }
                }
            })
        }

    }

}
