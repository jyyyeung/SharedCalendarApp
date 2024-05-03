package com.example.sharedcalendar


//import java.time.LocalDateTime
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class FirebaseViewModel : ViewModel() {
    companion object {
        private val TAG: String = FirebaseViewModel::class.java.name
    }

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events


    private val _userCalendars = MutableLiveData<List<Calendar>>()
    val userCalendars: LiveData<List<Calendar>> = _userCalendars

    private val _sharedCalendars = MutableLiveData<List<Calendar>>()
    val sharedCalendars: LiveData<List<Calendar>> = _sharedCalendars

    //    private val _calendars = MutableLiveData<List<Calendar>>()
    val calendars: LiveData<List<Calendar>> = MediatorLiveData<List<Calendar>>().apply {
        val observer = Observer<List<Calendar>> {
            val shared = sharedCalendars.value
            val owned = userCalendars.value
            if (shared?.isNotEmpty() == true && owned?.isNotEmpty() == true) {
                value = it
            }
        }
        addSource(userCalendars, observer)
        addSource(sharedCalendars, observer)
    }

    private val _eventsByCalendar = MutableLiveData<List<Event>>()
    val eventsByCalendar: LiveData<List<Event>> = _eventsByCalendar

    private val _userShares = MutableLiveData<List<Share>>()
    val userShares: LiveData<List<Share>> = _userShares

    val user = Firebase.auth.currentUser!!
    private val calendarsSharedWithUser = mutableListOf<String>()


    fun addEventToCalendar(event: Event?) {
        if (event is Event) {
            calendars.value?.get(0)?.events?.add(event)
            Log.i(TAG, calendars.toString())
        }
    }

    fun getUserShares() {
        Log.i(TAG, "getUserShares()")
        val shares = ArrayList<Share>()
        val db = Firebase.firestore
        db.collection("shares").whereEqualTo("userEmail", user.email).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val share = document.toObject(Share::class.java)
                    share.id = document.id
                    shares.add(share)
                    calendarsSharedWithUser.add(share.calendarId)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                Log.i(TAG, calendarsSharedWithUser.toString())
                _userShares.postValue(shares)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting shares.", exception)
            }
    }

    fun getCalendars() {
        Log.i(TAG, "getCalendars()")
        var calendars = ArrayList<Calendar>()
        val db = Firebase.firestore

        if (calendarsSharedWithUser.isNotEmpty()) {
            Log.i(TAG, "Getting calendar shares")
            db.collection("calendars").whereIn(FieldPath.documentId(), calendarsSharedWithUser)
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        val calendar = document.toObject(Calendar::class.java)
                        calendar.id = document.id
                        calendar.owner = getUserById(calendar.ownerId)
//                        calendar.events = getEventsByCalendar(document.id)

                        Log.d(TAG, "Shared with me: ${document.id} => ${document.data}")
                        calendars.add(calendar)
                    }
                    _userCalendars.postValue(calendars)
                }
        }
        calendars = ArrayList()

        db.collection("calendars").whereEqualTo("ownerId", user.uid).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val calendar = document.toObject(Calendar::class.java)
                    calendar.id = document.id
                    calendar.owner = null
                    calendar.events = getEventsByCalendar(document.id)

                    calendars.add(calendar)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                _sharedCalendars.postValue(calendars)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


//        return calendars
    }

    fun getCurrentMonthEvents(currentMonthOnly: Boolean = false) {
        Log.d(TAG, "getCurrentMonthEvents")
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
        var query: Query = db.collection("calendars").whereEqualTo("ownerId", user.uid)

        val calendarsSharedWithUser = _userShares.value?.map { s -> s.calendarId }?.toMutableList()
        if (calendarsSharedWithUser?.isNotEmpty() == true) {
            query = db.collection("calendars").where(
                Filter.or(
                    Filter.equalTo("ownerId", user.uid),
                    Filter.inArray(FieldPath.documentId(), calendarsSharedWithUser)
                )
            )
        }

        query.get().addOnSuccessListener { result ->
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
                            if (event.color.isEmpty() && calendarDocument.data["color"].toString()
                                    .isNotEmpty()
                            ) {
                                event.color = calendarDocument.data["color"].toString()
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

//        _events.postValue(calendarEvents.toList())

    }


    fun getEvents(): ArrayList<Event> {
        Log.i(TAG, "getEvents()")
        val eventsList = ArrayList<Event>()
        val db = Firebase.firestore
        db.collection("events")
//            .whereEqualTo("owner_id", user.uid)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val event = document.toObject(Event::class.java)

                    event.id = document.id
                    event.startTime = LocalDateTime.parse(document.get("startTimestamp").toString())
                    event.endTime = LocalDateTime.parse(document.get("endTimestamp").toString())

                    eventsList.add(event)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return eventsList
    }

    fun getUserById(
        userId: String
    ): User? {
        var user: User? = null
        val db = Firebase.firestore
        db.collection("users").document(userId).get().addOnSuccessListener { result ->
            user = result.toObject(User::class.java)
            user?.id = result.id
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting user with specified ID. ", exception)
        }
        return user
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

