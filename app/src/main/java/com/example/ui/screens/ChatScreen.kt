package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import com.example.ui.screens.DigitText as Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ai.ChatAiMode
import com.example.ai.StudySection
import com.example.data.local.AloChatEntity
import com.example.ui.AloTtsManager
import com.example.ui.AppUserMode
import com.example.ui.theme.AaloYellow
import com.example.ui.theme.AaloYellowContainer
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.KidStarGold
import com.example.ui.theme.OnAaloYellowContainer
import com.example.ui.theme.PremiumPurple
import com.example.util.AloTextSanitizer

@Composable
fun ChatScreen(
    chatList: List<AloChatEntity>,
    inputText: String,
    isThinking: Boolean,
    chatAiMode: ChatAiMode,
    selectedSection: StudySection,
    activeChapterContext: String? = null,
    activeSubjectContext: String? = null,
    activeFollowUpChat: AloChatEntity?,
    sectionCounts: Map<String, Int> = emptyMap(),
    userMode: AppUserMode = AppUserMode.KIDS,
    onModeChanged: (ChatAiMode) -> Unit,
    onSectionChanged: (StudySection) -> Unit,
    onClearChapterContext: () -> Unit = {},
    onStartFollowUp: (AloChatEntity) -> Unit,
    onCancelFollowUp: () -> Unit,
    onInputChanged: (String) -> Unit,
    onSendQuery: (String?) -> Unit,
    onSaveToNotes: (AloChatEntity) -> Unit,
    onDeleteChat: (Long) -> Unit,
    onClearAllChats: () -> Unit,
    onMinimizeChat: () -> Unit,
    onCloseChat: () -> Unit,
    onGoHome: () -> Unit,
    ttsManager: AloTtsManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var currentlySpeakingChatId by remember { mutableStateOf<Long?>(null) }
    var showHistory by remember { mutableStateOf(false) }

    LaunchedEffect(chatList.size) {
        if (chatList.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    PremiumLearningChatCanvas(
        chatList = chatList,
        inputText = inputText,
        isThinking = isThinking,
        chatAiMode = chatAiMode,
        userMode = userMode,
        selectedSection = selectedSection,
        activeChapterContext = activeChapterContext,
        activeSubjectContext = activeSubjectContext,
        activeFollowUpChat = activeFollowUpChat,
        sectionCounts = sectionCounts,
        showHistory = showHistory,
        onToggleHistory = { showHistory = !showHistory },
        onModeChanged = onModeChanged,
        onSectionChanged = onSectionChanged,
        onClearChapterContext = onClearChapterContext,
        onStartFollowUp = onStartFollowUp,
        onCancelFollowUp = onCancelFollowUp,
        onInputChanged = onInputChanged,
        onSendQuery = onSendQuery,
        onSaveToNotes = onSaveToNotes,
        onDeleteChat = onDeleteChat,
        onClearAllChats = onClearAllChats,
        onMinimizeChat = onMinimizeChat,
        onCloseChat = onCloseChat,
        onGoHome = onGoHome,
        ttsManager = ttsManager,
        context = context,
        listState = listState,
        currentlySpeakingChatId = currentlySpeakingChatId,
        onSpeakingChanged = { currentlySpeakingChatId = it }
    )
    return

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .testTag("chat_screen")
    ) {
        // This is intentionally an immersive learning room, not the small chat tab.
        // Curriculum actions enter here so reading and follow-up happen in one place.
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(34.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_aalo_circle_logo),
                        contentDescription = "আলো এআই",
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Text(
                        text = "AI শিক্ষকের সাথে পড়া",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (activeFollowUpChat != null) "ফলো-আপ চলছে" else "লার্নিং চ্যাট",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                IconButton(onClick = onMinimizeChat, modifier = Modifier.testTag("minimize_learning_chat")) {
                    Icon(Icons.Default.Minimize, "চ্যাট ছোট করো", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = onCloseChat, modifier = Modifier.testTag("close_learning_chat")) {
                    Icon(Icons.Default.Close, "চ্যাট বন্ধ করো", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // A clear landing point keeps the tutor, saved discussion and a fresh start
        // understandable before a child reaches the message composer.
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.42f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.14f))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "আলো এআই টিউটর",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "প্রশ্ন করো, আগের আলোচনা চালিয়ে যাও, বা নতুন করে শুরু করো",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(shape = CircleShape, color = AaloYellow, modifier = Modifier.size(34.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showHistory = !showHistory }
                            .testTag("chat_history_button")
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 11.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Menu, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("আলোচনার ইতিহাস", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AaloYellow,
                        modifier = Modifier
                            .clickable {
                                onCancelFollowUp()
                                onClearChapterContext()
                                onInputChanged("")
                                showHistory = false
                            }
                            .testTag("new_conversation_button")
                    ) {
                        Text(
                            text = "নতুন প্রশ্ন",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }
                }
                AnimatedVisibility(visible = showHistory) {
                    Column(modifier = Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("এই বিভাগের সাম্প্রতিক আলোচনা", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (chatList.isEmpty()) {
                            Text("এখনও কোনো আলোচনা নেই—নিচে প্রথম প্রশ্নটি লিখে শুরু করো।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            chatList.take(3).forEach { chat ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onStartFollowUp(chat); showHistory = false }
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.AutoMirrored.Filled.Reply, null, modifier = Modifier.size(15.dp), tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(chat.query, modifier = Modifier.weight(1f), fontSize = 12.sp, maxLines = 1, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Transparent,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onClearAllChats()
                                        showHistory = false
                                        Toast.makeText(context, "এই বিভাগের আলোচনা মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }
                            ) {
                                Text(
                                    text = "এই বিভাগের সব আলোচনা মুছুন",
                                    modifier = Modifier.padding(10.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        // Top Class / Section Selector Tabs
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StudySection.entries.forEach { section ->
                        val isSelected = section == selectedSection
                        val count = sectionCounts[section.id] ?: 0
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) AaloYellow else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = if (isSelected) BorderStroke(1.dp, AmberAccent) else BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onSectionChanged(section) }
                                .testTag("chat_tab_${section.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = section.emoji,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = section.titleBangla,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                )
                                if (count > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSelected) Color.Black.copy(alpha = 0.15f) else AaloYellow.copy(alpha = 0.4f),
                                        modifier = Modifier.size(18.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = count.toString(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Sub-Bar: Active Class Label and Mode Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                        .testTag("chat_sub_header_bar"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Active Class Badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = AaloYellow,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedSection.gradeBadge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " • ${chatList.size}টি আলোচনা",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Right: Mode Toggle Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                val nextMode = if (chatAiMode == ChatAiMode.CURRICULUM) ChatAiMode.GENERAL else ChatAiMode.CURRICULUM
                                onModeChanged(nextMode)
                            }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = if (chatAiMode == ChatAiMode.GENERAL) Icons.Default.Public else Icons.Default.School,
                            contentDescription = null,
                            tint = AaloYellow,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (chatAiMode == ChatAiMode.GENERAL) "জেনারেল এআই" else "কারিকুলাম এআই",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Active Chapter Context Thread Banner (If coming from Curriculum)
        AnimatedVisibility(
            visible = !activeChapterContext.isNullOrBlank(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut()
        ) {
            if (!activeChapterContext.isNullOrBlank()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = AaloYellowContainer.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, AaloYellow.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = OnAaloYellowContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "📖 অধ্যায়ভিত্তিক বিশেষ থ্রেড",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnAaloYellowContainer
                                )
                                Text(
                                    text = "${if (!activeSubjectContext.isNullOrBlank()) "$activeSubjectContext > " else ""}$activeChapterContext",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                        IconButton(
                            onClick = onClearChapterContext,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear chapter context",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Chat conversation list
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (chatList.isEmpty()) {
                EmptyChatWelcome(
                    section = selectedSection,
                    activeChapter = activeChapterContext,
                    mode = chatAiMode,
                    userMode = userMode,
                    onSelectPrompt = { prompt ->
                        onSendQuery(prompt)
                    }
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = chatList, key = { it.id }) { chat ->
                        ChatMessageItem(
                            chat = chat,
                            isSpeaking = currentlySpeakingChatId == chat.id,
                            onToggleSpeak = {
                                if (currentlySpeakingChatId == chat.id) {
                                    ttsManager.stop()
                                    currentlySpeakingChatId = null
                                } else {
                                    currentlySpeakingChatId = chat.id
                                    ttsManager.speak(AloTextSanitizer.cleanText(chat.responseBangla)) {
                                        if (currentlySpeakingChatId == chat.id) {
                                            currentlySpeakingChatId = null
                                        }
                                    }
                                }
                            },
                            onCopyText = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("AloPro Answer", AloTextSanitizer.cleanText(chat.responseBangla))
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "উত্তর কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                            },
                            onSaveToNotes = { onSaveToNotes(chat) },
                            onDelete = { onDeleteChat(chat.id) },
                            onStartFollowUp = { onStartFollowUp(chat) },
                            onEditQuery = {
                                onInputChanged(chat.query)
                                Toast.makeText(context, "প্রশ্নটি এডিট করে আবার পাঠান", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            // AI Thinking indicator overlay
            if (isThinking) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = AaloYellow,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (activeFollowUpChat != null)
                                "আগের প্রশ্নটি ধরে উত্তর খুঁজছি..."
                            else
                                "আলো এআই বিশ্লেষণ করছে...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Active Follow-up Topic Context Banner
        AnimatedVisibility(
            visible = activeFollowUpChat != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut()
        ) {
            if (activeFollowUpChat != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = AaloYellowContainer.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, AaloYellow.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Reply,
                                contentDescription = null,
                                tint = OnAaloYellowContainer,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "🔄 আগের কথার সঙ্গে মিলিয়ে উত্তর দিচ্ছি",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnAaloYellowContainer
                                )
                                Text(
                                    text = "প্রসঙ্গ: \"${activeFollowUpChat.query.take(45)}...\"",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                        IconButton(
                            onClick = onCancelFollowUp,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel Follow-up",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Input bottom bar (Cleanly matching user screenshot)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // History is also available next to the composer, so a learner can
                // resume a discussion without guessing what the old menu did.
                IconButton(
                    onClick = { showHistory = !showHistory },
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("chat_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "আলোচনার ইতিহাস",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                val placeholderText = if (activeFollowUpChat != null) {
                    "পূর্ববর্তী বিষয়ের প্রেক্ষিতে ফলো-আপ লিখুন..."
                } else if (!activeChapterContext.isNullOrBlank()) {
                    "$activeChapterContext সংক্রান্ত প্রশ্ন লিখুন..."
                } else {
                    "${selectedSection.titleBangla} এর যেকোনো প্রশ্ন লিখুন..."
                }

                // Rounded Pill Input Text Field
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputChanged,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    placeholder = {
                        Text(
                            text = placeholderText,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AaloYellow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Voice / Mic Button for Kids
                IconButton(
                    onClick = {
                        Toast.makeText(context, "ভয়েস চালু হচ্ছে... মুখে প্রশ্ন বলো বন্ধু!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(AaloYellowContainer)
                        .testTag("voice_input_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "মুখে বলো",
                        tint = OnAaloYellowContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Send Button (Arrow)
                IconButton(
                    onClick = { onSendQuery(null) },
                    enabled = inputText.trim().isNotEmpty() && !isThinking,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.trim().isNotEmpty() && !isThinking) AaloYellow
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .testTag("send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "পাঠান",
                        tint = if (inputText.trim().isNotEmpty() && !isThinking) Color.Black
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumLearningChatCanvas(
    chatList: List<AloChatEntity>, inputText: String, isThinking: Boolean,
    chatAiMode: ChatAiMode, userMode: AppUserMode, selectedSection: StudySection,
    activeChapterContext: String?, activeSubjectContext: String?, activeFollowUpChat: AloChatEntity?,
    sectionCounts: Map<String, Int>, showHistory: Boolean, onToggleHistory: () -> Unit,
    onModeChanged: (ChatAiMode) -> Unit, onSectionChanged: (StudySection) -> Unit,
    onClearChapterContext: () -> Unit, onStartFollowUp: (AloChatEntity) -> Unit,
    onCancelFollowUp: () -> Unit, onInputChanged: (String) -> Unit, onSendQuery: (String?) -> Unit,
    onSaveToNotes: (AloChatEntity) -> Unit, onDeleteChat: (Long) -> Unit,
    onClearAllChats: () -> Unit, onMinimizeChat: () -> Unit, onCloseChat: () -> Unit,
    onGoHome: () -> Unit,
    ttsManager: AloTtsManager, context: Context,
    listState: androidx.compose.foundation.lazy.LazyListState,
    currentlySpeakingChatId: Long?, onSpeakingChanged: (Long?) -> Unit
) {
    val navy = Color(0xFFFFFBF3)
    val panel = Color(0xFFFFFEFA)
    val lavender = Color(0xFF6C6A62)
    val mint = Color(0xFF2B8075)
    val ink = Color(0xFF222A2D)

    Box(
        modifier = Modifier.fillMaxSize().background(navy).statusBarsPadding().navigationBarsPadding().testTag("premium_learning_room")
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onGoHome, modifier = Modifier.size(38.dp).testTag("chat_go_home")) {
                    Icon(Icons.Default.Home, "হোমে যান", tint = mint)
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("আলো শিক্ষক", color = ink, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(if (activeFollowUpChat != null) "আগের প্রশ্নটি মনে আছে" else "আলো শিক্ষক তোমার সঙ্গে আছে", color = lavender, fontSize = 11.sp)
                }
                IconButton(onClick = onToggleHistory, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Default.Menu, "আলোচনার ইতিহাস", tint = ink, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onMinimizeChat, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Default.Minimize, "মিনিমাইজ", tint = ink, modifier = Modifier.size(19.dp))
                }
                IconButton(onClick = onCloseChat, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Close, "বন্ধ করো", tint = ink, modifier = Modifier.size(18.dp))
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                shape = RoundedCornerShape(18.dp), color = panel,
                border = BorderStroke(1.dp, Color(0xFFE8DCC7)), shadowElevation = 5.dp
            ) {
                Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (chatAiMode == ChatAiMode.GENERAL) Icons.Default.Public else Icons.Default.School, null, tint = mint, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text(if (!activeChapterContext.isNullOrBlank()) activeChapterContext else if (chatAiMode == ChatAiMode.GENERAL) "যেকোনো প্রশ্ন" else selectedSection.titleBangla, color = ink, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(if (!activeSubjectContext.isNullOrBlank()) activeSubjectContext else if (chatAiMode == ChatAiMode.GENERAL) "যেকোনো প্রশ্ন, পরিকল্পনা বা সহায়তা" else "কারিকুলাম-ভিত্তিক শেখা", color = lavender, fontSize = 12.sp, maxLines = 1)
                    }
                    Text(if (chatAiMode == ChatAiMode.GENERAL) "সাধারণ" else "পাঠ", color = mint, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFDFF2EA)).padding(horizontal = 7.dp, vertical = 4.dp))
                }
            }

            if (activeFollowUpChat != null) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Reply, null, tint = mint, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("আগের উত্তরটি নিয়ে আরও প্রশ্ন করো", color = mint, fontSize = 11.sp, modifier = Modifier.weight(1f))
                    IconButton(onClick = onCancelFollowUp, modifier = Modifier.size(26.dp)) { Icon(Icons.Default.Close, null, tint = mint, modifier = Modifier.size(16.dp)) }
                }
            }

            Box(Modifier.weight(1f).fillMaxWidth()) {
                if (chatList.isEmpty()) {
                    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(if (userMode == AppUserMode.TEACHER_PARENT)
                            R.drawable.studio_companion_v2 else R.drawable.tutor_companion_v2),
                            "প্রশ্নোত্তরে শেখার সঙ্গী", Modifier.size(112.dp))
                        Spacer(Modifier.height(18.dp))
                        Text(if (chatAiMode == ChatAiMode.GENERAL) "কী নিয়ে ভাবছেন?" else "আজ কী শিখবে?", color = ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(7.dp))
                        Text("প্রশ্ন লিখুন, উদাহরণ চান, বা আগের আলোচনায় ফিরুন।", color = lavender, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(chatList, key = { it.id }) { chat ->
                            ChatMessageItem(chat, currentlySpeakingChatId == chat.id,
                                onToggleSpeak = {
                                    if (currentlySpeakingChatId == chat.id) { ttsManager.stop(); onSpeakingChanged(null) }
                                    else { onSpeakingChanged(chat.id); ttsManager.speak(AloTextSanitizer.cleanText(chat.responseBangla)) { onSpeakingChanged(null) } }
                                },
                                onCopyText = { (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("AaloPro Answer", AloTextSanitizer.cleanText(chat.responseBangla))) },
                                onSaveToNotes = { onSaveToNotes(chat) }, onDelete = { onDeleteChat(chat.id) }, onStartFollowUp = { onStartFollowUp(chat) }, onEditQuery = { onInputChanged(chat.query) })
                        }
                    }
                }
                if (isThinking) Surface(modifier = Modifier.align(Alignment.BottomCenter).padding(14.dp), shape = RoundedCornerShape(16.dp), color = mint) { Text("আলো ভাবছে…", color = Color.White, modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp), fontWeight = FontWeight.Bold) }
            }

            Surface(color = panel, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, Color(0xFFE8DCC7))) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onModeChanged(if (chatAiMode == ChatAiMode.GENERAL) ChatAiMode.CURRICULUM else ChatAiMode.GENERAL) }) { Icon(Icons.Default.AutoAwesome, "মোড বদলান", tint = mint) }
                    OutlinedTextField(value = inputText, onValueChange = onInputChanged, modifier = Modifier.weight(1f).testTag("chat_input_field"), placeholder = { Text(if (activeFollowUpChat != null) "আর কী জানতে চাও?" else "আলো শিক্ষককে প্রশ্ন করো", color = lavender) }, maxLines = 3, shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ink, unfocusedTextColor = ink, focusedContainerColor = panel, unfocusedContainerColor = panel, focusedBorderColor = mint, unfocusedBorderColor = Color(0xFFD9E7E4)))
                    IconButton(onClick = { onSendQuery(null) }, enabled = inputText.isNotBlank() && !isThinking, modifier = Modifier.size(48.dp).clip(CircleShape).background(if (inputText.isNotBlank()) mint else Color(0xFFB8C9C6)).testTag("send_button")) { Icon(Icons.AutoMirrored.Filled.Send, "পাঠান", tint = Color.White) }
                }
            }
        }

        if (showHistory) Surface(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(top = 72.dp, start = 18.dp, end = 18.dp), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 18.dp) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text("আলোচনার ইতিহাস", color = ink, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f)); IconButton(onClick = onToggleHistory) { Icon(Icons.Default.Close, null, tint = ink) } }
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StudySection.values().forEach { section ->
                        Surface(modifier = Modifier.clickable { onSectionChanged(section) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedSection == section) mint else Color(0xFFDFF2EA)) {
                            Text("${section.titleBangla} ${sectionCounts[section.id] ?: 0}",
                                color = if (selectedSection == section) Color.White else ink,
                                fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp))
                        }
                    }
                }
                Text("নতুন প্রশ্ন শুরু", color = mint, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onCancelFollowUp(); onClearChapterContext(); onInputChanged("")
                        onModeChanged(ChatAiMode.GENERAL); onSectionChanged(StudySection.UNIVERSAL)
                        onToggleHistory()
                    }.padding(vertical = 10.dp))
                if (chatList.isEmpty()) Text("এখনও কোনো আলোচনা নেই।", color = lavender) else LazyColumn(modifier = Modifier.heightIn(max = 360.dp)) {
                    items(chatList, key = { it.id }) { chat ->
                        Text(chat.query, color = ink, maxLines = 2, modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onStartFollowUp(chat); onToggleHistory() }
                            .padding(vertical = 10.dp))
                    }
                }
                if (chatList.isNotEmpty()) Text("সব আলোচনা মুছুন", color = Color(0xFFFFA6B3), modifier = Modifier.clickable { onClearAllChats(); onToggleHistory() }.padding(top = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EmptyChatWelcome(
    section: StudySection,
    activeChapter: String? = null,
    mode: ChatAiMode,
    userMode: AppUserMode = AppUserMode.KIDS,
    onSelectPrompt: (String) -> Unit
) {
    val samplePrompts = if (!activeChapter.isNullOrBlank()) {
        listOf(
            "অধ্যায়: $activeChapter এর মূল সারসংক্ষেপ ও সহজ নিয়ম বলো",
            "$activeChapter এর গুরুত্বপূর্ণ কিছু উদাহরণ দাও",
            "$activeChapter থেকে পরীক্ষায় কী ধরণের প্রশ্ন আসতে পারে?"
        )
    } else if (userMode == AppUserMode.TEACHER_PARENT) {
        listOf(
            "৩য় শ্রেণির গণিতের জন্য একটি পূর্ণাঙ্গ লেসন প্ল্যান তৈরি করো",
            "ক্লাসে বাচ্চাদের মনোযোগ ধরে রাখার ৫টি কার্যকর টেকনিক বলো",
            "৪র্থ শ্রেণির বিজ্ঞানের উপর ১টি সৃজনশীল (CQ) প্রশ্ন তৈরি করো",
            "বাচ্চাদের মোবাইল ফোনের আসক্তি কমানোর উপায় কী?",
            "শিশুর হাতের লেখা সুন্দর করার সহজ বৈজ্ঞানিক কৌশল",
            "পরীক্ষার ভয় দূর করতে অভিভাবক হিসেবে কী করব?"
        )
    } else {
        listOf(
            "৭ এর ঘরের নামতা বলো",
            "আমাদের জাতীয় কবি কে?",
            "গাছপালা কীভাবে খাবার তৈরি করে?",
            "ছোটদের ৫টি দরকারি ইংরেজি বাক্য শেখাও",
            "সূর্য কেন দিনে আলো দেয় আর রাতে থাকে না?",
            "১ মিটার সমান কত সেন্টিমিটার?",
            "একটি সুন্দর শিক্ষণীয় রূপকথার গল্প শোনাও"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo / Mascot with playful golden glow
        Surface(
            shape = CircleShape,
            color = AaloYellowContainer,
            border = BorderStroke(2.dp, AaloYellow),
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_aalo_circle_logo),
                    contentDescription = "Aalo Mascot",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (userMode == AppUserMode.TEACHER_PARENT) {
            Text(
                text = "👨‍🏫 শিক্ষক ও অভিভাবক সহায়তা কর্নার",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "পড়ানো, প্রশ্ন বানানো বা শিশুর পড়ায় মন বসানো—যে সাহায্য দরকার, এখানে লিখুন।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 20.sp
            )
        } else {
            Text(
                text = if (!activeChapter.isNullOrBlank()) "অধ্যায়: $activeChapter" else "হ্যালো বন্ধু! আমি আলো ☀️",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (!activeChapter.isNullOrBlank()) {
                    "এই অধ্যায়ের যেকোনো নিয়ম, সমস্যা বা গাণিতিক হিসাব জিজ্ঞেস করো বন্ধু!"
                } else {
                    "১ম থেকে ৫ম শ্রেণির গণিত, বাংলা, বিজ্ঞান বা যেকোনো বিষয়ে সাহায্য করতে আমি প্রস্তুত!"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "নিচের যেকোনো প্রশ্নে ট্যাপ করো বা মুখে বলো:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Suggestion Chips (Horizontal Scroll)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            samplePrompts.forEach { query ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { onSelectPrompt(query) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = query,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    chat: AloChatEntity,
    isSpeaking: Boolean,
    onToggleSpeak: () -> Unit,
    onCopyText: () -> Unit,
    onSaveToNotes: () -> Unit,
    onDelete: () -> Unit,
    onStartFollowUp: () -> Unit,
    onEditQuery: () -> Unit
) {
    val cleanAnswer = remember(chat.responseBangla) {
        AloTextSanitizer.cleanText(chat.responseBangla)
    }

    val classSection = remember(chat.classCategory) {
        StudySection.fromId(chat.classCategory)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chat_message_${chat.id}"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Thread indicator if this message is a follow-up or has a chapter tag
        Row(
            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chat.parentChatId != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AaloYellowContainer.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Reply,
                            contentDescription = null,
                            tint = OnAaloYellowContainer,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "আগের উত্তর নিয়ে প্রশ্ন",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnAaloYellowContainer
                        )
                    }
                }
            }

            if (chat.chapterTitle.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "📖 ${chat.chapterTitle}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            } else if (classSection != StudySection.ALL && classSection != StudySection.UNIVERSAL) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${classSection.emoji} ${classSection.titleBangla}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // User Message Bubble (Right-aligned) with Edit option
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Edit query button
            IconButton(
                onClick = onEditQuery,
                modifier = Modifier
                    .size(28.dp)
                    .testTag("edit_query_btn_${chat.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "প্রশ্নটি বদলাও",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(15.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Surface(
                shape = RoundedCornerShape(18.dp, 4.dp, 18.dp, 18.dp),
                color = PremiumPurple,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .testTag("user_query_bubble")
            ) {
                Text(
                    text = chat.query,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(14.dp),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // AloPro AI Response Card (Left-aligned, beautiful styling matching screenshot)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.aalo_brand_logo_soft),
                    contentDescription = "Aalo AI",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, Color(0xFFE8DCC7)),
                tonalElevation = 2.dp,
                shadowElevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    // Clean AI response text
                    Text(
                        text = cleanAnswer,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action buttons (Follow-up, TTS, Copy, Save, Delete)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quick Follow-up button
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = AaloYellowContainer.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, AaloYellow.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .clickable { onStartFollowUp() }
                                .testTag("follow_up_btn_${chat.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Reply,
                                    contentDescription = "ফলো-আপ",
                                    tint = OnAaloYellowContainer,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "আরও জিজ্ঞেস করো",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnAaloYellowContainer
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // TTS Voice read aloud - Tactile Pill Button
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSpeaking) AaloYellow else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSpeaking) Color(0xFFD97706) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(onClick = onToggleSpeak)
                                    .testTag("speak_btn_${chat.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                        contentDescription = "Voice read aloud",
                                        tint = if (isSpeaking) Color.Black else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = if (isSpeaking) "থামান" else "শুনুন",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSpeaking) Color.Black else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // Copy Button
                            IconButton(
                                onClick = onCopyText,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy text",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Save to Notes Button
                            IconButton(
                                onClick = onSaveToNotes,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (chat.isSavedToNotes) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Save to notes",
                                    tint = if (chat.isSavedToNotes) AaloYellow else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Delete button
                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete message",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
