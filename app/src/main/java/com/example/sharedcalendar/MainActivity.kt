package com.example.sharedcalendar

import android.os.Bundle
import android.widget.Button
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

        val monthViewFragment = MonthViewFragment()
        val weekViewFragment = WeekViewFragment()
        val weekBtn : Button = findViewById(R.id.weekBtn)
        val monthBtn : Button = findViewById(R.id.monthBtn)


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, monthViewFragment)
            commit()
        }
        monthBtn.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, monthViewFragment)
                addToBackStack(null)
                commit()
            }
        }
        weekBtn.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, weekViewFragment)
                addToBackStack(null)
                commit()
            }
        }


    }
}
