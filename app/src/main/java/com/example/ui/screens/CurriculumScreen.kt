package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import com.example.ui.screens.DigitText as Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CurriculumClassLevel
import com.example.data.model.CurriculumPedagogyEngine
import com.example.data.model.CurriculumStage
import com.example.data.model.CurriculumSubject
import com.example.data.model.CurriculumTopic
import com.example.data.model.TopicQA
import com.example.ui.theme.AaloYellow
import com.example.ui.theme.AaloYellowContainer
import com.example.ui.theme.AaloYellowDark
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.OnAaloYellowContainer

/**
 * 3-Level Hierarchical Primary Curriculum Learning System (১ম থেকে ৫ম শ্রেণি):
 *
 * 1. LEVEL 1: শ্রেণি নির্বাচন ➔ সেই শ্রেণির সকল বিষয়ের তালিকা (Subject Cards)
 * 2. LEVEL 2: বিষয় নির্বাচন ➔ সেই বিষয়ের সকল অধ্যায়/চ্যাপ্টার তালিকা (Chapter List with Search)
 * 3. LEVEL 3: অধ্যায়/চ্যাপ্টার নির্বাচন ➔ সম্পূর্ণ সামগ্রিক পড়াশোনা গাইড (Full Learning Page):
 *    - অধ্যায় পরিচিতি ও সারসংক্ষেপ
 *    - এই অধ্যায়ে কী কী পড়াশোনা আছে (Learning Scope)
 *    - সহজে শেখার পদ্ধতি ও কৌশল (Pedagogical Learning Methodology)
 *    - প্রধান সূত্র ও নিয়মাবলি (Core Formulas & Rules)
 *    - বিস্তারিত পাঠ্যপুস্তক আলোচনা (Detailed Concepts)
 *    - প্রশ্নোত্তর ও মডেল সমাধান (Chapter Q&A & Answers)
 *    - গুরুত্বপূর্ণ তথ্য ও আলো এআই শিক্ষক সংযোগ
 */
@Composable
fun CurriculumScreen(
    selectedStage: CurriculumStage,
    selectedClassNumber: Int,
    selectedSubjectId: String?,
    searchQuery: String,
    currentClassLevel: CurriculumClassLevel?,
    selectedTopicDetail: CurriculumTopic?,
    onStageSelected: (CurriculumStage) -> Unit,
    onClassSelected: (Int) -> Unit,
    onSubjectSelected: (String?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onTopicClicked: (CurriculumTopic) -> Unit,
    onCloseTopicDetail: () -> Unit,
    onAskInChat: (CurriculumTopic) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeSubject = currentClassLevel?.subjects?.find { it.id == selectedSubjectId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("curriculum_screen")
    ) {
        // Top Class Selector Bar (১ম থেকে ৫ম শ্রেণি) - Always visible for instant class switching
        PrimaryClassSelectorTopBar(
            selectedClassNumber = selectedClassNumber,
            onClassSelected = { cNum ->
                onClassSelected(cNum)
                onSubjectSelected(null)
                onCloseTopicDetail()
            }
        )

        // Breadcrumb Navigation & Back Navigation Bar
        if (selectedTopicDetail != null) {
            // Level 3 Header: Back to Chapters List
            BreadcrumbNavigationHeader(
                levelText = "${currentClassLevel?.classNameBangla ?: ""} › ${activeSubject?.nameBangla ?: ""} › অধ্যায় পাঠ",
                backButtonText = "অধ্যায় তালিকায় ফিরুন",
                onBack = onCloseTopicDetail
            )
        } else if (selectedSubjectId != null && activeSubject != null) {
            // Level 2 Header: Back to Subjects Grid
            BreadcrumbNavigationHeader(
                levelText = "${currentClassLevel?.classNameBangla ?: ""} › ${activeSubject.nameBangla}",
                backButtonText = "সকল বিষয়ে ফিরুন",
                onBack = { onSubjectSelected(null) }
            )
        }

        // Screen Body: Switch dynamically between Level 1, Level 2, and Level 3
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                // LEVEL 3: In-depth Chapter Learning Screen
                selectedTopicDetail != null -> {
                    ChapterFullLearningScreen(
                        topic = selectedTopicDetail,
                        subject = activeSubject,
                        classNumber = selectedClassNumber,
                        onBack = onCloseTopicDetail,
                        onAskInChat = { onAskInChat(selectedTopicDetail) }
                    )
                }

                // LEVEL 2: Subject Chapters / Topics List
                selectedSubjectId != null && activeSubject != null -> {
                    SubjectChaptersListScreen(
                        subject = activeSubject,
                        classNumber = selectedClassNumber,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = onSearchQueryChanged,
                        onTopicSelected = onTopicClicked,
                        onAskInChat = onAskInChat
                    )
                }

                // LEVEL 1: Subjects Selection Grid for Selected Class
                else -> {
                    ClassSubjectsGridScreen(
                        classLevel = currentClassLevel,
                        selectedClassNumber = selectedClassNumber,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = onSearchQueryChanged,
                        onSubjectSelected = onSubjectSelected,
                        onTopicDirectSelected = { topic, subId ->
                            onSubjectSelected(subId)
                            onTopicClicked(topic)
                        },
                        onAskInChat = onAskInChat
                    )
                }
            }
        }
    }
}

/**
 * Top Class Selector Bar (Class 1 to Class 5).
 */
