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
    private const val PRIMARY_MODEL = "gemini-3.5-flash-lite"
    private const val FALLBACK_MODEL = "gemini-3.8-flash"

    enum class FailureCategory {
        NETWORK, TIMEOUT, SERVICE, APP_CHECK_OR_PERMISSION, QUOTA, MODEL, UNKNOWN
    }

    data class AnswerResult(
        val text: String? = null,
        val model: String? = null,
        val failureCategory: FailureCategory? = null
    )

    fun isConfigured(context: Context): Boolean = FirebaseApp.getApps(context).isNotEmpty()

    suspend fun answer(query: String, history: List<ChatTurn>, languageInstruction: String): AnswerResult {
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
        var lastFailure = FailureCategory.UNKNOWN
        listOf(PRIMARY_MODEL, FALLBACK_MODEL).forEach { modelName ->
            val model = Firebase.ai(backend = GenerativeBackend.googleAI())
                .generativeModel(modelName, requestOptions = RequestOptions(timeoutInMillis = 22_000L))
            try {
                val text = model.generateContent(prompt).text?.trim()?.takeIf { it.isNotEmpty() }
                if (text != null) return AnswerResult(text = text, model = modelName)
                lastFailure = FailureCategory.SERVICE
            } catch (e: Exception) {
                lastFailure = classifyFailure(e)
                if (lastFailure == FailureCategory.APP_CHECK_OR_PERMISSION ||
                    lastFailure == FailureCategory.QUOTA
                ) {
                    return AnswerResult(failureCategory = lastFailure)
                }
                if (modelName == PRIMARY_MODEL && (e is RequestTimeoutException || e is ServerException)) {
                    delay(500L)
                }
            }
        }
        return AnswerResult(failureCategory = lastFailure)
    }

    internal fun classifyFailure(error: Throwable): FailureCategory {
        if (error is RequestTimeoutException) return FailureCategory.TIMEOUT
        if (error is ServerException) return FailureCategory.SERVICE
        val details = generateSequence(error) { it.cause }
            .take(5)
            .joinToString(" ") { "${it.javaClass.simpleName} ${it.message.orEmpty()}" }
            .lowercase()
        return when {
            listOf("app check", "appcheck", "permission", "unauthorized", "forbidden", "403")
                .any(details::contains) -> FailureCategory.APP_CHECK_OR_PERMISSION
            listOf("quota", "rate limit", "resource exhausted", "429")
                .any(details::contains) -> FailureCategory.QUOTA
            listOf("model", "not found", "404")
                .any(details::contains) -> FailureCategory.MODEL
            listOf("network", "unable to resolve host", "connection", "socket", "offline")
                .any(details::contains) -> FailureCategory.NETWORK
            listOf("timeout", "timed out").any(details::contains) -> FailureCategory.TIMEOUT
            else -> FailureCategory.UNKNOWN
        }
    }
}
