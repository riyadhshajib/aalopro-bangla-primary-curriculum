package com.example

import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.example.analytics.AaloAnalytics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AloDatabase
import com.example.data.local.AloRepository
import com.example.ui.AloTab
import com.example.ui.AloTtsManager
import com.example.ui.AloViewModel
import com.example.ui.components.AloBottomNavigation
import com.example.ui.components.AloTopBar
import com.example.ui.screens.AaloOnboardingScreen
import com.example.ui.screens.AaloSplashScreen
import com.example.ui.screens.ChatScreen

import com.example.ui.screens.CurriculumScreen
import com.example.ui.screens.ReferenceCurriculumScreen
import com.example.ui.screens.FormulaScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ReferenceQuizScreen
import com.example.ui.screens.ReferenceStudioScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.AppUserMode
import com.example.ai.ChatAiMode
import com.example.ai.StudySection

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: AloTtsManager

    private val viewModel: AloViewModel by viewModels {
        val database = AloDatabase.getDatabase(applicationContext)
        val repository = AloRepository(database.aloDao())
        AloViewModel.Factory(repository, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)?.let {
            FirebaseAppCheckSetup.install()
            AaloAnalytics.initialize(this, productId = "bangla_primary")
        }
        enableEdgeToEdge()
        ttsManager = AloTtsManager(this)

        setContent {
            MyApplicationTheme {
                AloProApp(
                    viewModel = viewModel,
                    ttsManager = ttsManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

@Composable
fun AloProApp(
    viewModel: AloViewModel,
    ttsManager: AloTtsManager,
    modifier: Modifier = Modifier
) {
    val isShowingSplash by viewModel.isShowingSplash.collectAsStateWithLifecycle()
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val todayStats by viewModel.todayStats.collectAsStateWithLifecycle()
    val userMode by viewModel.userMode.collectAsStateWithLifecycle()
    val profileName by viewModel.profileName.collectAsStateWithLifecycle()
    val completedTopicIds by viewModel.completedTopicIds.collectAsStateWithLifecycle()
    val learningStars = (todayStats.queriesAnswered * 2) +
        (todayStats.quizScoreSum * 5) + (completedTopicIds.size * 10)
    val isChatMinimized by viewModel.isChatMinimized.collectAsStateWithLifecycle()
    LaunchedEffect(isShowingSplash, isOnboardingCompleted) {
        if (!isShowingSplash && isOnboardingCompleted) {
            AaloAnalytics.screen(currentTab.name.lowercase())
        }
    }
    var homeResetToken by remember { mutableIntStateOf(0) }
    val goHome: () -> Unit = {
        viewModel.openTopicDetail(null)
        viewModel.selectSubject(null)
        viewModel.setUserMode(AppUserMode.KIDS)
        viewModel.setTab(AloTab.CURRICULUM)
        homeResetToken++
    }

    if (isShowingSplash) {
        AaloSplashScreen(
            onSplashFinished = { viewModel.dismissSplash() }
        )
    } else if (!isOnboardingCompleted) {
        AaloOnboardingScreen(
            onGetStarted = { name, guardianPhone -> viewModel.completeOnboarding(name, guardianPhone) }
        )
    } else if (currentTab == AloTab.CHAT) {
        // The tutor is a destination of its own. It is intentionally outside the
        // app scaffold so no navigation chrome can shrink the learning canvas.
        LearningChatRoom(viewModel = viewModel, ttsManager = ttsManager, userMode = userMode,
            onGoHome = {
                viewModel.minimizeChat()
                goHome()
            })
    } else {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                if (currentTab != AloTab.CHAT) {
                    AloTopBar(
                        stats = todayStats,
                        completedTopicCount = completedTopicIds.size,
                        userMode = userMode,
                        profileName = profileName,
                        onUserModeChanged = { mode ->
                            viewModel.setUserMode(mode)
                            viewModel.openTopicDetail(null)
                            viewModel.setTab(AloTab.CURRICULUM)
                        }
                    )
                }
            },
            bottomBar = {
                if (currentTab != AloTab.CHAT) {
                    AloBottomNavigation(
                        currentTab = currentTab,
                        onTabSelected = { tab ->
                            if (tab == AloTab.CURRICULUM) goHome() else {
                                if (tab == AloTab.QUIZ) viewModel.setUserMode(AppUserMode.KIDS)
                                if (tab == AloTab.CHAT) {
                                    viewModel.cancelFollowUp()
                                    viewModel.clearChapterContext()
                                    viewModel.setChatAiMode(ChatAiMode.GENERAL)
                                    viewModel.setStudySection(StudySection.UNIVERSAL)
                                }
                                viewModel.setTab(tab)
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AloTab.CURRICULUM -> {
                    val selectedStage by viewModel.selectedStage.collectAsStateWithLifecycle()
                    val selectedClassNumber by viewModel.selectedClassNumber.collectAsStateWithLifecycle()
                    val selectedSubjectId by viewModel.selectedSubjectId.collectAsStateWithLifecycle()
                    val searchQuery by viewModel.curriculumSearchQuery.collectAsStateWithLifecycle()
                    val currentClassLevel = viewModel.getCurrentClassLevel()
                    val selectedTopicDetail by viewModel.selectedTopicForDetail.collectAsStateWithLifecycle()

                    if (userMode == AppUserMode.TEACHER_PARENT) {
                        ReferenceStudioScreen(
                            onAsk = { question ->
                                viewModel.askStudioInChat(question)
                            },
                            onOpenCurriculum = { viewModel.setUserMode(AppUserMode.KIDS) }
                        )
                    } else ReferenceCurriculumScreen(
                        selectedStage = selectedStage,
                        selectedClassNumber = selectedClassNumber,
                        selectedSubjectId = selectedSubjectId,
                        searchQuery = searchQuery,
                        currentClassLevel = currentClassLevel,
                        selectedTopicDetail = selectedTopicDetail,
                        completedTopicIds = completedTopicIds,
                        learningStars = learningStars,
                        onStageSelected = { viewModel.selectStage(it) },
                        onClassSelected = { viewModel.selectClass(it) },
                        onSubjectSelected = { viewModel.selectSubject(it) },
                        onSearchQueryChanged = { viewModel.setCurriculumSearchQuery(it) },
                        onTopicClicked = { viewModel.openTopicDetail(it) },
                        onCompleteTopic = { viewModel.markTopicComplete(it) },
                        onCloseTopicDetail = { viewModel.openTopicDetail(null) },
                        onAskInChat = { topic -> viewModel.askTopicInChat(topic) },
                        onOpenQuiz = { viewModel.setTab(AloTab.QUIZ) },
                        onOpenStudio = {
                            viewModel.setUserMode(AppUserMode.TEACHER_PARENT)
                        },
                        onOpenGeneralChat = {
                            viewModel.cancelFollowUp()
                            viewModel.clearChapterContext()
                            viewModel.setChatAiMode(ChatAiMode.GENERAL)
                            viewModel.setStudySection(StudySection.UNIVERSAL)
                            viewModel.setTab(AloTab.CHAT)
                        },
                        homeResetToken = homeResetToken
                    )
                }

                AloTab.CHAT -> Unit

                AloTab.FORMULAS -> {
                    val selectedSubject by viewModel.selectedFormulaSubject.collectAsStateWithLifecycle()
                    val searchQuery by viewModel.formulaSearchQuery.collectAsStateWithLifecycle()
                    val filteredFormulas = viewModel.getFilteredFormulas()

                    FormulaScreen(
                        formulas = filteredFormulas,
                        selectedSubject = selectedSubject,
                        searchQuery = searchQuery,
                        onSubjectSelected = { viewModel.setFormulaSubject(it) },
                        onSearchQueryChanged = { viewModel.setFormulaSearchQuery(it) },
                        onAskInChat = { formula -> viewModel.askFormulaInChat(formula) }
                    )
                }

                AloTab.QUIZ -> {
                    val selectedSubject by viewModel.selectedQuizSubject.collectAsStateWithLifecycle()
                    val questions = viewModel.getFilteredQuestions()
                    val currentIndex by viewModel.currentQuizIndex.collectAsStateWithLifecycle()
                    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()
                    val isEvaluated by viewModel.isAnswerEvaluated.collectAsStateWithLifecycle()
                    val score by viewModel.quizScore.collectAsStateWithLifecycle()
                    val isFinished by viewModel.quizFinished.collectAsStateWithLifecycle()

                    ReferenceQuizScreen(
                        questions = questions,
                        currentIndex = currentIndex,
                        selectedOption = selectedOption,
                        isEvaluated = isEvaluated,
                        score = score,
                        isFinished = isFinished,
                        selectedSubject = selectedSubject,
                        onSubjectSelected = { viewModel.setQuizSubject(it) },
                        onOptionSelected = { viewModel.selectQuizOption(it) },
                        onSubmitAnswer = { viewModel.submitQuizAnswer() },
                        onNextQuestion = { viewModel.nextQuizQuestion() },
                        onRestartQuiz = { viewModel.restartQuiz() },
                        onAskInChat = { query ->
                            viewModel.askQuizInChat(query)
                        }
                    )
                }

                AloTab.NOTES -> {
                    val notesList by viewModel.notesList.collectAsStateWithLifecycle()

                    NotesScreen(
                        notes = notesList,
                        onCreateNote = { title, content, subject ->
                            viewModel.createCustomNote(title, content, subject)
                        },
                        onTogglePin = { note -> viewModel.togglePinNote(note) },
                        onDeleteNote = { id -> viewModel.deleteNote(id) }
                    )
                }
            }

            if (isChatMinimized) {
                MinimizedTutorBubble(
                    onRestore = viewModel::restoreMinimizedChat,
                    onClose = viewModel::closeChat
                )
            }
        }
    }
}

}

@Composable
private fun MinimizedTutorBubble(onRestore: () -> Unit, onClose: () -> Unit) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        val density = LocalDensity.current
        val leftLimit = with(density) { -(maxWidth - 96.dp).toPx() }.coerceAtMost(0f)
        val topLimit = with(density) { -(maxHeight - 112.dp).toPx() }.coerceAtMost(0f)
        val bottomLimit = with(density) { 80.dp.toPx() }
        Box(
            Modifier
                .offset { IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt()) }
                .padding(end = 18.dp, bottom = 92.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        dragOffset = Offset(
                            (dragOffset.x + dragAmount.x).coerceIn(leftLimit, 0f),
                            (dragOffset.y + dragAmount.y).coerceIn(topLimit, bottomLimit)
                        )
                    }
                }
        ) {
            BadgedBox(badge = { Badge(containerColor = Color(0xFF2B8075), contentColor = Color.White) { Text("চ্যাট", fontSize = 9.sp, fontWeight = FontWeight.Bold) } }) {
                Surface(Modifier.clip(CircleShape).clickable(onClick = onRestore), CircleShape, Color.White, shadowElevation = 12.dp) {
                    Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AutoAwesome, "চ্যাট আবার খুলুন", tint = Color(0xFF2B8075), modifier = Modifier.size(30.dp))
                    }
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).size(32.dp)) {
                Box(Modifier.size(19.dp).background(Color(0xFF526966), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Close, "চ্যাট বন্ধ করো", tint = Color.White, modifier = Modifier.size(10.dp))
                }
            }
        }
    }
}

