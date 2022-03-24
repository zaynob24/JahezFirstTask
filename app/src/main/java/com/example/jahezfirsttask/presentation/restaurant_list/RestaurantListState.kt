package com.example.jahezfirsttask.presentation.restaurant_list

import com.example.jahezfirsttask.domain.model.Restaurant

data class RestaurantListState (
    val isLoading : Boolean = false,
    val restaurant: List<Restaurant> = emptyList(),
    val error : String = ""
)
