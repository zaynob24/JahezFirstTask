package com.example.jahezfirsttask.data.repository

import com.example.jahezfirsttask.domain.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth
):AuthRepository {
    override suspend fun firebaseLogin(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email,password).await()
    }

    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

}