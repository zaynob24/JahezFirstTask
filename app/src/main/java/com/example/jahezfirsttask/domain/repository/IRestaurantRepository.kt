package com.example.jahezfirsttask.domain.repository

import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface IRestaurantRepository {

    suspend fun getRestaurant() : Flow<Result<List<Restaurant>>>

}