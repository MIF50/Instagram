package com.mif50.instagram.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mif50.instagram.ui.main.MainActivity
import com.mif50.instagram.R
import com.mif50.instagram.di.component.ActivityComponent
import com.mif50.instagram.ui.base.BaseActivity
import com.mif50.instagram.ui.login.LoginActivity
import com.mif50.instagram.utils.common.Event

class SplashActivity : BaseActivity<SplashViewModel>() {

    companion object {
        const val TAG = "SplashActivity"
    }

    override fun provideLayoutId(): Int = R.layout.activity_splash

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }

    override fun setupObservers() {
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchMain.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })

        viewModel.launchLogin.observe(this, Observer<Event<Map<String,String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        })
    }
}
