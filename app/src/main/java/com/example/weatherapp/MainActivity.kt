package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var temperatureTextView: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        temperatureTextView = findViewById(R.id.temperatureTextView)

        val city = "London"
        val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"

        WeatherTask().execute(apiUrl)
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