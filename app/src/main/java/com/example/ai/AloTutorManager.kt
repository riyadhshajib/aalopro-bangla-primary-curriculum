package com.example.ai

import android.content.Context
import com.example.ai.gemini.ChatTurn
import com.example.data.local.AloChatEntity
import com.example.data.model.BangladeshCurriculumRepository
import com.example.data.model.CurriculumPedagogyEngine
import com.example.data.model.CurriculumTopic
import com.example.data.model.FullOfflineQuestionBank
import com.example.util.AloTextSanitizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class ChatAiMode(val title: String, val subtitle: String) {
    GENERAL(
        title = "জেনারেল এআই",
        subtitle = "যেকোনো বিষয়, ব্যাকরণ ও সাধারণ জিজ্ঞাসা"
    ),
    CURRICULUM(
        title = "অফলাইন পাঠ সহায়িকা",
        subtitle = "পাঠ্যবইয়ের প্রশ্ন, অনুশীলনী, ছবি ও ব্যাখ্যা"
    )
}

data class TutorResponse(
    val query: String,
    val text: String,
    val subjectTag: String,
    val isOnlineGemini: Boolean = false,
    val modelLabel: String = "অফলাইন পাঠ সহায়িকা",
    val isFollowUpResponse: Boolean = false
)

object AloTutorManager {

