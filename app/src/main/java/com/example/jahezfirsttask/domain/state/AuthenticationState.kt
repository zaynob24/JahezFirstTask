package com.example.jahezfirsttask.domain.state

data class AuthenticationState(

    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val error : String = ""

)
