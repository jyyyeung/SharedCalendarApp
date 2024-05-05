package com.example.sharedcalendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEntity
import com.alamkanak.weekview.jsr310.WeekViewSimpleAdapterJsr310
import com.alamkanak.weekview.jsr310.setEndTime
import com.alamkanak.weekview.jsr310.setStartTime
import com.example.sharedcalendar.databinding.FragmentWeekViewBinding
import com.example.sharedcalendar.models.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekViewSimpleAdapter : WeekViewSimpleAdapterJsr310<Event>() {
    // Reference API: https://github.com/thellmund/Android-Week-View/wiki/Public-API
    override fun onCreateEntity(item: Event): WeekViewEntity {
        // Setup each event passed to adapter
        val entity = WeekViewEntity.Event.Builder(item).setId(item.longId).setTitle(item.title)
            .setStartTime(item.startTime).setEndTime(item.endTime)

        if (item.color.isNotEmpty()) {
            val entityStyle =
                WeekViewEntity.Style.Builder().setBackgroundColor(Color.parseColor(item.color))
                    .build()
            entity.setStyle(entityStyle)
        }
        item.description?.let { entity.setSubtitle(it) }

        return entity.build()
    }

}


class WeekViewFragment : Fragment(R.layout.fragment_week_view) {
//    val toolbar : androidx.appcompat.widget.Toolbar
//        get() = binding.WeeklyToolbar

    private lateinit var selectedDate: LocalDate
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    lateinit var binding: FragmentWeekViewBinding
    private lateinit var firebaseViewModel: FirebaseViewModel

    companion object {
        private val TAG: String = WeekViewFragment::class.java.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekViewBinding.bind(view)
        selectedDate = LocalDate.now()

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        val weekView = view.findViewById<WeekView>(R.id.weekView)

        val adapter = WeekViewSimpleAdapter()
        weekView.adapter = adapter


        firebaseViewModel.calendars.observe(requireActivity()) {
            // Get Events from Database
            firebaseViewModel.getCurrentMonthEvents()
        }


        // Listen for Event Updates
        firebaseViewModel.events.observe(viewLifecycleOwner) { events ->
            Log.i(TAG, events.toString())
            // Update Event list upon updates
            adapter.submitList(events)
        }
    }
}