package com.example.data.model

/** Featured curriculum. Class One Mathematics is the first fully expanded book. */
object MvpFeaturedCurriculum {
    private data class Seed(val title: String, val question: String, val answer: String, val example: String)

    private fun seed(title: String, question: String, answer: String, example: String) =
        Seed(title, question, answer, example)

    private fun attachFullBookQuestions(topics: List<CurriculumTopic>, book: String): List<CurriculumTopic> =
        topics.mapIndexed { index, topic ->
            if (index == topics.lastIndex) topic.copy(qaList = topic.qaList + FullOfflineQuestionBank.all(1, book)) else topic
        }

    private val additions = mapOf(
        "c1_bangla" to listOf(seed("ছবি দেখে শব্দ বলি", "ছবি দেখে শব্দ কীভাবে বলব?", "ছবিতে যা দেখছ, তার নাম স্পষ্ট করে বলো। তারপর শব্দের প্রথম ধ্বনিটি চেনো।", "আমের ছবি দেখলে বলি ‘আম’; প্রথম ধ্বনি আ।")),
        "c1_english" to listOf(
            seed("Colours around us", "What colour is a leaf?", "A fresh leaf is usually green. Look at a real leaf and say: The leaf is green.", "A banana can be yellow; a leaf can be green."),
            seed("Simple words and pictures", "How do I learn a new English word?", "Look at the picture, say the English word aloud, then use it in a short sentence.", "Cat: This is a cat.")),
        "c1_health" to listOf(
            seed("নিরাপদে চলি", "রাস্তা পার হওয়ার আগে কী করব?", "বড়দের সঙ্গে নির্ধারিত পারাপারের জায়গা ব্যবহার করব। থেমে দুই দিক দেখে তবেই পার হব।", "দৌড়ে রাস্তা পার হব না।"),
            seed("পানি ও খাবার", "নিরাপদ পানি কেন পান করব?", "পরিষ্কার ও নিরাপদ পানি পান করলে পানিবাহিত অসুখের ঝুঁকি কমে। খাবারের আগে হাতও ধুতে হবে।", "ঢেকে রাখা নিরাপদ পানি পান করি।")),
        "c1_moral" to listOf(
            seed("মিলে-মিশে খেলি", "বন্ধুর সঙ্গে খেলনা কীভাবে ভাগ করব?", "পালাক্রমে খেলব এবং বন্ধুর কথাও শুনব।", "একজন খেললে অন্যজন তার পালার জন্য অপেক্ষা করে।"),
            seed("ভুল করলে স্বীকার করি", "ভুল হলে কী বলব?", "নিজের ভুল স্বীকার করে ক্ষমা চাইব এবং কীভাবে ঠিক করা যায় তা ভাবব।", "খাতা নষ্ট করলে বলি: দুঃখিত, আমি ঠিক করতে চেষ্টা করব।"),
            seed("প্রাণীর প্রতি যত্ন", "প্রাণীর সঙ্গে কেমন আচরণ করব?", "প্রাণীকে কষ্ট দেব না; নিরাপদ দূরত্ব রেখে সদয় আচরণ করব।", "পাখির বাসা ভাঙব না।")),
        "c2_bangla" to listOf(seed("অনুচ্ছেদ পড়ে বুঝি", "অনুচ্ছেদ বুঝে পড়ার উপায় কী?", "একবার ধীরে পড়ো, অচেনা শব্দ চিহ্নিত করো, তারপর নিজের ভাষায় মূল কথাটি বলো।", "‘নদী’ নিয়ে অনুচ্ছেদে নদীর কী উপকার বলা হয়েছে, তা খুঁজে বলো।")),
        "c2_english" to listOf(
            seed("Animals and birds", "What is the difference between a cat and a bird?", "A cat is an animal; a bird has feathers and usually has wings. Name each from a picture.", "This is a cat. This is a bird."),
            seed("My family", "How can I introduce my family?", "Use simple sentences to name family members without sharing private details.", "This is my mother. I love my family.")),
        "c2_social" to listOf(
            seed("পরিবারে আমার কাজ", "পরিবারে শিশু কীভাবে সাহায্য করতে পারে?", "নিজের জিনিস গুছিয়ে রাখা এবং বয়স অনুযায়ী নিরাপদ ছোট কাজে সাহায্য করা যায়।", "পড়া শেষে বই জায়গায় রাখি।"),
            seed("আমাদের বিদ্যালয়", "বিদ্যালয়ের নিয়ম মানা কেন দরকার?", "নিয়ম মানলে সবাই নিরাপদে ও শান্তিতে শিখতে পারে।", "শ্রেণিকক্ষে কথা বলার আগে হাত তুলি।")),
        "c2_moral" to listOf(
            seed("সত্য কথা বলি", "সত্য কথা বলা কেন ভালো?", "সত্য বললে অন্যরা আমাদের বিশ্বাস করতে পারে; ভুল হলে সেটিও স্বীকার করা যায়।", "ভুলে কিছু ভাঙলে তা লুকাব না।"),
            seed("অন্যকে সাহায্য করি", "বন্ধুকে কীভাবে সাহায্য করব?", "বন্ধুর প্রয়োজন শুনে নিরাপদ ও সম্ভব সাহায্য করব; না পারলে বড়কে জানাব।", "বন্ধুর বই পড়ে গেলে তুলে দিতে পারি।"),
            seed("সবার প্রতি সম্মান", "ভিন্ন মতের বন্ধুর সঙ্গে কী করব?", "তার কথা শুনব এবং অসম্মান না করে নিজের কথা বলব।", "খেলায় মতভেদ হলে পালা করে সিদ্ধান্ত নিই।")),
        "c3_science" to listOf(seed("উদ্ভিদের প্রয়োজন", "গাছ বাঁচতে কী লাগে?", "গাছের বৃদ্ধির জন্য পানি, আলো, বায়ু ও উপযুক্ত মাটি দরকার। বিভিন্ন গাছের প্রয়োজনের পরিমাণ ভিন্ন হতে পারে।", "টবে লাগানো গাছের মাটি শুকালে পরিমিত পানি দিই।")),
        "c3_bangla" to listOf(
            seed("গল্পের মূল কথা", "গল্পের মূল কথা কীভাবে খুঁজব?", "কে কী করল এবং শেষে কী হলো—এই তিনটি প্রশ্নের উত্তর দিয়ে গল্পের মূল কথা বলো।", "গল্পের শুরু, ঘটনা ও শেষ নিজের ভাষায় বলি।"),
            seed("শব্দ দিয়ে বাক্য", "একটি শব্দ দিয়ে বাক্য কীভাবে লিখব?", "শব্দটির অর্থ বুঝে এমন পূর্ণ বাক্য লেখো যাতে ভাব পরিষ্কার হয়।", "‘নদী’ দিয়ে: আমাদের গ্রামের পাশে একটি নদী আছে।")),
        "c3_english" to listOf(
            seed("Daily routine", "How do I describe my day?", "Use short sentences in a sensible order: morning, school, afternoon, night.", "I wake up in the morning. I go to school."),
            seed("Reading a short story", "How do I understand an English story?", "Find who the story is about, what happened, and how it ended. Use the pictures as clues.", "Ask: Who is the main character?")),
        "c3_social" to listOf(
            seed("শিশুর অধিকার ও নিরাপত্তা", "শিশু বিপদে পড়লে কী করবে?", "নিরাপদ জায়গায় গিয়ে বিশ্বস্ত বড় মানুষকে জানাবে। জরুরি হলে স্থানীয় জরুরি সেবার সাহায্য চাইবে।", "অচেনা কাউকে ব্যক্তিগত তথ্য দেব না।"),
            seed("আমাদের সংস্কৃতি", "সংস্কৃতি বলতে কী বোঝায়?", "মানুষের ভাষা, খাবার, পোশাক, গান ও উৎসবের মতো জীবনযাপনের রীতিগুলো সংস্কৃতির অংশ।", "পহেলা বৈশাখ একটি সাংস্কৃতিক উৎসব।")),
        "c3_religion" to listOf(
            seed("সত্যবাদিতা ও আমানত", "আমানত রক্ষা মানে কী?", "কেউ বিশ্বাস করে কিছু রাখলে তা যত্নে রাখা এবং যথাসময়ে ফিরিয়ে দেওয়া আমানত রক্ষার উদাহরণ।", "বন্ধুর বই ধার নিলে ভালো অবস্থায় ফেরত দিই।"),
            seed("পরিষ্কার-পরিচ্ছন্নতা", "পরিচ্ছন্ন থাকার একটি উপায় কী?", "নিয়মিত হাত ধোয়া, পরিষ্কার পোশাক রাখা ও ইবাদতের আগে প্রয়োজনমতো পবিত্রতা বজায় রাখা।", "খাবারের আগে সাবান দিয়ে হাত ধুই।")),
        "c4_bangla" to listOf(
            seed("অনুচ্ছেদের মূলভাব", "মূলভাব কীভাবে লিখব?", "অনুচ্ছেদের সবচেয়ে গুরুত্বপূর্ণ কথাটি নিজের ভাষায় অল্প কথায় লেখো; অপ্রয়োজনীয় বিবরণ বাদ দাও।", "একটি অনুচ্ছেদ পড়ে এক বাক্যে বিষয়টি বলি।"),
            seed("শব্দের অর্থ ও ব্যবহার", "নতুন শব্দ কীভাবে শিখব?", "শব্দটি যে বাক্যে আছে তা পড়ো, অর্থ বুঝে আরেকটি বাক্যে ব্যবহার করো।", "‘সহযোগিতা’ মানে একে অন্যকে সাহায্য করা।")),
        "c4_english" to listOf(
            seed("Asking for help politely", "How do I ask for help politely?", "Use please and a clear request. Thank the person after receiving help.", "Could you help me, please? Thank you."),
            seed("Describing a picture", "How can I describe a picture?", "Say what you see, where it is, and what it is doing in short sentences.", "A bird is sitting in a tree.")),
        "c4_social" to listOf(
            seed("নাগরিক দায়িত্ব", "নিজের এলাকার প্রতি দায়িত্ব কী?", "পরিবেশ পরিষ্কার রাখা, অন্যের অধিকার সম্মান করা এবং নিয়ম মেনে চলা ছোট নাগরিক দায়িত্ব।", "ময়লা নির্দিষ্ট স্থানে ফেলি।"),
            seed("ঐতিহ্য ও উৎসব", "ঐতিহ্য কেন সংরক্ষণ করব?", "ঐতিহ্য আমাদের ইতিহাস ও সংস্কৃতি জানতে সাহায্য করে; তাই তা সম্মান ও যত্ন করা দরকার।", "ঐতিহাসিক স্থানে লিখে বা আঁচড় দিয়ে ক্ষতি করি না।")),
        "c4_religion" to listOf(
            seed("সত্যবাদিতা", "সত্যবাদিতা কী?", "কথা ও কাজে সত্য থাকা এবং ভুল হলে স্বীকার করা সত্যবাদিতার পরিচয়।", "নিজের ভুল অন্যের ওপর চাপাই না।"),
            seed("মানুষের প্রতি সহমর্মিতা", "সহমর্মিতা কীভাবে দেখাব?", "অন্যের কষ্ট বুঝে ভদ্রভাবে কথা বলা ও সামর্থ্য অনুযায়ী সাহায্য করা।", "অসুস্থ বন্ধুর খোঁজ নিই।"),
            seed("প্রকৃতির যত্ন", "প্রকৃতির যত্ন কেন নেব?", "প্রাণী, গাছ ও পরিবেশের যত্ন নেওয়া ভালো আচরণের অংশ।", "অকারণে গাছের ডাল ভাঙি না।")),
        "c5_bangla" to listOf(seed("তথ্য ও মতামত আলাদা করি", "তথ্য আর মতামতের পার্থক্য কী?", "তথ্য যাচাই করা যায়; মতামত কারও ভাবনা বা পছন্দ প্রকাশ করে।", "‘ঢাকা বাংলাদেশের রাজধানী’ তথ্য; ‘ঢাকা সবচেয়ে সুন্দর শহর’ মতামত।")),
        "c5_english" to listOf(seed("Writing a short paragraph", "How do I write a paragraph?", "Start with one main idea, add two or three supporting sentences, and end clearly.", "My garden has flowers. I water the plants. I enjoy visiting it.")),
        "c5_religion" to listOf(seed("পরোপকার ও দায়িত্ব", "পরোপকার বলতে কী বোঝায়?", "নিজের সামর্থ্য অনুযায়ী অন্যের উপকার করা এবং কাউকে কষ্ট না দেওয়া পরোপকারের অংশ।", "সহপাঠী বুঝতে না পারলে ধৈর্য ধরে বোঝাই।"))
    )

