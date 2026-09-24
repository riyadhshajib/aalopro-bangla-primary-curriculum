package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ai.AloOfflineAiEngine
import com.example.ai.AloFollowUpExplainer
import com.example.ai.AloTutorManager
import com.example.ai.ChatAiMode
import com.example.ai.StudySection
import com.example.analytics.AaloAnalytics
import com.example.ai.gemini.ChatTurn
import com.example.ai.needle.AloCommandRouter
import com.example.ai.needle.NeedleIntent
import com.example.data.local.AloChatEntity
import com.example.data.local.AloNoteEntity
import com.example.data.local.AloRepository
import com.example.data.local.AloStudyStatEntity
import com.example.data.model.BangladeshCurriculumRepository
import com.example.data.model.CurriculumClassLevel
import com.example.data.model.CurriculumStage
import com.example.data.model.CurriculumTopic
import com.example.data.model.FormulaRepository
import com.example.data.model.QuizQuestion
import com.example.data.model.QuizRepository
import com.example.data.model.StudyFormula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppUserMode(val titleBangla: String) {
    KIDS("শিশুদের পড়াশোনা"),
    TEACHER_PARENT("শিক্ষক ও অভিভাবক")
}

enum class AaloUserRole(val titleBangla: String) {
    STUDENT("শিক্ষার্থী"), PARENT("অভিভাবক"), TEACHER("শিক্ষক")
}

enum class AloTab(val labelBangla: String) {
    CHAT("আলো বন্ধু"),
    CURRICULUM("বই ও পড়া"),
    FORMULAS("গণিত ও সূত্র"),
    QUIZ("ম্যাজিক কুইজ"),
    NOTES("আমার নোটস")
}

