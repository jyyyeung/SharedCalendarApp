package com.example.sharedcalendar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.sharedcalendar.ui.CalendarFragment
import com.example.sharedcalendar.ui.ManageCalendarsFragment
import com.example.sharedcalendar.ui.SettingsFragment
import com.example.sharedcalendar.ui.login.AuthActivity
import com.example.sharedcalendar.ui.login.LoginViewModelFactory
import com.example.sharedcalendar.ui.login.UserViewModel
import com.example.sharedcalendar.ui.share.ShareCalendarFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferencesContextWrapper


class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val firebaseViewModel by viewModels<FirebaseViewModel>()
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: FirebaseUser
    private lateinit var prefs: SharedPreferences

    private var isSignInDisplayed: Boolean = false
    private var isModifyingSettings: Boolean = false

    /*
     * Fragments
     */
    private lateinit var mFragmentManager: FragmentManager
    private var mHomeFragment: CalendarFragment = CalendarFragment()
    private var mManageCalendarsFragment: ManageCalendarsFragment = ManageCalendarsFragment()
//    private var mSettingsFragment: SettingsFragment = SettingsFragment()

    companion object {
        private val TAG: String = MainActivity::class.java.name
    }

    //
//    override fun onStart() {
//        super.onStart()
//        FirebaseAuth.getInstance().addAuthStateListener(this);
//    }
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(SharedFirebasePreferencesContextWrapper(newBase))
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (prefs != null) {
//            prefs?.keepSynced(true)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (prefs != null) {
//            prefs?.keepSynced(false)
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFragmentManager = supportFragmentManager

        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]

        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        user = Firebase.auth.currentUser!!
//        SharedFirebasePreferences.getInstance(
//            this,
//            "com-example-sharedcalendar_preferences",
//            Context.MODE_PRIVATE
//        ).pull()
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
//        prefs.edit().putString("name", user.displayName).apply()

        // START SIDEBAR NAVIGATION //
        //Drawer button
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val buttonDrawerToggle: ImageButton = findViewById(R.id.drawerLayoutToggle)
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
        buttonDrawerToggle.setOnClickListener {
            drawerLayout.open()

            val tvUsername: TextView = findViewById(R.id.tvSidebarUsername)
            val tvEmail: TextView = findViewById(R.id.tvSidebarEmail)
            // Update Sidebar username if current values are default
            tvUsername.text = user.displayName
            tvEmail.text = user.email
        }


        nvSidebar.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = false
            isModifyingSettings = false
            val mFragmentTransaction = mFragmentManager.beginTransaction()
            Log.i(TAG, menuItem.toString() + menuItem.itemId)
            if (menuItem.toString() == "Settings") {
                isModifyingSettings = true
                // Open settings activity
//                startActivity(Intent(this, SettingsActivity::class.java))
                mFragmentTransaction.replace(R.id.mainFragment, SettingsFragment(user))

            } else if (menuItem.toString() == "Logout") {
//                val localPrefs = PreferenceManager.getDefaultSharedPreferences(this)
//                prefs.edit().clear().apply().also {
//                prefs?.keepSynced(false)
//                prefs = null

                // Call Logout process
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, AuthActivity::class.java).run {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
                finishAffinity()
//                }
            } else if (menuItem.toString() == "Add Person to Calendar") {
                // TODO: Handle add person to calendar
                // Create and show the dialog.
                ShareCalendarFragment.display(supportFragmentManager)
//                ft.addToBackStack(null)
            } else if (menuItem.toString() == "Manage Calendars") {
//                supportFragmentManager.beginTransaction().apply {
//                    replace(
//                        R.id.mainFragment, ManageCalendarsFragment()
//                    )
//                    commit()
//                }
                mFragmentTransaction.replace(R.id.mainFragment, mManageCalendarsFragment)
            } else if (menuItem.toString() == "Home") {
                // By default, open the Calendar fragment
                mFragmentTransaction.replace(R.id.mainFragment, mHomeFragment)
//                supportFragmentManager.beginTransaction().apply {
//                    replace(R.id.mainFragment, CalendarFragment())
//                    commit()
//                }
            }
            mFragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
