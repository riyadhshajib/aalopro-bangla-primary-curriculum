package com.example.data.model

/** Reviewed 2026 NCTB Class Three book structure and offline learning content. */
object Class3BookContent {
    private data class Chapter(
        val title: String,
        val start: Int,
        val end: Int,
        val summary: String,
        val rule: String = "মূল ধারণা বুঝে বইয়ের উদাহরণ দেখি, নিজের ভাষায় বলি এবং অনুশীলন করি।",
        val example: String = "বইয়ের একটি ছবি বা উদাহরণ দেখে কী শিখলে তা দুইটি বাক্যে বলো।"
    )

    private fun bn(value: Int): String = value.toString().map {
        if (it in '0'..'9') ('০'.code + it.code - '0'.code).toChar() else it
    }.joinToString("")

    private fun visuals(book: String, start: Int, end: Int, source: String): List<VisualExercise> =
        (start..end).map { page ->
            VisualExercise(
                questionBangla = "পৃষ্ঠা ${bn(page)} মন দিয়ে দেখো। ছবি, লেখা, ছক বা সংখ্যার কাজটি কী জানতে চাইছে?",
                answerBangla = "প্রথমে নির্দেশনাটি পড়ো, তারপর ছবি বা উদাহরণ পর্যবেক্ষণ করো। ধাপে ধাপে কাজটি নিজে করো এবং বইয়ের তথ্যের সঙ্গে উত্তর মিলিয়ে যাচাই করো।",
                sourceBangla = "তৃতীয় শ্রেণির $source, ২০২৬ শিক্ষাবর্ষ, PDF পৃষ্ঠা ${bn(page)}",
                imageAssetPath = "books/class3/$book/page_${page.toString().padStart(3, '0')}.webp",
                pageQuestions = FullOfflineQuestionBank.forPage(3, book, page)
            )
        }

    private fun topic(id: String, chapter: Chapter, book: String, source: String, kind: String): CurriculumTopic {
        val method = when (kind) {
            "math" -> "প্রশ্ন বুঝি → ছবি বা উপকরণ দিয়ে করি → নিয়মে হিসাব করি → উল্টোভাবে যাচাই করি।"
            "english" -> "Look → Listen → Say → Read → Write → Use in a new sentence."
            "science" -> "প্রশ্ন করি → পর্যবেক্ষণ/পরীক্ষা করি → ফল লিখি → প্রমাণ দিয়ে ব্যাখ্যা করি।"
            else -> "ছবি ও শিরোনাম দেখি → মন দিয়ে পড়ি → মূল কথা বলি → উদাহরণ দিই → যাচাই করি।"
        }
        return CurriculumTopic(
            id = id,
            titleBangla = chapter.title,
            summaryBangla = chapter.summary,
            detailsBangla = "${chapter.title} পাঠে ${chapter.summary} বইয়ের ছবি, আলোচনা ও কাজের মাধ্যমে ধারণাটি ধাপে ধাপে শেখা হয়।",
            learningObjectives = listOf("মূল ধারণা শনাক্ত করা", "বইয়ের তথ্য ব্যবহার করে উত্তর দেওয়া", "নিজের ভাষায় কারণ ও উদাহরণ ব্যাখ্যা করা"),
            learningMethodology = method,
            formulaOrRule = chapter.rule,
            keyPoints = listOf(chapter.summary, chapter.rule, "উত্তরের আগে প্রশ্নে কী চাওয়া হয়েছে তা শনাক্ত করো।"),
            exampleBangla = chapter.example,
            qaList = listOf(
                TopicQA("${chapter.title} কী বা কী বিষয়ে?", chapter.summary),
                TopicQA("${chapter.title} সহজ করে বুঝিয়ে দাও।", "${chapter.summary} সহজভাবে: ${chapter.rule} উদাহরণ: ${chapter.example}"),
                TopicQA("এই পাঠটি কীভাবে অনুশীলন করব?", "$method বইয়ের একটি কাজ শেষ করে কেন উত্তরটি ঠিক, সেটিও বলো।")
            ) + FullOfflineQuestionBank.forPages(3, book, chapter.start, chapter.end),
            parentTeachingTips = listOf("শিশুকে আগে ছবি দেখে নিজের ভাষায় বলতে দিন; পরে বইয়ের শব্দের সঙ্গে মিলিয়ে দিন।", "একবারে একটি ছোট প্রশ্ন করুন এবং শুধু উত্তর নয়, কীভাবে বুঝেছে তা বলতে উৎসাহ দিন।"),
            commonMistakes = listOf("প্রশ্ন সম্পূর্ণ না পড়ে উত্তর দেওয়া", "ছবি, একক, ক্রম বা গুরুত্বপূর্ণ শব্দ উপেক্ষা করা"),
            homeActivity = "বাড়ি বা আশপাশ থেকে এই পাঠের সঙ্গে সম্পর্কিত একটি নিরাপদ উদাহরণ খুঁজে নিজের ভাষায় বোঝাও।",
            visualExercises = visuals(book, chapter.start, chapter.end, source)
        )
    }

