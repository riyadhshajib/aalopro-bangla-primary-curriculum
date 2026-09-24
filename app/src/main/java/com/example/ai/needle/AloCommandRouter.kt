package com.example.ai.needle

import android.content.Context

/** Fast rules handle common Bangla first; Needle 3 only sees ambiguous commands. */
object AloCommandRouter {
    // Base Needle 3 is bundled for controlled device evaluation, but its learned
    // fallback stays off until the multilingual acceptance benchmark passes.
    private const val ENABLE_NEEDLE_FALLBACK = false

    suspend fun classify(context: Context, input: String): NeedleIntent? {
        deterministic(input)?.let { return it }
        if (!ENABLE_NEEDLE_FALLBACK) return null
        if (!looksLikeCommand(input)) return null
        return AloNeedle3Engine.classify(context, input)
    }

    internal fun deterministic(input: String): NeedleIntent? {
        val q = input.trim().lowercase()
        val grade = when {
            Regex("(?:প্রথম|১ম|class\\s*1|ক্লাস\\s*১|ক্লাস\\s*1)").containsMatchIn(q) -> 1
            Regex("(?:দ্বিতীয়|২য়|class\\s*2|ক্লাস\\s*২|ক্লাস\\s*2)").containsMatchIn(q) -> 2
            Regex("(?:তৃতীয়|৩য়|class\\s*3|ক্লাস\\s*৩|ক্লাস\\s*3)").containsMatchIn(q) -> 3
            Regex("(?:চতুর্থ|৪র্থ|class\\s*4|ক্লাস\\s*৪|ক্লাস\\s*4)").containsMatchIn(q) -> 4
            Regex("(?:পঞ্চম|৫ম|class\\s*5|ক্লাস\\s*৫|ক্লাস\\s*5)").containsMatchIn(q) -> 5
            else -> null
        }
        val subject = when {
            listOf("গণিত", "অঙ্ক", "math").any(q::contains) -> "গণিত"
            listOf("বাংলা", "bangla").any(q::contains) -> "বাংলা"
            listOf("ইংরেজি", "english").any(q::contains) -> "ইংরেজি"
            listOf("বিজ্ঞান", "science").any(q::contains) -> "বিজ্ঞান"
            listOf("বিশ্বপরিচয়", "বিশ্বপরিচয়", "bgs").any(q::contains) -> "বাংলাদেশ ও বিশ্বপরিচয়"
            listOf("ইসলাম", "নৈতিক শিক্ষা", "islam").any(q::contains) -> "ইসলাম ও নৈতিক শিক্ষা"
            else -> null
        }
        val open = listOf("খোলো", "দেখাও", "পড়ব", "পড়ব", "open", "dekhao", "kholo", "porbo").any(q::contains)
        if (grade != null && subject != null && open) return NeedleIntent(
            "open_curriculum", mapOf("grade" to grade.toString(), "subject" to subject), 1.0)
        val quiz = listOf("কুইজ", "quiz", "test koro", "প্রশ্ন করো").any(q::contains)
        if (quiz) return NeedleIntent("start_quiz", buildMap {
            grade?.let { put("grade", it.toString()) }; subject?.let { put("subject", it) }
        }, 1.0)
        return null
    }

    private fun looksLikeCommand(input: String): Boolean {
        val q = input.lowercase()
        return input.length <= 160 && listOf(
            "খোলো", "দেখাও", "শুরু", "কুইজ", "নোট", "বুঝাও", "বোঝাও", "পড়াব",
            "open", "show", "start", "quiz", "note", "explain", "bujhao", "dekhao", "koro"
        ).any(q::contains)
    }
}
