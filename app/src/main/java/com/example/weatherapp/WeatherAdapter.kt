package com.example.weatherapp

import UnitViewModel
import WeatherPanel
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

private lateinit var unitViewModel: UnitViewModel

class WeatherAdapter(private val weatherPanelList: MutableList<WeatherPanel>, private val unitViewM: UnitViewModel) :
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
        val currentWeather = weatherPanelList[position]

        val unit = unitViewModel.unit.value
        val temperatureText = when (unit) {
            "metric" -> "${currentWeather.temperature}°C"
            "standard" -> "${currentWeather.temperature}K"
            else -> "${currentWeather.temperature}°"
        }
        holder.temperatureTextView.text = temperatureText

        val iconResId = holder.itemView.context.resources.getIdentifier(
            "icon_${currentWeather.icon}", "drawable", holder.itemView.context.packageName
        )
        if (iconResId != 0) {
            holder.weatherIconImageView.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, iconResId))
        } else {
            holder.weatherIconImageView.setImageDrawable(null)
        }

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
        return weatherPanelList.size
    }

    fun setData(newWeatherPanelList: List<WeatherPanel>) {
        weatherPanelList.clear()
        weatherPanelList.addAll(newWeatherPanelList)
        notifyDataSetChanged()
    }
}

