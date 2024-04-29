package com.example.sharedcalendar.models

import androidx.annotation.ColorRes
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.YearMonth

data class Event(
    var id: Long = 0,
    var calendarId: Int,
    var title: String,
    var description: String? = null,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
    var location: String? = null,
    var timezone: String,
    @ColorRes val color: Int,
    var isAllDay: Boolean? = false,
    var isPrivate: Boolean? = false,
    var participants: JSONObject? = null,
)

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
