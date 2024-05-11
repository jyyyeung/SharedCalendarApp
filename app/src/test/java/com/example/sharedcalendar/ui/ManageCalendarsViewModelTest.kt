package com.example.sharedcalendar.ui

import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.BaseTest
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ManageCalendarsViewModelTest : BaseTest() {
    override fun extendedSetup() {
        // Set up test data
    }

    // Test that the ManageCalendarsViewModel is not null
    fun test_manageCalendarsViewModel_isNotNull() {
        assertNotNull(ManageCalendarsViewModel())
    }

    // Test that the ManageCalendarsViewModel is an instance of ViewModel
    fun test_manageCalendarsViewModel_isViewModel() {
        assertTrue(ManageCalendarsViewModel() is ViewModel)
    }


    // Setting a calendar updates the state flow with the new calendar object
    @Test
    fun setting_a_calendar_updates_the_state_flow_with_the_new_calendar_object() {
        // Arrange
        val viewModel = ManageCalendarsViewModel()
        val calendar = Calendar(name = "Test Calendar")

        // Act
        viewModel.setCalendar(calendar)

        // Assert
        assertEquals(calendar, viewModel.calendar.value)
    }

    // Getting the calendar state flow returns the current calendar object
    @Test
    fun getting_the_calendar_state_flow_returns_the_current_calendar_object() {
        // Arrange
        val viewModel = ManageCalendarsViewModel()
        val calendar = Calendar(name = "Test Calendar")
        viewModel.setCalendar(calendar)

        // Act
        val result = viewModel.calendar.value

        // Assert
        assertEquals(calendar, result)
    }

    // Updating the calendar state flow with the same calendar object does not trigger an update
    @Test
    fun updating_the_calendar_state_flow_with_the_same_calendar_object_does_not_trigger_an_update() {
        // Arrange
        val viewModel = ManageCalendarsViewModel()
        val calendar = Calendar(name = "Test Calendar")
        viewModel.setCalendar(calendar)

        // Act
        viewModel.setCalendar(calendar)

        // Assert
        assertEquals(calendar, viewModel.calendar.value)
    }

    // Adding an event to the calendar updates the calendar object
    @Test
    fun adding_an_event_to_the_calendar_updates_the_calendar_object() {
        // Arrange
        val viewModel = ManageCalendarsViewModel()
        val calendar = Calendar(name = "Test Calendar")
        viewModel.setCalendar(calendar)
        val event = Event(title = "Test Event")

        // Act
        calendar.events.add(event)

        // Assert
        assertEquals(calendar, viewModel.calendar.value)
    }

    // Removing an event from the calendar updates the calendar object
    @Test
    fun removing_an_event_from_the_calendar_updates_the_calendar_object() {
        // Arrange
        val viewModel = ManageCalendarsViewModel()
        val event = Event(title = "Test Event")
        val calendar = Calendar(name = "Test Calendar", events = arrayListOf(event))
        viewModel.setCalendar(calendar)

        // Act
        calendar.events.remove(event)

        // Assert
        assertEquals(calendar, viewModel.calendar.value)
    }

    // Changing the name of the calendar updates the calendar object
    @Test
    fun changing_the_name_of_the_calendar_updates_the_calendar_object() {
        // Arrange
        val viewModel = ManageCalendarsViewModel()
        val calendar = Calendar(name = "Test Calendar")
        viewModel.setCalendar(calendar)
        val newName = "New Calendar Name"

        // Act
        calendar.name = newName

        // Assert
        assertEquals(calendar, viewModel.calendar.value)
    }


}