package com.example.sharedcalendar.models

data class Calendar(
    var name: String = "default",
    var color: String? = null,
    var timezone: String? = null,
    var ownerId: String = "",
    var shares: Map<String, String>?,
    var events: ArrayList<Event>?
)
