package com.example.weatherforecast

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import com.example.weatherforecast.data.WeatherRepositoryImpl
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.network.WeatherAPI
import com.example.weatherforecast.domain.WeatherRepository
import com.example.weatherforecast.ui.MainViewModel

class MainViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("unknown view model class")
    }

}