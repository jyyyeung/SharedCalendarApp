package com.example.sharedcalendar

import android.content.Intent
import android.os.Looper
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import com.example.sharedcalendar.ui.login.AuthActivity
import com.google.android.material.navigation.NavigationView
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf


@RunWith(RobolectricTestRunner::class)
class MainActivityTest : BaseTest() {
    private var activity: MainActivity? = null

    override fun extendedSetup() {
        mockFirebaseViewModel = spyk(FirebaseViewModel(mockAuth, mockFirestore))
        activity =
            Robolectric.buildActivity(
                MainActivity::class.java, null,
//                bundleOf("auth" to mockAuth)
                null
            )
                .create()
                .resume()
                .get()
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        setField(
            target = activity as MainActivity,
            fieldName = "firebaseViewModel",
            value = mockFirebaseViewModel
        )
        mockkStatic(MutableLiveData::class)
        val calendarsSharedWithUser = mutableListOf<String>()

    }

    @Test
    fun `check activity is initialized properly`() {
        assert(activity != null)
    }

    @Test
    fun `check activity is in correct state`() {
        Assert.assertEquals(Lifecycle.State.RESUMED, activity!!.lifecycle.currentState)
    }

    @Test
    fun `onStart with null currentUser starts AuthActivity`() {
        every { mockAuth.currentUser } returns null
        shadowOf(Looper.getMainLooper()).idle()
        activity?.onStart().apply {
            val intent =
                Shadows.shadowOf(activity).peekNextStartedActivity()
            assertEquals(intent?.component?.className, AuthActivity::class.java.canonicalName)
        }
    }

    @Test
    fun `onStart with non-null currentUser does not start AuthActivity`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        activity?.onStart().apply {
            val intent =
                Shadows.shadowOf(activity).peekNextStartedActivity()
            assert(intent == null)

        }
    }

    @Test
    fun `onStart initializes fragments correctly`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        activity?.onStart().apply {
            Assert.assertNotNull(activity!!.findViewById(R.id.mainFragment))
        }
    }

    @Test
    fun `onStart with null currentUser starts AuthActivity and finishes MainActivity`() {
        every { mockAuth.currentUser } returns null
        activity?.onStart().apply {
            val intent =
                Shadows.shadowOf(activity).peekNextStartedActivity()
            assertEquals(intent?.component?.className, AuthActivity::class.java.canonicalName)
            assertTrue(activity?.isFinishing == true)
        }
    }

    @Test
    fun `onStart with non-null currentUser does not start AuthActivity and MainActivity is not finished`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        activity?.onStart().apply {
            val intent =
                Shadows.shadowOf(activity).peekNextStartedActivity()

            assert(intent == null)
            assertFalse(activity?.isFinishing == true)
        }

    }

    @Test
    fun `onStart with non-null currentUser initializes sidebar navigation`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        activity?.onStart()

        val drawerLayout: DrawerLayout = activity?.findViewById(R.id.drawerLayout)!!
        val buttonDrawerToggle: ImageButton = activity?.findViewById(R.id.drawerLayoutToggle)!!
        val nvSidebar: NavigationView = activity?.findViewById(R.id.nvSidebar)!!

        assertNotNull(drawerLayout)
        assertNotNull(buttonDrawerToggle)
        assertNotNull(nvSidebar)
    }

    @Test
    fun `onStart with non-null currentUser initializes user shares`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
//        activity.firebaseViewModel = mockFirebaseViewModel

        activity?.onStart().apply {
            assert(activity?.firebaseViewModel != null)
            verify { mockFirebaseViewModel.getUserShares() }
        }

    }
}