    /** Routes verified local answers first and uses cloud AI only when configured. */
    suspend fun answerQuestion(
        context: Context,
        query: String,
        mode: ChatAiMode,
        section: StudySection = StudySection.UNIVERSAL,
        history: List<ChatTurn> = emptyList(),
        parentFollowUp: AloChatEntity? = null,
        chapterContext: String? = null
    ): TutorResponse = withContext(Dispatchers.IO) {
        val cleanQuery = query.trim()
        val isFollowUp = parentFollowUp != null

        // The studio's visible starter questions are curated local guides, not
        // arbitrary open prompts. Keep their answer stable with or without internet.
        if (mode == ChatAiMode.GENERAL) {
            OfflineReferenceGuide.quizAnswer(cleanQuery, chapterContext ?: parentFollowUp?.query)?.let { answer ->
                return@withContext TutorResponse(cleanQuery, answer, "কুইজ সমাধান",
                    isOnlineGemini = false, modelLabel = "অফলাইন কুইজ সহায়িকা",
                    isFollowUpResponse = isFollowUp)
            }
            StudioPromptGuide.answerFor(cleanQuery, chapterContext ?: parentFollowUp?.query)?.let { answer ->
                return@withContext TutorResponse(cleanQuery, answer, "অভিভাবক ও শিক্ষক সহায়িকা",
                    isOnlineGemini = false, modelLabel = "অফলাইন স্টুডিও সহায়িকা",
                    isFollowUpResponse = isFollowUp)
            }
        }

        // The MVP's three visible lessons per subject have an explicit offline
        // answer path. Do not let a broad keyword matcher replace their answer.
        if (mode == ChatAiMode.CURRICULUM && !chapterContext.isNullOrBlank()) {
            val bankGrade = section.id.removePrefix("CLASS_").toIntOrNull()
            if (bankGrade != null) {
                val bankQuestion = FullOfflineQuestionBank.exact(
                    bankGrade,
                    if (isFollowUp) parentFollowUp?.query.orEmpty() else cleanQuery
                )
                if (bankQuestion != null) {
                    val lead = if (isFollowUp)
                        "আরও সহজ করে বুঝি: প্রশ্নটি ছোট অংশে ভাগ করো, পৃষ্ঠার ছবি বা দেওয়া তথ্য চিহ্নিত করো, তারপর এক ধাপ করে উত্তর তৈরি করো।\n\n"
                    else ""
                    val reply = "$lead${bankQuestion.answer}\n\nউৎস: ${bankQuestion.source}, PDF পৃষ্ঠা ${bankQuestion.page}"
                    return@withContext TutorResponse(cleanQuery, AloTextSanitizer.cleanText(reply),
                        "যাচাইকৃত অফলাইন প্রশ্নব্যাংক", isOnlineGemini = false,
                        modelLabel = "২০২৬ NCTB অফলাইন পাঠ সহায়িকা", isFollowUpResponse = isFollowUp)
                }
            }
            OfflineReferenceGuide.formulaAnswer(chapterContext, cleanQuery, isFollowUp)?.let { answer ->
                return@withContext TutorResponse(cleanQuery, AloTextSanitizer.cleanText(answer), "সূত্রের ব্যাখ্যা",
                    isOnlineGemini = false, modelLabel = "অফলাইন সূত্র সহায়িকা",
                    isFollowUpResponse = isFollowUp)
            }
            val grade = section.id.removePrefix("CLASS_").toIntOrNull()
            val gradeTopics = BangladeshCurriculumRepository.featuredClassLevels.asSequence()
                .filter { grade == null || it.levelNumber == grade }
                .flatMap { it.subjects.asSequence() }
                .flatMap { it.topics.asSequence() }
                .toList()
            // A chapter title can legitimately occur in more than one subject
            // (for example, ধর্মীয় সম্প্রীতি in BGS and Islam). The visible,
            // book-qualified question is therefore the strongest identifier.
            if (!isFollowUp) {
                gradeTopics.firstNotNullOfOrNull { topic ->
                    topic.qaList.firstOrNull { it.question.trim().equals(cleanQuery, ignoreCase = true) }
                        ?.let { topic to it }
                }?.let { (topic, qa) ->
                    val reply = "${topic.titleBangla}\n\nপ্রশ্ন: ${qa.question}\n\nউত্তর: ${qa.answer}\n\nব্যাখ্যা: ${topic.detailsBangla}"
                    return@withContext TutorResponse(cleanQuery, AloTextSanitizer.cleanText(reply),
                        "বাছাই করা টপিক", isOnlineGemini = false,
                        modelLabel = "যাচাইকৃত অফলাইন পাঠ সহায়িকা")
                }
            }
            val featured = gradeTopics.asSequence()
                .firstOrNull { it.titleBangla.equals(chapterContext, ignoreCase = true) }
            if (featured != null) {
                // A visible curated question is authoritative. Check it before
                // support-keyword routing because a legitimate lesson title can
                // itself contain words such as "সমস্যা" or "অনুশীলন".
                val exactQa = featured.qaList.firstOrNull {
                    it.question.trim().equals(cleanQuery, ignoreCase = true)
                }
                if (exactQa != null && !isFollowUp) {
                    val reply = "${featured.titleBangla}\n\nপ্রশ্ন: ${exactQa.question}\n\nউত্তর: ${exactQa.answer}\n\nব্যাখ্যা: ${featured.detailsBangla}"
                    return@withContext TutorResponse(
                        cleanQuery,
                        AloTextSanitizer.cleanText(reply),
                        "বাছাই করা টপিক",
                        isOnlineGemini = false,
                        modelLabel = "যাচাইকৃত অফলাইন পাঠ সহায়িকা"
                    )
                }
                chapterSupportReply(chapterContext, cleanQuery, section)?.let { reply ->
                    return@withContext TutorResponse(cleanQuery, AloTextSanitizer.cleanText(reply), "টপিক সহায়িকা", isFollowUpResponse = isFollowUp)
                }
                val requestedPage = extractPdfPage(cleanQuery)
                if (requestedPage != null && featured.visualExercises.isNotEmpty()) {
                    val exercise = featured.visualExercises.firstOrNull {
                        extractPdfPage(it.sourceBangla) == requestedPage
                    }
                    if (exercise != null && exercise.pageQuestions.isNotEmpty()) {
                        val reviewedAnswers = exercise.pageQuestions.joinToString("\n\n") { qa ->
                            "প্রশ্ন: ${qa.question}\nউত্তর: ${qa.answer}"
                        }
                        return@withContext TutorResponse(
                            cleanQuery,
                            AloTextSanitizer.cleanText("$reviewedAnswers\n\nউৎস: ${exercise.sourceBangla}"),
                            "যাচাইকৃত ছবি অনুশীলনী",
                            isFollowUpResponse = isFollowUp
                        )
                    }
                    if (exercise != null && exercise.imageResId != 0) {
                        return@withContext TutorResponse(
                            cleanQuery,
                            AloTextSanitizer.cleanText("${exercise.answerBangla}\n\nউৎস: ${exercise.sourceBangla}"),
                            "যাচাইকৃত ছবি অনুশীলনী",
                            isFollowUpResponse = isFollowUp
                        )
                    }
                    return@withContext TutorResponse(
                        cleanQuery,
                        "এই অধ্যায়ের PDF পৃষ্ঠা ${toBanglaDigits(requestedPage)}-এর ছবি-অনুশীলনী এখনো আলাদাভাবে যাচাই করা হয়নি। যাচাই ছাড়া অনুমান করে উত্তর দিচ্ছি না।",
                        "যাচাইকৃত পাঠ প্রয়োজন",
                        isFollowUpResponse = isFollowUp
                    )
                }
                // These specialised engines contain Class One patterns such as
                // small-number and alphabet matches. Never let them intercept a
                // similarly worded lesson from another grade.
                if (section == StudySection.CLASS_1) {
                    Class1MathAnswerEngine.answer(chapterContext, cleanQuery)?.let { answer ->
                        return@withContext TutorResponse(
                            cleanQuery,
                            AloTextSanitizer.cleanText(answer),
                            "প্রথম শ্রেণির গণিত",
                            isOnlineGemini = false,
                            modelLabel = "যাচাইকৃত অফলাইন গণিত সহায়িকা",
                            isFollowUpResponse = isFollowUp
                        )
                    }
                    Class1LanguageAnswerEngine.answer(chapterContext, cleanQuery)?.let { answer ->
                        return@withContext TutorResponse(
                            cleanQuery,
                            AloTextSanitizer.cleanText(answer),
                            "প্রথম শ্রেণির ভাষা শিক্ষা",
                            isOnlineGemini = false,
                            modelLabel = "যাচাইকৃত অফলাইন পাঠ সহায়িকা",
                            isFollowUpResponse = isFollowUp
                        )
                    }
                }
                // Keep a simplification follow-up attached to the question the
                // learner selected; matching the words in "আরেকভাবে বোঝাও" can
                // otherwise pick a different canned question from this lesson.
                val qa = bestQuestionAnswer(featured, parentFollowUp?.query ?: cleanQuery)
                val simplified = AloFollowUpExplainer.isFollowUpSimplificationRequest(cleanQuery)
                if (isFollowUp && simplified) {
                    if (featured.id == "c1_m1" && qa?.question?.contains("আম") == true) {
                        val reply = "চলো আমগুলো গুনে দেখি। প্রথমে ৫টি আম আছে: ১, ২, ৩, ৪, ৫। " +
                            "এর সঙ্গে আরও ৩টি আম রাখলে পরের সংখ্যা ৬, ৭, ৮। " +
                            "তাই ৫ + ৩ = ৮; মোট ৮টি আম। " +
                            "তুমি চাইলে ৫টি ও ৩টি আলাদা দাগ এঁকে সব দাগ একসঙ্গে গুনেও ৮ পাবে।"
                        return@withContext TutorResponse(cleanQuery, reply, "বাছাই করা টপিক", isFollowUpResponse = true)
                    }
                    val answer = qa?.answer?.takeIf { it.isNotBlank() }
                        ?: featured.summaryBangla.ifBlank { featured.detailsBangla }
                    val explanation = featured.detailsBangla.trim()
                        .takeIf { it.isNotBlank() && !answer.contains(it) }
                    val example = featured.exampleBangla?.trim()
                        ?.takeIf { it.isNotBlank() && !answer.contains(it) && explanation?.contains(it) != true }
                    val points = featured.keyPoints.filter { it.isNotBlank() }.take(2)
                    val reply = buildString {
                        append("সহজ করে বুঝি — ").append(featured.titleBangla).append("\n\n")
                        append("১. মূল কথা: ").append(answer)
                        if (explanation != null) append("\n\n২. কেন বা কীভাবে: ").append(explanation)
                        if (points.isNotEmpty()) append("\n\nমনে রাখো:\n• ").append(points.joinToString("\n• "))
                        if (example != null) append("\n\nউদাহরণ: ").append(example)
                        append("\n\nএখন কোন ধাপটি বুঝতে অসুবিধা হচ্ছে? বাংলা বা Banglish-এ লিখতে পারো।")
                    }
                    return@withContext TutorResponse(cleanQuery, AloTextSanitizer.cleanText(reply), "বাছাই করা টপিক", isFollowUpResponse = true)
                }
                if (qa != null && (!isFollowUp || !AloFollowUpExplainer.isFollowUpSimplificationRequest(cleanQuery))) {
                    val reply = "${featured.titleBangla}\n\nপ্রশ্ন: ${qa.question}\n\nউত্তর: ${qa.answer}\n\nব্যাখ্যা: ${featured.detailsBangla}"
                    return@withContext TutorResponse(cleanQuery, AloTextSanitizer.cleanText(reply), "বাছাই করা টপিক", isFollowUpResponse = isFollowUp)
                }
                return@withContext TutorResponse(
                    cleanQuery,
                    "এই টপিকের ওই নির্দিষ্ট প্রশ্নটির যাচাইকৃত অফলাইন উত্তর এখনো নেই। ওপরের মূল প্রশ্নটি বা ‘আরেকভাবে বোঝাও’ লিখে চেষ্টা করো।",
                    "অতিরিক্ত উত্তর প্রয়োজন",
                    isFollowUpResponse = isFollowUp
                )
            }
        }

        // A catalog entry only proves that a chapter exists. An individually
        // reviewed visual exercise may still be answered by its exact question
        // or an explicit page reference, without unlocking the whole chapter.
        if (mode == ChatAiMode.CURRICULUM && !chapterContext.isNullOrBlank()) {
            val grade = section.id.removePrefix("CLASS_").toIntOrNull()
            val catalogTopic = BangladeshCurriculumRepository.classLevels.asSequence()
                .filter { grade == null || it.levelNumber == grade }
                .flatMap { it.subjects.asSequence() }
                .flatMap { it.topics.asSequence() }
                .firstOrNull { it.catalogOnly && it.titleBangla.equals(chapterContext, ignoreCase = true) }
            if (catalogTopic != null) {
                if (isFollowUp && parentFollowUp.subjectTag.contains("যাচাইকৃত ছবি অনুশীলনী") &&
                    AloFollowUpExplainer.isFollowUpSimplificationRequest(cleanQuery)) {
                    return@withContext TutorResponse(
                        query = cleanQuery,
                        text = AloFollowUpExplainer.generateFollowUpExplanation(cleanQuery, parentFollowUp),
                        subjectTag = "যাচাইকৃত ছবি অনুশীলনী",
                        isFollowUpResponse = true
                    )
                }
                val pageMatch = Regex("(?:পৃষ্ঠা|page)\\s*([০-৯0-9]+)", RegexOption.IGNORE_CASE)
                    .find(cleanQuery)?.groupValues?.getOrNull(1)
                val pageNumber = pageMatch?.map { digit ->
                    if (digit in '০'..'৯') ('0'.code + digit.code - '০'.code).toChar() else digit
                }?.joinToString("")
                val verifiedPageQuestion = catalogTopic.visualExercises.asSequence()
                    .flatMap { exercise -> exercise.pageQuestions.asSequence().map { exercise to it } }
                    .firstOrNull { (_, qa) -> qa.question.equals(cleanQuery, ignoreCase = true) }
                if (verifiedPageQuestion != null) return@withContext TutorResponse(
                    query = cleanQuery,
                    text = AloTextSanitizer.cleanText("${verifiedPageQuestion.second.answer}\n\nউৎস: ${verifiedPageQuestion.first.sourceBangla}"),
                    subjectTag = "যাচাইকৃত ছবি অনুশীলনী",
                    isFollowUpResponse = isFollowUp
                )
                val reviewedExercises = catalogTopic.visualExercises.filter { exercise ->
                    // Generated book-page guidance has neither a drawable nor reviewed page Q&A.
                    // It is useful in the reader, but must never be presented as a verified answer.
                    exercise.imageResId != 0 || exercise.pageQuestions.isNotEmpty()
                }
                val verifiedExercise = reviewedExercises.firstOrNull { exercise ->
                    (pageNumber != null && exercise.sourceBangla.endsWith(
                        "পৃষ্ঠা " + pageNumber.map { ('০'.code + it.code - '0'.code).toChar() }.joinToString(""))) ||
                        exercise.questionBangla == cleanQuery
                }
                if (verifiedExercise != null) return@withContext TutorResponse(
                    query = cleanQuery,
                    text = AloTextSanitizer.cleanText("${verifiedExercise.answerBangla}\n\nউৎস: ${verifiedExercise.sourceBangla}"),
                    subjectTag = "যাচাইকৃত ছবি অনুশীলনী",
                    isFollowUpResponse = isFollowUp
                )
                val availability = if (reviewedExercises.isNotEmpty())
                    " এই অধ্যায়ের ${reviewedExercises.size}টি ছবি-অনুশীলনী যাচাই করা আছে; সংশ্লিষ্ট PDF পৃষ্ঠা নম্বর লিখে নির্দিষ্ট উত্তর জানতে পারো।" else ""
                return@withContext TutorResponse(
                query = cleanQuery,
                text = "এই অধ্যায়ের পূর্ণ পাঠ ও প্রশ্নোত্তর এখনো যাচাই করা হয়নি। তাই বইয়ের নামে অনুমান করে উত্তর দিচ্ছি না।$availability সাধারণ প্রশ্ন হিসেবে আলাদা করে জিজ্ঞাসা করতে পারো।",
                subjectTag = "যাচাইকৃত পাঠ প্রয়োজন",
                isFollowUpResponse = isFollowUp
            )
            }
        }

        // Chapter-support follow-ups are answered from the selected chapter's
        // teaching guide, not by a broad keyword template. This gives parents,
        // teachers and learners a predictable, relevant continuation.
        if (isFollowUp && !chapterContext.isNullOrBlank()) {
            chapterSupportReply(chapterContext, cleanQuery, section)?.let { reply ->
                return@withContext TutorResponse(
                    query = cleanQuery,
                    text = AloTextSanitizer.cleanText(reply),
                    subjectTag = "অধ্যায় সহায়িকা",
                    isFollowUpResponse = true
                )
            }
        }

        // If this is a contextual follow-up request to explain, simplify, or give examples
        if (isFollowUp && AloFollowUpExplainer.isFollowUpSimplificationRequest(cleanQuery)) {
            if (mode == ChatAiMode.GENERAL) {
                val online = AloQwenEngine.answerWithQwen(context, cleanQuery, history)
                return@withContext TutorResponse(
                    query = cleanQuery,
                    text = AloTextSanitizer.cleanText(online.text),
                    subjectTag = online.subjectTag,
                    isOnlineGemini = online.isOnline,
                    modelLabel = online.modelName,
                    isFollowUpResponse = true
                )
            }
            val explainedText = AloFollowUpExplainer.generateFollowUpExplanation(cleanQuery, parentFollowUp)
            val sanitized = AloTextSanitizer.cleanText(explainedText)

            return@withContext TutorResponse(
                query = cleanQuery,
                text = sanitized,
                subjectTag = "সহজ ব্যাখ্যা",
                isOnlineGemini = false,
                modelLabel = "অফলাইন পাঠ সহায়িকা",
                isFollowUpResponse = true
            )
        }

        // The visual chapter badge must also be meaningful to the answer engine.
        // Without this, a short question such as "আরও সহজ করে বলো" could match an
        // unrelated lesson merely because the UI, rather than the tutor, held context.
        val queryWithContext = buildString {
            // Chapter context belongs to a deliberate follow-up only. A learner can
            // ask a completely new question from inside a chapter without inheriting
            // a misleading maths/science topic.
            if (isFollowUp && mode == ChatAiMode.CURRICULUM && !chapterContext.isNullOrBlank()) {
                append("অধ্যায়: ").append(chapterContext).append("। ")
            }
            if (isFollowUp) {
                append("পূর্বের প্রশ্ন: ").append(parentFollowUp.query).append("। ")
            }
            append("নতুন প্রশ্ন: ").append(cleanQuery)
        }

        // 1. If Curriculum mode is selected, prioritize NCTB Curriculum Engine
        if (mode == ChatAiMode.CURRICULUM) {
            val offlineResp = AloOfflineAiEngine.generateResponse(queryWithContext, allowCurriculumMatch = false)
            
            // If offline engine had a concrete match in curriculum
            if (offlineResp.subjectTag != "আলো এআই উত্তর" && offlineResp.subjectTag != "সাধারণ") {
                val prefix = if (isFollowUp) "↳ [${parentFollowUp.query.take(40)}...]-এর ধারাবাহিকতায়:\n\n" else ""
                val fullText = prefix + offlineResp.responseBangla
                val sanitized = AloTextSanitizer.cleanText(fullText)

                return@withContext TutorResponse(
                    query = cleanQuery,
                    text = sanitized,
                    subjectTag = offlineResp.subjectTag,
                    isOnlineGemini = false,
                    modelLabel = "অফলাইন পাঠ সহায়িকা",
                    isFollowUpResponse = isFollowUp
                )
            }
            
            // In textbook mode, an unverified cloud answer must not masquerade
            // as an answer from the selected NCTB book.
            return@withContext TutorResponse(
                query = cleanQuery,
                text = "এই প্রশ্নের যাচাইকৃত পাঠ্যবইভিত্তিক উত্তর এখনো যুক্ত নেই। ভুল উত্তর দেওয়ার বদলে আমি এখানে থামছি। চাইলে সাধারণ AI শিক্ষকে প্রশ্নটি করতে পারো; সেটি বইয়ের যাচাইকৃত উত্তর হিসেবে গণ্য হবে না।",
                subjectTag = "যাচাইকৃত উত্তর প্রয়োজন",
                isOnlineGemini = false,
                modelLabel = "অফলাইন পাঠ সহায়িকা",
                isFollowUpResponse = isFollowUp
            )
        }

        // GENERAL is the open-question tutor. Do not intercept it with broad
        // curriculum keywords: they can return an unrelated or age-inappropriate
        // canned lesson instead of answering the user's actual question.
        val qwenResult = AloQwenEngine.answerWithQwen(context, cleanQuery, history)
        val prefix = if (isFollowUp) "↳ [Following up on: ${parentFollowUp.query.take(40)}...]:\n\n" else ""
        val finalCleanText = AloTextSanitizer.cleanText(prefix + qwenResult.text)

        TutorResponse(
            query = cleanQuery,
            text = finalCleanText,
            subjectTag = qwenResult.subjectTag,
            isOnlineGemini = qwenResult.isOnline,
            modelLabel = qwenResult.modelName,
            isFollowUpResponse = isFollowUp
        )
    }

