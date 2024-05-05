package com.example.sharedcalendar.ui

import androidx.lifecycle.ViewModel
import com.example.sharedcalendar.models.Calendar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ManageCalendarsViewModel : ViewModel() {

    /**
     * Manage Calendar Screen state for this order
     */
    private val _calendar = MutableStateFlow(Calendar())
    val calendar: StateFlow<Calendar> = _calendar.asStateFlow()

    private val db = Firebase.firestore

    fun setCalendar(calendar: Calendar) {
        _calendar.update { calendar }
    }


}