@Composable
private fun PrimaryClassSelectorTopBar(
    selectedClassNumber: Int,
    onClassSelected: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedClassTitle = when (selectedClassNumber) {
        1 -> "🐣 প্রথম শ্রেণি"
        2 -> "🐰 দ্বিতীয় শ্রেণি"
        3 -> "🦊 তৃতীয় শ্রেণি"
        4 -> "🦁 চতুর্থ শ্রেণি"
        5 -> "🚀 পঞ্চম শ্রেণি"
        else -> "$selectedClassNumber শ্রেণি"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { isExpanded = true }
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp).size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("পড়ার ক্লাস", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(selectedClassTitle, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Icon(Icons.Default.ExpandMore, "শ্রেণি বদলাও", tint = MaterialTheme.colorScheme.primary)
            }

            DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                (1..5).forEach { cNum ->
                    val title = when (cNum) {
                        1 -> "🐣 প্রথম শ্রেণি"
                        2 -> "🐰 দ্বিতীয় শ্রেণি"
                        3 -> "🦊 তৃতীয় শ্রেণি"
                        4 -> "🦁 চতুর্থ শ্রেণি"
                        else -> "🚀 পঞ্চম শ্রেণি"
                    }
                    DropdownMenuItem(
                        text = { Text(title, fontWeight = if (cNum == selectedClassNumber) FontWeight.Bold else FontWeight.Medium) },
                        trailingIcon = {
                            if (cNum == selectedClassNumber) Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                        },
                        onClick = { isExpanded = false; onClassSelected(cNum) },
                        modifier = Modifier.testTag("class_dropdown_$cNum")
                    )
                }
            }
        }
    }
}

/**
 * Breadcrumb Back Navigation Header.
 */
@Composable
private fun BreadcrumbNavigationHeader(
    levelText: String,
    backButtonText: String,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onBack)
                    .padding(vertical = 4.dp, horizontal = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = backButtonText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = levelText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

// =============================================================================
// LEVEL 1: CLASS SUBJECTS SELECTION SCREEN
// =============================================================================

@Composable
private fun ClassSubjectsGridScreen(
    classLevel: CurriculumClassLevel?,
    selectedClassNumber: Int,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSubjectSelected: (String) -> Unit,
    onTopicDirectSelected: (CurriculumTopic, String) -> Unit,
    onAskInChat: (CurriculumTopic) -> Unit
) {
    val subjects = classLevel?.subjects ?: emptyList()
    val isSearching = searchQuery.trim().isNotEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("class_subjects_grid"),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome / Class Mascot Banner
        item {
            val (mascotEmoji, classMotto) = when (selectedClassNumber) {
                1 -> "🐣" to "ছবি ও মজার ছড়ায় শেখা শুরু!"
                2 -> "🐰" to "সহজ যোগ-বিয়োগ ও নীতিশিক্ষার গল্প!"
                3 -> "🦊" to "নামতা, গুণের জাদু ও প্রাথমিক বিজ্ঞান!"
                4 -> "🦁" to "ভাগ, ভগ্নাংশ ও প্রকৃতির রহস্য আবিষ্কার!"
                5 -> "🚀" to "সমাপনী সুপারস্টার ও আত্মবিশ্বাসী প্রস্তুতি!"
                else -> "⭐" to "জাতীয় শিক্ষাক্রমের অধ্যায়সূচি ও নির্বাচিত পাঠ"
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE3A0)),
                border = BorderStroke(1.dp, Color(0xFFE8D0A0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("তোমার শেখার জায়গা", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2B8075))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = classLevel?.classNameBangla ?: "${selectedClassNumber}ম শ্রেণি", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF222A2D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = classMotto, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF5F605A), lineHeight = 18.sp)
                        }
                        Image(
                            painter = painterResource(when (selectedClassNumber) {
                                1 -> R.drawable.grade_1_illustration
                                2 -> R.drawable.grade_2_illustration
                                3 -> R.drawable.grade_3_illustration
                                4 -> R.drawable.grade_4_illustration
                                else -> R.drawable.grade_5_illustration
                            }),
                            contentDescription = "পড়ার সঙ্গী",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(112.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF8E8),
                            border = BorderStroke(1.dp, Color(0xFFE8D0A0))
                        ) {
                            Text(
                                text = "ক্লাস $selectedClassNumber",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2B8075),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Parent Support & Offline Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFF7E6),
                        border = BorderStroke(0.8.dp, Color(0xFFE8D0A0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SupervisorAccount,
                                contentDescription = null,
                                tint = Color(0xFF2B8075),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "অফলাইন-প্রথম • পরিবার ও শিক্ষক সহায়তাসহ শেখা",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF5F605A)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "একটি বিষয় বেছে নিন, অথবা অধ্যায় ও সূত্র খুঁজুন।",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Color(0xFF5F605A)
                    )
                }
            }
        }

        // Search Bar across all subjects
        item {
            CurriculumSearchBar(
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged,
                placeholderText = "${classLevel?.classNameBangla ?: "এই শ্রেণির"} যেকোনো বিষয়, অধ্যায় বা সূত্র খুঁজুন..."
            )
        }

        // If searching: show filtered topic matches directly
        if (isSearching) {
            val matchingTopics = mutableListOf<Triple<CurriculumSubject, CurriculumTopic, String>>()
            val cleanQuery = searchQuery.trim().lowercase()
            for (sub in subjects) {
                for (top in sub.topics) {
                    if (top.titleBangla.lowercase().contains(cleanQuery) ||
                        top.summaryBangla.lowercase().contains(cleanQuery) ||
                        top.detailsBangla.lowercase().contains(cleanQuery) ||
                        (top.formulaOrRule?.lowercase()?.contains(cleanQuery) == true) ||
                        top.keyPoints.any { it.lowercase().contains(cleanQuery) }
                    ) {
                        matchingTopics.add(Triple(sub, top, sub.id))
                    }
                }
            }

            item {
                Text(
                    text = "${matchingTopics.size}টি অধ্যায় পাওয়া গেছে",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (matchingTopics.isEmpty()) {
                item {
                    EmptySearchPlaceholder()
                }
            } else {
                items(matchingTopics, key = { it.second.id }) { (subject, topic, subId) ->
                    CurriculumTopicCard(
                        subject = subject,
                        topic = topic,
                        onClick = { onTopicDirectSelected(topic, subId) },
                        onAskChat = { onAskInChat(topic) }
                    )
                }
            }
        } else {
            // Normal Level 1: List of all Subjects of this Class
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "পাঠ্য বিষয়সমূহ (${subjects.size}টি)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "বিষয় বেছে নিয়ে অধ্যায় পড়ুন",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(subjects, key = { it.id }) { subject ->
                SubjectCard(
                    subject = subject,
                    onClick = { onSubjectSelected(subject.id) }
                )
            }
        }
    }
}

