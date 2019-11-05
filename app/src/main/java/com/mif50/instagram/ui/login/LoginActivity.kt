package com.mif50.instagram.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mif50.instagram.ui.main.MainActivity
import com.mif50.instagram.R
import com.mif50.instagram.di.component.ActivityComponent
import com.mif50.instagram.ui.base.BaseActivity
import com.mif50.instagram.ui.register.SignUpActivity
import com.mif50.instagram.utils.common.Event
import com.mif50.instagram.utils.common.Status
import com.mif50.instagram.utils.view.getString
import com.mif50.instagram.utils.view.setupTextWatch
import com.mif50.instagram.utils.view.toggleVisibility
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel>() {

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun provideLayoutId(): Int = R.layout.activity_login

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {

        et_email.setupTextWatch { text ->
            viewModel.onEmailChange(text)
        }


        et_password.setupTextWatch { text ->
            viewModel.onPasswordChange(text)
        }

        bt_login.setOnClickListener { viewModel.onLogin() }

        bt_sign_up.setOnClickListener { viewModel.onSignUpTapped() }
    }

    override fun setupObservers() {
        super.setupObservers()
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchMain.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })

        viewModel.launchSignUp.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, SignUpActivity::class.java))
            }
        })

        viewModel.emailField.observe(this, Observer {
            if (et_email.getString() != it) et_email.setText(it)
        })

        viewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_email.error = it.data?.run { getString(this) }
                else -> layout_email.isErrorEnabled = false
            }
        })

        viewModel.passwordField.observe(this, Observer {
            if (et_password.getString() != it) et_email.setText(it)
        })

        viewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_password.error = it.data?.run { getString(this) }
                else -> layout_password.isErrorEnabled = false
            }
        })

        viewModel.loggingIn.observe(this, Observer {
            pb_loading.toggleVisibility(flag = it)
        })
    }

}