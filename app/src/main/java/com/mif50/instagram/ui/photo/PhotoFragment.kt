package com.mif50.instagram.ui.photo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mif50.instagram.R
import com.mif50.instagram.di.component.FragmentComponent
import com.mif50.instagram.ui.base.BaseFragment

class PhotoFragment : BaseFragment<PhotoViewModel>() {

    companion object {
        const val TAG = "PhotoFragment"

        fun newInstance() = PhotoFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun provideLayoutId(): Int  = R.layout.fragment_photo

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()
    }

    override fun setupView(view: View) {

    }

}
