package com.example.sharedcalendar.ui.login

/**
 * Authentication result : success (user details) or error message.
 * @property success User details data.
 * @property error Integer resource ID for the error message.
 * @constructor Creates a new instance of [LoginResult].
 * @see UserViewModel
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null,
)
