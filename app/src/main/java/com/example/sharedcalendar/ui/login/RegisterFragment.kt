package com.example.sharedcalendar.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireActivity())
        userViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Set variables for views
        val loUsername = view.findViewById<TextInputLayout>(R.id.loRegisterUsername)
        val etUsername = view.findViewById<TextInputEditText>(R.id.etRegisterUsername)
        val loEmail = view.findViewById<TextInputLayout>(R.id.loRegisterEmail) // binding.username
        val etEmail =
            view.findViewById<TextInputEditText>(R.id.etRegisterEmail) // binding.username
        val loPassword = view.findViewById<TextInputLayout>(R.id.loRegisterPassword)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etRegisterPassword)
        val loConfirmPassword = view.findViewById<TextInputLayout>(R.id.loRegisterConfirmPassword)
        val etConfirmPassword = view.findViewById<TextInputEditText>(R.id.etRegisterConfirmPassword)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val pbLoading = view.findViewById<CircularProgressIndicator>(R.id.pbLoading)

        userViewModel.loginResult.observe(
            // Observe changes in login result
            viewLifecycleOwner,
            Observer {
                val loginResult = it ?: return@Observer
//            Log.d(TAG, loginResult.toString())
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

//        // Listen to Done action on keyboard
//        etPassword.setOnEditorActionListener { _, actionId, _ ->
//            when (actionId) {
//                EditorInfo.IME_ACTION_DONE -> loginViewModel.login(
//                    etEmail?.text.toString(),
//                    etPassword.text.toString(),
//                    sessionManager,
//                )
//            }
//            false
//        }


        // Listen to Login btn click event
        btnRegister?.setOnClickListener {
            // On login clicked
            //Verify Data validity
            if (!userViewModel.isEmailValid(etEmail?.text!!) ||
                !userViewModel.isPasswordValid(
                    etPassword?.text!!
                ) || !userViewModel.isUsernameValid(etUsername?.text!!) || !userViewModel.valuesAreEqual(
                    etPassword.text!!,
                    etConfirmPassword?.text!!
                )
            ) {
                return@setOnClickListener
            }
            // Show circular loader
            pbLoading?.visibility = View.VISIBLE

            // Call Register process
            userViewModel.register(
                etUsername.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
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

}