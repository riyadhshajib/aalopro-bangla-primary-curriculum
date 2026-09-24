package com.example.ai

import com.example.util.AloTextSanitizer
import kotlinx.coroutines.delay

/**
 * AloPro Offline AI Engine.
 * Limited on-device reference and solvers for Bangladeshi students.
 * Unsupported questions must remain explicitly unanswered rather than guessed.
 */
object AloOfflineAiEngine {

    data class AiResponse(
        val query: String,
        val responseBangla: String,
        val subjectTag: String,
        val isOffline: Boolean = true
    )

    suspend fun generateResponse(userQuery: String, allowCurriculumMatch: Boolean = true): AiResponse {
        // Small realistic thinking delay for natural conversational rhythm
        delay(350)

        val cleanQuery = userQuery.trim()
        if (cleanQuery.isEmpty()) {
            return AiResponse(
                query = cleanQuery,
                responseBangla = "কী জানতে চান লিখুন। যাচাই করা অফলাইন পাঠ, কিছু গণিতের সমাধান ও সাধারণ তথ্য দিতে পারি। কোনো প্রশ্নের নির্ভরযোগ্য উত্তর না থাকলে সেটি জানিয়ে দেব।",
                subjectTag = "সাধারণ"
            )
        }

        // 0. Check and normalize Banglish / Latin queries to Bangla
        val banglishInfo = AloBanglishNormalizer.normalizeBanglish(cleanQuery)
        val queriesToTry = if (banglishInfo.isBanglish && banglishInfo.convertedBanglaQuery != cleanQuery) {
            listOf(cleanQuery, banglishInfo.convertedBanglaQuery, "${cleanQuery} ${banglishInfo.convertedBanglaQuery}")
        } else {
            listOf(cleanQuery)
        }

        // Direct, high-confidence Bangladesh facts must never fall through to a
        // loose curriculum keyword match (for example a Maths chapter selected in
        // the UI). Keep these core primary-school answers deterministic offline.
        for (q in queriesToTry) {
            val bangladeshFact = answerBangladeshFoundationFact(q)
            if (bangladeshFact != null) {
                return AiResponse(
                    query = cleanQuery,
                    responseBangla = bangladeshFact,
                    subjectTag = "বাংলাদেশ ও সংস্কৃতি"
                )
            }
        }

        // Health questions must not receive a fabricated diagnosis, dose, or treatment
        // from a primary-school learning app. Keep the learner safe and route to care.
        for (q in queriesToTry) {
            val healthSafety = healthSafetyResponse(q)
            if (healthSafety != null) {
                return AiResponse(cleanQuery, healthSafety, "স্বাস্থ্য নিরাপত্তা")
            }
        }

        // 1. Check Computational Math & Equation Solver
        for (q in queriesToTry) {
            val mathResult = AloMathSolver.trySolve(q)
            if (mathResult != null && mathResult.solved) {
                return AiResponse(
                    query = cleanQuery,
                    responseBangla = AloTextSanitizer.cleanText(mathResult.explanationBangla),
                    subjectTag = "গণিত"
                )
            }
        }

        // 2. Check Grammar (Bangla Sandhi, Samas, Karok, or English Tense, Voice, Narration)
        for (q in queriesToTry) {
            val grammarResult = AloGrammarEngine.tryParseGrammar(q)
            if (grammarResult != null && grammarResult.matched) {
                return AiResponse(
                    query = cleanQuery,
                    responseBangla = AloTextSanitizer.cleanText(grammarResult.responseBangla),
                    subjectTag = grammarResult.category
                )
            }
        }

        // 3. Check Academic Subject Knowledge Base (NCTB Lessons)
        for (q in queriesToTry) {
            val lesson = AloKnowledgeBase.findLesson(q)
            if (lesson != null) {
                return AiResponse(
                    query = cleanQuery,
                    responseBangla = AloTextSanitizer.cleanText(lesson.responseBangla),
                    subjectTag = lesson.subject
                )
            }
        }

        // 3.5 Check Bangladesh Curriculum Topics (Class 1-5 & 6-10)
        if (allowCurriculumMatch) {
            for (q in queriesToTry) {
                val curriculumResponse = checkCurriculumMatch(q)
                if (curriculumResponse != null) {
                    return curriculumResponse.copy(
                        query = cleanQuery,
                        responseBangla = AloTextSanitizer.cleanText(curriculumResponse.responseBangla)
                    )
                }
            }
        }

        // Do not turn a loose keyword match into an authoritative-looking lesson.
        // Unsupported questions are deliberately handed to the online tutor (when
        // available) or shown as an honest offline limitation by the caller.
        return AiResponse(
            query = cleanQuery,
            responseBangla = "এই প্রশ্নটির জন্য আমার অফলাইন পাঠভান্ডারে যাচাইকৃত উত্তর নেই। প্রশ্নটি আরেকটু নির্দিষ্ট করে লিখুন, অথবা ইন্টারনেট-সংযুক্ত এআই শিক্ষক ব্যবহার করুন।",
            subjectTag = "আলো এআই উত্তর"
        )
    }

