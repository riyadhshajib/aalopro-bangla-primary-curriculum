package com.example

import android.content.Context
import com.example.ai.AloTutorManager
import com.example.ai.ChatAiMode
import com.example.ai.StudySection
import com.example.ai.StudioPromptGuide
import com.example.ai.OfflineReferenceGuide
import com.example.data.local.AloChatEntity
import com.example.data.local.AloNoteEntity
import com.example.data.local.AloStudyStatEntity
import com.example.data.local.AloDatabase
import com.example.data.local.AloRepository
import androidx.room.Room
import com.example.data.model.BangladeshCurriculumRepository
import com.example.data.model.FormulaRepository
import com.example.data.model.QuizRepository
import com.example.data.model.FullOfflineQuestionBank
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ExampleRobolectricTest {

  @Test
  fun `all extracted textbook questions and banglish follow ups remain offline`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    FullOfflineQuestionBank.entries.groupBy { it.grade }.forEach { (grade, questions) ->
      questions.forEach { item ->
        val section = StudySection.forClassNumber(grade)
        val answer = AloTutorManager.answerQuestion(context, item.question, ChatAiMode.CURRICULUM,
          section, chapterContext = "বইয়ের অনুশীলনী")
        assertFalse("Online answer for class $grade page ${item.page}", answer.isOnlineGemini)
        fun normalized(value: String) = value
          .replace(Regex("[^\\p{L}\\p{N}\\s]"), "")
          .replace(Regex("\\s+"), " ")
          .trim()
        val normalizedQuestion = normalized(item.question)
        val acceptableAnswers = questions.asSequence()
          .filter { normalized(it.question) == normalizedQuestion }
          .map { normalized(it.answer) }
          .toSet()
        val normalizedActual = answer.text
          .replace(Regex("[^\\p{L}\\p{N}\\s]"), "")
          .replace(Regex("\\s+"), " ")
          .trim()
        assertTrue(
          "Offline answer mismatch for class $grade page ${item.page}: ${item.question}\n" +
            "Expected answer fragment: ${item.answer}\nActual: ${answer.text}",
          acceptableAnswers.any(normalizedActual::contains)
        )
        val follow = AloTutorManager.answerQuestion(context, "Bujhini, aro shohoj kore bujhao",
          ChatAiMode.CURRICULUM, section, chapterContext = "বইয়ের অনুশীলনী",
          parentFollowUp = AloChatEntity(query = item.question, responseBangla = answer.text,
            chapterTitle = "বইয়ের অনুশীলনী", classCategory = section.id))
        assertFalse(follow.isOnlineGemini)
        assertTrue(follow.isFollowUpResponse)
        assertTrue(
          "Banglish follow-up mismatch for class $grade page ${item.page}: ${item.question}\nActual: ${follow.text}",
          follow.text.contains("আরও সহজ করে বুঝি")
        )
      }
    }
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AaloPro Bangla Pri", appName)
  }

  @Test
  fun `new tester profile clears only old local study data`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = Room.inMemoryDatabaseBuilder(context, AloDatabase::class.java).build()
    try {
      db.aloDao().insertChat(AloChatEntity(query = "old", responseBangla = "old"))
      db.aloDao().insertNote(AloNoteEntity(title = "old", content = "old"))
      db.aloDao().insertOrUpdateStat(AloStudyStatEntity(dateKey = "2026-09-19", quizzesCompleted = 2))
      AloRepository(db.aloDao()).clearLocalStudyDataForNewProfile()
      assertTrue(db.aloDao().getAllChats().first().isEmpty())
      assertTrue(db.aloDao().getAllNotes().first().isEmpty())
      assertEquals(null, db.aloDao().getStatsForDate("2026-09-19"))
    } finally {
      db.close()
    }
  }

  @Test
  fun `every primary subject has three featured questions`() {
    val levels = BangladeshCurriculumRepository.featuredClassLevels
    assertEquals(listOf(1, 2, 3, 4, 5), levels.map { it.levelNumber })
    levels.forEach { level ->
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
          "c5_science" to 13, "c5_social" to 16, "c5_religion" to 5,
          "c5_scholarship" to 5
        )[subject.id]
        if (expandedCount != null) assertEquals(expandedCount, subject.topics.size)
        else assertEquals("${level.levelNumber} ${subject.id}", 3, subject.topics.size)
        subject.topics.forEach { topic ->
          assertTrue(topic.titleBangla.isNotBlank())
          assertTrue(topic.detailsBangla.isNotBlank())
          assertTrue(topic.qaList.first().question.isNotBlank())
          assertTrue(topic.qaList.first().answer.isNotBlank())
        }
      }
    }
    val firstMathQuestion = levels.first().subjects.first { it.id == "c1_math" }
      .topics.first().qaList.first().question
    assertTrue(firstMathQuestion.contains("বেশি ও কম"))
  }

  @Test
  fun `class three books have complete offline questions visuals and contextual follow ups`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val level = BangladeshCurriculumRepository.featuredClassLevels.single { it.levelNumber == 3 }
    assertEquals(setOf("c3_bangla", "c3_english", "c3_math", "c3_science", "c3_social", "c3_religion",
      "c3_hindu", "c3_buddhist", "c3_christian"),
      level.subjects.map { it.id }.toSet())
    level.subjects.filterNot { it.id.substringAfterLast('_') in setOf("hindu", "buddhist", "christian") }.forEach { subject ->
      subject.topics.forEach { topic ->
        assertTrue("No visual pages for ${topic.id}", topic.visualExercises.isNotEmpty())
        topic.visualExercises.forEach { exercise ->
          val path = exercise.imageAssetPath
          assertTrue("Missing image path in ${topic.id}", !path.isNullOrBlank())
          context.assets.open(path!!).use { stream ->
            assertTrue("Unreadable image asset $path", stream.read() >= 0)
          }
        }
        assertTrue("Expected offline QAs for ${topic.id}", topic.qaList.size >= 3)
        topic.qaList.forEach { qa ->
          assertTrue("Blank question in ${topic.id}", qa.question.isNotBlank())
          assertTrue("Blank answer in ${topic.id}", qa.answer.isNotBlank())
        }
        val answer = AloTutorManager.answerQuestion(
          context, topic.qaList.first().question, ChatAiMode.CURRICULUM,
          StudySection.CLASS_3, chapterContext = topic.titleBangla
        )
        assertFalse("Online answer for ${topic.id}", answer.isOnlineGemini)
        assertTrue("Wrong chapter answer for ${topic.id}: ${answer.text}",
          answer.text.contains(topic.qaList.first().answer))
        val followUp = AloTutorManager.answerQuestion(
          context, "Bujhini, aro shohoj kore bujhao", ChatAiMode.CURRICULUM,
          StudySection.CLASS_3, chapterContext = topic.titleBangla,
          parentFollowUp = AloChatEntity(
            query = topic.qaList.first().question,
            responseBangla = answer.text,
            chapterTitle = topic.titleBangla,
            classCategory = StudySection.CLASS_3.id
          )
        )
        assertFalse("Online follow-up for ${topic.id}", followUp.isOnlineGemini)
        assertTrue("Missing contextual follow-up for ${topic.id}", followUp.isFollowUpResponse)
        assertTrue("Unexplained follow-up for ${topic.id}", followUp.text.contains("সহজ করে বুঝি"))
      }
    }
  }

  @Test
  fun `reviewed class three buddhist exercises and banglish follow ups stay offline`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val topics = BangladeshCurriculumRepository.featuredClassLevels.single { it.levelNumber == 3 }
      .subjects.single { it.id == "c3_buddhist" }.topics.filterNot { it.catalogOnly }
    assertTrue("Expected at least two reviewed Buddhist topics", topics.size >= 2)
    topics.forEach { topic ->
      assertTrue("Expected reviewed questions for ${topic.id}", topic.qaList.isNotEmpty())
      assertTrue("Expected reviewed visual exercises for ${topic.id}", topic.visualExercises.isNotEmpty())
      topic.visualExercises.forEach { exercise ->
        context.assets.open(exercise.imageAssetPath!!).use { assertTrue(it.read() >= 0) }
        assertTrue(exercise.pageQuestions.isNotEmpty())
      }
      topic.qaList.forEach { qa ->
      val answer = AloTutorManager.answerQuestion(
        context, qa.question, ChatAiMode.CURRICULUM, StudySection.CLASS_3,
        chapterContext = topic.titleBangla
      )
      assertFalse("Online answer for ${qa.question}", answer.isOnlineGemini)
      assertTrue("Missing verified answer for ${qa.question}", answer.text.contains(qa.answer))
      val follow = AloTutorManager.answerQuestion(
        context, "bujhini aro shohoj kore bujhao", ChatAiMode.CURRICULUM,
        StudySection.CLASS_3, chapterContext = topic.titleBangla,
        parentFollowUp = AloChatEntity(
          query = qa.question, responseBangla = answer.text,
          chapterTitle = topic.titleBangla, classCategory = StudySection.CLASS_3.id
        )
      )
      assertFalse("Online follow-up for ${qa.question}", follow.isOnlineGemini)
      assertTrue("Missing follow-up for ${qa.question}", follow.isFollowUpResponse)
      assertTrue("Unexplained follow-up for ${qa.question}", follow.text.contains("সহজ করে বুঝি"))
      }
    }
  }

  @Test
  fun `class four books have complete offline questions visuals and contextual follow ups`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val level = BangladeshCurriculumRepository.featuredClassLevels.single { it.levelNumber == 4 }
    assertEquals(setOf("c4_bangla", "c4_english", "c4_math", "c4_science", "c4_social", "c4_religion",
      "c4_hindu", "c4_buddhist", "c4_christian"),
      level.subjects.map { it.id }.toSet())
    level.subjects.filterNot { it.id.substringAfterLast('_') in setOf("hindu", "buddhist", "christian") }.forEach { subject ->
      subject.topics.forEach { topic ->
        assertTrue("Expected offline QAs for ${topic.id}", topic.qaList.size >= 3)
        assertTrue("No visual pages for ${topic.id}", topic.visualExercises.isNotEmpty())
        topic.visualExercises.forEach { exercise ->
          val path = exercise.imageAssetPath
          assertTrue("Missing image path in ${topic.id}", !path.isNullOrBlank())
          context.assets.open(path!!).use { assertTrue("Unreadable image asset $path", it.read() >= 0) }
        }
        val answer = AloTutorManager.answerQuestion(context, topic.qaList.first().question,
          ChatAiMode.CURRICULUM, StudySection.CLASS_4, chapterContext = topic.titleBangla)
        assertFalse("Online answer for ${topic.id}", answer.isOnlineGemini)
        assertTrue("Wrong answer for ${topic.id}: ${answer.text}", answer.text.contains(topic.qaList.first().answer))
        val follow = AloTutorManager.answerQuestion(context, "Bujhini, aro shohoj kore bujhao",
          ChatAiMode.CURRICULUM, StudySection.CLASS_4, chapterContext = topic.titleBangla,
          parentFollowUp = AloChatEntity(query = topic.qaList.first().question,
            responseBangla = answer.text, chapterTitle = topic.titleBangla,
            classCategory = StudySection.CLASS_4.id))
        assertFalse("Online follow-up for ${topic.id}", follow.isOnlineGemini)
        assertTrue("Missing follow-up for ${topic.id}", follow.isFollowUpResponse)
        assertTrue("Unexplained follow-up for ${topic.id}", follow.text.contains("সহজ করে বুঝি"))
      }
    }
  }

  @Test
  fun `class five books have complete offline questions visuals and contextual follow ups`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val level = BangladeshCurriculumRepository.featuredClassLevels.single { it.levelNumber == 5 }
    assertEquals(setOf("c5_bangla", "c5_english", "c5_math", "c5_science", "c5_social", "c5_religion",
      "c5_hindu", "c5_buddhist", "c5_christian", "c5_scholarship"),
      level.subjects.map { it.id }.toSet())
    level.subjects.filterNot {
      it.id.substringAfterLast('_') in setOf("hindu", "buddhist", "christian", "scholarship")
    }.forEach { subject ->
      subject.topics.forEach { topic ->
        assertTrue("Expected offline QAs for ${topic.id}", topic.qaList.size >= 3)
        assertTrue("No visual pages for ${topic.id}", topic.visualExercises.isNotEmpty())
        topic.visualExercises.forEach { exercise ->
          val path = exercise.imageAssetPath
          assertTrue("Missing image path in ${topic.id}", !path.isNullOrBlank())
          context.assets.open(path!!).use { assertTrue("Unreadable image asset $path", it.read() >= 0) }
        }
        val answer = AloTutorManager.answerQuestion(context, topic.qaList.first().question,
          ChatAiMode.CURRICULUM, StudySection.CLASS_5, chapterContext = topic.titleBangla)
        assertFalse("Online answer for ${topic.id}", answer.isOnlineGemini)
        assertTrue("Wrong answer for ${topic.id}: ${answer.text}", answer.text.contains(topic.qaList.first().answer))
        val follow = AloTutorManager.answerQuestion(context, "Bujhini, aro shohoj kore bujhao",
          ChatAiMode.CURRICULUM, StudySection.CLASS_5, chapterContext = topic.titleBangla,
          parentFollowUp = AloChatEntity(query = topic.qaList.first().question,
            responseBangla = answer.text, chapterTitle = topic.titleBangla,
            classCategory = StudySection.CLASS_5.id))
        assertFalse("Online follow-up for ${topic.id}", follow.isOnlineGemini)
        assertTrue("Missing contextual follow-up for ${topic.id}", follow.isFollowUpResponse)
      }
    }
  }

  @Test
  fun `featured question and explanation follow up stay in same lesson offline`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val topic = BangladeshCurriculumRepository.featuredClassLevels.first().subjects
      .first { it.id == "c1_bangla" }.topics.first()
    val answer = AloTutorManager.answerQuestion(
      context, topic.qaList.first().question, ChatAiMode.CURRICULUM,
      StudySection.CLASS_1, chapterContext = topic.titleBangla
    )
    assertFalse(answer.isOnlineGemini)
    assertTrue(answer.text.contains(topic.titleBangla))
    val followUp = AloTutorManager.answerQuestion(
      context, "বুঝিনি, আরেকভাবে বোঝাও", ChatAiMode.CURRICULUM,
      StudySection.CLASS_1, chapterContext = topic.titleBangla,
      parentFollowUp = AloChatEntity(
        query = topic.qaList.first().question,
        responseBangla = answer.text,
        chapterTitle = topic.titleBangla,
        classCategory = StudySection.CLASS_1.id
      )
    )
    assertFalse(followUp.isOnlineGemini)
    assertTrue(followUp.text.contains("সহজ করে বুঝি"))
    assertTrue(followUp.text.contains(topic.qaList.first().answer))
    assertTrue(followUp.text.contains("উদাহরণ"))
    val banglishFollowUp = AloTutorManager.answerQuestion(
      context, "Bujhini, aro bujhao", ChatAiMode.CURRICULUM,
      StudySection.CLASS_1, chapterContext = topic.titleBangla,
      parentFollowUp = AloChatEntity(query = topic.qaList.first().question,
        responseBangla = answer.text, classCategory = StudySection.CLASS_1.id)
    )
    assertTrue(banglishFollowUp.isFollowUpResponse)
    assertTrue(banglishFollowUp.text.contains("মূল কথা"))
  }

  @Test
  fun `first grade maths follow up explains the selected lesson offline`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val topic = BangladeshCurriculumRepository.featuredClassLevels.first().subjects
      .first { it.id == "c1_math" }.topics.first()
    val reply = AloTutorManager.answerQuestion(
      context, "bujhini", ChatAiMode.CURRICULUM, StudySection.CLASS_1,
      chapterContext = topic.titleBangla,
      parentFollowUp = AloChatEntity(query = topic.qaList.first().question,
        responseBangla = topic.qaList.first().answer,
        classCategory = StudySection.CLASS_1.id)
    )
    assertFalse(reply.isOnlineGemini)
    assertTrue(reply.text.contains("সহজ করে বুঝি"))
    assertTrue(reply.text.contains("তুলনা"))
  }

  @Test
  fun `open question without internet asks for connection instead of guessing`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val reply = AloTutorManager.answerQuestion(
      context, "What is the capital of Kenya?", ChatAiMode.GENERAL
    )
    assertFalse(reply.isOnlineGemini)
    assertTrue(reply.text.contains("cannot reach the internet"))
    assertFalse(reply.text.contains("Punctuation"))
  }

  @Test
  fun `teacher and parent general question offline asks for internet in Bangla`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val reply = AloTutorManager.answerQuestion(
      context, "শিশুকে পড়ায় আগ্রহী করব কীভাবে?", ChatAiMode.GENERAL,
      StudySection.UNIVERSAL
    )
    assertFalse(reply.isOnlineGemini)
    assertTrue(reply.text.contains("ইন্টারনেট সংযোগ পাওয়া যাচ্ছে না"))
    assertFalse(reply.text.contains("বাছাই করা টপিক"))
  }

  @Test
  fun `all studio starter questions have specific offline answers`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prompts = StudioPromptGuide.parentQuestions + StudioPromptGuide.teacherQuestions +
      StudioPromptGuide.PARENT_OVERVIEW + StudioPromptGuide.TEACHER_OVERVIEW
    prompts.forEach { prompt ->
      val reply = AloTutorManager.answerQuestion(context, prompt, ChatAiMode.GENERAL)
      assertFalse("Unexpected online route: $prompt", reply.isOnlineGemini)
      assertTrue("Missing specific guide: $prompt", reply.text.length > 90)
      assertFalse("Asked for internet: $prompt", reply.text.contains("ইন্টারনেট সংযোগ চালু করুন"))
    }
    val routine = AloTutorManager.answerQuestion(context,
      StudioPromptGuide.parentQuestions.first(), ChatAiMode.GENERAL)
    assertTrue(routine.text.contains("বাড়িতে পড়ার সহজ রুটিন"))
    assertFalse(routine.text.contains("বাতিতে"))
  }

  @Test
  fun `studio follow up remains specific and offline`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prompt = StudioPromptGuide.parentQuestions.first()
    val reply = AloTutorManager.answerQuestion(context, "bujhini", ChatAiMode.GENERAL,
      parentFollowUp = AloChatEntity(query = prompt, responseBangla = "", classCategory = StudySection.UNIVERSAL.id))
    assertFalse(reply.isOnlineGemini)
    assertTrue(reply.text.contains("বিরতি"))
    assertTrue(reply.text.contains("উদাহরণ"))
  }

  @Test
  fun `every quiz card opens a specific offline explanation and follow up`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    QuizRepository.questions.forEach { question ->
      val first = AloTutorManager.answerQuestion(context, question.questionBangla, ChatAiMode.GENERAL,
        chapterContext = question.questionBangla)
      assertFalse("Online quiz: ${question.id}", first.isOnlineGemini)
      assertTrue("Wrong quiz answer: ${question.id}", first.text.contains(question.options[question.correctIndex]))
      assertTrue("Missing quiz explanation: ${question.id}", first.text.contains(question.explanationBangla))
      val follow = AloTutorManager.answerQuestion(context, "bujhini", ChatAiMode.GENERAL,
        chapterContext = question.questionBangla,
        parentFollowUp = AloChatEntity(query = question.questionBangla, responseBangla = first.text,
          classCategory = StudySection.UNIVERSAL.id))
      assertFalse("Online quiz follow up: ${question.id}", follow.isOnlineGemini)
      assertTrue("Missing contextual follow up: ${question.id}", follow.text.contains(question.explanationBangla))
    }
  }

  @Test
  fun `every formula card opens an offline worked example and follow up`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    FormulaRepository.formulas.forEach { formula ->
      val first = AloTutorManager.answerQuestion(context, formula.relatedQuery, ChatAiMode.CURRICULUM,
        StudySection.CLASS_5, chapterContext = formula.titleBangla)
      assertFalse("Online formula: ${formula.id}", first.isOnlineGemini)
      assertTrue("Missing formula: ${formula.id}", first.text.contains(formula.formulaSymbol))
      assertTrue("Missing example: ${formula.id}", first.text.contains(formula.workedExampleBangla))
      val follow = AloTutorManager.answerQuestion(context, "aro bujhao", ChatAiMode.CURRICULUM,
        StudySection.CLASS_5, chapterContext = formula.titleBangla,
        parentFollowUp = AloChatEntity(query = formula.relatedQuery, responseBangla = first.text,
          classCategory = StudySection.CLASS_5.id))
      assertFalse("Online formula follow up: ${formula.id}", follow.isOnlineGemini)
      assertTrue("Missing contextual formula follow up: ${formula.id}", follow.text.contains(formula.workedExampleBangla))
    }
    assertTrue(OfflineReferenceGuide.formulaAnswer("ভাগের মৌলিক সূত্র (নিঃশেষে বিভাজ্য ও অবিভাজ্য)",
      "ভাগের সূত্র ভাজ্য ভাজক ভাগফল ভাগশেষ", false)!!.contains("৪৫"))
  }

  @Test
  fun `expanded class five title provides reviewed teacher guide`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val reply = AloTutorManager.answerQuestion(
      context = context,
      query = "শিক্ষক কীভাবে পড়াবেন?",
      mode = ChatAiMode.CURRICULUM,
      section = StudySection.CLASS_5,
      chapterContext = "জীবের আবাসস্থল",
      parentFollowUp = AloChatEntity(
        query = "জীবের আবাসস্থল সম্পর্কে বলো",
        responseBangla = "অধ্যায়ের যাচাইকৃত পাঠ এখনো অন্তর্ভুক্ত নেই।",
        chapterTitle = "জীবের আবাসস্থল",
        classCategory = StudySection.CLASS_5.id
      )
    )
    assertTrue(reply.text.contains("জীবের আবাসস্থল — শিক্ষক সহায়িকা"))
    assertFalse(reply.text.contains("যাচাই করা হয়নি"))
    assertFalse(reply.isOnlineGemini)
  }

  @Test
  fun `curriculum follow up stays offline`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val reply = AloTutorManager.answerQuestion(
      context = context,
      query = "আরও সহজ করে বলো",
      mode = ChatAiMode.CURRICULUM,
      section = StudySection.CLASS_2,
      parentFollowUp = AloChatEntity(
        query = "২ + ৩ কত?",
        responseBangla = "২ + ৩ = ৫।",
        classCategory = StudySection.CLASS_2.id
      )
    )
    assertFalse(reply.isOnlineGemini)
    assertTrue(reply.text.contains("২ + ৩ = ৫"))
  }

  @Test
  fun `verified counting page answers offline without unlocking other pages`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val verified = AloTutorManager.answerQuestion(
      context, "পৃষ্ঠা ১৫-এর উত্তর বলো", ChatAiMode.CURRICULUM,
      StudySection.CLASS_1, chapterContext = "গণনা"
    )
    assertFalse(verified.isOnlineGemini)
    assertTrue(verified.text.contains("৬টি গাজর"))
    assertTrue(verified.text.contains("PDF পৃষ্ঠা ১৫"))

    val unknown = AloTutorManager.answerQuestion(
      context, "পৃষ্ঠা ৯৯-এর উত্তর বলো", ChatAiMode.CURRICULUM,
      StudySection.CLASS_1, chapterContext = "গণনা"
    )
    assertTrue(unknown.text.contains("এখনো আলাদাভাবে যাচাই করা হয়নি"))
    assertFalse(unknown.isOnlineGemini)

    val wrongChapter = AloTutorManager.answerQuestion(
      context, "পৃষ্ঠা ১৬-এর উত্তর বলো", ChatAiMode.CURRICULUM,
      StudySection.CLASS_1, chapterContext = "গণনা"
    )
    assertTrue(wrongChapter.text.contains("এখনো আলাদাভাবে যাচাই করা হয়নি"))
    val correctChapter = AloTutorManager.answerQuestion(
      context, "পৃষ্ঠা ১৬-এর উত্তর বলো", ChatAiMode.CURRICULUM,
      StudySection.CLASS_1, chapterContext = "সংখ্যা (১ থেকে ১০)"
    )
    assertTrue(correctChapter.text.contains("৪টি পেঁয়াজ"))
  }
}
