package com.example.ai

import com.example.data.local.AloChatEntity
import com.example.util.AloTextSanitizer
import java.util.Locale

/** A conservative offline continuation when the contextual online tutor is unavailable. */
object AloFollowUpExplainer {

    fun isFollowUpSimplificationRequest(query: String): Boolean {
        val q = query.lowercase(Locale.ROOT).replace(Regex("[?!.।,]+"), " ").trim()
        if (q in setOf("কেন", "কীভাবে", "কিভাবে", "মানে", "কি", "কী",
                "why", "how", "meaning", "keno", "kivabe", "mane")) return true
        if (listOf("কেন ", "কীভাবে ", "কিভাবে ", "why ", "how ", "keno ", "kivabe ")
                .any(q::startsWith)) return true
        return listOf(
            "বুঝিয়ে", "বুঝিয়ে", "বুঝায়", "বুঝিনা", "বুঝি না", "বুঝিনি",
            "বুঝলাম না", "বুঝতে পারছি না", "কী বললে", "কি বললে", "আবার বলো",
            "সহজ", "উদাহরণ", "বিস্তারিত", "ভেঙ্গে", "ভেঙে", "আরেকবার",
            "আরেকটু", "খুলে বলো", "টিপস", "explain", "simplify", "example",
            "details", "what did you say", "say it again", "i don't understand",
            "i do not understand", "didn't understand", "did not understand",
            "bujhiye", "bujhie", "bujhao", "bujhai", "bujhina", "bujhi na",
            "bujhini", "bujhlam na", "bujte parini", "bujhte parini",
            "bujhao", "bujhai dao", "bujhaye dao", "ki bolle", "ki bolcho",
            "abar bolo", "aro bujhao", "aro bujhiye dao", "bistarito",
            "sohoj", "arekbar", "arektu", "ar ektu"
        ).any(q::contains)
    }

    fun generateFollowUpExplanation(followUpQuery: String, parentChat: AloChatEntity): String {
        val earlier = AloTextSanitizer.cleanText(parentChat.responseBangla)
        if (earlier.isBlank() || earlier.contains("যাচাইকৃত উত্তর নেই") ||
            earlier.startsWith("একটি ত্রুটি ঘটেছে")) {
            return "আগের উত্তরের নির্ভরযোগ্য ব্যাখ্যা আমার কাছে নেই। প্রশ্নটি নির্দিষ্ট করে আবার লিখুন; ইন্টারনেট সংযোগ থাকলে আলো শিক্ষককে জিজ্ঞাসা করুন।"
        }

        // Reuse only the actual prior answer. The former generic templates
        // inserted unrelated equations and science examples into follow-ups.
        val excerpt = earlier.lineSequence().filter(String::isNotBlank)
            .take(8).joinToString("\n").take(1400)
        return "আগের উত্তরে বলা হয়েছিল:\n$excerpt\n\nকোন শব্দ বা ধাপটি বুঝতে অসুবিধা হচ্ছে, সেটি লিখো। যাচাই করা ব্যাখ্যা না থাকলে আমি অনুমান করে উত্তর দেব না।"
    }
}
