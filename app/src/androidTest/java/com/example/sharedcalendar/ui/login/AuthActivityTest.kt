package com.example.sharedcalendar.ui.login

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import com.example.sharedcalendar.R
import de.mannodermaus.junit5.ActivityScenarioExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class AuthActivityTest {
    @JvmField
    @RegisterExtension
    val scenarioExtension = ActivityScenarioExtension.launch<AuthActivity>()

    @Test
    @DisplayName("When Activity is launched, then it is not null")
    fun activityIsNotNull() {
        val scenario = scenarioExtension.scenario
        scenario.onActivity {
            Assertions.assertNotNull(it)
        }
    }

    @Test
    @DisplayName("When Activity is launched, then it is in correct state")
    fun activityIsInCorrectState() {
        val scenario = scenarioExtension.scenario
        scenario.onActivity { activity ->
            Assertions.assertEquals(Lifecycle.State.RESUMED, activity.lifecycle.currentState)
        }
    }


    @Test
    @DisplayName("When Activity is launched, then it has correct layout")
    fun activityHasCorrectLayout() {
        val scenario = scenarioExtension.scenario
        scenario.onActivity { activity ->
            val view = activity.findViewById<ConstraintLayout>(R.id.loAuth)
            Assertions.assertNotNull(view)
        }
    }

}