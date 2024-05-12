package com.example.sharedcalendar.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Fragment for the settings screen.
 *
 * @param user The current user.
 */
class SettingsFragment(private val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!) :
    PreferenceFragmentCompat() {
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var prefs: SharedPreferences
    private var isConfigChanged = false

    companion object {
        private val TAG: String? = SettingsFragment::class.java.name
    }

    /**
     * Called when the fragment is being destroyed.
     */
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

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?,
    ) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        val context = preferenceManager.context
        val screen = preferenceManager.preferenceScreen

        val lpDefaultView: ListPreference? = findPreference("calendar_view")
        lpDefaultView?.key = "${user.uid}|default|view"
        lpDefaultView?.setDefaultValue("month")
        lpDefaultView?.value = prefs.getString("${user.uid}|default|view", "month")

        val lpDefaultCalendar: ListPreference? = findPreference("default_calendar")
        lpDefaultCalendar?.key = "${user.uid}|default|calendar"

        val calendarCategory = PreferenceCategory(context)
        calendarCategory.key = "calendars"
        calendarCategory.title = "Show Calendars"
        screen.addPreference(calendarCategory)

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
                    "${user.uid}|default|calendar",
                    null,
                ) == null
            ) {
                prefs.edit().putString(
                    "${user.uid}|default|calendar",
                    calendars.filter { c -> c.isDefault }[0].id.toString(),
                ).apply()
//                lpDefaultCalendar?.value = calendars.filter { c -> c.isDefault }[0].id.toString()
            }
            lpDefaultCalendar?.value = prefs.getString(
                "${user.uid}|default|calendar",
                null,
            ) ?: when (calendars.filter { c -> c.isDefault }.size) {
                0 -> null
                else -> calendars.filter { c -> c.isDefault }[0].id.toString()
            }

            calendarCategory.removeAll()
            // Allow enabling calendars
            for (calendar in calendars) {
                val calendarPreference = CheckBoxPreference(context)
                calendarPreference.key = "${user.uid}|calendar|${calendar.id}"
                calendarPreference.title = calendar.name
                calendarPreference.isChecked =
                    prefs.getBoolean("${user.uid}|calendar|${calendar.id}", true)
                if (calendar.ownerId != Firebase.auth.currentUser?.uid) {
                    calendarPreference.summary = calendar.owner?.name ?: "Shared by another user"
                }

                calendarCategory.addPreference(calendarPreference)
            }
        }
    }
}
