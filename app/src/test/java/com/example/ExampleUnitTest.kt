package com.example

import com.example.ai.AloGrammarEngine
import com.example.ai.AloKnowledgeBase
import com.example.ai.AloMathSolver
import com.example.ai.AloOfflineAiEngine
import com.example.ai.AloQwenEngine
import com.example.ai.AloFollowUpExplainer
import com.example.ai.Class1MathAnswerEngine
import com.example.ai.Class1LanguageAnswerEngine
import com.example.data.local.AloChatEntity
import com.example.util.AloTextSanitizer
import com.example.data.model.PrimaryCurriculumData
import com.example.data.model.BangladeshCurriculumRepository
import com.example.data.model.FullOfflineQuestionBank
import com.example.data.model.MvpFeaturedCurriculum
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `user facing offline bank contains reviewed records only`() {
        assertTrue(FullOfflineQuestionBank.entries.size >= 12)
        assertTrue(FullOfflineQuestionBank.entries.all { it.source.isNotBlank() })
        assertTrue(FullOfflineQuestionBank.entries.all {
            it.question.isNotBlank() && it.answer.isNotBlank() && it.page > 0
        })
        assertTrue(FullOfflineQuestionBank.entries.none {
            it.answer.contains("এই তথ্য এবং পৃষ্ঠা") ||
                it.answer.contains("ধাপে ধাপে অনুসরণ করে কাজটি সম্পন্ন করো")
        })
    }

    @Test
    fun `verified bank tolerates harmless punctuation differences`() {
        val answer = FullOfflineQuestionBank.exact(2, "তিথির কেন গ্রাম ভালো লাগে")
        assertNotNull(answer)
        assertTrue(answer!!.answer.contains("শান্ত ও সবুজ"))
    }

    @Test
    fun `class two village and city official exercises have direct offline answers`() {
        val questions = FullOfflineQuestionBank.forPages(2, "bangla", 44, 46)
        assertTrue(questions.size >= 12)
        assertTrue(questions.any { it.question == "আমিন শহরে কী কী দেখে অবাক হয়েছে?" && it.answer.contains("বড় বড় দালান") })
        assertTrue(questions.any { it.question.contains("শূন্যস্থান পূরণ") && it.answer.contains("প্যাঁক প্যাঁক") })
        assertTrue(questions.any { it.question.contains("মিল-অমিল") && it.answer.contains("পুকুর") })
        assertTrue(questions.none { it.answer.contains("পৃষ্ঠা") && it.answer.contains("ব্যবহার করে") })
    }

    @Test
    fun `class two two little birds page exposes every printed answer offline`() {
        val english = MvpFeaturedCurriculum.levels.single { it.levelNumber == 2 }
            .subjects.single { it.id == "c2_english" }
        val page = english.topics.asSequence().flatMap { it.visualExercises.asSequence() }
            .single { it.imageAssetPath?.endsWith("page_060.webp") == true }
        assertEquals(4, page.pageQuestions.size)
        assertTrue(page.pageQuestions.any {
            it.question.contains("Thursday") && it.answer.contains("sat on a branch")
        })
        assertTrue(page.pageQuestions.any {
            it.question.contains("fly away") && it.answer.contains("Saturday")
        })
        assertTrue(english.topics.any { topic ->
            topic.qaList.any { it.question == "What did the birds do on Thursday?" }
        })
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun mathSolver_solvesLinearEquation() {
        val result = AloMathSolver.trySolve("2x + 4 = 10")
        assertNotNull(result)
        assertTrue(result!!.solved)
        assertTrue(result.explanationBangla.contains("৩")) // 3 in Bangla digit
    }

    @Test
    fun mathSolver_solvesPercentage() {
        val result = AloMathSolver.trySolve("500 এর 15%")
        assertNotNull(result)
        assertTrue(result!!.solved)
        assertTrue(result.explanationBangla.contains("৭৫")) // 75 in Bangla digit
    }

    @Test
    fun mathSolver_respectsOperationOrderAndParentheses() {
        val precedence = AloMathSolver.trySolve("২ + ৩ × ৪")
        assertNotNull(precedence)
        assertTrue(precedence!!.explanationBangla.contains("ফলাফল = ১৪"))
        val leftToRight = AloMathSolver.trySolve("১৬ ÷ ৪ × ২")
        assertNotNull(leftToRight)
        assertTrue(leftToRight!!.explanationBangla.contains("ফলাফল = ৮"))
        val parentheses = AloMathSolver.trySolve("(২ + ৩) × ৪")
        assertNotNull(parentheses)
        assertTrue(parentheses!!.explanationBangla.contains("ফলাফল = ২০"))
        val exponent = AloMathSolver.trySolve("-২^২")
        assertNotNull(exponent)
        assertTrue(exponent!!.explanationBangla.contains("ফলাফল = -৪"))
        assertEquals(null, AloMathSolver.trySolve("৫ ÷ ০"))
    }

    @Test
    fun mathSolver_solvesLcm() {
        val result = AloMathSolver.trySolve("লসাগু 12, 18, 24")
        assertNotNull(result)
        assertTrue(result!!.solved)
        assertTrue(result.explanationBangla.contains("৭২")) // 72 in Bangla digit
    }

    @Test
    fun grammarEngine_parsesSandhi() {
        val result = AloGrammarEngine.tryParseGrammar("বিদ্যালয় এর সন্ধি কি")
        assertNotNull(result)
        assertTrue(result!!.matched)
        assertTrue(result.responseBangla.contains("বিদ্যা + আলয়"))
    }

    @Test
    fun knowledgeBase_findsNewtonLesson() {
        val lesson = AloKnowledgeBase.findLesson("নিউটনের গতির সূত্র")
        assertNotNull(lesson)
        assertEquals("পদার্থবিজ্ঞান", lesson!!.subject)
        assertTrue(lesson.responseBangla.contains("F = ma"))
    }

    @Test
    fun offlineAiEngine_generatesBanglaResponse() = runBlocking {
        val response = AloOfflineAiEngine.generateResponse("সালোকসংশ্লেষণ কাকে বলে?")
        assertNotNull(response)
        assertTrue(response.isOffline)
        assertTrue(response.responseBangla.isNotEmpty())
        assertTrue(response.responseBangla.contains("ক্লোরোফিল") || response.responseBangla.contains("উদ্ভিদ"))
    }

    @Test
    fun nationalPoet_isAnsweredWithoutMathFallback() = runBlocking {
        val response = AloOfflineAiEngine.generateResponse("বাংলাদেশের জাতীয় কবি কে?")
        assertEquals("বাংলাদেশ ও সংস্কৃতি", response.subjectTag)
        assertTrue(response.responseBangla.contains("কাজী নজরুল ইসলাম"))
    }

    @Test
    fun nationalPoet_staysCorrectWithChatPromptAndUnrelatedMathChapter() = runBlocking {
        val prompts = listOf(
            "নতুন প্রশ্ন: আমাদের জাতীয় কবি কে?",
            "অধ্যায়: হাতে রেখে যোগ-বিয়োগ। নতুন প্রশ্ন: বাংলাদেশের জাতীয় কবি কে?"
        )
        prompts.forEach { prompt ->
            val response = AloOfflineAiEngine.generateResponse(prompt)
            assertEquals("বাংলাদেশ ও সংস্কৃতি", response.subjectTag)
            assertTrue(response.responseBangla.contains("কাজী নজরুল ইসলাম"))
        }
    }

    @Test
    fun englishNationalPoetQuestion_staysOnTopic() = runBlocking {
        val response = AloOfflineAiEngine.generateResponse("Who is the national poet of Bangladesh?")
        assertEquals("বাংলাদেশ ও সংস্কৃতি", response.subjectTag)
        assertTrue(response.responseBangla.contains("Kazi Nazrul Islam"))
    }

    @Test
    fun answerCard_omitsGreetingAndSelfIntroduction() {
        assertEquals("বাংলাদেশের জাতীয় কবি কাজী নজরুল ইসলাম।",
            AloTextSanitizer.cleanText("নমস্কার! আমি আলোপ্রো (AaloPro)। বাংলাদেশের জাতীয় কবি কাজী নজরুল ইসলাম।"))
        assertEquals("নমস্কার শব্দটি একটি অভিবাদন।",
            AloTextSanitizer.cleanText("নমস্কার শব্দটি একটি অভিবাদন।"))
    }

    @Test
    fun offlineFollowUp_doesNotInventAnUnrelatedMathMethod() {
        val parent = AloChatEntity(query = "২ + ৩ কত?", responseBangla = "২ + ৩ = ৫।")
        val reply = AloFollowUpExplainer.generateFollowUpExplanation("আরও সহজ করে বলো", parent)
        assertTrue(reply.contains("২ + ৩ = ৫"))
        assertTrue(!reply.contains("পক্ষান্তর"))
        assertTrue(!reply.contains("মার্বেল"))
    }

    @Test
    fun followUpIntent_understandsBanglaBanglishAndEnglish() {
        listOf(
            "বুঝিনি, বুঝিয়ে দাও", "কী বললে?", "কি বললে", "আবার বলো",
            "Bujhini, bujhiye dao", "ki bolle?", "bujhlam na",
            "What did you say?", "I don't understand", "Please explain again",
            "কেন?", "মানে?", "why?", "kivabe?"
        ).forEach { query ->
            assertTrue("Missed follow-up: $query", AloFollowUpExplainer.isFollowUpSimplificationRequest(query))
        }
        assertTrue(!AloFollowUpExplainer.isFollowUpSimplificationRequest("বাংলাদেশের জাতীয় কবি কে?"))
    }

    @Test
    fun quizAchievementUsesRealScoreBoundaries() {
        assertEquals("Little Hero", com.example.ui.quizAchievement(0, 10).title)
        assertEquals("Brain Hero", com.example.ui.quizAchievement(4, 10).title)
        assertEquals("Super Learner", com.example.ui.quizAchievement(7, 10).title)
        assertEquals("Aalo Champion", com.example.ui.quizAchievement(9, 10).title)
    }

    @Test
    fun healthQuestion_usesSafetyGuardrail() = runBlocking {
        val response = AloOfflineAiEngine.generateResponse("জ্বরের ওষুধের ডোজ কত?")
        assertEquals("স্বাস্থ্য নিরাপত্তা", response.subjectTag)
        assertTrue(response.responseBangla.contains("ডোজ বলা নিরাপদ নয়"))
        assertTrue(response.responseBangla.contains("ডাক্তার"))
    }

    @Test
    fun unsupportedQuestion_isNotFabricatedAsALesson() = runBlocking {
        val response = AloOfflineAiEngine.generateResponse("অজানা গ্রহ জোরিমার ইতিহাস বলো")
        assertEquals("আলো এআই উত্তর", response.subjectTag)
        assertTrue(response.responseBangla.contains("যাচাইকৃত উত্তর নেই"))
    }

    @Test
    fun capitalQuestion_doesNotTriggerPiAnswer() {
        val response = AloQwenEngine.generateLocalQwenResponse("What is the capital of Bangladesh?")
        assertTrue(response.subjectTag != "Mathematics")
        assertTrue(!response.text.contains("3.14159"))
    }

    @Test
    fun generalCapitalQuestion_doesNotMatchCapitalizationLesson() = runBlocking {
        val response = AloOfflineAiEngine.generateResponse(
            "Name the capital of Japan", allowCurriculumMatch = false
        )
        assertEquals("আলো এআই উত্তর", response.subjectTag)
        assertTrue(!response.responseBangla.contains("Punctuation"))
    }

    @Test
    fun englishScienceQuestionIsNotMisclassifiedAsBanglish() {
        assertTrue(!AloQwenEngine.prefersBangla("Explain photosynthesis to a child in two sentences."))
        assertTrue(AloQwenEngine.prefersBangla("salok songleshon bujhiye dao"))
        assertTrue(AloQwenEngine.prefersBangla("সালোকসংশ্লেষণ বুঝিয়ে দাও"))
    }

    @Test
    fun mvpHasThreeAnswerableTopicsPerSubjectInGradesOneToFive() {
        val levels = BangladeshCurriculumRepository.featuredClassLevels
        assertEquals((1..5).toList(), levels.map { it.levelNumber })
        levels.forEach { level ->
            assertTrue("No subjects in grade ${level.levelNumber}", level.subjects.isNotEmpty())
            level.subjects.forEach { subject ->
                if (subject.id.substringAfterLast('_') in setOf("hindu", "buddhist", "christian")) {
                    assertTrue("Empty catalog subject ${subject.id}", subject.topics.isNotEmpty())
                    assertTrue(subject.topics.all { it.titleBangla.isNotBlank() && it.detailsBangla.isNotBlank() })
                    return@forEach
                }
                val expandedCount = mapOf(
                    "c1_math" to 18, "c1_bangla" to 54, "c1_english" to 39,
                    "c2_math" to 7, "c2_bangla" to 29, "c2_english" to 10,
                    "c3_math" to 13, "c3_bangla" to 30, "c3_english" to 8,
                    "c3_science" to 11, "c3_social" to 12, "c3_religion" to 5,
                    "c4_math" to 11, "c4_bangla" to 23, "c4_english" to 18,
                    "c4_science" to 11, "c4_social" to 14, "c4_religion" to 5,
                    "c5_math" to 10, "c5_bangla" to 23, "c5_english" to 20,
                    "c5_science" to 13, "c5_social" to 16, "c5_religion" to 5
                )[subject.id]
                if (expandedCount != null) {
                    assertEquals("Expanded Class One topic count: ${subject.id}", expandedCount, subject.topics.size)
                } else {
                    assertEquals("${level.levelNumber}: ${subject.nameBangla}", 3, subject.topics.size)
                }
                subject.topics.forEach { topic ->
                    assertTrue("Missing question: ${topic.id}", topic.qaList.any { it.question.isNotBlank() && it.answer.isNotBlank() })
                }
            }
        }
    }

    @Test
    fun classOneMath_containsAllBookChaptersInOrder() {
        val math = BangladeshCurriculumRepository.featuredClassLevels
            .single { it.levelNumber == 1 }.subjects.single { it.id == "c1_math" }
        assertEquals(18, math.topics.size)
        assertEquals("তুলনা করি", math.topics.first().titleBangla)
        assertEquals("নিজে করি (২)", math.topics.last().titleBangla)
        assertTrue(math.topics.all { !it.catalogOnly && it.qaList.size >= 3 })
        assertEquals(115, math.topics.sumOf { it.visualExercises.size })
    }

    @Test
    fun classOneMath_dynamicAnswersAreExactAndOffline() {
        val cases = mapOf(
            "৪ + ৩ কত?" to "৪ + ৩ = ৭",
            "১৩ + ৪ কত?" to "১৩ + ৪ = ১৭",
            "১৮ - ৬ কত?" to "১৮ − ৬ = ১২",
            "৩৫ এর স্থানীয় মান" to "৩ দশক ও ৫ একক",
            "৩২ আর ২৩ কোনটি বড়?" to "৩২ বড়",
            "৯৯ এর পরে কী?" to "১০০",
            "triangle এর কয়টি বাহু?" to "৩টি"
        )
        cases.forEach { (question, expected) ->
            val answer = Class1MathAnswerEngine.answer(
                if ("triangle" in question) "জ্যামিতি" else "নিজে করি (২)", question
            )
            assertNotNull(question, answer)
            assertTrue("$question -> $answer", answer!!.contains(expected))
        }
    }

    @Test
    fun classOneMath_understandsBanglaDigitsAndBanglish() {
        assertTrue(Class1MathAnswerEngine.answer("যোগ", "24 + 13 koto?")!!.contains("৩৭"))
        assertTrue(Class1MathAnswerEngine.answer("সংখ্যা (২১ থেকে ৪০)", "32 ar 23 konta boro?")!!.contains("৩২ বড়"))
        assertTrue(Class1MathAnswerEngine.answer("স্থানীয় মান", "35 e koy doshok ekok?")!!.contains("৩ দশক"))
    }

    @Test
    fun classOneOfficialBooksAreFullyExpanded() {
        val first = BangladeshCurriculumRepository.featuredClassLevels.single { it.levelNumber == 1 }
        assertEquals(listOf("c1_math", "c1_bangla", "c1_english"), first.subjects.map { it.id })
        assertEquals(18, first.subjects.single { it.id == "c1_math" }.topics.size)
        assertEquals(54, first.subjects.single { it.id == "c1_bangla" }.topics.size)
        assertEquals(39, first.subjects.single { it.id == "c1_english" }.topics.size)
        assertTrue(first.subjects.flatMap { it.topics }.all { it.qaList.size >= 3 })
    }

    @Test
    fun classTwoOfficialBooksAreExpandedWithCompleteVisualPageCoverage() {
        val level = BangladeshCurriculumRepository.featuredClassLevels.single { it.levelNumber == 2 }
        assertEquals(setOf("c2_bangla", "c2_english", "c2_math"), level.subjects.map { it.id }.toSet())
        assertEquals(29, level.subjects.single { it.id == "c2_bangla" }.topics.size)
        assertEquals(10, level.subjects.single { it.id == "c2_english" }.topics.size)
        assertEquals(7, level.subjects.single { it.id == "c2_math" }.topics.size)
        assertEquals(65, level.subjects.single { it.id == "c2_bangla" }.topics.sumOf { it.visualExercises.size })
        assertEquals(100, level.subjects.single { it.id == "c2_english" }.topics.sumOf { it.visualExercises.size })
        assertEquals(128, level.subjects.single { it.id == "c2_math" }.topics.sumOf { it.visualExercises.size })
        assertTrue(level.subjects.flatMap { it.topics }.all { topic ->
            topic.qaList.size >= 3 && topic.visualExercises.all { !it.imageAssetPath.isNullOrBlank() }
        })
    }

    @Test
    fun classOneLanguageBookQuestionsStayOfflineAndRelevant() {
        assertTrue(Class1LanguageAnswerEngine.answer("আমাদের মুক্তিযুদ্ধ", "বিজয় দিবস কবে?")!!.contains("১৬ ডিসেম্বর"))
        assertTrue(Class1LanguageAnswerEngine.answer("মাছের রাজা", "জাতীয় মাছ কোনটি?")!!.contains("ইলিশ"))
        assertTrue(Class1LanguageAnswerEngine.answer("How Are You? (1)", "How are you এর উত্তর কী?")!!.contains("I am fine"))
        assertTrue(Class1LanguageAnswerEngine.answer("Where Do You Live?", "Where do you live?")!!.contains("Bangladesh"))
        assertTrue(Class1LanguageAnswerEngine.answer("Animal Sounds", "What sound does a cat make?")!!.contains("meow"))
    }

    @Test
    fun classOnePedagogyUsesAgeAppropriateChapterGuide() {
        val topic = BangladeshCurriculumRepository.featuredClassLevels.single { it.levelNumber == 1 }
            .subjects.single { it.id == "c1_math" }.topics.single { it.titleBangla == "যোগের ধারণা" }
        val guide = com.example.data.model.CurriculumPedagogyEngine.getPedagogyForTopic(1, "প্রাথমিক গণিত", topic)
        assertEquals(topic.parentTeachingTips, guide.teachingTips)
        assertTrue(guide.teachingTips.none { "carry" in it.lowercase() || "কোটি" in it })
    }

    @Test
    fun fifthGradeOfficialBookChaptersAreIndexed() {
        val fifthGrade = BangladeshCurriculumRepository.classLevels.single { it.levelNumber == 5 }
        val indexed = fifthGrade.subjects.flatMap { it.topics }.filter { it.catalogOnly }
        assertEquals(102, indexed.size)
        assertEquals(102, indexed.map { it.id }.distinct().size)
        assertTrue(indexed.any { it.titleBangla == "বৈচিত্র্যময় বাংলাদেশ" })
        assertTrue(indexed.any { it.titleBangla == "জীবের আবাসস্থল" })
    }

    @Test
    fun firstGradeCountingVisualExerciseHasVerifiedOfflineAnswer() {
        val topics = BangladeshCurriculumRepository.featuredClassLevels
            .single { it.levelNumber == 1 }.subjects
            .single { it.id == "c1_math" }.topics
        val counting = topics.single { it.id == "nctb2026_c1_math_2" }
        val numbers = topics.single { it.id == "nctb2026_c1_math_3" }
        assertEquals(4, counting.visualExercises.size)
        assertEquals(20, numbers.visualExercises.size)
        assertTrue(counting.visualExercises[0].answerBangla.contains("৫টি বাঘ"))
        assertTrue(counting.visualExercises[3].answerBangla.contains("৬টি গাজর"))
        assertTrue(numbers.visualExercises[0].answerBangla.contains("৪টি পেঁয়াজ"))
        assertTrue(numbers.visualExercises[7].answerBangla.contains("৭টি পেন্সিল"))
        assertTrue(numbers.visualExercises[8].answerBangla.contains("৮টি আনারস"))
        assertTrue(numbers.visualExercises[9].answerBangla.contains("৮টি টুপি"))
        assertTrue(numbers.visualExercises[11].answerBangla.contains("শূন্য"))
        assertTrue(numbers.visualExercises[13].answerBangla.contains("৬ = ৪ + ২"))
        assertTrue(numbers.visualExercises[14].answerBangla.contains("১০ = ৫ + ৫"))
        val banglaPageNumbers = (12..35).map { number -> number.toString().map { digit ->
            ('০'.code + digit.code - '0'.code).toChar()
        }.joinToString("") }
        assertEquals(banglaPageNumbers.map { "প্রথম শ্রেণির প্রাথমিক গণিত, ২০২৬ শিক্ষাবর্ষ, PDF পৃষ্ঠা $it" },
            (counting.visualExercises + numbers.visualExercises).map { it.sourceBangla })
    }

    @Test
    fun officialReligionChoicesAppearOnlyFromGradesThreeToFive() {
        assertTrue(BangladeshCurriculumRepository.classLevels.single { it.levelNumber == 1 }
            .subjects.none { it.iconName == "religion" })
        assertTrue(BangladeshCurriculumRepository.classLevels.single { it.levelNumber == 2 }
            .subjects.none { it.iconName == "religion" })
        for (grade in 3..5) {
            val religionSubjects = BangladeshCurriculumRepository.classLevels
                .single { it.levelNumber == grade }.subjects.filter { it.iconName == "religion" }
            assertEquals(4, religionSubjects.size)
            assertEquals(
                setOf("c${grade}_religion", "c${grade}_hindu", "c${grade}_buddhist", "c${grade}_christian"),
                religionSubjects.map { it.id }.toSet()
            )
            val hindu = religionSubjects.first { it.id.endsWith("hindu") }
            if (grade == 3) {
                assertTrue(hindu.topics.first().qaList.size >= 30)
                assertTrue(!hindu.topics.first().catalogOnly)
                assertTrue(hindu.topics.drop(1).all { it.catalogOnly })
            } else assertTrue(hindu.topics.all { it.catalogOnly })
            val buddhist = religionSubjects.first { it.id.endsWith("buddhist") }
            if (grade == 3) {
                assertTrue(buddhist.topics.first().qaList.size >= 20)
                assertTrue(!buddhist.topics.first().catalogOnly)
                assertTrue(buddhist.topics.first().visualExercises.size == 2)
                assertTrue(buddhist.topics[1].qaList.size >= 20)
                assertTrue(!buddhist.topics[1].catalogOnly)
                assertTrue(buddhist.topics[1].visualExercises.size == 2)
                assertTrue(buddhist.topics.drop(2).all { it.catalogOnly })
            } else assertTrue(buddhist.topics.all { it.catalogOnly })
            assertTrue(religionSubjects.first { it.id.endsWith("christian") }.topics.all { it.catalogOnly })

            val visibleIds = BangladeshCurriculumRepository.featuredClassLevels
                .single { it.levelNumber == grade }.subjects.map { it.id }.toSet()
            assertTrue("Class $grade religion choices must be visible in the app", religionSubjects.all { it.id in visibleIds })
        }
    }

    @Test
    fun gradesThreeToFiveHaveFiveIndexedIslamChaptersEach() {
        for (grade in 3..5) {
            val subject = BangladeshCurriculumRepository.classLevels
                .single { it.levelNumber == grade }.subjects
                .single { it.id == "c${grade}_religion" }
            assertEquals(5, subject.topics.count { it.catalogOnly })
            assertTrue(subject.topics.any { it.titleBangla == "ধর্মীয় সম্প্রীতি" })
        }
    }


    @Test
    fun everyListedPrimaryTopicHasReviewedOfflineLessonContent() {
        val incomplete = BangladeshCurriculumRepository.featuredClassLevels
            .flatMap { it.subjects }
            .flatMap { it.topics }
            .filterNot { it.catalogOnly }
            .filter { topic ->
                topic.titleBangla.isBlank() ||
                    topic.summaryBangla.isBlank() ||
                    topic.detailsBangla.isBlank() ||
                    topic.qaList.isEmpty() ||
                    topic.qaList.any { it.question.isBlank() || it.answer.isBlank() }
            }
            .map { it.titleBangla }

        assertTrue("Incomplete offline lesson content: $incomplete", incomplete.isEmpty())
    }

    @Test
    fun universalAiEngine_generatesMultilingualResponses() {
        val engResult = com.example.ai.AloUniversalOfflineEngine.generateUniversalResponse("Explain Quantum Superposition and Schrodinger's cat")
        assertEquals("English", engResult.detectedLanguage)
        assertTrue(engResult.responseText.contains("Quantum"))

        val mathResult = com.example.ai.AloUniversalOfflineEngine.generateUniversalResponse("Explain calculus derivative and integration")
        assertEquals("English", mathResult.detectedLanguage)
        assertTrue(mathResult.responseText.contains("Calculus"))

        val arabicResult = com.example.ai.AloUniversalOfflineEngine.generateUniversalResponse("اشرح قانون الجاذبية")
        assertEquals("العربية", arabicResult.detectedLanguage)
        assertTrue(arabicResult.responseText.contains("المعلم الذكي الشامل"))

        val banglaResult = com.example.ai.AloUniversalOfflineEngine.generateUniversalResponse("বিশ্ববিদ্যালয়ে কম্পিউটার সায়েন্স ও কোডিং ক্যারিয়ার")
        assertEquals("বাংলা", banglaResult.detectedLanguage)
        assertTrue(banglaResult.responseText.contains("উচ্চশিক্ষা"))
    }
}
