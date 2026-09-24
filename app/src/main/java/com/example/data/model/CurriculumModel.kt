package com.example.data.model

data class TopicQA(
    val question: String,
    val answer: String
)

data class VisualExercise(
    val imageResId: Int = 0,
    val questionBangla: String,
    val answerBangla: String,
    val sourceBangla: String,
    val imageAssetPath: String? = null,
    /** Individually reviewed questions printed inside this book page. */
    val pageQuestions: List<TopicQA> = emptyList()
)

data class CurriculumTopic(
    val id: String,
    val titleBangla: String,
    val summaryBangla: String,
    val detailsBangla: String,
    val learningObjectives: List<String> = emptyList(),
    val learningMethodology: String? = null,
    val formulaOrRule: String? = null,
    val keyPoints: List<String> = emptyList(),
    val exampleBangla: String? = null,
    val qaList: List<TopicQA> = emptyList(),
    val parentTeachingTips: List<String> = emptyList(),
    val commonMistakes: List<String> = emptyList(),
    val homeActivity: String? = null,
    val visualExercises: List<VisualExercise> = emptyList(),
    val catalogOnly: Boolean = false
)

data class CurriculumSubject(
    val id: String,
    val nameBangla: String,
    val iconName: String, // math, science, bangla, english, social, religion, health
    val topics: List<CurriculumTopic>
)

data class CurriculumClassLevel(
    val levelNumber: Int,
    val classNameBangla: String,
    val badgeBangla: String,
    val subjects: List<CurriculumSubject>
)

enum class CurriculumStage(val id: String, val titleBangla: String, val subtitleBangla: String, val minClass: Int, val maxClass: Int) {
    PRIMARY("primary", "প্রাথমিক পর্যায় (১ম-৫ম)", "১ম থেকে ৫ম শ্রেণি (এনসিটিবি পাঠ্যক্রম)", 1, 5),
    PRIMARY_LOWER("primary_lower", "প্রারম্ভিক (১ম-২য়)", "১ম ও ২য় শ্রেণি", 1, 2),
    PRIMARY_UPPER("primary_upper", "উচ্চ প্রাথমিক (৩য়-৫ম)", "৩য়, ৪র্থ ও ৫ম শ্রেণি", 3, 5)
}
