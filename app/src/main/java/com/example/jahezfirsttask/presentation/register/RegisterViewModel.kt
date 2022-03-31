package com.example.jahezfirsttask.presentation.register

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.base.BaseViewModel
import com.example.jahezfirsttask.common.Constants.EMPTY_CONFIRM_PASSWORD
import com.example.jahezfirsttask.common.Constants.EMPTY_EMAIL
import com.example.jahezfirsttask.common.Constants.EMPTY_PASSWORD
import com.example.jahezfirsttask.common.Constants.INVALID_EMAIL
import com.example.jahezfirsttask.common.Constants.INVALID_PASSWORD
import com.example.jahezfirsttask.common.Constants.PASSWORDS_NOT_MATCH
import com.example.jahezfirsttask.common.Constants.VALID_INPUTS
import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.useCase.authentication.RegisterUseCase
import com.example.jahezfirsttask.domain.state.BaseUIState
import com.example.jahezfirsttask.presentation.util.Validations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RegisterViewModel"
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,

) : BaseViewModel() {


    //shared flow for register
    private val _registerSharedFlow = MutableSharedFlow<Boolean>()
    val registerSharedFlow = _registerSharedFlow.asSharedFlow()


    //shared flow for Check the input fields validity
    private val _inputState = MutableSharedFlow<List<Int>>()
    val inputState = _inputState.asSharedFlow()

    //Register]
    fun register(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            registerUseCase(email, password).onEach { result ->

                when (result) {

                    is Result.Success -> {

                        _registerSharedFlow.emit( true)
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

    //check if all field contain data and give error massage if not
    fun checkDataValidity(email: String , password: String,confirmPassword:String) {
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
            } else if (!Validations.passwordIsValid(password)) {
                isAllDataValid = false
                inputStates.add(INVALID_PASSWORD)
            }

            //check confirmPassword
            if (confirmPassword.isBlank()) {
                isAllDataValid = false
                inputStates.add(EMPTY_CONFIRM_PASSWORD)
            } else if (password != confirmPassword) {
                isAllDataValid = false
                inputStates.add(PASSWORDS_NOT_MATCH)
            }

            if (isAllDataValid) {
                inputStates.clear()
                inputStates.add(VALID_INPUTS)
            }
            Log.d(TAG, "ViewModelStateList: $inputStates")
            _inputState.emit(inputStates)
        }
    }
}