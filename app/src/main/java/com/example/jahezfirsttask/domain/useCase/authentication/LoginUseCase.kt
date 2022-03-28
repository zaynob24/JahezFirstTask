package com.example.jahezfirsttask.domain.useCase.authentication

import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: IAuthRepository
) {

    operator fun invoke(email: String, password: String) : Flow<Result<Boolean>> = flow {

        try {
            // here we emit loading so in ui we show progress bar
            emit(Result.Loading())
            repository.firebaseLogin(email, password)
            emit(Result.Success(true))

        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage?:"Un  unexpected error occurred"))

        }catch (e: HttpException){

            emit(Result.Error(e.localizedMessage?:"Un  unexpected error occurred"))
        }catch (e: IOException){
            emit(Result.Error("couldn't reach server , check your internet connection") )
        }
    }


}