package com.example.weatherforecast.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.App
import com.example.weatherforecast.Constants
import com.example.weatherforecast.Event
import com.example.weatherforecast.data.WeatherRepositoryImpl
import com.example.weatherforecast.data.WhereGetWeatherData
import com.example.weatherforecast.data.toDomain
import com.example.weatherforecast.data.toEntity
import com.example.weatherforecast.domain.Weather
import com.example.weatherforecast.domain.WeatherRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.properties.Delegates

class MainViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherList = MutableLiveData<List<Weather>>()
    val weatherList get() = _weatherList
    private val _isFromDataBase = MutableLiveData<Event<Unit>>()
    val isFromDataBase get() = _isFromDataBase

    init {
        loadWeather()
    }

    private fun loadWeather() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (val weather = repository.loadWeather()) {
                    is WhereGetWeatherData.FromDataBase -> {
                        loadFromDataBase(weather)
                    }
                    is WhereGetWeatherData.FromNetWork -> {
                        loadFromNetWork(weather)
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.e("Error", e.toString())
            }
        }
    }

    private fun loadFromDataBase(weather: WhereGetWeatherData.FromDataBase) {
        _weatherList.postValue(weather.weather.weatherList)
    }

    private fun loadFromNetWork(weather: WhereGetWeatherData.FromNetWork) {
        _weatherList.postValue(weather.weather.weatherList)
        _isFromDataBase.postValue(Event(Unit))
    }
}