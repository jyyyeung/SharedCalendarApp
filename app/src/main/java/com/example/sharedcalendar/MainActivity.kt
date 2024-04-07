package com.example.sharedcalendar

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.CalendarAdapter.OnItemListener
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDate: LocalDate? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        monthYearText = findViewById<TextView>(R.id.monthViewTV)
    }

    private fun setMonthView() {
        monthYearText!!.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate?): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date!!.format(formatter)
    }

    fun previousMonthAction(view: View?) {
        selectedDate = selectedDate!!.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        selectedDate = selectedDate!!.plusMonths(1)
        setMonthView()
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != "") {
            val message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}