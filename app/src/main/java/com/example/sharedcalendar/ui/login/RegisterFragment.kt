package com.example.sharedcalendar.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcalendar.MainActivity
import com.example.sharedcalendar.R
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.datetime.TimeZone

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth

    companion object {
        private val TAG: String? = RegisterFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Set variables for views
        val loUsername = view.findViewById<TextInputLayout>(R.id.loRegisterUsername)
        val etUsername = view.findViewById<TextInputEditText>(R.id.etRegisterUsername)
        val loEmail = view.findViewById<TextInputLayout>(R.id.loRegisterEmail) // binding.username
        val etEmail = view.findViewById<TextInputEditText>(R.id.etRegisterEmail) // binding.username
        val loPassword = view.findViewById<TextInputLayout>(R.id.loRegisterPassword)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etRegisterPassword)
        val loConfirmPassword = view.findViewById<TextInputLayout>(R.id.loRegisterConfirmPassword)
        val etConfirmPassword = view.findViewById<TextInputEditText>(R.id.etRegisterConfirmPassword)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val pbLoading = view.findViewById<CircularProgressIndicator>(R.id.pbLoading)

        auth = Firebase.auth


        // Listen to changes in Email input
        etUsername.setOnKeyListener { _, _, _ ->
//            Log.d("LoginFragment", etEmailInput.text.toString())
            if (userViewModel.isUsernameValid(etUsername.text!!)) {
                // Clear Error
                loUsername.error = null
            } else {
                loUsername.error = getString(R.string.invalid_username)
            }
            false
        }

        // Listen to changes in Email input
        etEmail.setOnKeyListener { _, _, _ ->
//            Log.d("LoginFragment", etEmailInput.text.toString())
            if (userViewModel.isEmailValid(etEmail.text!!)) {
                // Clear Error
                loEmail.error = null
            } else {
                loEmail.error = getString(R.string.invalid_email)
            }
            false
        }

        // Listen to changes in Password input
        etPassword.setOnKeyListener { _, _, _ ->
            if (userViewModel.isPasswordValid(etPassword.text!!)) {
                // Clear Error
                loPassword.error = null
            } else {
                loPassword.error = getString(R.string.invalid_password)
            }
            false
        }


        // Listen to changes in Password input
        etConfirmPassword.setOnKeyListener { _, _, _ ->
            if (userViewModel.valuesAreEqual(etPassword.text!!, etConfirmPassword.text!!)) {
                // Clear Error
                loConfirmPassword.error = null
            } else {
                loConfirmPassword.error = getString(R.string.passwords_are_not_equal)
            }
            false
        }

        // Listen to Done action on keyboard
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> register(
                    etEmail.text.toString(), etPassword.text.toString(), etUsername.text.toString()
                )
            }
            false
        }


        // Listen to Login btn click event
        btnRegister?.setOnClickListener {
            // On login clicked
            //Verify Data validity
            if (!userViewModel.isEmailValid(etEmail?.text!!) || !userViewModel.isPasswordValid(
                    etPassword?.text!!
                ) || !userViewModel.isUsernameValid(etUsername?.text!!) || !userViewModel.valuesAreEqual(
                    etPassword.text!!, etConfirmPassword?.text!!
                )
            ) {
                return@setOnClickListener
            }
            // Show circular loader
            pbLoading?.visibility = View.VISIBLE

            // Call Register process
            register(
                etEmail.text.toString(), etPassword.text.toString(), etUsername.text.toString()
            )
            pbLoading?.visibility = View.GONE
        }
        return view
    }

    private fun register(email: String, password: String, username: String) {
        val loUsername = view?.findViewById<TextInputLayout>(R.id.loRegisterUsername)
        val loEmail = view?.findViewById<TextInputLayout>(R.id.loRegisterEmail) // binding.username
        val loPassword = view?.findViewById<TextInputLayout>(R.id.loRegisterPassword)
        val loConfirmPassword = view?.findViewById<TextInputLayout>(R.id.loRegisterConfirmPassword)

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                if (user != null) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
//                                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                    }
                    user.updateProfile(profileUpdates).addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            Log.d(TAG, "User profile updated.")
                        }
                    }

                    // Add user public information to users database
                    val userDetails = hashMapOf(
                        "name" to username, "email" to email
                    )
                    Firebase.firestore.collection("users").document(user.uid).set(userDetails)

                    // Create default calendar for user
                    createDefaultCalendar()

                    updateUiWithUser(user)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                showLoginFailed(R.string.register_failed)
                loEmail?.error = " "
                if (task.exception.toString().contains("email")) loEmail?.error =
                    task.exception?.localizedMessage

            }
        }
    }

    private fun createDefaultCalendar() {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser ?: return
        val defaultCalendar = hashMapOf(
            "name" to user.email,
            "color" to "#7886CB",
            "timezone" to TimeZone.currentSystemDefault().id,
            "ownerId" to user.uid,
        )
        // Add a new document with a generated ID
        db.collection("calendars").add(defaultCalendar).addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
    }

    // Called when User login successful
    private fun updateUiWithUser(model: FirebaseUser) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName ?: model.email
        // TODO : initiate successful logged in experience

        Toast.makeText(
            requireActivity().application,
            "$welcome $displayName",
            Toast.LENGTH_LONG,
        ).show()
    }

    // Called when user login failed
    private fun showLoginFailed(
        @StringRes errorString: Int,
    ) {
        Toast.makeText(requireActivity().application, errorString, Toast.LENGTH_SHORT).show()
    }

}