    private fun answerBangladeshFoundationFact(query: String): String? {
        val q = query.lowercase()
        return when {
            q.contains("national poet") && q.contains("bangladesh") ->
                "The national poet of Bangladesh is Kazi Nazrul Islam."
            (q.contains("জাতীয় কবি") || q.contains("জাতীয় কবি")) &&
                (q.contains("বাংলাদেশ") || q.contains("আমাদের") || q.contains("কে")) ->
                "বাংলাদেশের জাতীয় কবি হলেন বিদ্রোহী কবি **কাজী নজরুল ইসলাম**। তিনি ‘বিদ্রোহী’, ‘চল্ চল্ চল্’সহ অনেক বিখ্যাত কবিতা ও গান লিখেছেন।"
            q.contains("জাতীয় ফুল") || q.contains("জাতীয় ফুল") ->
                "বাংলাদেশের জাতীয় ফুল হলো **শাপলা**।"
            q.contains("জাতীয় পাখি") || q.contains("জাতীয় পাখি") ->
                "বাংলাদেশের জাতীয় পাখি হলো **দোয়েল**।"
            q.contains("জাতীয় ফল") || q.contains("জাতীয় ফল") ->
                "বাংলাদেশের জাতীয় ফল হলো **কাঁঠাল**।"
            else -> null
        }
    }

    private fun healthSafetyResponse(query: String): String? {
        val q = query.lowercase()
        val healthTerms = listOf("ওষুধ", "ডোজ", "জ্বর", "ব্যথা", "অসুস্থ", "ডাক্তার", "রোগ", "কাশি", "পেট ব্যথা", "medicine", "dose", "fever", "pain", "doctor")
        if (healthTerms.none { q.contains(it) }) return null
        return "স্বাস্থ্যসংক্রান্ত প্রশ্নে আমি সাধারণ শিক্ষামূলক তথ্য দিতে পারি, কিন্তু রোগ নির্ণয় বা ওষুধের ডোজ বলা নিরাপদ নয়। একজন অভিভাবককে জানান এবং ডাক্তার/যোগ্য স্বাস্থ্যকর্মীর পরামর্শ নিন। শ্বাসকষ্ট, অজ্ঞান হওয়া, খিঁচুনি, তীব্র ব্যথা বা দ্রুত অবনতি হলে জরুরি চিকিৎসা নিন।"
    }

