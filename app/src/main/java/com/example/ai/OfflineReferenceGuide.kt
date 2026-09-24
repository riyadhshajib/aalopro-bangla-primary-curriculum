package com.example.ai

import com.example.data.model.FormulaRepository
import com.example.data.model.QuizRepository

/** Exact, curated answers for the reference cards exposed by the MVP. */
object OfflineReferenceGuide {
    fun quizAnswer(query: String, previousQuestion: String? = null): String? {
        val question = QuizRepository.questions.firstOrNull {
            it.questionBangla.trim() == query.trim()
        } ?: if (previousQuestion != null && AloFollowUpExplainer.isFollowUpSimplificationRequest(query)) {
            QuizRepository.questions.firstOrNull { it.questionBangla.trim() == previousQuestion.trim() }
        } else null
        question ?: return null
        val answer = question.options.getOrNull(question.correctIndex) ?: return null
        return buildString {
            append("প্রশ্ন: ").append(question.questionBangla)
            append("\n\nসঠিক উত্তর: ").append(answer)
            append("\n\nকেন: ").append(question.explanationBangla)
            if (query.trim() != question.questionBangla.trim()) {
                append("\n\nকোন অংশটি বুঝতে অসুবিধা হচ্ছে? সেটি লিখলে ওই অংশ ধরে আবার বোঝাতে পারব।")
            }
        }
    }

    fun formulaAnswer(chapterContext: String, query: String, isFollowUp: Boolean): String? {
        val formula = FormulaRepository.formulas.firstOrNull {
            it.titleBangla.trim() == chapterContext.trim()
        } ?: return null
        val relevant = query.trim() == formula.relatedQuery.trim() ||
            query.trim() == formula.titleBangla.trim() ||
            (isFollowUp && AloFollowUpExplainer.isFollowUpSimplificationRequest(query))
        if (!relevant) return null
        return buildString {
            if (isFollowUp) append("আবার ধাপে ধাপে বুঝি।\n\n")
            append(formula.titleBangla)
            append("\n\nনিয়ম: ").append(formula.formulaSymbol)
            append("\n\nব্যাখ্যা: ").append(formula.explanationBangla)
            append("\n\n").append(formula.workedExampleBangla)
            if (isFollowUp) append("\n\nসূত্রে উদাহরণের সংখ্যাগুলো বসিয়ে প্রতিটি ধাপ মিলিয়ে দেখো। কোন ধাপটি আটকে গেছে লিখতে পারো।")
        }
    }
}
