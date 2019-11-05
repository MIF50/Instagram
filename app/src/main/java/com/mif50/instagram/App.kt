package com.mif50.instagram

import android.app.Application
import com.mif50.instagram.di.component.ApplicationComponent
import com.mif50.instagram.di.component.DaggerApplicationComponent
import com.mif50.instagram.di.module.ApplicationModule

class App: Application() {


    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}