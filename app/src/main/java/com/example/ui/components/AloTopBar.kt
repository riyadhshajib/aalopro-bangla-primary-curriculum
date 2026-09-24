package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ai.AloMathSolver
import com.example.data.local.AloStudyStatEntity
import com.example.ui.AppUserMode
import com.example.ui.AaloUserRole
import com.example.ui.learningRankFor
import com.example.ui.theme.PremiumPurple
import com.example.ui.theme.PremiumPurpleContainer
import com.example.ui.theme.WarmPaperBg
import com.example.ui.theme.WarmPaperBorder
import com.example.ui.theme.WarmTextPrimary
import com.example.ui.theme.WarmTextSecondary

@Composable
fun AloTopBar(
    stats: AloStudyStatEntity,
    completedTopicCount: Int = 0,
    userMode: AppUserMode = AppUserMode.KIDS,
    profileName: String = "",
    onUserModeChanged: (AppUserMode) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxWidth().testTag("alo_top_bar"), color = WarmPaperBg) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.aalo_brand_logo_soft), "AaloPro Logo",
                    Modifier.size(35.dp).clip(CircleShape))
                Spacer(Modifier.width(9.dp))
                Column {
                    Text("AaloPro", color = WarmTextPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = com.example.ui.theme.AaloWordmark,
                            fontWeight = FontWeight.Black
                        ))
                    Text(if (profileName.isBlank()) "শিশুর শেখার পথ" else profileName,
                        color = WarmTextSecondary, style = MaterialTheme.typography.labelSmall,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                val stars = (stats.queriesAnswered * 2) + (stats.quizScoreSum * 5) + (completedTopicCount * 10)
                if (userMode == AppUserMode.KIDS) {
                    val rank = learningRankFor(stars)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(rank.imageRes), rank.title, Modifier.size(24.dp))
                        Spacer(Modifier.width(3.dp))
                        Text(
                            "★ ${AloMathSolver.toBanglaDigits(stars)} · ${rank.title}",
                            color = PremiumPurple,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                }
                Surface(shape = RoundedCornerShape(12.dp), color = PremiumPurpleContainer,
                    border = BorderStroke(1.dp, WarmPaperBorder)) {
                    Row(Modifier.padding(3.dp), verticalAlignment = Alignment.CenterVertically) {
                        listOf(AppUserMode.KIDS to "শিশুদের", AppUserMode.TEACHER_PARENT to "বড়দের")
                            .forEach { (mode, title) ->
                                val active = userMode == mode
                                Surface(modifier = Modifier.clickable { onUserModeChanged(mode) },
                                    shape = RoundedCornerShape(9.dp),
                                    color = if (active) Color.White else PremiumPurpleContainer,
                                    shadowElevation = if (active) 2.dp else 0.dp) {
                                    Text(title, modifier = Modifier.padding(horizontal = 7.dp, vertical = 7.dp),
                                        color = if (active) PremiumPurple else WarmTextSecondary,
                                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                        style = MaterialTheme.typography.labelSmall)
                                }
                            }
                    }
                }
            }
        }
    }
}
