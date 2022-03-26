package com.example.jahezfirsttask.data.repository

import com.example.jahezfirsttask.domain.repository.IAuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth
):IAuthRepository {

    //Login
    override suspend fun firebaseLogin(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email,password).await()
    }

    // Register user in firebase
    override suspend fun firebaseRegister(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email,password).await()
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