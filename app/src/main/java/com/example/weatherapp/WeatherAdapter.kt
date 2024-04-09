package com.example.weatherapp

import WeatherData
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val weatherDataList: MutableList<WeatherData>) :
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
        return WeatherViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentWeather = weatherDataList[position]

        holder.temperatureTextView.text = currentWeather.temperature.toString() + "°"
        holder.weatherIconImageView.setImageBitmap(currentWeather.weatherIconBitmap) //
        holder.dateTextView.text = currentWeather.date
        holder.feelsTextView.text = "Feels: "+ currentWeather.feels.toString() + "°"
        holder.pressureTextView.text = "Pressure: " + currentWeather.pressure.toString() + "hPa"
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

