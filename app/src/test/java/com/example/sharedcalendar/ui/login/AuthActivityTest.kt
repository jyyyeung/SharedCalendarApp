package com.example.sharedcalendar.ui.login

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.time.Clock
import java.time.Instant
import java.time.ZoneId


@RunWith(RobolectricTestRunner::class)
class AuthActivityTest {
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var viewModel: FirebaseViewModel

    private val now = 1550160535168L

    private val fixedClock = Clock.fixed(Instant.ofEpochMilli(now), ZoneId.systemDefault())

    //    private lateinit var fixedClock: Clock
    private var activity: AuthActivity? = null
    private val userMock = mockk<FirebaseUser>(relaxed = true)
    private val userId = "testUserId"

    @Before
//    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.wtf(any(), String()) } returns 0
        every { Log.wtf(any(), Throwable()) } returns 0

        mockkStatic(Clock::class)
//        mockkStatic(Instant::class)
//        fixedClock = Clock.fixed(Instant.ofEpochMilli(now), ZoneId.systemDefault())
        // Default system clock
        every { Clock.systemUTC() } returns fixedClock

        mockkStatic(SystemClock::class)
        every { SystemClock.elapsedRealtime() } returns fixedClock.millis()
        every { SystemClock.uptimeMillis() } returns fixedClock.millis()


//        mockkStatic(Looper::class)
//        every { Looper.getMainLooper() } returns mockk(relaxed = true)

//        mockAuth = mockk(relaxed = true)
        mockkStatic(Firebase::class)
        mockkStatic(FirebaseAuth::class)
        mockAuth = mockk(relaxed = true)

        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { Firebase.auth } returns mockAuth
        every { FirebaseAuth.getInstance().currentUser } returns userMock
        every { FirebaseAuth.getInstance().uid } returns userId
        every { mockAuth.uid } returns userId
        every { mockAuth.currentUser } returns userMock


        mockFirestore = mockk(relaxed = true)

        activity =
            Robolectric.buildActivity(
                AuthActivity::class.java, null,
//                bundleOf("auth" to mockAuth)
                null
            )
                .create()
                .resume()
                .get()
    }

    @Test
    fun `check activity is initialized properly`() {
        assert(activity != null)
    }

    @Test
    fun `check activity is in correct state`() {
        assertEquals(Lifecycle.State.RESUMED, activity!!.lifecycle.currentState)
    }

    @Test
    fun `check activity has correct layout`() {
        assertNotNull(activity!!.findViewById(R.id.loAuth))
    }

    @Test
    fun shouldHaveViewPagerView() {
        assertNotNull(activity!!.findViewById(R.id.viewPager))
    }

    @Test
    fun shouldHaveCorrectViewPagerAdapter() {
        val viewPager = activity!!.findViewById<ViewPager2>(R.id.viewPager)
        val adapter = viewPager.adapter
        assertNotNull(adapter)
        assert(adapter is AuthenticationPagerAdapter)
    }

    @Test
    fun shouldHaveCorrectNumberOfFragments() {
        val viewPager = activity!!.findViewById<ViewPager2>(R.id.viewPager)
        val adapter = viewPager.adapter as AuthenticationPagerAdapter
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun shouldAddFragmentsToAdapter() {
        val viewPager = activity!!.findViewById<ViewPager2>(R.id.viewPager)
        val adapter = viewPager.adapter as AuthenticationPagerAdapter
        val loginFragment = LoginFragment()
        val registerFragment = RegisterFragment()
        adapter.addFragment(loginFragment)
        adapter.addFragment(registerFragment)
        assertEquals(4, adapter.itemCount)
        assertEquals(loginFragment, adapter.createFragment(2))
        assertEquals(registerFragment, adapter.createFragment(3))
    }

    @Test
    fun shouldHaveCorrectFragments() {
        val viewPager = activity!!.findViewById<ViewPager2>(R.id.viewPager)
        val adapter = viewPager.adapter as AuthenticationPagerAdapter
        assertEquals(LoginFragment::class.java, adapter.createFragment(0)::class.java)
        assertEquals(RegisterFragment::class.java, adapter.createFragment(1)::class.java)
    }


}