package com.example.sharedcalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.models.Event

class DayRecyclerViewAdapter(private val eventList: ArrayList<String>) : RecyclerView.Adapter<DayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.day_row_layout,parent,false
        )
        return DayViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
//        holder.tvTitle.text =  eventList[position].title
//        holder.tvStartDate.text = eventList[position].startTimestamp
//        holder.tvEndDate.text = eventList[position].endTimestamp
//        holder.tvDes.text = eventList[position].description
        holder.tvTitle.text = eventList[position]
    }


}

