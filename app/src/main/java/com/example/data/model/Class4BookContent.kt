package com.example.data.model

/** Reviewed 2026 NCTB Class Four book structure and deterministic offline support. */
object Class4BookContent {
    private data class Chapter(val title: String, val start: Int, val end: Int, val summary: String)
    private fun bn(n: Int) = n.toString().map { if (it.isDigit()) ('০'.code + it.code - '0'.code).toChar() else it }.joinToString("")
    private fun visuals(book: String, start: Int, end: Int, source: String) = (start..end).map { page ->
        VisualExercise(questionBangla = "পৃষ্ঠা ${bn(page)} দেখে মূল প্রশ্ন বা কাজটি বলো।",
            answerBangla = "নির্দেশনাটি পড়ো, ছবি/ছক/উদাহরণ পর্যবেক্ষণ করো, ধাপে ধাপে কাজটি করো এবং বইয়ের তথ্য দিয়ে যাচাই করো।",
            sourceBangla = "চতুর্থ শ্রেণির $source, ২০২৬ শিক্ষাবর্ষ, PDF পৃষ্ঠা ${bn(page)}",
            imageAssetPath = "books/class4/$book/page_${page.toString().padStart(3, '0')}.webp",
            pageQuestions = FullOfflineQuestionBank.forPage(4, book, page))
    }
    private fun make(id: String, c: Chapter, book: String, source: String, method: String) = CurriculumTopic(
        id=id, titleBangla=c.title, summaryBangla=c.summary,
        detailsBangla="${c.title} পাঠে ${c.summary} বইয়ের ব্যাখ্যা, ছবি ও কাজের মাধ্যমে ধারণাটি শেখা হয়।",
        learningObjectives=listOf("মূল ধারণা শনাক্ত করা","বইয়ের তথ্য দিয়ে উত্তর দেওয়া","কারণ ও উদাহরণ নিজের ভাষায় ব্যাখ্যা করা"),
        learningMethodology=method, formulaOrRule="প্রশ্ন বুঝি → তথ্য খুঁজি → ধাপে ধাপে উত্তর দিই → যাচাই করি।",
        keyPoints=listOf(c.summary,"প্রশ্নের গুরুত্বপূর্ণ শব্দ ও একক খেয়াল করো।","উত্তরের কারণ বা উদাহরণ দাও।"),
        exampleBangla="বইয়ের একটি উদাহরণ দেখে কী শিখলে তা দুইটি পূর্ণ বাক্যে বলো।",
        qaList=listOf(
            TopicQA("$source বইয়ে ${c.title} কী বা কী বিষয়ে?",c.summary),
            TopicQA("${c.title} সহজ করে বুঝিয়ে দাও।","${c.summary} প্রথমে বইয়ের ছবি বা উদাহরণ দেখো, তারপর মূল ধারণাটি নিজের ভাষায় বলো এবং একটি নতুন উদাহরণ দাও।"),
            TopicQA("এই পাঠ কীভাবে অনুশীলন করব?","$method একটি কাজ শেষ করে কেন উত্তরটি ঠিক, সেটিও বলো।")
        ) + FullOfflineQuestionBank.forPages(4,book,c.start,c.end),
        parentTeachingTips=listOf("শিশুকে আগে নিজে পর্যবেক্ষণ ও উত্তর দিতে দিন; পরে বইয়ের সঙ্গে মিলিয়ে দিন।","একবারে একটি ছোট প্রশ্ন করুন এবং উত্তরের কারণ বলতে উৎসাহ দিন।"),
        commonMistakes=listOf("প্রশ্ন সম্পূর্ণ না পড়া","ছবি, ধাপ, একক বা গুরুত্বপূর্ণ শব্দ বাদ দেওয়া"),
        homeActivity="বাড়ি বা আশপাশ থেকে পাঠটির একটি নিরাপদ বাস্তব উদাহরণ খুঁজে ব্যাখ্যা করো।",
        visualExercises=visuals(book,c.start,c.end,source))

    private fun chapters(titles: List<String>, starts: List<Int>, last: Int, summary: (String)->String) = titles.mapIndexed { i,t ->
        Chapter(t, starts[i], starts.getOrNull(i+1)?.minus(1) ?: last, summary(t))
    }

    private val banglaTitles=listOf("রূপময় বাংলাদেশ","কেমন বড়াই","শুভেচ্ছা জানাই","ভাষার খেলা","জন্মেছি এই দেশে","দোয়েল পাখি","একুশের কবিতা","চিল ও বিড়াল","বৃষ্টির ছড়া","ময়নামতি","বাঘ থেকো শিয়ালের ছানা","কাজলা দিদি","দানবীর মুহসিন","অমল ও দইওয়ালা","প্রিয় স্বাধীনতা","সোনার থালা","লিচু-চোর","মহীয়সী নারী","লাল জামা","চারদিকে দেখি","ভয় পেয়ো না","খলিফা উমর (রা.)","পাঠাগারের সদস্য হই")
    private val banglaStarts=listOf(10,15,21,26,33,38,43,47,55,62,67,72,78,82,87,93,98,103,108,112,119,124,129)
    val banglaTopics=chapters(banglaTitles,banglaStarts,134){"পাঠটি বুঝে পড়া, নতুন শব্দ শেখা, মূলভাব বলা এবং শুদ্ধ বাক্যে নিজের ভাব প্রকাশ করা।"}.mapIndexed{i,c->make("nctb2026_c4_bangla_${i+1}",c,"bangla","আমার বাংলা বই","দেখি → পড়ি → মূলভাব বলি → লিখি → যাচাই করি।")}

