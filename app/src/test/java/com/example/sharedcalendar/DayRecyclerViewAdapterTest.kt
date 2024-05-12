package com.example.sharedcalendar

import com.example.sharedcalendar.models.Event
import org.junit.Assert.assertEquals
import org.junit.Test

class DayRecyclerViewAdapterTest {

    // Adapter should display the correct number of events in the list
    @Test
    fun should_display_correct_number_of_events() {
        // Arrange
        val eventList = arrayListOf(
            Event(title = "Event 1"),
            Event(title = "Event 2"),
            Event(title = "Event 3")
        )
        val adapter = DayRecyclerViewAdapter(eventList)

        // Act
        val itemCount = adapter.itemCount

        // Assert
        assertEquals(3, itemCount)
    }

    // Adapter should handle empty event list gracefully
    @Test
    fun should_handle_empty_event_list_gracefully() {
        // Arrange
        val eventList = arrayListOf<Event>()
        val adapter = DayRecyclerViewAdapter(eventList)

        // Act
        val itemCount = adapter.itemCount

        // Assert
        assertEquals(0, itemCount)
    }


}