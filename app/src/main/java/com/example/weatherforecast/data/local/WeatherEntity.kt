package com.example.weatherforecast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val temperature: Double,
    val pressure: Int,
    val dtTxt: String,
    val iconURL: String
)
