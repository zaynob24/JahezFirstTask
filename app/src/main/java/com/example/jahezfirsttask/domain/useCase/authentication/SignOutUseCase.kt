package com.example.jahezfirsttask.domain.useCase.authentication

import com.example.jahezfirsttask.domain.repository.IAuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: IAuthRepository
)  {
    operator fun invoke() = repository.signOut()
}