class AloViewModel(
    private val repository: AloRepository,
    private val context: Context
) : ViewModel() {
    private val profilePrefs = context.getSharedPreferences("aalo_profile", Context.MODE_PRIVATE)
    private val _profileName = MutableStateFlow(profilePrefs.getString("name", "").orEmpty())
    val profileName: StateFlow<String> = _profileName.asStateFlow()
    private val _profileRole = MutableStateFlow(
        runCatching { AaloUserRole.valueOf(profilePrefs.getString("role", "STUDENT").orEmpty()) }
            .getOrDefault(AaloUserRole.STUDENT)
    )
    val profileRole: StateFlow<AaloUserRole> = _profileRole.asStateFlow()
    private val _completedTopicIds = MutableStateFlow(profilePrefs.getStringSet("completed_topics", emptySet())?.toSet().orEmpty())
    val completedTopicIds: StateFlow<Set<String>> = _completedTopicIds.asStateFlow()

    val chatList: StateFlow<List<AloChatEntity>> = repository.allChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Section filter for Chat: CLASS_1, CLASS_2, CLASS_3, CLASS_4, CLASS_5, UNIVERSAL, ALL
    private val _selectedStudySection = MutableStateFlow(StudySection.CLASS_1)
    val selectedStudySection: StateFlow<StudySection> = _selectedStudySection.asStateFlow()

    // Active Chapter / Topic context (when chatting about a specific textbook chapter)
    private val _activeChapterContext = MutableStateFlow<String?>(null)
    val activeChapterContext: StateFlow<String?> = _activeChapterContext.asStateFlow()

    private val _activeSubjectContext = MutableStateFlow<String?>(null)
    val activeSubjectContext: StateFlow<String?> = _activeSubjectContext.asStateFlow()

    // Active follow-up topic context
    private val _activeFollowUpChat = MutableStateFlow<AloChatEntity?>(null)
    val activeFollowUpChat: StateFlow<AloChatEntity?> = _activeFollowUpChat.asStateFlow()

    // Dynamic Class & Chapter isolated chat list
    val filteredChatList: StateFlow<List<AloChatEntity>> = combine(
        repository.allChats,
        _selectedStudySection,
        _activeChapterContext
    ) { chats, section, chapterCtx ->
        chats.filter { chat ->
            val matchesSection = when (section) {
                StudySection.ALL -> true
                else -> chat.classCategory == section.id
            }
            val matchesChapter = if (chapterCtx.isNullOrBlank()) {
                true
            } else {
                chat.chapterTitle.equals(chapterCtx, ignoreCase = true) || chat.chapterTitle.isBlank()
            }
            matchesSection && matchesChapter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Message counts for each class section badge
    val chatCountBySection: StateFlow<Map<String, Int>> = repository.allChats.combine(_selectedStudySection) { chats, _ ->
        val map = mutableMapOf<String, Int>()
        map["ALL"] = chats.size
        for (section in StudySection.entries) {
            if (section != StudySection.ALL) {
                map[section.id] = chats.count { it.classCategory == section.id }
            }
        }
        map
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val notesList: StateFlow<List<AloNoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Splash and Onboarding state
    private val _isShowingSplash = MutableStateFlow(true)
    val isShowingSplash: StateFlow<Boolean> = _isShowingSplash.asStateFlow()

    private val _isOnboardingCompleted = MutableStateFlow(
        profilePrefs.getBoolean("completed", false) && _profileName.value.isNotBlank()
    )
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    // Mode: KIDS (Primary 1-5 learning) vs TEACHER_PARENT (Pedagogy & Parenting Corner)
    private val _userMode = MutableStateFlow(
        if (_profileRole.value == AaloUserRole.STUDENT) AppUserMode.KIDS else AppUserMode.TEACHER_PARENT
    )
    val userMode: StateFlow<AppUserMode> = _userMode.asStateFlow()

    fun setUserMode(mode: AppUserMode) {
        if (_userMode.value != mode) AaloAnalytics.event(AaloAnalytics.Events.AUDIENCE_SELECTED, "audience" to mode.name.lowercase())
        _userMode.value = mode
    }

    private val _currentTab = MutableStateFlow(AloTab.CURRICULUM)
    val currentTab: StateFlow<AloTab> = _currentTab.asStateFlow()

    private val _isChatMinimized = MutableStateFlow(false)
    val isChatMinimized: StateFlow<Boolean> = _isChatMinimized.asStateFlow()

    // Chat AI Mode: GENERAL (Universal/Any Class/Any Language) vs CURRICULUM (NCTB 1-10)
    private val _chatAiMode = MutableStateFlow(ChatAiMode.GENERAL)
    val chatAiMode: StateFlow<ChatAiMode> = _chatAiMode.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _todayStats = MutableStateFlow(AloStudyStatEntity(dateKey = ""))
    val todayStats: StateFlow<AloStudyStatEntity> = _todayStats.asStateFlow()

    // Curriculum tab state
    private val _selectedStage = MutableStateFlow(CurriculumStage.PRIMARY)
    val selectedStage: StateFlow<CurriculumStage> = _selectedStage.asStateFlow()

    private val _selectedClassNumber = MutableStateFlow(1) // 1 to 10
    val selectedClassNumber: StateFlow<Int> = _selectedClassNumber.asStateFlow()

    private val _selectedSubjectId = MutableStateFlow<String?>(null)
    val selectedSubjectId: StateFlow<String?> = _selectedSubjectId.asStateFlow()

    private val _curriculumSearchQuery = MutableStateFlow("")
    val curriculumSearchQuery: StateFlow<String> = _curriculumSearchQuery.asStateFlow()

    private val _selectedTopicForDetail = MutableStateFlow<CurriculumTopic?>(null)
    val selectedTopicForDetail: StateFlow<CurriculumTopic?> = _selectedTopicForDetail.asStateFlow()

    // Formula tab state
    private val _selectedFormulaSubject = MutableStateFlow("সকল")
    val selectedFormulaSubject: StateFlow<String> = _selectedFormulaSubject.asStateFlow()

    private val _formulaSearchQuery = MutableStateFlow("")
    val formulaSearchQuery: StateFlow<String> = _formulaSearchQuery.asStateFlow()

    // Quiz tab state
    private val _selectedQuizSubject = MutableStateFlow("সকল")
    val selectedQuizSubject: StateFlow<String> = _selectedQuizSubject.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<Int?>(null)
    val selectedOption: StateFlow<Int?> = _selectedOption.asStateFlow()

    private val _isAnswerEvaluated = MutableStateFlow(false)
    val isAnswerEvaluated: StateFlow<Boolean> = _isAnswerEvaluated.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished.asStateFlow()

    init {
        refreshStats()
    }

    fun dismissSplash() {
        _isShowingSplash.value = false
    }

    fun completeOnboarding(name: String) {
        val safeName = name.trim().take(40)
        if (safeName.isBlank()) return
        viewModelScope.launch {
            // A new tester profile must never inherit this device's earlier test chats,
            // notes or points. This touches local app data only, not Firebase.
            repository.clearLocalStudyDataForNewProfile()
            profilePrefs.edit().putString("name", safeName).putString("role", AaloUserRole.STUDENT.name)
                .putBoolean("completed", true).remove("completed_topics").apply()
            _completedTopicIds.value = emptySet()
            _profileName.value = safeName
            _profileRole.value = AaloUserRole.STUDENT
            _userMode.value = AppUserMode.KIDS
            _isOnboardingCompleted.value = true
            _isShowingSplash.value = false
            AaloAnalytics.event(AaloAnalytics.Events.ONBOARDING_COMPLETED)
            refreshStats()
        }
    }

    fun setTab(tab: AloTab) {
        if (_currentTab.value != tab) AaloAnalytics.screen(tab.name.lowercase())
        _currentTab.value = tab
        if (tab == AloTab.CHAT) _isChatMinimized.value = false
    }

    /** Keeps the current discussion alive while returning to the learning map. */
    fun minimizeChat() {
        _isChatMinimized.value = true
        _currentTab.value = AloTab.CURRICULUM
    }

    fun restoreMinimizedChat() {
        _isChatMinimized.value = false
        _currentTab.value = AloTab.CHAT
    }

    /** Leaves the learning room without carrying a chapter or follow-up into a new chat. */
    fun closeChat() {
        _isChatMinimized.value = false
        _activeFollowUpChat.value = null
        clearChapterContext()
        _inputText.value = ""
        _currentTab.value = AloTab.CURRICULUM
    }


    fun setStudySection(section: StudySection) {
        _selectedStudySection.value = section
        _activeChapterContext.value = null // Reset chapter filter when user changes class tab
        when (section) {
            StudySection.CLASS_1 -> _selectedClassNumber.value = 1
            StudySection.CLASS_2 -> _selectedClassNumber.value = 2
            StudySection.CLASS_3 -> _selectedClassNumber.value = 3
            StudySection.CLASS_4 -> _selectedClassNumber.value = 4
            StudySection.CLASS_5 -> _selectedClassNumber.value = 5
            else -> {}
        }
    }

    fun clearChapterContext() {
        _activeChapterContext.value = null
        _activeSubjectContext.value = null
    }

    fun startFollowUp(chat: AloChatEntity) {
        _activeFollowUpChat.value = chat
        // If the chat belonged to a specific section, switch to it for context consistency
        val chatSection = StudySection.fromId(chat.classCategory)
        _chatAiMode.value = if (chatSection == StudySection.UNIVERSAL) ChatAiMode.GENERAL
            else ChatAiMode.CURRICULUM
        if (chatSection != StudySection.ALL && _selectedStudySection.value != chatSection) {
            _selectedStudySection.value = chatSection
        }
        if (chat.chapterTitle.isNotBlank()) {
            _activeChapterContext.value = chat.chapterTitle
        }
    }

    fun cancelFollowUp() {
        _activeFollowUpChat.value = null
    }

    fun clearCurrentSectionChats() {
        viewModelScope.launch {
            val section = _selectedStudySection.value
            if (section == StudySection.ALL) {
                repository.clearChats()
            } else {
                repository.clearChatsByCategory(section.id)
            }
        }
    }

    fun setChatAiMode(mode: ChatAiMode) {
        _chatAiMode.value = mode
        if (mode == ChatAiMode.GENERAL) {
            _selectedStudySection.value = StudySection.UNIVERSAL
            _activeFollowUpChat.value = null
            clearChapterContext()
        } else if (_selectedStudySection.value == StudySection.UNIVERSAL ||
            _selectedStudySection.value == StudySection.ALL) {
            _selectedStudySection.value = StudySection.forClassNumber(_selectedClassNumber.value)
        }
    }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun sendQuery(queryOverride: String? = null) {
        val q = queryOverride ?: _inputText.value
        val trimmed = q.trim()
        if (trimmed.isEmpty()) return

        val chapterForContinuation = _activeChapterContext.value
        val recentLessonAnswer = if (!chapterForContinuation.isNullOrBlank() &&
            trimmed.length <= 60 && AloFollowUpExplainer.isFollowUpSimplificationRequest(trimmed)) {
            filteredChatList.value.firstOrNull { chat ->
                chat.classCategory == _selectedStudySection.value.id &&
                    chat.chapterTitle.equals(chapterForContinuation, ignoreCase = true) &&
                    (chat.subjectTag.contains("যাচাইকৃত ছবি অনুশীলনী") ||
                        chat.subjectTag.contains("বাছাই করা টপিক")) &&
                    System.currentTimeMillis() - chat.timestamp in 0..600_000L
            }
        } else null
        // Short clarification phrases such as "বুঝিনি", "আরেকটু বুঝাও" or
        // "Explain it more simply" naturally refer to the immediately preceding
        // general answer even when the learner did not tap the Follow-up chip.
        val recentGeneralAnswer = if (_chatAiMode.value == ChatAiMode.GENERAL &&
            trimmed.length <= 60 && AloFollowUpExplainer.isFollowUpSimplificationRequest(trimmed)) {
            filteredChatList.value.firstOrNull { chat ->
                chat.classCategory == StudySection.UNIVERSAL.id &&
                    System.currentTimeMillis() - chat.timestamp in 0..600_000L &&
                    !chat.responseBangla.contains("unavailable right now", ignoreCase = true) &&
                    !chat.responseBangla.contains("এই মুহূর্তে উত্তর দিতে পারছে না") &&
                    !chat.responseBangla.startsWith("একটি ত্রুটি ঘটেছে")
            }
        } else null
        val parentFollowUp = _activeFollowUpChat.value ?: recentLessonAnswer ?: recentGeneralAnswer
        AaloAnalytics.event(
            AaloAnalytics.Events.TUTOR_QUESTION,
            "mode" to _chatAiMode.value.name.lowercase(),
            "follow_up" to if (parentFollowUp != null) "yes" else "no"
        )
        val isChapterPageQuestion = !_activeChapterContext.value.isNullOrBlank() &&
            Regex("(?:পৃষ্ঠা|page)\\s*[০-৯0-9]+", RegexOption.IGNORE_CASE).containsMatchIn(trimmed)
        // A typed question is a new question unless the learner explicitly
        // tapped Follow up. Opening a lesson must not label an unrelated new
        // question as belonging to that lesson.
        val currentChapter = if (parentFollowUp != null || queryOverride != null || isChapterPageQuestion) {
            _activeChapterContext.value ?: parentFollowUp?.chapterTitle ?: ""
        } else ""
        _inputText.value = ""
        _isAiThinking.value = true

        viewModelScope.launch {
            try {
                val command = AloCommandRouter.classify(context, trimmed)
                if (command != null && handleNavigationCommand(command, trimmed)) {
                    _activeFollowUpChat.value = null
                    _isAiThinking.value = false
                    return@launch
                }
                // Determine target section
                val currentSection = if (parentFollowUp != null) {
                    StudySection.fromId(parentFollowUp.classCategory)
                } else if (_chatAiMode.value == ChatAiMode.GENERAL) {
                    StudySection.UNIVERSAL
                } else if (_selectedStudySection.value != StudySection.ALL) {
                    _selectedStudySection.value
                } else if (_chatAiMode.value == ChatAiMode.CURRICULUM) {
                    StudySection.forClassNumber(_selectedClassNumber.value)
                } else {
                    StudySection.UNIVERSAL
                }

                // A new question must not inherit unrelated chats just because
                // they appear in the same class section. Only an explicit
                // follow-up carries its parent answer into the model context.
                val historyTurns = parentFollowUp?.let {
                    listOf(ChatTurn(query = it.query, response = it.responseBangla))
                } ?: emptyList()

                val tutorResponse = AloTutorManager.answerQuestion(
                    context = context,
                    query = trimmed,
                    mode = _chatAiMode.value,
                    section = currentSection,
                    history = historyTurns,
                    parentFollowUp = parentFollowUp,
                    chapterContext = currentChapter
                )
                AaloAnalytics.event(
                    AaloAnalytics.Events.TUTOR_ANSWER,
                    "mode" to _chatAiMode.value.name.lowercase(),
                    "source" to if (tutorResponse.isOnlineGemini) "online" else "offline",
                    "follow_up" to if (parentFollowUp != null) "yes" else "no"
                )

                repository.saveChat(
                    query = trimmed,
                    response = tutorResponse.text,
                    subjectTag = "${tutorResponse.subjectTag} • ${tutorResponse.modelLabel}",
                    classCategory = currentSection.id,
                    chapterTitle = currentChapter,
                    parentChatId = parentFollowUp?.id
                )

                // Reset active follow-up context after question is sent
                _activeFollowUpChat.value = null
                if (parentFollowUp == null && queryOverride == null && !isChapterPageQuestion) clearChapterContext()
                refreshStats()
            } catch (e: Exception) {
                AaloAnalytics.event(AaloAnalytics.Events.TUTOR_ERROR)
                repository.saveChat(
                    query = trimmed,
                    response = "কাজটি এখন করা গেল না: ${e.localizedMessage ?: "সমস্যাটি বোঝা যায়নি"}। আরেকবার চেষ্টা করো।",
                    subjectTag = "ত্রুটি",
                    classCategory = _selectedStudySection.value.id,
                    chapterTitle = currentChapter
                )
                _activeFollowUpChat.value = null
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    /** Applies only validated, reversible UI actions; Needle never supplies lesson answers. */
    private suspend fun handleNavigationCommand(command: NeedleIntent, originalInput: String): Boolean {
        return when (command.function) {
            "open_curriculum" -> {
                val grade = command.arguments["grade"]?.toIntOrNull()?.takeIf { it in 1..5 } ?: return false
                selectClass(grade)
                val requested = when (command.arguments["subject"].orEmpty().lowercase()) {
                    "math" -> "গণিত"
                    "bangla" -> "বাংলা"
                    "english" -> "ইংরেজি"
                    "science" -> "বিজ্ঞান"
                    "bgs" -> "বিশ্বপরিচয়"
                    "islam" -> "ইসলাম"
                    else -> command.arguments["subject"].orEmpty().lowercase()
                }
                val subject = getCurrentClassLevel()?.subjects?.firstOrNull { item ->
                    val name = item.nameBangla.lowercase()
                    name.contains(requested) || requested.contains(name) ||
                        (requested.contains("গণিত") && name.contains("গণিত")) ||
                        (requested.contains("বাংলা") && name.contains("বাংলা")) ||
                        ((requested.contains("ইংরেজ") || requested.contains("english")) && (name.contains("ইংরেজ") || name.contains("english")))
                } ?: return false
                selectSubject(subject.id)
                setTab(AloTab.CURRICULUM)
                AaloAnalytics.event("needle_route", "route" to "curriculum")
                true
            }
            "start_quiz" -> {
                command.arguments["grade"]?.toIntOrNull()?.takeIf { it in 1..5 }?.let(::selectClass)
                command.arguments["subject"]?.takeIf { it.isNotBlank() }?.let(::setQuizSubject)
                setTab(AloTab.QUIZ)
                AaloAnalytics.event("needle_route", "route" to "quiz")
                true
            }
            "save_note" -> {
                val text = command.arguments["text"]?.takeIf { it.isNotBlank() && originalInput.contains(it, true) } ?: return false
                repository.createCustomNote(text.take(60), text, "নিজের নোট")
                setTab(AloTab.NOTES)
                AaloAnalytics.event("needle_route", "route" to "note")
                true
            }
            else -> false
        }
    }

    fun askFormulaInChat(formula: StudyFormula) {
        _activeFollowUpChat.value = null
        _chatAiMode.value = ChatAiMode.CURRICULUM
        val classSection = StudySection.forClassNumber(_selectedClassNumber.value)
        _selectedStudySection.value = classSection
        _activeChapterContext.value = formula.titleBangla
        _activeSubjectContext.value = formula.subject
        setTab(AloTab.CHAT)
        sendQuery(formula.relatedQuery)
    }

    fun askQuizInChat(question: String) {
        _activeFollowUpChat.value = null
        _chatAiMode.value = ChatAiMode.GENERAL
        _selectedStudySection.value = StudySection.UNIVERSAL
        _activeChapterContext.value = question
        _activeSubjectContext.value = "কুইজ"
        setTab(AloTab.CHAT)
        sendQuery(question)
    }

    fun askStudioInChat(question: String) {
        _activeFollowUpChat.value = null
        _chatAiMode.value = ChatAiMode.GENERAL
        _selectedStudySection.value = StudySection.UNIVERSAL
        _activeChapterContext.value = question
        _activeSubjectContext.value = "অভিভাবক ও শিক্ষকের জন্য"
        setTab(AloTab.CHAT)
        sendQuery(question)
    }

    // Curriculum Management
    fun selectStage(stage: CurriculumStage) {
        _selectedStage.value = stage
        _selectedClassNumber.value = stage.minClass
        _selectedSubjectId.value = null
        _selectedTopicForDetail.value = null
        _selectedStudySection.value = StudySection.forClassNumber(stage.minClass)
    }

    fun selectClass(classNumber: Int) {
        AaloAnalytics.event(AaloAnalytics.Events.CLASS_SELECTED, "class_number" to classNumber.toString())
        _selectedClassNumber.value = classNumber
        _selectedSubjectId.value = null
        _selectedTopicForDetail.value = null
        _selectedStudySection.value = StudySection.forClassNumber(classNumber)
    }

    fun selectSubject(subjectId: String?) {
        _selectedSubjectId.value = subjectId
    }

    fun setCurriculumSearchQuery(query: String) {
        _curriculumSearchQuery.value = query
    }

    fun openTopicDetail(topic: CurriculumTopic?) {
        if (topic != null) AaloAnalytics.event(AaloAnalytics.Events.TOPIC_OPENED, "class_number" to _selectedClassNumber.value.toString())
        _selectedTopicForDetail.value = topic
    }

    fun markTopicComplete(topic: CurriculumTopic) {
        if (topic.id !in _completedTopicIds.value) AaloAnalytics.event(AaloAnalytics.Events.TOPIC_COMPLETED, "class_number" to _selectedClassNumber.value.toString())
        val updated = _completedTopicIds.value + topic.id
        _completedTopicIds.value = updated
        profilePrefs.edit().putStringSet("completed_topics", updated).apply()
    }

    fun askTopicInChat(topic: CurriculumTopic, subjectName: String? = null) {
        _activeFollowUpChat.value = null
        _chatAiMode.value = ChatAiMode.CURRICULUM
        val classSection = StudySection.forClassNumber(_selectedClassNumber.value)
        _selectedStudySection.value = classSection
        _activeChapterContext.value = topic.titleBangla
        _activeSubjectContext.value = subjectName ?: getCurrentClassLevel()?.subjects?.find { s -> s.topics.any { it.id == topic.id } }?.nameBangla
        setTab(AloTab.CHAT)
        sendQuery(topic.qaList.firstOrNull()?.question ?: "${topic.titleBangla} বিষয়টি সহজ করে বোঝাও।")
    }

    fun getCurrentClassLevel(): CurriculumClassLevel? {
        val classNum = _selectedClassNumber.value
        return BangladeshCurriculumRepository.featuredClassLevels.find { it.levelNumber == classNum }
    }

    fun saveChatAsNote(chat: AloChatEntity) {
        viewModelScope.launch {
            repository.saveChatAsNote(chat)
        }
    }

    fun deleteChat(id: Long) {
        viewModelScope.launch {
            repository.deleteChat(id)
        }
    }

    fun clearAllChats() {
        viewModelScope.launch {
            repository.clearChats()
        }
    }

    // Notes management
    fun createCustomNote(title: String, content: String, subject: String) {
        viewModelScope.launch {
            repository.createCustomNote(title, content, subject)
            AaloAnalytics.event(AaloAnalytics.Events.NOTE_CREATED)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun togglePinNote(note: AloNoteEntity) {
        viewModelScope.launch {
            repository.togglePinNote(note)
        }
    }

    // Formula Filters
    fun setFormulaSubject(subject: String) {
        _selectedFormulaSubject.value = subject
    }

    fun setFormulaSearchQuery(query: String) {
        _formulaSearchQuery.value = query
    }

    fun getFilteredFormulas(): List<StudyFormula> {
        val subject = _selectedFormulaSubject.value
        val query = _formulaSearchQuery.value.trim().lowercase()

        return FormulaRepository.formulas.filter { formula ->
            val matchSubject = (subject == "সকল" || formula.subject == subject)
            val matchQuery = query.isEmpty() ||
                formula.titleBangla.lowercase().contains(query) ||
                formula.formulaSymbol.lowercase().contains(query) ||
                formula.explanationBangla.lowercase().contains(query)
            matchSubject && matchQuery
        }
    }

    // Quiz functions
    fun setQuizSubject(subject: String) {
        _selectedQuizSubject.value = subject
        restartQuiz()
    }

    fun getFilteredQuestions(): List<QuizQuestion> {
        val subject = _selectedQuizSubject.value
        return if (subject == "সকল") {
            QuizRepository.questions
        } else {
            QuizRepository.questions.filter { it.subject == subject }
        }
    }

    fun selectQuizOption(index: Int) {
        if (!_isAnswerEvaluated.value) {
            _selectedOption.value = index
        }
    }

    fun submitQuizAnswer() {
        if (_isAnswerEvaluated.value) return
        val selected = _selectedOption.value ?: return
        val currentQuestions = getFilteredQuestions()
        val index = _currentQuizIndex.value
        if (index >= currentQuestions.size) return

        val question = currentQuestions[index]
        _isAnswerEvaluated.value = true
        AaloAnalytics.event(AaloAnalytics.Events.QUIZ_ANSWERED, "correct" to if (selected == question.correctIndex) "yes" else "no")

        if (selected == question.correctIndex) {
            _quizScore.value += 1
        }
    }

    fun nextQuizQuestion() {
        val currentQuestions = getFilteredQuestions()
        if (_currentQuizIndex.value + 1 < currentQuestions.size) {
            _currentQuizIndex.value += 1
            _selectedOption.value = null
            _isAnswerEvaluated.value = false
        } else {
            _quizFinished.value = true
            AaloAnalytics.event(AaloAnalytics.Events.QUIZ_COMPLETED)
            viewModelScope.launch {
                repository.recordQuizResult(_quizScore.value)
                refreshStats()
            }
        }
    }

    fun restartQuiz() {
        _currentQuizIndex.value = 0
        _selectedOption.value = null
        _isAnswerEvaluated.value = false
        _quizScore.value = 0
        _quizFinished.value = false
    }

    private fun refreshStats() {
        viewModelScope.launch {
            _todayStats.value = repository.getTodayStats()
        }
    }

    class Factory(
        private val repository: AloRepository,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AloViewModel::class.java)) {
                return AloViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
