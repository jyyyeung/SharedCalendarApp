package com.example.sharedcalendar.ui.login

import android.content.Intent
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.sharedcalendar.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

//@RunWith(RobolectricTestRunner::class)
class AuthActivityTest {
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var viewModel: FirebaseViewModel

    private val now = 1550160535168L
    private val fixedClock = Clock.fixed(Instant.ofEpochMilli(now), ZoneId.systemDefault())

    @BeforeEach
    fun setup() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.wtf(any(), String()) } returns 0

        mockkStatic(Clock::class)
        // Default system clock
        every { Clock.systemUTC() } returns fixedClock

        mockkStatic(SystemClock::class)
        every { SystemClock.elapsedRealtime() } returns fixedClock.millis()
        every { SystemClock.uptimeMillis() } returns fixedClock.millis()

        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk(relaxed = true)

//        mockAuth = mockk(relaxed = true)
        mockkStatic(FirebaseAuth::class)
        mockAuth = mockk(relaxed = true)

        mockFirestore = mockk(relaxed = true)
    }

    @Test
    fun `can fix clock`() {
        assertEquals(now, Instant.now().toEpochMilli())
    }

    //    @ExperimentalCoroutinesApi
    @Test
    fun `onStart skips authentication if user is already logged in`() = runBlockingTest {
        val mockAuth: FirebaseAuth = mockk(relaxed = true)
        val mockUser: FirebaseUser = mockk(relaxed = true)
        every { mockAuth.currentUser } returns mockUser

        val activity = spyk(AuthActivity())
        activity.auth = mockAuth
        every { activity.finishAffinity() } just Runs
        every { activity.startActivity(any()) } just Runs

        activity.onStart()

        verify { activity.finishAffinity() }
        verify { activity.startActivity(any<Intent>()) }
    }

    //    @ExperimentalCoroutinesApi
    @Test
    fun `onStart does not skip authentication if user is not logged in`() = runBlockingTest {
//        val mockAuth: FirebaseAuth = mockk(relaxed = true)
        every { mockAuth.currentUser } returns null

        val activity = spyk(AuthActivity())
        activity.auth = mockAuth
        every { activity.finishAffinity() } just Runs
        every { activity.startActivity(any()) } just Runs

        activity.onStart()

//        activity.onStart()

        verify(exactly = 0) { activity.finishAffinity() }
        verify(exactly = 0) { activity.startActivity(any<Intent>()) }
    }

    @Test
    fun `onCreate sets up view pager with correct fragments`() = runTest {
//        val mockAuth: FirebaseAuth = mockk(relaxed = true)
        val mockViewPager: ViewPager2 = mockk(relaxed = true)
        every { mockAuth.currentUser } returns null


        val activity = spyk(AuthActivity(), recordPrivateCalls = true)
//        val activity = Robolectric.buildActivity(AuthActivity::class.java).get()

        activity.auth = mockAuth
        every { activity.findViewById<ViewPager2>(any()) } returns mockViewPager

        activity.onCreate(null)

        verify { mockViewPager.adapter = any<AuthenticationPagerAdapter>() }
    }
}