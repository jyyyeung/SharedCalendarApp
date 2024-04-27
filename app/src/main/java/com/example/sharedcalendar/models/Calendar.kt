package com.example.sharedcalendar.models

import com.google.gson.annotations.SerializedName

data class Calendar(
    var name: String = "default",
    var color: String? = null,
    var timezone: String? = null,
    @SerializedName("owner_id")
    var ownerId: String = "",
    var shares: MutableMap<String, String>? = HashMap(),
    var events: ArrayList<Event?> = ArrayList()
)
