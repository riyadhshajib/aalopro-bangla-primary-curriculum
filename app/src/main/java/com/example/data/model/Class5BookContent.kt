package com.example.data.model

/** Reviewed 2026 NCTB Class Five book structure with deterministic offline learning support. */
object Class5BookContent {
    private data class Chapter(val title: String, val start: Int, val end: Int, val summary: String)
    private fun bn(n: Int) = n.toString().map { if (it.isDigit()) ('০'.code + it.code - '0'.code).toChar() else it }.joinToString("")
    private fun visuals(book: String, start: Int, end: Int, source: String) = (start..end).map { page ->
        VisualExercise(
            questionBangla = "পৃষ্ঠা ${bn(page)}-এর প্রশ্ন, ছবি, ছক বা কাজটি মন দিয়ে দেখো। কী জানতে চাওয়া হয়েছে?",
            answerBangla = "নির্দেশনাটি সম্পূর্ণ পড়ো, দেওয়া তথ্য/ছবি/ছক চিহ্নিত করো, প্রয়োজনীয় নিয়ম বা মূল ধারণা প্রয়োগ করে ধাপে ধাপে উত্তর দাও এবং বইয়ের উদাহরণের সঙ্গে যাচাই করো।",
            sourceBangla = "পঞ্চম শ্রেণির $source, ২০২৬ শিক্ষাবর্ষ, PDF পৃষ্ঠা ${bn(page)}",
            imageAssetPath = "books/class5/$book/page_${page.toString().padStart(3, '0')}.webp",
            pageQuestions = FullOfflineQuestionBank.forPage(5, book, page)
        )
    }
    private fun chapters(titles: List<String>, starts: List<Int>, last: Int, summary: (String) -> String) =
        titles.mapIndexed { index, title -> Chapter(title, starts[index], starts.getOrNull(index + 1)?.minus(1) ?: last, summary(title)) }

    private fun topic(id: String, chapter: Chapter, book: String, source: String, kind: String): CurriculumTopic {
        val method = when (kind) {
            "math" -> "প্রশ্ন বুঝি → তথ্য সাজাই → নিয়ম/সূত্র প্রয়োগ করি → ধাপে হিসাব করি → উল্টোভাবে যাচাই করি।"
            "science" -> "প্রশ্ন করি → পর্যবেক্ষণ বা নিরাপদ পরীক্ষণ করি → ফল লিখি → প্রমাণ দিয়ে ব্যাখ্যা করি।"
            "english" -> "Look/Listen → understand → speak/read → write → use in a new situation."
            else -> "শিরোনাম ও ছবি দেখি → তথ্য পড়ি → মূল কথা ও কারণ বলি → উদাহরণ দিই → উৎসে যাচাই করি।"
        }
        return CurriculumTopic(
            id = id, titleBangla = chapter.title, summaryBangla = chapter.summary,
            detailsBangla = "${chapter.title} পাঠে ${chapter.summary} বইয়ের আলোচনা, ছবি, ছক এবং অনুশীলনীর মাধ্যমে বিষয়টি ধাপে ধাপে শেখা হয়।",
            learningObjectives = listOf("মূল ধারণা ও প্রয়োজনীয় তথ্য শনাক্ত করা", "বইয়ের তথ্য ব্যবহার করে প্রাসঙ্গিক উত্তর দেওয়া", "কারণ, ধাপ ও উদাহরণ দিয়ে নিজের ভাষায় ব্যাখ্যা করা"),
            learningMethodology = method,
            formulaOrRule = "প্রশ্নে কী চাওয়া হয়েছে বুঝি → প্রাসঙ্গিক তথ্য/নিয়ম বাছি → পূর্ণ উত্তর দিই → যাচাই করি।",
            keyPoints = listOf(chapter.summary, "প্রশ্নের গুরুত্বপূর্ণ শব্দ, সংখ্যা ও একক খেয়াল করো।", "উত্তরের পক্ষে কারণ, ধাপ বা উদাহরণ দাও।"),
            exampleBangla = "বইয়ের একটি কাজ বেছে কী জানতে চেয়েছে, কোন তথ্য লাগবে এবং কীভাবে উত্তর যাচাই করবে তা বলো।",
            qaList = listOf(
                TopicQA("$source বইয়ে ${chapter.title} কী বা কী বিষয়ে?", chapter.summary),
                TopicQA("${chapter.title} সহজ করে বুঝিয়ে দাও।", "${chapter.summary} প্রথমে বইয়ের ছবি বা উদাহরণ দেখো, তারপর মূল ধারণাটি নিজের ভাষায় বলো এবং একটি নতুন উদাহরণ দাও।"),
                TopicQA("এই পাঠের প্রশ্ন কীভাবে সমাধান করব?", "$method উত্তর দেওয়ার পরে কেন উত্তরটি ঠিক, সেটিও বলো।")
            ) + FullOfflineQuestionBank.forPages(5, book, chapter.start, chapter.end),
            parentTeachingTips = listOf("শিশুকে আগে প্রশ্নটি নিজের ভাষায় বলতে এবং নিজে চেষ্টা করতে দিন।", "চূড়ান্ত উত্তর বলে না দিয়ে ছবি, তথ্য বা নিয়ম খুঁজতে ছোট ইঙ্গিত দিন।"),
            commonMistakes = listOf("প্রশ্ন সম্পূর্ণ না পড়ে উত্তর দেওয়া", "ছবি, ছক, একক, ক্রম বা গুরুত্বপূর্ণ তথ্য বাদ দেওয়া"),
            homeActivity = "বাড়ি বা আশপাশ থেকে পাঠটির একটি নিরাপদ বাস্তব উদাহরণ খুঁজে কারণসহ ব্যাখ্যা করো।",
            visualExercises = visuals(book, chapter.start, chapter.end, source)
        )
    }

