package com.example.weatherapp

import UnitViewModel
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
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class FragmentNextDays : Fragment() {

//    private lateinit var weatherAdapter: WeatherAdapter
//    private lateinit var recyclerView: RecyclerView
//
//    private lateinit var unitViewModel: UnitViewModel
//    private lateinit var cityViewModel: CityViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_nextdays, container, false)
//
//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//
//        unitViewModel = ViewModelProvider(requireActivity()).get(UnitViewModel::class.java)
//        cityViewModel = ViewModelProvider(requireActivity()).get(CityViewModel::class.java)
//
//
//        val list: MutableList<WeatherPanel> = ArrayList()
//        weatherAdapter = WeatherAdapter(list,unitViewModel)
//        recyclerView.adapter = weatherAdapter
//
//
//        cityViewModel.city.observe(viewLifecycleOwner, Observer { city ->
//            fetchWeatherData(city)
//        })
//        unitViewModel.unit.observe(viewLifecycleOwner, Observer { unit ->
//            fetchWeatherData(cityViewModel.city.value)
//        })
//
//        return view
//    }
//
//    private fun fetchWeatherData(city: String?) {
//        city?.let {
//            val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
//            val apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=$it&appid=$apiKey&units=${unitViewModel.unit.value}"
//
//            GlobalScope.launch(Dispatchers.Main) {
//                try {
//                    val response = withContext(Dispatchers.IO) {
//                        URL(apiUrl).readText()
//                    }
//                    handleResponse(response)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    private fun handleResponse(response: String) {
//        try {
//            val jsonObj = JSONObject(response)
//            val list = jsonObj.getJSONArray("list")
//
//            data class WeatherEntry(val dateTime: Date, val weatherPanel: WeatherPanel)
//
//            val sortedWeatherEntries = mutableListOf<WeatherEntry>()
//
//            for (i in 0 until list.length()) {
//                val forecastObj = list.getJSONObject(i)
//                val dateTimeString = forecastObj.getString("dt_txt")
//                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                val date = inputFormat.parse(dateTimeString)
//
//                if (date != null && dateTimeString.endsWith("12:00:00")) {
//                    val mainObj = forecastObj.getJSONObject("main")
//                    val weatherArray = forecastObj.getJSONArray("weather")
//                    val weatherObj = weatherArray.getJSONObject(0)
//
//                    val temperature = mainObj.getDouble("temp").toInt()
//                    val weatherIcon = weatherObj.getString("icon")
//
//                    val iconBaseUrl = "https://openweathermap.org/img/wn/"
//                    val weatherIconUrl = "${iconBaseUrl}${weatherIcon}@2x.png"
//
//                    GlobalScope.launch(Dispatchers.IO) {
//                        val iconBitmap = downloadBitmapFromUrl(weatherIconUrl)
//
//                        withContext(Dispatchers.Main) {
//                            val outputFormat = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
//                            val formattedDate = outputFormat.format(date)
//                            val feelsLike = mainObj.getDouble("feels_like").toInt()
//                            val pressure = mainObj.getInt("pressure")
//
//                            val weatherPanel = WeatherPanel(temperature, iconBitmap, formattedDate, feelsLike, pressure)
//                            sortedWeatherEntries.add(WeatherEntry(date, weatherPanel))
//
//                            if (sortedWeatherEntries.size == list.length() / 8) {
//                                sortedWeatherEntries.sortBy { it.dateTime }
//
//                                val sortedWeatherDataList = sortedWeatherEntries.map { it.weatherPanel }
//
//                                weatherAdapter.setData(sortedWeatherDataList.toMutableList())
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//
//
//    private fun downloadBitmapFromUrl(url: String): Bitmap? {
//        return try {
//            val connection = URL(url).openConnection()
//            connection.connect()
//            val inputStream = connection.getInputStream()
//            BitmapFactory.decodeStream(inputStream)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }


}
