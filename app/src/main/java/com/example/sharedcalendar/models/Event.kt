package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.YearMonth

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
) {
//    var startTime
//        get() =
//            _startTime
//        set(value: String) {
//            _startTime = LocalDateTime.parse(value)
//        }
}

fun generateEvents(): List<Event> = buildList {
    val currentMonth = YearMonth.now()

//    currentMonth.atDay(17).also { date ->
//        add(
//            Event(
//                0, 0, "Hangout", null, date.atTime(14, 0),
//                date.atTime(15, 0), null, "Null", R.color.red_800,
//                false, false, null
//            )
//        )
//
//        add(
//            Event(
//                1, 0, "Hangout", null, date.atTime(21, 0),
//                date.atTime(22, 0), null, "Null", R.color.blue_800,
//                false, false, null
//            )
//        )
//    }
//    currentMonth.atDay(22).also { date ->
//        add(
//            Event(
//                2, 0, "Lesson", null, date.atTime(13, 0),
//                date.atTime(20, 0), null, "Null", R.color.brown_700,
//                false, false, null
//            )
//        )
//    }
}
