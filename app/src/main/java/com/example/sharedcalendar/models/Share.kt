package com.example.sharedcalendar.models

data class Share(
    var id: Int = 0,
    var calendarId: Int,
    var accountId: Int,
    var permission: SharePermission? = SharePermission.READ,
)

enum class SharePermission {
    READ,
    WRITE,
    SHARE,
    AVAILABILITY,
}
