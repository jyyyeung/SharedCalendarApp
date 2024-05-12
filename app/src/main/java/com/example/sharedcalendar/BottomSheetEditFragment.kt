package com.example.sharedcalendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.sharedcalendar.models.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import java.time.LocalDateTime
import kotlin.random.Random


class BottomSheetEditFragment() : BottomSheetDialogFragment(){
    private lateinit var prefs: SharedFirebasePreferences
    private lateinit var firebaseViewModel: FirebaseViewModel

    private lateinit var startDateTime: LocalDateTime
    private lateinit var endDateTime: LocalDateTime
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView
    private lateinit var endDateText: TextView
    private lateinit var endTimeText: TextView
    private lateinit var swIsAllDay: MaterialSwitch
    private lateinit var thisEvent : Event
    private lateinit var etNewEventName : EditText
    private lateinit var etNewEventDescription : EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        prefs = SharedFirebasePreferences.getDefaultInstance(context)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        return inflater.inflate(R.layout.bottom_window_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {

        super.onViewCreated(view, savedInstanceState)
        val today = LocalDateTime.now()




        dateText = view.findViewById(R.id.edit_startDateTV)
        timeText = view.findViewById(R.id.edit_startTimeTV)
        endDateText = view.findViewById(R.id.edit_endDateTV)
        endTimeText = view.findViewById(R.id.edit_endTimeTV)
        swIsAllDay = view.findViewById(R.id.edit_swIsAllDay)
        etNewEventName = view.findViewById<EditText>(R.id.edit_etNewEventName)
        etNewEventDescription = view.findViewById<EditText>(R.id.edit_etNewEventDescription)

        SetupView()



        //Handle Click on StartDate
        dateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { _, year, month, day -> //Showing the picked value in the textView
                    val adjMonth = month + 1
                    dateText.text = "$year.$adjMonth.$day"
                    startDateTime =
                        if (timeText.text == "Time") LocalDateTime.of(year, adjMonth, day, 0, 0)
                        else LocalDateTime.of(
                            year, adjMonth, day, startDateTime.hour, startDateTime.minute
                        )
                    if (endDateText.text == "Date"){
                        endDateTime = LocalDateTime.of(startDateTime.year,startDateTime.month,startDateTime.dayOfMonth,0,0)
                        endDateText.text = "${startDateTime.year}.${startDateTime.month.value}.${startDateTime.dayOfMonth}"
                    }

                    Log.d("StartDate", "StartDate: $startDateTime")
                    dateCheck()
                },
                (thisEvent.startTime.year),
                (thisEvent.startTime.month.value - 1),
                (thisEvent.startTime.dayOfMonth)
            )

            datePickerDialog.show()

            /*val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("test")
                .build()
                datePicker.show(parentFragmentManager,"tag")*/
        }


