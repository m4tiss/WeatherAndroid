package com.example.weatherapp

import NetworkConnection
import WeatherDataForecast
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherForecastViewModel(application: Application) : ViewModel() {

    private val _weatherForecast = MutableLiveData<List<WeatherDataForecast>>()
    val weatherForecast: LiveData<List<WeatherDataForecast>>
        get() = _weatherForecast

    @SuppressLint("StaticFieldLeak")
    private val context: Context

    init {
        context  = application.applicationContext
    }

    fun fetchWeatherForecast(city: String, unit: String) {
        val networkConnection = NetworkConnection(context)
        if (networkConnection.isNetworkAvailable()) {
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
                    _weatherForecast.postValue(emptyList())
                }
            }
        } else {
            _weatherForecast.postValue(loadWeatherForecastFromFile(city))
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

                        val weatherData = WeatherDataForecast(date, temperature, feelsLike, pressure, humidity, description, icon)
                        weatherDataList.add(weatherData)
                    }
                }
            }

            _weatherForecast.postValue(weatherDataList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


     fun saveWeatherForecastToFile(city: String) {
        val fileName = "${city}_forecast.json"
        val weatherDataList = _weatherForecast.value ?: return
        val jsonArray = mutableListOf<JSONObject>()

        for (weatherData in weatherDataList) {
            val jsonObject = JSONObject().apply {
                put("dateTime", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(weatherData.dateTime))
                put("temperature", weatherData.temperature)
                put("feelsLike", weatherData.feelsLike)
                put("pressure", weatherData.pressure)
                put("humidity", weatherData.humidity)
                put("description", weatherData.description)
                put("icon", weatherData.icon)
            }
            jsonArray.add(jsonObject)
        }

        val fileDir = context.filesDir
        val file = File(fileDir, fileName)
        try {
            FileWriter(file).use { fileWriter ->
                fileWriter.write(jsonArray.toString())
                println("Weather forecast data saved to file: ${file.absolutePath}")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun deleteWeatherForecastFile(city: String) {
        val fileName = "${city}_forecast.json"
        val fileDir = context.filesDir
        val file = File(fileDir, fileName)
        if (file.exists()) {
            try {
                file.delete()
                println("Plik z danymi pogodowymi dla miasta $city został usunięty.")
            } catch (e: SecurityException) {
                e.printStackTrace()
                println("Błąd podczas usuwania pliku z danymi pogodowymi dla miasta $city: ${e.message}")
            }
        } else {
            println("Plik z danymi pogodowymi dla miasta $city nie istnieje.")
        }
    }
     private fun loadWeatherForecastFromFile(city: String): List<WeatherDataForecast> {
        val fileName = "${city}_forecast.json"
        println(city)
        val file = File(context.filesDir, fileName)
        println("Jest taki plik")
        if (file.exists()) {
            try {
                val jsonString = file.readText()
                val jsonArray = JSONArray(jsonString)

                val weatherDataList = mutableListOf<WeatherDataForecast>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val dateTimeString = jsonObject.getString("dateTime")
                    val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateTimeString)
                    val temperature = jsonObject.getInt("temperature")
                    val feelsLike = jsonObject.getDouble("feelsLike")
                    val pressure = jsonObject.getInt("pressure")
                    val humidity = jsonObject.getInt("humidity")
                    val description = jsonObject.getString("description")
                    val icon = jsonObject.getString("icon")
                    val weatherData = WeatherDataForecast(dateTime, temperature, feelsLike, pressure, humidity, description, icon)
                    weatherDataList.add(weatherData)
                }
                println(weatherDataList.size)
                return weatherDataList
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
         println("Zwracam pustą liste")
        return emptyList()
    }
}
