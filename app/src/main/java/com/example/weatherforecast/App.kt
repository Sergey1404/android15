package com.example.weatherforecast

import android.app.Application
import com.example.weatherforecast.data.WeatherRepositoryImpl
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.network.WeatherAPI
import com.example.weatherforecast.domain.WeatherRepository

class App : Application() {
    override fun onCreate() {
        val dataBase = WeatherDataBase.getDatabase(this)
        val api = WeatherAPI.createAPI()
        repository = WeatherRepositoryImpl(dataBase, api)
        super.onCreate()
    }

    companion object {
        lateinit var repository: WeatherRepository
            private set
    }

}