    private val englishTitles=listOf("Introducing One Another","Have a Great Day","My School Assembly","Enjoying Annual Sports","The Little Doctor","Sounds and Numbers","Telling the Time","My Family","Gardening","A Letter to a Friend","A Holiday Trip","Making Friends","Planning to Visit the Book Fair","Poem: Tears of the Earth","Environment Pollution","Talking with a Nepalese Boy","The Land of Mountains","Caring for Others")
    private val englishStarts=listOf(7,12,18,24,28,33,40,43,45,47,52,57,63,70,73,77,83,88)
    val englishTopics=chapters(englishTitles,englishStarts,98){"Listening, speaking, reading and writing are practised through meaningful everyday English."}.mapIndexed{i,c->make("nctb2026_c4_english_${i+1}",c,"english","English for Today","Look → Listen → Say → Read → Write → Use.")}

    private val mathTitles=listOf("সংখ্যা ও স্থানীয় মান","যোগ ও বিয়োগ","গুণ","ভাগ","গাণিতিক বাক্য","গুণিতক ও গুণনীয়ক","সাধারণ ভগ্নাংশ","দশমিক ভগ্নাংশ","পরিমাপ","জ্যামিতি","উপাত্ত সংগ্রহ ও বিন্যস্তকরণ")
    private val mathStarts=listOf(8,30,48,56,74,88,104,118,128,150,176)
    val mathTopics=chapters(mathTitles,mathStarts,182){"বাস্তব উদাহরণ, ছবি ও ধাপে ধাপে হিসাবের মাধ্যমে $it শেখা ও উত্তর যাচাই করা।"}.mapIndexed{i,c->make("nctb2026_c4_math_${i+1}",c,"math","প্রাথমিক গণিত","প্রশ্ন বুঝি → উপকরণ/ছবি দিয়ে করি → হিসাব লিখি → উল্টোভাবে যাচাই করি।")}

    private val scienceTitles=listOf("জীবের বৃদ্ধি ও প্রজনন","স্বাস্থ্য সুরক্ষা","দুর্ঘটনা ও প্রাথমিক চিকিৎসা","পদার্থ","শক্তি","গতি ও বল","প্রাকৃতিক সম্পদ","মহাবিশ্ব","আবহাওয়া","তথ্য ও প্রযুক্তি","সমস্যা সমাধানে তথ্য ও যোগাযোগ প্রযুক্তি")
    private val scienceStarts=listOf(7,18,32,43,59,73,78,96,115,132,145)
    val scienceTopics=chapters(scienceTitles,scienceStarts,158){"পর্যবেক্ষণ, অনুসন্ধান ও নিরাপদ পরীক্ষণের মাধ্যমে $it-এর বৈজ্ঞানিক ধারণা বোঝা।"}.mapIndexed{i,c->make("nctb2026_c4_science_${i+1}",c,"science","প্রাথমিক বিজ্ঞান","প্রশ্ন করি → পর্যবেক্ষণ/পরীক্ষণ করি → ফল লিখি → প্রমাণ দিয়ে ব্যাখ্যা করি।")}

    private val socialTitles=listOf("প্রাকৃতিক ও সামাজিক পরিবেশ","ধর্মীয় সম্প্রীতি","ছেলে-মেয়ে সমতা","বাংলাদেশের ক্ষুদ্র নৃগোষ্ঠী","বাংলাদেশের মুক্তিযুদ্ধের পটভূমি","বাংলাদেশের চিরায়ত সাংস্কৃতিক উৎসব","এশিয়ার ভৌগোলিক ও সাংস্কৃতিক বৈচিত্র্য","সামাজিক দায়িত্ব ও নাগরিক অধিকার","নৈতিক ও মানবিক গুণাবলি","আমাদের দেশ","বাংলাদেশের জনসংখ্যা","শ্রম ও পেশা","অর্থ ও সম্পদ","জরুরি পরিস্থিতি মোকাবিলা")
    private val socialStarts=listOf(6,14,23,30,40,47,54,64,76,82,85,100,107,113)
    val socialTopics=chapters(socialTitles,socialStarts,122){"$it সম্পর্কে বইভিত্তিক তথ্য, দায়িত্বশীল আচরণ এবং বাস্তব জীবনের প্রয়োগ।"}.mapIndexed{i,c->make("nctb2026_c4_social_${i+1}",c,"bgs","বাংলাদেশ ও বিশ্বপরিচয়","তথ্য দেখি → ব্যক্তি/স্থান/ঘটনা চিনি → কারণ ও দায়িত্ব বলি → উদাহরণ দিই।")}

    private val religionTitles=listOf("আকাইদ ও ইবাদত","নবি, রাসুল ও সাহাবিগণের জীবনচরিত","নৈতিক ও মানবিক গুণাবলি অর্জন","ধর্মীয় সম্প্রীতি","জীবজগৎ ও প্রকৃতির প্রতি ভালোবাসা")
    private val religionStarts=listOf(6,33,52,66,74)
    val religionTopics=chapters(religionTitles,religionStarts,82){"$it বিষয়ে ইসলাম শিক্ষার মৌলিক জ্ঞান, উত্তম চরিত্র ও দৈনন্দিন জীবনে সঠিক আচরণ।"}.mapIndexed{i,c->make("nctb2026_c4_religion_${i+1}",c,"islam","ইসলাম শিক্ষা","পড়ি → অর্থ বুঝি → জীবনঘনিষ্ঠ উদাহরণ দেখি → ভালো আচরণে প্রয়োগ করি।")}
}
