package com.example.sharedcalendar.ui.login

import android.app.Activity
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcalendar.MainActivity
import com.example.sharedcalendar.R
import com.example.sharedcalendar.data.SessionManager
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireActivity())
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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


        loginViewModel.loginResult.observe(
            // Observe changes in login result
            viewLifecycleOwner,
            Observer {
                val loginResult = it ?: return@Observer
                Log.d(TAG, loginResult.toString())
                pbLoading?.visibility = View.GONE
                if (loginResult.error != null) {
                    showLoginFailed(loginResult.error)
                }
                if (loginResult.success != null) {
                    updateUiWithUser(loginResult.success)
                    // NOTE: Commented for debugging
//                    startActivity(Intent(this, MainActivity::class.java))
                }
                activity?.setResult(Activity.RESULT_OK)

                // NOTE: For debugging only, will allow continue even login failed
                startActivity(Intent(activity, MainActivity::class.java))
                // Complete and destroy login activity once successful
                activity?.finish() // Do not allow user go back to sign in page

            },
        )

        // Listen to changes in Email input
        etEmailInput.setOnKeyListener { _, _, _ ->
//            Log.d("LoginFragment", etEmailInput.text.toString())
            if (loginViewModel.isEmailValid(etEmailInput.text!!)) {
                // Clear Error
                etEmail.error = null
            } else {
                etEmail.error = getString(R.string.invalid_email)
            }
            false
        }

        // Listen to changes in Password input
        etPasswordInput.setOnKeyListener { _, _, _ ->
            if (loginViewModel.isPasswordValid(etPasswordInput.text!!)) {
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
                EditorInfo.IME_ACTION_DONE -> loginViewModel.login(
                    etEmailInput?.text.toString(),
                    etPasswordInput.text.toString(),
                    sessionManager,
                )
            }
            false
        }


        // Listen to Login btn click event
        btnLogin?.setOnClickListener {
            // On login clicked
            //Verify Data validity
            if (!loginViewModel.isEmailValid(etEmailInput?.text!!) ||
                !loginViewModel.isPasswordValid(
                    etPasswordInput?.text!!
                )
            ) {
                return@setOnClickListener
            }
            // Show circular loader
            pbLoading?.visibility = View.VISIBLE

            // Call login process
            loginViewModel.login(
                etEmailInput.text.toString(),
                etPasswordInput.text.toString(),
                sessionManager,
            )
        }

        return view
    }


    // Called when User login successful
    private fun updateUiWithUser(model: LoggedInUserView) {
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


