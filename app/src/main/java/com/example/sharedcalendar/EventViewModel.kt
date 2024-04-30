package com.example.sharedcalendar


//import java.time.LocalDateTime
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.models.Event
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class EventViewModel : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    fun getEvents(currentMonthOnly: Boolean = false) {
        if (currentMonthOnly) {
            val currentMonth = YearMonth.now()
            // TODO: Query only events from this month
        }
        // TODO: Get from Database instead of local
        val calendarEvents = listOf<Event>(
            Event(
                "0",
                "0",
                "Hangout",
                description = null,
                startTime = LocalDateTime.of(2024, 4, 27, 14, 0),
                endTime = LocalDateTime.of(2024, 4, 27, 15, 0),
                location = null,
                timezone = "Null",
                color = "#F44336",
                isAllDay = false,
                isPrivate = false,
                participants = null
            ),
            Event(
                "1", "0", "Hangout", null, startTime = LocalDateTime.of(2024, 4, 27, 21, 0),
                endTime = LocalDateTime.of(2024, 4, 27, 22, 0),
                location = null, timezone = "Null",
                color = "#D2E3FC",
                isAllDay = false, isPrivate = false, participants = null
            ),

            Event(
                "2", "0", "Lesson", null, startTime = LocalDateTime.of(2024, 4, 29, 13, 0),
                endTime = LocalDateTime.of(2024, 4, 29, 20, 0),
                location = null, timezone = "Null",
                color = "#008489",
                isAllDay = false, isPrivate = false, participants = null
            )

        )
        _events.postValue(calendarEvents)

    }

    fun getGroupedEvents(): Map<LocalDate, List<Event>>? {
        val currentMonth = YearMonth.now()
//        val events = getEvents()
        if (_events.value == null) {
            return null
        }
        return buildList {
            for (event in _events.value!!) {
                currentMonth.atDay(event.startTime.dayOfMonth).also { date ->
                    add(event)
                }
            }
        }.groupBy { it.startTime.toLocalDate() }
    }


}

