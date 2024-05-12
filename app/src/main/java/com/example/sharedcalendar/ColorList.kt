package com.example.sharedcalendar

/**
 * Represents a list of colors.
 */
class ColorList {
    val default: Color = colors()[0]

    /**
     * Returns the position of the given color in the color list.
     *
     * @param color The color to find the position of.
     * @return The position of the color in the color list.
     */
    fun colorPosition(color: Color): Int {
        for (i in colors().indices) {
            if (color == colors()[i]) {
                return i
            }
        }
        return 0
    }

    /**
     * Returns a list of colors.
     *
     * @return the list of colors
     */
    fun colors(): List<Color> {
        return listOf(
            Color("Tomato", "#D50101"),
            Color("Sage", "#7AE7BF"),
            Color("Peacock", "#46D6DB"),
            Color("Graphite", "#E1E1E1"),
            Color("Basil", "#8FBC8F"),
            Color("Flamingo", "#FF887C"),
            Color("Banana", "#FBD75B"),
            Color("Tangerine", "#FFB878"),
            Color("Lavender", "#A4BDFC"),
            Color("Grape", "#DBADFF"),
            Color("Blueberry", "#5484ED"),
        )
    }
}