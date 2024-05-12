package com.example.sharedcalendar

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.alamkanak.weekview.WeekViewEntity
import com.alamkanak.weekview.jsr310.WeekViewSimpleAdapterJsr310
import com.alamkanak.weekview.jsr310.setEndTime
import com.alamkanak.weekview.jsr310.setStartTime
import com.example.sharedcalendar.models.Event

class WeekViewSimpleAdapter(private val fragmentManager: FragmentManager) :
    WeekViewSimpleAdapterJsr310<Event>() {
    // Reference API: https://github.com/thellmund/Android-Week-View/wiki/Public-API
    override fun onCreateEntity(item: Event): WeekViewEntity {
        Log.d("WeekViewSimpleAdapter", "Adding $item to Week View")
        // Setup each event passed to adapter
        val entity = WeekViewEntity.Event.Builder(item).setId(item.longId).setTitle(item.title)
            .setStartTime(item.startTime).setEndTime(item.endTime).setAllDay(item.isAllDay)


        if (item.color.isNotEmpty()) {
            val entityStyle =
                WeekViewEntity.Style.Builder().setBackgroundColor(Color.parseColor(item.color))
                    .build()
            entity.setStyle(entityStyle)

        }
        item.description?.let { entity.setSubtitle(it) }

        return entity.build()
    }

    override fun onEventClick(data: Event) {
        super.onEventClick(data)

        val newFragment =
            DayViewFragment(data.startTime.toLocalDate()) // Replace "YourNewFragment" with the fragment you want to navigate to
        fragmentManager.beginTransaction()
            .replace(
                R.id.flFragment,
                newFragment
            ) // Replace "R.id.fragmentContainer" with the ID of the container where you want to replace the fragment
            .addToBackStack(null)
            .commit()

    }

}