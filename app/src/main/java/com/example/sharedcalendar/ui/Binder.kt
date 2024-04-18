package com.example.sharedcalendar.ui

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth

open class ViewContainer(val view: View)

/**
 * Responsible for managing a view for a given [Data].
 * [create] will be called only once but [bind] will
 * be called whenever the view needs to be used to
 * bind an instance of the associated [Data].
 */
interface Binder<Data, Container : ViewContainer> {
    fun create(view: View): Container
    fun bind(container: Container, data: Data)
}

//interface WeekDayBinder<Container : ViewContainer> : Binder<WeekDay, Container>

//interface WeekHeaderFooterBinder<Container : ViewContainer> : Binder<Week, Container>

/**
 * Bind a day View to a Month View?
 */
interface MonthDayBinder<Container : ViewContainer> : Binder<CalendarDay, Container>

interface MonthHeaderFooterBinder<Container : ViewContainer> : Binder<CalendarMonth, Container>

typealias MonthScrollListener = (CalendarMonth) -> Unit

//typealias WeekScrollListener = (Week) -> Unit
