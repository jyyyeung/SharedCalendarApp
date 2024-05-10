import android.text.Editable
import androidx.core.util.PatternsCompat
import com.example.sharedcalendar.ui.login.UserViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserViewModelTest {

    private lateinit var viewModel: UserViewModel

    @BeforeEach
    fun setup() {
        viewModel = UserViewModel()
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
    }

    private val mockPassword = mockk<Editable>()
    private val mockEmail = mockk<Editable>()
    private val mockUsername = mockk<Editable>()


    @Test
    fun `valuesAreEqual returns true when values are equal`() {
        val value1 = mockk<Editable>()
        every { value1.toString() } returns "value"
        val value2 = mockk<Editable>()
        every { value2.toString() } returns "value"
        assertTrue(viewModel.valuesAreEqual(value1, value2))
    }

    @Test
    fun `valuesAreEqual returns false when values are not equal`() {
        val value1 = mockk<Editable>()
        every { value1.toString() } returns "value1"
        val value2 = mockk<Editable>()
        every { value1.toString() } returns "value2"
        assertFalse(viewModel.valuesAreEqual(value1, value2))
    }

    @Test
    fun `isUsernameValid returns true when username is valid`() {
        val validUsername = "testUsername"

        every { mockUsername.toString() } returns validUsername
        every { mockUsername.isNotBlank() } returns validUsername.isNotBlank()

        assertTrue(viewModel.isUsernameValid(mockUsername))
    }

    @Test
    fun `isUsernameValid returns false when username is not valid`() {
        val invalidUsernames =
            arrayOf("test username", "username ", " username", " test username ", " ", "")

        for (username in invalidUsernames) {
            every { mockUsername.toString() } returns username

            assertFalse(viewModel.isUsernameValid(mockUsername))
        }
    }

    @Test
    fun `isEmailValid returns true when email is valid`() {

        val validEmail = "test@example.com"
        every { mockEmail.toString() } returns validEmail
        every { mockEmail.length } returns validEmail.length

        assertTrue(viewModel.isEmailValid(mockEmail))
    }

    @Test
    fun `isEmailValid returns false when email is empty`() {
        val emptyEmail = ""

        every { mockEmail.toString() } returns emptyEmail
        every { mockEmail.length } returns emptyEmail.length

        assertFalse(viewModel.isEmailValid(mockEmail))
    }

    @Test
    fun `isEmailValid returns false when email is not valid`() {
        val invalidEmail = "test@example"
        every { mockEmail.toString() } returns invalidEmail
        every { mockEmail.length } returns invalidEmail.length

        assertFalse(viewModel.isEmailValid(mockEmail))
    }

    @Test
    fun `isPasswordValid returns true when password is valid`() {
        val validPassword = "password"
        every { mockPassword.toString() } returns validPassword

        assertTrue(viewModel.isPasswordValid(mockPassword))
    }

    @Test
    fun `isPasswordValid returns false when password is not valid`() {
        val invalidPasswords = arrayOf("pass", "", " ", " 1234", "1234 ", " 123 ")
        for (password in invalidPasswords) {
            every { mockPassword.toString() } returns password

            assertFalse(viewModel.isPasswordValid(mockPassword))
        }
    }
}
