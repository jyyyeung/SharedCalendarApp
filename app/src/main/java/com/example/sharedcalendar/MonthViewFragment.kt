package com.example.sharedcalendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek
import com.example.sharedcalendar.databinding.FragmentMonthViewBinding
import com.example.sharedcalendar.databinding.CalendarDayBinding

import com.example.sharedcalendar.ui.MonthDayBinder
//import com.kizitonwose.calendarview.model.CalendarDay
//import com.kizitonwose.calendarview.ui.ViewContainer
import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

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
        configureBinders(daysOfWeek)
        binding.MonthViewCalendar.setup(startMonth,endMonth,daysOfWeek.first())
        binding.MonthViewCalendar.scrollToMonth(currentMonth)

        //scroll listener


    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view:View) : com.example.sharedcalendar.ui.ViewContainer(view){
            lateinit var day : com.example.sharedcalendar.CalendarDay
            val binding = FragmentMonthViewBinding.bind(view)

            init{
                view.setOnClickListener{
                    if (day.position == DayPosition.MonthDate){
                        if(selectedDate != day.date){
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@MonthViewFragment.binding
                            binding.MonthViewCalendar.notifyDateChanged(day.date)
                            oldDate?.let{binding.MonthViewCalendar.notifyDateChanged(it)}
                            //updateAdapterForDate(day.date) For Flight Date
                        }
                    }
                }
            }
        }

        binding.MonthViewCalendar.dayBinder = object : MonthDayBinder<DayViewContainer>
        {
            override fun create(view:View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data:com.example.sharedcalendar.CalendarDay){
                container.day = data
                val context = container.binding.root.context
                val textView : TextView = container.binding.root.findViewById<TextView>(R.id.calendarDayText)
                val layout = container.binding.MonthViewCalendar
                textView.text = data.date.dayOfMonth.toString()

                if(data.position == DayPosition.MonthDate){
                    textView.setTextColor(resources.getColor(R.color.example_5_text_grey))
                    layout.setBackgroundResource(if(selectedDate == data.date) R.drawable.example_5_selected_bg else 0)
                }
            }
        }
    }


}







