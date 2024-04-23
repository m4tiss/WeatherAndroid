package com.example.weatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapterTablet(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentSettingsTablet()
            1 -> FragmentTodayWeatherTablet()
            2 -> FragmentNextDays()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}