package com.example.data.model

/** Reviewed book structure for the 2026 Class Two Bangla, English and Mathematics books. */
object Class2BookContent {
    private data class Lesson(val title: String, val start: Int, val end: Int)

    private fun pageQuestions(book: String, page: Int): List<TopicQA> {
        if (book == "english" && page == 60) return listOf(
            TopicQA("What was in the nest on Sunday?", "There was one egg in the nest on Sunday."),
            TopicQA("When did the two birds come out of the nest?", "The two little birds came out of the nest on Monday."),
            TopicQA("What did the birds do on Thursday?", "On Thursday, the two little birds sat on a branch."),
            TopicQA("When did the birds fly away?", "The little birds happily flew away on Saturday.")
        )
        return FullOfflineQuestionBank.forPage(2, book, page)
    }

    private fun visuals(book: String, start: Int, end: Int, source: String): List<VisualExercise> =
        (start..end).map { page ->
            val questions = pageQuestions(book, page)
            VisualExercise(
                questionBangla = "পৃষ্ঠা ${bn(page)} মন দিয়ে দেখো। ছবি, লেখা বা সংখ্যার কাজটি নিজের ভাষায় বলো।",
                answerBangla = if (questions.isEmpty())
                    "করণীয়: প্রথমে পৃষ্ঠার নির্দেশনা পড়ো বা বড় কারও কাছে শুনো। ছবি থাকলে বাম থেকে ডানে পর্যবেক্ষণ করো। তারপর বলা, পড়া, লেখা, গোনা বা মিল করার কাজটি ধাপে ধাপে নিজে করো এবং শেষে আবার যাচাই করো।"
                else "এই পৃষ্ঠার ${questions.size}টি প্রশ্নের যাচাইকৃত উত্তর নিচে আলাদাভাবে দেওয়া আছে।",
                sourceBangla = "দ্বিতীয় শ্রেণির $source, ২০২৬ শিক্ষাবর্ষ, PDF পৃষ্ঠা ${bn(page)}",
                imageAssetPath = "books/class2/$book/page_${page.toString().padStart(3, '0')}.webp",
                pageQuestions = questions
            )
        }

    private fun languageTopic(id: String, lesson: Lesson, book: String, source: String, english: Boolean = false): CurriculumTopic {
        val focus = if (english) "শোনা, বলা, পড়া ও ছোট বাক্যে ব্যবহার" else "শুনে-বুঝে বলা, সাবলীলভাবে পড়া এবং নিজের ভাষায় লেখা"
        val visualPages = visuals(book, lesson.start, lesson.end, source)
        val explanation = if (english)
            "ছবি ও কথোপকথন দেখে শব্দগুলোর অর্থ বোঝো। মডেল বাক্যটি ধীরে শোনো, বলো, জোড়ায় অনুশীলন করো এবং নিজের তথ্য দিয়ে একটি ছোট বাক্য তৈরি করো।"
        else "পাঠের ছবি ও শিরোনাম দেখে আগে অনুমান করো। এরপর অংশ করে পড়ো, চরিত্র বা মূল বিষয় চিহ্নিত করো এবং নিজের ভাষায় দুই-তিনটি বাক্যে বলো।"
        return CurriculumTopic(
            id = id,
            titleBangla = lesson.title,
            summaryBangla = "$focus—ছবি ও বইয়ের কাজের মাধ্যমে ${lesson.title} শিখি।",
            detailsBangla = explanation,
            learningObjectives = listOf("পাঠের মূল বিষয় শনাক্ত করা", "প্রশ্নের প্রাসঙ্গিক উত্তর দেওয়া", "নিজের ভাষায় শেখাটি প্রকাশ করা"),
            learningMethodology = "দেখি → শুনি → বলি → পড়ি → লিখি → যাচাই করি।",
            formulaOrRule = if (english) "Listen → Repeat → Practise → Use" else "শিরোনাম, মূল কথা, উদাহরণ ও নিজের বাক্য—এই চার ধাপে শিখি।",
            keyPoints = listOf("প্রথমে অর্থ বুঝি", "সম্পূর্ণ বাক্যে উত্তর দিই", "বইয়ের ছবি থেকে সূত্র নিই"),
            exampleBangla = if (english) "ছবিটি দেখে একটি সঠিক English sentence বলো এবং বাংলা অর্থ বোঝাও।" else "পাঠটি পড়ে কে, কী, কোথায়—এই তিনটি প্রশ্নের উত্তর বলো।",
            qaList = listOf(
                TopicQA("${lesson.title} পাঠে কী শিখব?", "এই পাঠে আমরা $focus অনুশীলন করব। বইয়ের ছবিগুলো দেখে মূল বিষয়টি সহজে বোঝা যাবে।"),
                TopicQA("${lesson.title} সহজ করে বুঝিয়ে দাও।", explanation),
                TopicQA("এই পাঠ কীভাবে অনুশীলন করব?", "একটি পৃষ্ঠা দেখো, মূল শব্দ বা ভাবটি বলো, একটি প্রশ্নের উত্তর দাও এবং শেষে নিজের মতো একটি বাক্য বলো বা লেখো।")
            ) + FullOfflineQuestionBank.forPages(2, book, lesson.start, lesson.end) +
                visualPages.flatMap { it.pageQuestions }.filterNot { pageQa ->
                    FullOfflineQuestionBank.forPages(2, book, lesson.start, lesson.end)
                        .any { it.question.equals(pageQa.question, ignoreCase = true) }
                },
            parentTeachingTips = listOf("শিশুকে আগে ছবি দেখে বলতে দিন; ভুল হলে সরাসরি থামিয়ে না দিয়ে সঠিক বাক্যটি মডেল করুন।", "একবারে একটি ছোট কাজ দিন এবং উত্তর মুখস্থ না করিয়ে কারণ জিজ্ঞাসা করুন।"),
            commonMistakes = listOf("ছবি না দেখে আন্দাজে উত্তর দেওয়া", "অর্থ না বুঝে বাক্য মুখস্থ বলা"),
            homeActivity = "পরিবারের কাউকে আজকের পৃষ্ঠাটি দেখিয়ে শেখা বিষয়টি নিজের ভাষায় বলো।",
            visualExercises = visualPages
        )
    }

