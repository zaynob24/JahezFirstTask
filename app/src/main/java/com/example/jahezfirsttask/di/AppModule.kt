package com.example.jahezfirsttask.di

import com.example.jahezfirsttask.data.repository.AuthRepositoryImpl
import com.example.jahezfirsttask.domain.repository.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //This mean : all module dependency live as long as application does
object AppModule {


    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseRepository(auth : FirebaseAuth): IAuthRepository {
        return AuthRepositoryImpl(auth)
    }
}