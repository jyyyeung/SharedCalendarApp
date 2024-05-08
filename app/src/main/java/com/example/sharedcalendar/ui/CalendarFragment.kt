package com.example.sharedcalendar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcalendar.BottomSheetFragment
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.MonthViewFragment
import com.example.sharedcalendar.R
import com.example.sharedcalendar.WeekViewFragment
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val prefs: SharedFirebasePreferences =
            SharedFirebasePreferences.getDefaultInstance(activity)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        // Change Fragment from month to week on button click
        // Set variables for change view buttons
        val weekBtn: Button = view.findViewById(R.id.weekBtn)
        val monthBtn: Button = view.findViewById(R.id.monthBtn)

        // Set variables for View Fragments
        val monthViewFragment = MonthViewFragment()
        val weekViewFragment = WeekViewFragment()


        // By default, the view fragment is month view fragment
        childFragmentManager.beginTransaction().apply {
            // Set Default View based on user preferences
            when (prefs?.getString("calendar_view", "month")) {
                "week" -> replace(R.id.flFragment, weekViewFragment)
                "month" -> replace(R.id.flFragment, monthViewFragment)
            }
            commit()
        }

        // When Click Month Button
        monthBtn.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                // Replace fragment with month view fragment
                replace(R.id.flFragment, monthViewFragment)
                addToBackStack(null)
                // commit the change
                commit()
            }
        }
        // When Click Week Button
        weekBtn.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                // Replace fragment with week view fragment
                replace(R.id.flFragment, weekViewFragment)
                addToBackStack(null)
                // Commit the changes
                commit()
            }
        }

        // When Click AddEvent Button
        val addEventBtn: ImageButton = view.findViewById(R.id.addEventBtn)
        val bottomWindow = BottomSheetFragment()

        addEventBtn.setOnClickListener {
            bottomWindow.show(
                childFragmentManager,
                "BottomSheetDialogue"
            )
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CalendarFragment.
         */
        @JvmStatic
        fun newInstance() = CalendarFragment()

        private val TAG: String = CalendarFragment::class.java.name
    }
}