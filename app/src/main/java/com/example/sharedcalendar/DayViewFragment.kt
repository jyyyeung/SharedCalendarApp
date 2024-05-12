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
import com.example.sharedcalendar.ui.editEvent.DayViewViewModel
import java.time.LocalDate


class DayViewFragment(selectedDate: LocalDate) : Fragment(R.layout.day_view_fragment) {
    private lateinit var binding: DayViewFragmentBinding

    lateinit var adapter: DayRecyclerViewAdapter
    private lateinit var itemList: ArrayList<Event>

    private var date = selectedDate

    private lateinit var firebaseViewModel: FirebaseViewModel
    private val dayViewViewModel by lazy {
        ViewModelProvider(this)[DayViewViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DayViewFragmentBinding.inflate(inflater, container, false)
        binding.dayRecyclerView.layoutManager = LinearLayoutManager(context)

        itemList = firebaseViewModel.getEventThisDay(date)

        //Firebase
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        firebaseViewModel.calendars.observe(requireActivity()) {
            //Get Events from database
            firebaseViewModel.getEvents()
        }
        adapter = DayRecyclerViewAdapter(itemList)
        binding.dayRecyclerView.adapter = adapter

        firebaseViewModel.events.observe(requireActivity()) {
            firebaseViewModel.getEventThisDay(date).also {
                adapter.updateList(it)
                itemList = it
            }
        }

        val bottomWindow = BottomSheetEditFragment()
        //RecyclerView Click
        adapter.onItemClick = {
            dayViewViewModel.setEditEvent(it)

            if (!bottomWindow.isAdded)
                bottomWindow.show(
                    childFragmentManager,
                    "BottomSheetDialogue"
                )
        }
        return binding.root
    }

}

