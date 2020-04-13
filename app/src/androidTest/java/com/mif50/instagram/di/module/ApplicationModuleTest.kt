package com.mif50.instagram.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.mif50.instagram.App
import com.mif50.instagram.BuildConfig
import com.mif50.instagram.data.local.db.DatabaseService
import com.mif50.instagram.data.remote.NetworkService
import com.mif50.instagram.data.remote.Networking
import com.mif50.instagram.di.ApplicationContext
import com.mif50.instagram.di.TempDirectory
import com.mif50.instagram.utils.common.FileUtils
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.RxSchedulerProvider
import com.mif50.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModuleTest (private val application: App){

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application.applicationContext

    /**
     * Since this function do not have @Singleton then each time CompositeDisposable is injected
     * then a new instance of CompositeDisposable will be provided
     */
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("bootcamp-instagram-project-prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @TempDirectory
    fun provideTempDirectory() = FileUtils.getDirectory(application, "temp")

    /**
     * We need to write @Singleton on the provide method if we are create the instance inside this method
     * to make it singleton. Even if we have written @Singleton on the instance's class
     */
    @Provides
    @Singleton
    fun provideDatabaseService(): DatabaseService =
        Room.databaseBuilder(
            application, DatabaseService::class.java,
            "instagram-db"
        ).build()

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.API_KEY,
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelper(application)
}