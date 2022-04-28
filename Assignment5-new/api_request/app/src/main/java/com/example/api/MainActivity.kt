package com.example.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.api.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        updateWeather("Nur-Sultan")

        binding.getWeatherBtn.setOnClickListener {
            updateWeather(binding.searchCityText.text.toString())
        }

    }

    private fun updateWeather(city: String) {
        lifecycleScope.launch {
            val weather = OpenWeatherApi.retrofitService.getWeather(city = city)
            binding.cityNameText.text = weather.name
            binding.temperatureText.text = weather.main.temp.toInt().toString() + "°"
            binding.mainText.text = weather.weather[0].main
            binding.searchCityText.text.clear()
        }
    }
}