        //Handle Click on EndDate
        endDateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { _, year, month, day -> //Showing the picked value in the textView
                    val adjMonth = month + 1
                    endDateText.text = "$year.$adjMonth.$day"
                    endDateTime =
                        if (endTimeText.text == "Time") LocalDateTime.of(year, adjMonth, day, 0, 0)
                        else LocalDateTime.of(
                            year, adjMonth, day, endDateTime.hour, endDateTime.minute
                        )
                    Log.d("StartDate", "endDate: $endDateTime")
                    dateCheck()
                },
                (thisEvent.endTime.year),
                (thisEvent.endTime.monthValue - 1),
                (thisEvent.endTime.dayOfMonth)
            )

            datePickerDialog.show()
        }
        //Handle Click on StartTime
        timeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(), { _, hour, minute ->
                    timeText.text = "$hour:$minute"
                    startDateTime =
                        if (dateText.text == "Date") LocalDateTime.of(0, 1, 1, hour, minute)
                        else LocalDateTime.of(
                            startDateTime.year,
                            startDateTime.month,
                            startDateTime.dayOfMonth,
                            hour,
                            minute
                        )
                    dateCheck()
                    Log.d("StartDate", "startDate: $startDateTime")
                }, thisEvent.startTime.hour, thisEvent.startTime.minute, false
            )
            timePickerDialog.show()
        }

        //Handle Click on EndTime
        endTimeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(), { _, hour, minute ->
                    endTimeText.text = "$hour:$minute"
                    endDateTime =
                        if (endDateText.text == "Date") LocalDateTime.of(0, 1, 1, hour, minute)
                        else LocalDateTime.of(
                            endDateTime.year,
                            endDateTime.month,
                            endDateTime.dayOfMonth,
                            hour,
                            minute
                        )
                    dateCheck()
                    Log.d("StartDate", "endDate: $endDateTime")
                }, thisEvent.endTime.hour, thisEvent.endTime.minute, false
            )
            timePickerDialog.show()
        }
        //Spinner
        loadColor()

        //Cancel Button
        val cancelBtn = view.findViewById<Button>(R.id.edit_cancelBtn)
        cancelBtn.setOnClickListener {
            this.dismiss()
        }

        //Switch
        swIsAllDay.setOnClickListener {
            if (swIsAllDay.isChecked) {
                timeText.visibility = View.INVISIBLE
                endTimeText.visibility = View.INVISIBLE
            } else {
                timeText.visibility = View.VISIBLE
                endTimeText.visibility = View.VISIBLE
            }
            dateCheck()
        }


        //Color select
        val colorText = view.findViewById<TextView>(R.id.edit_colorTextView)
        colorText.setOnClickListener {
            val spinner = view.findViewById<Spinner>(R.id.edit_colorSpinner)
            spinner?.performClick()

        }

        // TODO: Cannot edit Description after new event input
        // TODO: (later) allow user to choose which calendar to add to
        // TODO: Validate input fields

        //Save Button
        val saveBtn = view.findViewById<Button>(R.id.edit_saveBtn)
        saveBtn.setOnClickListener {

            var startHour = 0
            var startMinute = 0
            var endHour = 0
            var endMinute = 0

            val (startYear, startMonth, startDay) = dateText.text.split(".").map { it.toInt() }
            if (!swIsAllDay.isChecked) {
                val (sHour, sMinute) = timeText.text.split(":").map { it.toInt() }
                startHour = sHour
                startMinute = sMinute
            }

            val (endYear, endMonth, endDay) = endDateText.text.split(".").map { it.toInt() }
            if (!swIsAllDay.isChecked) {
                val (eHour, eMinute) = endTimeText.text.split(":").map { it.toInt() }
                endHour = eHour
                endMinute = eMinute
            }

            var defaultCalendar = prefs.getString("default_calendar", null)
            //Log.i(BottomSheetFragment.TAG, defaultCalendar.toString())
            if (defaultCalendar.isNullOrBlank()) {
                defaultCalendar = (activity as MainActivity).getCalendarId()
            }


            val newEvent = hashMapOf(
                "longId" to Random.nextLong(until = Long.MAX_VALUE),
                "calendarId" to defaultCalendar,
                "title" to etNewEventName.text.toString(),
                "description" to etNewEventDescription.text.toString(),
                "isAllDay" to swIsAllDay.isChecked,
                "startTimestamp" to LocalDateTime.of(
                    startYear, startMonth, startDay, startHour, startMinute
                ).toString(),
                "endTimestamp" to LocalDateTime.of(
                    endYear, endMonth, endDay, endHour, endMinute
                ).toString(),
                "color" to selectedColor.code
            )
//
            val db = Firebase.firestore
            db.collection("events").add(newEvent).addOnSuccessListener { documentReference ->
                //Log.d(BottomSheetFragment.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                documentReference.get().addOnSuccessListener { result ->
                    val event = result.toObject(Event::class.java)
                    if (event is Event) {
                        event.id = result.id
                        event.startTime =
                            LocalDateTime.parse(result.get("startTimestamp").toString())
                        event.endTime = LocalDateTime.parse(result.get("endTimestamp").toString())
                        firebaseViewModel.addEventToCalendar(event)
                    }
                    firebaseViewModel.getCurrentMonthEvents()

                    this.dismiss()
                }
            }.addOnFailureListener { e ->
                //Log.w(BottomSheetFragment.TAG, "Error adding document", e)
            }

        }

        dateCheck()


    }

    fun parseEvent(event:Event){
        thisEvent = event
    }
    fun SetupView(){
        dateText.text = thisEvent.startTime.toLocalDate().toString()
        timeText.text = "${thisEvent.startTime.hour}:${thisEvent.startTime.minute}"
        endDateText.text = thisEvent.endTime.toLocalDate().toString()
        endTimeText.text = "${thisEvent.endTime.hour}:${thisEvent.endTime.minute}"
        swIsAllDay.isChecked = thisEvent.isAllDay
        etNewEventName.setText(thisEvent.title)
        etNewEventDescription.setText(thisEvent.description)

        startDateTime = thisEvent.startTime
        endDateTime = thisEvent.endTime

    }

    fun dateCheck() {
        Log.d("dateCheck", "Checking Date")
        val saveBtn = view?.findViewById<Button>(R.id.edit_saveBtn)
        //Do nothing if not yet picked all date
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
            startDateTime = LocalDateTime.of(
                startDateTime.year,
                startDateTime.month,
                startDateTime.dayOfMonth,
                0,
                0
            )
            endDateTime =
                LocalDateTime.of(endDateTime.year, endDateTime.month, endDateTime.dayOfMonth, 0, 0)
        }


        if (startDateTime.isAfter(endDateTime)) {
            val startDateLL = view?.findViewById<LinearLayout>(R.id.bottom_window_edit_text2_layout)

            startDateLL?.setBackgroundColor(resources.getColor(R.color.highlight_red))
            saveBtn?.isEnabled = false
        } else {
            val startDateLL = view?.findViewById<LinearLayout>(R.id.bottom_window_edit_text2_layout)
            //val saveBtn = view?.findViewById<Button>(R.id.saveBtn)
            startDateLL?.setBackgroundColor(resources.getColor(R.color.white))
            saveBtn?.isEnabled = true
        }

    }

    companion object {
        private val TAG: String? = BottomSheetFragment::class.java.name

    }


    lateinit var selectedColor: Color

    private fun loadColor() {
        selectedColor = ColorList().default
        val spinner = view?.findViewById<Spinner>(R.id.colorSpinner)
        val colorTextView = view?.findViewById<TextView>(R.id.colorTextView)
        val colorView = view?.findViewById<View>(R.id.colorWindow)


        spinner?.adapter = SpinnerAdapter(requireContext(), ColorList().colors())
        spinner?.setSelection(ColorList().colorPosition(selectedColor), false)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
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

