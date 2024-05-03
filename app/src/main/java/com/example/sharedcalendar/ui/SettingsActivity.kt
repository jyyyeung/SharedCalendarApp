package com.example.sharedcalendar.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferencesContextWrapper

private val TAG: String = SettingsActivity::class.java.name


class SettingsActivity : AppCompatActivity(),
//    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPrefs: SharedFirebasePreferences

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(SharedFirebasePreferencesContextWrapper(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment())
                .commit()
        }

        sharedPrefs =
            SharedFirebasePreferences.getInstance(this, "app_settings", Context.MODE_PRIVATE)
        sharedPrefs.omitKeys("name")

        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tbSettingsToolbar: MaterialToolbar = findViewById(R.id.tbSettingsToolbar)
        tbSettingsToolbar.setNavigationOnClickListener {
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        // Set up a listener whenever a key changes
        sharedPrefs.keepSynced(true)
    }

    override fun onResume() {
        super.onResume()
        sharedPrefs.keepSynced(true)
    }

    override fun onPause() {
        super.onPause()
        sharedPrefs.keepSynced(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPrefs.keepSynced(false)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val firebaseViewModel by viewModels<FirebaseViewModel>()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            firebaseViewModel.getCalendars()
            val context = preferenceManager.context
//            val calendarScreen = preferenceManager.createPreferenceScreen(context)
            val screen = preferenceManager.preferenceScreen


            // Listen for Event Updates
            firebaseViewModel.calendars.observe(this) { calendars ->

                // Set Default Calendar Preference Values
                val lpDefaultCalendar: ListPreference? =
                    findPreference<ListPreference>("default_calendar")
                val defaultCalendarEntries: Array<CharSequence>? =
                    calendars?.map { c -> c.name }?.toTypedArray()
                val defaultCalendarValues: Array<CharSequence>? =
                    calendars?.map { c -> c.id.toString() }?.toTypedArray()

                lpDefaultCalendar?.entries = defaultCalendarEntries
                lpDefaultCalendar?.entryValues = defaultCalendarValues
                if (defaultCalendarValues != null) {
                    if (defaultCalendarValues.isNotEmpty()) lpDefaultCalendar?.setDefaultValue(
                        calendars[0].id.toString()
                    )
                }

                val calendarCategory = PreferenceCategory(context)
                calendarCategory.key = "calendars"
                calendarCategory.title = "Show Calendars"
                screen.addPreference(calendarCategory)

                // Allow enabling calendars
                for (calendar in calendars) {
                    val calendarPreference = CheckBoxPreference(context)
                    calendarPreference.key = calendar.id.toString()
                    calendarPreference.title = calendar.name
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
        recreate()

        // Update Changed Settings in Database
        userViewModel.updateUserSettings(sharedPreferences, key)
    }

}