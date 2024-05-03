package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

//@Serializable
data class Event(
    @Exclude var id: String = "0",
    @SerializedName("calendar_id") var calendarId: String = "",
    var title: String = "",
    var description: String? = null,
    @Exclude
    @SerializedName("start_timestamp") var startTimestamp: String? = LocalDateTime.now().toString(),
    @SerializedName("start_time") var startTime: LocalDateTime = LocalDateTime.now(),
    @Exclude
    @SerializedName("end_timestamp") var endTimestamp: String? = LocalDateTime.now().toString(),
    @SerializedName("end_time") var endTime: LocalDateTime = LocalDateTime.now(),
    var location: String? = null,
    var timezone: String = "",
    var color: String = "#616161",
    @field:JvmField @SerializedName("is_all_day") var isAllDay: Boolean? = false,
    @field:JvmField @SerializedName("is_private") var isPrivate: Boolean? = false,
    var participants: List<String>? = null,
    @SerializedName("long_id") var longId: Long = 0,
)