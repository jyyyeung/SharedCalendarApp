package com.example.sharedcalendar

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.models.Event

class DayRecyclerViewAdapter(private val eventList: ArrayList<Event>) :
    RecyclerView.Adapter<DayViewHolder>() {

    var onItemClick: ((Event) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.day_row_layout, parent, false
        )
        return DayViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }


    fun updateList(newList: List<Event>) {
        eventList.clear()
        eventList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        Log.d("DayRecyclerViewAdapter", "onBindViewHolder: $position")
        holder.tvTitle.text = eventList[position].title
        holder.tvStartDate.text = eventList[position].startTime.toLocalDate().toString()
        holder.tvEndDate.text = eventList[position].endTime.toLocalDate().toString()
        holder.tvDes.text = eventList[position].description
        if (eventList[position].isAllDay) {
            holder.tvStartTime.text = "All Day"
            holder.tvEndTime.text = "All Day"
        } else {

            holder.tvStartTime.text = eventList[position].startTime.toLocalTime().toString()
            holder.tvEndTime.text = eventList[position].endTime.toLocalTime().toString()
        }

        val event = eventList[position]
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(event)
        }
    }

}

