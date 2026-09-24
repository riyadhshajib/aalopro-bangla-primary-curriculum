package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val HindSiliguri = FontFamily(
  Font(R.font.hind_siliguri_regular, weight = FontWeight.Normal),
  Font(R.font.hind_siliguri_semibold, weight = FontWeight.Medium),
  Font(R.font.hind_siliguri_semibold, weight = FontWeight.SemiBold),
  Font(R.font.hind_siliguri_semibold, weight = FontWeight.Bold)
)
val AaloWordmark = FontFamily(Font(R.font.nunito_black, weight = FontWeight.Black))

// Set of Material typography styles to start with
val Typography =
  Typography(
    displaySmall = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 40.sp),
    headlineSmall = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, lineHeight = 31.sp),
    titleLarge = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.Bold, fontSize = 21.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 25.sp),
    titleSmall = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 22.sp),
    bodyLarge =
      TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
      ),
    bodyMedium = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp, letterSpacing = 0.sp),
    bodySmall = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 19.sp, letterSpacing = 0.sp),
    labelLarge = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 18.sp),
    labelSmall = TextStyle(fontFamily = HindSiliguri, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 17.sp)
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
  )
