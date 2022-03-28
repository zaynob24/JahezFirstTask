package com.example.jahezfirsttask.domain.repository

import com.google.firebase.auth.AuthResult

interface IAuthRepository {

    // Login
    suspend fun firebaseLogin(email: String, password: String): AuthResult

    // Register user in firebase
    suspend fun firebaseRegister(email: String, password: String) : AuthResult

    // check if user already logged in (user login state)
    fun isUserAuthenticatedInFirebase(): Boolean

    // Sign out
    fun signOut()
}