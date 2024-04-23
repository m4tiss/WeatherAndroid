package com.example.weatherapp

import FavouritesCitiesViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class FavouritesCitiesAdapter(
    private var cities: MutableList<String>,
    private val favouritesCitiesViewModel: FavouritesCitiesViewModel
) : RecyclerView.Adapter<FavouritesCitiesAdapter.FavouritesCitiesViewHolder>() {

    inner class FavouritesCitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityNameTextView: TextView = itemView.findViewById(R.id.cityName)
        val XIconTextView: TextView = itemView.findViewById(R.id.XIcon)

        init {
            XIconTextView.setOnClickListener {
                showRemoveConfirmationDialog(adapterPosition)
            }
        }

        private fun showRemoveConfirmationDialog(position: Int) {
            val context = itemView.context
            val cityToRemove = cities[position]

            AlertDialog.Builder(context)
                .setTitle("Confirm Removal")
                .setMessage("Are you sure you want to remove '$cityToRemove' from favorites?")
                .setPositiveButton("Yes") { dialog, _ ->
                    favouritesCitiesViewModel.removeFavouriteCity(context, cityToRemove)
                    cities.removeAt(position)
                    notifyItemRemoved(position)
                    dialog.dismiss()
                    Toast.makeText(context, "City removed from favorites", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesCitiesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_item, parent, false)
        return FavouritesCitiesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavouritesCitiesViewHolder, position: Int) {
        val currentCity = cities[position]
        holder.cityNameTextView.text = currentCity
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newCities: List<String>) {
        cities.clear()
        cities.addAll(newCities)
        notifyDataSetChanged()
        println("Set nowych danych")
    }
}



