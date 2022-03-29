package com.example.jahezfirsttask.presentation.register

import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.useCase.authentication.RegisterUseCase
import com.example.jahezfirsttask.domain.state.AuthenticationState
import com.example.jahezfirsttask.presentation.util.Validations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "RegisterViewModel"
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,

) : ViewModel() {

    //ObservableInt() -> binding data with setting a value from string resource
    val emailErrorMassage = ObservableInt()
    val passwordErrorMassage = ObservableInt()
    val confirmPasswordErrorMassage = ObservableInt()

    private val validator = Validations()


    //init Error Massage with empty string
    init {
        emailErrorMassage.set(R.string.empty_massage)
        passwordErrorMassage.set(R.string.empty_massage)
        confirmPasswordErrorMassage.set(R.string.empty_massage)
    }

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

    //check if all field contain data and give error massage if not
    fun checkDataValidity(email: String , password: String,confirmPassword:String) : Boolean {
        var isAllDataFilled = true

        //check email
        if (email.isEmpty() || email.isBlank()) {

            emailErrorMassage.set(R.string.required)
            isAllDataFilled = false
        } else {
            //check email validate
            if (validator.emailIsValid(email)){
                emailErrorMassage.set(R.string.empty_massage)

            }else{

                isAllDataFilled = false
                emailErrorMassage.set(R.string.invalid_email)
                Log.d(TAG,"invalid_email")
            }
        }

        //check password
        if (password.isEmpty() || password.isBlank()) {
            passwordErrorMassage.set(R.string.required)
            isAllDataFilled = false

        } else{

            if(validator.passwordIsValid(password)){
                passwordErrorMassage.set(R.string.empty_massage)

            }else{
                //check password validate
                isAllDataFilled = false
                passwordErrorMassage.set(R.string.invalid_password_massage)
                Log.d(TAG,"invalid_password_massage")
            }

        }

        //check confirmPassword
        if (confirmPassword.isEmpty() || confirmPassword.isBlank()) {
            confirmPasswordErrorMassage.set(R.string.required)

            isAllDataFilled = false

        }else if(password != confirmPassword){
            confirmPasswordErrorMassage.set(R.string.password_not_match)

            isAllDataFilled = false
            Log.d(TAG,"password_not_match")

        }else {
            confirmPasswordErrorMassage.set(R.string.empty_massage)

        }

        return isAllDataFilled
    }
}