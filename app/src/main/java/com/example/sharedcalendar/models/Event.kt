package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude
import java.time.LocalDateTime

//@Serializable
data class Event(
    @Exclude var id: String = "0",
    var calendarId: String = "",
    var title: String = "",
    var description: String? = null,
    @Exclude
    var startTimestamp: String? = LocalDateTime.now().toString(),
    var startTime: LocalDateTime = LocalDateTime.now(),
    @Exclude
    var endTimestamp: String? = LocalDateTime.now().toString(),
    var endTime: LocalDateTime = LocalDateTime.now(),
    var location: String? = null,
    var timezone: String = "",
    var color: String = "#616161",
    @field:JvmField var isAllDay: Boolean? = false,
    @field:JvmField var isPrivate: Boolean? = false,
    var participants: List<String>? = null,
    var longId: Long = 0,
)