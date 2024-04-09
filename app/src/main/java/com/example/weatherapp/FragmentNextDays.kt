package com.example.weatherapp

import CityViewModel
import UnitViewModel
import WeatherData
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class FragmentNextDays : Fragment() {

    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var unitViewModel: UnitViewModel
    private lateinit var cityViewModel: CityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nextdays, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val list: MutableList<WeatherData> = ArrayList()
        weatherAdapter = WeatherAdapter(list)
        recyclerView.adapter = weatherAdapter

        unitViewModel = ViewModelProvider(requireActivity()).get(UnitViewModel::class.java)
        cityViewModel = ViewModelProvider(requireActivity()).get(CityViewModel::class.java)

        cityViewModel.city.observe(viewLifecycleOwner, Observer { city ->
            fetchWeatherData(city)
        })
        unitViewModel.unit.observe(viewLifecycleOwner, Observer { unit ->
            fetchWeatherData(cityViewModel.city.value)
        })

        return view
    }

    private fun fetchWeatherData(city: String?) {
        city?.let {
            val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
            val apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=$it&appid=$apiKey&units=${unitViewModel.unit.value}"

            GlobalScope.launch(Dispatchers.Main) {
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
    }

    private fun handleResponse(response: String) {
        try {
            val jsonObj = JSONObject(response)
            val list = jsonObj.getJSONArray("list")

            val weatherDataList = mutableListOf<WeatherData>()

            for (i in 0 until list.length()) {
                val forecastObj = list.getJSONObject(i)
                val dateTime = forecastObj.getString("dt_txt")

                if (dateTime.contains("12:00:00")) {
                    val mainObj = forecastObj.getJSONObject("main")
                    val weatherArray = forecastObj.getJSONArray("weather")
                    val weatherObj = weatherArray.getJSONObject(0)

                    val temperature = mainObj.getDouble("temp").toInt()
                    val weatherIcon = weatherObj.getString("icon")

                    val iconBaseUrl = "https://openweathermap.org/img/wn/"
                    val weatherIconUrl = "${iconBaseUrl}${weatherIcon}@2x.png"

                    GlobalScope.launch(Dispatchers.IO) {
                        val iconBitmap = downloadBitmapFromUrl(weatherIconUrl)

                        withContext(Dispatchers.Main) {
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val outputFormat = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
                            val date = inputFormat.parse(dateTime)
                            val formattedDate = outputFormat.format(date)
                            val feelsLike = mainObj.getDouble("feels_like").toInt()
                            val pressure = mainObj.getInt("pressure")

                            val weatherData = WeatherData(temperature, iconBitmap, formattedDate, feelsLike, pressure)
                            weatherDataList.add(weatherData)

                            weatherAdapter.setData(weatherDataList)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun downloadBitmapFromUrl(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}
