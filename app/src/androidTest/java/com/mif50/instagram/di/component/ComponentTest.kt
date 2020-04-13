package com.mif50.instagram.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.mif50.instagram.App
import com.mif50.instagram.data.local.db.DatabaseService
import com.mif50.instagram.data.remote.NetworkService
import com.mif50.instagram.data.repository.UserRepository
import com.mif50.instagram.di.ApplicationContext
import com.mif50.instagram.di.TempDirectory
import com.mif50.instagram.di.module.ApplicationModuleTest
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModuleTest::class])
interface ComponentTest: ApplicationComponent{

    override fun inject(app: App)

    override fun getApplication(): Application

    @ApplicationContext
    override fun getContext(): Context

    /**
     * These methods are written in ApplicationComponent because the instance of
     * NetworkService is singleton and is maintained in the ApplicationComponent's implementation by Dagger
     * For NetworkService singleton instance to be accessible to say DummyActivity's DummyViewModel
     * this ApplicationComponent must expose a method that returns NetworkService instance
     * This method will be called when NetworkService is injected in DummyViewModel.
     * Also, in ActivityComponent you can find dependencies = [ApplicationComponent::class] to link this relationship
     */
    override fun getNetworkService(): NetworkService

    override fun getDatabaseService(): DatabaseService

    override fun getSharedPreferences(): SharedPreferences

    override fun getNetworkHelper(): NetworkHelper

    /**---------------------------------------------------------------------------
     * Dagger will internally create UserRepository instance using constructor injection.
     * Dependency through constructor
     * UserRepository ->
     *  [NetworkService -> Nothing is required],
     *  [DatabaseService -> Nothing is required],
     *  [UserPreferences -> [SharedPreferences -> provided by the function provideSharedPreferences in ApplicationModule class]]
     * So, Dagger will be able to create an instance of UserRepository by its own using constructor injection
     *---------------------------------------------------------------------------------
     */
    override fun getUserRepository(): UserRepository

    override fun getSchedulerProvider(): SchedulerProvider

    override fun getCompositeDisposable(): CompositeDisposable

    @TempDirectory
    override fun getTempDirectory(): File

}