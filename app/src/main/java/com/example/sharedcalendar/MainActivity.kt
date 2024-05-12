package com.example.sharedcalendar

/**
 * This file is the main activity of the Shared Calendar app.
 * It imports the necessary classes for Android intents.
 */
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.sharedcalendar.ui.CalendarFragment
import com.example.sharedcalendar.ui.ManageCalendarsFragment
import com.example.sharedcalendar.ui.SettingsFragment
import com.example.sharedcalendar.ui.login.AuthActivity
import com.example.sharedcalendar.ui.login.LoginViewModelFactory
import com.example.sharedcalendar.ui.login.UserViewModel
import com.example.sharedcalendar.ui.share.ShareCalendarFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.common.annotations.VisibleForTesting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * The main activity of the SharedCalendar app.
 *
 * @param auth The instance of FirebaseAuth used for authentication.
 */
class MainActivity(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) :
    AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    @VisibleForTesting
    lateinit var firebaseViewModel: FirebaseViewModel

    private lateinit var userViewModel: UserViewModel
    private lateinit var user: FirebaseUser
    private lateinit var prefs: SharedPreferences

    private var isModifyingSettings: Boolean = false

    /*
     * Fragments
     */
    private lateinit var mFragmentManager: FragmentManager

    @VisibleForTesting
    var mHomeFragment: CalendarFragment = CalendarFragment()

    @VisibleForTesting
    var mManageCalendarsFragment: ManageCalendarsFragment = ManageCalendarsFragment()

    companion object {
        private val TAG: String = MainActivity::class.java.name
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        if (currentUser != null) {
            user = currentUser
        }

        // START SIDEBAR NAVIGATION //
        // Drawer button
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
//        val buttonDrawerToggle: ImageButton = findViewById(R.id.drawerLayoutToggle)
        val nvSidebar: NavigationView = findViewById(R.id.nvSidebar)

        firebaseViewModel.getUserShares()

        val calendarPrefs = prefs.all?.filterKeys { it.contains("${user.uid}|calendar|") }
        Log.i("Calendar Prefs", calendarPrefs.toString())

        firebaseViewModel.userShares.observe(this) {
            if (calendarPrefs != null) {
                firebaseViewModel.getCalendars(calendarPrefs)
            }
        }

        // If Click on Burger, Open drawer Layout
        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            drawerLayout.open()

            val tvUsername: TextView = findViewById(R.id.tvSidebarUsername)
            val tvEmail: TextView = findViewById(R.id.tvSidebarEmail)
            // Update Sidebar username if current values are default
            tvUsername.text = prefs.getString("${user.uid}|name", null) ?: user.displayName
            tvEmail.text = user.email
        }

        nvSidebar.setNavigationItemSelectedListener { menuItem ->
            navigationItemSelectedListener(menuItem, drawerLayout)
        }

        // END SIDEBAR NAVIGATION //
        // By default, open the Calendar fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, mHomeFragment)
            commit()
        }
    }

    /**
     * Handles the selection of items in the navigation drawer.
     *
     * @param item The selected item in the navigation drawer.
     * @param drawerLayout The drawer layout to close after selecting an item.
     */
    private fun navigationItemSelectedListener(
        menuItem: MenuItem,
        drawerLayout: DrawerLayout,
    ): Boolean {
        // Handle menu item selected
        menuItem.isChecked = false
        isModifyingSettings = false
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        Log.i(TAG, menuItem.toString() + menuItem.itemId)
        if (menuItem.toString() == "Settings") {
            isModifyingSettings = true
            // Open settings activity
            mFragmentTransaction.replace(R.id.mainFragment, SettingsFragment(user))
        } else if (menuItem.toString() == "Logout") {
            // Call Logout process
            FirebaseAuth.getInstance().signOut()
            startActivity(
                Intent(this, AuthActivity::class.java).run {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
            )
            finishAffinity()
        } else if (menuItem.toString() == "Add Person to Calendar") {
            // Handle add person to calendar
            // Create and show the dialog.
            ShareCalendarFragment.display(supportFragmentManager)
        } else if (menuItem.toString() == "Manage Calendars") {
            mFragmentTransaction.replace(R.id.mainFragment, mManageCalendarsFragment)
        } else if (menuItem.toString() == "Home") {
            // By default, open the Calendar fragment
            mFragmentTransaction.replace(R.id.mainFragment, mHomeFragment)
        }
        mFragmentTransaction.commit()
        mFragmentManager.executePendingTransactions()
        drawerLayout.close()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFragmentManager = supportFragmentManager

        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
    }

    /**
     * Retrieves the calendar ID.
     *
     * @return The calendar ID as a String, or null if it cannot be retrieved.
     */
    fun getCalendarId(): String? {
        return firebaseViewModel.calendars.value?.get(0)?.id
    }

    /**
     * Called when a shared preference is changed, added, or removed.
     *
     * @param sharedPreferences The SharedPreferences that received the change.
     * @param key The key of the preference that was changed, added, or removed.
     */
    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?,
        key: String?,
    ) {
        Log.i(TAG, "Shared Preference Changed $key")
        if (FirebaseAuth.getInstance().currentUser != null && isModifyingSettings) {
            val calendarPrefs =
                sharedPreferences?.all?.filterKeys { it.contains("${user.uid}|calendar|") }
            if (calendarPrefs != null) {
                firebaseViewModel.getCalendars(calendarPrefs, true)
            }
        }
    }
}
