package com.example.jahezfirsttask.data.remote.dto

import com.example.jahezfirsttask.domain.model.Restaurant

data class RestaurantDto(
    val description: String,
    val distance: Double,
    val hasOffer: Boolean,
    val hours: String,
    val id: Int,
    val image: String,
    val name: String,
    val offer: String,
    val rating: Double
)



// For transfer all RestaurantDto data class to just the variable we need it in Restaurant class
// RestaurantDto have all data come from api , Restaurant class for just the data we need

fun RestaurantDto.toRestaurant() : Restaurant {
    return Restaurant(
        id= id ,
        distance= distance,
        name= name ,
        hasOffer= hasOffer ,
        hours= hours,
        image= image,
        rating= rating
    )
}