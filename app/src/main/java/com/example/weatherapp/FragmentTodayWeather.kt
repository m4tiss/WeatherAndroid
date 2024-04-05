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

class FragmentTodayWeather : Fragment() {

    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView
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
                }
            }
        }

        private fun handleResponse(response: String) {
            try {
                val jsonObj = JSONObject(response)
                val main = jsonObj.getJSONObject("main")
                val temp = main.getDouble("temp") // temperatura w kelwinach
                val city = jsonObj.getString("name")

                // Przekształć temperaturę na stopnie Celsiusza
                val tempInCelsius = temp - 273.15
                cityTextView.text = city.toString()
                temperatureTextView.text = "${tempInCelsius.toInt()}°C"
            } catch (e: Exception) {
                cityTextView.text = "-"
                temperatureTextView.text = "-"
            }
        }
    }
}
