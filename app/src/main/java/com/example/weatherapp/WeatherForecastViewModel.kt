package com.example.weatherapp

import WeatherDataForecast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherForecastViewModel : ViewModel() {

    private val _weatherForecast = MutableLiveData<List<WeatherDataForecast>>()
    val weatherForecast: LiveData<List<WeatherDataForecast>>
        get() = _weatherForecast

    fun fetchWeatherForecast(city: String, unit: String) {
        val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        val apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=$unit"

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    URL(apiUrl).readText()
                }
                handleResponse(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleResponse(response: String) {
        try {
            val jsonObj = JSONObject(response)
            val list = jsonObj.getJSONArray("list")

            val weatherDataList = mutableListOf<WeatherDataForecast>()

            for (i in 0 until list.length()) {
                val forecastObj = list.getJSONObject(i)
                val dateTimeString = forecastObj.getString("dt_txt")

                if (dateTimeString.endsWith("12:00:00")) {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val date = inputFormat.parse(dateTimeString)

                    if (date != null) {
                        val mainObj = forecastObj.getJSONObject("main")
                        val weatherArray = forecastObj.getJSONArray("weather")
                        val weatherObj = weatherArray.getJSONObject(0)

                        val temperature = mainObj.getInt("temp")
                        val feelsLike = mainObj.getDouble("feels_like")
                        val pressure = mainObj.getInt("pressure")
                        val humidity = mainObj.getInt("humidity")
                        val description = weatherObj.getString("description")
                        val icon = weatherObj.getString("icon")
                        val iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png"

                        val weatherData = WeatherDataForecast(date, temperature, feelsLike, pressure, humidity, description, iconUrl)
                        weatherDataList.add(weatherData)
                    }
                }
            }

            _weatherForecast.postValue(weatherDataList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
