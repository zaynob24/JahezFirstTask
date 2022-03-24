package com.example.jahezfirsttask.domain.use_case

import com.example.jahezfirsttask.common.Resource
import com.example.jahezfirsttask.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SigneInWithEmailPassword @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(email: String, password: String) : Flow<Resource<Boolean>> = flow {

        try {
            // here we emit loading so in ui we show progress bar
            emit(Resource.Loading())
            repository.firebaseLogin(email, password)
            emit(Resource.Success(true))

        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage?:"Un  unexpected error occurred"))

        }catch (e: HttpException){

            emit(Resource.Error(e.localizedMessage?:"Un  unexpected error occurred"))
        }catch (e: IOException){
            emit(Resource.Error("couldn't reach server , check your internet connection") )
        }
    }


}