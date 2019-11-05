package com.mif50.instagram.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mif50.instagram.data.repository.DummyRepository
import com.mif50.instagram.data.repository.PostRepository
import com.mif50.instagram.data.repository.UserRepository
import com.mif50.instagram.ui.base.BaseFragment
import com.mif50.instagram.ui.dummies.DummiesAdapter
import com.mif50.instagram.ui.dummies.DummiesViewModel
import com.mif50.instagram.ui.dummies.DummyItemViewModel
import com.mif50.instagram.ui.home.HomeViewModel
import com.mif50.instagram.ui.home.posts.PostsAdapter
import com.mif50.instagram.ui.photo.PhotoViewModel
import com.mif50.instagram.ui.profile.ProfileViewModel
import com.mif50.instagram.utils.ViewModelProviderFactory
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideDummiesViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        dummyRepository: DummyRepository
    ): DummiesViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(DummiesViewModel::class) {
                DummiesViewModel(schedulerProvider, compositeDisposable, networkHelper, dummyRepository)
            }
        ).get(DummiesViewModel::class.java)

    @Provides
    fun provideDummiesAdapter() = DummiesAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun providePostsAdapter() = PostsAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideHomeViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postRepository: PostRepository
    ): HomeViewModel = ViewModelProviders.of(
        fragment, ViewModelProviderFactory(HomeViewModel::class) {
            HomeViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                userRepository,
                postRepository,
                ArrayList(),
                PublishProcessor.create()

            )
        }).get(HomeViewModel::class.java)

    @Provides
    fun provideProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): ProfileViewModel = ViewModelProviders.of(
        fragment, ViewModelProviderFactory(ProfileViewModel::class) {
            ProfileViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(ProfileViewModel::class.java)
//
    @Provides
    fun providePhotoViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): PhotoViewModel = ViewModelProviders.of(
        fragment, ViewModelProviderFactory(PhotoViewModel::class) {
            PhotoViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(PhotoViewModel::class.java)
}