    private fun chapterSupportReply(chapterTitle: String, query: String, section: StudySection): String? {
        val selectedGrade = section.id.removePrefix("CLASS_").toIntOrNull()
        val match = BangladeshCurriculumRepository.featuredClassLevels.asSequence()
            .filter { selectedGrade == null || it.levelNumber == selectedGrade }
            .flatMap { level -> level.subjects.asSequence().map { subject -> Triple(level.levelNumber, subject.nameBangla, subject) } }
            .flatMap { (level, subjectName, subject) ->
                subject.topics.asSequence().map { topic -> ChapterMatch(level, subjectName, topic) }
            }
            .firstOrNull { !it.topic.catalogOnly && it.topic.titleBangla.equals(chapterTitle, ignoreCase = true) }
            ?: return null

        val q = query.lowercase()
        val pedagogy = CurriculumPedagogyEngine.getPedagogyForTopic(match.level, match.subject, match.topic)
        return when {
            listOf("শিক্ষক", "কীভাবে পড়াব", "পড়াব", "পাঠদান").any(q::contains) ->
                "${match.topic.titleBangla} — শিক্ষক সহায়িকা\n\n" +
                    pedagogy.teachingTips.mapIndexed { index, item -> "${index + 1}. $item" }.joinToString("\n")
            listOf("অভিভাবক", "বাড়িতে", "বাসায়", "অনুশীলন").any(q::contains) ->
                "${match.topic.titleBangla} — অভিভাবক সহায়িকা\n\nবাড়ির কাজ: ${pedagogy.homeActivity}\n\n" +
                    pedagogy.teachingTips.take(2).mapIndexed { index, item -> "${index + 1}. $item" }.joinToString("\n")
            listOf("ভুল", "সমস্যা", "কোথায় আটকে", "সতর্ক").any(q::contains) ->
                "${match.topic.titleBangla} — সাধারণ ভুল ও প্রতিকার\n\n" +
                    pedagogy.commonMistakes.mapIndexed { index, item -> "${index + 1}. $item\n   প্রতিকার: ভুলটি একটি ছোট উদাহরণ দিয়ে আবার করান এবং শিক্ষার্থীকে নিজের ভাষায় নিয়মটি বলতে বলুন।" }.joinToString("\n\n")
            listOf("কুইজ", "মুখে", "জিজ্ঞাসা", "প্রশ্ন দাও", "ওয়ার্কশিট").any(q::contains) ->
                "${match.topic.titleBangla} — দ্রুত যাচাই প্রশ্ন\n\n" +
                    pedagogy.oralQuestions.mapIndexed { index, item -> "${index + 1}. প্রশ্ন: ${item.question}\nউত্তর: ${item.answer}" }.joinToString("\n\n")
            else -> null
        }
    }

