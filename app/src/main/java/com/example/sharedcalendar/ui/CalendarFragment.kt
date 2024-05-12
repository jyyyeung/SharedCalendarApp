package com.example.sharedcalendar.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.sharedcalendar.BottomSheetFragment
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.MonthViewFragment
import com.example.sharedcalendar.R
import com.example.sharedcalendar.WeekViewFragment
import com.example.sharedcalendar.ui.editEvent.CalendarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    private lateinit var firebaseViewModel: FirebaseViewModel
    private val calendarViewModel by lazy {
        ViewModelProvider(requireActivity())[CalendarViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val prefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        firebaseViewModel.calendars.observe(requireActivity()) {

            // Get Events from Database
            firebaseViewModel.getEvents()
        }

        firebaseViewModel.getEvents()

        // Change Fragment from month to week on button click
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout_viewMode)

        // Set variables for View Fragments
        val monthViewFragment = MonthViewFragment()
        val weekViewFragment = WeekViewFragment(7)
        val halfWeekViewFragment = WeekViewFragment(3)
        val dayViewFragment = WeekViewFragment(1)
//        val dayViewFragment = DayViewFragment()
        val defaultViewModeFragment =
            when (prefs.getString("${FirebaseAuth.getInstance().uid}|default|view", "month")) {
                "week" -> weekViewFragment
                "month" -> monthViewFragment
                "3-day" -> halfWeekViewFragment
                "day" -> dayViewFragment
                else -> monthViewFragment
            }

        calendarViewModel.viewMode.observe(viewLifecycleOwner) {
            Log.d(TAG, "onCreateView: viewMode: $it")
            when (it) {
                "week" -> tabLayout.getTabAt(1)?.select()
                "month" -> tabLayout.getTabAt(0)?.select()
                "3-day" -> tabLayout.getTabAt(2)?.select()
                "day" -> tabLayout.getTabAt(3)?.select()
            }
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: ${tab?.text}")
                val selectedViewModeFragment = when (tab?.text) {
                    getString(R.string.tabItem_month) -> monthViewFragment
                    getString(R.string.tabItem_week) -> weekViewFragment
                    getString(R.string.tabItem_halfWeek) -> halfWeekViewFragment
                    getString(R.string.tabItem_day) -> dayViewFragment
                    else -> defaultViewModeFragment
                }
                // Handle tab select
                childFragmentManager.beginTransaction().apply {
                    // Replace fragment with month view fragment
                    replace(R.id.frameLayout_calendarFragment, selectedViewModeFragment)
                    addToBackStack(null)
                    // commit the change
                    commit()
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })


        // By default, the view fragment is month view fragment
        childFragmentManager.beginTransaction().apply {
            // Set Default View based on user preferences
            replace(R.id.frameLayout_calendarFragment, defaultViewModeFragment)
            commit()
        }


        // When Click AddEvent Button
        val addEventBtn: FloatingActionButton = view.findViewById(R.id.addEventBtn)
        val bottomWindow = BottomSheetFragment()

        addEventBtn.setOnClickListener {
            if (bottomWindow.isAdded) return@setOnClickListener
            bottomWindow.show(
                childFragmentManager, "BottomSheetDialogue"
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