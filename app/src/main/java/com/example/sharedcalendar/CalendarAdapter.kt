package com.example.sharedcalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter class for displaying calendar data in a RecyclerView.
 *
 * @param daysOfMonth The list of days to be displayed in the calendar.
 * @param onItemListener The listener for item clicks in the calendar.
 * @constructor Initializes the CalendarAdapter with the given list of days and item listener.
 */
class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    /**
     * Binds the data to the ViewHolder at the specified position.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(
        holder: CalendarViewHolder,
        position: Int,
    ) {
        holder.dayOfMonth.text = daysOfMonth[position]
    }

    /**
     * Returns the number of items in the list of days.
     *
     * @return The number of items in the list of days.
     */
    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    /**
     * Interface for handling item clicks in the calendar.
     */
    interface OnItemListener {
        fun onItemClick(
            position: Int,
            dayText: String?,
        )
    }
}
