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
import com.example.sharedcalendar.data.SessionManager
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
class LoginFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireActivity())
        userViewModel = ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Set variables for views
        val etEmail = view.findViewById<TextInputLayout>(R.id.login_email) // binding.username
        val etEmailInput =
            view.findViewById<TextInputEditText>(R.id.et_login_email) // binding.username
        val etPassword = view.findViewById<TextInputLayout>(R.id.login_password)
        val etPasswordInput = view.findViewById<TextInputEditText>(R.id.et_login_password)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val pbLoading = view.findViewById<CircularProgressIndicator>(R.id.pbLoading)


        // Listen to changes in Email input
        etEmailInput.setOnKeyListener { _, _, _ ->
//            Log.d("LoginFragment", etEmailInput.text.toString())
            if (userViewModel.isEmailValid(etEmailInput.text!!)) {
                // Clear Error
                etEmail.error = null
            } else {
                etEmail.error = getString(R.string.invalid_email)
            }
            false
        }

        // Listen to changes in Password input
        etPasswordInput.setOnKeyListener { _, _, _ ->
            if (userViewModel.isPasswordValid(etPasswordInput.text!!)) {
                // Clear Error
                etPassword.error = null
            } else {
                etPassword.error = getString(R.string.invalid_password)
            }
            false
        }

        // Listen to Done action on keyboard
        etPasswordInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> login(
                    etEmailInput.text.toString(), etPasswordInput.text.toString()
                )
            }
            false
        }


        // Listen to Login btn click event
        btnLogin?.setOnClickListener {
            // On login clicked
            //Verify Data validity
            if (!userViewModel.isEmailValid(etEmailInput?.text!!) || !userViewModel.isPasswordValid(
                    etPasswordInput?.text!!
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


    private fun login(email: String, password: String) {
        val etPassword = view?.findViewById<TextInputLayout>(R.id.login_password)
        val etEmail = view?.findViewById<TextInputLayout>(R.id.login_email) // binding.username
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                if (user != null) {
                    updateUiWithUser(user)
                    Log.i(TAG, user.toString())
                }
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }.addOnFailureListener { exception ->
            showLoginFailed(R.string.login_failed)
            Log.i(TAG, "Login Failed, $exception.localizedMessage")
            etEmail?.error = " "
            etPassword?.error = "Invalid Email or Password"
//            error("Login Failed, $exception.localizedMessage")
//                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
//                    .show()
        }
    }

    // Called when User login successful
    private fun updateUiWithUser(model: FirebaseUser) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
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

    companion object {
        private val TAG: String = LoginFragment::class.java.name
    }
}


