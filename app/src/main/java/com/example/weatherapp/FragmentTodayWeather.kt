package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class FragmentTodayWeather : Fragment() {

    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var coordinatesTextView: TextView
    private lateinit var weatherTextView: TextView
    private lateinit var pressureTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var searchButton: Button
    private lateinit var searchEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today_weather, container, false)
        temperatureTextView = view.findViewById(R.id.temperatureTextView)
        cityTextView = view.findViewById(R.id.cityTextView)
        coordinatesTextView = view.findViewById(R.id.coordinatesTextView)
        weatherTextView = view.findViewById(R.id.weatherTextView)
        pressureTextView = view.findViewById(R.id.pressureTextView)
        timeTextView = view.findViewById(R.id.timeTextView)


        searchButton = view.findViewById(R.id.searchButton)
        searchEditText = view.findViewById(R.id.searchEditText)

        val defaultCity = "Warsaw"
        val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        val defaultApiUrl = "https://api.openweathermap.org/data/2.5/weather?q=$defaultCity&appid=$apiKey"


        WeatherTask().execute(defaultApiUrl)

        searchButton.setOnClickListener {
            val city = searchEditText.text.toString()
            val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"
            WeatherTask().execute(apiUrl)
        }

        return view
    }

    inner class WeatherTask {
        fun execute(url: String) {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        URL(url).readText()
                    }
                    handleResponse(response)
                } catch (e: IOException) {
                    cityTextView.text = "-"
                    temperatureTextView.text = "-"
                    coordinatesTextView.text = "Coordinates: -"
                    weatherTextView.text = "Weather: -"
                    pressureTextView.text = "Pressure: -"
                    timeTextView.text = "Local Time: -"
                }
            }
        }

        private fun handleResponse(response: String) {
            try {
                val jsonObj = JSONObject(response)
                val main = jsonObj.getJSONObject("main")
                val temp = main.getDouble("temp") // temperatura w kelwinach
                val city = jsonObj.getString("name")
                val coord = jsonObj.getJSONObject("coord")
                val lon = coord.getDouble("lon")
                val lat = coord.getDouble("lat")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val description = weather.getString("description")
                val pressure = main.getInt("pressure")


                val timezoneOffset = jsonObj.getLong("timezone")
                val currentTimeUTC = System.currentTimeMillis() / 1000
                val localTime = currentTimeUTC + timezoneOffset

                val sdf = SimpleDateFormat("HH:mm:ss yyyy-MM-dd ", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val sdfTime = sdf.format(localTime * 1000)




                val tempInCelsius = temp - 273.15
                val formattedTemp = "${tempInCelsius.toInt()}°C"
                val formattedCoord = "Longitude: $lon, Latitude: $lat"
                val formattedWeather = "Weather: $description"
                val formattedPressure = "Pressure: $pressure hPa"
                val formattedTime = "Local Time: $sdfTime"

                cityTextView.text = city
                temperatureTextView.text = formattedTemp
                coordinatesTextView.text = formattedCoord
                weatherTextView.text = formattedWeather
                pressureTextView.text = formattedPressure
                timeTextView.text = formattedTime
            } catch (e: Exception) {
                cityTextView.text = "-"
                temperatureTextView.text = "-"
                coordinatesTextView.text = "Coordinates: -"
                weatherTextView.text = "Weather: -"
                pressureTextView.text = "Pressure: -"
                timeTextView.text = "Local Time: -"
            }
        }


    }
}
