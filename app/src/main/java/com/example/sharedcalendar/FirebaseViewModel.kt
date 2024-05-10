package com.example.sharedcalendar


//import java.time.LocalDateTime
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class FirebaseViewModel(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore
) : ViewModel() {
    companion object {
        private val TAG: String = FirebaseViewModel::class.java.name
    }

    private val _events = MutableLiveData<MutableList<Event>>()
    var events: LiveData<MutableList<Event>> = _events

    private val _userCalendars = MutableLiveData<MutableList<Calendar>>()
    var userCalendars: LiveData<MutableList<Calendar>> = _userCalendars

    private val _sharedCalendars = MutableLiveData<MutableList<Calendar>>()
    var sharedCalendars: LiveData<MutableList<Calendar>> = _sharedCalendars

//    fun getStaticCalendars(): List<Calendar> {
//        Log.i(TAG, _userCalendars.value.toString())
//        Log.i(TAG, _sharedCalendars.value.toString())
//        val calendarsList = mutableListOf<Calendar>()
//        _userCalendars.value?.let { calendarsList.addAll(it) }
//        _sharedCalendars.value?.let { calendarsList.addAll(it) }
//        return calendarsList.toList()
//    }

//    val calendars:MutableLiveData<List<Calendar>> = MutableLiveData<List<Calendar>>()

    val calendars: LiveData<MutableList<Calendar>> =
        MediatorLiveData<MutableList<Calendar>>().apply {
//            val observer = Observer<MutableList<Calendar>> {
//                if (it.isNotEmpty()) {
//                    value = _sharedCalendars + _userCalendars
//                }
//            }
            addSource(_userCalendars) { value = _sharedCalendars + it }
            addSource(_sharedCalendars) { value = _userCalendars + it }
        }

    private val _userShares = MutableLiveData<MutableList<Share>>()
    var userShares: LiveData<MutableList<Share>> = _userShares

    val user = auth.currentUser!!
    var calendarsSharedWithUser: MutableList<String> = mutableListOf<String>()

    private val _users = MutableLiveData<MutableMap<String, User>>()
    var users: LiveData<MutableMap<String, User>> = _users

    private val _shares = MutableLiveData(mutableMapOf<String, ArrayList<Share>>())
    var shares: LiveData<MutableMap<String, ArrayList<Share>>> = _shares

    private val _enabledCalendars = MutableLiveData<MutableList<String>>()
    var enabledCalendars: LiveData<MutableList<String>> = _enabledCalendars

//    private val db = Firebase.firestore


    fun createCalendar(
        uid: String,
        sharedPrefs: SharedPreferences,
        calendar: Calendar
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // Add a new document with a generated ID
            db.collection("calendars").add(calendar).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // Set Default calendar in preferences to created calendar
                sharedPrefs.edit().putString("${uid}|default|calendar", documentReference.id)
                    .apply()
                sharedPrefs.edit().putBoolean("${uid}|calendar|${documentReference.id}", true)
                    .apply()
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        }
    }

    fun addShare(newShare: Share) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("shares").add(newShare).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        }
    }

    fun editShare(shareId: String, editedFields: HashMap<String, String?>) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("shares").document(shareId).set(editedFields, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }.addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    fun createShare(newShare: Share) {

//        val newShare = hashMapOf(
//            "calendarId" to calendarId,
//            "userEmail" to userEmail,
//            "scope" to scope
//        )

        db.collection("shares")
            .where(
                Filter.and(
                    Filter.equalTo("calendarId", newShare.calendarId),
                    Filter.equalTo("userEmail", newShare.userEmail)
                )
            ).get().addOnSuccessListener { results ->

                // Delete existing share
//                for (result in results.toObjects(Share::class.java))
//                    deleteShare(result)

                Log.i(TAG, results.toString())
                if (results.isEmpty) {
//                    val newShare = hashMapOf(
//                        "calendarId" to calendarId,
//                        "userEmail" to userEmail,
//                        "scope" to scope
//                    db.collection("shares").add(newShare).addOnSuccessListener {
//                        Log.d(TAG, "DocumentSnapshot successfully written!")
//                    }.addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                    addShare(newShare)
                    // Share does not exist
                }

//                }
                else {
                    val share = results.documents[0]
                    val editedFields: HashMap<String, String?> =
                        hashMapOf("scope" to newShare.scope)
                    editShare(share.id, editedFields)
////
//                    db.collection("shares").document(share.id)
//                        .set(
//                            hashMapOf("scope" to newShare.scope),
//                            SetOptions.merge()
//                        ).addOnSuccessListener {
//                            Log.d(TAG, "DocumentSnapshot successfully written!")
//                        }.addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                }
//
                getShares(calendars.value!!)

            }
    }

    fun addEventToCalendar(newEvent: HashMap<String, Any?>) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("events").add(newEvent).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                documentReference.get().addOnSuccessListener { result ->
                    val event = result.toObject(Event::class.java)
                    if (event is Event) {
                        event.id = result.id
                        event.startTime =
                            LocalDateTime.parse(result.get("startTimestamp").toString())
                        event.endTime = LocalDateTime.parse(result.get("endTimestamp").toString())

                        calendars.value?.get(0)?.events?.add(event)
                        Log.i(TAG, calendars.toString())

                    }
                    getCurrentMonthEvents()

                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        }

    }

    fun editCalendar(calendarId: String, editedFields: HashMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "editCalendar() $calendarId $editedFields")

//            val db = Firebase.firestore
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

