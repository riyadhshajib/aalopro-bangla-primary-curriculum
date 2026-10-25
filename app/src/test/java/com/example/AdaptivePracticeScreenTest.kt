package com.example
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.data.model.QuizRepository
import com.example.ui.screens.ReferenceQuizScreen
import com.example.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers="w360dp-h740dp-mdpi",sdk=[35])
class AdaptivePracticeScreenTest {
    @get:Rule val compose=createComposeRule()
    @Test fun helpShowsStoredExplanationBeforeAnswer() {
        val q=QuizRepository.questions.first()
        compose.setContent {
            var helped by remember { mutableStateOf(false) }
            MyApplicationTheme { ReferenceQuizScreen(
                questions=listOf(q),currentIndex=0,selectedOption=null,isEvaluated=false,
                score=0,isFinished=false,selectedSubject="সকল",onSubjectSelected={},
                onOptionSelected={},onSubmitAnswer={},onNextQuestion={},onRestartQuiz={},onAskInChat={},
                assisted=helped,onHelp={helped=true}) }
        }
        compose.onNodeWithTag("adaptive_help").performScrollTo().performClick()
        compose.onNodeWithText(q.explanationBangla).assertExists()
        compose.onNodeWithText("সাহায্য নিয়ে শেখা হচ্ছে; এটি স্বাধীন উত্তর হিসেবে গণনা হবে না।").assertExists()
        compose.onNodeWithTag("quiz_submit_button").assertIsNotEnabled()
    }
}
