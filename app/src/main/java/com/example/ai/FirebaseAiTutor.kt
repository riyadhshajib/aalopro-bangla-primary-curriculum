package com.example.ai

import android.content.Context
import com.example.ai.gemini.ChatTurn
import com.google.firebase.FirebaseApp
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.RequestOptions
import com.google.firebase.ai.type.RequestTimeoutException
import com.google.firebase.ai.type.ServerException
import kotlinx.coroutines.delay

/** Managed Firebase AI Logic route; no Gemini API secret is stored in the APK. */
object FirebaseAiTutor {
    private const val MODEL = "gemini-3.5-flash-lite"

    fun isConfigured(context: Context): Boolean = FirebaseApp.getApps(context).isNotEmpty()

    suspend fun answer(query: String, history: List<ChatTurn>, languageInstruction: String): String? {
        val prompt = buildString {
            appendLine("You are AaloPro, a careful tutor for Bangladesh primary learners in classes 1-5, their parents, and teachers.")
            appendLine(languageInstruction)
            appendLine("Give a direct, age-appropriate answer. Begin with the answer itself: no greeting (including Namaskar), no self-introduction, and no filler such as 'Great question'. Obey the requested language, length, and format exactly; do not append a 'Reasoning' section or explain your answer-writing process. For arithmetic, give short working steps only when they fit the requested length. Never invent textbook quotations, page numbers, or answer keys. If uncertain, clearly say so. For medical issues involving children, do not diagnose or prescribe doses.")
            if (history.isNotEmpty()) {
                appendLine("Relevant prior exchange:")
                history.takeLast(4).forEach { turn ->
                    appendLine("Question: ${turn.query.take(1200)}")
                    appendLine("Answer: ${turn.response.take(2500)}")
                }
            }
            appendLine("Current question: $query")
        }
        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(MODEL, requestOptions = RequestOptions(timeoutInMillis = 25_000L))
        repeat(2) { attempt ->
            try {
                return model.generateContent(prompt).text?.trim()?.takeIf { it.isNotEmpty() }
            } catch (e: Exception) {
                if (attempt == 1 || (e !is RequestTimeoutException && e !is ServerException)) throw e
                delay(750L)
            }
        }
        return null
    }
}
