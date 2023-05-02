package com.example.weatherforecast.data.local

import androidx.room.*

@Dao
interface WeatherDao {
    @Insert
    suspend fun saveWeather(vararg weather: WeatherEntity)

    @Query(value = "Select * from WeatherEntity")
    suspend fun getAllWeather() : List<WeatherEntity>

}