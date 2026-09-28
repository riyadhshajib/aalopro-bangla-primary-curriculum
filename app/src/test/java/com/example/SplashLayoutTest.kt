package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.example.ui.screens.AaloSplashScreen
import com.example.ui.screens.AaloOnboardingScreen
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
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
        composeTestRule.setContent { MyApplicationTheme { AaloOnboardingScreen(onComplete = { }) } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/onboarding-compact.png")
    }

    @Test
    fun guardianValidationAdvancesToBengaliChildProfile() {
        composeTestRule.setContent {
            MyApplicationTheme { AaloOnboardingScreen(onComplete = { }, initialConsent = true) }
        }
        composeTestRule.onNodeWithTag("guardian_name").performTextReplacement("রিয়াদের মা")
        composeTestRule.onNodeWithTag("guardian_phone").performTextReplacement("01700000000")
        composeTestRule.onNodeWithTag("guardian_name").assertTextContains("রিয়াদের মা")
        composeTestRule.onNodeWithTag("guardian_phone").assertTextContains("01700000000")
        composeTestRule.onNodeWithTag("guardian_consent").assertHasClickAction()
        composeTestRule.onNodeWithTag("guardian_consent").assertIsOn()
        composeTestRule.onNodeWithTag("guardian_continue").performScrollTo().performClick()
        composeTestRule.onNodeWithTag("child_name").assertExists().performTextInput("রিয়াদ")
        composeTestRule.onNodeWithTag("child_continue").assertExists()
    }
}
