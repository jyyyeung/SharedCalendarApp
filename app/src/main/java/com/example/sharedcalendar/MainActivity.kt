package com.example.sharedcalendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // START SIDEBAR NAVIGATION //
        //Drawer button
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val buttonDrawerToggle: ImageButton = findViewById(R.id.drawerLayoutToggle)

        // If Click on Burger, Open drawer Layout
        buttonDrawerToggle.setOnClickListener {
            drawerLayout.open()
        }
        // END SIDEBAR NAVIGATION //


        // Change Fragment from month to week on button click
        // Set variables for change view buttons
        val weekBtn: Button = findViewById(R.id.weekBtn)
        val monthBtn: Button = findViewById(R.id.monthBtn)

        // Set variables for View Fragments
        val monthViewFragment = MonthViewFragment()
        val weekViewFragment = WeekViewFragment()

        // By default, the view fragment is month view fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, monthViewFragment)
            commit()
        }

        // When Click Month Button
        monthBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                // Replace fragment with month view fragment
                replace(R.id.flFragment, monthViewFragment)
                addToBackStack(null)
                // commit the change
                commit()
            }
        }
        // When Click Week Button
        weekBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                // Replace fragment with week view fragment
                replace(R.id.flFragment, weekViewFragment)
                addToBackStack(null)
                // Commit the changes
                commit()
            }
        }

        // When Click AddEvent Button
        val addEventBtn: ImageButton = findViewById(R.id.addEventBtn)
        val bottomWindow = BottomSheetFragment()
        addEventBtn.setOnClickListener() {
            //showBottomWindow()
            bottomWindow.show(supportFragmentManager, "BottomSheetDialogue")

        }


    }
}
