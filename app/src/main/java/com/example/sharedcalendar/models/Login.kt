package com.example.sharedcalendar.models


data class LoginRequest(
    var email: String, var password: String, var deviceName: String
)


data class LoginResponse(
    var token: String,
    var user: User,
    var tokenType: String?,
    var status: String?,
    var message: String?
)
