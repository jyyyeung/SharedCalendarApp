package com.example.sharedcalendar.ui.login

/**
 * User details post authentication that is exposed to the UI
 * @property displayName Display name of the user.
 */
data class LoggedInUserView(
    val displayName: String,
    // ... other data fields that may be accessible to the UI
)
