package com.example.sharedcalendar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcalendar.data.SessionManager
import com.example.sharedcalendar.ui.SettingsActivity
import com.example.sharedcalendar.ui.login.AuthActivity
import com.example.sharedcalendar.ui.login.LoginViewModelFactory
import com.example.sharedcalendar.ui.login.UserViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val TAG: String = MainActivity::class.java.name

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManager = SessionManager(this)
        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
        val currentUser = Firebase.auth.currentUser

        // START SIDEBAR NAVIGATION //
        //Drawer button
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val buttonDrawerToggle: ImageButton = findViewById(R.id.drawerLayoutToggle)
        val nvSidebar: NavigationView = findViewById(R.id.nvSidebar)

        // If Click on Burger, Open drawer Layout
        buttonDrawerToggle.setOnClickListener {
            drawerLayout.open()

            val tvUsername: TextView = findViewById(R.id.tvSidebarUsername)
            val tvEmail: TextView = findViewById(R.id.tvSidebarEmail)
            // Update Sidebar username if current values are default
            if (tvUsername.text == getString(R.string.default_sidebar_username) || tvEmail.text == getString(
                    R.string.default_sidebar_email
                )
            ) {
                tvUsername.text = currentUser?.displayName
                tvEmail.text = currentUser?.email
            }
        }


        nvSidebar.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = false

            Log.i(TAG, menuItem.toString() + menuItem.itemId)
            if (menuItem.toString() == "Settings") {
                // Open settings activity
                startActivity(Intent(this, SettingsActivity::class.java))
            } else if (menuItem.toString() == "Logout") {
                // Call Logout process
                Firebase.auth.signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            } else if (menuItem.toString() == "Add Person to Calendar") {
                // TODO: Handle add person to calendar
            }
//            menuItem.itemId
            drawerLayout.close()
            true
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


    }
}