    private val banglaLessons = listOf(
        Lesson("আমার পরিচয়",10,11), Lesson("স্কুলে কেমন লাগছে",12,14), Lesson("আমার বাড়ি আমার কাজ",15,15),
        Lesson("ডালিমকুমার ও কঙ্কাবতী",16,20), Lesson("আবার পড়ি বর্ণমালা",21,23), Lesson("আয় দেখে যা নাচ",24,24),
        Lesson("কারচিহ্ন দিয়ে শব্দ বানাই",25,26), Lesson("সিংহ আর ইঁদুরের গল্প",27,28), Lesson("দেখে বুঝে কাজ করি",29,29),
        Lesson("যুক্তবর্ণ শিখি",30,30), Lesson("একুশের গান",31,31), Lesson("ফলা শিখি",32,35), Lesson("রেফ চিনি",36,36),
        Lesson("নানা রকম লেখা",37,38), Lesson("কাজের আনন্দ",39,41), Lesson("বাক্য লিখি",42,42), Lesson("রাজুর আঁকা ছবি",43,43),
        Lesson("গ্রাম ও শহর",44,46), Lesson("প্রজাপতি",47,48), Lesson("বিড়াল ছানা",49,51), Lesson("ছয় ঋতু",52,56),
        Lesson("নববর্ষ",57,58), Lesson("আমাদের ছোটো নদী",59,60), Lesson("নিজের মতো লিখি",61,62),
        Lesson("সবাই মিলে কাজ করি",63,64), Lesson("মুক্তিসেনা",65,66), Lesson("দুখু মিয়ার জীবন",67,68),
        Lesson("স্কুলের মাঠে",69,71), Lesson("বাক্য নিয়ে খেলা",72,74)
    )

    private val englishUnits = listOf(
        Lesson("Unit 1 · Greetings, Introductions and Farewells",7,11),
        Lesson("Unit 2 · The Alphabet, Words and Numbers",12,36),
        Lesson("Unit 3 · Commands, Instructions and Requests",37,41),
        Lesson("Unit 4 · Asking and Answering Questions",42,47),
        Lesson("Unit 5 · Days of the Week",48,60),
        Lesson("Unit 6 · Let's Play with Sounds",61,68),
        Lesson("Unit 7 · Colours, Shapes and Signs",69,80),
        Lesson("Unit 8 · My Family, Friends and I",81,89),
        Lesson("Unit 9 · Animals and Birds",90,98),
        Lesson("Unit 10 · Story Time",99,106)
    )

    val banglaTopics = banglaLessons.mapIndexed { index, lesson ->
        languageTopic("nctb2026_c2_bangla_${index + 1}", lesson, "bangla", "আমার বাংলা বই")
    }
    val englishTopics = englishUnits.mapIndexed { index, lesson ->
        languageTopic("nctb2026_c2_english_${index + 1}", lesson, "english", "English for Today", true)
    }

