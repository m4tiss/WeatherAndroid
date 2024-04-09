package com.example.weatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentSettings()
            1 -> FragmentTodayWeather()
            2 -> FragmentAdvancedInfo()
            3 -> FragmentNextDays()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
