package com.example.jahezfirsttask.presentation.login


import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.base.BaseViewModel
import com.example.jahezfirsttask.common.Constants.EMPTY_EMAIL
import com.example.jahezfirsttask.common.Constants.EMPTY_PASSWORD
import com.example.jahezfirsttask.common.Constants.INVALID_EMAIL
import com.example.jahezfirsttask.common.Constants.VALID_INPUTS
import com.example.jahezfirsttask.domain.useCase.authentication.IsUserAuthenticatedUseCase
import com.example.jahezfirsttask.domain.useCase.authentication.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.state.BaseUIState
import com.example.jahezfirsttask.presentation.util.Validations
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signeInUseCase: LoginUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,

) : BaseViewModel() {


    //authentication
    //check user authentication (if user already logged in or not)
    fun isUserAuthenticated()= isUserAuthenticatedUseCase.invoke()

    //shared flow for login
    private val _loginSharedFlow = MutableSharedFlow<Boolean>()
    val loginSharedFlow = _loginSharedFlow.asSharedFlow()


    //shared flow for Check the input fields validity
    private val _inputState = MutableSharedFlow<List<Int>>()
    val inputState = _inputState.asSharedFlow()


    //Login
    fun login(email: String, password: String) {

        viewModelScope.launch {

            signeInUseCase(email, password).onEach { result ->

                when (result) {

                    is Result.Success -> {
                        _loginSharedFlow.emit(true)
                        _baseUIState.emit(BaseUIState())
                    }

                    is Result.Error -> {
                        _baseUIState.emit(BaseUIState(error = result.message ?: "An unaccepted error accrue"))

                    }
                    is Result.Loading -> {
                        _baseUIState.emit(BaseUIState(isLoading = true))

                    }
                }

            }.launchIn(viewModelScope)


        }
    }

    //check if all field contain Valid data and give error massage if not
    fun checkDataValidity(email: String, password: String) {

        viewModelScope.launch {

            var isAllDataValid = true

            val inputStates = mutableListOf<Int>()

            //check email
            if (email.isBlank()) {
                inputStates.add(EMPTY_EMAIL)
                isAllDataValid = false
            } else if (!Validations.emailIsValid(email)) {
                inputStates.add(INVALID_EMAIL)
                isAllDataValid = false
            }

            //check password
            if (password.isBlank()) {
                isAllDataValid = false
                inputStates.add(EMPTY_PASSWORD)
            }
            _inputState.emit(inputStates)

            if (isAllDataValid)
                _inputState.emit(listOf(VALID_INPUTS))
        }
    }


}