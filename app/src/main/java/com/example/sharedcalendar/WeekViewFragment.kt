package com.example.sharedcalendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEntity
import com.alamkanak.weekview.jsr310.WeekViewSimpleAdapterJsr310
import com.alamkanak.weekview.jsr310.setEndTime
import com.alamkanak.weekview.jsr310.setStartTime
import com.example.sharedcalendar.databinding.FragmentWeekViewBinding
import com.example.sharedcalendar.models.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekViewSimpleAdapter : WeekViewSimpleAdapterJsr310<Event>() {
    // Reference API: https://github.com/thellmund/Android-Week-View/wiki/Public-API
    override fun onCreateEntity(item: Event): WeekViewEntity {
        // Setup each event passed to adapter
        val entity = WeekViewEntity.Event.Builder(item).setId(item.longId).setTitle(item.title)
            .setStartTime(item.startTime).setEndTime(item.endTime)

        if (item.color.isNotEmpty()) {
            val entityStyle =
                WeekViewEntity.Style.Builder().setBackgroundColor(Color.parseColor(item.color))
                    .build()
            entity.setStyle(entityStyle)
        }
        item.description?.let { entity.setSubtitle(it) }

        return entity.build()
    }

}


class WeekViewFragment : Fragment(R.layout.fragment_week_view) {
//    val toolbar : androidx.appcompat.widget.Toolbar
//        get() = binding.WeeklyToolbar

    private lateinit var selectedDate: LocalDate
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    lateinit var binding: FragmentWeekViewBinding
    private val viewModel by viewModels<EventViewModel>()

    companion object {
        private val TAG: String = WeekViewFragment::class.java.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekViewBinding.bind(view)
        selectedDate = LocalDate.now()

        val weekView = view.findViewById<WeekView>(R.id.weekView)

        val adapter = WeekViewSimpleAdapter()
        weekView.adapter = adapter

        // Get Events from Database
        viewModel.getCurrentMonthEvents()

        // Listen for Event Updates
        viewModel.events.observe(viewLifecycleOwner) { events ->
            Log.i(TAG, events.toString())
            // Update Event list upon updates
            adapter.submitList(events)
        }

//        class DayViewContainer(view: View) : ViewContainer(view) {
//            val bind = WeeklyCalenderDayBinding.bind(view)
//            lateinit var day: WeekDay
//
//            init {
//                view.setOnClickListener {
//                    if (selectedDate != day.date) {
//                        val oldDate = selectedDate
//                        selectedDate = day.date
////                        binding.WeekView.notifyDateChanged(day.date)
////                        oldDate.let { binding.WeekView.notifyDateChanged(it) }
//                    }
//                }
//            }
//
//            fun bind(day: WeekDay) {
//                this.day = day
//                bind.weeklyDateText.text = dateFormatter.format(day.date)
//                bind.weeklyDayOfWeekText.text = day.date.dayOfWeek.displayText()
//
//                val colorRes = if (day.date == selectedDate) {
//                    R.color.example_7_yellow
//                } else {
//                    R.color.example_7_white
//                }
//                bind.weeklyDateText.setTextColor(view.context.getColorCompat(colorRes))
//                bind.weeklyDaySelectedView.isVisible = day.date == selectedDate
//            }
//        }

//        binding.WeekView.dayBinder = object : WeekDayBinder<DayViewContainer> {
//            override fun create(view: View) = DayViewContainer(view)
//            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
//        }
//
//        binding.WeekView.weekScrollListener = { weekDays ->
//            binding.WeeklyToolbar.title = getWeekPageTitle(weekDays)
//        }
//        val currentMonth = YearMonth.now()
//        binding.WeekView.setup(
//            currentMonth.minusMonths(5).atStartOfMonth(),
//            currentMonth.plusMonths(5).atEndOfMonth(),
//            firstDayOfWeekFromLocale(),
//        )
//        binding.WeekView.scrollToDate(LocalDate.now())
    }
}