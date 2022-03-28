package com.example.jahezfirsttask.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.useCase.authentication.RegisterUseCase
import com.example.jahezfirsttask.data.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,

) : ViewModel() {


    //----------------------------------------------------------------------------------------------//

    //state Flow
    private val _stateFlow = MutableStateFlow(AuthenticationState())
    val stateFlow = _stateFlow.asStateFlow()

    //Register
    fun Register(email: String, password: String) {

        registerUseCase(email, password).onEach { result ->

            when (result) {

                is Result.Success -> {

                    _stateFlow.value = AuthenticationState(isSuccess = true)
                }

                is Result.Error -> {

                    _stateFlow.value =
                        AuthenticationState(error = result.message ?: "An unaccepted error accrue")
                }

                is Result.Loading -> {

                    _stateFlow.value = AuthenticationState(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)
    }
}