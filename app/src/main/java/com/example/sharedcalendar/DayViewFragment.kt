package com.example.sharedcalendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.databinding.DayViewFragmentBinding
import com.example.sharedcalendar.models.Event
import java.time.LocalDate
import java.time.LocalDateTime


class DayViewFragment(selectedDate: LocalDate): Fragment(R.layout.day_view_fragment){
    lateinit var binding: DayViewFragmentBinding

    lateinit var adapter: DayRecyclerViewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var itemList : ArrayList<Event>
    //lateinit var testList : ArrayList<String>

    var date = selectedDate

    private lateinit var firebaseViewModel: FirebaseViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = DayViewFragmentBinding.bind(view)

        initialiseObject()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.dayRecyclerView)
        recyclerView.layoutManager = layoutManager
        adapter = DayRecyclerViewAdapter(itemList)
        recyclerView.adapter = adapter

        //Firebase
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        firebaseViewModel.calendars.observe(requireActivity()){
            //Get Events from database
            //firebaseViewModel.get
        }


        //RecyclerView Click
        adapter.onItemClick = {
            val bottomWindow = BottomSheetEditFragment.newInstance(it)
            bottomWindow.show(
                childFragmentManager,
                "BottomSheetDialogue"
            )
        }

    }


    //TODO: Click on event


    private fun initialiseObject(){
        itemList = arrayListOf<Event>()
        var test = Event(
            "0","0","event0","description")
        var test1 = Event(
            "1","0","event1","description")
        var test2 = Event(
            "2","0","event2","description")
        var test3 = Event(
            "3","0","event3","description")
        var test4 = Event(
            "4","0","event4","description")

        itemList.add(test)
        itemList.add(test2)
        itemList.add(test3)
        itemList.add(test4)
    }
}

