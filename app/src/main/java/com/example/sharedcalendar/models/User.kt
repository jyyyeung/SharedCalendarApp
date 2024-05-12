package com.example.sharedcalendar.models

/**
 * Represents a user.
 *
 * @param id The ID of the user.
 * @param name The name of the user.
 * @param email The email of the user.
 */
data class User(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
)