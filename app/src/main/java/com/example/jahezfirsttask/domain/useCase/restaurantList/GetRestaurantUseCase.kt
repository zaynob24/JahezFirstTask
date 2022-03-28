package com.example.jahezfirsttask.domain.useCase.restaurantList

import com.example.jahezfirsttask.common.Result
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

     suspend operator fun invoke():  Flow<Result<List<Restaurant>>> = repository.getRestaurant()

}