package com.example.weatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentSettings()
            1 -> FragmentFavouritesCities()
            2 -> FragmentTodayWeather()
            3 -> FragmentAdvancedInfo()
            4 -> FragmentNextDays()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
