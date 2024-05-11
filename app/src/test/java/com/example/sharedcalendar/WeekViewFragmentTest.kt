package com.example.sharedcalendar

import android.content.Context
import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEntity
import com.example.sharedcalendar.models.Event
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime
import com.example.sharedcalendar.WeekViewFragment
import com.example.sharedcalendar.models.Calendar
import io.mockk.core.ValueClassSupport.maybeUnboxValueForMethodReturn
import io.mockk.spyk
import org.robolectric.Robolectric
import org.robolectric.Shadows
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class WeekViewFragmentTest : BaseTest() {
    private lateinit var context: Context
    private lateinit var fragment: WeekViewFragment
    private lateinit var scenario: FragmentScenario<WeekViewFragment>
    private lateinit var fragmentScenario: FragmentScenario<WeekViewFragment>
    override fun extendedSetup() {
        context = mockApplicationContext
        val activity =
            Robolectric.buildActivity(MainActivity::class.java).create().start().resume().get()
        fragment = WeekViewFragment()
        activity.supportFragmentManager.beginTransaction().apply {
            add(
                fragment, "weekViewFragment"
            )
            commit()
        }

        scenario = launchFragmentInContainer<WeekViewFragment>()

        setField(fragment, "firebaseViewModel", spykFirebaseViewModel)
        fragmentScenario = FragmentScenario.launchInContainer(WeekViewFragment::class.java)

    }


    // WeekViewFragment is created successfully
    @Test
    fun test_week_view_fragment_created_successfully() {
        assertNotNull(fragment)
    }

    // WeekViewFragment's selectedDate is initialized to LocalDate.now()
    @Test
    fun test_week_view_fragment_selected_date_initialized() {
        val selectedDate = getField<LocalDate>(fragment, "selectedDate")
        assertEquals(LocalDate.now(), selectedDate)
    }

    // WeekViewFragment's weekView is initialized successfully
    @Test
    fun test_week_view_fragment_week_view_initialized() {
        val weekView = getField<WeekView>(fragment, "weekView")
        assertNotNull(weekView)
    }

    // WeekViewFragment's adapter is set successfully
    @Test
    fun test_week_view_fragment_adapter_set_successfully() {
        val weekView = getField<WeekView>(fragment, "weekView")
        val adapter = getField<WeekViewSimpleAdapter>(fragment, "adapter")
        assertNotNull(adapter)
    }

    // WeekViewFragment observes changes in firebaseViewModel.calendars successfully
    @Test
    fun test_week_view_fragment_observes_calendars_successfully() {
        assertNotNull(spykFirebaseViewModel.calendars)
    }

    // firebaseViewModel.calendars is null
    @Test
    fun test_firebase_view_model_calendars_null() {
        assertNull(spykFirebaseViewModel.calendars.value)
    }

    // firebaseViewModel.events is null
    @Test
    fun test_firebase_view_model_events_null() {
        assertNull(spykFirebaseViewModel.events.value)
    }

}
