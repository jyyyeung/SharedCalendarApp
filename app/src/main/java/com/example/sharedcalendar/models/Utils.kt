package com.example.sharedcalendar.models

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * Returns the text of the day of the week.
 *
 * @param uppercase Whether to return the text in uppercase.
 * @return The text of the day of the week.
 */
fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

/**
 * Returns the text of the month.
 *
 * @param short Whether to return the text in short form.
 * @return The text of the month.
 */
fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

/**
 * Returns the text of the month.
 *
 * @param short Whether to return the text in short form.
 * @return The text of the month.
 */
fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

/**
 * Returns the color of the resource.
 *
 * @param color The color resource.
 * @return The color of the resource.
 */
fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)

/**
 * Returns the week page title.
 * @param week The week.
 * @return The week page title.
 */
fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }

        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
        }

        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}