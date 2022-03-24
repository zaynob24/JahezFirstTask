package com.example.jahezfirsttask.domain.use_case.authentication

import com.example.jahezfirsttask.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserAuthenticated @Inject constructor(
    private val repository: AuthRepository
)  {
    //operator fun invoke() = repository.isUserAuthenticatedInFirebase()
     val isUserAuthenticated = repository.isUserAuthenticatedInFirebase()

}