package com.example.jahezfirsttask.domain.model

data class Restaurant (
    val distance: Double,
    val hasOffer: Boolean,
    val hours: String,
    val id: Int,
    val image: String,
    val name: String,
    val rating: Double
)