package com.example.sharedcalendar.ui.login


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AuthenticationPagerAdapterTest {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var lifecycle: Lifecycle
    private lateinit var authenticationPagerAdapter: AuthenticationPagerAdapter

    @BeforeEach
    fun setup() {
        fragmentManager = mockk(relaxed = true)
        lifecycle = mockk(relaxed = true)
        authenticationPagerAdapter = AuthenticationPagerAdapter(fragmentManager, lifecycle)
    }

    @Test
    @DisplayName("When AuthenticationPagerAdapter is created, then itemCount is zero")
    fun itemCountIsZero() {
        assertEquals(0, authenticationPagerAdapter.itemCount)
    }

    @Test
    @DisplayName("When a fragment is added, then itemCount increases")
    fun itemCountIncreases() {
        val fragment = mockk<Fragment>(relaxed = true)
        authenticationPagerAdapter.addFragment(fragment)

        assertEquals(1, authenticationPagerAdapter.itemCount)
    }

    @Test
    @DisplayName("When a fragment is added, then it can be retrieved")
    fun addedFragmentCanBeRetrieved() {
        val fragment = mockk<Fragment>(relaxed = true)
        authenticationPagerAdapter.addFragment(fragment)

        assertEquals(fragment, authenticationPagerAdapter.createFragment(0))
    }
}