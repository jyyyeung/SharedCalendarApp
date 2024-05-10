package com.example.sharedcalendar.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.R
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Share
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ShareCalendarFragment : DialogFragment() {
    //    private val viewModel by viewModels<EventViewModel>()
    private var toolbar: Toolbar? = null
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var shareViewModel: ShareViewModel
    private val scopes: List<String> = listOf(
        "Availability", "View", "Edit", "Full"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_share_calendar, container, false)
        toolbar = view.findViewById(R.id.toolbar)

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        shareViewModel = ViewModelProvider(this)[ShareViewModel::class.java]

        val selectCalendar: TextInputLayout = view.findViewById(R.id.selectCalendar)
        val dropdownSelectCalendar: MaterialAutoCompleteTextView =
            view.findViewById(R.id.dropdownSelectCalendar)
        val shareUserEmail: TextInputLayout = view.findViewById(R.id.shareUserEmail)
        val etUserEmail: EditText = view.findViewById(R.id.etUserEmail)
        val shareScope: TextInputLayout = view.findViewById(R.id.shareScope)
        val dropdownShareScope: MaterialAutoCompleteTextView =
            view.findViewById(R.id.dropdownShareScope)

        val calendarList = firebaseViewModel.calendars.value?.map { c -> c.name }?.toTypedArray()
        if (calendarList != null) {
            dropdownSelectCalendar.setSimpleItems(calendarList)
        }

        firebaseViewModel.calendars.observe(this) { calendars ->
            Log.i(TAG, calendars.toString())
            dropdownSelectCalendar.setSimpleItems(calendars.map { c -> c.name }.toTypedArray())
        }


        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_dropdown_item_1line,
            scopes
        )
        dropdownShareScope.setText("Availability", false)
        dropdownShareScope.setAdapter(adapter)

        val btnShare: Button = view.findViewById(R.id.btnShare)
        btnShare.setOnClickListener {
            var calendar: Calendar? = null

            if (firebaseViewModel.calendars.value?.map { c -> c.name }?.toTypedArray()
                    ?.contains(dropdownSelectCalendar.text.toString()) != true
            ) {
                selectCalendar.error = "Cannot find calendar with this name"
                return@setOnClickListener
            } else {
                selectCalendar.error = null
                val calendarName = dropdownSelectCalendar.text.toString()
                calendar = firebaseViewModel.calendars.value?.filter { c -> c.name == calendarName }
                    ?.get(0)
            }

            if (!shareViewModel.isEmailValid(etUserEmail.text)) {
                shareUserEmail.error = getString(R.string.invalid_email)
                return@setOnClickListener
            } else if (etUserEmail.text.toString() == FirebaseAuth.getInstance().currentUser?.email) {
                shareUserEmail.error = "You cannot share with yourself"
                return@setOnClickListener
            } else {
                shareUserEmail.error = null
            }


            if (dropdownShareScope.text.toString() !in scopes) {
                shareScope.error = "Invalid Share Scope"
                return@setOnClickListener
            } else {
                shareScope.error = null
            }

            val userEmail: String = etUserEmail.text.toString()
            val calendarId = calendar?.id.toString()
            val scope = dropdownShareScope.text.toString()

            // Save Share
            val db = Firebase.firestore
            val newShare: Share = Share(calendarId, userEmail, scope)
            firebaseViewModel.createShare(newShare).also {
                this.dismiss()
            }
//            db.collection("shares")
//                .where(
//                    Filter.and(
//                        Filter.equalTo("calendarId", calendarId),
//                        Filter.equalTo("userEmail", userEmail)
//                    )
//                ).get().addOnSuccessListener { results ->
//                    Log.i(TAG, results.toString())
//                    if (results.isEmpty) {
//                        val newShare = hashMapOf(
//                            "calendarId" to calendarId,
//                            "userEmail" to userEmail,
//                            "scope" to scope
//                        )
//                        // Share does not exist
//                        db.collection("shares").add(newShare).addOnSuccessListener { result ->
//                            Log.d(TAG, "DocumentSnapshot successfully written!")
//                            this.dismiss()
//                        }.addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//
//                    } else {
//                        val share = results.documents[0]
//
//                        db.collection("shares").document(share.id)
//                            .set(
//                                hashMapOf("scope" to scope),
//                                SetOptions.merge()
//                            ).addOnSuccessListener {
//                                Log.d(TAG, "DocumentSnapshot successfully written!")
//                                this.dismiss()
//                            }.addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
//                    }
//                    firebaseViewModel.getShares(firebaseViewModel.calendars.value!!)
//
//                }


        }

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
        toolbar!!.setOnMenuItemClickListener { item: MenuItem? ->
            Log.d(TAG, item.toString())
//            if (item.toString() == "save") {
//                if (saveNewShare()) dismiss()
//            }

            true
        }


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