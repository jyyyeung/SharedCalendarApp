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

/**
 * WeekViewFragment class.
 *
 * This class extends the Fragment class and is responsible for displaying a week view of the calendar.
 * It observes changes in the selected date and events from the ViewModel and updates the UI accordingly.
 *
 * @property visibleDays The number of days to be visible in the week view.
 * @constructor Create [WeekViewFragment]
 */
class WeekViewFragment(private val visibleDays: Int = 3) : Fragment(R.layout.fragment_week_view) {

    private lateinit var selectedDate: LocalDate
    private lateinit var binding: FragmentWeekViewBinding
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var adapter: WeekViewSimpleAdapter
    private lateinit var weekView: WeekView

    companion object {
        private val TAG: String = WeekViewFragment::class.java.name
    }

    /**
     * Called when the view is created.
     *
     * This function initializes the binding, selectedDate, firebaseViewModel, calendarViewModel, weekView, and adapter.
     * It also sets up observers for the selected date and events.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekViewBinding.bind(view)
        selectedDate = LocalDate.now()

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        calendarViewModel = ViewModelProvider(requireActivity())[CalendarViewModel::class.java]

        weekView = view.findViewById<WeekView>(R.id.weekView)
        weekView.numberOfVisibleDays = visibleDays

        adapter = WeekViewSimpleAdapter(parentFragmentManager)
        weekView.adapter = adapter

        // Observe changes in the selected date and scroll to the selected date
        calendarViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            if (date == null) {
                return@observe
            }
            weekView.scrollToDate(date)
        }

        // Observe changes in the events and update the adapter's list
        firebaseViewModel.events.observe(viewLifecycleOwner) { events ->
            val enabledCalendarIds = firebaseViewModel.enabledCalendars.value ?: mutableListOf()
            val filtered = events.filter { enabledCalendarIds.contains(it.calendarId) }
            Log.i(TAG, "Week view events update $events")
            adapter.submitList(filtered)
        }
    }
}