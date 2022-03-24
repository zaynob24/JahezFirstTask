package com.example.jahezfirsttask.domain.use_case.authentication

import com.example.jahezfirsttask.domain.repository.IAuthRepository
import javax.inject.Inject

class IsUserAuthenticated @Inject constructor(
    private val repositoryI: IAuthRepository
)  {
    //operator fun invoke() = repository.isUserAuthenticatedInFirebase()
     val isUserAuthenticated = repositoryI.isUserAuthenticatedInFirebase()

}