    private val banglaStarts = listOf(10, 13, 17, 18, 21, 24, 27, 31, 35, 40, 43, 47, 53, 56, 60, 63, 67, 70, 74, 78, 81, 84, 87, 90, 93, 95, 97, 101, 104, 107)
    private val banglaTitles = listOf(
        "আমাদের কথা", "আমাদের পরিবার ও আমাদের প্রতিবেশী", "ময়লার বাকসো", "আবার পড়ি কারচিহ্ন", "আবার পড়ি ফলাচিহ্ন",
        "দেখে বুঝে কাজ করি", "ঘাসফড়িং আর পিঁপড়ার গল্প", "আমি হব", "ব্যাঙের সাজা", "বাক্য পড়ি ও লিখি",
        "আনন্দের দিন", "বালুচরে একদিন", "আমাদের গ্রাম", "নদীর দেশ", "হার-জিতের গল্প", "হাসি", "আমাদের উৎসব",
        "রাষ্ট্রভাষা বাংলা চাই", "আজিকার শিশু", "ঢাকাই মসলিন", "হজরত আবু বকর (রা.)", "আমার পণ", "মানব জয়ের গল্প",
        "তালগাছ", "রবীন্দ্রনাথ ঠাকুরের ছেলেবেলা", "আদর্শ ছেলে", "মুক্তিযুদ্ধে রাজারবাগ", "নিজের মতো লিখি",
        "প্রতিযোগিতায় নাম লিখি", "শব্দভাণ্ডার"
    )
    val banglaTopics = banglaTitles.mapIndexed { index, title ->
        val start = banglaStarts[index]
        val end = (banglaStarts.getOrNull(index + 1)?.minus(1) ?: 110).coerceAtLeast(start)
        topic("nctb2026_c3_bangla_${index + 1}", Chapter(title, start, end,
            "পাঠটি বুঝে পড়া, নতুন শব্দ শেখা, মূলভাব বলা এবং শুদ্ধ বাক্যে নিজের ভাব প্রকাশ করা।",
            "শিরোনাম → চরিত্র/বিষয় → মূল ঘটনা বা ভাব → নিজের ভাষায় উত্তর।",
            "পাঠটি পড়ে কে, কী, কোথায়, কেন—এই প্রশ্নগুলোর পূর্ণ বাক্যে উত্তর দাও।"), "bangla", "আমার বাংলা বই", "bangla")
    }

    private val english = listOf(
        Chapter("Unit 1 · Greetings, Farewells, Introductions and Numbers",7,26,"Greetings, introductions, farewells and numbers 1–30 are used in short conversations."),
        Chapter("Unit 2 · My Friends, Family and Numbers",27,39,"Friends and family are introduced with simple sentences, together with numbers 31–70."),
        Chapter("Unit 3 · Commands, Instructions, Requests and Numbers",40,53,"Simple commands, instructions and polite requests are understood and used."),
        Chapter("Unit 4 · Let’s Play with Sounds and Numbers",54,70,"Middle vowel sounds, rhymes, numbers 91–100 and ordinal numbers are practised."),
        Chapter("Unit 5 · Their Days",71,89,"School, village and city routines are described in a meaningful sequence."),
        Chapter("Unit 6 · Cleanliness",90,103,"Good habits and correct handwashing are explained and practised."),
        Chapter("Unit 7 · Save Our Water",104,115,"Uses of water, water pollution and ways to save water are discussed."),
        Chapter("Unit 8 · Facts and Fables",116,130,"Stories and factual texts are read to find characters, events and the main idea.")
    )
    val englishTopics = english.mapIndexed { index, chapter -> topic("nctb2026_c3_english_${index + 1}", chapter, "english", "English for Today", "english") }

