package com.example.jahezfirsttask.domain.repository

import com.example.jahezfirsttask.data.remote.dto.RestaurantDto


interface IRestaurantRepository {

    suspend fun getRestaurant() : List<RestaurantDto>
}