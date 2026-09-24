package com.example.ai

/**
 * Categorized study stages and sections for Alo AI Tutor (Class 1 to 5 Primary Education).
 * Enables class-aware pedagogical responses and isolated or unified chat feeds.
 */
enum class StudySection(
    val id: String,
    val titleBangla: String,
    val subtitleBangla: String,
    val gradeBadge: String,
    val emoji: String,
    val pedagogyPrompt: String,
    val suggestionChips: List<String>
) {
    ALL(
        id = "ALL",
        titleBangla = "সব শ্রেণি",
        subtitleBangla = "১ম থেকে ৫ম শ্রেণির সংরক্ষিত পাঠ ও আলোচনা",
        gradeBadge = "সকল শ্রেণি (১ম-৫ম)",
        emoji = "🎒",
        pedagogyPrompt = "Provide an intuitive, clear, and age-appropriate answer matching the complexity of the primary inquiry (Class 1 to 5).",
        suggestionChips = listOf(
            "৭ এর ঘরের সহজ নামতা শেখাও",
            "গাছ কীভাবে খাদ্য তৈরি করে?",
            "গড় নির্ণয়ের সহজ সূত্র কী?",
            "যুক্তবর্ণ কীভাবে ভেঙে লিখতে হয়?"
        )
    ),

    CLASS_1(
        id = "CLASS_1",
        titleBangla = "প্রথম শ্রেণি",
        subtitleBangla = "বর্ণমালা, সহজ যোগ-বিয়োগ, ছড়া ও প্রাথমিক ইংরেজি",
        gradeBadge = "প্রথম শ্রেণি",
        emoji = "✏️",
        pedagogyPrompt = "Target: Class 1 students (age 6). Use very cheerful, simple language with fruits, toys, animals, and rhymes. Explain step by step without difficult terms.",
        suggestionChips = listOf(
            "স্বরবর্ণ ও ব্যঞ্জনবর্ণ কয়টি?",
            "৫ + ৩ যোগফল কত হয়?",
            "English Alphabet A to Z শেখাও",
            "হাত ধোয়ার সঠিক নিয়ম কী?"
        )
    ),

    CLASS_2(
        id = "CLASS_2",
        titleBangla = "দ্বিতীয় শ্রেণি",
        subtitleBangla = "নামতা, বাক্য গঠন, পরিবেশ ও ইংরেজি বাক্য",
        gradeBadge = "দ্বিতীয় শ্রেণি",
        emoji = "📖",
        pedagogyPrompt = "Target: Class 2 students (age 7). Focus on simple 1-10 multiplication tables, carrying addition/subtraction, small stories, and basic environmental hygiene.",
        suggestionChips = listOf(
            "৪ এর ঘরের নামতা বলো",
            "হাতে রেখে যোগ করার সহজ নিয়ম",
            "বিপরীত শব্দ: দিন, আলো, বড়",
            "Days of the Week ইংরেজিতে বলো"
        )
    ),

    CLASS_3(
        id = "CLASS_3",
        titleBangla = "তৃতীয় শ্রেণি",
        subtitleBangla = "প্রাথমিক গণিত, বিজ্ঞান, সমাজ ও ইংরেজি ব্যাকরণ",
        gradeBadge = "তৃতীয় শ্রেণি",
        emoji = "🌱",
        pedagogyPrompt = "Target: Class 3 students (age 8). Teach 4-digit arithmetic, fractions introduction (1/2, 1/4), living vs non-living things, and social responsibilities.",
        suggestionChips = listOf(
            "জীব ও জড়ের মধ্যে পার্থক্য কী?",
            "ভগ্নাংশ ১/২ ও ১/৪ কাকে বলে?",
            "বিশেষ্য ও সর্বনাম পদের উদাহরণ",
            "সুষম খাদ্য তালিকা কী?"
        )
    ),

    CLASS_4(
        id = "CLASS_4",
        titleBangla = "চতুর্থ শ্রেণি",
        subtitleBangla = "লসাগু-গসাগু, বিজ্ঞান, পরিবেশ, সমাজ ও ব্যাকরণ",
        gradeBadge = "চতুর্থ শ্রেণি",
        emoji = "📐",
        pedagogyPrompt = "Target: Class 4 students (age 9). Explain factors, multiples, LCM, HCF, states of matter, photosynthesis basics, and Bangladesh natural disaster management clearly.",
        suggestionChips = listOf(
            "১২ ও ১৮ এর লসাগু ও গসাগু কত?",
            "পদার্থের তিন অবস্থা কী কী?",
            "সালোকসংশ্লেষণ প্রক্রিয়া কীভাবে ঘটে?",
            "বাংলাদেশের প্রধান প্রধান নদ-নদী"
        )
    ),

    CLASS_5(
        id = "CLASS_5",
        titleBangla = "পঞ্চম শ্রেণি",
        subtitleBangla = "সমাপনী স্তর, ভগ্নাংশ, শতকরা, পরিবেশ ও মুক্তিযুদ্ধ",
        gradeBadge = "পঞ্চম শ্রেণি",
        emoji = "🏆",
        pedagogyPrompt = "Target: Class 5 students (age 10, Primary School Completion level). Follow standard NCTB PEC curriculum guidelines. Provide clear formulas for average, percentage, simple interest, geometry area, food chains, and Bangladesh Liberation War history.",
        suggestionChips = listOf(
            "গড় ও শতকরা নির্ণয়ের নিয়ম",
            "খাদ্যশৃঙ্খল ও খাদ্যজাল কী?",
            "১৯৭১ সালের মুক্তিযুদ্ধ ও মুজিবনগর সরকার",
            "বৃত্তের ব্যাস, ব্যাসার্ধ ও পরিধি"
        )
    ),

    UNIVERSAL(
        id = "UNIVERSAL",
        titleBangla = "উন্মুক্ত এআই",
        subtitleBangla = "যেকোনো সাধারণ জ্ঞান ও মুক্ত জিজ্ঞাসা",
        gradeBadge = "উন্মুক্ত এআই",
        emoji = "🌟",
        pedagogyPrompt = "Target: General learner or open inquiry. Provide clear, accurate multi-dimensional reasoning in Bangla or English.",
        suggestionChips = listOf(
            "What is the value of Pi (π)?",
            "সৌরজগতের গ্রহসমূহ সম্পর্কে বলো",
            "কম্পিউটার কীভাবে কাজ করে?",
            "আলোর বেগ কত?"
        )
    );

    companion object {
        fun fromId(id: String?): StudySection {
            return entries.find { it.id == id } ?: ALL
        }

        fun forClassNumber(classNum: Int): StudySection {
            return when (classNum) {
                1 -> CLASS_1
                2 -> CLASS_2
                3 -> CLASS_3
                4 -> CLASS_4
                5 -> CLASS_5
                else -> ALL
            }
        }
    }
}
