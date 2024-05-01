package com.example.sharedcalendar


//import java.time.LocalDateTime
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class EventViewModel : ViewModel() {
    companion object {
        private val TAG: String = EventViewModel::class.java.name
    }

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _calendars = MutableLiveData<List<Calendar>>()
    val calendars: LiveData<List<Calendar>> = _calendars

    private val _eventsByCalendar = MutableLiveData<List<Event>>()
    val eventsByCalendar: LiveData<List<Event>> = _eventsByCalendar

    val user = Firebase.auth.currentUser!!

    fun getCalendars(): ArrayList<Calendar> {
        val calendars = ArrayList<Calendar>()
        val db = Firebase.firestore
        db.collection("calendars").whereEqualTo("owner_id", user.uid).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val calendar = document.toObject(Calendar::class.java)
                    calendar.id = document.id

                    calendar.events = getEventsByCalendar(document.id)

                    calendars.add(calendar)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                _calendars.postValue(calendars)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return calendars
    }

    fun getCurrentMonthEvents(currentMonthOnly: Boolean = false) {
//        getCalendars()
//        val calendars = ArrayList<Calendar>()
        val calendarEvents = ArrayList<Event>()

        calendarEvents.add(
            Event(
                "0",
                "0",
                "Hangout",
                description = null,
                startTime = LocalDateTime.of(2024, 5, 3, 14, 0),
                endTime = LocalDateTime.of(2024, 5, 3, 15, 0),
                location = null,
                timezone = "Null",
                color = "#F44336",
                isAllDay = false,
                isPrivate = false,
                participants = null
            )
        )
        calendarEvents.add(
            Event(
                "1",
                "0",
                "Hangout",
                null,
                startTime = LocalDateTime.of(2024, 5, 2, 21, 0),
                endTime = LocalDateTime.of(2024, 5, 2, 22, 0),
                location = null,
                timezone = "Null",
                color = "#D2E3FC",
                isAllDay = false,
                isPrivate = false,
                participants = null
            )
        )

        calendarEvents.add(
            Event(
                "2",
                "0",
                "Lesson",
                null,
                startTime = LocalDateTime.of(2024, 5, 1, 13, 0),
                endTime = LocalDateTime.of(2024, 5, 1, 20, 0),
                location = null,
                timezone = "Null",
                color = "#008489",
                isAllDay = false,
                isPrivate = false,
                participants = null
            )
        )

        val db = Firebase.firestore
        db.collection("calendars").whereEqualTo("owner_id", user.uid).get()
            .addOnSuccessListener { result ->
                for (calendarDocument in result) {
                    db.collection("events").whereEqualTo("calendarId", calendarDocument.id).get()
                        .addOnSuccessListener { result1 ->
                            for (document1 in result1) {
                                val event = document1.toObject(Event::class.java)

                                event.id = document1.id
                                event.startTime =
                                    LocalDateTime.parse(document1.get("startTimestamp").toString())
                                event.endTime =
                                    LocalDateTime.parse(document1.get("endTimestamp").toString())
                                if (event.color.isEmpty() && calendarDocument.data.get("color")
                                        .toString().isNotEmpty()
                                ) {
                                    event.color = calendarDocument.data.get("color").toString()
                                }
                                calendarEvents.add(event)

//                                Log.wtf(TAG, "${document1.id} => $event => $calendarEvents")
                                _events.postValue(calendarEvents.toList())

                            }
                        }.addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting documents.", exception)
                        }
//                    calendarEvents.addAll(events)
//                    Log.i(TAG, "${document.id} => $events=> $calendarEvents")

                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        if (currentMonthOnly) {
            val currentMonth = YearMonth.now()
            // TODO: Query only events from this month
        }
        // TODO: Get from Database instead of local
//        val calendarEvents = listOf<Event>(
//            Event(
//                "0",
//                "0",
//                "Hangout",
//                description = null,
//                startTime = LocalDateTime.of(2024, 4, 27, 14, 0),
//                endTime = LocalDateTime.of(2024, 4, 27, 15, 0),
//                location = null,
//                timezone = "Null",
//                color = "#F44336",
//                isAllDay = false,
//                isPrivate = false,
//                participants = null
//            ),
//            Event(
//                "1", "0", "Hangout", null, startTime = LocalDateTime.of(2024, 4, 27, 21, 0),
//                endTime = LocalDateTime.of(2024, 4, 27, 22, 0),
//                location = null, timezone = "Null",
//                color = "#D2E3FC",
//                isAllDay = false, isPrivate = false, participants = null
//            ),
//
//            Event(
//                "2", "0", "Lesson", null, startTime = LocalDateTime.of(2024, 4, 29, 13, 0),
//                endTime = LocalDateTime.of(2024, 4, 29, 20, 0),
//                location = null, timezone = "Null",
//                color = "#008489",
//                isAllDay = false, isPrivate = false, participants = null
//            )
//
//        )

    }


    fun getEvents(): ArrayList<Event> {
        val events = ArrayList<Event>()
        val db = Firebase.firestore
        db.collection("events")
//            .whereEqualTo("owner_id", user.uid)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val event = document.toObject(Event::class.java)

                    event.id = document.id
                    event.startTime = LocalDateTime.parse(document.get("startTimestamp").toString())
                    event.endTime = LocalDateTime.parse(document.get("endTimestamp").toString())

                    events.add(event)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return events
    }

    private fun getEventsByCalendar(
        calendarId: String, currentMonthOnly: Boolean = false
    ): ArrayList<Event> {
        val eventArrayList: ArrayList<Event> = ArrayList()
        val db = Firebase.firestore

//        if (currentMonthOnly) {
//            val currentMonth = YearMonth.now()
//            //  Query only events from this month
//        }
        db.collection("events").whereEqualTo("calendarId", calendarId).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val event = document.toObject(Event::class.java)

                    event.id = document.id
                    event.startTime = LocalDateTime.parse(document.get("startTimestamp").toString())
                    event.endTime = LocalDateTime.parse(document.get("endTimestamp").toString())
                    eventArrayList.add(event)

                    Log.wtf(TAG, "${document.id} => $event => $eventArrayList")

                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        // TODO: event array list returns before on successListener is called
        Log.wtf(TAG, eventArrayList.toString())
        return eventArrayList
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

