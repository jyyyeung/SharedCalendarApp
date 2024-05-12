package com.example.sharedcalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.models.Event

/**
 * Adapter for the RecyclerView in the DayFragment.
 *
 * @param eventList The list of events to be displayed in the RecyclerView.
 */
class DayRecyclerViewAdapter(private val eventList: ArrayList<Event>) :
    RecyclerView.Adapter<DayViewHolder>() {
    var onItemClick: ((Event) -> Unit)? = null

    /**
     * Creates and returns a new ViewHolder object for the RecyclerView.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DayViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.day_row_layout,
                parent,
                false,
            )
        return DayViewHolder(itemView)
    }

    /**
     * Returns the number of items in the list of events.
     *
     * @return The number of items in the list of events.
     */
    override fun getItemCount(): Int {
        return eventList.size
    }

    /**
     * Updates the list of events in the RecyclerView.
     *
     * @param newList The new list of events to be displayed in the RecyclerView.
     */
    fun updateList(newList: List<Event>) {
        eventList.clear()
        eventList.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * Binds the data to the ViewHolder at the specified position.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list of events.
     */
    override fun onBindViewHolder(
        holder: DayViewHolder,
        position: Int,
    ) {
        // Bind the data to the ViewHolder
        holder.tvTitle.text = eventList[position].title
        holder.tvStartDate.text = eventList[position].startTime.toLocalDate().toString()
        holder.tvEndDate.text = eventList[position].endTime.toLocalDate().toString()
        holder.tvDes.text = eventList[position].description
        // If the event is an all-day event, display "All Day" for the start and end times
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