    private val banglaTitles = listOf("বৈচিত্র্যময় বাংলাদেশ","তিতুমীর","দূরের পাল্লা","পত্র লিখি","ঠিক আছে","সুখু আর দুখু","সাইক্লোন","রয়েল বেঙ্গল টাইগার","টুকটুক ও চিকু","রাখাল ছেলে","কুটির শিল্প","শিষ্যের সাধনা","পাখির মতো","কুপোকাত","সংকল্প","স্মরণীয় যাঁরা বরণীয় যাঁরা","মাটির নিচে পুরানো নগর","ইচ্ছামতী","ভাষার খেলা","শিক্ষাগুরুর মর্যাদা","বিদায় হজের ভাষণ","আমরা তোমাদের ভুলব না","পোস্টার লিখি, প্ল্যাকার্ড লিখি")
    private val banglaStarts = listOf(10,15,28,33,37,41,47,51,56,62,66,72,78,83,93,99,107,111,116,121,128,133,139)
    val banglaTopics = chapters(banglaTitles,banglaStarts,142){"পাঠটি বুঝে পড়া, শব্দ ও ভাষার ব্যবহার শেখা, মূলভাব/ঘটনা বিশ্লেষণ এবং শুদ্ধভাবে বলা ও লেখা।"}.mapIndexed{i,c->topic("nctb2026_c5_bangla_${i+1}",c,"bangla","আমার বাংলা বই","bangla")}

    private val englishTitles = listOf("At the Library","Our School Garden","Be Quiet, Please","My Home District","Student Council","An Email from Indonesia","The Sundarbans","A Field Trip to Remember","Get Ready to Listen","Poem: The Swing","Making Requests","Eating Healthy","Quality Time Together","The Champion Girl","Poem: The Secret Song","The Giving Tree","The Air We Share","Nakshi Kantha","The Wind and the Sun","Writing a Story")
    private val englishStarts = listOf(7,13,18,22,30,36,41,45,53,58,60,67,75,80,85,88,97,103,106,112)
    val englishTopics = chapters(englishTitles,englishStarts,118){"Meaningful listening, speaking, reading and writing are practised through the unit text, conversation and activities."}.mapIndexed{i,c->topic("nctb2026_c5_english_${i+1}",c,"english","English for Today","english")}

