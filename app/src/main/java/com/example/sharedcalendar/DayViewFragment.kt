package com.example.sharedcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedcalendar.databinding.DayViewFragmentBinding
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.ui.editEvent.CalendarViewModel
import java.time.LocalDate

/**
 * A fragment that displays the day view for a selected date.
 *
 * @param selectedDate The date to be displayed in the day view.
 */
class DayViewFragment(selectedDate: LocalDate) : Fragment(R.layout.day_view_fragment) {
    private lateinit var binding: DayViewFragmentBinding

    /**
     * The adapter used for the RecyclerView in the DayViewFragment.
     */
    lateinit var adapter: DayRecyclerViewAdapter
    private lateinit var itemList: ArrayList<Event>

    private var date = selectedDate

    private lateinit var firebaseViewModel: FirebaseViewModel
    private val calendarViewModel by lazy {
        ViewModelProvider(requireActivity())[CalendarViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DayViewFragmentBinding.inflate(inflater, container, false)
        binding.dayRecyclerView.layoutManager = LinearLayoutManager(context)

        // Get Events from database
        itemList = firebaseViewModel.getEventThisDay(date)

        // Firebase
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        firebaseViewModel.calendars.observe(requireActivity()) {
            // Get Events from database
            firebaseViewModel.getEvents()
        }
        // Set up RecyclerView
        adapter = DayRecyclerViewAdapter(itemList)
        binding.dayRecyclerView.adapter = adapter

        // Observe Events
        firebaseViewModel.events.observe(requireActivity()) {
            // Get Events from database
            firebaseViewModel.getEventThisDay(date).also {
                // Update RecyclerView
                adapter.updateList(it)
                itemList = it
            }
        }

        val bottomWindow = BottomSheetEditFragment()
        // RecyclerView Click
        adapter.onItemClick = {
            // Set the event to be edited
            calendarViewModel.setEditEvent(it)

            // Show the bottom sheet if it is not already shown
            if (!bottomWindow.isAdded) { // To Prevent multiple instances of the bottom sheet
                bottomWindow.show(
                    childFragmentManager,
                    "BottomSheetDialogue",
                )
            }
        }
        return binding.root
    }
}
