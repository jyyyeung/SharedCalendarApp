package com.example.sharedcalendar.models

data class Calendar(
    var id: Int = 0,
    var name: String? = null,
    var color: String? = null,
    var timezone: String? = null,
    var owner_id: Int = 0
)

class GetAllCalendarsResponse(
    var calendars: List<Calendar>,
    var status: Boolean
)