    private val mathTitles = listOf("গুণ ও ভাগ","গাণিতিক বাক্য","গুণিতক ও গুণনীয়ক","সাধারণ ভগ্নাংশ","দশমিক ভগ্নাংশ","শতকরা","গড়","পরিমাপ","জ্যামিতি","উপাত্ত বিন্যস্তকরণ")
    private val mathStarts = listOf(8,24,38,56,70,98,112,122,156,178)
    val mathTopics = chapters(mathTitles,mathStarts,190){"বাস্তব সমস্যা, ছবি, ছক এবং ধাপে ধাপে হিসাবের মাধ্যমে $it শেখা ও উত্তর যাচাই করা।"}.mapIndexed{i,c->topic("nctb2026_c5_math_${i+1}",c,"math","প্রাথমিক গণিত","math")}

    private val scienceTitles = listOf("জীবের আবাসস্থল","জীব ও পরিবেশের পারস্পরিক নির্ভরশীলতা","খাদ্য","বয়ঃসন্ধিকাল","পদার্থের গঠন","শক্তির রূপান্তর","বলের ধারণা","ভূমিরূপ","পরিবেশ সংরক্ষণ","পৃথিবীর গতি","জলবায়ু পরিবর্তন","আমাদের জীবনে প্রযুক্তি","সমস্যা সমাধানে তথ্য ও যোগাযোগ প্রযুক্তি")
    private val scienceStarts = listOf(7,17,28,37,46,54,67,78,91,97,108,124,135)
    val scienceTopics = chapters(scienceTitles,scienceStarts,166){"পর্যবেক্ষণ, অনুসন্ধান, নিরাপদ পরীক্ষণ এবং প্রমাণভিত্তিক ব্যাখ্যার মাধ্যমে $it বোঝা।"}.mapIndexed{i,c->topic("nctb2026_c5_science_${i+1}",c,"science","প্রাথমিক বিজ্ঞান","science")}

    private val socialTitles = listOf("জলবায়ু পরিবর্তন","আমরা মানুষ","বাংলাদেশের ক্ষুদ্র নৃগোষ্ঠী","আমাদের স্মরণীয় নেতা","আমাদের মুক্তিযুদ্ধ","বাংলাদেশের ঐতিহাসিক স্থান ও নিদর্শন","দক্ষিণ এশিয়ার সাংস্কৃতিক বৈচিত্র্য","আঞ্চলিক ও আন্তর্জাতিক সংস্থা","রাষ্ট্র এবং সমাজে আমার অধিকার ও কর্তব্য","নৈতিক ও সামাজিক আচরণ","বাংলাদেশের যোগাযোগ মানচিত্র","বাংলাদেশের বনভূমি ও প্রাকৃতিক পর্যটন স্থান","বাংলাদেশের প্রাকৃতিক সম্পদ","বাংলাদেশের জনসংখ্যা ও জনসম্পদ","ব্যক্তিগত বাজেট ও ব্যাংক","জরুরি পরিস্থিতি")
    private val socialStarts = listOf(6,14,24,37,46,66,82,92,99,115,120,131,138,146,153,159)
    val socialTopics = chapters(socialTitles,socialStarts,166){"$it সম্পর্কে বইভিত্তিক তথ্য, কারণ-প্রভাব, নাগরিক দায়িত্ব এবং বাস্তব জীবনের প্রয়োগ।"}.mapIndexed{i,c->topic("nctb2026_c5_social_${i+1}",c,"bgs","বাংলাদেশ ও বিশ্বপরিচয়","social")}

    private val religionTitles = listOf("আকাইদ ও ইবাদত","নবি, রাসুল ও মহানবি (স.)-এর সাহাবিগণের জীবনচরিত","নৈতিক ও মানবিক গুণাবলি অর্জন","ধর্মীয় সম্প্রীতি","জীবজগৎ ও প্রকৃতির প্রতি ভালোবাসা")
    private val religionStarts = listOf(6,41,59,71,77)
    val religionTopics = chapters(religionTitles,religionStarts,86){"$it বিষয়ে ইসলাম শিক্ষার মৌলিক জ্ঞান, উত্তম চরিত্র এবং দৈনন্দিন জীবনে দায়িত্বশীল আচরণ।"}.mapIndexed{i,c->topic("nctb2026_c5_religion_${i+1}",c,"islam","ইসলাম শিক্ষা","religion")}
}
