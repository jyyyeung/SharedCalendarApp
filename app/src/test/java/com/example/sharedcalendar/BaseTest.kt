package com.example.sharedcalendar

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Looper
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Event
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.robolectric.Shadows.shadowOf

abstract class BaseTest {
    protected var mockFirebaseViewModel = mockk<FirebaseViewModel>(relaxed = true)
    protected lateinit var mockAuth: FirebaseAuth
    protected lateinit var mockFirestore: FirebaseFirestore
    protected lateinit var spykFirebaseViewModel: FirebaseViewModel

    //    protected val mockSharedPreferences = mockk<SharedPreferences>()
//    protected val mockEditor = mockk<SharedPreferences.Editor>()
    protected val mockCollectionReference: CollectionReference = mockk(relaxed = true)
    protected val mockUser = mockk<FirebaseUser>(relaxed = true)
    protected val mockTask = mockk<Task<Void>>(relaxed = true)
    protected val mockTaskAuthResult = mockk<Task<AuthResult>>(relaxed = true)
    protected val mockTaskDocumentReference = mockk<Task<DocumentReference>>(relaxed = true)
    protected val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
    protected val mockTaskQuerySnapshot = mockk<Task<QuerySnapshot>>(relaxed = true)
    protected val mockQuerySnapshot = mockk<QuerySnapshot>(relaxed = true)
    protected val mockTaskQueryDocumentSnapshot = mockk<Task<QueryDocumentSnapshot>>(relaxed = true)
    protected val mockQueryDocumentSnapshot = mockk<QueryDocumentSnapshot>(relaxed = true)
    protected val mockCalendar = mockk<Calendar>(relaxed = true)
    protected val mockEvent = mockk<Event>(relaxed = true)
    protected val mockShare = mockk<Share>(relaxed = true)
    protected val mockUserData = mockk<User>(relaxed = true)
    val completionVoidListenerCapture = slot<OnCompleteListener<Void>>()
    protected val successDocumentReferenceCapture = slot<OnSuccessListener<DocumentReference>>()
    protected val successQuerySnapshotListenerCapture = slot<OnSuccessListener<QuerySnapshot>>()
    val successDocumentReferenceListenerCapture = slot<OnSuccessListener<DocumentReference>>()
    val successAuthResultListenerCapture = slot<OnSuccessListener<AuthResult>>()
    private val failureListenerCapture = slot<OnFailureListener>()
    protected val mockException = mockk<Exception>(relaxed = true)

    protected val mockQuerySnapshotIterator =
        mockk<MutableIterator<QueryDocumentSnapshot>>(relaxed = true)

    protected val mockSharedPrefs = mockk<SharedPreferences>(relaxed = true)
    protected val mockSharedPrefsEditor =
        mockk<SharedPreferences.Editor>(relaxed = true)

    protected val application: Application by lazy {
        ApplicationProvider.getApplicationContext<ApplicationStub>()
    }
    protected val context: Context by lazy {
        application
    }
    protected val mockContext = mockk<Context>(relaxed = true)

    internal class ApplicationStub : Application()

    protected val testEmail = "test@email.com"
    protected val testPassword = "testPassword"
    protected val testUserId = "testUserId"
    protected val testUsername = "testUser"
    abstract fun extendedSetup()

    @Suppress("UNCHECKED_CAST")
    protected fun <T> setField(target: Any, fieldName: String, value: T) {
        Log.i("Base Test Cast", "${target.javaClass.fields}")
        val field = target.javaClass.getDeclaredField(fieldName)
        Log.i("Base Test Cast", "Setting field $fieldName to $value")
        field.isAccessible = true
        field.set(target, value)
    }

    @Before
    fun setup() {
//        MockKAnnotations.init(this)
        shadowOf(Looper.getMainLooper()).idle()

        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.wtf(any(), String()) } returns 0
        every { Log.wtf(any(), Throwable()) } returns 0

        mockkStatic(FirebaseAuth::class)
        mockAuth = mockk<FirebaseAuth>(relaxed = true)
        every { FirebaseAuth.getInstance() } returns mockAuth

        mockkStatic(FirebaseFirestore::class)
        mockFirestore = mockk<FirebaseFirestore>(relaxed = true)
        every { FirebaseFirestore.getInstance() } returns mockFirestore

