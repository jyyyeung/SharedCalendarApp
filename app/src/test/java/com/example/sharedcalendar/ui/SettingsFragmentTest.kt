package com.example.sharedcalendar.ui

import android.content.SharedPreferences
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import com.example.sharedcalendar.BaseTest
import com.example.sharedcalendar.MainActivity
import com.example.sharedcalendar.models.Calendar
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class SettingsFragmentTest : BaseTest() {
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var prefs: SharedPreferences

    override fun extendedSetup() {
        MockKAnnotations.init(this)
        val activity =
            Robolectric.buildActivity(MainActivity::class.java).create().start().resume().get()

        settingsFragment = SettingsFragment(mockUser)

        activity.supportFragmentManager.beginTransaction().apply {
            add(settingsFragment, "settingsFragment")
            commit()
        }
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        prefs = PreferenceManager.getDefaultSharedPreferences(
            settingsFragment.requireContext()
        )
        setField(settingsFragment, "firebaseViewModel", spykFirebaseViewModel)
        setField(settingsFragment, "prefs", prefs)
    }

    @Test
    fun `onCreatePreferences sets correct default view key`() {
        settingsFragment.onCreatePreferences(null, null)

        val lpDefaultView =
            settingsFragment.findPreference<ListPreference>("${mockUser.uid}|default|view")
        assertNotNull(lpDefaultView)
    }

    @Test
    fun `onCreatePreferences sets correct default calendar key`() {
        settingsFragment.onCreatePreferences(null, null)

        val lpDefaultCalendar =
            settingsFragment.findPreference<ListPreference>("${mockUser.uid}|default|calendar")
        assertNotNull(lpDefaultCalendar)
    }

    @Test
    fun `onDestroyView updates user shares`() {
        every { spykFirebaseViewModel.getCalendars(any(), any()) } returns Unit
        prefs.edit().putBoolean("${mockUser.uid}|calendar|1", true).apply()

        settingsFragment.onDestroyView()

        verify { spykFirebaseViewModel.getCalendars(any(), true) }
    }

    @Test
    fun `onDestroyView does not update user shares when config is not changed`() {
//        settingsFragment.isConfigChanged = false
        prefs.edit().clear().apply()
        settingsFragment.onDestroyView()

        verify(exactly = 0) { spykFirebaseViewModel.getCalendars(any(), true) }
    }


    @Test
    fun `onCreate sets preferences`() {
        settingsFragment.onCreate(null)

        assertNotNull(prefs.all)
    }

    @Test
    fun `onCreate sets default view key`() {
        settingsFragment.onCreate(null)

        val lpDefaultView =
            settingsFragment.findPreference<ListPreference>("${mockUser.uid}|default|view")
        assertNotNull(lpDefaultView)
    }

    @Test
    fun `onCreate sets default calendar key`() {
        settingsFragment.onCreate(null)

        val lpDefaultCalendar =
            settingsFragment.findPreference<ListPreference>("${mockUser.uid}|default|calendar")
        assertNotNull(lpDefaultCalendar)
    }

    @Test
    fun `onCreate sets default calendar key values`() {
        settingsFragment.onCreate(null)

        val lpDefaultCalendar =
            settingsFragment.findPreference<ListPreference>("${mockUser.uid}|default|calendar")
        assertNotNull(lpDefaultCalendar?.entries)
        assertNotNull(lpDefaultCalendar?.entryValues)
    }


    @Test
    fun `onCreate sets default calendar key value to null when no default calendar is set and no calendars are available`() {
        prefs.edit().clear().apply()
        every { spykFirebaseViewModel.calendars.value } returns mutableListOf()
        settingsFragment.onCreate(null)

        val lpDefaultCalendar =
            settingsFragment.findPreference<ListPreference>("${mockUser.uid}|default|calendar")
        assertNull(lpDefaultCalendar?.value)
    }

    @Test
    fun `onCreate sets default calendar key value to null when no default calendar is set and no default calendars are available`() {
        prefs.edit().clear().apply()
        every { spykFirebaseViewModel.calendars.value } returns mutableListOf(
            mockCalendar.copy(
                isDefault = false
            )
        )
        settingsFragment.onCreate(null)

        val lpDefaultCalendar =
            settingsFragment.findPreference<ListPreference>("${mockUser.uid}|default|calendar")
        assertNull(lpDefaultCalendar?.value)
    }

}