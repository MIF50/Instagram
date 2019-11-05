package com.mif50.instagram.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mif50.instagram.data.repository.UserRepository
import com.mif50.instagram.ui.base.BaseViewModel
import com.mif50.instagram.utils.common.*
import com.mif50.instagram.utils.dsl.isNotNull
import com.mif50.instagram.utils.log.Logger.e
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignUpViewModel (

    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository

): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper){

    private val validationsList: MutableLiveData<List<Validation>> = MutableLiveData()

    val launchHome: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    val fullNameField: MutableLiveData<String> = MutableLiveData()
    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()
    val signingUp: MutableLiveData<Boolean> = MutableLiveData()

    val fullNameValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.NAME)
    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)

    private fun filterValidation(field: Validation.Field) =
        Transformations.map(validationsList) {
            it.find { validation -> validation.field == field }
                ?.run { return@run  this.resource }
                ?: Resource.unknown()
        }

    override fun onCreate() {}

    fun onFullNameChange(fullName: String) = fullNameField.postValue(fullName)

    fun onEmailChange(email: String) = emailField.postValue(email)

    fun onPasswordChange(password: String) = passwordField.postValue(password)

    fun onSignUp() {
        val fullName = fullNameField.value
        val email = emailField.value
        val password = passwordField.value

        val validations = Validator.validateSignUpFields(fullName, email, password)
        validationsList.postValue(validations)

        if (validations.isNotEmpty() && fullName.isNotNull() && email.isNotNull() && password.isNotNull()) {
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
            if (successValidation.count() == validations.count() && checkInternetConnectionWithMessage()) {
                e("MIF","email = $email, password = $password, fullName = $fullName")
                signingUp.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doUserSignUp(fullName!!,email!!,password!!)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                            {
                                userRepository.saveCurrentUser(it)
                                signingUp.postValue(false)
                                launchHome.postValue(Event(emptyMap()))

                            },{
                                handleNetworkError(it)
                                signingUp.postValue(false)
                            }
                        )
                )
            }
        }
    }

    fun onLoginTapped() = launchLogin.postValue(Event(emptyMap()))
}