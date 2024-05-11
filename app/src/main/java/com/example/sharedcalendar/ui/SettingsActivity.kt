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


private val TAG: String = SettingsActivity::class.java.name

class SettingsActivity : AppCompatActivity(),
//    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
//    FirebaseAuth.AuthStateListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPrefs: SharedFirebasePreferences

    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        userId = FirebaseAuth.getInstance().uid.toString()

//        FirebaseAuth.getInstance().addAuthStateListener(this)
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
//
//    override fun onStart() {
//        super.onStart()
////        if (FirebaseAuth.getInstance().uid != userId) {
////            sharedPrefs = SharedFirebasePreferences.getDefaultInstance(this)
////            sharedPrefs.omitKeys("name")
////        }
//        // Set up a listener whenever a key changes
//        if (this::sharedPrefs.isInitialized) {
//            Log.i(TAG, "75 true $userId ${sharedPrefs.all}")
//            sharedPrefs.keepSynced(true)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
////        if (FirebaseAuth.getInstance().uid != userId) {
////            sharedPrefs = SharedFirebasePreferences.getDefaultInstance(this)
////            sharedPrefs.omitKeys("name")
////        }
//        if (this::sharedPrefs.isInitialized) {
//            Log.i(TAG, "85 true $userId ${sharedPrefs.all}")
//            sharedPrefs.keepSynced(true)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (this::sharedPrefs.isInitialized) {
//            Log.i(TAG, "91 false $userId ${sharedPrefs.all}")
//            sharedPrefs.keepSynced(false)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (this::sharedPrefs.isInitialized) {
//            Log.i(TAG, "97 false $userId ${sharedPrefs.all}")
//            sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
//        }
//    }
//

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?, key: String?
    ) {
//        recreate()
//        showView()

        // Update Changed Settings in Database
//        userViewModel.updateUserSettings(sharedPreferences, key)
    }

}

//class SettingsFragment(private val sharedPrefs: SharedFirebasePreferences) :
class SettingsFragment(private val user: FirebaseUser) :
    PreferenceFragmentCompat() {
    //    private val firebaseViewModel by viewModels<FirebaseViewModel>()
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var prefs: SharedPreferences
    private var isConfigChanged = false

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "Finish Updating Settings")
        val calendarPrefs =
            prefs.all?.filterKeys { it.contains("${user.uid}|calendar|") }
        if (calendarPrefs != null) {
            firebaseViewModel.getCalendars(calendarPrefs, true)
        }
        if (isConfigChanged) {
            Toast.makeText(activity, "Config Changed ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs =
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
        Log.i(TAG, prefs.all.toString())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

//        val calendarPrefs = sharedPrefs.all.filterKeys { it.contains("calendar|") }
//        firebaseViewModel.getUserShares()
//        firebaseViewModel.userShares.observe(this) {
//            firebaseViewModel.getCalendars(calendarPrefs)
//        }

        val context = preferenceManager.context
        val screen = preferenceManager.preferenceScreen

//        val defaultCategory = PreferenceCategory(context)
//        defaultCategory.key = "default"
//        defaultCategory.title = "Default Values"
//        screen.addPreference(defaultCategory)

        val lpDefaultView: ListPreference? = findPreference("calendar_view")
//        lpDefaultView?.setDefaultValue("month")
//        lpDefaultView?.entries = resources.getStringArray(R.array.calendar_view_entries)
//        lpDefaultView.entryValues = resources.getStringArray(R.array.calendar_view_values)
//        lpDefaultView.title = resources.getString(R.string.pref_calendar_view_title)
        lpDefaultView?.key = "${user.uid}|default|view"
//        defaultCategory.addPreference(lpDefaultView)

        val calendarCategory = PreferenceCategory(context)
        calendarCategory.key = "calendars"
        calendarCategory.title = "Show Calendars"
        screen.addPreference(calendarCategory)

        val lpDefaultCalendar: ListPreference? = findPreference("default_calendar")
        lpDefaultCalendar?.key = "${user.uid}|default|calendar"
        lpDefaultCalendar?.setDefaultValue("month")

//        screen.onPreferenceChangeListener = Preference.OnPreferenceChangeListener()

        // Listen for Event Updates
        firebaseViewModel.calendars.observe(this) { calendars ->
            // Set Default Calendar Preference Values
            val defaultCalendarEntries: Array<CharSequence>? =
                calendars?.map { c -> c.name }?.toTypedArray()
            val defaultCalendarValues: Array<CharSequence>? =
                calendars?.map { c -> c.id.toString() }?.toTypedArray()

            lpDefaultCalendar?.entries = defaultCalendarEntries
            lpDefaultCalendar?.entryValues = defaultCalendarValues
            if (!defaultCalendarValues.isNullOrEmpty() &&
                prefs.getString(
                    "${user.uid}|default|calendar",
                    null
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
                calendars.filter { c -> c.isDefault }[0].id.toString()
            )

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
                    calendarPreference.summary =
                        calendar.owner?.name ?: "Shared by another user"
                }

                calendarCategory.addPreference(calendarPreference)
            }
        }
    }
}