package com.example.weatherapp

import UnitViewModel
import WeatherData
import WeatherDataForecast
import WeatherViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.util.ArrayList
import java.util.Locale
import kotlin.math.log


class FragmentFavouritesCities : Fragment() {

    private lateinit var recyclerCitiesView: RecyclerView
    private lateinit var favouritesCitiesAdapter: FavouritesCitiesAdapter
    private var cities: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourites_cities, container, false)


        recyclerCitiesView = view.findViewById(R.id.recyclerCitiesView)
        recyclerCitiesView.layoutManager = LinearLayoutManager(context)

        cities.add("Zgierz")
        cities.add("Warszawa")
        favouritesCitiesAdapter = FavouritesCitiesAdapter(cities)
        recyclerCitiesView.adapter = favouritesCitiesAdapter

        return view
    }


}
