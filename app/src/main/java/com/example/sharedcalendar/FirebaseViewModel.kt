package com.example.sharedcalendar


//import java.time.LocalDateTime
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class FirebaseViewModel : ViewModel() {
    companion object {
        private val TAG: String = FirebaseViewModel::class.java.name
    }

    private val _events = MutableLiveData<MutableList<Event>>()
    val events: LiveData<MutableList<Event>> = _events


    private val _userCalendars = MutableLiveData<MutableList<Calendar>>()
    val userCalendars: LiveData<MutableList<Calendar>> = _userCalendars

    private val _sharedCalendars = MutableLiveData<MutableList<Calendar>>()
    val sharedCalendars: LiveData<MutableList<Calendar>> = _sharedCalendars

    //    private val _calendars = MutableLiveData<List<Calendar>>()
    val calendars: LiveData<MutableList<Calendar>> =
        MediatorLiveData<MutableList<Calendar>>().apply {
            val observer = Observer<MutableList<Calendar>> {
                if (it.isNotEmpty()) {
                    value = _sharedCalendars + _userCalendars
                }
            }
            addSource(userCalendars, observer)
            addSource(sharedCalendars, observer)
        }

    private val _eventsByCalendar = MutableLiveData<MutableList<Event>>()
    val eventsByCalendar: LiveData<MutableList<Event>> = _eventsByCalendar

    private val _userShares = MutableLiveData<MutableList<Share>>()
    val userShares: LiveData<MutableList<Share>> = _userShares

    val user = Firebase.auth.currentUser!!
    private val calendarsSharedWithUser = mutableListOf<String>()

    private val _users = MutableLiveData<MutableMap<String, User>>()
    val users: LiveData<MutableMap<String, User>> = _users

    private val _shares = MutableLiveData(mutableMapOf<String, ArrayList<Share>>())
    val shares: LiveData<MutableMap<String, ArrayList<Share>>> = _shares

    private val db = Firebase.firestore


    fun addEventToCalendar(event: Event?) {
        if (event is Event) {
            calendars.value?.get(0)?.events?.add(event)
            Log.i(TAG, calendars.toString())
        }
    }

    fun editCalendar(calendarId: String, editedFields: HashMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "editCalendar() $calendarId $editedFields")

            val db = Firebase.firestore
            db.collection("calendars").document(calendarId).set(editedFields, SetOptions.merge())
                .addOnSuccessListener {
                    Log.i(TAG, "Calendar updated")
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error updating calendar.", exception)
                }
        }
    }

    fun deleteCalendar(calendarId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "deleteCalendar()")

            val db = Firebase.firestore

            db.collection("calendars").document(calendarId).delete().addOnSuccessListener {
                Log.i(TAG, "Calendar deleted")
                db.collection("events").whereEqualTo("calendarId", calendarId).get()
                    .addOnSuccessListener { results ->
                        for (event in results.documents) {
                            db.collection("events").document(event.id).delete()
                        }
                    }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error deleting calendar.", exception)
            }
        }
    }

    fun getShares(calendars: List<Calendar>) {
        val newShares = shares.value
        viewModelScope.launch(Dispatchers.IO) {
            for (calendar in calendars) {
                val sharesList = arrayListOf<Share>()
                db.collection("shares").whereEqualTo("calendarId", calendar.id).get()
                    .addOnSuccessListener { result ->
                        for (share in result.documents) {
                            val shareEntry = share.toObject(Share::class.java)!!
                            shareEntry.id = share.id
                            sharesList.add(shareEntry)
                            Log.d(TAG, "${share.id} => ${share.data}")
                        }
                        newShares?.set(calendar.id.toString(), sharesList)
                        _shares.postValue(newShares)
                    }.addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting calendar shares.", exception)

                    }
                Log.d(TAG, newShares.toString())
            }

        }
    }

    init {
        getUserShares()
    }

    fun getUserShares() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getUserShares()")
            val shares = ArrayList<Share>()
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
    }

    fun getCalendars() {
        viewModelScope.launch(Dispatchers.IO) {

            Log.i(TAG, "getCalendars()")
            val db = Firebase.firestore

            val calendars1 = ArrayList<Calendar>()
            db.collection("calendars").whereEqualTo("ownerId", user.uid).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val calendar = document.toObject(Calendar::class.java)
                        calendar.id = document.id
                        calendar.owner = null
                        calendar.scope = "Full"
                        calendar.events = getEventsByCalendar(document.id, "Full")

                        calendars1.add(calendar)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    _userCalendars.postValue(calendars1)
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

            val calendars2 = ArrayList<Calendar>()
            if (calendarsSharedWithUser.isNotEmpty()) {
                Log.i(TAG, "Getting calendar shares")
                db.collection("calendars").whereIn(FieldPath.documentId(), calendarsSharedWithUser)
                    .get().addOnSuccessListener { result ->
                        for (document in result) {
                            val calendar = document.toObject(Calendar::class.java)
                            calendar.id = document.id
                            calendar.owner = getUserById(calendar.ownerId)
                            val scope =
                                userShares.value?.filter { c -> c.calendarId == document.id }
                                    ?.toTypedArray()?.get(0)?.scope
                            calendar.events = getEventsByCalendar(document.id, scope)
                            calendar.scope = scope

                            Log.d(TAG, "Shared with me: ${document.id} => ${document.data}")
                            calendars2.add(calendar)
                        }
                        _sharedCalendars.postValue(calendars2)
                    }
            }
        }
    }

    fun getCurrentMonthEvents(currentMonthOnly: Boolean = false) {
        Log.d(TAG, "getCurrentMonthEvents")
        viewModelScope.launch(Dispatchers.IO) {

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
            Log.i(TAG, "Calendars to find events for $calendars")
            if (calendars.value?.isNotEmpty() == true) {
                for (calendar in calendars.value!!) {
                    Log.i(TAG, "Finding Events for Calendar ${calendar.name}")
                    db.collection("events").whereEqualTo("calendarId", calendar.id).get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val event = document.toObject(Event::class.java)

                                event.id = document.id

                                if (calendar.scope == "Availability") {
                                    event.title = "Busy"
                                    event.description = ""
                                }

                                event.startTime =
                                    LocalDateTime.parse(document.get("startTimestamp").toString())
                                event.endTime =
                                    LocalDateTime.parse(document.get("endTimestamp").toString())
                                if (event.color.isEmpty() && calendar.color.toString()
                                        .isNotEmpty()
                                ) {
                                    event.color = calendar.color.toString()
                                }
                                calendarEvents.add(event)
                                _events.postValue(calendarEvents)
                            }
                        }.addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting documents.", exception)
                        }
                    //                    Log.i(TAG, "${document.id} => $events=> $calendarEvents")


                }
            }

            if (currentMonthOnly) {
                val currentMonth = YearMonth.now()
                // TODO: Query only events from this month
            }

