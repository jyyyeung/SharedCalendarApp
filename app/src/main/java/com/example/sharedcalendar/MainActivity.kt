package com.example.sharedcalendar

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {
//    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        var buttonDrawerToggle : ImageButton = findViewById(R.id.drawerLayoutToggle)

        buttonDrawerToggle.setOnClickListener({
            drawerLayout.open()
        })
    }
}
