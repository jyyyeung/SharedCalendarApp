package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude

/**
 * Share data class
 *
 * @property id
 * @property calendarId
 * @property userEmail
 * @property scope
 */
data class Share(
    @Exclude
    var id: String? = "",
    var calendarId: String = "",
    var userEmail: String = "",
    var scope: String? = "View",
)

