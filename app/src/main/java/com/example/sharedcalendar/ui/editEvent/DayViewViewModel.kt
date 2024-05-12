package com.example.sharedcalendar.ui.editEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.models.Event

/**
 * ViewModel for the login screen.
 * @constructor Creates a new instance of [DayViewViewModel].
 */
class DayViewViewModel : ViewModel() {

    companion object {
        private val TAG: String = DayViewViewModel::class.java.name
    }


    /**
     * Manage Calendar Screen state for this order
     */
    private val _editEvent = MutableLiveData<Event>()
    val editEvent: LiveData<Event> = _editEvent


    fun setEditEvent(event: Event) {
        _editEvent.value = event
    }
}