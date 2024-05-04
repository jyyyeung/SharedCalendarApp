package com.example.sharedcalendar.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.sharedcalendar.models.Calendar

class CalendarPreviewParameterProvider : PreviewParameterProvider<Calendar> {

    override val values = sequenceOf(
        Calendar(
            "01",
            name = "Calendar01",
            color = "#008489",
            timezone = "Asia/Hong_Kong",
            ownerId = "01",
            scope = "Full"
        ),
        Calendar(
            "02",
            name = "Calendar02",
            color = "#0097A7",
            timezone = "Asia/Hong_Kong",
            ownerId = "02",
            scope = "View"
        ),
        Calendar(
            "03",
            name = "Calendar03",
            color = "#00796B",
            timezone = "Asia/Hong_Kong",
            ownerId = "03",
            scope = "View"
        ),
        Calendar(
            "04",
            name = "Calendar04",
            color = "#0097A7",
            timezone = "Asia/Hong_Kong",
            ownerId = "04",
            scope = "Availability"
        ),
    )


}
