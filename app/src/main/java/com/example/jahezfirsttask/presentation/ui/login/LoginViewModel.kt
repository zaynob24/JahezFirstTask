package com.example.jahezfirsttask.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.common.Resource
import com.example.jahezfirsttask.domain.use_case.authentication.IsUserAuthenticatedUseCase
import com.example.jahezfirsttask.domain.use_case.authentication.LoginUseCase
import com.example.jahezfirsttask.presentation.authentication.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.Interceptor.Companion.invoke
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signeInUseCase: LoginUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase

) : ViewModel() {

    //check user authentication (if user already logged in or not)

    fun isUserAuthenticated()= isUserAuthenticatedUseCase.invoke()

     //----------------------------------------------------------------------------------------------//

    //state Flow
    private val _stateFlow = MutableStateFlow(AuthenticationState())
    val stateFlow = _stateFlow.asStateFlow()

    //Login
     fun login(email: String, password: String) {

        signeInUseCase(email, password).onEach { result ->

            when (result) {

                is Resource.Success -> {

                    _stateFlow.value = AuthenticationState(isSuccess = true)
                }

                is Resource.Error -> {

                    _stateFlow.value =
                        AuthenticationState(error = result.message ?: "An unaccepted error accrue")
                }

                is Resource.Loading -> {

                    _stateFlow.value = AuthenticationState(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)
    }
}