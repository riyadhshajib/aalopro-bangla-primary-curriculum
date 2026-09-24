package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.ui.screens.AaloSplashScreen
import com.example.ui.screens.AaloOnboardingScreen
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class SplashLayoutTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    @Config(qualifiers = "w320dp-h560dp-mdpi")
    fun compactSplashRenders() {
        composeTestRule.setContent { MyApplicationTheme { AaloSplashScreen(onSplashFinished = {}) } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/splash-compact.png")
    }

    @Test
    @Config(qualifiers = "w412dp-h892dp-mdpi")
    fun regularSplashRenders() {
        composeTestRule.setContent { MyApplicationTheme { AaloSplashScreen(onSplashFinished = {}) } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/splash-regular.png")
    }

    @Test
    @Config(qualifiers = "w360dp-h740dp-mdpi")
    fun compactOnboardingRenders() {
        composeTestRule.setContent { MyApplicationTheme { AaloOnboardingScreen(onGetStarted = { _ -> }) } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/onboarding-compact.png")
    }

    @Test
    fun bengaliNameCanStartWithoutSelectingARole() {
        var submittedName = ""
        composeTestRule.setContent {
            MyApplicationTheme { AaloOnboardingScreen(onGetStarted = { submittedName = it }) }
        }
        composeTestRule.onNodeWithTag("onboarding_name").performTextInput("রিয়াদ")
        composeTestRule.onNodeWithTag("onboarding_start_button").performClick()
        assertEquals("রিয়াদ", submittedName)
    }
}
