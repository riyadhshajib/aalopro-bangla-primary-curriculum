package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import com.example.data.model.BangladeshCurriculumRepository
import com.example.data.model.CurriculumStage
import com.example.ui.screens.ReferenceCurriculumScreen
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
@Config(qualifiers = "w360dp-h740dp-mdpi", sdk = [35])
class HomeLayoutTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun compactLearningHomeRenders() {
        composeTestRule.setContent {
            MyApplicationTheme {
                ReferenceCurriculumScreen(
                    selectedStage = CurriculumStage.PRIMARY,
                    selectedClassNumber = 1,
                    selectedSubjectId = null,
                    searchQuery = "",
                    currentClassLevel = BangladeshCurriculumRepository.featuredClassLevels.first(),
                    selectedTopicDetail = null,
                    onStageSelected = {}, onClassSelected = {}, onSubjectSelected = {},
                    onSearchQueryChanged = {}, onTopicClicked = {}, onCloseTopicDetail = {},
                    onAskInChat = {}, onOpenQuiz = {}, onOpenStudio = {}, onOpenGeneralChat = {}
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/home-compact.png")
    }

    @Test
    fun expandedTopicsRenderAsOneReadableList() {
        composeTestRule.setContent {
            var selectedSubject by remember { mutableStateOf<String?>(null) }
            MyApplicationTheme {
                ReferenceCurriculumScreen(
                    selectedStage = CurriculumStage.PRIMARY,
                    selectedClassNumber = 1,
                    selectedSubjectId = selectedSubject,
                    searchQuery = "",
                    currentClassLevel = BangladeshCurriculumRepository.featuredClassLevels.first(),
                    selectedTopicDetail = null,
                    onStageSelected = {}, onClassSelected = {},
                    onSubjectSelected = { selectedSubject = it },
                    onSearchQueryChanged = {}, onTopicClicked = {}, onCloseTopicDetail = {},
                    onAskInChat = {}, onOpenQuiz = {}, onOpenStudio = {}, onOpenGeneralChat = {}
                )
            }
        }
        composeTestRule.onNodeWithText("শ্রেণি বেছে নাও").performClick()
        composeTestRule.onNodeWithText("প্রথম শ্রেণি").performClick()
        composeTestRule.onNodeWithText("প্রাথমিক গণিত").performClick()
        composeTestRule.onRoot().captureRoboImage(filePath = "build/home-topics-compact.png")
    }
}
