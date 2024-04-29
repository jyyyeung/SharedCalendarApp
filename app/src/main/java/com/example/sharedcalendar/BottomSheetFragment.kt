package com.example.sharedcalendar

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Handle Click on StartDate
        val dateText = view.findViewById<TextView>(R.id.startDateTV)
        dateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day -> //Showing the picked value in the textView
                    dateText.setText("$year.$month.$day")
                },
                2023,
                1,
                20
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
                    endDateText.setText("$year.$monthAdj.$day")
                },
                2023,
                1,
                20
            )

            datePickerDialog.show()
        }

        val timeText = view.findViewById<TextView>(R.id.startTimeTV)
        timeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(),
                OnTimeSetListener { TimePicker, hour, minute ->
                    timeText.setText("$hour:$minute")
                },
                15,
                30,
                false
            )
            timePickerDialog.show()
        }
        //Handle Click on EndTime
        val endTimeText = view.findViewById<TextView>(R.id.endTimeTV)
        endTimeText.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.requireContext(),
                OnTimeSetListener { TimePicker, hour, minute ->
                    endTimeText.setText("$hour:$minute")
                },
                15,
                30,
                false
            )
            timePickerDialog.show()
        }
        //Spinner
        loadColor()

        //Cancel Button
        val cancelBtn = view.findViewById<Button>(R.id.cancelBtn)
        cancelBtn.setOnClickListener{
            this.dismiss()
        }

        //Save Button
        val saveBtn = view.findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener{
            //TODO:Save data to database
            this.dismiss()
        }




    }

    lateinit var selectedColor: Color

    fun loadColor()
    {
        selectedColor = ColorList().default
        val spinner = view?.findViewById<Spinner>(R.id.colorSpinner)
        spinner?.adapter = SpinnerAdapter(requireContext(),ColorList().Colors())
        spinner?.setSelection(ColorList().colorPosition(selectedColor),false)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedColor = ColorList().Colors()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


}