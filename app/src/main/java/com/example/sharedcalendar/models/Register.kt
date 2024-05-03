package com.example.sharedcalendar.models

data class RegisterRequest(
    var username: String,
    var email: String,
    var password: String,
    var deviceName: String
)

data class RegisterResponse(
    var token: String,
    var user: User,
    var tokenType: String?,
    var status: Int?,
    var message: String?
)
