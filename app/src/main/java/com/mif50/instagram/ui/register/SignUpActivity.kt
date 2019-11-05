package com.mif50.instagram.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mif50.instagram.ui.main.MainActivity
import com.mif50.instagram.R
import com.mif50.instagram.di.component.ActivityComponent
import com.mif50.instagram.ui.base.BaseActivity
import com.mif50.instagram.ui.login.LoginActivity
import com.mif50.instagram.utils.common.Event
import com.mif50.instagram.utils.common.Status
import com.mif50.instagram.utils.view.getString
import com.mif50.instagram.utils.view.setupTextWatch
import com.mif50.instagram.utils.view.toggleVisibility
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity<SignUpViewModel>() {

    companion object{
        const val TAG = "SignUpActivity"
    }
    override fun provideLayoutId(): Int  = R.layout.activity_sign_up

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        et_full_name.setupTextWatch{ text ->
            viewModel.onFullNameChange(text)
        }

        et_email.setupTextWatch { text ->
            viewModel.onEmailChange(text)
        }

        et_password.setupTextWatch { text ->
            viewModel.onPasswordChange(text)
        }

        bt_sign_up.setOnClickListener {
            viewModel.onSignUp()
        }

        bt_login.setOnClickListener { viewModel.onLoginTapped() }
    }

    override fun setupObservers() {
        super.setupObservers()
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity

        viewModel.launchHome.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }

        })

        viewModel.launchLogin.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        })

        viewModel.fullNameField.observe(this, Observer {
            if (et_full_name.getString() != it) et_full_name.setText(it)
        })

        viewModel.fullNameValidation.observe(this, Observer {
            when(it.status){
                Status.ERROR -> layout_fullName.error = it.data?.run { getString(this) }
                else -> layout_fullName.isErrorEnabled = false
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
            if (et_password.getString() != it) et_password.setText(it)
        })

        viewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_password.error = it.data?.run { getString(this) }
                else -> layout_password.isErrorEnabled = false
            }
        })

        viewModel.signingUp.observe(this, Observer {
            pb_loading.toggleVisibility(it)
        })

    }

}
