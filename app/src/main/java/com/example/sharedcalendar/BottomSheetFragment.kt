package com.example.sharedcalendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.common.annotations.VisibleForTesting
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * A fragment that represents a bottom sheet dialog.
 * This class extends [BottomSheetDialogFragment] and provides the necessary functionality for displaying a bottom sheet dialog.
 */
class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var prefs: SharedPreferences
    private lateinit var firebaseViewModel: FirebaseViewModel

    @VisibleForTesting
    lateinit var startDateTime: LocalDateTime

    @VisibleForTesting
    lateinit var endDateTime: LocalDateTime

    @VisibleForTesting
    lateinit var dateText: TextView

    @VisibleForTesting
    lateinit var timeText: TextView

    @VisibleForTesting
    lateinit var endDateText: TextView

    @VisibleForTesting
    lateinit var endTimeText: TextView

    @VisibleForTesting
    lateinit var swIsAllDay: MaterialSwitch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        return inflater.inflate(R.layout.bottom_window, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val today = LocalDateTime.now()

        // Handle Click on StartDate
        dateText = view.findViewById(R.id.startDateTV)
        timeText = view.findViewById(R.id.startTimeTV)
        endDateText = view.findViewById(R.id.endDateTV)
        endTimeText = view.findViewById(R.id.endTimeTV)
        swIsAllDay = view.findViewById(R.id.swIsAllDay)

        // Handle Click on StartDate
        dateText.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(
                    this.requireContext(),
                    { _, year, month, day -> // Showing the picked value in the textView
                        val adjMonth = month + 1
                        dateText.text = "$year.$adjMonth.$day"
                        startDateTime =
                            if (timeText.text == "Time") {
                                LocalDateTime.of(year, adjMonth, day, 0, 0)
                            } else {
                                LocalDateTime.of(
                                    year,
                                    adjMonth,
                                    day,
                                    startDateTime.hour,
                                    startDateTime.minute,
                                )
                            }
                        if (endDateText.text == "Date") {
                            endDateTime = LocalDateTime.of(
                                startDateTime.year,
                                startDateTime.month,
                                startDateTime.dayOfMonth,
                                0,
                                0
                            )
                            endDateText.text =
                                "${startDateTime.year}.${startDateTime.month.value}.${startDateTime.dayOfMonth}"
                        }

                        Log.d("StartDate", "StartDate: $startDateTime")
                        checkDateIsValid()
                    },
                    (today.year),
                    (today.month.value - 1),
                    (today.dayOfMonth),
                )

            datePickerDialog.show()
        }

        // Handle Click on EndDate
        endDateText.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(
                    this.requireContext(),
                    { _, year, month, day -> // Showing the picked value in the textView
                        val adjMonth = month + 1
                        endDateText.text = "$year.$adjMonth.$day"
                        endDateTime =
                            if (endTimeText.text == "Time") {
                                LocalDateTime.of(year, adjMonth, day, 0, 0)
                            } else {
                                LocalDateTime.of(
                                    year,
                                    adjMonth,
                                    day,
                                    endDateTime.hour,
                                    endDateTime.minute,
                                )
                            }
                        Log.d("StartDate", "endDate: $endDateTime")
                        checkDateIsValid()
                    },
                    (today.year),
                    (today.month.value - 1),
                    (today.dayOfMonth),
                )

            datePickerDialog.show()
        }
        // Handle Click on StartTime
        timeText.setOnClickListener {
            val timePickerDialog =
                TimePickerDialog(
                    this.requireContext(),
                    { _, hour, minute ->
                        timeText.text = "$hour:$minute"
                        startDateTime =
                            if (dateText.text == "Date") {
                                LocalDateTime.of(0, 1, 1, hour, minute)
                            } else {
                                LocalDateTime.of(
                                    startDateTime.year,
                                    startDateTime.month,
                                    startDateTime.dayOfMonth,
                                    hour,
                                    minute,
                                )
                            }
                        checkDateIsValid()
                        Log.d("StartDate", "startDate: $startDateTime")
                    },
                    today.hour,
                    today.minute,
                    false,
                )
            timePickerDialog.show()
        }

        // Handle Click on EndTime
        endTimeText.setOnClickListener {
            val timePickerDialog =
                TimePickerDialog(
                    this.requireContext(),
                    { _, hour, minute ->
                        endTimeText.text = "$hour:$minute"
                        endDateTime =
                            if (endDateText.text == "Date") {
                                LocalDateTime.of(0, 1, 1, hour, minute)
                            } else {
                                LocalDateTime.of(
                                    endDateTime.year,
                                    endDateTime.month,
                                    endDateTime.dayOfMonth,
                                    hour,
                                    minute,
                                )
                            }
                        checkDateIsValid()
                        Log.d("StartDate", "endDate: $endDateTime")
                    },
                    today.plusHours(1).hour,
                    today.minute,
                    false,
                )
            timePickerDialog.show()
        }

        // Select Color Spinner
        loadColor()

        // Cancel Button
        val cancelBtn = view.findViewById<Button>(R.id.cancelBtn)
        cancelBtn.setOnClickListener {
            this.dismiss()
        }

        // Is All Day Switch
        swIsAllDay.setOnClickListener {
            if (swIsAllDay.isChecked) {
                timeText.visibility = View.INVISIBLE
                endTimeText.visibility = View.INVISIBLE
            } else {
                timeText.visibility = View.VISIBLE
                endTimeText.visibility = View.VISIBLE
            }
            checkDateIsValid()
        }

        // Color select
        val colorText = view.findViewById<TextView>(R.id.colorTextView)
        colorText.setOnClickListener {
            val spinner = view.findViewById<Spinner>(R.id.colorSpinner)
            spinner?.performClick()
        }

        val etNewEventName = view.findViewById<EditText>(R.id.etNewEventName)
        val etNewEventDescription = view.findViewById<EditText>(R.id.etNewEventDescription)

        // Save Button
        val saveBtn = view.findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            var startHour = 0
            var startMinute = 0
            var endHour = 0
            var endMinute = 0

            // Get start date
            val (startYear, startMonth, startDay) = dateText.text.split(".").map { it.toInt() }
            if (!swIsAllDay.isChecked) {
                val (sHour, sMinute) = timeText.text.split(":").map { it.toInt() }
                startHour = sHour
                startMinute = sMinute
            }

            // Get end date
            val (endYear, endMonth, endDay) = endDateText.text.split(".").map { it.toInt() }
            if (!swIsAllDay.isChecked) {
                val (eHour, eMinute) = endTimeText.text.split(":").map { it.toInt() }
                endHour = eHour
                endMinute = eMinute
            }

            // Get default calendar
            var defaultCalendar =
                prefs.getString("${FirebaseAuth.getInstance().uid}|default|calendar", null)
            Log.i(TAG, defaultCalendar.toString())
            if (defaultCalendar.isNullOrBlank()) {
                defaultCalendar = (activity as MainActivity).getCalendarId()
            }

            // Create new event
            val newEvent: HashMap<String, Any?> =
                hashMapOf(
                    "longId" to Random.nextLong(until = Long.MAX_VALUE),
                    "calendarId" to defaultCalendar,
                    "title" to etNewEventName.text.toString(),
                    "description" to etNewEventDescription.text.toString(),
                    "isAllDay" to swIsAllDay.isChecked,
                    "startTimestamp" to
                            LocalDateTime.of(
                                startYear,
                                startMonth,
                                startDay,
                                startHour,
                                startMinute,
                            ).toString(),
                    "endTimestamp" to
                            LocalDateTime.of(
                                endYear,
                                endMonth,
                                endDay,
                                endHour,
                                endMinute,
                            ).toString(),
                    "color" to selectedColor.code,
                )
            firebaseViewModel.addEventToCalendar(newEvent).run {
                this@BottomSheetFragment.dismiss()
            }
        }
    }

    /**
     * Check Date Is Valid.
     *
     */
    fun checkDateIsValid() {
        Log.d("dateCheck", "Checking Date")
        val saveBtn = view?.findViewById<Button>(R.id.saveBtn)
        // Do nothing if not yet picked all date
        if (dateText.text == "Date") {
            saveBtn?.isEnabled = false
            return
        }
        if (endDateText.text == "Date") {
            saveBtn?.isEnabled = false
            return
        }
        if (!swIsAllDay.isChecked) {
            if (timeText.text == "Time") {
                saveBtn?.isEnabled = false
                return
            }
            if (endTimeText.text == "Time") {
                saveBtn?.isEnabled = false
                return
            }
        } else {
            startDateTime =
                LocalDateTime.of(
                    startDateTime.year,
                    startDateTime.month,
                    startDateTime.dayOfMonth,
                    0,
                    0,
                )
            endDateTime =
                LocalDateTime.of(endDateTime.year, endDateTime.month, endDateTime.dayOfMonth, 0, 0)
        }

        // If Start Date time is after End Date time, show error and disable save button
        val startDateLL = view?.findViewById<LinearLayout>(R.id.bottom_window_text2_layout)
        if (startDateTime.isAfter(endDateTime)) {
            startDateLL?.setBackgroundColor(resources.getColor(R.color.md_theme_error))
            saveBtn?.isEnabled = false
        } else {
            startDateLL?.setBackgroundColor(resources.getColor(R.color.md_theme_background))
            saveBtn?.isEnabled = true
        }
    }

    companion object {
        private val TAG: String? = BottomSheetFragment::class.java.name
    }

    lateinit var selectedColor: Color

    /**
     * Load color spinner
     */
    private fun loadColor() {
        selectedColor = ColorList().default
        val spinner = view?.findViewById<Spinner>(R.id.colorSpinner)
        val colorTextView = view?.findViewById<TextView>(R.id.colorTextView)
        val colorView = view?.findViewById<View>(R.id.colorWindow)

        spinner?.adapter = SpinnerAdapter(requireContext(), ColorList().colors())
        spinner?.setSelection(ColorList().colorPosition(selectedColor), false)
        spinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    colorTextView?.text = ColorList().colors()[position].name
                    ColorList().colors()[position].name
                    colorView?.background?.setTint(android.graphics.Color.parseColor(ColorList().colors()[position].code))
                    selectedColor = ColorList().colors()[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }
}
