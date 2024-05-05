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
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.example.sharedcalendar.models.Event
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences
import java.time.LocalDateTime
import kotlin.random.Random


class BottomSheetFragment : BottomSheetDialogFragment() {
    //    private val eventViewModel by viewModels<EventViewModel>()
    private lateinit var prefs: SharedFirebasePreferences
    private lateinit var firebaseViewModel: FirebaseViewModel

    lateinit var startDateTime: LocalDateTime
    lateinit var endDateTime: LocalDateTime
    lateinit var dateText: TextView
    lateinit var timeText: TextView
    lateinit var endDateText: TextView
    lateinit var endTimeText: TextView
    lateinit var switchBtn: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        prefs = SharedFirebasePreferences.getDefaultInstance(context)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        return inflater.inflate(R.layout.bottom_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val today = LocalDateTime.now()


        //Handle Click on StartDate
        dateText = view.findViewById<TextView>(R.id.startDateTV)
        timeText = view.findViewById<TextView>(R.id.startTimeTV)
        endDateText = view.findViewById<TextView>(R.id.endDateTV)
        endTimeText = view.findViewById<TextView>(R.id.endTimeTV)
        switchBtn = view.findViewById<Switch>(R.id.addDaySwitch)

        //Handle Click on StartDate
        dateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                OnDateSetListener { datePicker, year, month, day -> //Showing the picked value in the textView
                    val adjMonth = month + 1
                    dateText.text = "$year.$adjMonth.$day"
                    if (timeText.text == "Time")
                        startDateTime = LocalDateTime.of(year, adjMonth, day, 0, 0)
                    else
                        startDateTime = LocalDateTime.of(
                            year,
                            adjMonth,
                            day,
                            startDateTime.hour,
                            startDateTime.minute
                        )
                    Log.d("StartDate", "StartDate: " + startDateTime.toString())
                    dateCheck()
                },
                (LocalDateTime.now().year),
                (LocalDateTime.now().month.value - 1),
                (LocalDateTime.now().dayOfMonth)
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
                OnDateSetListener { datePicker, year, month, day -> //Showing the picked value in the textView
                    val adjMonth = month + 1
                    endDateText.text = "$year.$adjMonth.$day"
                    if (endTimeText.text == "Time")
                        endDateTime = LocalDateTime.of(year, adjMonth, day, 0, 0)
                    else
                        endDateTime = LocalDateTime.of(
                            year,
                            adjMonth,
                            day,
                            endDateTime.hour,
                            endDateTime.minute
                        )
                    Log.d("StartDate", "endDate: " + endDateTime.toString())
                    dateCheck()
                },
                (LocalDateTime.now().year),
                (LocalDateTime.now().month.value - 1),
                (LocalDateTime.now().dayOfMonth)
            )

            datePickerDialog.show()
        }
        //Handle Click on StartTime
        timeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(), OnTimeSetListener { TimePicker, hour, minute ->
                    timeText.text = "$hour:$minute"
                    if (dateText.text == "Date")
                        startDateTime = LocalDateTime.of(0, 1, 1, hour, minute)
                    else
                        startDateTime = LocalDateTime.of(
                            startDateTime.year,
                            startDateTime.month,
                            startDateTime.dayOfMonth,
                            hour,
                            minute
                        )
                    dateCheck()
                    Log.d("StartDate", "startDate: " + startDateTime.toString())
                }, 15, 30, false
            )
            timePickerDialog.show()
        }

        //Handle Click on EndTime
        endTimeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(), OnTimeSetListener { TimePicker, hour, minute ->
                    endTimeText.text = "$hour:$minute"
                    if (endDateText.text == "Date")
                        endDateTime = LocalDateTime.of(0, 1, 1, hour, minute)
                    else
                        endDateTime = LocalDateTime.of(
                            endDateTime.year,
                            endDateTime.month,
                            endDateTime.dayOfMonth,
                            hour,
                            minute
                        )
                    dateCheck()
                    Log.d("StartDate", "endDate: " + endDateTime.toString())
                }, 15, 30, false
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

        //Switch
        val switchBtn = view.findViewById<Switch>(R.id.addDaySwitch)
        switchBtn.setOnClickListener {
            if (switchBtn.isChecked) {
                timeText.visibility = View.INVISIBLE
                endTimeText.visibility = View.INVISIBLE
            } else {
                timeText.visibility = View.VISIBLE
                endTimeText.visibility = View.VISIBLE
            }
            dateCheck()
        }


        //Color select
        val colorText = view.findViewById<TextView>(R.id.colorTextView)
        colorText.setOnClickListener {
            val spinner = view?.findViewById<Spinner>(R.id.colorSpinner)
            spinner?.performClick()

        }

        // TODO: Caannot edit Description after new event input
        // TODO: Do not allow new line in Event name - enter button now dismiss the keyboard
        // TODO: Allow user to select if event is all day - done
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
                        firebaseViewModel.addEventToCalendar(event)
                    }
                    this.dismiss()
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        }



    }

    fun dateCheck() {
        Log.d("dateCheck", "Checking Date")
        val saveBtn = view?.findViewById<Button>(R.id.saveBtn)
        //Do nothing if not yet picked all date
        if (dateText.text == "Date") {
            saveBtn?.isEnabled = false
            return
        }
        if (endDateText.text == "Date") {
            saveBtn?.isEnabled = false
            return
        }
        if (!switchBtn.isChecked) {
            if (timeText.text == "Time") {
                saveBtn?.isEnabled = false
                return
            }
            if (endTimeText.text == "Time") {
                saveBtn?.isEnabled = false
                return
            }
        }


        if (startDateTime.isAfter(endDateTime)) {
            val startDateLL = view?.findViewById<LinearLayout>(R.id.bottom_window_text2_layout)

            startDateLL?.setBackgroundColor(resources.getColor(R.color.highlight_red))
            saveBtn?.isEnabled = false
        } else {
            val startDateLL = view?.findViewById<LinearLayout>(R.id.bottom_window_text2_layout)
            val saveBtn = view?.findViewById<Button>(R.id.saveBtn)
            startDateLL?.setBackgroundColor(resources.getColor(R.color.white))
            saveBtn?.isEnabled = true
        }

    }

    companion object {
        private val TAG: String? = BottomSheetFragment::class.java.name
    }


    lateinit var selectedColor: Color

    fun loadColor() {
        selectedColor = ColorList().default
        val spinner = view?.findViewById<Spinner>(R.id.colorSpinner)
        val colorTextView = view?.findViewById<TextView>(R.id.colorTextView)
        val ColorView = view?.findViewById<View>(R.id.colorWindow)


        spinner?.adapter = SpinnerAdapter(requireContext(), ColorList().Colors())
        spinner?.setSelection(ColorList().colorPosition(selectedColor), false)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                colorTextView?.text = ColorList().Colors()[position].name
                ColorList().Colors()[position].name
                ColorView?.background?.setTint(android.graphics.Color.parseColor(ColorList().Colors()[position].code))
                selectedColor = ColorList().Colors()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


}