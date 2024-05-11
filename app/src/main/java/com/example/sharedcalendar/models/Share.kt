package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude

data class Share(
    @Exclude
    var id: String = "",
    var calendarId: String = "",
    var userEmail: String = "",
    var scope: String? = "View",
)

//enum class ShareScope {
//    Availability,
//    View,
//    Edit,
//    Full,
//}
