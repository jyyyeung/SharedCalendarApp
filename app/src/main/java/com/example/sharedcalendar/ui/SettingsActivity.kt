package com.example.sharedcalendar.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.sharedcalendar.R
import com.example.sharedcalendar.data.SessionManager
import com.example.sharedcalendar.data.UserDataSource
import com.example.sharedcalendar.data.UserRepository
import com.example.sharedcalendar.ui.login.LoginViewModelFactory
import com.example.sharedcalendar.ui.login.UserViewModel

private val TAG: String = SettingsActivity::class.java.name


class SettingsActivity : AppCompatActivity(),
//    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment())
                .commit()
        }

        userRepository = UserRepository(
            dataSource = UserDataSource()
        )
        userViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
        sessionManager = SessionManager(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        // Set up a listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this)
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        // Set up a listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this)
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this)
            ?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            ?.unregisterOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?, key: String?
    ) {
        recreate()

        // Update Changed Settings in Database
        userViewModel.updateUserSettings(sessionManager, sharedPreferences, key)
    }

}