    private data class MathChapter(val title: String, val start: Int, val end: Int, val summary: String, val rule: String, val example: String)
    private val mathChapters = listOf(
        MathChapter("সংখ্যা ও স্থানীয় মান",10,48,"২১–১০০ পর্যন্ত সংখ্যা, তুলনা, স্থানীয় মান, জোড়-বিজোড়, প্যাটার্ন ও ক্রমবাচক সংখ্যা।","দশকের অঙ্ক × ১০ + এককের অঙ্ক = সংখ্যার মান।","৬৪ = ৬ দশক + ৪ একক।"),
        MathChapter("যোগ ও বিয়োগ",49,81,"১০০-এর মধ্যে যোগ-বিয়োগ, সম্পর্ক এবং বাস্তব সমস্যার সমাধান।","একক এককের নিচে ও দশক দশকের নিচে লিখে হিসাব করি।","৪৮ + ২৫ = ৭৩; ৭৩ − ২৫ = ৪৮।"),
        MathChapter("গুণ",82,110,"সমান দল ও বারবার যোগ থেকে গুণের ধারণা।","একই সংখ্যা যতবার যোগ হয়: সংখ্যা × বার।","৪টি দলে ৩টি করে ফল = ৩ + ৩ + ৩ + ৩ = ১২।"),
        MathChapter("জ্যামিতিক আকৃতি ও প্যাটার্ন",111,115,"পরিচিত সমতল আকৃতি এবং নিয়ম মেনে প্যাটার্ন সম্পূর্ণ করা।","বাহু, কোণ ও পুনরাবৃত্তির নিয়ম দেখে চিনি।","বৃত্ত–ত্রিভুজ–বৃত্ত–ত্রিভুজের পর বৃত্ত।"),
        MathChapter("পরিমাপ",116,129,"দৈর্ঘ্য, ওজন, তরলের আয়তন ও সময় তুলনা ও পরিমাপ।","একই একক ও একই শুরু বিন্দু ব্যবহার করি।","স্কেল দিয়ে দৈর্ঘ্য সেন্টিমিটারে মাপি।"),
        MathChapter("বাংলাদেশি মুদ্রা",130,133,"প্রচলিত টাকা ও পয়সা চিনে সহজ কেনাকাটার হিসাব।","দামগুলোর যোগ = মোট; দেওয়া টাকা − দাম = ফেরত।","২০ টাকা থেকে ১৩ টাকা খরচ হলে ফেরত ৭ টাকা।"),
        MathChapter("উপাত্ত সংগ্রহ ও সাজানো",134,137,"বস্তু গুনে তালিকা, ট্যালি বা ছবির মাধ্যমে তথ্য সাজানো।","একটি চিহ্ন একটি বস্তুকে বোঝালে চিহ্ন গুনেই মোট পাই।","৩টি আমের জন্য তিনটি ট্যালি দাগ।")
    )
    val mathTopics = mathChapters.mapIndexed { index, chapter ->
        CurriculumTopic(
            id = "nctb2026_c2_math_${index + 1}", titleBangla = chapter.title,
            summaryBangla = chapter.summary, detailsBangla = "বাস্তব বস্তু, বইয়ের ছবি ও ধাপে ধাপে অনুশীলনের মাধ্যমে ${chapter.title} বোঝা হয়। ${chapter.summary}",
            learningObjectives = listOf("ধারণাটি ছবি ও বস্তু দিয়ে বোঝা", "নিয়ম ব্যবহার করে সমাধান করা", "উত্তর যাচাই করা"),
            learningMethodology = "ছবি দেখি → বস্তু দিয়ে করি → সংখ্যায় লিখি → উল্টোভাবে যাচাই করি।",
            formulaOrRule = chapter.rule, keyPoints = listOf(chapter.rule, "প্রশ্নে কী চাওয়া হয়েছে আগে বলো।", "শেষে উত্তরটি যুক্তিসংগত কি না যাচাই করো।"),
            exampleBangla = chapter.example,
            qaList = listOf(
                TopicQA("${chapter.title} কী?", chapter.summary),
                TopicQA("${chapter.title} সহজ করে বুঝিয়ে দাও।", "প্রথমে বইয়ের ছবিটি দেখো, তারপর বাস্তব বস্তু দিয়ে একই কাজ করো। নিয়ম: ${chapter.rule} উদাহরণ: ${chapter.example}"),
                TopicQA("উত্তর কীভাবে যাচাই করব?", "একই কাজ আরেকভাবে করো—গুনে, উল্টো হিসাব করে বা বাস্তব বস্তু মিলিয়ে দেখো। দুইভাবে একই ফল এলে উত্তরটি ঠিক।")
            ) + FullOfflineQuestionBank.forPages(2, "math", chapter.start, chapter.end),
            parentTeachingTips = listOf("বোতামের ঢাকনা, কাঠি, কয়েন বা ঘড়ির মতো নিরাপদ উপকরণ ব্যবহার করুন।", "শিশুকে পদ্ধতি মুখে বলতে দিন; শুধু চূড়ান্ত উত্তরকে গুরুত্ব দেবেন না।"),
            commonMistakes = listOf("স্থানীয় মান বা একক না মিলিয়ে হিসাব করা", "ছবির কোনো বস্তু বাদ দেওয়া বা দুবার গোনা"),
            homeActivity = "বাড়ির নিরাপদ বস্তু ব্যবহার করে আজকের নিয়মের একটি নতুন উদাহরণ বানাও।",
            visualExercises = visuals("math", chapter.start, chapter.end, "প্রাথমিক গণিত")
        )
    }

    private fun bn(value: Int): String = value.toString().map { if (it in '0'..'9') ('০'.code + it.code - '0'.code).toChar() else it }.joinToString("")
}