@Composable
private fun LearningChatRoom(
    viewModel: AloViewModel,
    ttsManager: AloTtsManager,
    userMode: com.example.ui.AppUserMode,
    onGoHome: () -> Unit
) {
    val chatList by viewModel.filteredChatList.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val isThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val chatAiMode by viewModel.chatAiMode.collectAsStateWithLifecycle()
    val selectedSection by viewModel.selectedStudySection.collectAsStateWithLifecycle()
    val activeChapterContext by viewModel.activeChapterContext.collectAsStateWithLifecycle()
    val activeSubjectContext by viewModel.activeSubjectContext.collectAsStateWithLifecycle()
    val activeFollowUpChat by viewModel.activeFollowUpChat.collectAsStateWithLifecycle()
    val sectionCounts by viewModel.chatCountBySection.collectAsStateWithLifecycle()

    ChatScreen(
        chatList = chatList,
        inputText = inputText,
        isThinking = isThinking,
        chatAiMode = chatAiMode,
        selectedSection = selectedSection,
        activeChapterContext = activeChapterContext,
        activeSubjectContext = activeSubjectContext,
        activeFollowUpChat = activeFollowUpChat,
        sectionCounts = sectionCounts,
        userMode = userMode,
        onModeChanged = viewModel::setChatAiMode,
        onSectionChanged = viewModel::setStudySection,
        onClearChapterContext = viewModel::clearChapterContext,
        onStartFollowUp = viewModel::startFollowUp,
        onCancelFollowUp = viewModel::cancelFollowUp,
        onInputChanged = viewModel::updateInputText,
        onSendQuery = viewModel::sendQuery,
        onSaveToNotes = viewModel::saveChatAsNote,
        onDeleteChat = viewModel::deleteChat,
        onClearAllChats = viewModel::clearCurrentSectionChats,
        onMinimizeChat = viewModel::minimizeChat,
        onCloseChat = viewModel::closeChat,
        onGoHome = onGoHome,
        ttsManager = ttsManager
    )
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
