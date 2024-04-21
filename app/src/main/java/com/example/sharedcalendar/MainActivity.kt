package com.example.sharedcalendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior

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
        val addEventBtn : ImageButton = findViewById(R.id.addEventBtn)
        val bottomWindow = BottomSheetFragment()
        addEventBtn.setOnClickListener(){
            //showBottomWindow()
            bottomWindow.show(supportFragmentManager,"BottomSheetDialogue")

        }






    }
/*
    fun showBottomWindow(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_window)



        //val text1Layout : LinearLayout = findViewById(R.id.bottom_window_text1_layout)
        //val text2Layout : LinearLayout = findViewById(R.id.bottom_window_text2_layout)
*//*
        text1Layout.setOnClickListener(){
            dialog.dismiss()
            Toast.makeText(this, "Text1", Toast.LENGTH_SHORT).show()
        }
        text2Layout.setOnClickListener(){
            dialog.dismiss()
            Toast.makeText(this, "Text2", Toast.LENGTH_SHORT).show()
        }*//*

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.SlideAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        fun onClick1(){
            dialog.dismiss()
            Toast.makeText(this, "Text1", Toast.LENGTH_SHORT).show()
        }
        fun onClick2(){
            dialog.dismiss()
            Toast.makeText(this, "Text2", Toast.LENGTH_SHORT).show()
        }



    }*/

}
