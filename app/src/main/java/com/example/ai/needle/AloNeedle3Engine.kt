package com.example.ai.needle

import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class NeedleIntent(
    val function: String,
    val arguments: Map<String, String>,
    val confidence: Double
)

internal object AloNeedle3Native {
    private val loaded = runCatching { System.loadLibrary("aalo_needle3") }.isSuccess
    fun isLoaded(): Boolean = loaded
    external fun loadAndInit(assetManager: AssetManager, systemPrompt: String, toolsJson: String): String?
    external fun complete(input: String, maxTokens: Int): String
    external fun reset()
}

/**
 * Small on-device intent model. It never writes curriculum answers: every result is
 * treated as an untrusted routing suggestion and must pass the allowlist below.
 */
object AloNeedle3Engine {
    private val mutex = Mutex()
    @Volatile private var initialized = false
    @Volatile private var disabledReason: String? = null
    private val allowed = setOf(
        "open_curriculum", "explain_again", "start_quiz", "open_parent_guide",
        "open_teacher_guide", "save_note", "ask_general_online"
    )

    suspend fun classify(context: Context, input: String): NeedleIntent? = withContext(Dispatchers.IO) {
        if (input.isBlank() || input.length > 240 || disabledReason != null) return@withContext null
        mutex.withLock {
            if (!ensureInitialized(context.applicationContext)) return@withLock null
            val raw = runCatching { AloNeedle3Native.complete(input, 192) }
                .getOrElse { disabledReason = it.message ?: "native completion failed"; return@withLock null }
            parseAndValidate(raw, input)
        }
    }

    fun status(): String = when {
        initialized -> "ready"
        disabledReason != null -> "disabled: $disabledReason"
        else -> "not_loaded"
    }

    private fun ensureInitialized(context: Context): Boolean {
        if (initialized) return true
        if (!AloNeedle3Native.isLoaded()) {
            disabledReason = "native library unavailable"
            return false
        }
        return runCatching {
            val system = context.assets.open("needle3/system.txt").bufferedReader().use { it.readText() }
            val tools = context.assets.open("needle3/tools.json").bufferedReader().use { it.readText() }
            val error = AloNeedle3Native.loadAndInit(context.assets, system, tools)
            if (!error.isNullOrBlank()) error(error)
            initialized = true
            true
        }.getOrElse {
            disabledReason = it.message ?: "initialization failed"
            false
        }
    }

    internal fun parseAndValidate(raw: String, originalInput: String): NeedleIntent? {
        val root = runCatching { JSONObject(raw) }.getOrNull() ?: return null
        if (!root.optBoolean("success", false)) return null
        if (root.optDouble("confidence", 0.0) < 0.72) return null
        val validation = root.optJSONObject("validation")
        if (validation?.optJSONArray("ungrounded")?.length()?.let { it > 0 } == true) return null
        val calls = root.optJSONArray("function_calls") ?: return null
        if (calls.length() != 1) return null
        val call = calls.optJSONObject(0) ?: return null
        var name = call.optString("name")
        val argsJson = call.optJSONObject("arguments") ?: JSONObject()
        if (name == "route_aalopro_action") {
            name = when (argsJson.optString("action")) {
                "parent_guide" -> "open_parent_guide"
                "teacher_guide" -> "open_teacher_guide"
                "general_online" -> "ask_general_online"
                else -> argsJson.optString("action")
            }
        }
        if (name !in allowed) return null
        val args = buildMap {
            argsJson.keys().forEach { key -> if (key != "action") put(key, argsJson.opt(key)?.toString().orEmpty()) }
        }
        val grade = args["grade"]?.toIntOrNull()
        if (grade != null && grade !in 1..5) return null
        // Destructive or data-bearing tools must remain grounded in the exact input.
        if (name == "save_note" && args["text"]?.let { !originalInput.contains(it, ignoreCase = true) } != false) return null
        return NeedleIntent(name, args, root.optDouble("confidence"))
    }
}
