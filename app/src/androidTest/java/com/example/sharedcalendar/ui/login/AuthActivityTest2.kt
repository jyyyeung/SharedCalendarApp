package com.example.sharedcalendar.ui.login


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.sharedcalendar.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthActivityTest2 {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AuthActivity::class.java)

    @Test
    fun authActivityTest2() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.etUserEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.login_email),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("test10@example.com"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.etLoginPassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.login_password),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("password"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.btnLogin), withText("Sign in or register"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val frameLayout = onView(
            allOf(
                withId(R.id.flFragment),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))

        val frameLayout2 = onView(
            allOf(
                withId(R.id.mainFragment),
                withParent(withParent(withId(R.id.drawerLayout))),
                isDisplayed()
            )
        )
        frameLayout2.check(matches(isDisplayed()))

        val materialButton2 = onView(
            allOf(
                withId(R.id.weekBtn), withText("Week"),
                childAtPosition(
                    allOf(
                        withId(R.id.appTopBar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val view = onView(
            allOf(
                withId(R.id.weekView),
                withParent(withParent(withId(R.id.flFragment))),
                isDisplayed()
            )
        )
        view.check(matches(isDisplayed()))

        val materialButton3 = onView(
            allOf(
                withId(R.id.monthBtn), withText("Month"),
                childAtPosition(
                    allOf(
                        withId(R.id.appTopBar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val frameLayout3 = onView(
            allOf(
                withId(R.id.flFragment),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        frameLayout3.check(matches(isDisplayed()))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.addEventBtn),
                childAtPosition(
                    allOf(
                        withId(R.id.appTopBar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val linearLayout = onView(
            allOf(
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.design_bottom_sheet),
                        withParent(withId(com.google.android.material.R.id.coordinator))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val view2 = onView(
            allOf(
                withId(com.google.android.material.R.id.touch_outside),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.coordinator),
                        childAtPosition(
                            withId(com.google.android.material.R.id.container),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        view2.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.drawerLayoutToggle), withContentDescription("Toggle Sidebar"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar2),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val recyclerView = onView(
            allOf(
                withId(com.google.android.material.R.id.design_navigation_view),
                withParent(
                    allOf(
                        withId(R.id.nvSidebar),
                        withParent(withId(R.id.drawerLayout))
                    )
                ),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tvSidebarUsername), withText("Admin010"),
                withParent(
                    allOf(
                        withId(R.id.nvDrawerHeader),
                        withParent(withId(com.google.android.material.R.id.navigation_header_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Admin010")))

        val textView2 = onView(
            allOf(
                withId(R.id.tvSidebarEmail), withText("test10@example.com"),
                withParent(
                    allOf(
                        withId(R.id.nvDrawerHeader),
                        withParent(withId(com.google.android.material.R.id.navigation_header_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("test10@example.com")))

        val linearLayoutCompat = onView(
            allOf(
                withId(R.id.home_item),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        withParent(withId(R.id.nvSidebar))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayoutCompat.check(matches(isDisplayed()))

        val linearLayoutCompat2 = onView(
            allOf(
                withId(R.id.add_person_item),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        withParent(withId(R.id.nvSidebar))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayoutCompat2.check(matches(isDisplayed()))

        val linearLayoutCompat3 = onView(
            allOf(
                withId(R.id.manage_calendars),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        withParent(withId(R.id.nvSidebar))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayoutCompat3.check(matches(isDisplayed()))

        val linearLayoutCompat4 = onView(
            allOf(
                withId(R.id.settings_item),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        withParent(withId(R.id.nvSidebar))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayoutCompat4.check(matches(isDisplayed()))

        val linearLayoutCompat5 = onView(
            allOf(
                withId(R.id.logout_item),
                withParent(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        withParent(withId(R.id.nvSidebar))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayoutCompat5.check(matches(isDisplayed()))

        val navigationMenuItemView = onView(
            allOf(
                withId(R.id.home_item),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nvSidebar),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.drawerLayoutToggle), withContentDescription("Toggle Sidebar"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar2),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val navigationMenuItemView2 = onView(
            allOf(
                withId(R.id.add_person_item),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nvSidebar),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView2.perform(click())

        val viewGroup = onView(
            allOf(
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val autoCompleteTextView = onView(
            allOf(
                withId(R.id.dropdownSelectCalendar), withText("Choose Calendar to Share"),
                withParent(withParent(withId(R.id.selectCalendar))),
                isDisplayed()
            )
        )
        autoCompleteTextView.check(matches(isDisplayed()))

        val editText = onView(
            allOf(
                withId(R.id.etUserEmail), withText("User Email"),
                withParent(withParent(withId(R.id.shareUserEmail))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("User Email")))

        val button = onView(
            allOf(
                withId(R.id.btnShare), withText("Share"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialAutoCompleteTextView = onView(
            allOf(
                withId(R.id.dropdownSelectCalendar),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.selectCalendar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialAutoCompleteTextView.perform(click())

        val materialTextView = onData(anything())
            .inRoot(isPlatformPopup())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(0)
        materialTextView.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.etUserEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.shareUserEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("test8@example.com"), closeSoftKeyboard())

        val checkableImageButton = onView(
            allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.textfield.EndCompoundLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        checkableImageButton.perform(click())

        val materialTextView2 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(0)
        materialTextView2.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btnShare), withText("Share"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val appCompatImageButton4 = onView(
            allOf(
                withId(R.id.drawerLayoutToggle), withContentDescription("Toggle Sidebar"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar2),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        val navigationMenuItemView3 = onView(
            allOf(
                withId(R.id.manage_calendars),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nvSidebar),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView3.perform(click())

        val view3 = onView(
            allOf(
                withParent(withParent(IsInstanceOf.instanceOf(android.view.View::class.java))),
                isDisplayed()
            )
        )
        view3.check(matches(isDisplayed()))

        val view4 = onView(
            allOf(
                withParent(withParent(IsInstanceOf.instanceOf(android.view.View::class.java))),
                isDisplayed()
            )
        )
        view4.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withText("test10@example.com"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.View::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("test10@example.com")))

        val textView4 = onView(
            allOf(
                withText("View Calendar Details"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.View::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("View Calendar Details")))

        val scrollView = onView(
            allOf(
                withParent(withParent(IsInstanceOf.instanceOf(android.view.View::class.java))),
                isDisplayed()
            )
        )
        scrollView.check(matches(isDisplayed()))

        val textView5 = onView(
            allOf(
                withText("Admin010"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.View::class.java))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Admin010")))

        val textView6 = onView(
            allOf(
                withText("Full"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.View::class.java))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Full")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