    private fun synthesizeOfflineResponse(query: String): AiResponse {
        val q = query.lowercase()

        // Detect Domain
        return when {
            q.contains("গণিত") || q.contains("math") || q.contains("বীজগণিত") || q.contains("জ্যামিতি") || q.contains("ত্রিকোণমিতি") || q.contains("সূত্র") -> {
                AiResponse(
                    query = query,
                    subjectTag = "গণিত",
                    responseBangla = """
                        গণিত নির্দেশিকা ও সমাধান পদ্ধতি
                        প্রশ্ন: $query

                        মৌলিক গাণিতিক সূত্রসমূহ:
                        ১. বীজগণিতীয় প্রধান সূত্র:
                           • (a + b)² = a² + 2ab + b²
                           • (a - b)² = a² - 2ab + b²
                           • a² - b² = (a + b)(a - b)
                           • a³ + b³ = (a + b)(a² - ab + b²)
                        
                        ২. সমীকরণ সমাধানের নিয়ম:
                           • পক্ষান্তরের সময় চিহ্ন বিপরীত হয় (+ থাকলে -, × থাকলে ÷)।
                           • উভয় পক্ষে একই সংখ্যা যোগ, বিয়োগ, গুণ বা ভাগ করা যায়।

                        টিপস: আপনি সরাসরি নির্দিষ্ট সমীকরণ লিখেও তাৎক্ষণিক সমাধান পেতে পারেন।
                    """.trimIndent()
                )
            }

            q.contains("পদার্থ") || q.contains("physics") || q.contains("গতি") || q.contains("বেগ") || q.contains("ত্বরণ") || q.contains("আলো") -> {
                AiResponse(
                    query = query,
                    subjectTag = "পদার্থবিজ্ঞান",
                    responseBangla = """
                        পদার্থবিজ্ঞানের মূল ধারণা
                        বিষয়: $query

                        মূল সূত্রাবলী ও সংজ্ঞা:
                        • গতির সমীকরণসমূহ:
                          ১. v = u + at (যেখানে u=আদিবেগ, v=শেষবেগ, a=ত্বরণ, t=সময়)
                          ২. s = ut + ½ at² (s=দূরত্ব)
                          ৩. v² = u² + 2as
                          ৪. s = ((u + v) / 2) × t
                        • অভিকর্ষজ ত্বরণ (g): ভূপৃষ্ঠে আদর্শ মান প্রায় ৯.৮ m/s²।
                        • ঘনত্ব (ρ): ρ = m / V (ভর ÷ আয়তন)।

                        পরীক্ষার পরামর্শ:
                        পদার্থবিজ্ঞানের অঙ্কে এককের দিকে সতর্ক থাকুন (যেমন: গ্রামকে কেজিতে এবং সেন্টিমিটারকে মিটারে রূপান্তর করে হিসাব করুন)।
                    """.trimIndent()
                )
            }

            q.contains("রসায়ন") || q.contains("chemistry") || q.contains("পরমাণু") || q.contains("বিক্রিয়া") || q.contains("মৌল") -> {
                AiResponse(
                    query = query,
                    subjectTag = "রসায়ন",
                    responseBangla = """
                        রসায়নের মূল নিয়ম ও ব্যাখ্যা
                        বিষয়: $query

                        গুরুত্বপূর্ণ রাসায়নিক নিয়মাবলী:
                        • যোজনী (Valency): একটি পরমাণুর অন্য পরমাণুর সাথে যুক্ত হওয়ার ক্ষমতা। যেমন: H এর ১, O এর ২, C এর ৪, Na এর ১, Cl এর ১।
                        • মোল ধারণা: ১ মোল যেকোনো পদার্থে ৬.০২৩ × ১০²³ টি অণু বা পরমাণু থাকে (অ্যাভোগাড্রো সংখ্যা)।
                        • সংকেত লেখার নিয়ম: যোজনী বিনিময় করে লিখতে হয় (যেমন Ca এর ২ ও Cl এর ১ মিলে CaCl₂)।

                        পরামর্শ:
                        পর্যায় সারণির প্রথম ২০টি মৌলের নাম ও পারমাণবিক সংখ্যা মনে রাখলে রসায়ন বোঝা অনেক সহজ হয়।
                    """.trimIndent()
                )
            }

            q.contains("ইংরেজি") || q.contains("english") || q.contains("গ্রামার") || q.contains("grammar") || q.contains("sentence") -> {
                AiResponse(
                    query = query,
                    subjectTag = "ইংরেজি গ্রামার",
                    responseBangla = """
                        ইংরেজি ব্যাকরণ ও বাক্য গঠনের নিয়ম
                        বিষয়: $query

                        মূল বাক্য কাঠামো:
                        • Subject + Verb + Object / Extension
                        • Subject যদি Third Person Singular Number (He, She, It, যেকোনো একক নাম) হয় এবং Present Indefinite Tense হয়, তবে Verb-এর সাথে s বা es যুক্ত হয়। যেমন: He plays football.
                        
                        Right Form of Verbs এর জরুরি নিয়ম:
                        • Modal Auxiliary (can, could, may, might, must, should) এর পর Verb-এর বেস ফর্ম (V1) বসে। যেমন: You should study regularly.
                        • To এর পর সাধারণত Verb1 বসে। তবে with a view to, look forward to এর পর verb-ing হয়।
                    """.trimIndent()
                )
            }

            q.contains("পড়া") || q.contains("পড়া") || q.contains("টিপস") || q.contains("রুটিন") || q.contains("study") || q.contains("পরীক্ষা") || q.contains("মনোযোগ") -> {
                AiResponse(
                    query = query,
                    subjectTag = "স্টাডি টিপস",
                    responseBangla = """
                        স্মার্ট স্টাডি গাইড ও পরামর্শ

                        ১. পোমোডোরো টেকনিক:
                           • ২৫ মিনিট সম্পূর্ণ মনোযোগ দিয়ে পড়ুন।
                           • এরপর ৫ মিনিট বিরতি নিন। ৪টি সেশনের পর ২০ মিনিটের বড় বিরতি। এতে মস্তিষ্কে ক্লান্তি আসে না।

                        ২. অ্যাক্টিভ রিকল ও নিজের ভাষায় নোট:
                           • শুধু রিডিং না পড়ে বই বন্ধ করে নিজেকে প্রশ্ন করুন: 'আমি কী বুঝলাম?'
                           • নিজের ভাষায় খাতায় সংক্ষেপে পয়েন্টগুলো লিখুন।

                        ৩. ভুলের খাতা:
                           • মডেল টেস্ট বা কুইজে যে প্রশ্নগুলো ভুল হয়, সেগুলো আলাদা খাতায় লিখে রাখুন এবং পরীক্ষার আগে রিভিশন দিন।

                        ৪. পর্যাপ্ত ঘুম ও বিশ্রাম:
                           • প্রতিদিন অন্তত ৭ ঘণ্টা গভীর ঘুম প্রয়োজন, কারণ ঘুমের মধ্যেই মস্তিষ্ক নতুন তথ্য দীর্ঘস্থায়ী স্মৃতিতে রূপান্তর করে।
                    """.trimIndent()
                )
            }

            else -> {
                AiResponse(
                    query = query,
                    subjectTag = "আলো এআই উত্তর",
                    responseBangla = """
                        শিক্ষামূলক বিশ্লেষণ ও সমাধান
                        বিষয়: $query

                        সংক্ষিপ্ত ব্যাখ্যা:
                        আপনার প্রশ্নের প্রেক্ষিতে মূল শিক্ষণীয় বিষয়সমূহ নিচে সহজ ও সুশৃঙ্খলভাবে তুলে ধরা হলো:

                        ১. মূল ধারণা: পাঠ্যবইয়ের সংশ্লিষ্ট অধ্যায়ের মৌলিক সংজ্ঞা ও সূত্রসমূহ স্পষ্টভাবে চিহ্নিত করুন।
                        ২. কার্যকারণ সম্পর্ক: কেন এবং কীভাবে এই নিয়মটি প্রয়োগ করা হয় তা ধাপে ধাপে বুঝে নিন।
                        ৩. সমাধান ও অনুশীলন: পরীক্ষায় ভালো ফলের জন্য নিয়মিত প্রশ্ন অনুশীলন ও খাতায় লিখে চর্চা করা সবচেয়ে ফলপ্রসূ।

                        নির্দিষ্ট বিষয়ে বিস্তারিত জানতে যেকোনো সমীকরণ, সূত্র বা অধ্যায়ের নাম লিখে সরাসরি প্রশ্ন করতে পারেন।
                    """.trimIndent()
                )
            }
        }
    }

