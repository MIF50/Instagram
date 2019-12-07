package com.mif50.instagram.ui.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer

import com.mif50.instagram.R
import com.mif50.instagram.di.component.FragmentComponent
import com.mif50.instagram.ui.base.BaseFragment
import com.mif50.instagram.ui.login.LoginActivity
import com.mif50.instagram.ui.profile.edit.EditProfileActivity
import com.mif50.instagram.utils.common.LayoutRes
import com.mif50.instagram.utils.ktx.startActivity
import com.mif50.instagram.utils.view.toggleVisibility
import kotlinx.android.synthetic.main.fragment_profile.*

@LayoutRes(layout = R.layout.fragment_profile)
class ProfileFragment : BaseFragment<ProfileViewModel>() {

    companion object {
        const val TAG = "ProfileFragment"

        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }


    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.launchEditProfileActivity.observe(this, Observer {
            it.getIfNotHandled()?.run {
                activity?.startActivity<EditProfileActivity>()
            }
        })

        viewModel.loggingOut.observe(this, Observer {
            profileProgressBar.toggleVisibility(it)
        })

        viewModel.launchLoginScreen.observe(this, Observer {
            it.getIfNotHandled()?.run {
                activity?.startActivity<LoginActivity>()
            }
        })

    }

    override fun setupView(view: View) {
        btnEditProfile.setOnClickListener {
            viewModel.onEditProfileTapped()
        }

        btnLogout.setOnClickListener {
            viewModel.onLogout()
        }

    }

}