//            val db = Firebase.firestore

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

    fun getUserShares(forceRefresh: Boolean = false) {
//        if(!forceRefresh && userShares.isInitialized)
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getUserShares()")
            val shares = ArrayList<Share>()
            val shareCalendarIds = ArrayList<String>()
            db.collection("shares").whereEqualTo("userEmail", user.email).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val share = document.toObject(Share::class.java)
                        share.id = document.id
                        shares.add(share)
                        shareCalendarIds.add(share.calendarId)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    Log.i(TAG, calendarsSharedWithUser.toString())
                    _userShares.postValue(shares)
                    calendarsSharedWithUser = shareCalendarIds
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting shares.", exception)
                }
        }
    }

    private fun isCalendarEnabled(calendarPrefs: Map<String, Any?>, calendarId: String): Boolean {
        val calendarKey = "${FirebaseAuth.getInstance().uid}|calendar|${calendarId}"
        Log.i(TAG, "Check isCalendarEnabled $calendarKey")
        return !calendarPrefs.containsKey(calendarKey) || calendarPrefs.getValue(
            calendarKey
        ) == true
    }

    private var isUserCalendarFetched = false
    private var isSharedCalendarFetched = false
    fun getCalendars(calendarPrefs: Map<String, Any?>, forceRefresh: Boolean = false) {
        if (isUserCalendarFetched && isSharedCalendarFetched && !forceRefresh) return
        isUserCalendarFetched = true
        isSharedCalendarFetched = true
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getCalendars()")
//            val db = Firebase.firestore

            val calendars1 = ArrayList<Calendar>()
            val calendarsEnabled = ArrayList<String>()
            db.collection("calendars")
                .whereEqualTo("ownerId", user.uid).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val calendar = document.toObject(Calendar::class.java)
                        calendar.id = document.id
                        calendar.owner = null
                        calendar.scope = "Full"
                        calendar.events = getEventsByCalendar(document.id, "Full")
                        // Check if calendar is enabled in preferences
                        if (isCalendarEnabled(calendarPrefs, document.id))
                            calendarsEnabled.add(document.id)

                        calendars1.add(calendar)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    _userCalendars.postValue(calendars1)
                    _enabledCalendars.postValue(calendarsEnabled)
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

            val calendars2 = ArrayList<Calendar>()
            if (calendarsSharedWithUser.isNotEmpty()) {
                Log.d(TAG, "Getting calendar shares $calendarsSharedWithUser")
                db.collection("calendars").whereIn(FieldPath.documentId(), calendarsSharedWithUser)
                    .get().addOnSuccessListener { documents ->
                        Log.d(TAG, "calendars2 size ${documents.size()}")
                        for (document in documents) {
                            val calendar = document.toObject(Calendar::class.java)
                            calendar.id = document.id
                            calendar.owner = getUserById(calendar.ownerId)
                            val scope =
                                userShares.value?.filter { c -> c.calendarId == document.id }
                                    ?.toTypedArray()?.get(0)?.scope
                            calendar.events = getEventsByCalendar(document.id, scope)
                            calendar.scope = scope

                            // Check if calendar is enabled in preferences
                            if (isCalendarEnabled(calendarPrefs, document.id)) {
                                calendarsEnabled.add(document.id)
                            }

                            Log.d(TAG, "Shared with me: ${document.id} => ${document.data}")
                            calendars2.add(calendar)
                        }
                        _sharedCalendars.postValue(calendars2)
                        _enabledCalendars.postValue(calendarsEnabled)
                    }
            }
        }
    }

    fun getCurrentMonthEvents(currentMonthOnly: Boolean = false) {
        Log.d(TAG, "getCurrentMonthEvents")
        viewModelScope.launch(Dispatchers.IO) {

            val calendarEvents = ArrayList<Event>()
//            val db = Firebase.firestore
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
                }
            }
        }
    }


    operator fun <T> MutableLiveData<MutableList<T>>.plus(list: MutableList<T>): MutableList<T> {
        val newList = mutableListOf<T>()
        this.value?.let { newList.addAll(it) }
        newList.addAll(list)
        return newList
    }

    operator fun <T> MutableLiveData<MutableMap<String, T>>.set(key: String, item: T) {
        val value = this.value ?: mutableMapOf()
        value[key] = item
        this.value = value
    }

    fun getUserById(
        userId: String
    ): User? {
        if (users.value?.get(userId) != null) {
            return users.value?.get(userId)
        }
        var user: User? = null
//        val db = Firebase.firestore
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("users").document(userId).get().addOnSuccessListener { result ->
                user = result.toObject(User::class.java)
                user?.id = result.id
                if (user != null) _users[userId] = user!!
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting user with specified ID. ", exception)
            }
        }
        return user
    }

    fun getEventsByCalendar(
        calendarId: String, scope: String?, currentMonthOnly: Boolean = false
    ): ArrayList<Event> {
        val eventArrayList: ArrayList<Event> = ArrayList()
//        val db = Firebase.firestore

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
        if (events.value == null) {
            return null
        }
        return buildList {
            for (event in events.value!!) {
                if (enabledCalendars.value?.contains(event.calendarId) == true)
                    currentMonth.atDay(event.startTime.dayOfMonth).also {
                        add(event)
                    }
            }
        }.groupBy { it.startTime.toLocalDate() }
    }

    fun deleteShare(share: Share) {
        val shareId = share.id

        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "deleteCalendar()")

//            val db = Firebase.firestore

            db.collection("shares").document(shareId).delete().addOnSuccessListener {
                Log.i(TAG, "share deleted")

                calendars.value?.let { it1 -> getShares(it1) }

            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error deleting Share.", exception)
            }
        }
    }
//
//    fun updatePreferences(sharedPreferences: SharedPreferences?) {
//
//        viewModelScope.launch(Dispatchers.IO) {
//
//
//
//        }
//    }

}