    private val STOP_WORDS = setOf(
        "আমাকে", "একটু", "বলো", "বলুন", "দাও", "কী", "কি", "কেন", "কীভাবে", "কেমনে",
        "সম্পর্কে", "বিস্তারিত", "বিশ্লেষণ", "বুঝিয়ে", "ব্যাখ্যা", "করুন", "করো",
        "কাকে", "বলে", "এর", "ও", "এবং", "নিয়ে", "জানাও", "পারি", "চাই", "অধ্যায়",
        "পাঠ", "বলতে", "বোঝায়", "সম্পর্কে", "জানতে", "দাওনা", "please", "explain",
        "what", "is", "the", "of", "a", "an", "to", "in", "for", "on", "with", "at",
        "by", "from", "about", "into", "through", "during", "before", "after", "above",
        "below", "how", "why", "when", "where", "who", "which", "value", "values",
        "tell", "me", "give", "define", "meaning"
    )

    private fun checkCurriculumMatch(cleanQuery: String): AiResponse? {
        val q = cleanQuery.lowercase()
        
        // If the query is in English and not specifically mentioning an NCTB class or subject,
        // let the general Qwen engine handle it rather than pulling an irrelevant class topic.
        val hasBengaliLetters = q.any { it in '\u0980'..'\u09FF' }
        val mentionsClass = q.contains("class") || q.contains("শ্রেণি") || q.contains("অধ্যায়") ||
                q.contains("ssc") || q.contains("hsc") || q.contains("প্রাথমিক") || q.contains("বই")
        val classLevels = com.example.data.model.BangladeshCurriculumRepository.classLevels
        // English chapters, such as "Days of the Week & Months", are valid
        // textbook routes even when the learner does not write "Class" first.
        val containsListedTopicTitle = classLevels
            .flatMap { it.subjects }
            .flatMap { it.topics }
            .filterNot { it.catalogOnly }
            .any { topic ->
                val title = topic.titleBangla.lowercase()
                q.contains(title) || title.contains(q)
            }
        if (!hasBengaliLetters && !mentionsClass && !containsListedTopicTitle) {
            return null
        }

        val tokens = q.split("\\s+".toRegex())
            .map { it.trim(',', '.', '?', '!', ':', ';', '(', ')') }
            .filter { it.length >= 2 && it !in STOP_WORDS }

        if (tokens.isEmpty() && q.length < 3) return null

        var bestTopic: com.example.data.model.CurriculumTopic? = null
        var bestSubject: com.example.data.model.CurriculumSubject? = null
        var bestLevel: com.example.data.model.CurriculumClassLevel? = null
        var highestScore = 0

        for (level in classLevels) {
            val levelMatches = (q.contains("${level.levelNumber}") || 
                                q.contains(level.classNameBangla.take(4).lowercase()) ||
                                (level.levelNumber == 1 && (q.contains("১ম") || q.contains("প্রথম"))) ||
                                (level.levelNumber == 2 && (q.contains("২য়") || q.contains("দ্বিতীয়"))) ||
                                (level.levelNumber == 3 && (q.contains("৩য়") || q.contains("তৃতীয়"))) ||
                                (level.levelNumber == 4 && (q.contains("৪র্থ") || q.contains("চতুর্থ"))) ||
                                (level.levelNumber == 5 && (q.contains("৫ম") || q.contains("পঞ্চম"))) ||
                                (level.levelNumber == 6 && (q.contains("৬ষ্ঠ") || q.contains("ষষ্ঠ"))) ||
                                (level.levelNumber == 7 && (q.contains("৭ম") || q.contains("সপ্তম"))) ||
                                (level.levelNumber == 8 && (q.contains("৮ম") || q.contains("অষ্টম"))) ||
                                (level.levelNumber == 9 && (q.contains("৯ম") || q.contains("নবম"))) ||
                                (level.levelNumber == 10 && (q.contains("১০ম") || q.contains("দশম") || q.contains("এসএসসি") || q.contains("ssc"))))

            for (subject in level.subjects) {
                val subjectNameClean = subject.nameBangla.lowercase()
                val subjectMatches = q.contains(subjectNameClean) || subjectNameClean.contains(q)

                for (topic in subject.topics) {
                    if (topic.catalogOnly) continue
                    var score = 0
                    val titleLower = topic.titleBangla.lowercase()
                    val detailsLower = topic.detailsBangla.lowercase()
                    val summaryLower = topic.summaryBangla.lowercase()
                    val formulaLower = topic.formulaOrRule?.lowercase() ?: ""

                    // Exact title containment
                    if (q.contains(titleLower) || titleLower.contains(q)) {
                        score += 35
                    }

                    // Token matching
                    for (token in tokens) {
                        if (titleLower.contains(token)) {
                            score += 15
                        }
                        if (formulaLower.contains(token)) {
                            score += 10
                        }
                        if (topic.keyPoints.any { it.lowercase().contains(token) }) {
                            score += 8
                        }
                        if (summaryLower.contains(token)) {
                            score += 6
                        }
                        if (detailsLower.contains(token)) {
                            score += 4
                        }
                    }

                    if (subjectMatches) score += 12
                    if (levelMatches) score += 10

                    if (score > highestScore) {
                        highestScore = score
                        bestTopic = topic
                        bestSubject = subject
                        bestLevel = level
                    }
                }
            }
        }

        // Threshold score to qualify as a valid analytical match
        // A topic must have a strong exact signal or at least two meaningful token hits.
        // This prevents a single loose word from hijacking an unrelated general question.
        val meaningfulMatches = tokens.count { token ->
            bestTopic?.let { topic ->
                topic.titleBangla.lowercase().contains(token) ||
                    topic.summaryBangla.lowercase().contains(token) ||
                    topic.keyPoints.any { it.lowercase().contains(token) }
            } == true
        }
        if (highestScore >= 35 && (meaningfulMatches >= 2 || (bestTopic != null && q.contains(bestTopic!!.titleBangla.lowercase()))) && bestTopic != null && bestSubject != null && bestLevel != null) {
            val buffer = StringBuilder()
            buffer.append("📚 **${bestTopic.titleBangla}**\n\n")

            buffer.append("বিষয় সারসংক্ষেপ:\n")
            buffer.append("${bestTopic.summaryBangla}\n\n")

            buffer.append("বিস্তারিত ব্যাখ্যা:\n")
            buffer.append("${bestTopic.detailsBangla}\n\n")

            bestTopic.formulaOrRule?.let { formula ->
                buffer.append("প্রয়োজনীয় সূত্র ও নিয়ম:\n")
                buffer.append("$formula\n\n")
            }

            if (bestTopic.keyPoints.isNotEmpty()) {
                buffer.append("মূল পয়েন্টসমূহ:\n")
                bestTopic.keyPoints.forEachIndexed { idx, point ->
                    buffer.append("${idx + 1}. $point\n")
                }
                buffer.append("\n")
            }

            bestTopic.exampleBangla?.let { example ->
                buffer.append("বাস্তব উদাহরণ ও প্রয়োগ:\n")
                buffer.append("$example\n\n")
            }

            buffer.append("পরীক্ষার জন্য গুরুত্বপূর্ণ পরামর্শ:\n")
            buffer.append("• এই অধ্যায়ের প্রশ্নে সংজ্ঞা ও সূত্রের পাশাপাশি একক এবং প্রাসঙ্গিক চিত্র দিলে পূর্ণ নম্বর পাওয়া যায়।\n")
            buffer.append("• সৃজনশীল প্রশ্নের ক্ষেত্রে কার্যকারণ বিশ্লেষণ ও সূত্রের প্রয়োগের ধাপগুলো ক্রমানুসারে পরিষ্কারভাবে উপস্থাপন করুন।\n")

            return AiResponse(
                query = cleanQuery,
                responseBangla = buffer.toString().trim(),
                subjectTag = bestSubject.nameBangla
            )
        }

        return null
    }
}
