package com.example.sharedcalendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * ViewHolder class for the CalendarAdapter.
 * This class represents a single item view in the RecyclerView.
 *
 * @param itemView The view representing a single item in the RecyclerView.
 */
class CalendarViewHolder(
    itemView: View,
    private val onItemListener: CalendarAdapter.OnItemListener,
) : RecyclerView.ViewHolder(itemView) {
    val dayOfMonth: TextView = itemView.findViewById(R.id.dayOfMonth)

    init {
        itemView.setOnClickListener {
            onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
        }
    }
}
