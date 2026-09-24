package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.platform.LocalContext
import com.example.ai.ChatAiMode
import com.example.ai.StudySection
import com.example.data.local.AloChatEntity
import com.example.data.model.FormulaRepository
import com.example.data.model.QuizRepository
import com.example.ui.AloTtsManager
import com.example.ui.AppUserMode
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.FormulaScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.ReferenceQuizScreen
import com.example.ui.screens.ReferenceStudioScreen
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
class FeatureLayoutTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test fun studioHeaderIsCompact() {
        composeTestRule.setContent { MyApplicationTheme {
            ReferenceStudioScreen(onAsk = {}, onOpenCurriculum = {})
        } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/studio-compact.png")
    }

    @Test fun quizHeaderAndChoicesAreReadable() {
        composeTestRule.setContent { MyApplicationTheme {
            ReferenceQuizScreen(
                questions = QuizRepository.questions, currentIndex = 0, selectedOption = null,
                isEvaluated = false, score = 0, isFinished = false, selectedSubject = "সকল",
                onSubjectSelected = {}, onOptionSelected = {}, onSubmitAnswer = {},
                onNextQuestion = {}, onRestartQuiz = {}, onAskInChat = {}
            )
        } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/quiz-compact.png")
    }

    @Test fun emptyNotesIllustrationIsReadable() {
        composeTestRule.setContent { MyApplicationTheme {
            NotesScreen(notes = emptyList(), onCreateNote = { _, _, _ -> },
                onTogglePin = {}, onDeleteNote = {})
        } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/notes-compact.png")
    }

    @Test fun formulaLibraryIsReadable() {
        composeTestRule.setContent { MyApplicationTheme {
            FormulaScreen(
                formulas = FormulaRepository.formulas,
                selectedSubject = "সকল",
                searchQuery = "",
                onSubjectSelected = {},
                onSearchQueryChanged = {},
                onAskInChat = {}
            )
        } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/formula-compact.png")
    }

    @Test fun fullScreenTutorChatIsReadable() {
        composeTestRule.setContent { MyApplicationTheme {
            val tts = AloTtsManager(LocalContext.current)
            ChatScreen(
                chatList = listOf(
                    AloChatEntity(
                        id = 1,
                        query = "৫ আর ৩ যোগ করলে কত হয়?",
                        responseBangla = "৫টি আমের সঙ্গে আরও ৩টি আম যোগ করলে মোট ৮টি আম হয়। তাই ৫ + ৩ = ৮।",
                        subjectTag = "গণিত",
                        classCategory = "CLASS_1",
                        chapterTitle = "সহজ যোগ"
                    )
                ),
                inputText = "",
                isThinking = false,
                chatAiMode = ChatAiMode.CURRICULUM,
                selectedSection = StudySection.CLASS_1,
                activeChapterContext = "সহজ যোগ",
                activeSubjectContext = "গণিত",
                activeFollowUpChat = null,
                userMode = AppUserMode.KIDS,
                onModeChanged = {}, onSectionChanged = {}, onStartFollowUp = {},
                onCancelFollowUp = {}, onInputChanged = {}, onSendQuery = {},
                onSaveToNotes = {}, onDeleteChat = {}, onClearAllChats = {},
                onMinimizeChat = {}, onCloseChat = {}, onGoHome = {}, ttsManager = tts
            )
        } }
        composeTestRule.onRoot().captureRoboImage(filePath = "build/chat-compact.png")
    }
}
