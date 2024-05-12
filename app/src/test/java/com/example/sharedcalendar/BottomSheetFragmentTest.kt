package com.example.sharedcalendar

import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.sharedcalendar.models.Event
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BottomSheetFragmentTest : BaseTest() {

    private lateinit var fragment: BottomSheetFragment
    private lateinit var context: Context

    override fun extendedSetup() {
        MockKAnnotations.init(this)
//        every { sharedPreferences.getString(any(), any()) } returns "someDefaultCalendarId"
//        every { firebaseAuth.uid } returns "testUid"

        context = mockApplicationContext
        val activity =
            Robolectric.buildActivity(MainActivity::class.java).create().start().resume().get()
        fragment = spyk(BottomSheetFragment())
        activity.supportFragmentManager.beginTransaction().apply {
            add(
                fragment, "bottomSheetFragment"
            )
            commit()
        }


        setField(fragment, "prefs", mockSharedPrefs)
        setField(fragment, "firebaseViewModel", spykFirebaseViewModel)
//        fragment.view = mockk(relaxed = true)
    }

    fun `when BottomSheetFragment is launched, then it is not null`() {
        assertNotNull(fragment)
    }

    fun `when BottomSheetFragment is launched, its view is not null`() {
        assertNotNull(fragment.view)
    }


    @Test
    fun `onViewCreated sets up date and time pickers`() {
        val events = MutableLiveData<List<Event>>()
//        every { viewModel.events } returns events
//        every { viewModel.getGroupedEvents() } returns emptyMap()

//        fragment.onViewCreated(Robolectric.buildActivity(MainActivity::class.java).get(), null)

        val dateText = getField<TextView>(fragment, "dateText")
        assertNotNull(dateText)
        assertNotNull(fragment.timeText)
        assertNotNull(fragment.endDateText)
        assertNotNull(fragment.endTimeText)
        assertNotNull(fragment.swIsAllDay)
    }

    @Test
    fun `onViewCreated updates events on viewModel events change`() {
        val events = MutableLiveData<MutableList<Event>>()
        every { spykFirebaseViewModel.events } returns events
        every { spykFirebaseViewModel.getGroupedEvents() } returns emptyMap()

//        fragment.onViewCreated(Robolectric.buildActivity(MainActivity::class.java).get(), null)

        events.value = mutableListOf(mockk())

        verify { spykFirebaseViewModel.getGroupedEvents() }
    }

    @Test
    fun `dateCheck disables save button when dates and times are not valid`() {
//        fragment.onViewCreated(Robolectric.buildActivity(MainActivity::class.java).get(), null)

        fragment.dateText.text = "Date"
        fragment.endDateText.text = "Date"
        fragment.timeText.text = "Time"
        fragment.endTimeText.text = "Time"

        fragment.checkDateIsValid()

        assertEquals(false, fragment.view?.findViewById<Button>(R.id.saveBtn)?.isEnabled)
    }

    @Test
    fun `dateCheck enables save button when dates and times are valid`() {
//        fragment.onViewCreated(Robolectric.buildActivity(MainActivity::class.java).get(), null)

        fragment.dateText.text = "2022.12.31"
        fragment.endDateText.text = "2023.01.01"
        fragment.timeText.text = "12:00"
        fragment.endTimeText.text = "12:00"

        fragment.checkDateIsValid()

        assertEquals(true, fragment.view?.findViewById<Button>(R.id.saveBtn)?.isEnabled)
    }


}