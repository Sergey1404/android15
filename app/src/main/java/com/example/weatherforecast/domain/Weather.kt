package com.example.weatherforecast.domain

data class Weather(
    val temperature: Double,
    val pressure: Int,
    val dtTxt: String,
    val iconURL: String
)
