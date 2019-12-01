package com.mif50.instagram.ui.profile

import androidx.lifecycle.MutableLiveData
import com.mif50.instagram.ui.base.BaseViewModel
import com.mif50.instagram.utils.common.Event
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel (
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
): BaseViewModel(schedulerProvider,compositeDisposable,networkHelper) {

    val launchEditProfileActivity: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    override fun onCreate() {}

    fun onEditProfileTapped() {
        launchEditProfileActivity.postValue(Event(emptyMap()))
    }
}