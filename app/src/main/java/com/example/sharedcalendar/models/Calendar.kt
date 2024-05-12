package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude

/**
 * Represents a calendar.
 *
 * @param id The ID of the calendar.
 * @param name The name of the calendar.
 * @param color The color of the calendar.
 * @param timezone The timezone of the calendar.
 * @param ownerId The ID of the owner of the calendar.
 * @param owner The owner of the calendar.
 * @param events The events in the calendar.
 * @param scope The scope of the calendar.
 * @param isDefault Whether the calendar is the default calendar.
 * @param description The description of the calendar.
 */
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
    var scope: String? = "Full",
    @field:JvmField var isDefault: Boolean = false,
    var description: String = ""
)
