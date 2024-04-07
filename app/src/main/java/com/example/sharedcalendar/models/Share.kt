package com.example.sharedcalendar.models


enum class SharePermission {
    READ, WRITE, SHARE, AVAILABILITY
}

data class Share(
    var id: Int = 0,
    var calendarId: Int,
    var accountId: Int,
    var permission: SharePermission? = SharePermission.READ
)
