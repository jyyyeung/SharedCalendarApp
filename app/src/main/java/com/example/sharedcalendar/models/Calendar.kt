package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude

data class Calendar(
    @Exclude var id: String? = null,
    var name: String = "default",
    var color: String? = null,
    var timezone: String? = null,
    var ownerId: String = "",
    @Exclude
    var owner: User? = null,
    var events: ArrayList<Event> = ArrayList(),
    @Exclude
    var scope: String? = "Full"
)
