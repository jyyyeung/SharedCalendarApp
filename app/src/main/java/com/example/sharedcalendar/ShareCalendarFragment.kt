package com.example.sharedcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.sharedcalendar.models.Calendar


class ShareCalendarFragment : DialogFragment() {
    private var toolbar: Toolbar? = null
    private lateinit var calendars: ArrayList<Calendar>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_share_calendar, container, false)
        toolbar = view.findViewById(R.id.toolbar)

        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar!!.setNavigationOnClickListener { dismiss() }
        toolbar!!.title = "Share Calendar"
        toolbar!!.inflateMenu(R.menu.share_calendar_dialog)
        toolbar!!.setOnMenuItemClickListener { _: MenuItem? ->
            dismiss()
            true
        }


        calendars = MainActivity().getCalendars()
//
//        val selectCalendar: TextInputLayout = view.findViewById(R.id.selectCalendar)
//        val dropdownSelectCalendar: MaterialAutoCompleteTextView =
//            view.findViewById(R.id.dropdownSelectCalendar)
//
//        Log.i(TAG, calendars.toString())
//
//        dropdownSelectCalendar.setSimpleItems(calendars.map { c -> c.name }.toTypedArray())

    }

    companion object {
        private val TAG: String? = ShareCalendarFragment::class.java.name
        fun display(
            fragmentManager: FragmentManager?
        ): ShareCalendarFragment {
            val shareCalendarFragment = ShareCalendarFragment()
            shareCalendarFragment.show(fragmentManager!!, TAG)
            return shareCalendarFragment
        }
    }
}