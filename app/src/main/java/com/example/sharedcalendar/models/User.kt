package com.example.sharedcalendar.models

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.util.Date

data class User(
    @SerializedName("id")
    var id: Int,
    var username: String,
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("email_verified_at")
    var emailVerifiedAt: String? = null,
    @SerializedName("settings")
    var settings: Any? = null, // FIX: try to define type
    @SerializedName("created_at")
    var createdAt: Date? = null,
    @SerializedName("updated_at")
    var updatedAt: Date? = null
)

data class UserSettings(
    @SerializedName("default_view")
    var defaultView: String?, // or ENUM?
    @SerializedName("default_calendar")
    var defaultCalendar: String?, // or ENUM?
    @SerializedName("default_timezone")
    var defaultTimezone: String?, // or ENUM?
    @SerializedName("default_reminder")
    var defaultReminder: String?, // or ENUM?
)