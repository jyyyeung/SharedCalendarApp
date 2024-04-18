package com.example.sharedcalendar

import android.os.Build
import android.text.TextUtils

/**
 *  Returns the consumer friendly device name
 *  Taken from: https://stackoverflow.com/questions/1995439/get-android-phone-model-programmatically-how-to-get-device-name-and-model-prog/26117427#26117427
 *  */
fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        capitalize(model)
    } else capitalize(manufacturer) + " " + model
}


private fun capitalize(str: String): String {
    if (TextUtils.isEmpty(str)) {
        return str
    }
    val arr = str.toCharArray()
    var capitalizeNext = true
    var phrase = ""
    for (c in arr) {
        if (capitalizeNext && Character.isLetter(c)) {
            phrase += c.uppercaseChar()
            capitalizeNext = false
            continue
        } else if (Character.isWhitespace(c)) {
            capitalizeNext = true
        }
        phrase += c
    }
    return phrase
}