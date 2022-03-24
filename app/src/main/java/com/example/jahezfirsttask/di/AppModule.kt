package com.example.jahezfirsttask.di

import com.example.jahezfirsttask.common.Constants
import com.example.jahezfirsttask.data.remote.IJahezRestaurantsApi
import com.example.jahezfirsttask.data.repository.AuthRepositoryImpl
import com.example.jahezfirsttask.data.repository.RestaurantRepositoryImpl
import com.example.jahezfirsttask.domain.repository.IAuthRepository
import com.example.jahezfirsttask.domain.repository.IRestaurantRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //This mean : all module dependency live as long as application does
object AppModule {

                   //-------------------------For firebase--------------------------------//
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseRepository(auth : FirebaseAuth): IAuthRepository {
        return AuthRepositoryImpl(auth)
    }


                //-------------------------For API---------------------------------------//

    @Provides
    @Singleton //This mean : make sure there is only single instance throw the whole time of our app
    fun provideJahezRestaurantsApi(): IJahezRestaurantsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IJahezRestaurantsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRestaurantRepository(api:IJahezRestaurantsApi): IRestaurantRepository {
        return RestaurantRepositoryImpl(api)
    }
}