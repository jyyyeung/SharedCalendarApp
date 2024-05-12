package com.example.sharedcalendar

import android.content.Context
import android.widget.Button
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import com.example.sharedcalendar.models.Event
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
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
    private lateinit var scenario: FragmentScenario<BottomSheetFragment>

    override fun extendedSetup() {
        MockKAnnotations.init(this)
//        every { sharedPreferences.getString(any(), any()) } returns "someDefaultCalendarId"
//        every { firebaseAuth.uid } returns "testUid"

        context = mockApplicationContext
        val activity =
            Robolectric.buildActivity(MainActivity::class.java).create().start().resume().get()
        fragment = BottomSheetFragment()

        activity.supportFragmentManager.beginTransaction().apply {
            add(
                fragment, "bottomSheetFragment"
            )
            commit()
        }

//        scenario = launchFragmentInContainer<BottomSheetFragment>(
//            themeResId = R.style.Base_Theme_SharedCalendar,
//        )


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
        scenario = launchFragmentInContainer<BottomSheetFragment>(
            themeResId = R.style.Base_Theme_SharedCalendar,
        )
        scenario.onFragment {
            it.onViewCreated(it.requireView(), null)

            setField(it, "prefs", mockSharedPrefs)
            setField(it, "firebaseViewModel", spykFirebaseViewModel)

            assertNotNull(it.timeText)
            assertNotNull(it.endDateText)
            assertNotNull(it.endTimeText)
            assertNotNull(it.swIsAllDay)

        }

    }

    @Test
    fun `onViewCreated updates events on viewModel events change`() {
        val events = MutableLiveData<MutableList<Event>>()
        every { spykFirebaseViewModel.events } returns events
        every { spykFirebaseViewModel.getGroupedEvents() } returns emptyMap()

//        fragment.onViewCreated(Robolectric.buildActivity(MainActivity::class.java).get(), null)
        val scenario = launchFragmentInContainer<BottomSheetFragment>(
            themeResId = R.style.Base_Theme_SharedCalendar,
        )
        scenario.onFragment {
            it.onViewCreated(it.requireView(), null)
            events.value = mutableListOf(mockk())

            setField(it, "prefs", mockSharedPrefs)
            setField(it, "firebaseViewModel", spykFirebaseViewModel)

            verify { spykFirebaseViewModel.getGroupedEvents() }
        }

    }

    @Test
    fun `dateCheck disables save button when dates and times are not valid`() {
        scenario = launchFragmentInContainer<BottomSheetFragment>(
            themeResId = R.style.Base_Theme_SharedCalendar,
        )
        scenario.onFragment {
            it.onViewCreated(it.requireView(), null)

            it.dateText.text = "Date"
            it.endDateText.text = "Date"
            it.timeText.text = "Time"
            it.endTimeText.text = "Time"

            it.checkDateIsValid()

            assertEquals(false, it.view?.findViewById<Button>(R.id.saveBtn)?.isEnabled)
        }
    }

    @Test
    fun `dateCheck enables save button when dates and times are valid`() {
//        fragment.onViewCreated(Robolectric.buildActivity(MainActivity::class.java).get(), null)
        val scenario = launchFragmentInContainer<BottomSheetFragment>(
            themeResId = R.style.Base_Theme_SharedCalendar,
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment {
            it.onViewCreated(it.requireView(), null)

            it.dateText.text = "2022.12.31"
            it.endDateText.text = "2023.01.01"
            it.timeText.text = "12:00"
            it.endTimeText.text = "12:00"

            it.checkDateIsValid()

            assertEquals(true, it.view?.findViewById<Button>(R.id.saveBtn)?.isEnabled)
        }
    }


}