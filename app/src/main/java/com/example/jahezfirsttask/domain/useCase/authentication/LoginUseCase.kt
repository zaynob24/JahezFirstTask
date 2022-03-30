package com.example.jahezfirsttask.domain.useCase.authentication

import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.model.Restaurant
import com.example.jahezfirsttask.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.PasswordAuthentication
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(email :String,password:String): Flow<Result<Boolean>> = repository.firebaseLogin(email, password)
}