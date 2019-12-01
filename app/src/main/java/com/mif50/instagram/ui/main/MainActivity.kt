package com.mif50.instagram.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mif50.instagram.R
import com.mif50.instagram.di.component.ActivityComponent
import com.mif50.instagram.ui.base.BaseActivity
import com.mif50.instagram.ui.home.HomeFragment
import com.mif50.instagram.ui.photo.PhotoFragment
import com.mif50.instagram.ui.profile.ProfileFragment
import com.mif50.instagram.utils.common.LayoutRes
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@LayoutRes(layout = R.layout.activity_main)
class MainActivity : BaseActivity<MainViewModel>() {

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    companion object {
        const val TAG = "MainActivity"
    }

    private var activityFragment: Fragment? = null

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        bottomNavigation.run {
            itemIconTintList = null
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.itemHome -> {
                        viewModel.onHomeTapped()
                        true
                    }
                    R.id.itemAddPhotos -> {
                        viewModel.onPhotoTapped()
                        true
                    }
                    R.id.itemProfile -> {
                        viewModel.onProfileTapped()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.homeNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run { showHome() }
        })

        viewModel.profileNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run { showProfile() }
        })

        viewModel.photoNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run { showAddPhoto() }
        })

        mainSharedViewModel.homeRedirection.observe(this, Observer {
            it.getIfNotHandled()?.run { bottomNavigation.selectedItemId = R.id.itemHome }
        })

    }

    private fun showHome() {
        if (activityFragment is HomeFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment?

        if (fragment == null) {
            fragment = HomeFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment, fragment, HomeFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activityFragment != null) fragmentTransaction.hide(activityFragment as Fragment)

        fragmentTransaction.commit()

        activityFragment = fragment

    }

    private fun showProfile() {

        if (activityFragment is ProfileFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) as ProfileFragment?

        if (fragment == null) {
            fragment = ProfileFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment, fragment, ProfileFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activityFragment != null) fragmentTransaction.hide(activityFragment as Fragment)

        fragmentTransaction.commit()

        activityFragment = fragment

    }

    private fun showAddPhoto() {

        if (activityFragment is PhotoFragment) return
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(PhotoFragment.TAG) as PhotoFragment?

        if (fragment == null) {
            fragment = PhotoFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment, fragment, PhotoFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activityFragment != null) fragmentTransaction.hide(activityFragment as Fragment)

        fragmentTransaction.commit()
        activityFragment = fragment

    }

}
