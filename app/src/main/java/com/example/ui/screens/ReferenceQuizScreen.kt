package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import com.example.ui.screens.DigitText as Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.QuizQuestion
import com.example.data.model.QuizRepository
import com.example.R
import com.example.ui.quizAchievement
import com.example.ai.AloMathSolver
import com.example.ui.theme.PremiumPurple
import com.example.ui.theme.PremiumPurpleContainer
import com.example.ui.theme.WarmTextPrimary
import com.example.ui.theme.WarmTextSecondary
import com.example.ui.theme.WarmPaperBg
import com.example.ui.theme.WarmPaperBorder

/** Reference-screen quiz skin; selection, evaluation, scoring and progression stay in AloViewModel. */
@Composable
fun ReferenceQuizScreen(
    questions: List<QuizQuestion>, currentIndex: Int, selectedOption: Int?,
    isEvaluated: Boolean, score: Int, isFinished: Boolean, selectedSubject: String,
    onSubjectSelected: (String) -> Unit, onOptionSelected: (Int) -> Unit,
    onSubmitAnswer: () -> Unit, onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit, onAskInChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuOpen by remember { mutableStateOf(false) }
    Column(modifier.fillMaxSize().background(WarmPaperBg).verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 24.dp).testTag("quiz_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("অনুশীলন", color = PremiumPurple, style = MaterialTheme.typography.labelSmall)
                Text("কুইজ ও সহায়তা", color = WarmTextPrimary, style = MaterialTheme.typography.headlineSmall)
            }
            Image(painterResource(R.drawable.grade_5_illustration),
                "শেখার অনুশীলনী ইলাস্ট্রেশন", Modifier.size(74.dp), contentScale = ContentScale.Fit)
        }
        Text("কুইজে সঠিক উত্তর দিলে তারা বাড়ে। Little Hero থেকে Aalo Champion পর্যন্ত নতুন Hero Rank খোলো।",
            color = WarmTextSecondary, style = MaterialTheme.typography.bodySmall)
        Surface(modifier = Modifier.fillMaxWidth().clickable { menuOpen = true }.testTag("quiz_subject_dropdown"),
            shape = RoundedCornerShape(14.dp), color = Color.White,
            border = BorderStroke(1.dp, WarmPaperBorder), shadowElevation = 4.dp) {
            Text("বিষয়: $selectedSubject  ▾", modifier = Modifier
                .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFFFF8E9))))
                .padding(14.dp),
                color = WarmTextPrimary, style = MaterialTheme.typography.titleSmall)
        }
        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
            QuizRepository.quizSubjects.forEach { subject ->
                DropdownMenuItem(text = { Text(subject) }, onClick = {
                    onSubjectSelected(subject); menuOpen = false
                })
            }
        }
        if (questions.isEmpty()) {
            Text("এই বিষয়ে এখনও কুইজ নেই। অন্য বিষয় বেছে নাও।", color = WarmTextSecondary)
        } else if (isFinished) {
            ReferenceQuizCard {
                Text("আজকের কুইজ শেষ!", style = MaterialTheme.typography.titleLarge, color = WarmTextPrimary)
                Spacer(Modifier.height(8.dp))
                Text("${AloMathSolver.toBanglaDigits(score)} / ${AloMathSolver.toBanglaDigits(questions.size)} সঠিক উত্তর", color = PremiumPurple,
                    style = MaterialTheme.typography.headlineSmall)
                val achievement = quizAchievement(score, questions.size)
                Spacer(Modifier.height(12.dp))
                Surface(shape = RoundedCornerShape(14.dp), color = Color(0xFFFFEDC5),
                    border = BorderStroke(1.dp, Color(0xFFEAD4A5))) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(achievement.imageRes), achievement.title,
                            Modifier.size(62.dp), contentScale = ContentScale.Fit)
                        Column(Modifier.weight(1f).padding(start = 10.dp)) {
                            Text("তোমার উপাধি: ${achievement.title}", color = Color(0xFF805B1E),
                                style = MaterialTheme.typography.titleMedium)
                            Text(achievement.message, color = WarmTextPrimary,
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = onRestartQuiz, colors = ButtonDefaults.buttonColors(containerColor = PremiumPurple)) {
                    Text("আবার শুরু করি")
                }
            }
        } else {
            val q = questions[currentIndex.coerceIn(0, questions.lastIndex)]
            Text("প্রশ্ন ${AloMathSolver.toBanglaDigits(currentIndex + 1)} / ${AloMathSolver.toBanglaDigits(questions.size)}", color = WarmTextSecondary,
                style = MaterialTheme.typography.labelLarge)
            LinearProgressIndicator(progress = { (currentIndex + 1f) / questions.size },
                modifier = Modifier.fillMaxWidth(), color = PremiumPurple,
                trackColor = Color(0xFFFFE8BA))
            Text(q.questionBangla, style = MaterialTheme.typography.titleLarge, color = WarmTextPrimary)
            q.options.forEachIndexed { index, option ->
                val chosen = selectedOption == index
                val correct = isEvaluated && q.correctIndex == index
                Surface(modifier = Modifier.fillMaxWidth().clickable(enabled = !isEvaluated) { onOptionSelected(index) },
                    shape = RoundedCornerShape(15.dp),
                    color = when { correct -> Color(0xFFE6F6EE); chosen -> PremiumPurpleContainer; else -> Color.White },
                    border = BorderStroke(1.dp, if (chosen || correct) PremiumPurple else WarmPaperBorder),
                    shadowElevation = if (chosen) 5.dp else 3.dp) {
                    Row(Modifier.padding(16.dp)) {
                        Text("${'ক' + index}  ", color = PremiumPurple, fontWeight = FontWeight.Bold)
                        Text(option, color = WarmTextPrimary, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            if (isEvaluated) {
                ReferenceQuizCard {
                    Text(if (selectedOption == q.correctIndex) "সঠিক উত্তর!" else "শেখার সুযোগ",
                        color = PremiumPurple, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(5.dp))
                    Text(q.explanationBangla, color = WarmTextPrimary, style = MaterialTheme.typography.bodyMedium)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { onAskInChat(q.questionBangla) }, modifier = Modifier.weight(1f)) {
                        Text("AI-কে জিজ্ঞাসা")
                    }
                    Button(onClick = onNextQuestion, modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumPurple)) {
                        Text(if (currentIndex + 1 < questions.size) "পরের প্রশ্ন" else "ফলাফল")
                    }
                }
            } else {
                Button(onClick = onSubmitAnswer, enabled = selectedOption != null,
                    modifier = Modifier.fillMaxWidth().testTag("quiz_submit_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPurple)) {
                    Text("উত্তর মিলিয়ে দেখি")
                }
            }
        }
    }
}

@Composable private fun ReferenceQuizCard(content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = Color.White,
        border = BorderStroke(1.dp, WarmPaperBorder), shadowElevation = 5.dp) {
        Column(Modifier.background(Brush.verticalGradient(listOf(Color.White, Color(0xFFFFF8E9))))
            .padding(18.dp), content = content)
    }
}
