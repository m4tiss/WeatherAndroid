package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class FragmentTodayWeather : Fragment() {

    private lateinit var temperatureTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today_weather, container, false)
        temperatureTextView = view.findViewById(R.id.temperatureTextView)

        val city = "London"
        val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"

        WeatherTask().execute(apiUrl)

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
                    temperatureTextView.text = "Błąd podczas pobierania danych"
                }
            }
        }

        private fun handleResponse(response: String) {
            try {
                val jsonObj = JSONObject(response)
                val main = jsonObj.getJSONObject("main")
                val temp = main.getDouble("temp") // temperatura w kelwinach

                // Przekształć temperaturę na stopnie Celsiusza
                val tempInCelsius = temp - 273.15

                temperatureTextView.text = "Temperatura w Londynie: ${tempInCelsius.toInt()}°C"
            } catch (e: Exception) {
                temperatureTextView.text = "Błąd podczas przetwarzania danych"
            }
        }
    }
}
