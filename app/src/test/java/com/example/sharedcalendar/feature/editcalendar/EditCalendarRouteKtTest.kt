package com.example.sharedcalendar.feature.editcalendar

import androidx.compose.runtime.Composable
import com.example.sharedcalendar.BaseTest
import org.junit.Assert.*
import com.example.sharedcalendar.models.Calendar
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EditCalendarRouteKtTest : BaseTest() {
    private lateinit var mockOnBackButtonClicked: () -> Unit
    override fun extendedSetup() {
        mockOnBackButtonClicked = mockk<() -> Unit>()
        every { mockOnBackButtonClicked.invoke() } just Runs

    }


    @Test
    fun test_modifies_values_in_db_when_calendar_id_not_null() {
        // Set up test data
        val calendar = Calendar(id = "123", name = "Test Calendar")
        val editedFields: HashMap<String, Any> = hashMapOf("name" to "New Name")

        // Call the function under test
        editCalendar(calendar, editedFields, spykFirebaseViewModel, mockOnBackButtonClicked)

        // Verify that the editCalendar function is called with the correct parameters
        coVerify {
            spykFirebaseViewModel.editCalendar("123", hashMapOf("name" to "New Name"))
        }

        // Verify that the onBackButtonClicked function is called
        verify {
            mockOnBackButtonClicked.invoke()
        }
    }

    // Calls editCalendar function with correct parameters
    @Test
    fun test_calls_editCalendar_function_with_correct_parameters() {
        // Set up test data
        val calendar = Calendar(id = "123", name = "Test Calendar")
        val editedFields: HashMap<String, Any> = hashMapOf("name" to "New Name")

        // Call the function under test
        editCalendar(calendar, editedFields, spykFirebaseViewModel, mockOnBackButtonClicked)

        // Verify that the editCalendar function is called with the correct parameters
        coVerify {
            spykFirebaseViewModel.editCalendar("123", hashMapOf("name" to "New Name"))
        }
    }

    // Executes onBackButtonClicked function after editing calendar
    @Test
    fun test_executes_onBackButtonClicked_function_after_editing_calendar() {
        // Set up test data
        val calendar = Calendar(id = "123", name = "Test Calendar")
        val editedFields: HashMap<String, Any> = hashMapOf("name" to "New Name")

        // Call the function under test
        editCalendar(calendar, editedFields, spykFirebaseViewModel, mockOnBackButtonClicked)

        // Verify that the onBackButtonClicked function is called
        verify {
            mockOnBackButtonClicked.invoke()
        }
    }

    // Does not modify values in db when calendar id is null
    @Test
    fun test_does_not_modify_values_in_db_when_calendar_id_null() {
        // Set up test data
        val calendar = Calendar(name = "Test Calendar")
        val editedFields: HashMap<String, Any> = hashMapOf("name" to "New Name")

        // Call the function under test
        editCalendar(calendar, editedFields, spykFirebaseViewModel, mockOnBackButtonClicked)

        // Verify that the editCalendar function is not called
        coVerify(exactly = 0) {
            spykFirebaseViewModel.editCalendar(any(), any())
        }
    }

    // Does not modify values in db when editedFields is empty
    @Test
    fun test_does_not_modify_values_in_db_when_editedFields_empty() {
        // Set up test data
        val calendar = Calendar(id = "123", name = "Test Calendar")
        val editedFields: HashMap<String, Any> = hashMapOf<String, Any>()

        // Call the function under test
        editCalendar(calendar, editedFields, spykFirebaseViewModel, mockOnBackButtonClicked)

        // Verify that the editCalendar function is not called
        coVerify(exactly = 0) {
            spykFirebaseViewModel.editCalendar(any(), any())
        }
    }


    // If the calendar name and description are not changed, do not call 'editCalendar' function and return immediately.
    @Test
    fun test_calendar_name_and_description_not_changed() {
        // Arrange
        val calendarName = "default"
        val calendarDescription = ""
        val calendar = Calendar()
        val editedFields = HashMap<String, Any>()
        var onBackButtonClickedCalled = false
        val onBackButtonClicked: () -> Unit = { onBackButtonClickedCalled = true }
        every { spykFirebaseViewModel.editCalendar(any(), any()) } just Runs
        // Act
        onClickSaveButton(
            calendarName,
            calendarDescription,
            calendar,
            editedFields,
            spykFirebaseViewModel,
            onBackButtonClicked
        )

        // Assert
        assertTrue(onBackButtonClickedCalled)
        verify(inverse = true) { spykFirebaseViewModel.editCalendar(any(), any()) }
        assertTrue(editedFields.isEmpty())
    }

    // If the calendar name is changed, add the new name to 'editedFields' and call 'editCalendar' function.
    @Test
    fun test_calendar_name_changed() {
        // Arrange
        val calendarName = "New Calendar Name"
        val calendarDescription = ""
        val calendar = Calendar()
        val editedFields = HashMap<String, Any>()
        var onBackButtonClickedCalled = false
        val onBackButtonClicked: () -> Unit = { onBackButtonClickedCalled = true }

        // Act
        onClickSaveButton(
            calendarName,
            calendarDescription,
            calendar,
            editedFields,
            spykFirebaseViewModel,
            onBackButtonClicked
        )

        // Assert
        assertFalse(onBackButtonClickedCalled)
        assertEquals(1, editedFields.size)
        assertEquals(calendarName, editedFields["name"])
    }

    // If the calendar description is changed, add the new description to 'editedFields' and call 'editCalendar' function.
    @Test
    fun test_calendar_description_changed() {
        // Arrange
        val calendarName = "default"
        val calendarDescription = "New Calendar Description"
        val calendar = Calendar()
        val editedFields = HashMap<String, Any>()
        var onBackButtonClickedCalled = false
        val onBackButtonClicked: () -> Unit = { onBackButtonClickedCalled = true }

        // Act
        onClickSaveButton(
            calendarName,
            calendarDescription,
            calendar,
            editedFields,
            spykFirebaseViewModel,
            onBackButtonClicked
        )

        // Assert
        assertFalse(onBackButtonClickedCalled)
        assertEquals(1, editedFields.size)
        assertEquals(calendarDescription, editedFields["description"])
    }

    // If both calendar name and description are changed, add both to 'editedFields' and call 'editCalendar' function.
    @Test
    fun test_calendar_name_and_description_changed() {
        // Arrange
        val calendarName = "New Calendar Name"
        val calendarDescription = "New Calendar Description"
        val calendar = Calendar()
        val editedFields = HashMap<String, Any>()
        var onBackButtonClickedCalled = false
        val onBackButtonClicked: () -> Unit = { onBackButtonClickedCalled = true }

        // Act
        onClickSaveButton(
            calendarName,
            calendarDescription,
            calendar,
            editedFields,
            spykFirebaseViewModel,
            onBackButtonClicked
        )

        // Assert
        assertFalse(onBackButtonClickedCalled)
        assertEquals(2, editedFields.size)
        assertEquals(calendarName, editedFields["name"])
        assertEquals(calendarDescription, editedFields["description"])
    }

    // If 'calendarName' is null or empty, do not add it to 'editedFields' and do not call 'editCalendar' function.
    @Test
    fun test_calendar_name_null_or_empty() {
        // Arrange
        val calendarName = ""
        val calendarDescription = ""
        val calendar = Calendar()
        val editedFields = HashMap<String, Any>()
        var onBackButtonClickedCalled = false
        val onBackButtonClicked: () -> Unit = { onBackButtonClickedCalled = true }
        every { spykFirebaseViewModel.editCalendar(any(), any()) } just Runs

        // Act
        onClickSaveButton(
            calendarName,
            calendarDescription,
            calendar,
            editedFields,
            spykFirebaseViewModel,
            onBackButtonClicked
        )

        // Assert
        assertTrue(onBackButtonClickedCalled)
        verify(inverse = true) { spykFirebaseViewModel.editCalendar(any(), any()) }
        assertTrue(editedFields.isEmpty())
    }

}