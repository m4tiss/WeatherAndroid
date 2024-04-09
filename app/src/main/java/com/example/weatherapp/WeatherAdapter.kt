package com.example.weatherapp

import CityViewModel
import UnitViewModel
import WeatherData
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

private lateinit var unitViewModel: UnitViewModel

class WeatherAdapter(private val weatherDataList: MutableList<WeatherData>, private val unitViewM: UnitViewModel) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temperatureTextView: TextView = itemView.findViewById(R.id.temperature)
        val weatherIconImageView: ImageView = itemView.findViewById(R.id.weatherIcon)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val feelsTextView: TextView = itemView.findViewById(R.id.feels)
        val pressureTextView: TextView = itemView.findViewById(R.id.pressure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item, parent, false)
        unitViewModel = unitViewM
        return WeatherViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentWeather = weatherDataList[position]

        val unit = unitViewModel.unit.value
        val temperatureText = when (unit) {
            "metric" -> "${currentWeather.temperature}°C"
            "standard" -> "${currentWeather.temperature}K"
            else -> "${currentWeather.temperature}°"
        }
        holder.temperatureTextView.text = temperatureText

        holder.weatherIconImageView.setImageBitmap(currentWeather.weatherIconBitmap)

        holder.dateTextView.text = currentWeather.date

        val feelsText = when (unit) {
            "metric" -> "Feels: ${currentWeather.feels}°C"
            "standard" -> "Feels: ${currentWeather.feels}K"
            else -> "Feels: ${currentWeather.feels}°"
        }
        holder.feelsTextView.text = feelsText

        holder.pressureTextView.text = "Pressure: ${currentWeather.pressure}hPa"
    }


    override fun getItemCount(): Int {
        return weatherDataList.size
    }

    fun setData(newWeatherDataList: List<WeatherData>) {
        weatherDataList.clear()
        weatherDataList.addAll(newWeatherDataList)
        notifyDataSetChanged()
    }
}

