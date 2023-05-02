package com.example.weatherforecast.data

import android.util.Log
import com.example.weatherforecast.Constants
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.network.WeatherAPI
import com.example.weatherforecast.domain.Weather
import com.example.weatherforecast.domain.WeatherData
import com.example.weatherforecast.domain.WeatherRepository
import kotlin.properties.Delegates
sealed interface WhereGetWeatherData {
    data class FromNetWork(val weather: WeatherData) : WhereGetWeatherData
    data class FromDataBase(val weather: WeatherData): WhereGetWeatherData
}

class WeatherRepositoryImpl(
    private val dataBase: WeatherDataBase,
    private val weatherAPI: WeatherAPI
) : WeatherRepository {

    override suspend fun loadWeather(): WhereGetWeatherData {
        try {
            val response = weatherAPI.getForecast(
                Constants.API_CITY,
                Constants.API_KEY,
                Constants.API_UNITS,
                Constants.API_LANG
            )

            val weather = response.body()?.list?.map {
                it.toDomain()
            }

            val cachedWeather = dataBase.weatherDao().getAllWeather()

            weather?.forEach {
                val date = it.dtTxt.split(" ")[0]

                val isDatabaseContainsThisWeather =
                    cachedWeather.any { it.dtTxt.split(" ")[0] == date }

                if (!isDatabaseContainsThisWeather) {
                    dataBase.weatherDao().saveWeather(it.toEntity())
                }
            }
            return WhereGetWeatherData.FromNetWork(WeatherData(weather.orEmpty()))
        } catch (e: java.lang.Exception) {
            Log.e("aaa", "Exception ${e.message}")
            val weather = dataBase.weatherDao().getAllWeather().map { it.toDomain() }
            return WhereGetWeatherData.FromDataBase(WeatherData(weather))
        }
    }
}