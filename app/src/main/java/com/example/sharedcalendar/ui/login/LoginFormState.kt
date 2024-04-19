package com.example.sharedcalendar.ui.login

/**
 * Data validation state of the login form.
 * @param emailError Integer resource ID for the email error, or null if there is no error.
 * @param passwordError Integer resource ID for the password error, or null if there is no error.
 * @param isDataValid If the data entered is valid.
 * @constructor Creates a new instance of [LoginFormState].
 * @see LoginViewModel
 */
data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)