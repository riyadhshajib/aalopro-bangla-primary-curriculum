package com.example.data.model

/**
 * Textbook-grounded practice for Class 5 scholarship preparation.
 * These are reviewed NCTB exercises, not official scholarship questions.
 */
object Class5ScholarshipCurriculum {
    private data class PracticeSource(
        val id: String,
        val title: String,
        val book: String,
        val guidance: String,
    )

    private val sources = listOf(
        PracticeSource("bangla", "বাংলা অনুশীলন", "bangla", "পাঠ বুঝে উত্তর, শব্দার্থ ও লিখিত প্রকাশ অনুশীলন করো।"),
        PracticeSource("english", "English Practice", "english", "Read carefully and answer with short, complete sentences."),
        PracticeSource("math", "গণিত অনুশীলন", "math", "প্রশ্ন বুঝে ধাপ লিখে হিসাব করো এবং উত্তর মিলিয়ে দেখো।"),
        PracticeSource("science", "বিজ্ঞান অনুশীলন", "science", "বইয়ের ধারণা, কারণ ও উদাহরণ ব্যবহার করে উত্তর দাও।"),
        PracticeSource("bgs", "বাংলাদেশ ও বিশ্বপরিচয় অনুশীলন", "bgs", "তথ্য, কারণ ও করণীয় আলাদা করে পূর্ণ বাক্যে লেখো।"),
    )

    val subject = CurriculumSubject(
        id = "c5_scholarship",
        nameBangla = "বৃত্তি প্রস্তুতি",
        iconName = "scholarship",
        topics = sources.map { source ->
            val questions = FullOfflineQuestionBank.all(5, source.book)
                .distinctBy { it.question.trim().lowercase() }
                .take(30)
            CurriculumTopic(
                id = "c5_scholarship_${source.id}",
                titleBangla = source.title,
                summaryBangla = "যাচাই করা পঞ্চম শ্রেণির বইভিত্তিক অনুশীলন",
                detailsBangla = source.guidance + " এগুলো অফিসিয়াল বৃত্তি প্রশ্ন নয়।",
                learningObjectives = listOf(
                    "বইয়ের প্রশ্ন বুঝে নিজের ভাষায় উত্তর দেওয়া",
                    "ভুল উত্তর দেখে আবার বইয়ের ধারণা ঝালিয়ে নেওয়া",
                    "নিয়মিত ছোট অনুশীলনের মাধ্যমে প্রস্তুতি নেওয়া",
                ),
                learningMethodology = "প্রথমে নিজে উত্তর দাও, তারপর যাচাই করা উত্তর মিলিয়ে দেখো।",
                qaList = questions,
            )
        }.filter { it.qaList.isNotEmpty() },
    )

    fun mergeInto(levels: List<CurriculumClassLevel>): List<CurriculumClassLevel> = levels.map { level ->
        if (level.levelNumber != 5 || level.subjects.any { it.id == subject.id }) level
        else level.copy(subjects = level.subjects + subject)
    }
}
