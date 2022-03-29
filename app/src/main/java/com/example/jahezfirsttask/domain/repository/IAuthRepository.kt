package com.example.jahezfirsttask.domain.repository

import com.example.jahezfirsttask.common.Result
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {

    // Login
    suspend fun firebaseLogin(email: String, password: String): Flow<Result<Boolean>>

    // Register user in firebase
    suspend fun firebaseRegister(email: String, password: String) : AuthResult

    // check if user already logged in (user login state)
    fun isUserAuthenticatedInFirebase(): Boolean

    // Sign out
    fun signOut()
}