/**
 * Subject Card in Level 1 Grid/List.
 */
@Composable
private fun SubjectCard(
    subject: CurriculumSubject,
    onClick: () -> Unit
) {
    val accent = when (subject.id.lowercase()) {
        "bangla" -> Color(0xFFC56D51)
        "english" -> Color(0xFF966B21)
        "math", "mathematics" -> Color(0xFF2B8075)
        "science" -> Color(0xFF307F9B)
        else -> Color(0xFF2B8075)
    }
    val accentSurface = accent.copy(alpha = 0.11f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("subject_card_${subject.id}"),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Subject Icon container
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = accentSurface,
                modifier = Modifier.size(52.dp),
                border = BorderStroke(1.dp, accent.copy(alpha = 0.24f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = getSubjectIcon(subject.iconName),
                        contentDescription = subject.nameBangla,
                        tint = accent,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Subject Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.nameBangla,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${subject.topics.size}টি অধ্যায় • পাঠ, অনুশীলন ও প্রশ্ন",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = accent
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Sample topics snippet
                val sampleTitles = subject.topics.take(2).joinToString(", ") { it.titleBangla }
                Text(
                    text = "অধ্যায়: $sampleTitles...",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Arrow button
            Surface(
                shape = CircleShape,
                color = accentSurface,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Chapters",
                        tint = accent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// =============================================================================
// LEVEL 2: SUBJECT CHAPTERS LIST SCREEN
// =============================================================================

@Composable
private fun SubjectChaptersListScreen(
    subject: CurriculumSubject,
    classNumber: Int,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onTopicSelected: (CurriculumTopic) -> Unit,
    onAskInChat: (CurriculumTopic) -> Unit
) {
    val cleanQuery = searchQuery.trim().lowercase()
    val filteredTopics = subject.topics.filter { topic ->
        cleanQuery.isEmpty() ||
            topic.titleBangla.lowercase().contains(cleanQuery) ||
            topic.summaryBangla.lowercase().contains(cleanQuery) ||
            topic.detailsBangla.lowercase().contains(cleanQuery) ||
            (topic.formulaOrRule?.lowercase()?.contains(cleanQuery) == true) ||
            topic.keyPoints.any { it.lowercase().contains(cleanQuery) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("subject_chapters_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Subject Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AaloYellowContainer.copy(alpha = 0.65f)),
                border = BorderStroke(1.dp, AaloYellow.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AaloYellow,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = getSubjectIcon(subject.iconName),
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = subject.nameBangla,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnAaloYellowContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$classNumber শ্রেণি • মোট ${subject.topics.size}টি অধ্যায় অন্তর্ভুক্ত",
                            fontSize = 12.sp,
                            color = OnAaloYellowContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Search within this subject
        item {
            CurriculumSearchBar(
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged,
                placeholderText = "${subject.nameBangla} এর অধ্যায় বা সূত্র খুঁজুন..."
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "অধ্যায় তালিকা (${filteredTopics.size}টি অধ্যায়)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "অধ্যায়ে ট্যাপ করে বিস্তারিত পড়ুন",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (filteredTopics.isEmpty()) {
            item {
                EmptySearchPlaceholder()
            }
        } else {
            itemsIndexed(filteredTopics, key = { _, topic -> topic.id }) { index, topic ->
                SubjectChapterItemCard(
                    chapterIndex = index + 1,
                    subject = subject,
                    topic = topic,
                    onClick = { onTopicSelected(topic) },
                    onAskChat = { onAskInChat(topic) }
                )
            }
        }
    }
}

/**
 * Chapter Card in Level 2 Chapter List.
 */
@Composable
private fun SubjectChapterItemCard(
    chapterIndex: Int,
    subject: CurriculumSubject,
    topic: CurriculumTopic,
    onClick: () -> Unit,
    onAskChat: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("chapter_card_${topic.id}"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AaloYellow.copy(alpha = 0.28f)),
        colors = CardDefaults.cardColors(
            containerColor = when (chapterIndex % 4) {
                1 -> Color(0xFFFFFBF3)
                2 -> Color(0xFFF5FBF8)
                3 -> Color(0xFFFFF8EE)
                else -> Color(0xFFF6FAFC)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Chapter Header Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AaloYellowContainer,
                    border = BorderStroke(1.dp, AaloYellowDark.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "অধ্যায় $chapterIndex",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnAaloYellowContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Quick Ask Alo AI
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onAskChat)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = AaloYellowDark,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "এআই শিক্ষক",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chapter Title
            Text(
                text = topic.titleBangla,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Summary
            Text(
                text = topic.summaryBangla,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Formula or Rule Tag if available
            topic.formulaOrRule?.let { formula ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AaloYellowContainer.copy(alpha = 0.35f),
                    border = BorderStroke(0.8.dp, AaloYellow.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Calculate,
                            contentDescription = null,
                            tint = AaloYellowDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formula,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            color = OnAaloYellowContainer,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action: Full Chapter Learning Guide
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "পড়াশোনা, নিয়ম ও প্রশ্নোত্তর",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = EmeraldPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "শেখা শুরু করো",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AaloYellowDark
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = AaloYellowDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

// =============================================================================
// LEVEL 3: COMPREHENSIVE CHAPTER LEARNING SCREEN
// (কি কি বিষয় আছে, কি কি পড়াশোনা আছে, শেখার পদ্ধতি, প্রশ্নোত্তর ও উত্তর)
// =============================================================================

@Composable
private fun ChapterFullLearningScreen(
    topic: CurriculumTopic,
    subject: CurriculumSubject?,
    classNumber: Int,
    onBack: () -> Unit,
    onAskInChat: () -> Unit
) {
    // Key the state to the chapter. Without this, Compose can reuse the class-list
    // scroll offset and open a newly selected lesson halfway down the page.
    val chapterScrollState = remember(topic.id) { ScrollState(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(chapterScrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("chapter_full_learning_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Chapter Title & Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = AaloYellowContainer.copy(alpha = 0.8f)),
            border = BorderStroke(1.2.dp, AaloYellowDark.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AaloYellow,
                        border = BorderStroke(1.dp, AaloYellowDark)
                    ) {
                        Text(
                            text = "${classNumber}ম শ্রেণি • ${subject?.nameBangla ?: "পাঠ্য বিষয়"}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Icon(
                        imageVector = getSubjectIcon(subject?.iconName ?: "book"),
                        contentDescription = null,
                        tint = AaloYellowDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = topic.titleBangla,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnAaloYellowContainer,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = topic.summaryBangla,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = OnAaloYellowContainer.copy(alpha = 0.9f)
                )
            }
        }

        // 2. SECTION: এই অধ্যায়ে কী কী পড়াশোনা আছে (Learning Scope & Objectives)
        LearningSectionCard(
            icon = Icons.Filled.MenuBook,
            iconTint = EmeraldPrimary,
            title = "🎯 এই অধ্যায়ে কী কী পড়াশোনা ও বিষয়বস্তু আছে:"
        ) {
            val objectives = if (topic.learningObjectives.isNotEmpty()) {
                topic.learningObjectives
            } else {
                topic.keyPoints
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                objectives.forEachIndexed { idx, obj ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = EmeraldPrimary.copy(alpha = 0.15f),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = obj,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // 3. SECTION: 👨‍🏫 শিক্ষক ও অভিভাবক নির্দেশিকা (Teacher & Parent Pedagogical Hub)
        ParentTeachingGuideSection(
            topic = topic,
            subject = subject,
            classNumber = classNumber,
            onAskInChat = onAskInChat
        )

        // 4. SECTION: পড়াশোনা শেখার পদ্ধতি ও সহজ কৌশল (Pedagogical Learning Methodology)
        LearningSectionCard(
            icon = Icons.Filled.Lightbulb,
            iconTint = AmberAccent,
            title = "💡 এই অধ্যায় সহজে শেখার পদ্ধতি ও মনে রাখার কৌশল:"
        ) {
            val methodologyText = topic.learningMethodology ?: getSubjectDefaultMethodology(
                subjectName = subject?.nameBangla ?: "",
                topicTitle = topic.titleBangla
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AmberAccent.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, AmberAccent.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = methodologyText,
                        fontSize = 13.sp,
                        lineHeight = 21.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Step-by-step checklist tips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircleOutline,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "টিপস: প্রতিদিন অন্তত ২টি সমস্যা নিজে খাতায় লিখে সমাধান অনুশীলন করো।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 5. SECTION: প্রধান সূত্র, নিয়ম ও ব্যাকরণিক কাঠামো (Core Formula / Rule)
        topic.formulaOrRule?.let { formula ->
            LearningSectionCard(
                icon = Icons.Filled.Calculate,
                iconTint = AaloYellowDark,
                title = "📌 প্রধান সূত্র, নিয়ম ও মূলনীতি:"
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AaloYellowContainer.copy(alpha = 0.4f),
                    border = BorderStroke(1.dp, AaloYellow.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = formula,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = OnAaloYellowContainer,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }

        // 6. SECTION: বিস্তারিত পাঠ্যপুস্তক আলোচনা ও তথ্য (Comprehensive Theory & Details)
        LearningSectionCard(
            icon = Icons.Filled.Book,
            iconTint = MaterialTheme.colorScheme.primary,
            title = "📖 বিস্তারিত পাঠ ও বিষয়বস্তু আলোচনা:"
        ) {
            Text(
                text = topic.detailsBangla,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // 7. SECTION: অধ্যায়ের গুরুত্বপূর্ণ প্রশ্নোত্তর ও পূর্ণাঙ্গ সমাধান (Q&A and Answers)
        LearningSectionCard(
            icon = Icons.Filled.Quiz,
            iconTint = EmeraldPrimary,
            title = "📝 গুরুত্বপূর্ণ প্রশ্নোত্তর ও সমাধান:"
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // If the topic has worked example, show it as structured Q&A
                topic.exampleBangla?.let { example ->
                    QuestionAnswerItem(
                        question = "অনুশীলনী প্রশ্ন ও বাস্তব উদাহরণ:",
                        answer = example
                    )
                }

                // If explicit QA list exists
                topic.qaList.forEach { qa ->
                    QuestionAnswerItem(
                        question = qa.question,
                        answer = qa.answer
                    )
                }

                // If no custom QA list, provide intelligent subject-based interactive review question
                if (topic.qaList.isEmpty() && topic.exampleBangla == null) {
                    QuestionAnswerItem(
                        question = "প্রশ্ন: '${topic.titleBangla}' অধ্যায়ের মূল সারমর্ম কী?",
                        answer = "উত্তর: ${topic.summaryBangla} বিস্তারিত জানার জন্য উপরে পাঠ্যপুস্তক আলোচনা ও সূত্রের নিয়মগুলো মনোযোগ দিয়ে পড়তে হবে।"
                    )
                }
            }
        }

        // 8. SECTION: গুরুত্বপূর্ণ তথ্য ও দ্রুত রিভিশন (Key Takeaway Points)
        if (topic.keyPoints.isNotEmpty()) {
            LearningSectionCard(
                icon = Icons.Filled.CheckCircle,
                iconTint = EmeraldPrimary,
                title = "⭐ মনে রাখার মতো গুরুত্বপূর্ণ তথ্য (Quick Revision):"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    topic.keyPoints.forEach { point ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = AaloYellowDark,
                                modifier = Modifier
                                    .size(15.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = point,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // 9. SECTION: Interactive AI Tutor Action Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AaloYellow),
            border = BorderStroke(1.2.dp, AaloYellowDark)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "এই অধ্যায় নিয়ে আলো এআই-কে প্রশ্ন করো",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "কোনো অঙ্ক বা নিয়ম বুঝতে অসুবিধা হলে কৃত্রিম বুদ্ধিমত্তা শিক্ষকের সাহায্য নাও",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onAskInChat,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("ask_ai_topic_btn"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "আলো শিক্ষককে প্রশ্ন করো",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// =============================================================================
// PARENT ASSISTANCE HUB (অভিভাবক কর্নার ও পাঠদান সহায়ক)
// =============================================================================

@Composable
private fun ParentTeachingGuideSection(
    topic: CurriculumTopic,
    subject: CurriculumSubject?,
    classNumber: Int,
    onAskInChat: () -> Unit
) {
    val pedagogy = remember(classNumber, subject?.nameBangla, topic.id) {
        CurriculumPedagogyEngine.getPedagogyForTopic(
            classLevelNumber = classNumber,
            subjectName = subject?.nameBangla ?: "",
            topic = topic
        )
    }

    val parentTips = if (topic.parentTeachingTips.isNotEmpty()) {
        topic.parentTeachingTips
    } else {
        pedagogy.teachingTips
    }

    val commonMistakes = if (topic.commonMistakes.isNotEmpty()) {
        topic.commonMistakes
    } else {
        pedagogy.commonMistakes
    }

    val homeActivity = topic.homeActivity ?: pedagogy.homeActivity
    val oralQuestions = if (topic.qaList.isNotEmpty()) {
        topic.qaList.take(3)
    } else {
        pedagogy.oralQuestions
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("parent_teaching_hub_section"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, AaloYellowDark.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = AaloYellowContainer,
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.SupervisorAccount,
                            contentDescription = null,
                            tint = OnAaloYellowContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "👨‍🏫 শিক্ষক ও অভিভাবক নির্দেশিকা",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "অধ্যায়ভিত্তিক আধুনিক পাঠদান কৌশল, সাধারণ ভুল ও তাৎক্ষণিক প্রতিকার",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(14.dp))

            // 1. Parent Teaching Strategy
            Text(
                text = "💡 শিক্ষক ও অভিভাবকের জন্য পাঠদান কৌশল ও ধাপ:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                parentTips.forEachIndexed { idx, tip ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${idx + 1}.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberAccent,
                            modifier = Modifier.width(18.dp)
                        )
                        Text(
                            text = tip,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Common Mistakes & Fixes
            if (commonMistakes.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "⚠️ শিক্ষার্থীরা এই অধ্যায়ে যেসব ভুল সচরাচর করে (সতর্কবার্তা):",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        commonMistakes.forEach { mistake ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "•",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.width(14.dp)
                                )
                                Text(
                                    text = mistake,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // 3. Parent Oral Pop-Quiz Sheet
            Text(
                text = "🗣️ মুখে মুখে ছোট কুইজ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
            Text(
                text = "বই বন্ধ করে নিচের প্রশ্নগুলো করো। উত্তর দেখতে ‘উত্তর’ বোতামে চাপ দাও।",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                oralQuestions.forEachIndexed { qIdx, qa ->
                    ParentOralQuizItem(
                        questionIndex = qIdx + 1,
                        question = qa.question,
                        answer = qa.answer
                    )
                }
            }

            // 4. Hands-on Home Activity
            if (!homeActivity.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldPrimary.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🏡 বাড়ি ও শ্রেণিকক্ষের ব্যবহারিক কাজ (Hands-on Activity):",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = homeActivity,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Worksheet trigger button
            OutlinedButton(
                onClick = onAskInChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, AaloYellowDark)
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = AaloYellowDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "এআই শিক্ষক থেকে এই অধ্যায়ের স্পেশাল ওয়ার্কশিট নিন",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AaloYellowDark
                )
            }
        }
    }
}

/**
 * Parent Oral Quiz Item with Interactive Reveal/Hide Answer.
 */
@Composable
private fun ParentOralQuizItem(
    questionIndex: Int,
    question: String,
    answer: String
) {
    var isAnswerRevealed by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "প্র-$questionIndex:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = question,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isAnswerRevealed) MaterialTheme.colorScheme.surfaceVariant else AaloYellowContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { isAnswerRevealed = !isAnswerRevealed }
                        .padding(start = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isAnswerRevealed) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = if (isAnswerRevealed) MaterialTheme.colorScheme.onSurfaceVariant else OnAaloYellowContainer,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isAnswerRevealed) "লুকান" else "উত্তর",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAnswerRevealed) MaterialTheme.colorScheme.onSurfaceVariant else OnAaloYellowContainer
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isAnswerRevealed) {
                Column {
                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "উত্তর:",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = answer,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

/**
 * Reusable Section Card for Learning Screen.
 */
@Composable
private fun LearningSectionCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

/**
 * Question and Answer Item Card with clean formatting.
 */
@Composable
private fun QuestionAnswerItem(
    question: String,
    answer: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "❓",
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = question,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "✅",
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = answer,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// =============================================================================
// HELPER COMPONENTS & UTILS
// =============================================================================

@Composable
private fun CurriculumSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    placeholderText: String
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("curriculum_search_bar"),
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = AaloYellowDark,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AaloYellowDark,
            unfocusedBorderColor = AaloYellow.copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun CurriculumTopicCard(
    subject: CurriculumSubject,
    topic: CurriculumTopic,
    onClick: () -> Unit,
    onAskChat: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("curriculum_topic_card_${topic.id}"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AaloYellow.copy(alpha = 0.35f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AaloYellowContainer,
                    border = BorderStroke(1.dp, AaloYellowDark.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = getSubjectIcon(subject.iconName),
                            contentDescription = null,
                            tint = AaloYellowDark,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = subject.nameBangla,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnAaloYellowContainer
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = AaloYellowContainer.copy(alpha = 0.55f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onAskChat)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = AaloYellowDark,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "এআই শিক্ষক",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnAaloYellowContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = topic.titleBangla,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = topic.summaryBangla,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "সম্পূর্ণ অধ্যায় ও সমাধান পড়ুন",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AaloYellowDark
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = AaloYellowDark,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptySearchPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "কোনো ফলাফল পাওয়া যায়নি",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "বানান ঠিক করে বা অন্য শব্দ লিখে আবার খোঁজো",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
            )
        }
    }
}

private fun getSubjectDefaultMethodology(subjectName: String, topicTitle: String): String {
    return when {
        subjectName.contains("গণিত") -> "১. প্রশ্নটি মনোযোগ দিয়ে ২ বার পড়ে কি কি তথ্য দেওয়া আছে তা আলাদা করো।\n২. প্রয়োজনীয় সূত্র বা চার প্রক্রিয়ার নিয়ম খাতায় লিখে নাও।\n৩. ধাপে ধাপে হিসাব সম্পন্ন করো এবং শেষে উত্তরের একক (টাকা/মিটার/টি) মিলিয়ে নাও।"
        subjectName.contains("বিজ্ঞান") -> "১. বৈজ্ঞানিক ধারণাটি চারপাশের বাস্তব প্রকৃতির সাথে মিলিয়ে বোঝার চেষ্টা করো।\n২. প্রধান কারণ ও ফলাফল চিহ্নিত করো।\n৩. চিত্র বা ডায়াগ্রামের মাধ্যমে প্রক্রিয়াটি মনে রাখার অভ্যাস করো।"
        subjectName.contains("বাংলা") -> "১. পাঠের প্রতিটি বাক্য স্পষ্ট উচ্চারণে রিডিং পড়ো।\n২. নতুন ও কঠিন শব্দগুলোর অর্থ ও যুক্তবর্ণ ভেঙে লিখে অনুশীলন করো।\n৩. মূল ভাব নিজের ভাষায় ২টি বাক্যে প্রকাশ করার চেষ্টা করো।"
        subjectName.contains("English") -> "1. Read the keywords aloud with correct pronunciation.\n2. Note the grammatical sentence pattern.\n3. Make 2 new practice sentences of your own using the vocabulary."
        subjectName.contains("সমাজ") || subjectName.contains("পরিচয়") -> "১. ঐতিহাসিক ঘটনা বা সামাজিক নিয়মের মূল কারণ ও ফলাফল মনে রাখো।\n২. মানচিত্র বা গুরুত্বপূর্ণ সন ও স্থান চিহ্নিত করে পড়ো।"
        subjectName.contains("ধর্ম") || subjectName.contains("নৈতিক") -> "১. পাঠটি আমাদের কী শেখায়, নিজের ভাষায় বলো।\n২. শেখা কথাটি প্রতিদিনের ভালো কাজে ব্যবহার করো।"
        else -> "১. মূল বিষয়বস্তু মনোযোগ সহকারে পাঠ করো।\n২. গুরুত্বপূর্ণ অংশগুলো দাগিয়ে রিভিশন দাও।"
    }
}

private fun getSubjectIcon(iconName: String): ImageVector {
    return when (iconName) {
        "math", "higher_math" -> Icons.Filled.Calculate
        "science", "chemistry" -> Icons.Filled.Science
        "physics" -> Icons.Filled.Psychology
        "biology" -> Icons.Filled.Biotech
        "bangla" -> Icons.Filled.Book
        "english" -> Icons.Filled.Language
        "social", "history" -> Icons.Filled.Public
        "ict" -> Icons.Filled.Computer
        "commerce", "finance", "accounting" -> Icons.Filled.School
        "scholarship" -> Icons.Filled.School
        "health" -> Icons.Filled.Favorite
        "religion", "moral" -> Icons.Filled.MenuBook
        else -> Icons.Filled.MenuBook
    }
}

private fun getParentTeachingTips(subjectName: String, topic: CurriculumTopic): List<String> {
    return when {
        subjectName.contains("গণিত") -> listOf(
            "বাস্তব বস্তু (যেমন: চকলেট, মার্বেল, মুদ্রা বা পেনসিল) সামনে রেখে উদাহরণ দিয়ে সমস্যাটি বোঝান।",
            "সন্তানকে সরাসরি উত্তর না বলে নিজে হিসাব করার সুযোগ দিন এবং ধাপে ধাপে প্রশ্ন করে উৎসাহিত করুন।",
            "প্রতিদিন অন্তত ১৫ মিনিট খাতায় অঙ্ক লিখে নিজে নিজে সমাধান করার অভ্যাস তৈরি করুন।"
        )
        subjectName.contains("বিজ্ঞান") -> listOf(
            "পাঠ্যবইয়ের বৈজ্ঞানিক বিষয়টিকে ঘরের চারপাশের পরিবেশ ও প্রকৃতির সাথে মিলিয়ে দেখান।",
            "সন্তানের মনে 'কেন এমন হয়?' প্রশ্ন জাগিয়ে তুলুন এবং নিজে উত্তর খুঁজে বের করতে সাহায্য করুন।",
            "সম্ভব হলে সহজ নিরাপদ উপকরণ দিয়ে ছোট পরীক্ষণ বা ছবি আঁকিয়ে শেখান।"
        )
        subjectName.contains("বাংলা") -> listOf(
            "সন্তানকে দিয়ে পুরো গল্প বা কবিতাটি স্পষ্ট উচ্চারণে অন্তত ২ বার রিডিং পড়ান।",
            "কঠিন শব্দগুলো আলাদা খাতায় লিখে অর্থ ও যুক্তবর্ণ ভেঙে লেখার অনুশীলন করান।",
            "পড়া শেষে 'আজকে কী শিখলে?' জিজ্ঞেস করে তার নিজের ভাষায় বর্ণনা শুনতে চান।"
        )
        subjectName.contains("English") -> listOf(
            "নতুন শব্দগুলো অর্থসহ মুখে মুখে ৪-৫ বার জোরে উচ্চারণ করিয়ে শোনান।",
            "দৈনন্দিন ছোট ছোট বাক্যে (যেমন: 'Give me the book', 'Open the door') কথা বলার অভ্যাস করান।",
            "স্পেলিং ও গ্রামার প্যাটার্ন লিখে লিখে বানান নির্ভুল করার চর্চা করান।"
        )
        subjectName.contains("সমাজ") || subjectName.contains("পরিচয়") -> listOf(
            "আমাদের দেশ, মুক্তিযুদ্ধ, ইতিহাস ও নাগরিক দায়িত্ব নিয়ে গল্পের মতো করে আলোচনা করুন।",
            "বাংলাদেশের মানচিত্রে বিভিন্ন জেলা, নদী ও গুরুত্বপূর্ণ ঐতিহাসিক স্থানগুলো চিহ্নিত করতে শেখান।",
            "পরিবেশ পরিচ্ছন্ন রাখা ও বয়োজ্যেষ্ঠদের সম্মান করার নৈতিক দায়িত্ব স্মরণ করিয়ে দিন।"
        )
        subjectName.contains("ধর্ম") || subjectName.contains("নৈতিক") -> listOf(
            "শুধু মুখস্থ নয়, নৈতিক শিক্ষাগুলো বাস্তব জীবনে (সত্য বলা, মানুষের উপকার করা) চর্চা করতে শেখান।",
            "প্রতিদিনের ছোট ছোট ভালো কাজ করার পর সন্তানকে প্রশংসা ও উৎসাহ দিন।"
        )
        else -> listOf(
            "পাঠ্যবইয়ের মূল বিষয়বস্তু মনোযোগ দিয়ে একসঙ্গে পড়ে গুরুত্বপূর্ণ পয়েন্টগুলো মার্ক করে দিন।",
            "সন্তানের যেকোনো প্রশ্ন মনোযোগ দিয়ে শুনুন এবং স্নেহপূর্ণভাবে বুঝিয়ে বলুন।"
        )
    }
}

private fun getCommonMistakes(subjectName: String, topic: CurriculumTopic): List<String> {
    return when {
        subjectName.contains("গণিত") -> listOf(
            "হিসাবের সময় যোগ/বিয়োগে হাতে থাকা সংখ্যা (Carry) যোগ করতে ভুলে যাওয়া।",
            "উত্তরে একক (টাকা, মিটার, গ্রাম, টি) লিখতে ভুলে যাওয়া।",
            "প্রশ্নের মূল চাওয়া (কী বের করতে বলেছে) ভালো করে না পড়ে আগেই হিসাব শুরু করা।"
        )
        subjectName.contains("বিজ্ঞান") -> listOf(
            "সংজ্ঞা বা বৈজ্ঞানিক কারণ মুখস্থ করলেও বাস্তব উদাহরণের সাথে মেলাতে না পারা।",
            "উদ্ভিদ ও প্রাণীর বিভিন্ন অংশের কাজ গুলিয়ে ফেলা।"
        )
        subjectName.contains("বাংলা") -> listOf(
            "যুক্তবর্ণ ভাঙার ক্ষেত্রে ভুল বর্ণ বসানো (যেমন: ষ্ণ = ষ্ + ণ, ক্ষ = ক্ + ষ)।",
            "দাঁড়ি, কমা ও যতিচিহ্ন সঠিকভাবে ব্যবহার না করে বাক্য একটানা পড়া।"
        )
        subjectName.contains("English") -> listOf(
            "Subject-Verb Agreement এ ভুল (যেমন: He play এর বদলে He plays হবে)।",
            "Plural বা Past Tense এ 's' বা 'ed' যোগ করার নিয়ম গুলিয়ে ফেলা।"
        )
        subjectName.contains("সমাজ") || subjectName.contains("পরিচয়") -> listOf(
            "গুরুত্বপূর্ণ ঐতিহাসিক সন ও তারিখ অদল-বদল করে ফেলা।",
            "মানচিত্রে দিক (উত্তর, দক্ষিণ, পূর্ব, পশ্চিম) চিনতে বিভ্রান্ত হওয়া।"
        )
        else -> listOf(
            "প্রশ্ন না বুঝে তাড়াতাড়ি উত্তর লেখার তাড়াহুড়ো করা।"
        )
    }
}

private fun getHomeActivity(subjectName: String, topic: CurriculumTopic): String {
    return when {
        subjectName.contains("গণিত") -> "ঘরে থাকা কয়েন, চামচ বা বোতলের ছিপি একত্র করে সন্তানকে বিভিন্ন দল ও সংখ্যায় ভাগ করে হিসাব করতে দিন।"
        subjectName.contains("বিজ্ঞান") -> "বারান্দার টবের গাছ বা রান্নাঘরের শাকসবজি পর্যবেক্ষণ করে মূল, কাণ্ড, পাতা আলাদা করে খাতায় ছবি আঁকতে বলুন।"
        subjectName.contains("বাংলা") -> "আজকের গল্প বা পাঠ থেকে সন্তানকে পছন্দের চরিত্র বেছে নিয়ে ৩ লাইনের একটি চিঠি লিখতে বলুন।"
        subjectName.contains("English") -> "Ask your child to find 5 objects in the room and say their English names and colors aloud."
        subjectName.contains("সমাজ") || subjectName.contains("পরিচয়") -> "পরিবারের সদস্যদের নাম ও বয়সের তালিকা এবং পরিবারের সবার কী কী দায়িত্ব তা একটি চার্টে সাজাতে বলুন।"
        else -> "আজকের পঠিত অধ্যায়টি থেকে ৩টি সুন্দর বাক্য নিজের খাতায় ক্যালিগ্রাফির মতো পরিচ্ছন্নভাবে লিখতে দিন।"
    }
}

private fun getParentOralQuizQuestions(subjectName: String, topic: CurriculumTopic): List<TopicQA> {
    if (topic.qaList.isNotEmpty()) {
        return topic.qaList.take(3)
    }
    return when {
        subjectName.contains("গণিত") -> listOf(
            TopicQA(
                question = "${topic.titleBangla} থেকে একটি সাধারণ হিসাব মুখে মুখে বলো।",
                answer = "উত্তর: ${topic.formulaOrRule ?: topic.summaryBangla}"
            ),
            TopicQA(
                question = "এই অধ্যায়ের মূল নিয়মটি কী?",
                answer = "উত্তর: ${topic.summaryBangla}"
            )
        )
        subjectName.contains("বিজ্ঞান") -> listOf(
            TopicQA(
                question = "${topic.titleBangla} বিষয়টি আমাদের পরিবেশে কোথায় দেখতে পাই?",
                answer = "উত্তর: ${topic.summaryBangla}"
            ),
            TopicQA(
                question = "এর একটি বাস্তব উপকারিতা বলো।",
                answer = "উত্তর: বিস্তারিত পাঠ্যসূচির মূল পয়েন্টে আলোচনা রয়েছে।"
            )
        )
        subjectName.contains("বাংলা") -> listOf(
            TopicQA(
                question = "${topic.titleBangla} পাঠটির মূল শিক্ষা বা সারমর্ম কী?",
                answer = "উত্তর: ${topic.summaryBangla}"
            )
        )
        subjectName.contains("English") -> listOf(
            TopicQA(
                question = "Tell one sentence about ${topic.titleBangla}.",
                answer = "Answer: ${topic.summaryBangla}"
            )
        )
        else -> listOf(
            TopicQA(
                question = "${topic.titleBangla} অধ্যায়ের প্রধান শিক্ষণীয় বিষয় কী?",
                answer = "উত্তর: ${topic.summaryBangla}"
            )
        )
    }
}