    private val math = listOf(
        Chapter("সংখ্যা",7,26,"চার অঙ্কের সংখ্যা গঠন, পড়া, লেখা, স্থানীয় মান, তুলনা ও ক্রম।","হাজার × ১০০০ + শতক × ১০০ + দশক × ১০ + একক।","২৩৫৪ = ২ হাজার + ৩ শত + ৫ দশক + ৪ একক।"),
        Chapter("যোগ",27,36,"স্থানীয় মান ঠিক রেখে বড় সংখ্যার যোগ ও বাস্তব সমস্যার সমাধান।","একক, দশক, শতক ও হাজার একই কলামে লিখে ডান দিক থেকে যোগ করি।","১২৪৫ + ২৩১২ = ৩৫৫৭।"),
        Chapter("বিয়োগ",37,42,"বড় সংখ্যার বিয়োগ, ধার নেওয়া এবং বিয়োগ দিয়ে সমস্যা সমাধান।","একই স্থানীয় মানের অঙ্ক একই কলামে রেখে ডান দিক থেকে বিয়োগ করি।","৩৫৫৭ − ২৩১২ = ১২৪৫।"),
        Chapter("যোগ ও বিয়োগের সম্পর্ক",43,48,"যোগ ও বিয়োগকে বিপরীত প্রক্রিয়া হিসেবে ব্যবহার করে উত্তর যাচাই।","যোগফল − একটি যোগ্য = অন্য যোগ্য।","২৫ + ১৭ = ৪২, তাই ৪২ − ১৭ = ২৫।"),
        Chapter("গুণ",48,68,"সমান দল, নামতা ও বহু অঙ্কের গুণ।","গুণ হলো একই সংখ্যা বারবার যোগ করার সংক্ষিপ্ত রূপ।","৪ × ৬ = ৬ + ৬ + ৬ + ৬ = ২৪।"),
        Chapter("ভাগ",69,83,"সমানভাবে বণ্টন, ভাগফল ও ভাগশেষ।","ভাজ্য = ভাজক × ভাগফল + ভাগশেষ।","১৭ ÷ ৫ = ৩, ভাগশেষ ২।"),
        Chapter("গুণ ও ভাগের সম্পর্ক",84,89,"গুণ ও ভাগের বিপরীত সম্পর্ক দিয়ে অজানা সংখ্যা ও উত্তর যাচাই।","ভাজক × ভাগফল = ভাজ্য, যখন ভাগশেষ শূন্য।","৬ × ৪ = ২৪, তাই ২৪ ÷ ৬ = ৪।"),
        Chapter("চার প্রক্রিয়ার সমস্যা",90,96,"যোগ, বিয়োগ, গুণ ও ভাগের উপযুক্ত প্রক্রিয়া বেছে কথার সমস্যা সমাধান।","প্রশ্ন পড়ি → তথ্য লিখি → প্রক্রিয়া বাছি → হিসাব → এককসহ উত্তর।","৪ ঝুড়িতে ৬টি করে আম হলে মোট ২৪টি আম।"),
        Chapter("ভগ্নাংশ",97,112,"সমান অংশ, লব-হর, একক ভগ্নাংশ এবং সহজ তুলনা।","হর মোট সমান অংশ; লব নেওয়া অংশ।","৮ সমান টুকরার ৩টি = ৩/৮।"),
        Chapter("বাংলাদেশি মুদ্রা",113,123,"টাকা-পয়সা চেনা, সমমান, মোট দাম ও ফেরত হিসাব।","১০০ পয়সা = ১ টাকা; দেওয়া টাকা − মোট দাম = ফেরত।","৫০ টাকা দিয়ে ৩৮ টাকার জিনিস কিনলে ফেরত ১২ টাকা।"),
        Chapter("পরিমাপ",124,138,"দৈর্ঘ্য, ভর, ধারণক্ষমতা ও সময়ের প্রমিত একক।","১০০ সেমি = ১ মিটার; ১০০০ গ্রাম = ১ কেজি; ৬০ মিনিট = ১ ঘণ্টা।","২ মিটার = ২০০ সেন্টিমিটার।"),
        Chapter("জ্যামিতি",139,154,"বিন্দু, রেখা, রেখাংশ, কোণ ও পরিচিত সমতল আকৃতি।","আকৃতি চেনার সময় বাহু, কোণ ও প্রান্ত গুনি।","ত্রিভুজের ৩টি বাহু ও ৩টি কোণ।"),
        Chapter("উপাত্ত সংগ্রহ ও বিন্যস্তকরণ",155,162,"তথ্য সংগ্রহ, ট্যালি, তালিকা ও চিত্রলেখ থেকে উত্তর।","প্রতিটি চিহ্ন কতটি বস্তু বোঝায় তা দেখে মোট গুনি।","একটি ★ যদি ২টি ফল বোঝায়, ৪টি ★ = ৮টি ফল।")
    )
    val mathTopics = math.mapIndexed { index, chapter -> topic("nctb2026_c3_math_${index + 1}", chapter, "math", "প্রাথমিক গণিত", "math") }

