package com.example.tp3veilletechnologique

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ParkInfoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.park_info)

        val parkName = intent.getStringExtra("parkName")
        val location = intent.getStringExtra("parkLocation")
        val latitude = intent.getDoubleExtra("parkLatitude", 0.0)
        val longitude = intent.getDoubleExtra("parkLongitude", 0.0)


        findViewById<TextView>(R.id.ParkName).text = parkName
        findViewById<TextView>(R.id.Location).text = location
        findViewById<TextView>(R.id.Latitude).text = latitude.toString()
        findViewById<TextView>(R.id.Longitude).text = longitude.toString()
    }
}