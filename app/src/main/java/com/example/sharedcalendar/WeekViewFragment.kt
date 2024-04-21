package com.example.sharedcalendar

import android.net.LocalSocketAddress
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.sharedcalendar.databinding.FragmentWeekViewBinding
import com.example.sharedcalendar.databinding.WeeklyCalenderDayBinding
import com.example.sharedcalendar.models.displayText
import com.example.sharedcalendar.models.getColorCompat
import com.example.sharedcalendar.models.getWeekPageTitle
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Date


class WeekViewFragment : Fragment(R.layout.fragment_week_view){
    val toolbar : androidx.appcompat.widget.Toolbar
        get() = binding.WeeklyToolbar

    private lateinit var selectedDate : LocalDate
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    lateinit var binding : FragmentWeekViewBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekViewBinding.bind(view)
        selectedDate = LocalDate.now()

        class DayViewContainer(view: View) : ViewContainer(view){
            val bind = WeeklyCalenderDayBinding.bind(view)
            lateinit var day:WeekDay

            init {
                view.setOnClickListener{
                    if(selectedDate != day.date){
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.WeekView.notifyDateChanged(day.date)
                        oldDate?.let{binding.WeekView.notifyDateChanged(it)}
                    }
                }
            }

            fun bind(day:WeekDay){
                this.day = day
                bind.weeklyDateText.text = dateFormatter.format(day.date)
                bind.weeklyDayOfWeekText.text = day.date.dayOfWeek.displayText()

                val  colorRes = if(day.date == selectedDate){
                    R.color.example_7_yellow
                } else{
                    R.color.example_7_white
                }
                bind.weeklyDateText.setTextColor(view.context.getColorCompat(colorRes))
                bind.weeklyDaySelectedView.isVisible = day.date==selectedDate
            }
        }

        binding.WeekView.dayBinder = object : WeekDayBinder<DayViewContainer>{
            override fun create(view:View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: WeekDay) = container.bind(data)
        }

        binding.WeekView.weekScrollListener = {weekDays ->
            binding.WeeklyToolbar.title = getWeekPageTitle(weekDays)
        }
        val currentMonth = YearMonth.now()
        binding.WeekView.setup(
            currentMonth.minusMonths(5).atStartOfMonth(),
            currentMonth.plusMonths(5).atEndOfMonth(),
            firstDayOfWeekFromLocale(),
        )
        binding.WeekView.scrollToDate(LocalDate.now())
    }
}