    private val science = listOf(
        Chapter("উদ্ভিদ পরিচিতি",8,17,"উদ্ভিদের প্রধান অংশ, অংশের কাজ এবং উদ্ভিদের প্রয়োজন।"),
        Chapter("প্রাণী পরিচিতি",18,29,"প্রাণীর বৈশিষ্ট্য, আবাসস্থল, খাদ্য ও চলাচলের বৈচিত্র্য।"),
        Chapter("সুস্বাস্থ্যের জন্য খাদ্য",30,45,"সুষম খাদ্য, খাদ্যের উৎস, নিরাপদ খাবার ও সুস্থ অভ্যাস।"),
        Chapter("পদার্থ",46,65,"পদার্থের কঠিন, তরল ও গ্যাসীয় অবস্থা এবং পর্যবেক্ষণযোগ্য ধর্ম।"),
        Chapter("শক্তি",66,75,"আলো, তাপ, শব্দসহ শক্তির পরিচিত উৎস ও ব্যবহার।"),
        Chapter("বস্তুর উপর বলের প্রভাব",76,86,"ধাক্কা ও টানে বস্তুর গতি, দিক বা আকৃতি পরিবর্তন।"),
        Chapter("পানি",87,101,"পানির উৎস, ব্যবহার, তিন অবস্থা, দূষণ ও সংরক্ষণ।"),
        Chapter("মাটি",102,117,"মাটির উপাদান, বিভিন্ন ধরন, ব্যবহার ও সংরক্ষণ।"),
        Chapter("জীবনের জন্য সূর্য",118,128,"সূর্যের আলো ও তাপ জীবজগতের জন্য কেন অপরিহার্য।"),
        Chapter("প্রযুক্তির সঙ্গে পরিচয়",129,139,"প্রয়োজন মেটাতে ব্যবহৃত সহজ প্রযুক্তি ও নিরাপদ ব্যবহার।"),
        Chapter("তথ্য ও যোগাযোগ",140,154,"তথ্য সংগ্রহ, সংরক্ষণ ও যোগাযোগের পরিচিত মাধ্যম।")
    )
    val scienceTopics = science.mapIndexed { index, chapter -> topic("nctb2026_c3_science_${index + 1}", chapter, "science", "প্রাথমিক বিজ্ঞান", "science") }

    private val bgsTitles = listOf("আমাদের পরিবেশ", "আমরা সবাই মানুষ", "আমাদের ইতিহাস", "আমাদের সংস্কৃতি", "মহাদেশ ও মহাসাগর", "পরিবার ও বিদ্যালয়ে শিশুর ভূমিকা", "শিশু অধিকার ও নিরাপত্তা", "নৈতিক ও মানবিক গুণ", "আমাদের দেশ", "বিভিন্ন পেশা", "টাকার ব্যবহার", "জরুরি পরিস্থিতি মোকাবিলা")
    // Boundaries are verified against the chapter-opening pages in the supplied PDF.
    private val bgsStarts = listOf(6, 17, 26, 40, 47, 58, 67, 77, 83, 95, 105, 110)
    val socialTopics = bgsTitles.mapIndexed { index, title ->
        val start = bgsStarts[index]
        val end = bgsStarts.getOrNull(index + 1)?.minus(1) ?: 118
        topic("nctb2026_c3_social_${index + 1}", Chapter(title, start, end,
            "$title সম্পর্কে বইভিত্তিক পরিচয়, দায়িত্বশীল আচরণ এবং বাস্তব জীবনের প্রয়োগ।",
            "তথ্য দেখি → ব্যক্তি/স্থান/ঘটনা চিহ্নিত করি → দায়িত্ব বা কারণ বলি → বাস্তব উদাহরণ দিই।"), "bgs", "বাংলাদেশ ও বিশ্বপরিচয়", "social")
    }

    private val islam = listOf(
        Chapter("স্রষ্টা ও সৃষ্টি",7,35,"মহান আল্লাহর অস্তিত্ব, একত্ববাদ, সৃষ্টি, ইবাদত ও মৌলিক ধর্মীয় জ্ঞান।"),
        Chapter("রাসুল (সা.) ও তাঁর সাহাবিগণের জীবনচরিত অনুসরণ",36,45,"রাসুল (সা.) ও সাহাবিগণের জীবন থেকে সত্য, ধৈর্য ও উত্তম আচরণ শেখা।"),
        Chapter("নৈতিক ও মানবিক গুণাবলি অর্জন",46,55,"সত্যবাদিতা, আমানতদারি, পরিচ্ছন্নতা, সম্মান ও সহমর্মিতার চর্চা।"),
        Chapter("ধর্মীয় সম্প্রীতি",56,65,"নিজ ধর্ম পালন এবং অন্যের ধর্মীয় বিশ্বাস ও অধিকারের প্রতি সম্মান।"),
        Chapter("জীবজগৎ ও প্রকৃতির প্রতি ভালোবাসা",66,74,"মানুষ, প্রাণী, উদ্ভিদ ও পরিবেশের প্রতি দায়িত্বশীল ও দয়ালু আচরণ।")
    )
    val religionTopics = islam.mapIndexed { index, chapter -> topic("nctb2026_c3_religion_${index + 1}", chapter, "islam", "ইসলাম ও নৈতিক শিক্ষা", "religion") }
}
