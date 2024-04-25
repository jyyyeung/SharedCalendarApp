package com.example.sharedcalendar.ui.login

import android.text.Editable
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcalendar.R
import com.example.sharedcalendar.data.Result
import com.example.sharedcalendar.data.SessionManager
import com.example.sharedcalendar.data.UserRepository
import kotlinx.coroutines.launch

private val TAG: String = LoginViewModel::class.java.name

/**
 * ViewModel for the login screen.
 * @property userRepository The login repository to be used.
 * @constructor Creates a new instance of [LoginViewModel].
 * @see UserRepository
 */
class LoginViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    // LiveData holds state which is observed by the UI
//    private val _loginForm = MutableLiveData<LoginFormState>()
//    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * Logs in the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param sessionManager The session manager to be used.
     * @see SessionManager
     */
    fun login(
        username: String,
        password: String,
        sessionManager: SessionManager,
    ) {
        viewModelScope.launch {
            try {
                // can be launched in a separate asynchronous job
                val result = userRepository.login(username, password, sessionManager)
                Log.i(TAG, "Received login result: $result")

                if (result is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.user.username))
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            } catch (ex: Exception) {
                Log.d(TAG, "Login Failed in loginViewModel: $ex")
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        sessionManager: SessionManager,
    ) {
        viewModelScope.launch {
            try {
                // can be launched in a separate asynchronous job
                val result = userRepository.register(username, email, password, sessionManager)

                if (result is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.user.username))
                } else {
                    _loginResult.value = LoginResult(error = R.string.register_failed)
                }
            } catch (ex: Exception) {
                _loginResult.value = LoginResult(error = R.string.register_failed)
            }
        }
    }

    fun logout(sessionManager: SessionManager) {
        viewModelScope.launch {

            try {
                userRepository.logout(sessionManager = sessionManager)
            } catch (ex: Exception) {
                Log.d(TAG, "Logout Failed: $ex")
            }
        }
    }


    private fun valueIsNotEmpty(value: Editable): Boolean {
        return value.isNotBlank()
    }

    fun valuesAreEqual(value1: Editable, value2: Editable): Boolean {
        return value1.toString() == value2.toString()
    }

    fun isUsernameValid(username: Editable): Boolean {
        return valueIsNotEmpty(username) && !username.contains(' ')
    }

    /**
     * Email Validation: Checks if the email is valid.
     * @param email The email to be checked.
     * @return True if the email is valid, false otherwise.
     */
    fun isEmailValid(email: Editable): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    /**
     * Password Validation: Checks if the password is valid.
     * @param password The password to be checked.
     * @return True if the password is valid, false otherwise.
     */
    fun isPasswordValid(password: Editable): Boolean {
        return password.length > 5
    }
}
