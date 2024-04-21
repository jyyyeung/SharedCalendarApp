package com.example.sharedcalendar.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    var username: String,
    var email: String,
    var password: String,
    @SerializedName("device_name")
    var deviceName: String
)

data class RegisterResponse(
    var token: String,
    var user: User,
    @SerializedName("token_type")
    var tokenType: String?,
    var status: Int?,
    var message: String?
)
