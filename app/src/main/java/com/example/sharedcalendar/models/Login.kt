package com.example.sharedcalendar.models


data class LoginRequest (
    var email: String,
    var password: String
)
data class LoginResponse(
    var token: String,
    var status: String,
    var message: String
)
