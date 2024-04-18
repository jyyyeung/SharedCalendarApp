package com.example.sharedcalendar

import java.io.Serializable
import java.time.LocalDate

    data class CalendarDay(val date: LocalDate, val position: DayPosition) : Serializable