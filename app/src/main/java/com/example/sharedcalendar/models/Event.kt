package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude
import java.time.LocalDateTime

/**
 * Represents an event.
 *
 * @param id The ID of the event.
 * @param calendarId The ID of the calendar that the event belongs to.
 * @param title The title of the event.
 * @param description The description of the event.
 * @param startTimestamp The start timestamp of the event.
 * @param startTime The start time of the event.
 * @param endTimestamp The end timestamp of the event.
 * @param endTime The end time of the event.
 * @param location The location of the event.
 * @param timezone The timezone of the event.
 * @param color The color of the event.
 * @param scope The scope of the event.
 * @param isAllDay Whether the event is an all-day event.
 * @param isPrivate Whether the event is private.
 * @param participants The participants of the event.
 * @param longId The long ID of the event.
 */
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
    @Exclude
    var scope: String = "View",
    @field:JvmField var isAllDay: Boolean = false,
    @field:JvmField var isPrivate: Boolean? = false,
    var participants: List<String>? = null,
    var longId: Long = 0,
)