//            menuItem.itemId
            drawerLayout.close()
            true
        }


        // END SIDEBAR NAVIGATION //
//
//        val calendarFragment = CalendarFragment()

        // By default, open the Calendar fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, mHomeFragment)
            commit()
        }


    }

    fun getCalendarId(): String? {
        return firebaseViewModel.calendars.value?.get(0)?.id
//        return calendars[0].id
    }

    //
    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
//        // Get the current firebase user
//        val currentUser: FirebaseUser? = firebaseAuth.currentUser;
//
//        Log.i(TAG, "Changed $firebaseAuth ${firebaseAuth.currentUser?.uid}")
//        if (currentUser == null) {
//            if (isSignInDisplayed) {
//                prefs = null
//                return
//            }
//            isSignInDisplayed = true
//            prefs = null
//            startActivity(Intent(this, AuthActivity::class.java))
//            finishAffinity()
//        } else {
//            isSignInDisplayed = false
//            Log.d(
//                TAG,
//                prefs.toString() + " " + SharedFirebasePreferences.getInstance(
//                    this,
//                    "com-example-sharedcalendar_preferences",
//                    Context.MODE_PRIVATE
//                )
//                    .toString() + " " + PreferenceManager.getDefaultSharedPreferences(this)
//                    .toString()
//            )
//            prefs = SharedFirebasePreferences.getInstance(
//                this,
//                "com-example-sharedcalendar_preferences",
//                Context.MODE_PRIVATE
//            )
//            Log.d(
//                TAG,
//                prefs.toString() + " " + SharedFirebasePreferences.getInstance(
//                    this,
//                    "com-example-sharedcalendar_preferences",
//                    Context.MODE_PRIVATE
//                )
//                    .toString() + " " + PreferenceManager.getDefaultSharedPreferences(this)
//                    .toString()
//            )
//            PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
//            if (prefs!!.all.isNullOrEmpty() || prefs == {}) {
//                prefs = null
//                return
//            }
//            mSettingsFragment = SettingsFragment()
////            prefs!!.keepSynced(true)
////            prefs.registerOnSharedPreferenceChangeListener(this)
////            prefs.omitKeys("name")
////            prefs.edit().putString("name", firebaseAuth.currentUser!!.displayName).apply()
//            Log.i(TAG, "Shared Prefs auth state changed ${prefs!!.all}")
//            prefs!!.pull().addOnPullCompleteListener(object :
//                SharedFirebasePreferences.OnPullCompleteListener {
//                override fun onPullSucceeded(preferences: SharedFirebasePreferences) {
//                    Log.d(TAG, "${preferences.all}")
//                    val mFragmentTransaction: FragmentTransaction =
//                        mFragmentManager.beginTransaction()
//                    mFragmentTransaction.replace(R.id.mainFragment, mHomeFragment)
//                    mFragmentTransaction.commit()
//                    mFragmentManager.executePendingTransactions()
//                }
//
//                override fun onPullFailed(e: Exception) {
////                    supportFragmentManager.beginTransaction()
////                        .replace(R.id.settings, SettingsActivity.SettingsFragment(prefs))
////                        .commitAllowingStateLoss()
////                    showView()
////                    recreate()
//                    Toast.makeText(this@MainActivity, "Fetch failed", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
    }

    //
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.i(TAG, "Shared Preference Changed $key")
        if (FirebaseAuth.getInstance().currentUser != null && isModifyingSettings) {
            val calendarPrefs =
                sharedPreferences?.all?.filterKeys { it.contains("${user.uid}|calendar|") }
            if (calendarPrefs != null) {
                firebaseViewModel.getCalendars(calendarPrefs, true)
            }
//            SharedFirebasePreferences.getInstance(
//                this,
//                "com-example-sharedcalendar_preferences",
//                Context.MODE_PRIVATE
//            ).push()
        }
    }


}
