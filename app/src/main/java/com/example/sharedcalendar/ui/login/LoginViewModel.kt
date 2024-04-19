package com.example.sharedcalendar.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcalendar.R
import com.example.sharedcalendar.data.LoginRepository
import com.example.sharedcalendar.data.Result
import com.example.sharedcalendar.data.SessionManager
import kotlinx.coroutines.launch

/**
 * ViewModel for the login screen.
 * @property loginRepository The login repository to be used.
 * @constructor Creates a new instance of [LoginViewModel].
 * @see LoginRepository
 */
class LoginViewModel(
    private val loginRepository: LoginRepository,
) : ViewModel() {
    // LiveData holds state which is observed by the UI
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

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
                val result = loginRepository.login(username, password, sessionManager)

                if (result is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.user.username))
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            } catch (ex: Exception) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    /**
     * Validates the login data.
     * @param username The username of the user.
     * @param password The password of the user.
     * @see LoginFormState
     */
    fun loginDataChanged(
        username: String,
        password: String,
    ) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    /**
     * Username Validation: Checks if the username is valid.
     * @param username The username to be checked.
     * @return True if the username is valid, false otherwise.
     */
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    /**
     * Password Validation: Checks if the password is valid.
     * @param password The password to be checked.
     * @return True if the password is valid, false otherwise.
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
