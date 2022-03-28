package com.example.jahezfirsttask.domain.useCase.restaurantList

import com.example.jahezfirsttask.common.Resource
import com.example.jahezfirsttask.data.remote.dto.toRestaurant
import com.example.jahezfirsttask.domain.model.Restaurant
import com.example.jahezfirsttask.domain.repository.IRestaurantRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor(
    private val repository: IRestaurantRepository
) {

        operator fun invoke() : Flow<Resource<List<Restaurant>>> = flow {
        try {
            // here we emit loading so in ui we show progress bar
            emit(Resource.Loading())
            val restaurant = repository.getRestaurant().map { it.toRestaurant() }
            emit(Resource.Success(restaurant))
        }catch (e: HttpException){

            emit(Resource.Error(e.localizedMessage?:"Un  unexpected error occurred"))
        }catch (e: IOException){
            emit(Resource.Error("couldn't reach server , check your internet connection") )
        }
    }
}