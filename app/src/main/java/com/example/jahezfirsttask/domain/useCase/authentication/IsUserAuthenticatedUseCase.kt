package com.example.jahezfirsttask.domain.useCase.authentication

import com.example.jahezfirsttask.domain.repository.IAuthRepository
import javax.inject.Inject

class IsUserAuthenticatedUseCase @Inject constructor(
    private val repository: IAuthRepository
)  {
    operator fun invoke() = repository.isUserAuthenticatedInFirebase()
}