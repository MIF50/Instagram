package com.mif50.instagram.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.mif50.instagram.ui.main.MainActivity
import com.mif50.instagram.R
import com.mif50.instagram.di.component.ActivityComponent
import com.mif50.instagram.ui.base.BaseActivity
import com.mif50.instagram.ui.login.LoginActivity
import com.mif50.instagram.utils.common.Event
import com.mif50.instagram.utils.common.LayoutRes
import com.mif50.instagram.utils.ktx.startActivity

@LayoutRes(layout = R.layout.activity_splash)
class SplashActivity : BaseActivity<SplashViewModel>() {

    companion object {
        const val TAG = "SplashActivity"
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }

    override fun setupObservers() {
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        observerMainScreen()
        observerLoginScreen()
    }

    private fun observerMainScreen(){
        viewModel.launchMain.observe(this, Observer {
            it.getIfNotHandled()?.run {
                Handler().postDelayed({ startActivity<MainActivity>() },1000)
            }
        })
    }

    private fun observerLoginScreen(){
        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        })
    }
}