//        _events.postValue(calendarEvents.toList())

        }
    }


    operator fun <T> MutableLiveData<MutableList<T>>.plus(list: MutableLiveData<MutableList<T>>): MutableList<T> {
        val newList = this.value ?: mutableListOf()
        list.value?.let { newList.addAll(it) }
        return newList
    }

    operator fun <T> MutableLiveData<MutableMap<String, T>>.set(key: String, item: T) {
        val value = this.value ?: mutableMapOf()
        value[key] = item
        this.value = value
    }

    private fun getUserById(
        userId: String
    ): User? {
        if (users.value?.get(userId) != null) {
            return users.value?.get(userId)
        }
        var user: User? = null
        val db = Firebase.firestore
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("users").document(userId).get().addOnSuccessListener { result ->
                user = result.toObject(User::class.java)
                user?.id = result.id
                if (user != null) _users[userId] = user!!
//            _users.postValue(users)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting user with specified ID. ", exception)
            }
        }
        return user
    }

    private fun getEventsByCalendar(
        calendarId: String, scope: String?, currentMonthOnly: Boolean = false
    ): ArrayList<Event> {
        val eventArrayList: ArrayList<Event> = ArrayList()
        val db = Firebase.firestore

//        if (currentMonthOnly) {
//            val currentMonth = YearMonth.now()
//            //  Query only events from this month
//        }
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("events").whereEqualTo("calendarId", calendarId).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val event = document.toObject(Event::class.java)

                        event.id = document.id

                        if (scope == "Availability") {
                            event.title = "Busy"
                            event.description = ""
                        }

                        event.startTime =
                            LocalDateTime.parse(document.get("startTimestamp").toString())
                        event.endTime = LocalDateTime.parse(document.get("endTimestamp").toString())
                        eventArrayList.add(event)

                        Log.wtf(TAG, "${document.id} => $event => $eventArrayList")
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
            // TODO: event array list returns before on successListener is called
            Log.wtf(TAG, eventArrayList.toString())
        }
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

