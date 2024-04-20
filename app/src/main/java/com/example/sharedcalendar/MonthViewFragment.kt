package com.example.sharedcalendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sharedcalendar.databinding.FragmentMonthViewBinding
import com.example.sharedcalendar.databinding.CalendarDayBinding
import com.example.sharedcalendar.databinding.CalendarEventBinding
import com.example.sharedcalendar.databinding.CalendarHeaderBinding

import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import android.graphics.Typeface
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.displayText
import com.example.sharedcalendar.models.generateEvents
import com.example.sharedcalendar.models.getColorCompat
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import java.time.Month
import java.time.format.TextStyle
import java.util.Date

class MonthViewFragment : Fragment(R.layout.fragment_month_view) {

    private var selectedDate: LocalDate? = null

    private val thisEvents = generateEvents().groupBy { it.startTime.toLocalDate() }
    lateinit var binding: FragmentMonthViewBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMonthViewBinding.bind(view)

        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(200)
        val endMonth = currentMonth.plusMonths(200)
        configureBinders(daysOfWeek)
        binding.MonthViewCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.MonthViewCalendar.scrollToMonth(currentMonth)

        binding.MonthViewCalendar.monthScrollListener = { month ->
            binding.MonthYearText.text = month.yearMonth.displayText()

            selectedDate?.let {
                selectedDate = null
                binding.MonthViewCalendar.notifyDateChanged(it)
            }
        }
        binding.MonthYearNext.setOnClickListener {
            binding.MonthViewCalendar.findFirstVisibleMonth()?.let {
                binding.MonthViewCalendar.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        binding.MonthYearLast.setOnClickListener {
            binding.MonthViewCalendar.findFirstVisibleMonth()?.let {
                binding.MonthViewCalendar.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }


    }

//    @OptIn(ExperimentalStdlibApi::class)
//    fun DaysOfWeek(firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek): List<DayOfWeek> {
//        val pivot = 7 - firstDayOfWeek.ordinal
//        val daysOfWeek = DayOfWeek.entries
//        return (daysOfWeek.takeLast(pivot) + daysOfWeek.dropLast(pivot))
//    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@MonthViewFragment.binding
                            binding.MonthViewCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.MonthViewCalendar.notifyDateChanged(it) }
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
                textView.text = data.date.dayOfMonth.toString()

                val eventTopView = container.binding.EventTop
                val eventBottomView = container.binding.EventBottom
                eventTopView.background = null
                eventBottomView.background = null

                if (data.position == DayPosition.MonthDate) {
                    textView.setTextColor(resources.getColor(R.color.example_5_text_grey))
                    layout.setBackgroundResource(if (selectedDate == data.date) R.drawable.example_5_selected_bg else 0)

                    val thisEvents = thisEvents[data.date]
                    if (thisEvents != null){
                        if(thisEvents.count() == 1){
                            eventBottomView.setBackgroundColor(context.getColorCompat(thisEvents[0].color))
                        }else{
                            eventTopView.setBackgroundColor(context.getColorCompat(thisEvents[0].color))
                            eventBottomView.setBackgroundColor(context.getColorCompat(thisEvents[1].color))
                        }
                    }
                } else {
                    textView.setTextColor(resources.getColor(R.color.example_5_text_grey))
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout
        }

        val typeFace = Typeface.create("sans-serif-light", Typeface.NORMAL)
        binding.MonthViewCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].displayText(uppercase = true)
                                tv.setTextColor(resources.getColor(R.color.white))
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                                tv.typeface = typeFace
                            }
                    }
                }
            }


    }






}







