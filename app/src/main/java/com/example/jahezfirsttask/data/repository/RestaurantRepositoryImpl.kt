package com.example.jahezfirsttask.data.repository

import android.util.Log
import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.data.remote.IJahezRestaurantsApi
import com.example.jahezfirsttask.data.remote.dto.toRestaurant
import com.example.jahezfirsttask.domain.model.Restaurant
import com.example.jahezfirsttask.domain.repository.IRestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "RestaurantRepositoryImp"
class RestaurantRepositoryImpl @Inject constructor(
    private val api : IJahezRestaurantsApi
) :IRestaurantRepository {

    override suspend fun getRestaurant(): Flow<Result<List<Restaurant>>> = flow {

        Log.d(TAG,"getRestaurant()")
        try {
            // here we emit loading so in ui we show progress bar
            emit(Result.Loading())


            val restaurantResponse = api.getRestaurants()

            if( restaurantResponse.isSuccessful){

                restaurantResponse.body()?.let {  restaurantDto ->
                    emit(Result.Success(restaurantDto.map { it.toRestaurant() }))

                    Log.d(TAG,restaurantDto.map { it.toRestaurant() }.toString())
                }

            }else{
                emit(Result.Error(restaurantResponse.message()?:"Un  unexpected error occurred"))
                Log.d(TAG,restaurantResponse.message()?:"Un  unexpected error occurred")

            }

        }catch (e: HttpException){

            emit(Result.Error(e.localizedMessage?:"Un  unexpected error occurred"))
            Log.d(TAG,e.localizedMessage?:"Un  unexpected error occurred")

        }catch (e: IOException){
            emit(Result.Error("couldn't reach server , check your internet connection") )
            Log.d(TAG,"couldn't reach server , check your internet connection")

        }
    }

}