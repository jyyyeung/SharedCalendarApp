package com.example.sharedcalendar

class ColorList {
    val default: Color = Colors()[0]

    fun colorPosition(color: Color): Int {
        for (i in Colors().indices) {
            if (color == Colors()[i])
                return i
        }
        return 0
    }

    fun Colors(): List<Color> {
        return listOf(
            Color("Tomato", "#D50101"),
            Color("Sage", "#33B679"),
            Color("Peacock", "#7886CB"),
            Color("Graphite", "#616161")

        )
    }
}