package com.example.jahezfirsttask.data.repository

import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.repository.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth
):IAuthRepository {

    //Login
    override suspend fun firebaseLogin(email: String, password: String): Flow<Result<Boolean>> = flow {

        try {
            // here we emit loading so in ui we show progress bar
            emit(Result.Loading())

            auth.signInWithEmailAndPassword(email,password).await()
            emit(Result.Success(true))

        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage?:"Un  unexpected error occurred"))

        }catch (e: HttpException){

            emit(Result.Error(e.localizedMessage?:"Un  unexpected error occurred"))
        }catch (e: IOException){
            emit(Result.Error("couldn't reach server , check your internet connection") )
        }
    }

    // Register user in firebase
    override suspend fun firebaseRegister(email: String, password: String): Flow<Result<Boolean>> = flow {

        try {
            // here we emit loading so in ui we show progress bar
            emit(Result.Loading())
            auth.createUserWithEmailAndPassword(email,password).await()
            emit(Result.Success(true))

        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage?:"Un  unexpected error occurred"))

        }catch (e: HttpException){

            emit(Result.Error(e.localizedMessage?:"Un  unexpected error occurred"))
        }catch (e: IOException){
            emit(Result.Error("couldn't reach server , check your internet connection") )
        }
    }
    // check if user already logged in (user login state)
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    // Sign out
    override fun signOut() {
        auth.signOut()
    }
}