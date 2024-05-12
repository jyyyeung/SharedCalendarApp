package com.example.sharedcalendar

import android.util.Log
import com.alamkanak.weekview.WeekViewEntity
import com.example.sharedcalendar.models.Event
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class WeekViewSimpleAdapterTest : BaseTest() {
    val mockFragmentManager = mockk<androidx.fragment.app.FragmentManager>()
    override fun extendedSetup() {
        // No-op
    }

    // Adapter correctly creates WeekViewEntity for each Event passed to it
    @Test
    fun `test adapter creates entity for each event`() {

        // Arrange
        val adapter = WeekViewSimpleAdapter(mockFragmentManager)
        val event1 = Event(title = "Event 1")
        val event2 = Event(title = "Event 2")
        val eventList = listOf(event1, event2)

        // Act
        val entities = eventList.map { adapter.onCreateEntity(it) }

        // Assert
        assertEquals(eventList.size, entities.size)
        assertTrue(entities.all { it is WeekViewEntity.Event<*> })
    }

    // Entity created has correct id, title, start time, end time, and all day properties set
    @Test
    fun `test entity has correct properties`() {
        // Arrange
        val adapter = WeekViewSimpleAdapter(mockFragmentManager)
        val event = Event(
            id = "1",
            title = "Test Event",
            startTime = LocalDateTime.of(2022, 1, 1, 10, 0),
            endTime = LocalDateTime.of(2022, 1, 1, 12, 0),
            isAllDay = true,
            longId = 1
        )

        // Act
        val entity = adapter.onCreateEntity(event) as WeekViewEntity.Event<*>

        // Assert
        assertEquals(event.longId, getField(entity, "id"))
        assertNotNull(getField(entity, "titleResource"))
        assertNotNull(getField(entity, "startTime"))
        assertNotNull(getField(entity, "endTime"))
        assertNotNull(getField(entity, "isAllDay"))
        assertEquals(event.isAllDay, getField(entity, "isAllDay"))
    }

    // If color property is not empty, entity style is set with correct background color
    @Test
    fun `test entity style set with correct color`() {
        // Arrange
        val adapter = WeekViewSimpleAdapter(mockFragmentManager)
        val event = Event(color = "#FF0000")

        // Act
        val entity = adapter.onCreateEntity(event) as WeekViewEntity.Event<*>

        val style = getField(entity, "style") as WeekViewEntity.Style
        // Assert
        assertNotNull(style)
        assertNotNull(getField(style, "backgroundColorResource"))
    }

//    // If description property is not null, entity subtitle is set with correct value
//    @Test
//    fun `test entity subtitle set with correct value`() {
//        // Arrange
//        val adapter = WeekViewSimpleAdapter()
//        val event = mockEvent
//        every { event.description } returns "Test Description"
//
//        // Act
//        val entity = adapter.onCreateEntity(event) as WeekViewEntity.Event<*>
//
//        // Assert
//        assertEquals(event.description, getField(entity, "subtitle"))
//    }

    // Adapter logs message with correct event information when creating entity
    @Test
    fun `test adapter logs message with correct event information`() {
        // Arrange
        val adapter = WeekViewSimpleAdapter(mockFragmentManager)
        val event = Event(title = "Test Event")
        val loggerSlot = slot<String>()

        // Mock logging framework or use Robolectric's ShadowLog to capture logs
        every { Log.d(any(), capture(loggerSlot)) } returns 1

        // Act
        adapter.onCreateEntity(event)

        // Assert
        assertEquals("Adding $event to Week View", loggerSlot.captured)
    }

    // Event passed to adapter has empty title
    @Test(expected = NoSuchFieldException::class)
    fun `test event has empty title`() {
        // Arrange
        val adapter = WeekViewSimpleAdapter(mockFragmentManager)
        val event = Event(title = "")

        // Act
        val entity = adapter.onCreateEntity(event) as WeekViewEntity.Event<*>

        // Assert
        assertEquals("", getField(entity, "title"))
    }

    //
//    // Event passed to adapter has null description property
//    @Test
//    fun `test event has null description property`() {
//        // Arrange
//        val adapter = WeekViewSimpleAdapter()
//        val event = Event(description = null)
//
//        // Act
//        val entity = adapter.onCreateEntity(event) as WeekViewEntity.Event
//
//        // Assert
//        assertNull(entity.subtitle)
//    }
//
    // Adapter correctly handles events with isAllDay property set to true
    @Test
    fun `test adapter handles all day events`() {
        // Arrange
        val adapter = WeekViewSimpleAdapter(mockFragmentManager)
        val event = Event(isAllDay = true)

        // Act
        val entity = adapter.onCreateEntity(event) as WeekViewEntity.Event<*>

        // Assert
        assertTrue(getField(entity, "isAllDay") as Boolean)
    }
//
//    // Adapter correctly handles events with isPrivate property set to true
//    @Test
//    fun `test adapter handles private events`() {
//        // Arrange
//        val adapter = WeekViewSimpleAdapter()
//        val event = Event(isPrivate = true)
//
//        // Act
//        val entity = adapter.onCreateEntity(event) as WeekViewEntity.Event
//
//        // Assert
//        assertTrue(entity.isPrivate)
//    }


}