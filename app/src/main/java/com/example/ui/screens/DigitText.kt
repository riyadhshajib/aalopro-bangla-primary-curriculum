package com.example.ui.screens

import androidx.compose.material3.Text as MaterialText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.R

private val clearBanglaDigits = FontFamily(Font(R.font.noto_sans_bengali_numbers))

/** Preserve existing Bengali typography; change only Bengali numeral glyphs. */
internal fun readableDigits(value: String): AnnotatedString = buildAnnotatedString {
    append(value)
    value.forEachIndexed { index, char ->
        if (char in '০'..'৯') addStyle(SpanStyle(fontFamily = clearBanglaDigits), index, index + 1)
    }
}

@Composable
fun DigitText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = TextStyle.Default
) {
    MaterialText(readableDigits(text), modifier, color, fontSize, fontStyle, fontWeight,
        fontFamily, letterSpacing, textDecoration, textAlign, lineHeight, overflow,
        softWrap, maxLines, minLines, style = style)
}
