package com.example.jahezfirsttask.presentation.authentication

data class AuthenticationState(

    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val error : String = ""

)
