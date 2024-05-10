package com.example.sharedcalendar

import MainDispatcherRule
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class FirebaseViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()


    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockFirestore: FirebaseFirestore
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

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.wtf(any(), String()) } returns 0

//        mockAuth = mockk(relaxed = true)
        mockkStatic(FirebaseAuth::class)
        mockAuth = mockk(relaxed = true)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { FirebaseAuth.getInstance().currentUser } returns userMock
        every { FirebaseAuth.getInstance().uid } returns userId
        every { mockAuth.uid } returns userId
        every { mockAuth.currentUser } returns userMock

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

    //    @Test
//    fun `addEventToCalendar adds event to first calendar`() = runTest {
//        val event = Event()
//        val calendar = Calendar().apply { events = mutableListOf() }
//        coEvery { viewModel.calendars.value } returns mutableListOf(calendar)
//
//        viewModel.addEventToCalendar(event)
//
//        assertEquals(1, viewModel.calendars.value?.get(0)?.events?.size)
//        assertEquals(event, viewModel.calendars.value?.get(0)?.events?.get(0))
//    }
    private val mockCalendar = mockk<Calendar>(relaxed = true)
    private val mockShare = mockk<Share>(relaxed = true)
    private val mockEvent = mockk<Event>(relaxed = true)

    @ExperimentalCoroutinesApi
    @Test
    fun `createCalendar adds calendar to firestore`() = runTest {
        coEvery { viewModel.user.uid } returns userId
        viewModel.createCalendar(userId, sharedPrefs, mockCalendar)

        coVerify { mockFirestore.collection("calendars").add(mockCalendar) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `editCalendar updates calendar fields`() = runTest {
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
    fun `deleteCalendar removes calendar from firestore`() = runTest {
        val calendarId = mockCalendar.id ?: "testCalendarId"

        viewModel.deleteCalendar(calendarId)

        coVerify { mockFirestore.collection("calendars").document(calendarId).delete() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addEventToCalendar adds event to firestore`() = runTest {
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
    fun `addShare adds share to firestore`() = runTest {
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
    fun `editShare updates share fields`() = runTest {
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
    fun `createShare does not add share if already exists`() = runTest {
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
    fun `getShares retrieves shares for given calendars`() = runTest {
        val calendar = Calendar().apply { id = "testCalendarId" }

        viewModel.getShares(listOf(calendar))

        coVerify {
            mockFirestore.collection("shares").whereEqualTo("calendarId", calendar.id).get()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getUserShares retrieves shares for current user`() = runTest {
        val userEmail = "test@example.com"
        coEvery { viewModel.user.email } returns userEmail

        viewModel.getUserShares()

        coVerify { mockFirestore.collection("shares").whereEqualTo("userEmail", userEmail).get() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getCalendars retrieves user calendars`() = runTest {
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
//    fun `getCalendars retrieves user and shared calendars`() = runTest {
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
    fun `getCurrentMonthEvents retrieves events for current month`() = runTest {
        val calendar = Calendar().apply { id = "testCalendarId" }
        coEvery { viewModel.calendars.value } returns mutableListOf(calendar)

        viewModel.getCurrentMonthEvents()

        coVerify {
            mockFirestore.collection("events").whereEqualTo("calendarId", calendar.id).get()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `deleteShare removes share from firestore`() = runTest {
        val share = Share().apply { id = "testShareId" }

        viewModel.deleteShare(share)

        coVerify { mockFirestore.collection("shares").document(share.id).delete() }
    }
}

typealias Clock = () -> Instant