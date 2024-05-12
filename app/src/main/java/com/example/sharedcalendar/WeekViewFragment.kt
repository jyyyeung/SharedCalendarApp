package com.example.sharedcalendar

import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.alamkanak.weekview.WeekView
import com.example.sharedcalendar.databinding.FragmentWeekViewBinding
import java.time.LocalDate

class WeekViewSimpleAdapter(private val fragmentManager: FragmentManager) : WeekViewSimpleAdapterJsr310<Event>() {
    // Reference API: https://github.com/thellmund/Android-Week-View/wiki/Public-API
    override fun onCreateEntity(item: Event): WeekViewEntity {
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

        val newFragment = DayViewFragment(data.startTime.toLocalDate()) // Replace "YourNewFragment" with the fragment you want to navigate to
        fragmentManager.beginTransaction()
            .replace(R.id.flFragment, newFragment) // Replace "R.id.fragmentContainer" with the ID of the container where you want to replace the fragment
            .addToBackStack(null)
            .commit()

    }

}



class WeekViewFragment : Fragment(R.layout.fragment_week_view) {
//    val toolbar : androidx.appcompat.widget.Toolbar
//        get() = binding.WeeklyToolbar

    private lateinit var selectedDate: LocalDate
    private lateinit var binding: FragmentWeekViewBinding
    private lateinit var firebaseViewModel: FirebaseViewModel
//    private lateinit var prefs: SharedFirebasePreferences

    private val adapter = WeekViewSimpleAdapter()
    private lateinit var weekView: WeekView


    companion object {
        private val TAG: String = WeekViewFragment::class.java.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekViewBinding.bind(view)
        selectedDate = LocalDate.now()

//        val prefs = SharedFirebasePreferences.getDefaultInstance(activity)

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        weekView = view.findViewById<WeekView>(R.id.weekView)


        val adapter = WeekViewSimpleAdapter(parentFragmentManager)
        weekView.adapter = adapter

        firebaseViewModel.calendars.observe(requireActivity()) {

            // Get Events from Database
            firebaseViewModel.getCurrentMonthEvents()
        }

        firebaseViewModel.getCurrentMonthEvents()


        // Listen for Event Updates
//        firebaseViewModel.events.observe(viewLifecycleOwner) { events ->
        firebaseViewModel.events.observe(viewLifecycleOwner) { events ->
            val enabledCalendarIds = firebaseViewModel.enabledCalendars.value ?: mutableListOf()

            val filtered = events.filter { enabledCalendarIds.contains(it.calendarId) }
            // Update Event list upon updates

            Log.i(TAG, "Week view events update $events")

            adapter.submitList(filtered)

        }
    }
}