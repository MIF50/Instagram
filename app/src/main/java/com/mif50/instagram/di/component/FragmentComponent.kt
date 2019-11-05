package com.mif50.instagram.di.component

import com.mif50.instagram.di.FragmentScope
import com.mif50.instagram.di.module.FragmentModule
import com.mif50.instagram.ui.dummies.DummiesFragment
import com.mif50.instagram.ui.home.HomeFragment
import com.mif50.instagram.ui.photo.PhotoFragment
import com.mif50.instagram.ui.profile.ProfileFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: DummiesFragment)
//
    fun inject(fragment: HomeFragment)
//
    fun inject(fragment: PhotoFragment)
//
    fun inject(fragment: ProfileFragment)
}