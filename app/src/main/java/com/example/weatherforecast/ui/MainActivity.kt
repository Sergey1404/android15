package com.example.weatherforecast.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.*
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.domain.Weather
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private val weatherAdapter = WeatherAdapter(
        onLongClick = {
            Log.d("aaa", "$it")
            shareWeather(it)
        }
    )
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycleViewInit()

        viewModelFactory = MainViewModelFactory(App.repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.weatherList.observe(this) {
            weatherAdapter.submitList(it)
        }
        showSnackBar(binding.root)
    }

    private fun showSnackBar(view: View) {
        viewModel.isFromDataBase.observe(this) {
            it.getContentIfNotHandled()?.let {
                val mySnackbar = Snackbar.make(view, "Данные загружены", Snackbar.LENGTH_LONG)
                mySnackbar.show()
            }
        }
    }

    private fun recycleViewInit() {
        binding.rvWeather.adapter = weatherAdapter
        binding.rvWeather.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun shareWeather(weather: Weather) {
        val sendIntent = Intent().apply {
            val weatherTimeDate = weather.dtTxt.split(" ")

            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "${Constants.API_CITY} ${weather.dtTxt} ${weather.temperature}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Send text")
        startActivity(shareIntent)
    }
}
