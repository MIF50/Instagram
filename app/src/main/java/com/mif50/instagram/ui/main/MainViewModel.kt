package com.mif50.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.mif50.instagram.ui.base.BaseViewModel
import com.mif50.instagram.utils.common.Event
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainViewModel (
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {


    val profileNavigation = MutableLiveData<Event<Boolean>>()
    val homeNavigation = MutableLiveData<Event<Boolean>>()
    val photoNavigation = MutableLiveData<Event<Boolean>>()


    override fun onCreate() {
        homeNavigation.postValue(Event(true))
    }

    fun onProfileTapped() {
        profileNavigation.postValue(Event(true))
    }

    fun onHomeTapped() {
        homeNavigation.postValue(Event(true))
    }

    fun onPhotoTapped() {
        photoNavigation.postValue(Event(true))
    }
}