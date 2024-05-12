package com.example.sharedcalendar

import MainDispatcherRule
import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class FirebaseViewModelTest : BaseTest() {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()


    private lateinit var viewModel: FirebaseViewModel

    // 1. Mock Context and SharePreference

    private val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
    private val sharedPrefsEditor =
        mockk<SharedPreferences.Editor>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)

    private val userId = "testUserId"
    val preKey = "USER_ID"

    private val userMock = mockk<FirebaseUser>(relaxed = true)
    private val databaseMock = mockk<DatabaseReference>(relaxed = true)
    override fun extendedSetup() {
        mockFirestore = mockk(relaxed = true)
        viewModel = spyk(FirebaseViewModel(mockAuth, mockFirestore))


        // 2. Use every returns to return mocking sharedPreference.
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.edit() }
            .returns(sharedPrefsEditor)
        every { sharedPrefsEditor.putString(any(), any()) }
            .returns(sharedPrefsEditor)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `createCalendar adds calendar to firestore`() {
        coEvery { viewModel.user.uid } returns userId
        viewModel.createCalendar(userId, sharedPrefs, mockCalendar)

        coVerify { mockFirestore.collection("calendars").add(mockCalendar) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `editCalendar updates calendar fields`() {
//        val calendarId = "testCalendarId"
        val calendarId = mockCalendar.id ?: "testCalendarId"
        val editedFields: HashMap<String, Any> = hashMapOf("name" to "New Name")

        viewModel.editCalendar(calendarId, editedFields)

        coVerify {
            mockFirestore.collection("calendars").document(calendarId)
                .set(editedFields, SetOptions.merge())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteCalendar removes calendar from firestore`() {
        val calendarId = mockCalendar.id ?: "testCalendarId"

        viewModel.deleteCalendar(calendarId)

        coVerify { mockFirestore.collection("calendars").document(calendarId).delete() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addEventToCalendar adds event to firestore`() {
        val newEvent: HashMap<String, Any?> = hashMapOf(
            "calendarId" to "testCalendarId",
            "title" to "Test Event",
            "description" to "This is a test event",
            "start" to LocalDateTime.now(),
            "end" to LocalDateTime.now().plusHours(1)
        )

        viewModel.addEventToCalendar(newEvent)

        coVerify { mockFirestore.collection("events").add(newEvent) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addShare adds share to firestore`() {
//        val share = Share().apply {
//            calendarId = "testCalendarId"
//            userEmail = "test@example.com"
//            scope = "View"
//        }

//        viewModel.addShare(share)
        viewModel.addShare(mockShare)

        coVerify { mockFirestore.collection("shares").add(mockShare) }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `editShare updates share fields`() {
        val shareId = "testShareId"
        val editedFields: HashMap<String, String?> = hashMapOf("scope" to "Edit")

        viewModel.editShare(shareId, editedFields)

        coVerify {
            mockFirestore.collection("shares").document(shareId)
                .set(editedFields, SetOptions.merge())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `createShare does not add share if already exists`() {
        val share = Share().apply {
            calendarId = "testCalendarId"
            userEmail = "test@example.com"
            scope = "read"
        }

        coEvery {
            mockFirestore.collection("shares").whereEqualTo("calendarId", share.calendarId)
                .whereEqualTo("userEmail", share.userEmail).get()
        } returns mockk(relaxed = true)

        viewModel.createShare(share)

        coVerify(inverse = true) { mockFirestore.collection("shares").add(share) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getShares retrieves shares for given calendars`() {
        val calendar = Calendar().apply { id = "testCalendarId" }

        viewModel.getShares(listOf(calendar))

        coVerify {
            mockFirestore.collection("shares").whereEqualTo("calendarId", calendar.id).get()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getUserShares retrieves shares for current user`() {
        val userEmail = "test@example.com"
        coEvery { viewModel.user.email } returns userEmail

        viewModel.getUserShares()

        coVerify { mockFirestore.collection("shares").whereEqualTo("userEmail", userEmail).get() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getCalendars retrieves user calendars`() {
//        val userId = "testUserId"
        coEvery { viewModel.user.uid } returns userId

        viewModel.getCalendars(emptyMap(), true)

        coVerify {
            mockFirestore.collection("calendars").whereEqualTo("ownerId", userId).get()
        }
        coVerify(inverse = true) {
            mockFirestore.collection("calendars")
                .whereIn(FieldPath.documentId(), viewModel.calendarsSharedWithUser).get()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getCalendars should fetch user calendars and not shared calendars`() {
        // Given
        val calendarPrefs = mapOf<String, Any?>()
        coEvery { viewModel.user.uid } returns userId

        mockkStatic(MutableLiveData::class)

        val calendarsSharedWithUser = mutableListOf<String>()

        viewModel.apply {
            this.calendarsSharedWithUser = calendarsSharedWithUser
        }
        // When
        viewModel.getCalendars(calendarPrefs)
        // Then
        coVerify { mockFirestore.collection("calendars") }
        coVerify { mockFirestore.collection("calendars").whereEqualTo("ownerId", userId) }
        coVerify(inverse = true) {
            mockFirestore.collection("calendars").whereIn(FieldPath.documentId(), any())
        }

        // TODO: Cannot verify if post value works
//        coVerify { userCalendars.postValue(any()) }
//        coVerify { sharedCalendars.postValue(any()) }
//        coVerify { enabledCalendars.postValue(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getCalendars should fetch user calendars and shared calendars`() {
        // Given
        val calendarPrefs = mapOf<String, Any?>()
        coEvery { viewModel.user.uid } returns userId

        mockkStatic(MutableLiveData::class)

//        val user = mockk<FirebaseAuth>()
        val db = mockk<FirebaseFirestore>()
        val userCalendars = MutableLiveData<MutableList<Calendar>>()
        val sharedCalendars = MutableLiveData<MutableList<Calendar>>()
        val enabledCalendars = MutableLiveData<MutableList<String>>()
        val userShares = MutableLiveData<MutableList<Share>>()
        val calendarsSharedWithUser = mutableListOf<String>("calendar_id")
        val users = MutableLiveData<MutableMap<String, User>>()
        val shares = MutableLiveData(mutableMapOf<String, ArrayList<Share>>())

//        every { mockAuth.currentUser } returns user
//        coEvery { mockFirestore.collection(any()) } returns mockk()
//        every { user.uid } returns "user_id"
//        coEvery { userCalendars.postValue(any()) } just Runs
//        coEvery { sharedCalendars.postValue(any()) } just Runs
//        coEvery { enabledCalendars.postValue(any()) } just Runs

        viewModel.apply {
//            this.userCalendars = userCalendars
//            this.sharedCalendars = sharedCalendars
//            this.enabledCalendars = enabledCalendars
//            this.userShares = userShares
//            this.users = users
//            this.shares = shares
//            this.user = user

            this.calendarsSharedWithUser = calendarsSharedWithUser
        }

        // When
        viewModel.getCalendars(calendarPrefs)

//        coEvery { calendarsSharedWithUser.isNotEmpty() } returns true

        // Then
        coVerify { mockFirestore.collection("calendars") }
        coVerify { mockFirestore.collection("calendars").whereEqualTo("ownerId", userId) }
        coVerify { mockFirestore.collection("calendars").whereIn(FieldPath.documentId(), any()) }

        // TODO: Cannot verify if post value works
//        coVerify { userCalendars.postValue(any()) }
//        coVerify { sharedCalendars.postValue(any()) }
//        coVerify { enabledCalendars.postValue(any()) }
    }

//
//    @ExperimentalCoroutinesApi
//    @Test
//    fun `getCalendars retrieves user and shared calendars`()  {
////        val userId = "testUserId"
//        coEvery { viewModel.user.uid } returns userId
//        val calendarsSharedWithUserMock = mockk<MutableList<String>>(relaxed = true)
//
//        every { viewModel.calendarsSharedWithUser } returns calendarsSharedWithUserMock
//        coEvery {
//            viewModel.calendarsSharedWithUser
//        } returns calendarsSharedWithUserMock
//        coEvery { viewModel.calendarsSharedWithUser.isNotEmpty() } returns true
//
//
//        viewModel.getCalendars(emptyMap(), true)
//
//        val mockTaskQuerySnapshot = mockk<Task<QuerySnapshot>>(relaxed = true)
//        coEvery {
//            mockFirestore.collection("calendars")
//                .whereIn(FieldPath.documentId(), viewModel.calendarsSharedWithUser).get()
//        } returns mockTaskQuerySnapshot
//        coEvery { mockTaskQuerySnapshot.addOnSuccessListener(any()) } returns mockTaskQuerySnapshot
//
//        coVerify {
//            mockFirestore.collection("calendars").whereEqualTo("ownerId", userId).get()
//        }
//        coVerify {
//            mockFirestore.collection("calendars")
//                .whereIn(FieldPath.documentId(), viewModel.calendarsSharedWithUser).get()
//        }
//    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getCurrentMonthEvents retrieves events for current month`() {
        val calendar = Calendar().apply { id = "testCalendarId" }
        coEvery { viewModel.calendars.value } returns mutableListOf(calendar)

        viewModel.getEvents()

        coVerify {
            mockFirestore.collection("events").whereEqualTo("calendarId", calendar.id).get()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteShare removes share from firestore`() {
        val share = Share().apply { id = "testShareId" }

        viewModel.deleteShare(share)

        coVerify { share.id?.let { mockFirestore.collection("shares").document(it).delete() } }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getUserById returns user when user exists in users LiveData`() {
        val userId = "testUserId"
        val user = mockk<User>(relaxed = true)
        coEvery { user.id } returns userId
        val users = mutableMapOf(userId to user)
        coEvery { viewModel.users.value } returns users
        viewModel.apply {
            this.users = MutableLiveData(users)
        }

        val result = viewModel.getUserById(userId)

        assertEquals(user, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getUserById returns null when user does not exist in users LiveData`() {
        val userId = "testUserId"
        viewModel.apply {
            this.users = MutableLiveData(mutableMapOf())
        }

        val result = viewModel.getUserById(userId)

        assertNull(result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getUserById fetches user from firestore when user does not exist in users LiveData`() =
        runTest {
            val userId = "testUserId"
            viewModel.apply {
                this.users = MutableLiveData(mutableMapOf())
            }

            coEvery { mockFirestore.collection("users").document(userId).get() } returns mockk(
                relaxed = true
            )

            viewModel.getUserById(userId)

            coVerify { mockFirestore.collection("users").document(userId).get() }
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `getGroupedEvents returns correct map when events exist`() {
        val event1 = Event().apply {
            id = "event1"
            startTime = LocalDateTime.of(2022, 1, 1, 10, 0)
            endTime = LocalDateTime.of(2022, 1, 1, 11, 0)
            calendarId = "calendar1"
        }
        val event2 = Event().apply {
            id = "event2"
            startTime = LocalDateTime.of(2022, 1, 2, 10, 0)
            endTime = LocalDateTime.of(2022, 1, 2, 11, 0)
            calendarId = "calendar1"
        }
        val event3 = Event().apply {
            id = "event3"
            startTime = LocalDateTime.of(2022, 1, 1, 12, 0)
            endTime = LocalDateTime.of(2022, 1, 1, 13, 0)
            calendarId = "calendar1"
        }
        val events = mutableListOf(event1, event2, event3)
        viewModel.apply {
            this.events = MutableLiveData(events)
            this.enabledCalendars = MutableLiveData(mutableListOf("calendar1"))
        }

        val result = viewModel.getGroupedEvents()

        assertEquals(2, result?.size)
        assertEquals(2, result?.get(LocalDate.of(2022, 1, 1))?.size)
        assertEquals(1, result?.get(LocalDate.of(2022, 1, 2))?.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getGroupedEvents returns empty map when no events exist`() {
        viewModel.apply {
            this.events = MutableLiveData(mutableListOf())
            this.enabledCalendars = MutableLiveData(mutableListOf())
        }
        val result = viewModel.getGroupedEvents()

        assertTrue(result?.isEmpty() == true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getGroupedEvents returns map without disabled calendars events`() {
        val event1 = Event().apply {
            id = "event1"
            startTime = LocalDateTime.of(2022, 1, 1, 10, 0)
            endTime = LocalDateTime.of(2022, 1, 1, 11, 0)
            calendarId = "calendar1"
        }
        val event2 = Event().apply {
            id = "event2"
            startTime = LocalDateTime.of(2022, 1, 2, 10, 0)
            endTime = LocalDateTime.of(2022, 1, 2, 11, 0)
            calendarId = "calendar2"
        }
        val events = mutableListOf(event1, event2)
        viewModel.apply {
            this.events = MutableLiveData(events)
            this.enabledCalendars = MutableLiveData(mutableListOf("calendar1"))
        }

        val result = viewModel.getGroupedEvents()

        assertEquals(1, result?.size)
        assertEquals(1, result?.get(LocalDate.of(2022, 1, 1))?.size)
        assertNull(result?.get(LocalDate.of(2022, 1, 2)))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `getEventsByCalendar returns empty list when no events exist`() {
        val calendarId = "testCalendarId"
        val scope = "View"

        viewModel.apply {
//            this.enabledCalendars = MutableLiveData(mutableListOf(calendarId))
            this.events = MutableLiveData(mutableListOf<Event>())
        }

        val result = viewModel.getEventsByCalendar(calendarId, scope)

        assertTrue(result.isEmpty())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getEventsByCalendar returns events without events from other calendars`() {
        val calendarId = "testCalendarId"
        val scope = "View"
        val event1 = Event(
            id = "event1",
            startTime = LocalDateTime.of(2022, 1, 1, 10, 0),
            endTime = LocalDateTime.of(2022, 1, 1, 11, 0),
            calendarId = calendarId
        )
        val event2 = Event(
            id = "event2",
            startTime = LocalDateTime.of(2022, 1, 2, 10, 0),
            endTime = LocalDateTime.of(2022, 1, 2, 11, 0),
            calendarId = "otherCalendarId"
        )
        val events = mutableListOf(event1, event2)
        viewModel.apply {
            this.events = MutableLiveData(events)
            this.enabledCalendars = MutableLiveData(mutableListOf(calendarId))
        }

        val result = viewModel.getEventsByCalendar(calendarId, scope)

        // TODO: Fix this test
//        assertEquals(1, result.size)
//        assertTrue(result.contains(event1))
        assertFalse(result.contains(event2))
    }

}

typealias Clock = () -> Instant