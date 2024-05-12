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

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : Fragment() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Set variables for views
        val etEmail = view.findViewById<TextInputLayout>(R.id.login_email) // binding.username
        val etEmailInput =
            view.findViewById<TextInputEditText>(R.id.etUserEmail) // binding.username
        val etPassword = view.findViewById<TextInputLayout>(R.id.login_password)
        val etPasswordInput = view.findViewById<TextInputEditText>(R.id.etLoginPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val pbLoading = view.findViewById<CircularProgressIndicator>(R.id.pbLoading)

        // Listen to changes in Email input
        etEmailInput.setOnKeyListener { _, _, _ ->
            if (userViewModel.isEmailValid(etEmailInput.text!!)) {
                // Clear Error
                etEmail.error = null
            } else {
                etEmail.error = getString(R.string.invalid_email)
            }
            false
        }

        // Listen to Done action on keyboard
        etPasswordInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    login(
                        etEmailInput.text.toString(),
                        etPasswordInput.text.toString(),
                    )
            }
            false
        }

        // Listen to Login btn click event
        btnLogin?.setOnClickListener {
            // On login clicked
            // Verify Data validity
            if (!userViewModel.isEmailValid(etEmailInput?.text!!) ||
                !userViewModel.isPasswordValid(
                    etPasswordInput?.text!!,
                )
            ) {
                return@setOnClickListener
            }
            // Show circular loader
            pbLoading?.visibility = View.VISIBLE

            // Call login process
            login(etEmailInput.text.toString(), etPasswordInput.text.toString())
            pbLoading?.visibility = View.GONE
        }

        return view
    }

    /**
     * Performs the login operation.
     */
    fun login(
        email: String,
        password: String,
    ) {
        val etPassword = view?.findViewById<TextInputLayout>(R.id.login_password)
        val etEmail = view?.findViewById<TextInputLayout>(R.id.login_email) // binding.username
        auth.signInWithEmailAndPassword(
            email,
            password,
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                if (user != null) {
                    updateUiWithUser(user)
                    Log.i(TAG, user.toString())
                }
                activity?.finishAffinity()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }.addOnFailureListener { exception ->
            showLoginFailed(R.string.login_failed)
            Log.i(TAG, "Login Failed, $exception.localizedMessage")
            etEmail?.error = " "
            etPassword?.error = "Invalid Email or Password"
        }
    }

    // Called when User login successful
    private fun updateUiWithUser(model: FirebaseUser) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName

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

    companion object {
        private val TAG: String = LoginFragment::class.java.name
    }
}
