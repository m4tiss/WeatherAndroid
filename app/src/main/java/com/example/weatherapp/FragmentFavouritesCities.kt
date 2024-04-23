package com.example.weatherapp

import FavouritesCitiesViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentFavouritesCities : Fragment() {

    private lateinit var recyclerCitiesView: RecyclerView
    private lateinit var favouritesCitiesAdapter: FavouritesCitiesAdapter
    private lateinit var favouritesCitiesViewModel: FavouritesCitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourites_cities, container, false)

        recyclerCitiesView = view.findViewById(R.id.recyclerCitiesView)
        recyclerCitiesView.layoutManager = LinearLayoutManager(requireContext())

        favouritesCitiesViewModel = ViewModelProvider(requireActivity()).get(FavouritesCitiesViewModel::class.java)

        val emptyList: MutableList<String> = mutableListOf()

        favouritesCitiesAdapter = FavouritesCitiesAdapter(emptyList, favouritesCitiesViewModel)
        recyclerCitiesView.adapter = favouritesCitiesAdapter

        favouritesCitiesViewModel.loadFavouriteCities(requireContext())

        favouritesCitiesViewModel.favouritesCities.observe(viewLifecycleOwner) { favouritesCities ->
            favouritesCitiesAdapter.setData(favouritesCities)

        }

        return view
    }
}

