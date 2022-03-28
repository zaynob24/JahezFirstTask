package com.example.jahezfirsttask.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.common.Resource
import com.example.jahezfirsttask.domain.useCase.authentication.IsUserAuthenticatedUseCase
import com.example.jahezfirsttask.domain.useCase.authentication.LoginUseCase
import com.example.jahezfirsttask.data.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signeInUseCase: LoginUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,

) : ViewModel() {


    //------------------------------------------authentication----------------------------------------------------//

    //check user authentication (if user already logged in or not)
    fun isUserAuthenticated()= isUserAuthenticatedUseCase.invoke()

     //------------------------------------------FOR LOGIN----------------------------------------------------//


    //shared Flow
    private val _sharedFlow = MutableSharedFlow<AuthenticationState>()
    val sharedFlow = _sharedFlow.asSharedFlow()


    //Login
    fun login(email: String, password: String) {

        signeInUseCase(email, password).onEach { result ->

            when (result) {

                is Resource.Success -> {

                    _sharedFlow.emit(AuthenticationState(isSuccess = true))
                }

                is Resource.Error -> {
                    _sharedFlow.emit( AuthenticationState(error = result.message ?: "An unaccepted error accrue"))

                }

                is Resource.Loading -> {
                    _sharedFlow.emit( AuthenticationState(isLoading = true))

                }
            }

        }.launchIn(viewModelScope)
    }
}