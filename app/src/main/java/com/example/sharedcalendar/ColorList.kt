package com.example.sharedcalendar

class ColorList {
    val default: Color = colors()[0]

    fun colorPosition(color: Color): Int {
        for (i in colors().indices) {
            if (color == colors()[i])
                return i
        }
        return 0
    }

    fun colors(): List<Color> {
        return listOf(
            Color("Tomato", "#D50101"),
            Color("Sage", "#33B679"),
            Color("Peacock", "#7886CB"),
            Color("Graphite", "#616161")

        )
    }
}