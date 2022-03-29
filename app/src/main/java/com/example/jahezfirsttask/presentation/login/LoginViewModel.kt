package com.example.jahezfirsttask.presentation.login


import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.domain.useCase.authentication.IsUserAuthenticatedUseCase
import com.example.jahezfirsttask.domain.useCase.authentication.LoginUseCase
import com.example.jahezfirsttask.domain.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import com.example.jahezfirsttask.common.Result
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signeInUseCase: LoginUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,

) : ViewModel() {

    //ObservableInt() -> binding data with setting a value from string resource
    val emailErrorMassage = ObservableInt()
    val passwordErrorMassage = ObservableInt()


    //init Error Massage with empty string
    init {
        emailErrorMassage.set(R.string.empty_massage)
        passwordErrorMassage.set(R.string.empty_massage)
    }

    //authentication
    //check user authentication (if user already logged in or not)
    fun isUserAuthenticated()= isUserAuthenticatedUseCase.invoke()

    //shared flow for login
    private val _loginSharedFlow = MutableSharedFlow<AuthenticationState>()
    val loginSharedFlow = _loginSharedFlow.asSharedFlow()


    //Login
    fun login(email: String, password: String) {

        signeInUseCase(email, password).onEach { result ->

            when (result) {

                is Result.Success -> {

                    _loginSharedFlow.emit(AuthenticationState(isSuccess = true))
                }

                is Result.Error -> {
                    _loginSharedFlow.emit( AuthenticationState(error = result.message ?: "An unaccepted error accrue"))

                }

                is Result.Loading -> {
                    _loginSharedFlow.emit( AuthenticationState(isLoading = true))

                }
            }

        }.launchIn(viewModelScope)
    }

    //check if all field contain data and give error massage if not
    fun checkDataValidity(email: String , password: String) : Boolean {
        var isAllDataFilled = true

        //check email
        if (email.isEmpty() || email.isBlank()) {

            emailErrorMassage.set(R.string.required)
            isAllDataFilled = false

        } else {
            emailErrorMassage.set(R.string.empty_massage)
        }

        //check password
        if (password.isEmpty() || password.isBlank()) {

            passwordErrorMassage.set(R.string.required)
            isAllDataFilled = false
        } else {

            passwordErrorMassage.set(R.string.empty_massage)
        }

        return isAllDataFilled
    }


}