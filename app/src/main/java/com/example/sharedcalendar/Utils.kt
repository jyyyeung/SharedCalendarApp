package com.example.sharedcalendar

import androidx.compose.ui.graphics.Color


/**
 * Object to convert hex color strings to Jetpack Compose Color objects.
 */
object HexToJetpackColor {
    /**
     * Converts a hex color string to a Jetpack Compose Color object.
     *
     * @param colorString The hex color string to be converted.
     * @return The Jetpack Compose Color object.
     */
    fun getColor(colorString: String): Color {
        return Color(android.graphics.Color.parseColor(colorString))
    }
}