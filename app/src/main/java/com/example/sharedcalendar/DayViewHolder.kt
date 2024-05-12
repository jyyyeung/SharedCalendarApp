package com.example.sharedcalendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * This file is the view holder for the day view.
 * It imports the necessary classes for Android intents.
 */
class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle: TextView = itemView.findViewById(R.id.cardTitleTV)
    val tvStartDate: TextView = itemView.findViewById(R.id.cardStartDateTV)
    val tvEndDate: TextView = itemView.findViewById(R.id.cardEndDateTV)
    val tvDes: TextView = itemView.findViewById(R.id.cardDescriptionTV)
    val tvStartTime: TextView = itemView.findViewById(R.id.cardStartTimeTV)
    val tvEndTime: TextView = itemView.findViewById(R.id.cardEndTimeTV)
}
