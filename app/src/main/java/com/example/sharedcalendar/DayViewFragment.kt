package com.example.sharedcalendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.databinding.DayViewFragmentBinding
import com.example.sharedcalendar.models.Event



class DayViewFragment : Fragment(R.layout.day_view_fragment){
    lateinit var binding: DayViewFragmentBinding

    lateinit var adapter: DayRecyclerViewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var itemList : ArrayList<Event>
    lateinit var testList : ArrayList<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DayViewFragmentBinding.bind(view)

        initialiseObject()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.dayRecyclerView)
        recyclerView.layoutManager = layoutManager
        adapter = DayRecyclerViewAdapter(testList)
        recyclerView.adapter = adapter

    }

    private fun initialiseObject(){
        testList = arrayListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")
        testList.add("d")
    }
}

