package com.example.jahezfirsttask.data.repository

import com.example.jahezfirsttask.data.remote.IJahezRestaurantsApi
import com.example.jahezfirsttask.data.remote.dto.RestaurantDto
import com.example.jahezfirsttask.domain.repository.IRestaurantRepository
import javax.inject.Inject


class RestaurantRepositoryImpl @Inject constructor(
    private val api : IJahezRestaurantsApi
) :IRestaurantRepository {

    override suspend fun getRestaurant(): List<RestaurantDto> {
        return api.getRestaurants()
    }
}