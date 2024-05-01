package com.example.sharedcalendar

//import java.time.LocalDateTime

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.sharedcalendar.models.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import java.time.LocalDateTime
import kotlin.random.Random


class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var prefs: SharedFirebasePreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        prefs = SharedFirebasePreferences.getDefaultInstance(context)

        return inflater.inflate(R.layout.bottom_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val today = LocalDateTime.now()


        //Handle Click on StartDate
        val dateText = view.findViewById<TextView>(R.id.startDateTV)
        dateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                OnDateSetListener { datePicker, year, month, day -> //Showing the picked value in the textView
                    val monthAdj = month + 1
                    dateText.text = "$year.$monthAdj.$day"
                },
                today.year,
                today.monthValue,
                today.dayOfMonth
            )

            datePickerDialog.show()
        }

        //Handle Click on EndDate
        val endDateText = view.findViewById<TextView>(R.id.endDateTV)
        endDateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                OnDateSetListener { datePicker, year, month, day -> //Showing the picked value in the textView
                    val monthAdj = month + 1
                    endDateText.text = "$year.$monthAdj.$day"
                },
                today.year,
                today.monthValue,
                today.dayOfMonth
            )

            datePickerDialog.show()
        }

        val timeText = view.findViewById<TextView>(R.id.startTimeTV)
        timeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(), OnTimeSetListener { TimePicker, hour, minute ->
                    timeText.text = "$hour:$minute"
                }, today.hour, 0, false
            )
            timePickerDialog.show()
        }
        //Handle Click on EndTime
        val endTimeText = view.findViewById<TextView>(R.id.endTimeTV)
        endTimeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(), OnTimeSetListener { TimePicker, hour, minute ->
                    endTimeText.text = "$hour:$minute"
                }, today.hour, 0, false
            )
            timePickerDialog.show()
        }
        //Spinner
        loadColor()

        //Cancel Button
        val cancelBtn = view.findViewById<Button>(R.id.cancelBtn)
        cancelBtn.setOnClickListener {
            this.dismiss()
        }

        // TODO: Caannot edit Description after new event input
        // TODO: Do not allow new line in Event name
        // TODO: Allow user to select if event is all day
        // TODO: (later) allow user to choose which calendar to add to
        // TODO: Validate input fields
        val etNewEventName = view.findViewById<EditText>(R.id.etNewEventName)
        val etNewEventDescription = view.findViewById<EditText>(R.id.etNewEventDescription)
        //Save Button
        val saveBtn = view.findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            //TODO:Save data to database

            val (sYear, sMonth, sDay) = dateText.text.split(".").map { it.toInt() }
            val (sHour, sMinute) = timeText.text.split(":").map { it.toInt() }

            val (endYear, endMonth, endDay) = endDateText.text.split(".").map { it.toInt() }
            val (endHour, endMinute) = endTimeText.text.split(":").map { it.toInt() }

            var defaultCalendar = prefs.getString("default_calendar", null)
            if (defaultCalendar.isNullOrBlank()) {
                defaultCalendar = (activity as MainActivity).getCalendarId()
            }


            val newEvent = hashMapOf(
                "longId" to Random.nextLong(),
                "calendarId" to defaultCalendar,
                "title" to etNewEventName.text.toString(),
                "description" to etNewEventDescription.text.toString(),
                "startTimestamp" to LocalDateTime.of(
                    sYear, sMonth, sDay, sHour, sMinute
                ).toString(),
                "endTimestamp" to LocalDateTime.of(
                    endYear, endMonth, endDay, endHour, endMinute
                ).toString(),
                "color" to selectedColor.code
            )
//
            val db = Firebase.firestore
            db.collection("events").add(newEvent).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                documentReference.get().addOnSuccessListener { result ->
                    val event = result.toObject(Event::class.java)
                    if (event is Event) {
                        event.id = result.id
                        event.startTime =
                            LocalDateTime.parse(result.get("startTimestamp").toString())
                        event.endTime = LocalDateTime.parse(result.get("endTimestamp").toString())
                        (activity as MainActivity).addEventToCalendar(event)
                    }
                    this.dismiss()
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        }


    }

    companion object {
        private val TAG: String? = BottomSheetFragment::class.java.name
    }


    lateinit var selectedColor: Color

    fun loadColor() {
        selectedColor = ColorList().default
        val spinner = view?.findViewById<Spinner>(R.id.colorSpinner)
        spinner?.adapter = SpinnerAdapter(requireContext(), ColorList().Colors())
        spinner?.setSelection(ColorList().colorPosition(selectedColor), false)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedColor = ColorList().Colors()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


}