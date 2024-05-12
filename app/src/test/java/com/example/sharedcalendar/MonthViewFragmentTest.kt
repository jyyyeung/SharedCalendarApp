package com.example.sharedcalendar

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import com.example.sharedcalendar.databinding.FragmentMonthViewBinding
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.displayText
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import java.time.LocalDate
import java.time.YearMonth

@RunWith(RobolectricTestRunner::class)
class MonthViewFragmentTest : BaseTest() {

    private lateinit var fragment: MonthViewFragment

    override fun extendedSetup() {
        val activity =
            Robolectric.buildActivity(MainActivity::class.java).create().start().resume().get()
        fragment = spyk(MonthViewFragment())
        activity.supportFragmentManager.beginTransaction().apply {
            add(fragment, "monthViewFragment")
            commitNowAllowingStateLoss()
        }
        Shadows.shadowOf(activity.mainLooper).idle()

        setField(fragment, "firebaseViewModel", spykFirebaseViewModel)
//        var localContext = fragment.requireContext()
        setField(fragment, "binding", FragmentMonthViewBinding.bind(fragment.view!!))

    }

    @Test
    fun `onViewCreated sets up calendar`() {
        val events = MutableLiveData<MutableList<Event>>()
        every { spykFirebaseViewModel.events } returns events
        every { spykFirebaseViewModel.getGroupedEvents() } returns emptyMap()


        fragment.view?.let { fragment.onViewCreated(it, null) }

        assertNotNull(fragment.binding.MonthViewCalendar.dayBinder)
        assertNotNull(fragment.binding.MonthViewCalendar.monthHeaderBinder)
    }

    @Test
    fun `onViewCreated updates events on viewModel events change`() {
        val events = MutableLiveData<MutableList<Event>>()
        every { spykFirebaseViewModel.events } returns events
        every { spykFirebaseViewModel.getGroupedEvents() } returns emptyMap()

        fragment.onViewCreated(fragment.view!!, null).apply {

            events.value = mutableListOf(mockk<Event>(relaxed = true))

            verify { spykFirebaseViewModel.getGroupedEvents() }

        }
    }

    @Test
    fun `onViewCreated updates events on viewModel events change with new events`() {
        val events = MutableLiveData<MutableList<Event>>()
        every { spykFirebaseViewModel.events } returns events
        every { spykFirebaseViewModel.getGroupedEvents() } returns emptyMap()

        fragment.onViewCreated(fragment.view!!, null).apply {

            events.value = mutableListOf(mockk<Event>(relaxed = true))

            verify { spykFirebaseViewModel.getGroupedEvents() }

        }
    }

    @Test
    fun `onViewCreated sets up calendar and observes events`() {
        val events = MutableLiveData<MutableList<Event>>()
        val groupedEvents = mapOf(LocalDate.now() to listOf(Event()))
        every { spykFirebaseViewModel.getGroupedEvents() } returns groupedEvents
        every { spykFirebaseViewModel.events } returns events

        launchFragmentInContainer<MonthViewFragment>()

        verify(exactly = 1) { spykFirebaseViewModel.getGroupedEvents() }
        verify(exactly = 1) { spykFirebaseViewModel.events }
    }

    @Test
    fun `onEventUpdate updates calendar with new events`() {
        val events = MutableLiveData<MutableList<Event>>()
        val groupedEvents = mapOf(LocalDate.now() to listOf(Event()))
        every { spykFirebaseViewModel.getGroupedEvents() } returns groupedEvents
        every { spykFirebaseViewModel.events } returns events

        launchFragmentInContainer<MonthViewFragment>()

        events.postValue(groupedEvents.values.flatten().toMutableList())

        verify(exactly = 1) { spykFirebaseViewModel.getGroupedEvents() }
        verify(exactly = 1) { spykFirebaseViewModel.events }
        verify(exactly = 1) { fragment.binding.MonthViewCalendar.notifyCalendarChanged() }
    }

    @Test
    fun test_month_view_displayed_with_current_month_and_year() {
        // Arrange
//        val fragment = spyk(MonthViewFragment())
        val mockViewModel = mockk<FirebaseViewModel>()
        fragment.firebaseViewModel = mockViewModel
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(200)
        val endMonth = currentMonth.plusMonths(200)
//        val mockView = mockk<View>()

        val mockView = fragment.view
        if (mockView == null) {
            assert(false)
        }

//        val binding = mockk<FragmentMonthViewBinding>(relaxed = true)
        val binding = FragmentMonthViewBinding.bind(mockView!!)
        every { binding.MonthViewCalendar } returns mockk(relaxed = true)
        every { binding.MonthViewCalendar.setup(startMonth, endMonth, any()) } just Runs
        every { binding.MonthViewCalendar.scrollToMonth(currentMonth) } just Runs
        every { binding.MonthYearNext.setOnClickListener(any()) } just Runs
        every { binding.MonthYearLast.setOnClickListener(any()) } just Runs
        every { binding.MonthViewCalendar.monthScrollListener = any() } just Runs
        every { binding.MonthYearText.text = any<String>() } just Runs
        every { binding.MonthViewCalendar.notifyDateChanged(any()) } just Runs
        every { binding.MonthViewCalendar.findFirstVisibleMonth() } returns null

        fragment.binding = binding

        // Act
        fragment.onViewCreated(mockView, mockk())

        // Assert
        verify { binding.MonthViewCalendar.setup(startMonth, endMonth, any()) }
        verify { binding.MonthViewCalendar.scrollToMonth(currentMonth) }
        verify { binding.MonthYearNext.setOnClickListener(any()) }
        verify { binding.MonthYearLast.setOnClickListener(any()) }
        verify { binding.MonthViewCalendar.monthScrollListener = any() }
        verify { binding.MonthYearText.text = currentMonth.displayText() }
    }
}