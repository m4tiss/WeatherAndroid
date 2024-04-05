package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
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
    private lateinit var weatherImageView: ImageView
    private lateinit var coordinatesTextView: TextView
    private lateinit var weatherTextView: TextView
    private lateinit var pressureTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var searchButton: Button
    private lateinit var searchEditText: TextInputEditText
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
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
        weatherImageView = view.findViewById(R.id.weatherImageView)

        searchButton = view.findViewById(R.id.searchButton)
        searchEditText = view.findViewById(R.id.searchEditText)

        searchButton.setOnClickListener {
            val city = searchEditText.text.toString()
            val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
            val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"
            WeatherTask().execute(apiUrl)
        }

        val defaultCity = "Warsaw"
        val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        val defaultApiUrl = "https://api.openweathermap.org/data/2.5/weather?q=$defaultCity&appid=$apiKey"
        WeatherTask().execute(defaultApiUrl)

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
                    weatherImageView.setImageResource(R.drawable.nodata)
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



                sharedPreferences = requireContext().getSharedPreferences("WeatherPreferences", Context.MODE_PRIVATE)
                val currentUnit = sharedPreferences.getString("temperatureUnit", "Kelvin")
                val tempInCelsius = if (currentUnit == "Kelvin") {
                    temp - 273.15
                } else {
                    temp
                }
                val temperatureValue = tempInCelsius.toInt()
                val temperatureUnit = if (currentUnit == "Kelvin") "K" else "°C"
                val formattedTemp = "$temperatureValue$temperatureUnit"

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

                val icon = when (description) {
                    "clear sky" -> R.drawable.sun_and_cloud
                    "few clouds" -> R.drawable.cloud
                    "scattered clouds" -> R.drawable.cloud
                    "broken clouds" -> R.drawable.cloud
                    "shower rain" -> R.drawable.rain
                    "rain" -> R.drawable.rain
                    "thunderstorm" -> R.drawable.storm
                    "snow" -> R.drawable.snow
                    "mist" -> R.drawable.mist
                    "light rain" -> R.drawable.little_rain
                    "light intensity shower rain" -> R.drawable.little_rain
                    "overcast clouds" -> R.drawable.cloud
                    else -> R.drawable.nodata
                }
                weatherImageView.setImageResource(icon)

            } catch (e: Exception) {
                cityTextView.text = "-"
                temperatureTextView.text = "-"
                coordinatesTextView.text = "Coordinates: -"
                weatherTextView.text = "Weather: -"
                pressureTextView.text = "Pressure: -"
                timeTextView.text = "Local Time: -"
                weatherImageView.setImageResource(R.drawable.nodata)
            }
        }


    }
}
