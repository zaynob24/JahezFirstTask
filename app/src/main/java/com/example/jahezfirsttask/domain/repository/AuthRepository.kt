package com.example.jahezfirsttask.domain.repository

import com.example.jahezfirsttask.common.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface AuthRepository {

    // Login
    suspend fun firebaseLogin(email: String, password: String): AuthResult

    // check if user already logged in (user login state)
    fun isUserAuthenticatedInFirebase(): Boolean
}