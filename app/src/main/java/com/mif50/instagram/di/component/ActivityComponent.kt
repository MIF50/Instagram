package com.mif50.instagram.di.component

import com.mif50.instagram.ui.main.MainActivity
import com.mif50.instagram.di.ActivityScope
import com.mif50.instagram.di.module.ActivityModule
import com.mif50.instagram.ui.dummy.DummyActivity
import com.mif50.instagram.ui.login.LoginActivity
import com.mif50.instagram.ui.profile.edit.EditProfileActivity
import com.mif50.instagram.ui.register.SignUpActivity
import com.mif50.instagram.ui.splash.SplashActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: SplashActivity)

    fun inject(activity: DummyActivity)

    fun inject(activity: LoginActivity)

    fun inject(activity: SignUpActivity)

    fun inject(activity: MainActivity)

    fun inject(activity: EditProfileActivity)
}