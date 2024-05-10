package com.example.sharedcalendar

import androidx.test.espresso.intent.rule.IntentsTestRule
import de.mannodermaus.junit5.ActivityScenarioExtension
import org.junit.Rule
import org.junit.jupiter.api.extension.RegisterExtension


class MainActivityTest {
    @JvmField
    @RegisterExtension
    val scenarioExtension = ActivityScenarioExtension.launch<MainActivity>()

    @get:Rule
    var mActivityTestRule = IntentsTestRule(MainActivity::class.java, true, false)
//
//
//    private lateinit var mockAuth: FirebaseAuth
//
//
//    private val userMock = mockk<FirebaseUser>(relaxed = true)
//    private val userId = "testUserId"

    //    private lateinit var mainActivity: MainActivity
//    private lateinit var activity: MainActivity

//    @BeforeEach
//    fun setup() {
//        MockKAnnotations.init(this)
//
////        mockkStatic(FirebaseAuth::class)
////        mockAuth = mockk(relaxed = true)
////
////        every { FirebaseAuth.getInstance().currentUser } returns userMock
////        every { FirebaseAuth.getInstance().uid } returns userId
////        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
////        every { mockAuth.uid } returns userId
////        every { mockAuth.currentUser } returns userMock
////        mainActivity = MainActivity(auth)
////        activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
//    }

//    @Test
//    @DisplayName("When MainActivity is launched, then it is not null")
//    fun activityIsNotNull() {
////        val scenario = scenarioExtension.scenario
////        scenario.onActivity {
////            assertNotNull(it)
////        }
//        mActivityTestRule.launchActivity(null)
//        mActivityTestRule.activity.apply {
//
//        }
//        onView(withId(R.id.flFragment)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    @DisplayName("When MainActivity is launched, then it is in correct state")
//    fun activityIsInCorrectState() {
//        val scenario = scenarioExtension.scenario
//        scenario.onActivity { activity ->
//            Assertions.assertEquals(Lifecycle.State.RESUMED, activity.lifecycle.currentState)
//        }
//    }
//
//
//    @Test
//    @DisplayName("When MainActivity is launched, then it has correct layout")
//    fun activityHasCorrectLayout() {
//        val scenario = scenarioExtension.scenario
//        scenario.onActivity { activity ->
//            val view = activity.findViewById<FrameLayout>(R.id.flFragment)
//            assertNotNull(view)
//        }
//    }
//
//
//    @Test
//    fun shouldNotBeNull() {
//        val scenario = scenarioExtension.scenario
//        scenario.onActivity {
//            assertNotNull(it)
//        }
//    }
//
//
//    @Test
//    fun testActivity() {
//        val scenario = scenarioExtension.scenario
//        scenario.onActivity {
//            val mainFragment = it.findViewById<FrameLayout>(R.id.flFragment)
//            assertNotNull(mainFragment)
//
//        }
//    }


//    @Test
////    @DisplayName("When MainActivity is created, then FirebaseAuth is not null")
//    fun firebaseAuthIsNotNull(scenario: ActivityScenario<MainActivity>) {
////        val scenario = scenarioExtension.scenario
//        scenarioExtension.scenario.onActivity {
//
////            assertNotNull(auth)
//            assertNotNull(it.getUser())
//        }
//    }
//
//    @Test
//    fun clickingButton_shouldChangeResultsViewText() {
//        val activity: Activity = Robolectric.setupActivity(MyActivity::class.java)
//        val button = activity.findViewById<View>(R.id.press_me_button) as Button
//        val results = activity.findViewById<View>(R.id.results_text_view) as TextView
//        button.performClick()
//        assertThat(results.text.toString(), equalTo("Testing Android Rocks!"))
//    }


}