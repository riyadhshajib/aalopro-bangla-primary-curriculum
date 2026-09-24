package com.example.ui

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.widget.Toast
import java.util.Locale

class AloTtsManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var onSpeechCompletedListener: (() -> Unit)? = null

    init {
        initTts()
    }

    private fun initTts() {
        try {
            // On Samsung devices (like Galaxy A50), the default engine is often Samsung TTS (com.samsung.SMT)
            // which lacks Bengali voice support. We explicitly prefer Google TTS (com.google.android.tts)
            // if available, then fallback to default system engine.
            val appContext = context.applicationContext
            val pm = appContext.packageManager
            val isGoogleTtsInstalled = try {
                pm.getPackageInfo("com.google.android.tts", 0)
                true
            } catch (_: Exception) {
                false
            }

            if (isGoogleTtsInstalled) {
                tts = TextToSpeech(appContext, this, "com.google.android.tts")
            } else {
                tts = TextToSpeech(appContext, this)
            }
        } catch (_: Exception) {
            try {
                tts = TextToSpeech(context.applicationContext, this)
            } catch (_: Exception) {}
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            configureVoiceSettings()
        } else {
            isInitialized = false
            // Retry once with default engine if specific engine failed
            try {
                tts = TextToSpeech(context.applicationContext) { retryStatus ->
                    if (retryStatus == TextToSpeech.SUCCESS) {
                        isInitialized = true
                        configureVoiceSettings()
                    }
                }
            } catch (_: Exception) {}
        }
    }

    private fun configureVoiceSettings() {
        val ttsEngine = tts ?: return

        try {
            // Set Bengali locale (prefer Bangladesh, fallback to India, fallback to general Bengali)
            val banglaBD = Locale("bn", "BD")
            val banglaIN = Locale("bn", "IN")
            val banglaGen = Locale("bn")

            var langResult = ttsEngine.setLanguage(banglaBD)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                langResult = ttsEngine.setLanguage(banglaIN)
                if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    langResult = ttsEngine.setLanguage(banglaGen)
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Fallback to default locale if Bengali is not yet downloaded
                        ttsEngine.setLanguage(Locale.getDefault())
                    }
                }
            }

            // Natural, crystal clear speech rate and pitch for learning
            ttsEngine.setPitch(1.0f)
            ttsEngine.setSpeechRate(0.95f)

            // Listen for utterance completion
            ttsEngine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}

                override fun onDone(utteranceId: String?) {
                    if (utteranceId?.startsWith("ALO_LAST_") == true || utteranceId?.startsWith("ALO_SINGLE_") == true) {
                        onSpeechCompletedListener?.invoke()
                    }
                }

                override fun onError(utteranceId: String?) {
                    if (utteranceId?.startsWith("ALO_LAST_") == true || utteranceId?.startsWith("ALO_SINGLE_") == true) {
                        onSpeechCompletedListener?.invoke()
                    }
                }
            })
        } catch (_: Exception) {
        }
    }

    /**
     * Converts educational text with math symbols, markdown, and bullet points into
     * smooth, natural, spoken Bangla sentences that flow like a real human teacher.
     */
    fun prepareTextForSpeech(rawText: String): List<String> {
        if (rawText.isBlank()) return emptyList()

        var text = rawText

        // 1. Remove markdown syntax (asterisks, hashtags, backticks, quotes, block lines, bullet dashes)
        text = text.replace(Regex("""[*#_`~>|$\\]"""), " ")
        // Remove markdown list hyphens/bullets without mistaking them for math
        text = text.replace(Regex("""^\s*[-*•–—]+\s+""", RegexOption.MULTILINE), " ")
        text = text.replace(Regex("""\s+[-–—]\s+"""), " ") // hyphens as separators/dashes

        // 2. Remove all emoji characters and miscellaneous symbols
        text = text.replace(Regex("[\\p{So}\\p{Cn}\\p{Cs}\\p{Sk}\\p{Sm}&&[^+=/×÷\\-]]"), " ")
        text = text.replace(Regex("[💡⭐🍎⚡🔬📐✨📌🎓🌍🚀🔥🎯📝⚛️🔍📖🌱🏷️🔘🔹🔸▶️✅❌💬🗣️🤖🧑‍🏫]"), " ")

        // 3. Pronounce educational units and clear math expressions naturally in Bengali
        text = text.replace("°C", " ডিগ্রি সেলসিয়াস ")
        text = text.replace("m/s²", " মিটার প্রতি সেকেন্ড স্কয়ার ")
        text = text.replace("m/s", " মিটার প্রতি সেকেন্ড ")
        text = text.replace("km/h", " কিলোমিটার প্রতি ঘণ্টা ")
        text = text.replace("kg", " কিলোগ্রাম ")
        text = text.replace("cm", " সেন্টিমিটার ")
        // Only convert + and - when surrounded by numbers or spaces as real math operations
        text = text.replace(Regex("""(?<=[0-9০-৯\s])\+(?=[0-9০-৯\s])"""), " যোগ ")
        text = text.replace(Regex("""(?<=[0-9০-৯\s])-(?=[0-9০-৯\s])"""), " বিয়োগ ")
        text = text.replace("×", " গুণ ")
        text = text.replace("÷", " ভাগ ")
        text = text.replace(Regex("""(?<=[0-9০-৯\s])=(?=[0-9০-৯\s])"""), " সমান ")
        text = text.replace("%", " শতাংশ ")
        text = text.replace("•", " ")
        text = text.replace(Regex("""[+\\-]"""), " ") // eliminate any lingering rogue operator symbols

        // 4. Split into clean sentences using Bangla Dari (।), periods, question marks, and line breaks
        val rawSentences = text.split(Regex("""[।\n\r.?!]+"""))
        val cleanChunks = mutableListOf<String>()

        for (item in rawSentences) {
            val trimmed = item.trim().replace(Regex("""\s+"""), " ")
            if (trimmed.length > 1) {
                // If a sentence is very long, split at comma or conjunction for smooth breath pauses
                if (trimmed.length > 140) {
                    val subParts = trimmed.split(Regex("""[,;]+"""))
                    for (sub in subParts) {
                        val cleanSub = sub.trim()
                        if (cleanSub.isNotBlank()) {
                            cleanChunks.add(cleanSub)
                        }
                    }
                } else {
                    cleanChunks.add(trimmed)
                }
            }
        }

        return cleanChunks
    }

    /**
     * Speaks the educational response smoothly and completely sentence-by-sentence.
     */
    fun speak(text: String, onCompleted: (() -> Unit)? = null) {
        if (!isInitialized) {
            Toast.makeText(context, "ভয়েস ইঞ্জিন প্রস্তুত হচ্ছে...", Toast.LENGTH_SHORT).show()
            return
        }

        stop()
        this.onSpeechCompletedListener = onCompleted

        val chunks = prepareTextForSpeech(text)
        if (chunks.isEmpty()) {
            onCompleted?.invoke()
            return
        }

        val ttsEngine = tts ?: return

        // Detect if the content is predominantly English
        val sample = chunks.take(3).joinToString(" ")
        var latinChars = 0
        var totalLetters = 0
        for (ch in sample) {
            if (ch.isLetter()) {
                totalLetters++
                if (ch.code in 65..90 || ch.code in 97..122) {
                    latinChars++
                }
            }
        }
        val isEnglish = totalLetters > 0 && (latinChars.toDouble() / totalLetters) > 0.45
        if (isEnglish) {
            ttsEngine.setLanguage(Locale.US)
        } else {
            val banglaBD = Locale("bn", "BD")
            var result = ttsEngine.setLanguage(banglaBD)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                result = ttsEngine.setLanguage(Locale("bn", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    result = ttsEngine.setLanguage(Locale("bn"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        ttsEngine.setLanguage(Locale.getDefault())
                    }
                }
            }
        }

        // Queue all chunks so the speech never cuts off in the middle
        chunks.forEachIndexed { index, sentence ->
            val queueMode = if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
            val isLast = index == chunks.lastIndex
            val utteranceId = if (isLast) "ALO_LAST_${System.currentTimeMillis()}" else "ALO_CHUNK_$index"

            ttsEngine.speak(sentence, queueMode, null, utteranceId)
        }
    }

    fun stop() {
        if (tts?.isSpeaking == true) {
            tts?.stop()
        }
        onSpeechCompletedListener?.invoke()
        onSpeechCompletedListener = null
    }

    fun isSpeaking(): Boolean = tts?.isSpeaking == true

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
