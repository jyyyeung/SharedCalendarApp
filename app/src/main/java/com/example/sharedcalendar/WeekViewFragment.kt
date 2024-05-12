package com.example.sharedcalendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alamkanak.weekview.WeekView
import com.example.sharedcalendar.databinding.FragmentWeekViewBinding
import com.example.sharedcalendar.ui.editEvent.CalendarViewModel
import java.time.LocalDate


class WeekViewFragment(private val visibleDays: Int = 3) : Fragment(R.layout.fragment_week_view) {
//    val toolbar : androidx.appcompat.widget.Toolbar
//        get() = binding.WeeklyToolbar

    private lateinit var selectedDate: LocalDate
    private lateinit var binding: FragmentWeekViewBinding
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var calendarViewModel: CalendarViewModel
//    private lateinit var prefs: SharedFirebasePreferences

    private lateinit var adapter: WeekViewSimpleAdapter
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
        calendarViewModel =
            ViewModelProvider(requireParentFragment())[CalendarViewModel::class.java]

        weekView = view.findViewById<WeekView>(R.id.weekView)


        weekView.numberOfVisibleDays = visibleDays

        val adapter = WeekViewSimpleAdapter(parentFragmentManager)
        weekView.adapter = adapter

        calendarViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            if (date == null) {
                return@observe
            }
            weekView.scrollToDate(date)
        }


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