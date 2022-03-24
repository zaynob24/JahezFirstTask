package com.example.jahezfirsttask.presntation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.common.Resource
import com.example.jahezfirsttask.domain.use_case.SigneInWithEmailPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signeInUseCase: SigneInWithEmailPassword
) : ViewModel() {


    //state Flow
    private val _stateFlow = MutableStateFlow(LoginState())
    val stateFlow = _stateFlow.asStateFlow()


     fun login(email: String, password: String) {

        signeInUseCase(email, password).onEach { result ->

            when (result) {

                is Resource.Success -> {

                    _stateFlow.value = LoginState(isLogin = true)
                }

                is Resource.Error -> {

                    _stateFlow.value =
                        LoginState(error = result.message ?: "An unaccepted error accrue")
                }

                is Resource.Loading -> {

                    _stateFlow.value = LoginState(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)
    }
}