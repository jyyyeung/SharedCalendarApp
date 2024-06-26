package com.example.sharedcalendar

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcalendar.databinding.CalendarDayBinding
import com.example.sharedcalendar.databinding.CalendarHeaderBinding
import com.example.sharedcalendar.databinding.FragmentMonthViewBinding
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.displayText
import com.example.sharedcalendar.ui.editEvent.CalendarViewModel
import com.google.common.annotations.VisibleForTesting
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


class MonthViewFragment : Fragment(R.layout.fragment_month_view) {

    @VisibleForTesting
    lateinit var firebaseViewModel: FirebaseViewModel
    lateinit var calendarViewModel: CalendarViewModel
    private var selectedDate: LocalDate? = null


    private var eventsThisMonth: Map<LocalDate, List<Event>>? = null
    lateinit var binding: FragmentMonthViewBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        eventsThisMonth = firebaseViewModel.getGroupedEvents()

        binding = FragmentMonthViewBinding.bind(view)
        calendarViewModel =
            ViewModelProvider(requireActivity())[CalendarViewModel::class.java]
        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(200)
        val endMonth = currentMonth.plusMonths(200)

        // Listen for Event Updates
        firebaseViewModel.events.observe(viewLifecycleOwner) { events ->
            Log.i(TAG, events.toString())
            // Update Event list upon updates
            eventsThisMonth = firebaseViewModel.getGroupedEvents()
            Log.i(TAG, "Month View calendar Events updated")
            binding.MonthViewCalendar.notifyCalendarChanged()
        }

        configureBinders(daysOfWeek)

        // Setup Month View
        binding.MonthViewCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        // Scroll month view to current month
        binding.MonthViewCalendar.scrollToMonth(currentMonth)

        // listen to month scroll action
        binding.MonthViewCalendar.monthScrollListener = { month ->
            // Update Month + Year display text on top of month view
            binding.MonthYearText.text = month.yearMonth.displayText()

            selectedDate?.let {
                selectedDate = null
                binding.MonthViewCalendar.notifyDateChanged(it)
            }
        }
        // Click listener for next month button
        binding.MonthYearNext.setOnClickListener {
            binding.MonthViewCalendar.findFirstVisibleMonth()?.let {
                binding.MonthViewCalendar.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        // Click Listener for previous month button
        binding.MonthYearLast.setOnClickListener {
            binding.MonthViewCalendar.findFirstVisibleMonth()?.let {
                binding.MonthViewCalendar.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }


    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = CalendarDayBinding.bind(view)

            init {
                // Click listener for each day
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date

                            calendarViewModel.setSelectedDate(selectedDate!!)
                            // Change Fragment from month to week on button click
                            calendarViewModel.changeViewMode("3-day")
                        }
                    }
                }
            }
        }

        binding.MonthViewCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val context = container.binding.root.context
                val textView = container.binding.calendarDayText
                val layout = container.binding.calendarDayLayout
                // Set Day of month for each day "cell"
                textView.text = data.date.dayOfMonth.toString()

                val eventTopView = container.binding.EventTop
                val eventBottomView = container.binding.EventBottom
                eventTopView.background = null
                eventBottomView.background = null

                if (data.position == DayPosition.MonthDate) {
                    textView.setTextColor(resources.getColor(R.color.black))
                    layout.setBackgroundResource(if (selectedDate == data.date) R.drawable.example_5_selected_bg else 0)

                    val eventsThisDay = eventsThisMonth?.get(data.date)
//                    Log.i(TAG, eventsThisDay.toString())
                    if (eventsThisDay != null) {
                        // Indicate number of events today
                        if (eventsThisDay.count() == 1) {
                            eventBottomView.setBackgroundColor(Color.parseColor(eventsThisDay[0].color))
                        } else {
                            eventTopView.setBackgroundColor(Color.parseColor(eventsThisDay[0].color))
                            eventBottomView.setBackgroundColor(Color.parseColor(eventsThisDay[1].color))
                        }
                    }
                } else {
                    textView.setTextColor(resources.getColor(R.color.example_5_text_grey))
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val monthHeader = CalendarHeaderBinding.bind(view).monthHeader.root
        }

        // Set font
        val typeFace = Typeface.create("sans-serif-light", Typeface.NORMAL)
        binding.MonthViewCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.monthHeader.tag == null) {
                        container.monthHeader.tag = data.yearMonth
                        container.monthHeader.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].displayText(uppercase = true)
                                tv.setTextColor(resources.getColor(R.color.black))
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                                tv.typeface = typeFace
                            }
                    }
                }
            }


    }

    companion object {
        private val TAG: String = MonthViewFragment::class.java.name
    }

}







