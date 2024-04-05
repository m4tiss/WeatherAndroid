package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()


        val fragmentTodayWeather = FragmentTodayWeather()
        val fragmentAdvancedInfo = FragmentAdvancedInfo()


        fragmentTransaction.add(R.id.fragmentTodayContainer, fragmentTodayWeather)
        fragmentTransaction.add(R.id.fragmentAdvancedInfo, fragmentAdvancedInfo)


        fragmentTransaction.commit()



    }


}