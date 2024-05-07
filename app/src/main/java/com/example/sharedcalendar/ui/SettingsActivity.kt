package com.example.sharedcalendar.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.R
import com.example.sharedcalendar.ui.login.LoginViewModelFactory
import com.example.sharedcalendar.ui.login.UserViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferencesContextWrapper


private val TAG: String = SettingsActivity::class.java.name

class SettingsActivity : AppCompatActivity(),
//    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
    FirebaseAuth.AuthStateListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPrefs: SharedFirebasePreferences

    private lateinit var userId: String

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(SharedFirebasePreferencesContextWrapper(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        userId = FirebaseAuth.getInstance().uid.toString()

        FirebaseAuth.getInstance().addAuthStateListener(this)
//        sharedPrefs = SharedFirebasePreferences.getDefaultInstance(this)
//        sharedPrefs.omitKeys("name")
//        Log.i(TAG, "46 true $userId ${sharedPrefs.all}")
//        sharedPrefs.keepSynced(true)

//        if (savedInstanceState == null) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.settings, SettingsFragment(sharedPrefs))
//            .commitAllowingStateLoss()
//        }


        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tbSettingsToolbar: MaterialToolbar =
            findViewById(com.example.sharedcalendar.R.id.tbSettingsToolbar)
        tbSettingsToolbar.setNavigationOnClickListener {
            if (this::sharedPrefs.isInitialized) {
                Log.i(TAG, "61 false $userId ${sharedPrefs.all}")
                sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
            }
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
//        if (FirebaseAuth.getInstance().uid != userId) {
//            sharedPrefs = SharedFirebasePreferences.getDefaultInstance(this)
//            sharedPrefs.omitKeys("name")
//        }
        // Set up a listener whenever a key changes
        if (this::sharedPrefs.isInitialized) {
            Log.i(TAG, "75 true $userId ${sharedPrefs.all}")
            sharedPrefs.keepSynced(true)
        }
    }

    override fun onResume() {
        super.onResume()
//        if (FirebaseAuth.getInstance().uid != userId) {
//            sharedPrefs = SharedFirebasePreferences.getDefaultInstance(this)
//            sharedPrefs.omitKeys("name")
//        }
        if (this::sharedPrefs.isInitialized) {
            Log.i(TAG, "85 true $userId ${sharedPrefs.all}")
            sharedPrefs.keepSynced(true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::sharedPrefs.isInitialized) {
            Log.i(TAG, "91 false $userId ${sharedPrefs.all}")
            sharedPrefs.keepSynced(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::sharedPrefs.isInitialized) {
            Log.i(TAG, "97 false $userId ${sharedPrefs.all}")
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
        }
    }

    class SettingsFragment(private val sharedPrefs: SharedFirebasePreferences) :
        PreferenceFragmentCompat() {
        private val firebaseViewModel by viewModels<FirebaseViewModel>()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val calendarPrefs = sharedPrefs.all.filterKeys { it.contains("calendar|") }
            firebaseViewModel.getUserShares()
            firebaseViewModel.userShares.observe(this) {
                firebaseViewModel.getCalendars(calendarPrefs)
            }

            val context = preferenceManager.context
            val screen = preferenceManager.preferenceScreen

            val calendarCategory = PreferenceCategory(context)
            calendarCategory.key = "calendars"
            calendarCategory.title = "Show Calendars"
            screen.addPreference(calendarCategory)

            // Listen for Event Updates
            firebaseViewModel.calendars.observe(this) { calendars ->
                // Set Default Calendar Preference Values
                val lpDefaultCalendar: ListPreference? = findPreference("default_calendar")
                val defaultCalendarEntries: Array<CharSequence>? =
                    calendars?.map { c -> c.name }?.toTypedArray()
                val defaultCalendarValues: Array<CharSequence>? =
                    calendars?.map { c -> c.id.toString() }?.toTypedArray()

                lpDefaultCalendar?.entries = defaultCalendarEntries
                lpDefaultCalendar?.entryValues = defaultCalendarValues
                if (!defaultCalendarValues.isNullOrEmpty() &&
                    sharedPrefs.getString(
                        "default_calendar",
                        null
                    ) == null
                ) {
                    lpDefaultCalendar?.setDefaultValue(calendars.filter { c -> c.isDefault }[0].id.toString())
                }

                calendarCategory.removeAll()
                // Allow enabling calendars
                for (calendar in calendars) {
                    val calendarPreference = CheckBoxPreference(context)
                    calendarPreference.key = "calendar|${calendar.id.toString()}"
                    calendarPreference.title = calendar.name
                    calendarPreference.isChecked =
                        sharedPrefs.getBoolean("calendar|${calendar.id.toString()}", true)
                    if (calendar.ownerId != Firebase.auth.currentUser?.uid) {
                        calendarPreference.summary =
                            calendar.owner?.name ?: "Shared by another user"
                    }

                    calendarCategory.addPreference(calendarPreference)
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?, key: String?
    ) {
//        recreate()
//        showView()

        // Update Changed Settings in Database
        userViewModel.updateUserSettings(sharedPreferences, key)
    }

//    private fun showView() {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.settings, SettingsFragment())
//            .commitAllowingStateLoss()
////        findViewById<View>(com.example.sharedcalendar.R.id.progessBar).visibility = View.GONE
//    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        Log.i(TAG, "Changed $firebaseAuth ${firebaseAuth.currentUser?.uid}")
        if (firebaseAuth.currentUser != null) {
            sharedPrefs = SharedFirebasePreferences.getDefaultInstance(this)
            sharedPrefs.keepSynced(true)
            sharedPrefs.registerOnSharedPreferenceChangeListener(this)
//            sharedPrefs.omitKeys("name")
            sharedPrefs.edit().putString("name", firebaseAuth.currentUser!!.displayName).apply()
            Log.i(TAG, "Shared Prefs auth state changed ${sharedPrefs.all}")
            sharedPrefs.pull().addOnPullCompleteListener(object :
                SharedFirebasePreferences.OnPullCompleteListener {
                override fun onPullSucceeded(preferences: SharedFirebasePreferences) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.settings, SettingsFragment(sharedPrefs))
                        .commitAllowingStateLoss()
//                    showView()
//                    recreate()
                }

                override fun onPullFailed(e: Exception) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.settings, SettingsFragment(sharedPrefs))
                        .commitAllowingStateLoss()
//                    showView()
//                    recreate()
                    Toast.makeText(this@SettingsActivity, "Fetch failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}