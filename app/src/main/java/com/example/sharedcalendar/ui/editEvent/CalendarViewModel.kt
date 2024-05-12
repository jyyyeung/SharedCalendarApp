package com.example.sharedcalendar.ui.editEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.models.Event
import java.time.LocalDate
import java.util.Calendar

/**
 * ViewModel for the login screen.
 * @constructor Creates a new instance of [CalendarViewModel].
 */
class CalendarViewModel : ViewModel() {
    companion object {
        private val TAG: String = CalendarViewModel::class.java.name
    }

    private val _selectedDate = MutableLiveData<Calendar>()
    val selectedDate: LiveData<Calendar> = _selectedDate

    /**
     * Manage Calendar Screen state for this order
     */
    private val _editEvent = MutableLiveData<Event>()
    val editEvent: LiveData<Event> = _editEvent

    private val _viewMode = MutableLiveData<String>()
    val viewMode: LiveData<String> = _viewMode

    fun changeViewMode(viewMode: String) {
        Log.d(TAG, "changeViewMode: viewMode: $viewMode")
        _viewMode.value = viewMode
    }

    fun setEditEvent(event: Event) {
        _editEvent.value = event
    }

    fun setSelectedDate(selectedDate: LocalDate) {
        _selectedDate.value =
            Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedDate.year)
                set(Calendar.MONTH, selectedDate.monthValue - 1)
                set(Calendar.DAY_OF_MONTH, selectedDate.dayOfMonth)
            }
    }
}
