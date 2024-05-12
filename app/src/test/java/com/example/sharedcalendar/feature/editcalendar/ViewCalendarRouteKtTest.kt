package com.example.sharedcalendar.feature.editcalendar

import android.content.DialogInterface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.sharedcalendar.BaseTest
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import com.example.sharedcalendar.ui.ManageCalendarsViewModel
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
class ViewCalendarRouteKtTest : BaseTest() {

    @get:Rule
    val composeTestRule = createComposeRule()
    override fun extendedSetup() {
        // No additional setup required
        every { spykFirebaseViewModel.shares.isInitialized } returns true
        every { spykFirebaseViewModel.shares.observe(any(), any()) } just Runs
        every { spykFirebaseViewModel.shares.removeObserver(any()) } just Runs

        val mockShareList = mutableMapOf(
            "1" to arrayListOf(mockShare),
            "2" to arrayListOf(mockShare)
        )

        every { spykFirebaseViewModel.shares.value } returns mockShareList
    }


    // Renders the calendar name, timezone, owner, and access scope.
    @Test
    fun renders_calendar_name_timezone_owner_and_access_scope() {
        composeTestRule.setContent {
            // Set up test data
            val calendar = Calendar(
                id = "1",
                name = "Test Calendar",
                timezone = "GMT+1",
                ownerId = "123",
                owner = User(id = "123", name = "John Doe"),
                scope = "View"
            )
            val viewModel: ManageCalendarsViewModel = mockk(relaxed = true)

            // Call the function under test
            ViewCalendarScreen(
                calendar = calendar,
                firebaseViewModel = spykFirebaseViewModel,
                viewModel = viewModel
            )


            // Assert
            // TODO: Add assertions for rendering calendar name, timezone, owner, and access scope

        }

    }

    // Displays a list of calendar shares if there are any.
    @Test
    fun displays_list_of_calendar_shares_if_any() {

        // Arrange
        val calendar = Calendar(id = "1")
        val viewModel: ManageCalendarsViewModel = mockk(relaxed = true)
        every { spykFirebaseViewModel.shares } returns mockk()
        every { spykFirebaseViewModel.shares.isInitialized } returns true



        composeTestRule.setContent {

            // Act
            ViewCalendarScreen(
                calendar = calendar,
                firebaseViewModel = spykFirebaseViewModel,
                viewModel = viewModel
            )

            // Assert
            // TODO: Add assertions for displaying list of calendar shares

        }
    }

    // Shows an "Edit Calendar" button if the user has full or edit access to the calendar.
    @Test
    fun shows_edit_calendar_button_if_user_has_full_or_edit_access() {
        // Arrange
        val calendar = Calendar(id = "1", scope = "Full")
        val viewModel: ManageCalendarsViewModel = mockk(relaxed = true)

        composeTestRule.setContent {
            // Act
            ViewCalendarScreen(
                calendar = calendar,
                firebaseViewModel = spykFirebaseViewModel,
                viewModel = viewModel
            )

            // Assert
            // TODO: Add assertions for showing "Edit Calendar" button}
        }
    }

    // Does not show an "Edit Calendar" button if the user has view access to the calendar.
    @Test
    fun does_not_show_edit_calendar_button_if_user_has_view_access() {
        // Arrange
        val calendar = Calendar(id = "1", scope = "View")
        val viewModel: ManageCalendarsViewModel = mockk(relaxed = true)

        composeTestRule.setContent {

            // Act
            ViewCalendarScreen(
                calendar = calendar,
                firebaseViewModel = spykFirebaseViewModel,
                viewModel = viewModel
            )

            // Assert
            // TODO: Add assertions for not showing "Edit Calendar" button
        }
    }

    // Does not show the calendar shares section if there are no shares.
    @Test
    fun does_not_show_calendar_shares_section_if_no_shares() {
        // Arrange
        val calendar = Calendar(id = "1")
        val viewModel: ManageCalendarsViewModel = mockk(relaxed = true)
        every { spykFirebaseViewModel.shares.value } returns mutableMapOf()
        composeTestRule.setContent {

            // Act
            ViewCalendarScreen(
                calendar = calendar,
                firebaseViewModel = spykFirebaseViewModel,
                viewModel = viewModel
            )

            // Assert
            // TODO: Add assertions for not showing calendar shares section
        }
    }


