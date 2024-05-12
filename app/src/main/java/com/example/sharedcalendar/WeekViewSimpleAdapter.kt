package com.example.sharedcalendar

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.alamkanak.weekview.WeekViewEntity
import com.alamkanak.weekview.jsr310.WeekViewSimpleAdapterJsr310
import com.alamkanak.weekview.jsr310.setEndTime
import com.alamkanak.weekview.jsr310.setStartTime
import com.example.sharedcalendar.models.Event

/**
 * Week View Simple Adapter.
 *
 * This class extends the WeekViewSimpleAdapterJsr310 and is responsible for creating WeekViewEntity objects
 * from Event objects and handling event clicks.
 *
 * @property fragmentManager FragmentManager used for fragment transactions.
 * @constructor Create [WeekViewSimpleAdapter]
 */
class WeekViewSimpleAdapter(private val fragmentManager: FragmentManager) :
    WeekViewSimpleAdapterJsr310<Event>() {
    // Reference API: https://github.com/thellmund/Android-Week-View/wiki/Public-API

    /**
     * Creates a WeekViewEntity from an Event object.
     *
     * @param item The Event object to be converted.
     * @return The created WeekViewEntity.
     */
    override fun onCreateEntity(item: Event): WeekViewEntity {
        Log.d("WeekViewSimpleAdapter", "Adding $item to Week View")
        // Setup each event passed to adapter
        val entity = WeekViewEntity.Event.Builder(item).setId(item.longId).setTitle(item.title)
            .setStartTime(item.startTime).setEndTime(item.endTime).setAllDay(item.isAllDay)

        // Set color of event
        if (item.color.isNotEmpty()) {
            val entityStyle =
                WeekViewEntity.Style.Builder().setBackgroundColor(Color.parseColor(item.color))
                    .build()
            entity.setStyle(entityStyle)
        }
        // Set description of event
        item.description?.let { entity.setSubtitle(it) }

        return entity.build()
    }

    /**
     * Handles the event click.
     *
     * When an event is clicked, it navigates to the DayViewFragment for the date of the clicked event.
     *
     * @param data The clicked Event object.
     */
    override fun onEventClick(data: Event) {
        super.onEventClick(data)

        val newFragment =
            DayViewFragment(data.startTime.toLocalDate()) // Replace "YourNewFragment" with the fragment you want to navigate to
        fragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout_calendarFragment,
                newFragment
            ) // Replace "R.id.fragmentContainer" with the ID of the container where you want to replace the fragment
            .addToBackStack(null)
            .commit()
    }
}