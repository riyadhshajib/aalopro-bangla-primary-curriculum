package com.example.ai.retrieval

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.extensions.OrtxPackage
import android.content.Context
import android.app.ActivityManager
import android.os.Build
import android.os.Process
import android.util.Log
import com.example.data.model.OfflineBookQuestion
import com.example.data.model.VerifiedAnswerCandidate
import com.example.data.model.VerifiedAnswerCandidateProvider
import com.example.data.model.VerifiedOfflineQuestionBank
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.LongBuffer
import kotlin.math.sqrt

/** Arm64-only experimental semantic ranker. The retrieval gate remains the safety boundary. */
class E5VerifiedAnswerCandidateProvider private constructor(context: Context) :
    VerifiedAnswerCandidateProvider {
    private val appContext = context.applicationContext
    private val environment by lazy { OrtEnvironment.getEnvironment() }
    private val lock = Any()
    @Volatile private var runtime: Runtime? = null

    override fun rank(
        query: String,
        eligibleEntries: List<OfflineBookQuestion>,
    ): List<VerifiedAnswerCandidate> {
        val supportedDevice = isSupportedDevice()
        val supportedLanguage = isSupportedLanguage(query)
        if (!supportedDevice || !supportedLanguage) {
            Log.i(
                TAG,
                "abstain device=$supportedDevice language=$supportedLanguage " +
                    "sdk=${Build.VERSION.SDK_INT} is64=${Process.is64Bit()} " +
                    "abis=${Build.SUPPORTED_ABIS.joinToString()}",
            )
            return emptyList()
        }
        return try {
            synchronized(lock) {
                val active = runtime ?: loadRuntime().also { runtime = it }
                val queryVector = active.embed(query)
                eligibleEntries.mapNotNull { entry ->
                    val index = VerifiedOfflineQuestionBank.entries.indexOf(entry)
                    if (index < 0) null else VerifiedAnswerCandidate(
                        entry,
                        cosine(queryVector, active.passages[index]).toDouble(),
                    )
                }.sortedByDescending { it.score }.also { ranked ->
                    Log.i(TAG, "ranked language=bn_or_en top=${ranked.firstOrNull()?.score ?: -1.0}")
                }
            }
        } catch (failure: Throwable) {
            Log.e(TAG, "semantic rank failed", failure)
            emptyList()
        }
    }

    private fun loadRuntime(): Runtime {
        check(VerifiedOfflineQuestionBank.entries.size == PASSAGE_COUNT) {
            "Embedding corpus mismatch: expected $PASSAGE_COUNT, found ${VerifiedOfflineQuestionBank.entries.size}"
        }
        val tokenizerFile = copyAsset("e5/tokenizer.onnx", TOKENIZER_BYTES)
        val encoderFile = copyAsset("e5/encoder.onnx", ENCODER_BYTES)
        val tokenizerOptions = OrtSession.SessionOptions().apply {
            registerCustomOpLibrary(OrtxPackage.getLibraryPath())
            setIntraOpNumThreads(1)
        }
        val encoderOptions = OrtSession.SessionOptions().apply {
            setIntraOpNumThreads(2)
            setInterOpNumThreads(1)
            setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT)
        }
        return Runtime(
            environment.createSession(tokenizerFile.absolutePath, tokenizerOptions),
            environment.createSession(encoderFile.absolutePath, encoderOptions),
            loadPassages(),
        ).also { Log.i(TAG, "runtime loaded entries=$PASSAGE_COUNT") }
    }

    private fun loadPassages(): Array<FloatArray> {
        val bytes = appContext.assets.open("e5/verified_passages.f32").use { it.readBytes() }
        check(bytes.size == PASSAGE_COUNT * DIMENSIONS * Float.SIZE_BYTES)
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer()
        return Array(PASSAGE_COUNT) { FloatArray(DIMENSIONS).also(buffer::get) }
    }

    private fun copyAsset(path: String, expectedBytes: Long): File {
        val output = File(appContext.filesDir, path.substringAfterLast('/'))
        if (output.length() == expectedBytes) return output
        val temporary = File(output.parentFile, "${output.name}.part")
        appContext.assets.open(path).use { input ->
            temporary.outputStream().buffered().use(input::copyTo)
        }
        check(temporary.length() == expectedBytes)
        check(temporary.renameTo(output) || temporary.copyTo(output, overwrite = true).length() == expectedBytes)
        temporary.delete()
        return output
    }

    private fun isSupportedDevice(): Boolean {
        val memory = appContext.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val info = ActivityManager.MemoryInfo()
        memory?.getMemoryInfo(info)
        val hasEnoughMemory = memory?.isLowRamDevice != true &&
            (info.totalMem == 0L || info.totalMem >= MIN_TOTAL_MEMORY_BYTES)
        return Build.VERSION.SDK_INT >= 24 && Process.is64Bit() &&
            Build.SUPPORTED_ABIS.contains("arm64-v8a") && hasEnoughMemory
    }

    private inner class Runtime(
        private val tokenizer: OrtSession,
        private val encoder: OrtSession,
        val passages: Array<FloatArray>,
    ) {
        fun embed(query: String): FloatArray {
            val ids = tokenize("query: ${query.trim()}")
            val shape = longArrayOf(1, ids.size.toLong())
            val mask = LongArray(ids.size) { 1L }
            val types = LongArray(ids.size)
            OnnxTensor.createTensor(environment, LongBuffer.wrap(ids), shape).use { idTensor ->
                OnnxTensor.createTensor(environment, LongBuffer.wrap(mask), shape).use { maskTensor ->
                    OnnxTensor.createTensor(environment, LongBuffer.wrap(types), shape).use { typeTensor ->
                        val inputs = mapOf(
                            "input_ids" to idTensor,
                            "attention_mask" to maskTensor,
                            "token_type_ids" to typeTensor,
                        )
                        encoder.run(inputs).use { result ->
                            @Suppress("UNCHECKED_CAST")
                            val hidden = result[0].value as Array<Array<FloatArray>>
                            val pooled = FloatArray(DIMENSIONS)
                            hidden[0].forEach { token ->
                                for (dimension in pooled.indices) pooled[dimension] += token[dimension]
                            }
                            for (dimension in pooled.indices) pooled[dimension] /= hidden[0].size
                            normalize(pooled)
                            return pooled
                        }
                    }
                }
            }
        }

        private fun tokenize(text: String): LongArray {
            OnnxTensor.createTensor(environment, arrayOf(text)).use { input ->
                tokenizer.run(mapOf("text" to input)).use { result ->
                    val raw = result[0].value as IntArray
                    val usable = raw.take(MAX_TOKENS - 2)
                    return LongArray(usable.size + 2) { index ->
                        when (index) {
                            0 -> 0L
                            usable.size + 1 -> 2L
                            else -> usable[index - 1].toLong() + 1L
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AALO_E5"
        private const val PASSAGE_COUNT = 2081
        private const val DIMENSIONS = 384
        private const val MAX_TOKENS = 192
        private const val ENCODER_BYTES = 118_054_593L
        private const val TOKENIZER_BYTES = 5_069_499L
        private const val MIN_TOTAL_MEMORY_BYTES = 3L * 1024L * 1024L * 1024L
        @Volatile private var instance: E5VerifiedAnswerCandidateProvider? = null

        fun get(context: Context): E5VerifiedAnswerCandidateProvider = instance
            ?: synchronized(this) {
                instance ?: E5VerifiedAnswerCandidateProvider(context).also { instance = it }
            }

        internal fun isSupportedLanguage(query: String): Boolean {
            val value = query.trim().lowercase()
            if (value.isBlank()) return false
            val hasBangla = value.any { it in '\u0980'..'\u09ff' }
            val hasLatin = value.any { it in 'a'..'z' }
            if (hasBangla) return !hasLatin
            if (!hasLatin || !value.all { it.isLetterOrDigit() || it.isWhitespace() || it in "?'.,!-" }) return false
            val words = Regex("[a-z]+(?:'[a-z]+)?").findAll(value).map { it.value }.toList()
            val banglishMarkers = setOf("ki", "kivabe", "keno", "hoy", "ke", "kothay", "bolo", "bujhao", "raat", "din")
            if (words.any(banglishMarkers::contains)) return false
            val englishMarkers = setOf("what", "why", "who", "where", "when", "how", "which", "is", "are", "does", "do", "the")
            return words.any(englishMarkers::contains)
        }

        private fun cosine(left: FloatArray, right: FloatArray): Float {
            var sum = 0f
            for (index in left.indices) sum += left[index] * right[index]
            return sum.coerceIn(0f, 1f)
        }

        private fun normalize(vector: FloatArray) {
            val norm = sqrt(vector.sumOf { (it * it).toDouble() }).toFloat()
            check(norm.isFinite() && norm > 0f)
            for (index in vector.indices) vector[index] /= norm
        }
    }
}
