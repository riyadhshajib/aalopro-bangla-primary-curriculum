package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.screens.DigitText as Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AaloWordmark
import kotlinx.coroutines.delay

@Composable
fun AaloSplashScreen(onSplashFinished: () -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(2200)
        onSplashFinished()
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFFCF5), Color(0xFFFFF9E9))))
            .testTag("aalo_splash_screen")
    ) {
        val compact = maxHeight < 620.dp || maxWidth < 330.dp
        val markSize = if (compact) 248.dp else 300.dp
        val wordmarkSize = if (compact) 36.sp else 46.sp

        // Decoration belongs to the page, never to a disc behind the logo.
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val top = Path().apply {
                moveTo(0f, 0f)
                lineTo(w * .54f, 0f)
                cubicTo(w * .43f, h * .075f, w * .18f, h * .055f, 0f, h * .15f)
                close()
            }
            drawPath(top, Brush.linearGradient(listOf(Color(0xFFFFE18D), Color(0xFFFFBC37))))

            val backWave = Path().apply {
                moveTo(0f, h * .79f)
                cubicTo(w * .37f, h * .78f, w * .53f, h * .91f, w, h * .82f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(backWave, Color(0xFFFFDD80))
            val frontWave = Path().apply {
                moveTo(0f, h * .86f)
                cubicTo(w * .30f, h * .84f, w * .52f, h * .98f, w, h * .89f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(frontWave, Brush.horizontalGradient(listOf(Color(0xFF62A790), Color(0xFFACD7AD))))
            drawCircle(Color(0xFFFFE5A4).copy(alpha = .6f), w * .018f, Offset(w * .12f, h * .48f))
            drawCircle(Color(0xFFFFE5A4).copy(alpha = .6f), w * .012f, Offset(w * .16f, h * .46f))
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.aalo_splash_mark),
                contentDescription = "AaloPro লোগো",
                modifier = Modifier.size(markSize)
            )
            Spacer(Modifier.height(if (compact) 4.dp else 10.dp))
            Text(
                "AaloPro", color = Color(0xFF20312F), fontSize = wordmarkSize,
                fontFamily = AaloWordmark, fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "শেখা, প্রশ্ন আর আবিষ্কারের সঙ্গী", color = Color(0xFF536965),
                fontSize = if (compact) 12.sp else 14.sp,
                fontWeight = FontWeight.Medium, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(if (compact) 28.dp else 42.dp))
            Box(
                Modifier.width(if (compact) 106.dp else 132.dp).height(4.dp)
                    .background(Color(0xFFFFE8B4), RoundedCornerShape(50))
            ) {
                Box(Modifier.width(if (compact) 56.dp else 72.dp).height(4.dp)
                    .background(Color(0xFFF5B900), RoundedCornerShape(50)))
            }
        }
    }
}
