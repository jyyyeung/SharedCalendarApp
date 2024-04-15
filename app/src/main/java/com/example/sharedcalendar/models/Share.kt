package com.example.sharedcalendar.models

data class Share(
    var id: Int = 0,
    var calendarId: Int,
    var userId: Int,
    var permission: SharePermission? = SharePermission.READ,
)

enum class SharePermission {
    READ,
    WRITE,
    ADMIN,
    AVAILABILITY,
}
