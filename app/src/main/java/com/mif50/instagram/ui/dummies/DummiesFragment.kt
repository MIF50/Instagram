package com.mif50.instagram.ui.dummies


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.mif50.instagram.R
import com.mif50.instagram.di.component.FragmentComponent
import com.mif50.instagram.ui.base.BaseFragment
import com.mif50.instagram.utils.common.LayoutRes
import kotlinx.android.synthetic.main.fragment_dummies.*
import javax.inject.Inject

@LayoutRes(layout = R.layout.fragment_dummies)
class DummiesFragment : BaseFragment<DummiesViewModel>() {

    companion object {

        const val TAG = "DummiesFragment"

        fun newInstance(): DummiesFragment {
            val args = Bundle()
            val fragment = DummiesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var dummiesAdapter: DummiesAdapter


    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        viewModel.getDummies().observe(this, Observer {
            it?.run { dummiesAdapter.appendData(this) }
        })
    }

    override fun setupView(view: View) {
        rv_dummy.layoutManager = linearLayoutManager
        rv_dummy.adapter = dummiesAdapter
    }

}