    private fun bestQuestionAnswer(topic: CurriculumTopic, query: String): com.example.data.model.TopicQA? {
        if (topic.qaList.isEmpty()) return null
        val stop = setOf("কি", "কী", "কত", "করে", "করব", "কীভাবে", "কিভাবে", "the", "a", "is", "how", "what")
        fun words(value: String) = value.lowercase()
            .replace(Regex("[^\\p{L}\\p{N}]+"), " ")
            .split(' ').filter { it.length > 1 && it !in stop }.toSet()
        val queryWords = words(query)
        val scored = topic.qaList.map { qa -> qa to words(qa.question).intersect(queryWords).size }
        val best = scored.maxByOrNull { it.second }
        return when {
            best == null -> null
            best.second > 0 -> best.first
            query.equals(topic.qaList.first().question, ignoreCase = true) -> topic.qaList.first()
            else -> null
        }
    }

    private fun extractPdfPage(text: String): Int? {
        val value = Regex("(?:পৃষ্ঠা|page)\\s*([০-৯0-9]+)", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.getOrNull(1) ?: return null
        return value.map { digit ->
            if (digit in '০'..'৯') ('0'.code + digit.code - '০'.code).toChar() else digit
        }.joinToString("").toIntOrNull()
    }

    private fun toBanglaDigits(value: Int): String = value.toString().map { digit ->
        if (digit in '0'..'9') ('০'.code + digit.code - '0'.code).toChar() else digit
    }.joinToString("")

    private data class ChapterMatch(val level: Int, val subject: String, val topic: CurriculumTopic)
}
