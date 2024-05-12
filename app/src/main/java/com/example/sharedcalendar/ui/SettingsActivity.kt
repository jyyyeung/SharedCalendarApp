package com.example.sharedcalendar.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.MainActivity
import com.example.sharedcalendar.R
import com.example.sharedcalendar.ui.login.LoginViewModelFactory
import com.example.sharedcalendar.ui.login.UserViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferencesContextWrapper


class SettingsFragment(private val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!) :
    PreferenceFragmentCompat() {
    //    private val firebaseViewModel by viewModels<FirebaseViewModel>()
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var prefs: SharedPreferences
    private var isConfigChanged = false

    companion object {
        private val TAG: String? = SettingsFragment::class.java.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "Finish Updating Settings")
        val calendarPrefs = prefs.all?.filterKeys { it.contains("${user.uid}|calendar|") }
        if (!calendarPrefs.isNullOrEmpty()) {
            firebaseViewModel.getCalendars(calendarPrefs, true)
        }
        if (isConfigChanged) {
            Toast.makeText(activity, "Config Changed ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        Log.i(TAG, prefs.all.toString())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]


        val context = preferenceManager.context
        val screen = preferenceManager.preferenceScreen

        val lpDefaultView: ListPreference? = findPreference("calendar_view")
        lpDefaultView?.key = "${user.uid}|default|view"

        val calendarCategory = PreferenceCategory(context)
        calendarCategory.key = "calendars"
        calendarCategory.title = "Show Calendars"
        screen.addPreference(calendarCategory)

        val lpDefaultCalendar: ListPreference? = findPreference("default_calendar")
        lpDefaultCalendar?.key = "${user.uid}|default|calendar"
        lpDefaultCalendar?.setDefaultValue("month")

        // Listen for Event Updates
        firebaseViewModel.calendars.observe(this) { calendars ->
            // Set Default Calendar Preference Values
            val defaultCalendarEntries: Array<CharSequence>? =
                calendars?.map { c -> c.name }?.toTypedArray()
            val defaultCalendarValues: Array<CharSequence>? =
                calendars?.map { c -> c.id.toString() }?.toTypedArray()

            lpDefaultCalendar?.entries = defaultCalendarEntries
            lpDefaultCalendar?.entryValues = defaultCalendarValues
            if (!defaultCalendarValues.isNullOrEmpty() && prefs.getString(
                    "${user.uid}|default|calendar", null
                ) == null
            ) {
                prefs.edit().putString(
                    "${user.uid}|default|calendar",
                    calendars.filter { c -> c.isDefault }[0].id.toString()
                ).apply()
//                lpDefaultCalendar?.value = calendars.filter { c -> c.isDefault }[0].id.toString()
            }
            lpDefaultCalendar?.value = prefs.getString(
                "${user.uid}|default|calendar",
                null
            ) ?: when (calendars.filter { c -> c.isDefault }.size) {
                0 -> null
                else -> calendars.filter { c -> c.isDefault }[0].id.toString()
            }

//            defaultCategory.addPreference(lpDefaultCalendar)

            calendarCategory.removeAll()
            // Allow enabling calendars
            for (calendar in calendars) {
                val calendarPreference = CheckBoxPreference(context)
                calendarPreference.key = "${user.uid}|calendar|${calendar.id.toString()}"
                calendarPreference.title = calendar.name
                calendarPreference.isChecked =
                    prefs.getBoolean("${user.uid}|calendar|${calendar.id.toString()}", true)
                if (calendar.ownerId != Firebase.auth.currentUser?.uid) {
                    calendarPreference.summary = calendar.owner?.name ?: "Shared by another user"
                }

                calendarCategory.addPreference(calendarPreference)
            }
        }
    }
}