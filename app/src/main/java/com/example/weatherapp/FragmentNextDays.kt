package com.example.weatherapp

import UnitViewModel
import WeatherDataForecast
import WeatherPanel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class FragmentNextDays : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter

    private lateinit var unitViewModel: UnitViewModel
    private lateinit var weatherForecastViewModel: WeatherForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nextdays, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        unitViewModel = ViewModelProvider(requireActivity()).get(UnitViewModel::class.java)
        weatherForecastViewModel =
            ViewModelProvider(requireActivity()).get(WeatherForecastViewModel::class.java)

        weatherAdapter = WeatherAdapter(mutableListOf(), unitViewModel)
        recyclerView.adapter = weatherAdapter

        weatherForecastViewModel.weatherForecast.observe(
            viewLifecycleOwner,
            Observer { weatherForecast ->
                displayWeatherDataForecast(weatherForecast)
            })

        return view
    }

    private fun displayWeatherDataForecast(forecastList: List<WeatherDataForecast>) {
        val weatherPanelList = mutableListOf<WeatherPanel>()

        forecastList.forEach { weatherDataForecast ->
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val iconBitmap = downloadBitmapFromUrl(weatherDataForecast.iconUrl)
                    val formattedDate =
                        SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault()).format(
                            weatherDataForecast.dateTime
                        )
                    val feelsLike = weatherDataForecast.feelsLike.toInt()
                    val pressure = weatherDataForecast.pressure

                    val weatherPanel = WeatherPanel(
                        weatherDataForecast.temperature,
                        iconBitmap,
                        formattedDate,
                        feelsLike,
                        pressure
                    )
                    weatherPanelList.add(weatherPanel)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (weatherPanelList.size == forecastList.size) {
                    weatherPanelList.sortBy { it.date }
                    weatherAdapter.setData(weatherPanelList)
                }
            }
        }
    }

}

private suspend fun downloadBitmapFromUrl(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
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



