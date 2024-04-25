//package com.example.sharedcalendar.ui.login
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.view.inputmethod.EditorInfo
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ProgressBar
//import android.widget.Toast
//import androidx.annotation.StringRes
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.example.sharedcalendar.MainActivity
//import com.example.sharedcalendar.R
//import com.example.sharedcalendar.data.SessionManager
//import com.example.sharedcalendar.databinding.ActivityLoginBinding
//
//class LoginActivity : AppCompatActivity() {
//    private lateinit var loginViewModel: LoginViewModel
//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var sessionManager: SessionManager
//
//    /**
//     * Called when the activity is starting.
//     * This is where most initialization should go: calling [setContentView(int)] to inflate the activity's UI,
//     * using [findViewById] to programmatically interact with widgets in the UI,
//     * and [onRestoreInstanceState] to restore the activity's state from the saved instance state.
//     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in [onSaveInstanceState].
//     */
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Set variables for views
//        val etEmail = findViewById<EditText>(R.id.login_email) // binding.username
//        val etPassword = findViewById<EditText>(R.id.etPassword)
//        val btnLogin = findViewById<Button>(R.id.btnLogin)
//        val pbLoading = findViewById<ProgressBar>(R.id.pbLoading)
//
//        // Init session manager
//        sessionManager = SessionManager(this)
//        // set login view model
//        loginViewModel =
//            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
//
//        // Observe changes in login form state
//        loginViewModel.loginFormState.observe(
//            this@LoginActivity,
//            Observer {
//                val loginState = it ?: return@Observer
//
//                // disable login button unless both username / password is valid
//                btnLogin.isEnabled = loginState.isDataValid
//
//                // Ensure email is valid
//                if (loginState.emailError != null) {
//                    etEmail.error = getString(loginState.emailError)
//                }
//                // Ensure password is valid
//                if (loginState.passwordError != null) {
//                    etPassword.error = getString(loginState.passwordError)
//                }
//            },
//        )
//
//        loginViewModel.loginResult.observe(
//            // Observe changes in login result
//            this@LoginActivity,
//            Observer {
//                val loginResult = it ?: return@Observer
////            Log.d(TAG, loginResult.toString())
//                pbLoading.visibility = View.GONE
//                if (loginResult.error != null) {
//                    showLoginFailed(loginResult.error)
//                }
//                if (loginResult.success != null) {
//                    updateUiWithUser(loginResult.success)
//                    // NOTE: Commented for debugging
////                    startActivity(Intent(this, MainActivity::class.java))
//                }
//                setResult(Activity.RESULT_OK)
//
//                // NOTE: For debugging only, will allow continue even login failed
//                startActivity(Intent(this, MainActivity::class.java))
//                // Complete and destroy login activity once successful
//                finish() // Do not allow user go back to sign in page
//
//            },
//        )
//
//        // Listen to changes in Email input
//        etEmail.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                etEmail.text.toString(),
//                etPassword.text.toString(),
//            )
//        }
//
//        // Listen to changes in Password input
//        etPassword.apply {
//            afterTextChanged {
//                loginViewModel.loginDataChanged(
//                    etEmail.text.toString(),
//                    etPassword.text.toString(),
//                )
//            }
//
//            // Listen to Done action on keyboard
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE -> loginViewModel.login(
//                        etEmail.text.toString(),
//                        etPassword.text.toString(),
//                        sessionManager,
//                    )
//                }
//                false
//            }
//
//            // Listen to Login btn click event
//            btnLogin.setOnClickListener {
//                pbLoading.visibility = View.VISIBLE
//                loginViewModel.login(
//                    etEmail.text.toString(),
//                    etPassword.text.toString(),
//                    sessionManager,
//                )
//            }
//        }
//    }
//
//    // Called when User login successful
//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome)
//        val displayName = model.displayName
//        // TODO : initiate successful logged in experience
//
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG,
//        ).show()
//    }
//
//    // Called when user login failed
//    private fun showLoginFailed(
//        @StringRes errorString: Int,
//    ) {
//        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
//    }
//}
////
/////**
//// * Extension function to simplify setting an afterTextChanged action to EditText components.
//// */
////fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
////    this.addTextChangedListener(
////        object : TextWatcher {
////            override fun afterTextChanged(editable: Editable?) {
////                afterTextChanged.invoke(editable.toString())
////            }
////
////            override fun beforeTextChanged(
////                s: CharSequence,
////                start: Int,
////                count: Int,
////                after: Int,
////            ) {
////            }
////
////            override fun onTextChanged(
////                s: CharSequence,
////                start: Int,
////                before: Int,
////                count: Int,
////            ) {
////            }
////        },
////    )
////}
