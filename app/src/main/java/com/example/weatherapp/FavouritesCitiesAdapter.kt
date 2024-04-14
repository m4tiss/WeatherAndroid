package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FavouritesCitiesAdapter(private var cities: MutableList<String>) :
    RecyclerView.Adapter<FavouritesCitiesAdapter.FavouritesCitiesViewHolder>() {

    inner class FavouritesCitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityNameTextView: TextView = itemView.findViewById(R.id.cityName)
        val XIconTextView: TextView = itemView.findViewById(R.id.XIcon)
        init {
            XIconTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    cities.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesCitiesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_item, parent, false)
        return FavouritesCitiesViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavouritesCitiesViewHolder, position: Int) {
        val currentCity = cities[position]
        holder.cityNameTextView.text = currentCity
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun setData(newCities: MutableList<String>) {
        cities = newCities
        notifyDataSetChanged()
    }
}


