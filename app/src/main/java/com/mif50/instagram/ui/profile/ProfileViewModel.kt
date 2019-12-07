package com.mif50.instagram.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mif50.instagram.data.repository.UserRepository
import com.mif50.instagram.ui.base.BaseViewModel
import com.mif50.instagram.utils.common.Event
import com.mif50.instagram.utils.log.Logger
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel (
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
): BaseViewModel(schedulerProvider,compositeDisposable,networkHelper) {

    private companion object{
        private const val TAG = "ProfileViewModel"
    }

    val launchEditProfileActivity: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchLoginScreen : MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    val loggingOut : MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate() {}

    fun onEditProfileTapped() {
        launchEditProfileActivity.postValue(Event(emptyMap()))
    }

    fun onLogout(){
        if (checkInternetConnectionWithMessage()) {
            loggingOut.postValue(true)
            val user = userRepository.getCurrentUser()!!
            compositeDisposable.add(
                userRepository.doUserLogout(user)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        Logger.d(TAG,it.toString())
                        if (it.status == 200) {
                            userRepository.removeCurrentUser()
                            loggingOut.postValue(false)
                            launchLoginScreen.postValue(Event(emptyMap()))
                        } else {
                            Logger.d(TAG,it.message)
                        }

                    }, {
                        handleNetworkError(it)
                        loggingOut.postValue(false)
                    })
            )

        }

    }
}