        spykFirebaseViewModel = spyk(FirebaseViewModel(mockAuth, mockFirestore))
//        mockkStatic(SharedPreferences::class)
//        mockkStatic(SharedPreferences.Editor::class)
        mockkStatic(Task::class)
        mockkStatic(DocumentReference::class)

        every { FirebaseAuth.getInstance() } returns mockAuth
        every { FirebaseFirestore.getInstance() } returns mockFirestore
        every { mockAuth.currentUser } returns mockUser
        every { mockAuth.uid } returns testUserId
        every { mockUser.uid } returns testUserId
        every { mockUser.email } returns testEmail
        every { mockUser.displayName } returns testUsername

        coEvery { mockFirestore.collection(ofType(String::class)) } returns mockCollectionReference
        coEvery {
            mockCollectionReference.add(any())
        } returns mockTaskDocumentReference
        coEvery {
            mockCollectionReference.document(any())
        } returns mockDocumentReference
        coEvery {
            mockDocumentReference.set(any())
        } returns mockTask
        coEvery { mockCollectionReference.document(any()).set(any()) } returns mockTask
        coEvery {
            mockCollectionReference.whereEqualTo(
                ofType(String::class),
                any()
            ).get()
        } returns mockTaskQuerySnapshot
        coEvery {
            mockTaskQuerySnapshot.addOnSuccessListener(
                capture(
                    successQuerySnapshotListenerCapture
                )
            )
        } answers {
            successQuerySnapshotListenerCapture.captured.onSuccess(mockQuerySnapshot)
            mockTaskQuerySnapshot
        }
        coEvery {
            mockTask.addOnCompleteListener(capture(completionVoidListenerCapture))
        } answers {
            completionVoidListenerCapture.captured.onComplete(mockTask)
            mockTask
        }
        coEvery {
            mockTaskDocumentReference.addOnSuccessListener(
                capture(
                    successDocumentReferenceCapture
                )
            )
        } answers {
            successDocumentReferenceCapture.captured.onSuccess(mockDocumentReference)
            mockTaskDocumentReference
        }
        coEvery {
            mockTaskDocumentReference.addOnFailureListener(capture(failureListenerCapture))
        } answers {
            failureListenerCapture.captured.onFailure(mockException)
            mockTaskDocumentReference
        }

        coEvery { mockQuerySnapshotIterator.hasNext() } returns false
        coEvery {
            mockQuerySnapshotIterator.next()
        } returns mockQueryDocumentSnapshot
        coEvery {
            mockQueryDocumentSnapshot.toObject(
                Calendar::class.java
            )
        } returns mockCalendar
        coEvery {
            mockQueryDocumentSnapshot.toObject(
                Event::class.java
            )
        } returns mockEvent
        coEvery {
            mockQueryDocumentSnapshot.toObject(
                Share::class.java
            )
        } returns mockShare
        coEvery {
            mockQueryDocumentSnapshot.toObject(
                User::class.java
            )
        } returns mockUserData
        coEvery {
            mockQuerySnapshot.iterator()
        } answers {
            mockQuerySnapshotIterator
        }
        coEvery { mockTaskQuerySnapshot.addOnFailureListener(capture(failureListenerCapture)) } answers {
            failureListenerCapture.captured.onFailure(mockException)
            mockTaskQuerySnapshot
        }

        every { mockDocumentReference.id } returns "documentReferenceId"

        every { mockContext.getSharedPreferences(any(), any()) }
            .returns(mockSharedPrefs)
        every { mockSharedPrefs.edit() }
            .returns(mockSharedPrefsEditor)
        every { mockSharedPrefsEditor.putString(any(), any()) }
            .returns(mockSharedPrefsEditor)
        every { mockSharedPrefsEditor.putBoolean(any(), any()) }
            .returns(mockSharedPrefsEditor)
        every { mockSharedPrefsEditor.putLong(any(), any()) }
            .returns(mockSharedPrefsEditor)
        every { mockSharedPrefsEditor.putInt(any(), any()) }
            .returns(mockSharedPrefsEditor)
        every { mockSharedPrefsEditor.commit() } returns true


        extendedSetup()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}