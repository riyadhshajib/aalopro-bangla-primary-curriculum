package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import com.example.ui.screens.DigitText as Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ai.AloMathSolver
import com.example.data.model.BangladeshCurriculumRepository
import com.example.data.model.CurriculumClassLevel
import com.example.data.model.CurriculumStage
import com.example.data.model.CurriculumSubject
import com.example.data.model.CurriculumTopic
import com.example.ui.theme.PremiumPurple
import com.example.ui.theme.PremiumPurpleContainer
import com.example.ui.theme.PremiumTeal
import com.example.ui.theme.WarmTextPrimary
import com.example.ui.theme.WarmTextSecondary
import com.example.ui.theme.WarmPaperBg
import com.example.ui.theme.WarmPaperBorder
import com.example.ui.theme.AaloYellowContainer
import com.example.ui.learningRankFor
import com.example.ui.learningRanks

/** The seven-screen reference's quiet learning path, using the existing curriculum state and data. */
@Composable
fun ReferenceCurriculumScreen(
    selectedStage: CurriculumStage,
    selectedClassNumber: Int,
    selectedSubjectId: String?,
    searchQuery: String,
    currentClassLevel: CurriculumClassLevel?,
    selectedTopicDetail: CurriculumTopic?,
    completedTopicIds: Set<String> = emptySet(),
    learningStars: Int = 0,
    onStageSelected: (CurriculumStage) -> Unit,
    onClassSelected: (Int) -> Unit,
    onSubjectSelected: (String?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onTopicClicked: (CurriculumTopic) -> Unit,
    onCompleteTopic: (CurriculumTopic) -> Unit = {},
    onCloseTopicDetail: () -> Unit,
    onAskInChat: (CurriculumTopic) -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenStudio: () -> Unit,
    onOpenGeneralChat: () -> Unit,
    homeResetToken: Int = 0,
    modifier: Modifier = Modifier
) {
    var onHome by rememberSaveable { mutableStateOf(true) }
    var showFullGuide by rememberSaveable { mutableStateOf(false) }
    var expandedClass by rememberSaveable { mutableStateOf<Int?>(null) }
    var showClassMenu by rememberSaveable { mutableStateOf(false) }
    var showRanks by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(homeResetToken) {
        onHome = true
        showFullGuide = false
        showClassMenu = false
        showRanks = false
        expandedClass = null
    }
    val subject = currentClassLevel?.subjects?.firstOrNull { it.id == selectedSubjectId }
    val isSearching = searchQuery.isNotBlank()

    BackHandler(enabled = selectedTopicDetail != null || selectedSubjectId != null || !onHome) {
        when {
            showFullGuide -> showFullGuide = false
            selectedTopicDetail != null -> onCloseTopicDetail()
            selectedSubjectId != null -> onSubjectSelected(null)
            else -> onHome = true
        }
    }

    if (showFullGuide && selectedTopicDetail != null) {
        Column(modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showFullGuide = false }) { Icon(Icons.Filled.ArrowBack, "পাঠে ফিরুন") }
                Text("পাঠ ও শেখার সাহায্য", style = MaterialTheme.typography.titleMedium)
            }
            CurriculumScreen(
                selectedStage, selectedClassNumber, selectedSubjectId, searchQuery,
                currentClassLevel, selectedTopicDetail, onStageSelected, onClassSelected,
                onSubjectSelected, onSearchQueryChanged, onTopicClicked, onCloseTopicDetail,
                onAskInChat, Modifier.weight(1f)
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(WarmPaperBg),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            if (!onHome || selectedTopicDetail != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        when {
                            selectedTopicDetail != null -> onCloseTopicDetail()
                            selectedSubjectId != null -> onSubjectSelected(null)
                            else -> onHome = true
                        }
                    }) { Icon(Icons.Filled.ArrowBack, "পেছনে") }
                    Text(
                        text = when {
                            selectedTopicDetail != null -> "পাঠের মূল অভিজ্ঞতা"
                            selectedSubjectId != null -> "অধ্যায়ের তালিকা"
                            else -> "বিষয় বেছে নাও"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = PremiumPurple
                    )
                }
            }
        }

        when {
            selectedTopicDetail != null -> {
                val topic = selectedTopicDetail
                val isMathSubject = selectedSubjectId?.endsWith("_math") == true
                // Mathematics keeps the full visual learning sequence. Other subjects only
                // show pages whose printed questions have structured offline answers.
                val visibleVisualExercises = if (isMathSubject) topic.visualExercises
                else topic.visualExercises.filter { it.pageQuestions.isNotEmpty() }
                item {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            ReferenceHeading("আজকের পাঠ", topic.titleBangla, "সহজ ভাষায় একটি করে বিষয় বোঝো")
                        }
                        Image(painterResource(gradeArtRes(selectedClassNumber)),
                            "এই শ্রেণির শেখার ইলাস্ট্রেশন", modifier = Modifier.size(72.dp),
                            contentScale = ContentScale.Fit)
                    }
                }
                item {
                    ReferenceCard(tint = Color(0xFFFFF1D2)) {
                        Icon(Icons.Filled.Lightbulb, null, tint = PremiumTeal, modifier = Modifier.size(34.dp))
                        Spacer(Modifier.height(12.dp))
                        Text(topic.summaryBangla.ifBlank { "এই অধ্যায়ের বিষয়গুলো একে একে শিখি।" },
                            style = MaterialTheme.typography.titleMedium, color = WarmTextPrimary)
                    }
                }
                if (topic.detailsBangla.isNotBlank()) item {
                    ReferenceCard {
                        Text("সহজ ব্যাখ্যা", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(topic.detailsBangla, style = MaterialTheme.typography.bodyMedium, color = WarmTextSecondary)
                    }
                }
                if (visibleVisualExercises.isNotEmpty()) item {
                    var exerciseIndex by rememberSaveable(topic.id, "exerciseIndex") { mutableIntStateOf(0) }
                    val safeIndex = exerciseIndex.coerceIn(visibleVisualExercises.indices)
                    val exercise = visibleVisualExercises[safeIndex]
                    var showAnswer by rememberSaveable(topic.id, safeIndex) { mutableStateOf(false) }
                    var largeImage by rememberSaveable(topic.id, safeIndex, "image") { mutableStateOf(false) }
                    ReferenceCard(tint = Color(0xFFFFF5E5)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isMathSubject) "ছবি দিয়ে অঙ্ক শিখি" else "বইয়ের প্রশ্ন ও উত্তর",
                                color = PremiumPurple,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(1f))
                            Text("${safeIndex + 1}/${visibleVisualExercises.size}", color = WarmTextSecondary,
                                style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(Modifier.height(8.dp))
                        Image(visualExercisePainter(exercise.imageResId, exercise.imageAssetPath), "ছবিভিত্তিক পাঠ ও অনুশীলনী",
                            modifier = Modifier.fillMaxWidth().height(if (largeImage) 490.dp else 265.dp)
                                .clickable { largeImage = !largeImage },
                            contentScale = ContentScale.Fit)
                        Text("ছবি বড় বা ছোট করতে চাপ দাও", color = WarmTextSecondary,
                            style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(10.dp))
                        Text(exercise.questionBangla, color = WarmTextPrimary,
                            style = MaterialTheme.typography.bodyLarge)
                        TextButton(onClick = { showAnswer = !showAnswer }) {
                            val activityPage = exercise.answerBangla.startsWith("করণীয়:")
                            Text(if (showAnswer) {
                                if (exercise.pageQuestions.isNotEmpty()) "প্রশ্ন ও উত্তর লুকাও"
                                else if (activityPage) "করণীয় লুকাও" else "উত্তর লুকাও"
                            } else {
                                if (exercise.pageQuestions.isNotEmpty())
                                    "এই পৃষ্ঠার প্রশ্ন ও উত্তর (${exercise.pageQuestions.size})"
                                else if (activityPage) "কীভাবে করব" else "উত্তর দেখো"
                            })
                        }
                        if (showAnswer) {
                            Text(exercise.answerBangla, color = WarmTextPrimary,
                                style = MaterialTheme.typography.bodyMedium)
                            exercise.pageQuestions.forEachIndexed { index, qa ->
                                Spacer(Modifier.height(10.dp))
                                Surface(
                                    color = if (index % 2 == 0) Color(0xFFFFFBF3) else Color(0xFFF0FAF7),
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(1.dp, PremiumTeal.copy(alpha = 0.15f))
                                ) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text("${index + 1}. ${qa.question}",
                                            color = WarmTextPrimary,
                                            style = MaterialTheme.typography.titleSmall)
                                        Spacer(Modifier.height(6.dp))
                                        Text("উত্তর: ${qa.answer}",
                                            color = WarmTextSecondary,
                                            style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                        Text(exercise.sourceBangla, color = WarmTextSecondary,
                            style = MaterialTheme.typography.bodySmall)
                        if (visibleVisualExercises.size > 1) {
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                TextButton(onClick = { exerciseIndex = safeIndex - 1 },
                                    enabled = safeIndex > 0) { Text("আগেরটি") }
                                TextButton(onClick = { exerciseIndex = safeIndex + 1 },
                                    enabled = safeIndex < visibleVisualExercises.lastIndex) { Text("পরেরটি") }
                            }
                        }
                    }
                }
                if (!topic.formulaOrRule.isNullOrBlank()) item {
                    ReferenceCard {
                        Text("নিয়ম / সূত্র", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        Text(topic.formulaOrRule, color = PremiumPurple, style = MaterialTheme.typography.titleMedium)
                    }
                }
                if (topic.qaList.isNotEmpty()) items(topic.qaList.take(3)) { qa ->
                    var answerVisible by rememberSaveable(topic.id, qa.question) { mutableStateOf(false) }
                    ReferenceCard(tint = Color(0xFFFFF5E4)) {
                        Text("একটু ভাবো ✦", color = Color(0xFFA45C12), style = MaterialTheme.typography.labelLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(qa.question, style = MaterialTheme.typography.titleMedium, color = WarmTextPrimary)
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = { answerVisible = !answerVisible }) {
                            Text(if (answerVisible) "উত্তর ঢেকে দাও" else "উত্তর ও ব্যাখ্যা দেখো")
                        }
                        if (answerVisible) Text(qa.answer, style = MaterialTheme.typography.bodyMedium, color = WarmTextPrimary)
                    }
                }
                item {
                    val completed = topic.id in completedTopicIds
                    Button(onClick = { onCompleteTopic(topic) }, enabled = !completed,
                        modifier = Modifier.fillMaxWidth()) {
                        Text(if (completed) "✦ নতুন উপাধি: Power Explorer" else "পাঠ শেষ হলে নতুন উপাধি পাবে")
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onAskInChat(topic) }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumPurple)) {
                            Icon(Icons.Filled.ChatBubbleOutline, null, modifier = Modifier.size(18.dp))
                            Text("  আলো শিক্ষককে জিজ্ঞাসা")
                        }
                        OutlinedButton(onClick = onOpenQuiz) { Text("কুইজ") }
                    }
                }
                item { OutlinedButton(onClick = { showFullGuide = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("পুরো পাঠ, শিক্ষক ও অভিভাবকের জন্য সাহায্য")
                } }
            }
            subject != null && !onHome -> {
                item { ReferenceHeading("${subject.topics.size}টি শেখার টপিক", subject.nameBangla, "${currentClassLevel?.classNameBangla} · একটি টপিক খুলে দেখো") }
                val topics = subject.topics
                items(topics, key = { it.id }) { topic ->
                    ReferenceListRow(
                        title = topic.titleBangla,
                        subtitle = topic.qaList.firstOrNull()?.question ?: topic.summaryBangla.take(54),
                        symbol = "খেলি",
                        onClick = { onTopicClicked(topic) }
                    )
                }
                if (topics.isEmpty()) item { Text("কোনো অধ্যায় পাওয়া যায়নি।", color = WarmTextSecondary) }
            }
            !onHome -> {
                item { ReferenceHeading("বিষয়", "কোন বিষয় নিয়ে খেলবে?", "${currentClassLevel?.classNameBangla} · বিষয়ভিত্তিক শেখার টপিক") }
                item { ReferenceCard(tint = AaloYellowContainer) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AutoStories, null, tint = PremiumPurple, modifier = Modifier.size(40.dp))
                        Text("  বই থেকে শেখা শুরু", style = MaterialTheme.typography.titleMedium, color = WarmTextPrimary)
                    }
                } }
                currentClassLevel?.subjects?.let { subjects ->
                    items(subjects, key = { it.id }) { item ->
                        ReferenceListRow(item.nameBangla,
                            if (item.topics.isEmpty()) "অধ্যায়ের তালিকা তৈরি হচ্ছে" else "${item.topics.size}টি অধ্যায়",
                            subjectMark(item),
                            onClick = { onSubjectSelected(item.id) })
                    }
                }
                item { OutlinedButton(onClick = onOpenGeneralChat, modifier = Modifier.fillMaxWidth()) { Text("যেকোনো প্রশ্ন আলো শিক্ষককে করো") } }
            }
            else -> {
                item { ReferenceHeading("ছোট্ট শেখার জগৎ", "খেলতে খেলতে শিখি", "শ্রেণি → বিষয় → শেখার টপিক") }
                item { GradeIllustrationCard(expandedClass ?: 1) }
                item {
                    val currentRank = learningRankFor(learningStars)
                    Surface(modifier = Modifier.fillMaxWidth().clickable { showRanks = !showRanks },
                        shape = RoundedCornerShape(19.dp), color = Color(0xFFF3EEFA),
                        border = BorderStroke(1.dp, Color(0xFFDCD0EC)), shadowElevation = 7.dp) {
                        Column(
                            Modifier.fillMaxWidth()
                                .background(Brush.verticalGradient(listOf(Color(0xFFFBF8FF), Color(0xFFF0E9F8))))
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(painterResource(currentRank.imageRes), currentRank.title,
                                    Modifier.size(52.dp), contentScale = ContentScale.Fit)
                                Column(Modifier.weight(1f).padding(start = 10.dp)) {
                                    Text("শিখলেই নতুন Hero Rank", color = WarmTextPrimary,
                                        style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                                    Text("${AloMathSolver.toBanglaDigits(learningStars)} ★ · ${currentRank.title}",
                                        color = PremiumPurple, style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold)
                                }
                                Text(if (showRanks) "▴" else "▾", color = PremiumPurple,
                                    style = MaterialTheme.typography.titleMedium)
                            }
                            if (showRanks) {
                                Spacer(Modifier.height(8.dp))
                                learningRanks.forEach { rank ->
                                val active = rank.title == currentRank.title
                                Surface(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                    shape = RoundedCornerShape(13.dp),
                                    color = if (active) Color(0xFFFFE5BE) else Color.White.copy(alpha = 0.78f),
                                    border = BorderStroke(1.dp, if (active) Color(0xFFD79A42) else Color(0xFFE2D8EC)),
                                    shadowElevation = if (active) 4.dp else 0.dp
                                ) {
                                    Row(Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically) {
                                        Image(painterResource(rank.imageRes), rank.title, Modifier.size(40.dp),
                                            contentScale = ContentScale.Fit)
                                        Text(rank.title, Modifier.weight(1f).padding(start = 9.dp),
                                            color = WarmTextPrimary, style = MaterialTheme.typography.labelLarge,
                                            fontWeight = if (active) FontWeight.ExtraBold else FontWeight.SemiBold)
                                        val range = rank.maxStars?.let {
                                            "${AloMathSolver.toBanglaDigits(rank.minStars)}–${AloMathSolver.toBanglaDigits(it)} ★"
                                        } ?: "${AloMathSolver.toBanglaDigits(rank.minStars)}+ ★"
                                        Text(range, color = if (active) PremiumPurple else WarmTextSecondary,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }
                            }
                        }
                    }
                }
                if (completedTopicIds.isNotEmpty()) item {
                    Text("✦ ${completedTopicIds.size}টি পাঠ শেষ · তুমি ${learningRankFor(learningStars).title}!",
                        color = PremiumPurple, style = MaterialTheme.typography.labelLarge)
                }
                item {
                    val selectorPalette = gradePalette(expandedClass ?: 1)
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable { showClassMenu = !showClassMenu },
                        shape = RoundedCornerShape(18.dp), color = selectorPalette.surface,
                        border = BorderStroke(1.dp, selectorPalette.accent.copy(alpha = 0.28f)),
                        shadowElevation = 8.dp
                    ) {
                        Row(Modifier.background(Brush.horizontalGradient(listOf(selectorPalette.surface, selectorPalette.highlight)))
                            .padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(32.dp).background(selectorPalette.accent.copy(alpha = 0.17f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.AutoStories, null, tint = selectorPalette.accent, modifier = Modifier.size(18.dp))
                            }
                            Text(expandedClass?.let { grade ->
                                BangladeshCurriculumRepository.featuredClassLevels.firstOrNull { it.levelNumber == grade }
                                    ?.classNameBangla?.substringBefore(" (")
                            } ?: "শ্রেণি বেছে নাও", Modifier.weight(1f).padding(start = 12.dp), color = WarmTextPrimary,
                                style = MaterialTheme.typography.titleSmall)
                            Text(if (showClassMenu) "▴" else "▾", color = selectorPalette.accent)
                        }
                    }
                }
                if (showClassMenu) {
                    for (grade in 1..5) {
                        val classLevel = BangladeshCurriculumRepository.featuredClassLevels.firstOrNull { it.levelNumber == grade }
                        val palette = gradePalette(grade)
                        item(key = "grade_$grade") {
                            Surface(modifier = Modifier.fillMaxWidth().clickable {
                                expandedClass = grade
                                showClassMenu = false
                                onClassSelected(grade)
                                onSubjectSelected(null)
                            }, shape = RoundedCornerShape(15.dp), color = palette.surface,
                                border = BorderStroke(1.dp, palette.accent.copy(alpha = 0.24f)),
                                shadowElevation = 5.dp) {
                                Row(
                                    Modifier.fillMaxWidth()
                                        .background(Brush.horizontalGradient(listOf(palette.surface, palette.highlight)))
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        Modifier.size(34.dp).background(palette.accent.copy(alpha = 0.16f), RoundedCornerShape(11.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$grade", color = palette.accent, fontWeight = FontWeight.ExtraBold)
                                    }
                                    Text(classLevel?.classNameBangla?.substringBefore(" (") ?: "শ্রেণি $grade",
                                        Modifier.weight(1f).padding(start = 12.dp),
                                        color = WarmTextPrimary, style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold)
                                    Text("→", color = palette.accent, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                val grade = expandedClass
                val classLevel = BangladeshCurriculumRepository.featuredClassLevels.firstOrNull { it.levelNumber == grade }
                if (grade != null && classLevel != null) {
                    for (item in classLevel.subjects) {
                            item(key = "subject_${item.id}_$grade") {
                                Surface(
                                    modifier = Modifier.fillMaxWidth()
                                        .clickable { onSubjectSelected(if (selectedSubjectId == item.id) null else item.id) },
                                    shape = RoundedCornerShape(15.dp), color = subjectCardWash(item),
                                    border = BorderStroke(1.dp, subjectAccent(item).copy(alpha = 0.22f)),
                                    shadowElevation = 2.dp
                                ) {
                                    Row(Modifier.background(Brush.verticalGradient(listOf(Color.White, subjectCardWash(item))))
                                        .padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Box(Modifier.size(40.dp).background(subjectAccent(item).copy(alpha = 0.14f), RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center) {
                                            Text(subjectMark(item), color = subjectAccent(item), fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(Modifier.size(12.dp))
                                        Text(item.nameBangla, Modifier.weight(1f), color = WarmTextPrimary,
                                            style = MaterialTheme.typography.titleSmall)
                                        Text("${item.topics.size}টি টপিক  ▾", color = subjectAccent(item),
                                            style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                            if (selectedSubjectId == item.id) {
                                val topics = item.topics
                                topicGroups(item).forEachIndexed { groupIndex, group ->
                                    item(key = "topics_${item.id}_${grade}_$groupIndex") {
                                        val groupWash = topicGroupWash(groupIndex)
                                        Surface(
                                            modifier = Modifier.fillMaxWidth().padding(start = 14.dp, bottom = 5.dp),
                                            shape = RoundedCornerShape(17.dp),
                                            color = groupWash,
                                            border = BorderStroke(1.dp, subjectAccent(item).copy(alpha = 0.18f)),
                                            shadowElevation = 5.dp
                                        ) {
                                            Column(Modifier.background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.92f), groupWash)))) {
                                                Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 11.dp),
                                                    verticalAlignment = Alignment.CenterVertically) {
                                                    Box(Modifier.size(28.dp).background(subjectAccent(item).copy(alpha = 0.13f), RoundedCornerShape(9.dp)),
                                                        contentAlignment = Alignment.Center) {
                                                        Text("${groupIndex + 1}", color = subjectAccent(item), style = MaterialTheme.typography.labelMedium)
                                                    }
                                                    Spacer(Modifier.size(10.dp))
                                                    Text(group.first, color = WarmTextPrimary, style = MaterialTheme.typography.titleSmall)
                                                }
                                                HorizontalDivider(color = subjectAccent(item).copy(alpha = 0.11f))
                                                group.second.forEachIndexed { index, topic ->
                                                Row(
                                                    Modifier.fillMaxWidth().clickable { onTopicClicked(topic) }
                                                        .padding(horizontal = 14.dp, vertical = 13.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text("${topics.indexOf(topic) + 1}.", color = subjectAccent(item),
                                                        style = MaterialTheme.typography.labelLarge)
                                                    Spacer(Modifier.size(12.dp))
                                                    Text(topic.titleBangla, Modifier.weight(1f),
                                                        color = WarmTextPrimary, style = MaterialTheme.typography.bodyMedium)
                                                    Icon(Icons.Filled.ArrowForward, null, tint = subjectAccent(item),
                                                        modifier = Modifier.size(16.dp))
                                                }
                                                if (index < group.second.lastIndex) {
                                                    HorizontalDivider(
                                                        modifier = Modifier.padding(start = 42.dp, end = 14.dp),
                                                        thickness = 1.dp,
                                                        color = subjectAccent(item).copy(alpha = 0.12f)
                                                    )
                                                }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (topics.isEmpty()) item { Text("এই বিষয়ে অধ্যায় যোগ করা হচ্ছে।", color = WarmTextSecondary) }
                            }
                    }
                }
                item { ReferenceListRow("অভিভাবক ও শিক্ষকের জায়গা", "বাড়ি ও ক্লাসে শেখানোর সহজ পরামর্শ", "স্টুডিও", onClick = onOpenStudio) }
                item { ReferenceListRow("আলো শিক্ষকের সঙ্গে কথা", "আগের আলোচনা, নতুন প্রশ্ন ও ফলো-আপ", "AI", onClick = onOpenGeneralChat) }
            }
        }
    }
}

@Composable
private fun visualExercisePainter(imageResId: Int, assetPath: String?): Painter {
    val context = LocalContext.current
    if (!assetPath.isNullOrBlank()) {
        val bitmap = remember(assetPath) {
            context.assets.open(assetPath).use(android.graphics.BitmapFactory::decodeStream).asImageBitmap()
        }
        return remember(bitmap) { BitmapPainter(bitmap) }
    }
    return painterResource(imageResId)
}

@Composable private fun GradeIllustrationCard(grade: Int) {
    val art = gradeArtRes(grade)
    val palette = gradePalette(grade)
    Surface(shape = RoundedCornerShape(21.dp), color = palette.surface,
        border = BorderStroke(1.dp, palette.accent.copy(alpha = 0.24f)), shadowElevation = 10.dp) {
        Row(Modifier.fillMaxWidth().height(148.dp)
            .background(Brush.horizontalGradient(listOf(palette.surface, palette.highlight)))
            .padding(start = 18.dp, end = 7.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(if (grade == 1) "আজকের শেখার অভিযান" else "শ্রেণি $grade · শেখার অভিযান", style = MaterialTheme.typography.labelLarge, color = PremiumPurple)
                Spacer(Modifier.height(4.dp))
                Text("মজার শেখার টপিক", style = MaterialTheme.typography.titleMedium, color = WarmTextPrimary)
                Text("বিষয় বেছে নিয়ে শুরু করো", style = MaterialTheme.typography.bodySmall, color = WarmTextSecondary)
            }
            Box(Modifier.size(112.dp), contentAlignment = Alignment.Center) {
                Image(painterResource(art), "শ্রেণি $grade-এর শেখার ইলাস্ট্রেশন",
                    modifier = Modifier.size(110.dp), contentScale = ContentScale.Fit)
            }
        }
    }
}

private data class GradePalette(val surface: Color, val highlight: Color, val accent: Color)

/** Five calm warm shades make grade switching obvious without turning the page into visual clutter. */
private fun gradePalette(grade: Int): GradePalette = when (grade) {
    1 -> GradePalette(Color(0xFFFFE6A8), Color(0xFFFFF8E6), Color(0xFFB66A08)) // honey
    2 -> GradePalette(Color(0xFFFFDDD0), Color(0xFFFFF5EF), Color(0xFFB6533C)) // peach
    3 -> GradePalette(Color(0xFFDCEFE1), Color(0xFFF5FBF5), Color(0xFF367A58)) // sage
    4 -> GradePalette(Color(0xFFD9ECE9), Color(0xFFF3FAF8), Color(0xFF287A72)) // soft teal
    else -> GradePalette(Color(0xFFE6E0F2), Color(0xFFFAF7FD), Color(0xFF735B96)) // lavender
}

private fun gradeArtRes(grade: Int): Int = when (grade) {
        1 -> R.drawable.grade_1_illustration
        2 -> R.drawable.grade_2_illustration
        3 -> R.drawable.grade_3_illustration
        4 -> R.drawable.grade_4_illustration
        else -> R.drawable.grade_5_illustration
    }

@Composable private fun ReferenceHeading(eyebrow: String, title: String, subtitle: String) {
    Column {
        Text(eyebrow, color = PremiumPurple, style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(5.dp))
        Text(title, color = WarmTextPrimary, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(3.dp))
        Text(subtitle, color = WarmTextSecondary, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable private fun ReferenceCard(tint: Color = Color.White, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = tint),
        border = BorderStroke(1.dp, WarmPaperBorder), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
        val lowerTint = if (tint == Color.White) Color(0xFFFFF9EE) else tint
        Column(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color.White, lowerTint)))
            .padding(18.dp), content = content)
    }
}

@Composable private fun ReferenceListRow(title: String, subtitle: String, symbol: String, onClick: () -> Unit) {
    val accent = when {
        title.contains("স্টুডিও") -> Color(0xFFB9684B)
        title.contains("শিক্ষকের সঙ্গে") -> Color(0xFF397D72)
        symbol == "খেলি" -> listOf(Color(0xFF397D72), Color(0xFFB9684B), Color(0xFF8A7540))[(title.hashCode() and Int.MAX_VALUE) % 3]
        else -> Color(0xFF397D72)
    }
    val wash = when {
        title.contains("স্টুডিও") -> Color(0xFFFFEDE5)
        title.contains("শিক্ষকের সঙ্গে") -> Color(0xFFE6F4EE)
        symbol == "খেলি" -> accent.copy(alpha = 0.11f)
        else -> Color(0xFFEAF4F1)
    }
    Surface(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(17.dp),
        color = wash, border = BorderStroke(1.dp, accent.copy(alpha = 0.20f)), shadowElevation = 5.dp) {
        Row(Modifier.background(Brush.verticalGradient(listOf(Color.White, wash)))
            .padding(14.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(42.dp).background(accent.copy(alpha = 0.16f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                if (title.contains("স্টুডিও")) {
                    Icon(Icons.Filled.Groups, null, tint = accent, modifier = Modifier.size(22.dp))
                } else {
                    Text(symbol, color = accent, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                }
            }
            Column(Modifier.weight(1f)) {
                Text(title, color = WarmTextPrimary, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, color = WarmTextSecondary, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            }
            Icon(Icons.Filled.ArrowForward, null, tint = accent, modifier = Modifier.size(18.dp))
        }
    }
}

private fun subjectMark(subject: CurriculumSubject): String = when {
    subject.nameBangla.contains("গণিত") -> "∑"
    subject.nameBangla.contains("বাংলা") -> "আ"
    subject.nameBangla.contains("ইংরেজ") -> "A"
    subject.nameBangla.contains("বিজ্ঞান") -> "বিঃ"
    else -> "বই"
}

private fun subjectAccent(subject: CurriculumSubject): Color = when {
    subject.nameBangla.contains("গণিত") -> Color(0xFF2B8075)
    subject.nameBangla.contains("বাংলা") -> Color(0xFFC56D51)
    subject.nameBangla.contains("English") || subject.nameBangla.contains("ইংরেজ") -> Color(0xFF966B21)
    subject.nameBangla.contains("বিজ্ঞান") -> Color(0xFF307F9B)
    subject.nameBangla.contains("ইসলাম") || subject.nameBangla.contains("নৈতিক") -> Color(0xFF9B6530)
    else -> Color(0xFFB3634F)
}

private fun subjectCardWash(subject: CurriculumSubject): Color = when {
    subject.nameBangla.contains("গণিত") -> Color(0xFFEAF5F1)
    subject.nameBangla.contains("বাংলা") -> Color(0xFFFFEEE7)
    subject.nameBangla.contains("English") || subject.nameBangla.contains("ইংরেজ") -> Color(0xFFFFF5DC)
    subject.nameBangla.contains("বিজ্ঞান") -> Color(0xFFEAF4F8)
    subject.nameBangla.contains("ইসলাম") || subject.nameBangla.contains("নৈতিক") -> Color(0xFFFFF2E4)
    else -> Color(0xFFF6F0E9)
}

private fun topicGroups(subject: CurriculumSubject): List<Pair<String, List<CurriculumTopic>>> {
    val topics = subject.topics
    if (topics.isEmpty()) return emptyList()
    return when (subject.id) {
        "c1_bangla" -> listOf(
            "শুনি, বলি ও আঁকি" to topics.slice(0..8),
            "স্বরবর্ণ ও কারচিহ্ন" to topics.slice(9..17),
            "ব্যঞ্জনবর্ণ ও শব্দ" to topics.slice(18..33),
            "পড়ি ও লিখি" to topics.slice(34..43),
            "গল্প, কবিতা ও দেশ" to topics.slice(44..53)
        )
        "c1_english" -> listOf(
            "Unit 1 · Greetings" to topics.slice(0..5),
            "Unit 2 · Alphabet and Numbers" to topics.slice(6..26),
            "Unit 3 · Classroom Instructions" to topics.slice(27..29),
            "Unit 4 · Questions and Answers" to topics.slice(30..35),
            "Unit 5 · Rhymes and Sounds" to topics.slice(36..38)
        )
        "c1_math" -> listOf(
            "গণনা ও সংখ্যার শুরু" to topics.slice(0..4),
            "১১–৪০ ও স্থানীয় মান" to topics.slice(5..10),
            "জ্যামিতি ও প্যাটার্ন" to topics.slice(11..12),
            "৪১–১০০, হিসাব ও মুদ্রা" to topics.slice(13..17)
        )
        "c2_bangla" -> listOf(
            "পরিচয়, বিদ্যালয় ও পরিবার" to topics.slice(0..3),
            "বর্ণ, কার ও যুক্তবর্ণ" to topics.slice(4..12),
            "পড়ি, বুঝি ও বাক্য লিখি" to topics.slice(13..17),
            "কবিতা, প্রকৃতি ও উৎসব" to topics.slice(18..23),
            "দেশ, মানুষ ও ভাষার খেলা" to topics.slice(24..28)
        )
        "c2_english" -> listOf(
            "Units 1–2 · Talk, Words and Numbers" to topics.slice(0..1),
            "Units 3–5 · Instructions, Questions and Days" to topics.slice(2..4),
            "Units 6–7 · Sounds, Colours and Shapes" to topics.slice(5..6),
            "Units 8–10 · Family, Animals and Stories" to topics.slice(7..9)
        )
        "c2_math" -> listOf(
            "সংখ্যা ও হিসাব" to topics.slice(0..2),
            "আকৃতি ও পরিমাপ" to topics.slice(3..4),
            "মুদ্রা ও উপাত্ত" to topics.slice(5..6)
        )
        else -> topics.chunked(6).mapIndexed { index, group -> "শেখার অংশ ${index + 1}" to group }
    }
}

private fun topicGroupWash(index: Int): Color = when (index % 5) {
    0 -> Color(0xFFFFF3D4)
    1 -> Color(0xFFE7F5EE)
    2 -> Color(0xFFFFEAE2)
    3 -> Color(0xFFE8F3F8)
    else -> Color(0xFFF3ECF8)
}
