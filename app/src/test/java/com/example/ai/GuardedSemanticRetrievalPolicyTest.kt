package com.example.ai

import com.example.data.model.OfflineBookQuestion
import com.example.data.model.VerifiedAnswerCandidate
import com.example.data.model.VerifiedAnswerCandidateProvider
import com.example.data.model.VerifiedAnswerRetrievalGate
import com.example.data.model.VerifiedAnswerRetrievalConfig
import com.example.data.model.VerifiedAnswerRetrievalResult
import org.junit.Assert.assertEquals
import org.junit.Test

class GuardedSemanticRetrievalPolicyTest {
    private val math = entry(3, "math", "তিন আর চার যোগ করলে কত?", "সাত।")
    private val science = entry(3, "science", "দিন রাত কীভাবে হয়?", "পৃথিবীর আবর্তনের কারণে।")
    private val classFourMath = entry(4, "math", "তিন আর চার যোগ করলে কত?", "সাত।")

    @Test
    fun `exact verified match wins before a higher semantic score`() {
        val gate = gate(VerifiedAnswerCandidateProvider { _, _ ->
            listOf(VerifiedAnswerCandidate(science, 0.99), VerifiedAnswerCandidate(math, 0.85))
        })
        assertEquals(
            match(math, VerifiedAnswerRetrievalResult.Kind.EXACT),
            gate.retrieve("  তিন আর চার যোগ করলে কত  ", 3, "math"),
        )
    }

    @Test
    fun `grade and subject filters apply before selection`() {
        val gate = gate(VerifiedAnswerCandidateProvider { _, _ ->
            listOf(VerifiedAnswerCandidate(math, 0.99), VerifiedAnswerCandidate(classFourMath, 0.84))
        })
        assertEquals(
            match(classFourMath, VerifiedAnswerRetrievalResult.Kind.EXACT),
            gate.retrieve("তিন আর চার যোগ করলে কত?", 4, "math"),
        )
    }

    @Test
    fun `semantic fallback requires confidence and runner up margin`() {
        val accepted = gate(VerifiedAnswerCandidateProvider { _, _ ->
            listOf(VerifiedAnswerCandidate(science, 0.88))
        }).retrieve("how does day become night", 3, "science")
        val low = gate(VerifiedAnswerCandidateProvider { _, _ ->
            listOf(VerifiedAnswerCandidate(science, 0.81))
        }).retrieve("how does day become night", 3, "science")
        val ambiguous = gate(VerifiedAnswerCandidateProvider { _, _ ->
            listOf(VerifiedAnswerCandidate(science, 0.90), VerifiedAnswerCandidate(math, 0.84))
        }).retrieve("explain this", 3)

        assertEquals(match(science, VerifiedAnswerRetrievalResult.Kind.SEMANTIC), accepted)
        assertEquals(abstain(VerifiedAnswerRetrievalResult.Reason.LOW_CONFIDENCE), low)
        assertEquals(abstain(VerifiedAnswerRetrievalResult.Reason.AMBIGUOUS), ambiguous)
    }

    @Test
    fun `provider cannot inject an unverified answer`() {
        val invented = entry(3, "bgs", "রাজধানী কী?", "বানানো উত্তর")
        val result = gate(VerifiedAnswerCandidateProvider { _, _ ->
            listOf(VerifiedAnswerCandidate(invented, 1.0))
        }).retrieve("রাজধানী কী?", 3)

        assertEquals(abstain(VerifiedAnswerRetrievalResult.Reason.NO_SEMANTIC_CANDIDATE), result)
    }

    @Test
    fun `disabled semantic provider preserves exact only behavior`() {
        val gate = VerifiedAnswerRetrievalGate(
            listOf(math),
            VerifiedAnswerCandidateProvider { _, _ -> error("must not run") },
        )

        assertEquals(
            abstain(VerifiedAnswerRetrievalResult.Reason.SEMANTIC_DISABLED),
            gate.retrieve("অন্য প্রশ্ন", 3),
        )
        assertEquals(match(math, VerifiedAnswerRetrievalResult.Kind.EXACT), gate.retrieve(math.question, 3))
    }

    @Test
    fun `provider failure abstains instead of escaping or fabricating`() {
        val gate = gate(VerifiedAnswerCandidateProvider { _, _ ->
            throw IllegalStateException("model unavailable")
        })

        assertEquals(
            abstain(VerifiedAnswerRetrievalResult.Reason.PROVIDER_FAILURE),
            gate.retrieve("how does day become night", 3, "science"),
        )
    }

    private fun gate(provider: VerifiedAnswerCandidateProvider) =
        VerifiedAnswerRetrievalGate(
            listOf(math, science, classFourMath),
            provider,
            VerifiedAnswerRetrievalConfig(semanticEnabled = true),
        )

    private fun entry(grade: Int, book: String, question: String, answer: String) =
        OfflineBookQuestion(grade, book, 1, "reviewed source", question, answer)

    private fun match(entry: OfflineBookQuestion, kind: VerifiedAnswerRetrievalResult.Kind) =
        VerifiedAnswerRetrievalResult.Match(entry, kind)

    private fun abstain(reason: VerifiedAnswerRetrievalResult.Reason) =
        VerifiedAnswerRetrievalResult.Abstain(reason)
}
