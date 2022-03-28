package com.example.jahezfirsttask.data.remote

import com.example.jahezfirsttask.data.remote.dto.RestaurantDto
import retrofit2.Response
import retrofit2.http.GET

interface IJahezRestaurantsApi {

    @GET("/restaurants.json")
    suspend fun getRestaurants() : Response<List<RestaurantDto>>

}