package com.example.sharedcalendar.models

import org.json.JSONObject
import java.sql.Date

data class Event(
    var id: Int = 0,
    var calendarId: Int,
    var title: String,
    var description: String? = null,
    var startTime: Date,
    var endTime: Date,
    var location: String? = null,
    var timezone: String,
    var color: String? = null,
    var isAllDay: Boolean? = false,
    var isPrivate: Boolean? = false,
    var participants: JSONObject? = null
)


