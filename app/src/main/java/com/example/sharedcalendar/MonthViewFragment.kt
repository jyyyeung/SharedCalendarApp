package com.example.sharedcalendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sharedcalendar.databinding.FragmentMonthViewBinding
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
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder

class MonthViewFragment : Fragment(R.layout.fragment_month_view) {

    private var selectedDate: LocalDate? = null

    lateinit var binding: FragmentMonthViewBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMonthViewBinding.bind(view)

        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(200)
        val endMonth = currentMonth.plusMonths(200)


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
            val binding = FragmentMonthViewBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {

                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date

                            val binding = this@MonthViewFragment.binding
                            binding.MonthViewCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.MonthViewCalendar.notifyDateChanged(it) }
                            //updateAdapterForDate(day.date) For Flight Date
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
//                val textView = container.binding.
                val layout = container.binding.MonthViewCalendar
                //textView.text =
            }
        }
    }

//    enum class DayPosition {
//        InDate,
//        MonthDate,
//        OutDate,
//    }

//    data class CalendarDay(val date: LocalDate, val position: DayPosition) : Serializable


}







