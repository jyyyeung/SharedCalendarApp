package com.example.sharedcalendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.R

class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle: TextView = itemView.findViewById(R.id.cardTitleTV)
    val tvStartDate: TextView = itemView.findViewById(R.id.cardStartDateTV)
    val tvEndDate: TextView = itemView.findViewById(R.id.cardEndDateTV)
    val tvDes: TextView = itemView.findViewById(R.id.cardDescriptionTV)

}