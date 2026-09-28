package com.example.data.model

data class VerifiedAnswerCandidate(val entry: OfflineBookQuestion, val score: Double)

/** Semantic retrieval is opt-in. Production stays exact-only unless this flag is enabled. */
data class VerifiedAnswerRetrievalConfig(
    val semanticEnabled: Boolean = false,
    val minimumConfidence: Double = 0.82,
    val minimumMargin: Double = 0.08,
)

/** Optional ranker. It may rank verified entries, but cannot supply answer text. */
fun interface VerifiedAnswerCandidateProvider {
    fun rank(query: String, eligibleEntries: List<OfflineBookQuestion>): List<VerifiedAnswerCandidate>
}

sealed class VerifiedAnswerRetrievalResult {
    data class Match(val entry: OfflineBookQuestion, val kind: Kind) : VerifiedAnswerRetrievalResult()
    data class Abstain(val reason: Reason) : VerifiedAnswerRetrievalResult()

    enum class Kind { EXACT, SEMANTIC }
    enum class Reason {
        NO_ELIGIBLE_ENTRY,
        SEMANTIC_DISABLED,
        NO_SEMANTIC_CANDIDATE,
        PROVIDER_FAILURE,
        LOW_CONFIDENCE,
        AMBIGUOUS,
    }
}

/** Exact-first safety boundary for learner-visible verified answers. */
class VerifiedAnswerRetrievalGate(
    private val verifiedEntries: List<OfflineBookQuestion>,
    private val candidateProvider: VerifiedAnswerCandidateProvider? = null,
    private val config: VerifiedAnswerRetrievalConfig = VerifiedAnswerRetrievalConfig(),
) {
    init {
        require(config.minimumConfidence in 0.0..1.0)
        require(config.minimumMargin in 0.0..1.0)
    }

    fun retrieve(query: String, grade: Int, book: String? = null): VerifiedAnswerRetrievalResult {
        val eligible = verifiedEntries.filter { entry ->
            entry.grade == grade && (book == null || entry.book == book)
        }
        if (eligible.isEmpty()) return abstain(VerifiedAnswerRetrievalResult.Reason.NO_ELIGIBLE_ENTRY)

        val wanted = matchKey(query)
        eligible.firstOrNull { matchKey(it.question) == wanted }?.let { entry ->
            return VerifiedAnswerRetrievalResult.Match(entry, VerifiedAnswerRetrievalResult.Kind.EXACT)
        }

        if (!config.semanticEnabled) {
            return abstain(VerifiedAnswerRetrievalResult.Reason.SEMANTIC_DISABLED)
        }
        val provider = candidateProvider
            ?: return abstain(VerifiedAnswerRetrievalResult.Reason.NO_SEMANTIC_CANDIDATE)
        val allowedEntries = eligible.toSet()
        val candidates = try {
            provider.rank(query, eligible)
        } catch (_: Exception) {
            return abstain(VerifiedAnswerRetrievalResult.Reason.PROVIDER_FAILURE)
        }
        val ranked = candidates
            .asSequence()
            .filter { it.entry in allowedEntries && it.score.isFinite() && it.score in 0.0..1.0 }
            .distinctBy { it.entry }
            .sortedByDescending { it.score }
            .toList()
        val first = ranked.firstOrNull()
            ?: return abstain(VerifiedAnswerRetrievalResult.Reason.NO_SEMANTIC_CANDIDATE)
        if (first.score < config.minimumConfidence) {
            return abstain(VerifiedAnswerRetrievalResult.Reason.LOW_CONFIDENCE)
        }
        val runnerUp = ranked.getOrNull(1)
        if (runnerUp != null && first.score - runnerUp.score < config.minimumMargin) {
            return abstain(VerifiedAnswerRetrievalResult.Reason.AMBIGUOUS)
        }
        return VerifiedAnswerRetrievalResult.Match(
            first.entry,
            VerifiedAnswerRetrievalResult.Kind.SEMANTIC,
        )
    }

    private fun abstain(reason: VerifiedAnswerRetrievalResult.Reason) =
        VerifiedAnswerRetrievalResult.Abstain(reason)

    private fun matchKey(value: String): String = value.lowercase()
        .replace(Regex("[^\\p{L}\\p{N}]+"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}