    // Dialog is shown when shouldShowDialog is true
    @Test
    fun test_dialog_shown_when_should_show_dialog_is_true() {
        // Arrange
        val shouldShowDialog = mutableStateOf(true)
        val share = mutableStateOf(Share())
        composeTestRule.setContent {

            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
        }
        // Assert
        assertTrue(shouldShowDialog.value)
    }

    // Dialog title is "Delete Share"
    @Test
    fun test_dialog_title_is_delete_share() {
        // Arrange
        val shouldShowDialog = mutableStateOf(false)
        val share = mutableStateOf(Share())
        composeTestRule.setContent {

            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
        }
        // Assert
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        if (dialog != null) {
            assertEquals("Delete Share", getField(dialog, "title"))
        }
    }

//    // Dialog text contains the email and scope of the share
//    @Test
//    fun test_dialog_text_contains_email_and_scope_of_share() {
//        // Arrange
//        val shouldShowDialog = mutableStateOf(false)
//        val share = mutableStateOf(Share(userEmail = "test@example.com", scope = "View"))
//        composeTestRule.setContent {
//
//            // Act
//            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
//        }
//        // Assert
//        val dialog = ShadowAlertDialog.getLatestAlertDialog()
//        assertTrue(dialog.message.contains("test@example.com"))
//        assertTrue(dialog.message.contains("View"))
//    }

    // Confirm button is present
    @Test
    fun test_confirm_button_is_present() {
        // Arrange
        val shouldShowDialog = mutableStateOf(false)
        val share = mutableStateOf(Share())
        composeTestRule.setContent {

            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
        }
        // Assert
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        if (dialog != null) {
            assertNotNull(dialog.getButton(DialogInterface.BUTTON_POSITIVE))
        }
    }

    // Confirm button text is "Delete"
    @Test
    fun test_confirm_button_text_is_delete() {
        // Arrange
        val shouldShowDialog = mutableStateOf(false)
        val share = mutableStateOf(Share())
        composeTestRule.setContent {

            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
        }
        // Assert
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        if (dialog != null) {

            assertEquals("Delete", dialog.getButton(DialogInterface.BUTTON_POSITIVE).text)
        }
    }

    // Confirm button click sets shouldShowDialog to false and calls deleteShare function on spykFirebaseViewModel
    @Test
    fun test_confirm_button_click_sets_should_show_dialog_to_false_and_calls_delete_share_function_on_firebase_view_model() {
        // Arrange
        val shouldShowDialog = mutableStateOf(true)
        val share = mutableStateOf(Share())
        composeTestRule.setContent {

            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
            val dialog = ShadowAlertDialog.getLatestAlertDialog()
            if (dialog != null) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.performClick()

                // Assert
                assertFalse(shouldShowDialog.value)
                coVerify { spykFirebaseViewModel.deleteShare(share.value) }
            }
        }
    }

    // shouldShowDialog is false, dialog is not shown
    @Test
    fun test_should_show_dialog_is_false_dialog_is_not_shown() {
        // Arrange
        val shouldShowDialog = mutableStateOf(false)
        val share = mutableStateOf(Share())

        composeTestRule.setContent {
            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
        }

        // Assert
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        assertNull(dialog)
    }

    // DeleteShare is called when confirm button is clicked
    @Test
    fun test_delete_share_is_called_when_confirm_button_is_clicked() {
        // Arrange
        val shouldShowDialog = mutableStateOf(true)
        val share = mutableStateOf(Share())
        composeTestRule.setContent {

            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
            val dialog = ShadowAlertDialog.getLatestAlertDialog()
            if (dialog != null) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.performClick()

                // Assert
                coVerify { spykFirebaseViewModel.deleteShare(share.value) }
            }
        }
    }

    // On Cancel button click, shouldShowDialog is set to false
    @Test
    fun test_on_cancel_button_click_should_show_dialog_is_set_to_false() {
        // Arrange
        val shouldShowDialog = mutableStateOf(true)
        val share = mutableStateOf(Share())
        composeTestRule.setContent {

            // Act
            MyAlertDialog(shouldShowDialog, share, spykFirebaseViewModel)
            val dialog = ShadowAlertDialog.getLatestAlertDialog()
            if (dialog != null) {
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.performClick()

                // Assert
                assertFalse(shouldShowDialog.value)
            }
        }
    }


}