    private val classOneMathCatalog = Nctb2026Catalog.mergeInto(PrimaryCurriculumData.levels)
        .single { it.levelNumber == 1 }.subjects.single { it.id == "c1_math" }.topics

    val levels: List<CurriculumClassLevel> = PrimaryCurriculumData.levels.map { level ->
        val officialSubjects = if (level.levelNumber in 1..2) {
            level.subjects.filter { it.id in setOf(
                "c${level.levelNumber}_bangla", "c${level.levelNumber}_english", "c${level.levelNumber}_math"
            ) }
        } else level.subjects
        level.copy(subjects = officialSubjects.map { subject ->
            if (subject.id == "c1_math") {
                val expanded = Class1Math2026Content.topics.mapIndexed { topicIndex, reviewed ->
                    val catalog = classOneMathCatalog.firstOrNull { it.id == reviewed.id }
                    reviewed.copy(visualExercises = Class1MathVisualPages.forTopic(
                        topicIndex, catalog?.visualExercises.orEmpty()
                    ))
                }
                return@map subject.copy(topics = attachFullBookQuestions(expanded, "math"))
            }
            if (subject.id == "c1_bangla") {
                return@map subject.copy(topics = attachFullBookQuestions(Class1Bangla2026Content.topics, "bangla"))
            }
            if (subject.id == "c1_english") {
                return@map subject.copy(topics = attachFullBookQuestions(Class1English2026Content.topics, "english"))
            }
            if (subject.id == "c2_bangla") {
                return@map subject.copy(topics = Class2BookContent.banglaTopics)
            }
            if (subject.id == "c2_english") {
                return@map subject.copy(topics = Class2BookContent.englishTopics)
            }
            if (subject.id == "c2_math") {
                return@map subject.copy(topics = Class2BookContent.mathTopics)
            }
            if (subject.id == "c3_bangla") {
                return@map subject.copy(topics = Class3BookContent.banglaTopics)
            }
            if (subject.id == "c3_english") {
                return@map subject.copy(topics = Class3BookContent.englishTopics)
            }
            if (subject.id == "c3_math") {
                return@map subject.copy(topics = Class3BookContent.mathTopics)
            }
            if (subject.id == "c3_science") {
                return@map subject.copy(topics = Class3BookContent.scienceTopics)
            }
            if (subject.id == "c3_social") {
                return@map subject.copy(topics = Class3BookContent.socialTopics)
            }
            if (subject.id == "c3_religion") {
                return@map subject.copy(topics = Class3BookContent.religionTopics)
            }
            if (subject.id == "c4_bangla") return@map subject.copy(topics = Class4BookContent.banglaTopics)
            if (subject.id == "c4_english") return@map subject.copy(topics = Class4BookContent.englishTopics)
            if (subject.id == "c4_math") return@map subject.copy(topics = Class4BookContent.mathTopics)
            if (subject.id == "c4_science") return@map subject.copy(topics = Class4BookContent.scienceTopics)
            if (subject.id == "c4_social") return@map subject.copy(topics = Class4BookContent.socialTopics)
            if (subject.id == "c4_religion") return@map subject.copy(topics = Class4BookContent.religionTopics)
            if (subject.id == "c5_bangla") return@map subject.copy(topics = Class5BookContent.banglaTopics)
            if (subject.id == "c5_english") return@map subject.copy(topics = Class5BookContent.englishTopics)
            if (subject.id == "c5_math") return@map subject.copy(topics = Class5BookContent.mathTopics)
            if (subject.id == "c5_science") return@map subject.copy(topics = Class5BookContent.scienceTopics)
            if (subject.id == "c5_social") return@map subject.copy(topics = Class5BookContent.socialTopics)
            if (subject.id == "c5_religion") return@map subject.copy(topics = Class5BookContent.religionTopics)
            val existing = subject.topics.take(3).map { topic ->
                val qa = topic.qaList.ifEmpty {
                    val sample = topic.exampleBangla.orEmpty()
                    val question = Regex("(?:প্রশ্ন|Question):\\s*([^\\n]+)", RegexOption.IGNORE_CASE)
                        .find(sample)?.groupValues?.getOrNull(1)
                    val answer = Regex("(?:উত্তর|Answer):\\s*([^\\n]+)", RegexOption.IGNORE_CASE)
                        .find(sample)?.groupValues?.getOrNull(1)
                    listOf(if (!question.isNullOrBlank() && !answer.isNullOrBlank())
                        TopicQA(question, answer) else
                        TopicQA("${topic.titleBangla} বিষয়টি সহজ করে বোঝাও।", topic.detailsBangla))
                }
                topic.copy(qaList = qa)
            }
            val extra = additions[subject.id].orEmpty().take((3 - existing.size).coerceAtLeast(0))
                .mapIndexed { index, entry ->
                    CurriculumTopic(
                        id = "mvp_${subject.id}_${index + 1}",
                        titleBangla = entry.title,
                        summaryBangla = entry.answer,
                        detailsBangla = entry.answer + "\n\nউদাহরণ: " + entry.example,
                        exampleBangla = entry.example,
                        qaList = listOf(TopicQA(entry.question, entry.answer + " উদাহরণ: " + entry.example))
                    )
                }
            subject.copy(topics = existing + extra)
        })
    }
}
