package com.example.sharedcalendar

import android.os.Looper
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import com.example.sharedcalendar.ui.ManageCalendarsFragment
import com.example.sharedcalendar.ui.SettingsFragment
import com.example.sharedcalendar.ui.login.AuthActivity
import com.example.sharedcalendar.ui.share.ShareCalendarFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf


@RunWith(RobolectricTestRunner::class)
class MainActivityTest : BaseTest() {
    private var activity: MainActivity? = null

    // Declare the nvSidebar object that we want to test
    private lateinit var nvSidebar: NavigationView

    // Mock the dependencies that are used in the nvSidebar.setNavigationItemSelectedListener
//    private val menuItem = mockk<MenuItem>(relaxed = true)
    private val menuItem = spyk<MenuItem>()
    private val mFragmentManager = mockk<FragmentManager>(relaxed = true)
    private val mFragmentTransaction = mFragmentManager.beginTransaction()
    private val drawerLayout = mockk<DrawerLayout>(relaxed = true)
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
        every { mockAuth.signOut() }.just(Runs)

        setField(
            target = activity as MainActivity,
            fieldName = "firebaseViewModel",
            value = mockFirebaseViewModel
        )
        setField(
            target = activity as MainActivity,
            fieldName = "mFragmentManager",
            value = mFragmentManager
        )

        mockkStatic(MutableLiveData::class)
        val calendarsSharedWithUser = mutableListOf<String>()

        nvSidebar = activity?.findViewById(R.id.nvSidebar)!!


        every { menuItem.isChecked } returns false
        every { menuItem.toString() } returns ""
        every { menuItem.itemId } returns 0
        every { mFragmentManager.beginTransaction() } returns mockk<FragmentTransaction> {
            every { commit() } answers { 1 }
            every { replace(any(), any<Fragment>()) } answers { mockk() }
        }
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
    fun `onStart with null currentUser starts AuthActivity`() {
        every { mockAuth.currentUser } returns null
        shadowOf(Looper.getMainLooper()).idle()
        activity?.onStart().apply {
            val intent =
                shadowOf(activity).peekNextStartedActivity()
            assertEquals(intent?.component?.className, AuthActivity::class.java.canonicalName)
        }
    }

    @Test
    fun `onStart with non-null currentUser does not start AuthActivity`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        activity?.onStart().apply {
            val intent =
                shadowOf(activity).peekNextStartedActivity()
            assert(intent == null)

        }
    }

    @Test
    fun `onStart initializes fragments correctly`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        activity?.onStart().apply {
            assertNotNull(activity!!.findViewById(R.id.mainFragment))
        }
    }

    @Test
    fun `onStart with null currentUser starts AuthActivity and finishes MainActivity`() {
        every { mockAuth.currentUser } returns null
        activity?.onStart().apply {
            val intent =
                shadowOf(activity).peekNextStartedActivity()
            assertEquals(intent?.component?.className, AuthActivity::class.java.canonicalName)
            assertTrue(activity?.isFinishing == true)
        }
    }

    @Test
    fun `onStart with non-null currentUser does not start AuthActivity and MainActivity is not finished`() {
        every { mockAuth.currentUser } returns mockk(relaxed = true)
        activity?.onStart().apply {
            val intent =
                shadowOf(activity).peekNextStartedActivity()

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
        val appBarLayout: AppBarLayout = activity?.findViewById(R.id.appBarLayout_topAppBar)!!
        val nvSidebar: NavigationView = activity?.findViewById(R.id.nvSidebar)!!

        assertNotNull(drawerLayout)
        assertNotNull(appBarLayout)
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

    @Test
    fun testNavigationItemSelectedListener_Settings() {
        // Arrange
        every { mockAuth.currentUser } returns mockUser
        every { menuItem.toString() } returns "Settings"
        every { drawerLayout.close() }.just(Runs)

        activity?.onStart().apply {

            // Act
            val function = activity?.javaClass?.getDeclaredMethod(
                "navigationItemSelectedListener",
                MenuItem::class.java,
                DrawerLayout::class.java
            )
            function?.isAccessible = true
            function?.invoke(activity, menuItem, drawerLayout)

            // Assert
            verify(exactly = 1) { drawerLayout.close() }
            verify(exactly = 1) {
                mFragmentTransaction.replace(
                    R.id.mainFragment,
                    any<SettingsFragment>()
                )
            }
//            verify(exactly = 1) { mFragmentManager.beginTransaction().commit() }
//            verify(exactly = 1) { mFragmentManager.executePendingTransactions() }
        }
    }

    @Test
    fun testNavigationItemSelectedListener_AddPersonToCalendar() {
        // Arrange
        every { mockAuth.currentUser } returns mockUser
        every { menuItem.toString() } returns "Add Person to Calendar"
        every { drawerLayout.close() }.just(Runs)
        mockkStatic(ShareCalendarFragment::class)
        mockkStatic(FragmentManager::class)
//        val mSupportFragmentManager = mockk<FragmentManager>(relaxed = true)
//        every { activity.supportFragmentManager } returns mFragmentManager
//        every { ShareCalendarFragment.display(any()) } answers { mockk(relaxed = true) }

        activity?.onStart().apply {

            // Act
            val function = activity?.javaClass?.getDeclaredMethod(
                "navigationItemSelectedListener",
                MenuItem::class.java,
                DrawerLayout::class.java
            )
            function?.isAccessible = true
            function?.invoke(activity, menuItem, drawerLayout)

            mockkStatic(ShareCalendarFragment::class)

            // Assert
            verify(exactly = 1) { drawerLayout.close() }
            verify(exactly = 1) {
                ShareCalendarFragment.display(mFragmentManager)
            }
        }
    }

    @Test
    fun testNavigationItemSelectedListener_ManageCalendars() {
        // Arrange
        every { mockAuth.currentUser } returns mockUser
        every { menuItem.toString() } returns "Manage Calendars"
        every { drawerLayout.close() }.just(Runs)

        activity?.onStart().apply {

            // Act
            val function = activity?.javaClass?.getDeclaredMethod(
                "navigationItemSelectedListener",
                MenuItem::class.java,
                DrawerLayout::class.java
            )
            function?.isAccessible = true
            function?.invoke(activity, menuItem, drawerLayout)

            // Assert
            verify(exactly = 1) { drawerLayout.close() }
            verify(exactly = 1) {
                mFragmentTransaction.replace(
                    R.id.mainFragment, any<ManageCalendarsFragment>()
                )
            }
//            verify(exactly = 1) { mFragmentManager.beginTransaction().commit() }
//            verify(exactly = 1) { mFragmentManager.executePendingTransactions() }
        }
    }

    @Test
    fun testNavigationItemSelectedListener_Logout() {
        // Arrange
        every { mockAuth.currentUser } returns mockUser
        every { menuItem.toString() } returns "Logout"

        // Act
        val function = activity?.javaClass?.getDeclaredMethod(
            "navigationItemSelectedListener",
            MenuItem::class.java,
            DrawerLayout::class.java
        )
        function?.isAccessible = true
        function?.invoke(activity, menuItem, drawerLayout)


        // Assert
        // You might need to add specific assertions for the logout behavior
        verify(exactly = 1) { mockAuth.signOut() }
    }

}