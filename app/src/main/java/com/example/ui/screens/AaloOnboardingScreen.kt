package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import com.example.ui.screens.DigitText as Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.AaloWordmark
import com.example.ui.theme.PremiumPurple
import com.example.ui.theme.WarmTextPrimary
import com.example.ui.theme.WarmTextSecondary
import com.example.ui.theme.WarmPaperBg

@Composable
fun AaloOnboardingScreen(onGetStarted: (String) -> Unit, modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFFFF8E8), WarmPaperBg, Color(0xFFF3FAF6)))
        )
            .statusBarsPadding().verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp).testTag("aalo_onboarding_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        Image(painter = painterResource(R.drawable.aalo_brand_logo_soft),
            contentDescription = "AaloPro লোগো", modifier = Modifier.size(80.dp).clip(CircleShape))
        Spacer(Modifier.height(12.dp))
        Text("AaloPro", style = MaterialTheme.typography.headlineLarge, fontFamily = AaloWordmark,
            fontWeight = FontWeight.Black, color = WarmTextPrimary)
        Spacer(Modifier.height(8.dp))
        Text("প্রশ্ন করো। বুঝে শেখো।", style = MaterialTheme.typography.headlineSmall,
            color = WarmTextPrimary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("প্রথম থেকে পঞ্চম শ্রেণির বাছাই করা টপিক দেখো, আলো শিক্ষককে প্রশ্ন করো, আর আগের আলোচনা থেকে শেখা চালিয়ে যাও।",
            style = MaterialTheme.typography.bodyMedium, color = WarmTextSecondary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(18.dp))
        Text("আপনার নাম লিখুন", style = MaterialTheme.typography.titleSmall,
            color = WarmTextPrimary, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = name, onValueChange = { name = it.take(40) },
            modifier = Modifier.fillMaxWidth().testTag("onboarding_name"),
            label = { Text("আপনার নাম") }, singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PremiumPurple))
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onGetStarted(name) }, enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(54.dp)
            .testTag("onboarding_start_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PremiumPurple),
            shape = RoundedCornerShape(16.dp)) {
            Text("শেখা শুরু করি", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(Modifier.height(22.dp))
        OnboardingPath(Icons.Filled.AutoStories, "শেখার টপিক", "শ্রেণি ও বিষয় বেছে সব শেখার টপিক দেখো",
            wash = Color(0xFFFFF0D1), accent = Color(0xFF936B22))
        Spacer(Modifier.height(8.dp))
        OnboardingPath(Icons.Filled.ChatBubbleOutline, "আলো শিক্ষক", "পূর্ণ পর্দায় প্রশ্ন ও ফলো-আপ করো",
            wash = Color(0xFFE8F5EF), accent = Color(0xFF2B8075))
        Spacer(Modifier.height(8.dp))
        OnboardingPath(Icons.Filled.Groups, "অভিভাবক ও শিক্ষক", "শেখানোর সহজ পরামর্শ দেখো",
            wash = Color(0xFFFFEEE6), accent = Color(0xFFB9684B))
        Spacer(Modifier.height(14.dp))
        Text("কিছু পাঠ ও নিয়ম অফলাইনে আছে। নতুন বা খোলা প্রশ্নের জন্য ইন্টারনেট লাগতে পারে।",
            style = MaterialTheme.typography.bodySmall, color = WarmTextSecondary,
            textAlign = TextAlign.Center)
    }
}

@Composable
private fun OnboardingPath(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String,
                           wash: Color, accent: Color) {
    Surface(shape = RoundedCornerShape(20.dp), color = wash,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.16f)), shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.background(Brush.horizontalGradient(listOf(Color.White, wash.copy(alpha = 0.88f)))).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)) {
            Box(Modifier.size(42.dp).background(accent.copy(alpha = 0.13f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = accent, modifier = Modifier.size(23.dp))
            }
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, color = WarmTextPrimary)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = WarmTextSecondary)
            }
        }
    }
}
