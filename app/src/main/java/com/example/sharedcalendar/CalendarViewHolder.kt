package com.example.sharedcalendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//class CalendarViewHolder(itemView: View, onItemListener: CalendarAdapter.OnItemListener) :
//    RecyclerView.ViewHolder(itemView), View.OnClickListener {
//    val dayOfMonth: TextView
//    private val onItemListener: CalendarAdapter.OnItemListener
//
//    init {
//        dayOfMonth = itemView.findViewById<TextView>(R.id.dayOfMonth)
//        this.onItemListener = onItemListener
//        itemView.setOnClickListener(this)
//    }
//
//
//    override fun onClick(view: View) {
//        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
//    }
//}
class CalendarViewHolder(
    itemView: View,
    private val onItemListener: CalendarAdapter.OnItemListener
) : RecyclerView.ViewHolder(itemView) {
    val dayOfMonth: TextView = itemView.findViewById(R.id.dayOfMonth)

    init {
        itemView.setOnClickListener {
            onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
        }
    }
}