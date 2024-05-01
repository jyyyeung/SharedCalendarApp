package com.example.sharedcalendar.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.sharedcalendar.EventViewModel
import com.example.sharedcalendar.R
import com.example.sharedcalendar.data.SessionManager
import com.example.sharedcalendar.data.UserDataSource
import com.example.sharedcalendar.data.UserRepository
import com.example.sharedcalendar.ui.login.LoginViewModelFactory
import com.example.sharedcalendar.ui.login.UserViewModel
import com.google.android.material.appbar.MaterialToolbar
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferencesContextWrapper

private val TAG: String = SettingsActivity::class.java.name


class SettingsActivity : AppCompatActivity(),
//    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel
    private lateinit var sessionManager: SessionManager
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

        userRepository = UserRepository(
            dataSource = UserDataSource()
        )
        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
        sessionManager = SessionManager(this)

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
//        PreferenceManager.getDefaultSharedPreferences(this)
//            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        sharedPrefs.keepSynced(true)
        // Set up a listener whenever a key changes
//        PreferenceManager.getDefaultSharedPreferences(this)
//            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPrefs.keepSynced(false)
        // Unregister the listener whenever a key changes
//        PreferenceManager.getDefaultSharedPreferences(this)
//            ?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPrefs.keepSynced(false)
//        PreferenceManager.getDefaultSharedPreferences(this)
//            ?.unregisterOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val viewModel by viewModels<EventViewModel>()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            viewModel.getCalendars()


            // Listen for Event Updates
            viewModel.calendars.observe(this) { calendars ->

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

            }
        }

    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?, key: String?
    ) {
        recreate()

        // Update Changed Settings in Database
//        userViewModel.updateUserSettings(sessionManager, sharedPreferences, key)
    }

}