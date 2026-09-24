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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ai.StudioPromptGuide
import com.example.ui.theme.PremiumPurple
import com.example.ui.theme.PremiumPurpleContainer
import com.example.ui.theme.WarmTextPrimary
import com.example.ui.theme.WarmTextSecondary
import com.example.ui.theme.WarmPaperBg
import com.example.ui.theme.WarmPaperBorder

@Composable
fun ReferenceStudioScreen(
    onAsk: (String) -> Unit,
    onOpenCurriculum: () -> Unit,
    modifier: Modifier = Modifier
) {
    var parentSelected by remember { mutableStateOf(true) }
    val suggestions = if (parentSelected) StudioPromptGuide.parentQuestions else StudioPromptGuide.teacherQuestions
    Column(modifier.fillMaxSize().background(WarmPaperBg).verticalScroll(rememberScrollState())
        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("আলো স্টুডিও", color = PremiumPurple, style = MaterialTheme.typography.labelSmall)
                Text("পাঠের বাইরেও শেখার সহায়তা", color = WarmTextPrimary,
                    style = MaterialTheme.typography.headlineSmall)
                Text("শিশুর পাশে থেকে তাকে শেখার পথ খুঁজে নিতে সাহায্য করি।",
                    color = WarmTextSecondary, style = MaterialTheme.typography.bodyMedium)
            }
            Image(painterResource(R.drawable.studio_companion_v2),
                "শিশুদের শেখার ইলাস্ট্রেশন", Modifier.size(78.dp), contentScale = ContentScale.Fit)
        }
        Surface(shape = RoundedCornerShape(14.dp), color = PremiumPurpleContainer) {
            Row(Modifier.fillMaxWidth().padding(5.dp)) {
                listOf(true to "অভিভাবক", false to "শিক্ষক").forEach { (parent, label) ->
                    Surface(modifier = Modifier.weight(1f).clickable { parentSelected = parent },
                        shape = RoundedCornerShape(11.dp),
                        color = if (parentSelected == parent) Color.White else PremiumPurpleContainer) {
                        Text(label, modifier = Modifier.padding(11.dp),
                            color = if (parentSelected == parent) PremiumPurple else WarmTextSecondary,
                            fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
        Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFFFE7B5),
            border = BorderStroke(1.dp, WarmPaperBorder), shadowElevation = 6.dp) {
            Column(Modifier.fillMaxWidth().background(Brush.verticalGradient(
                listOf(Color.White, Color(0xFFFFE7B5)))).padding(20.dp)) {
                Text(if (parentSelected) "বাড়িতে শেখার সহায়তা" else "শ্রেণিকক্ষে শেখার সহায়তা",
                    color = WarmTextPrimary, style = MaterialTheme.typography.titleLarge)
                Text(if (parentSelected) "শিশুর শেখার জন্য বাস্তব পরামর্শ নিন।" else "পাঠদান, মূল্যায়ন ও ফলো-আপ সাজান।",
                    color = WarmTextSecondary, style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { onAsk(if (parentSelected) StudioPromptGuide.PARENT_OVERVIEW else StudioPromptGuide.TEACHER_OVERVIEW) },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumPurple)) {
                    Text("আলোকে প্রশ্ন করি  →")
                }
            }
        }
        Text("একটি প্রশ্ন দিয়ে শুরু করো", color = WarmTextPrimary,
            style = MaterialTheme.typography.titleMedium)
        suggestions.forEach { prompt ->
            Surface(modifier = Modifier.fillMaxWidth().clickable { onAsk(prompt) },
                shape = RoundedCornerShape(14.dp), color = Color.White,
                border = BorderStroke(1.dp, WarmPaperBorder), shadowElevation = 4.dp) {
                Text("$prompt  →", modifier = Modifier.background(Brush.verticalGradient(
                    listOf(Color.White, Color(0xFFFFF8E9)))).padding(15.dp),
                    color = WarmTextPrimary, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Button(onClick = onOpenCurriculum, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PremiumPurple)) {
            Text("বই ও অধ্যায়ে ফিরে যাই")
        }
    }
}
