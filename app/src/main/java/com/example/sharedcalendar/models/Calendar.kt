package com.example.sharedcalendar.models

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName

data class Calendar(
    @Exclude var id: String? = null,
    var name: String = "default",
    var color: String? = null,
    var timezone: String? = null,
    @SerializedName("owner_id") var ownerId: String = "",
    @Exclude
    @SerializedName("owner") var owner: User? = null,
    var shares: MutableMap<String, String>? = HashMap(),
    var events: ArrayList<Event> = ArrayList()
)
