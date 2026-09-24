package com.example.data.model

/** Reviewed unit/lesson support for NCTB 2026 English for Today, Class One. */
object Class1English2026Content {
    private data class Lesson(val unit: String, val title: String, val focus: String, val model: String)

    private val lessons = listOf(
        Lesson("Unit 1 · Greetings and Farewells", "Good Morning (1)", "greeting someone in the morning", "Good morning. How are you?"),
        Lesson("Unit 1 · Greetings and Farewells", "Good Morning (2)", "using ‘Good morning’ with teachers and friends", "Good morning, teacher."),
        Lesson("Unit 1 · Greetings and Farewells", "How Are You? (1)", "asking and answering about wellbeing", "How are you? I am fine, thank you."),
        Lesson("Unit 1 · Greetings and Farewells", "How Are You? (2)", "continuing a short greeting dialogue", "I am fine too. Thanks."),
        Lesson("Unit 1 · Greetings and Farewells", "Goodbye", "saying goodbye politely", "Goodbye, teacher. Goodbye, Runa."),
        Lesson("Unit 1 · Greetings and Farewells", "Two Little Blackbirds", "reciting a rhyme with actions", "Two little blackbirds sitting on a wall."),
        Lesson("Unit 2 · Alphabet and Numbers", "The Alphabet Song", "saying the English alphabet in order", "a b c d e f g … x y z"),
        Lesson("Unit 2 · Alphabet and Numbers", "aA-bB", "recognising, tracing and sounding A/a and B/b", "A for apple. B for ball."),
        Lesson("Unit 2 · Alphabet and Numbers", "cC-dD", "recognising, tracing and sounding C/c and D/d", "C for cat. D for dog."),
        Lesson("Unit 2 · Alphabet and Numbers", "Numbers 1-2", "counting and saying one and two", "one (1), two (2)"),
        Lesson("Unit 2 · Alphabet and Numbers", "Two Little Hands", "reciting a body-parts rhyme with actions", "Two little hands go clap, clap, clap."),
        Lesson("Unit 2 · Alphabet and Numbers", "eE-fF", "recognising, tracing and sounding E/e and F/f", "E for egg. F for fish."),
        Lesson("Unit 2 · Alphabet and Numbers", "Numbers 3-4", "counting and saying three and four", "three (3), four (4)"),
        Lesson("Unit 2 · Alphabet and Numbers", "Counting Cats", "counting objects in English", "One cat, two cats, three cats."),
        Lesson("Unit 2 · Alphabet and Numbers", "gG-hH", "recognising, tracing and sounding G/g and H/h", "G for goat. H for hen."),
        Lesson("Unit 2 · Alphabet and Numbers", "Numbers 5-6", "counting and saying five and six", "five (5), six (6)"),
        Lesson("Unit 2 · Alphabet and Numbers", "iI-jJ", "recognising, tracing and sounding I/i and J/j", "I for ice cream. J for jug."),
        Lesson("Unit 2 · Alphabet and Numbers", "Numbers 7-8", "counting and saying seven and eight", "seven (7), eight (8)"),
        Lesson("Unit 2 · Alphabet and Numbers", "kK-lL", "recognising, tracing and sounding K/k and L/l", "K for kite. L for lion."),
        Lesson("Unit 2 · Alphabet and Numbers", "mM-nN", "recognising, tracing and sounding M/m and N/n", "M for mango. N for nest."),
        Lesson("Unit 2 · Alphabet and Numbers", "Numbers 9-10", "counting and saying nine and ten", "nine (9), ten (10)"),
        Lesson("Unit 2 · Alphabet and Numbers", "oO-pP", "recognising, tracing and sounding O/o and P/p", "O for orange. P for pen."),
        Lesson("Unit 2 · Alphabet and Numbers", "qQ-rR", "recognising, tracing and sounding Q/q and R/r", "Q for queen. R for rat."),
        Lesson("Unit 2 · Alphabet and Numbers", "sS-tT", "recognising, tracing and sounding S/s and T/t", "S for sun. T for tiger."),
        Lesson("Unit 2 · Alphabet and Numbers", "uU-vV-wW", "recognising and sounding U/u, V/v and W/w", "U for umbrella. V for van. W for watch."),
        Lesson("Unit 2 · Alphabet and Numbers", "xX-yY-zZ", "recognising and sounding X/x, Y/y and Z/z", "X for x-ray. Y for yo-yo. Z for zebra."),
        Lesson("Unit 2 · Alphabet and Numbers", "Alphabet Review", "reviewing A-Z, letter sounds and numbers 1-10", "Say, match, trace and write the letters."),
        Lesson("Unit 3 · Classroom Instructions", "Classroom Instructions 1", "understanding simple classroom commands", "Stand up. Sit down. Open your book."),
        Lesson("Unit 3 · Classroom Instructions", "Classroom Instructions 2", "listening and responding to classroom directions", "Look at the board. Listen. Repeat."),
        Lesson("Unit 3 · Classroom Instructions", "Classroom Instructions 3", "following two-step classroom directions", "Take your pencil and write your name."),
        Lesson("Unit 4 · Questions and Answers", "Self-introduction", "saying one’s name in a short introduction", "Hello. My name is Rafi. I am a student."),
        Lesson("Unit 4 · Questions and Answers", "In the Village", "naming familiar things in a village", "I see a tree, a pond and a cow."),
        Lesson("Unit 4 · Questions and Answers", "In the City", "naming familiar things in a city", "I see a bus, a road and a building."),
        Lesson("Unit 4 · Questions and Answers", "At Village Home", "asking and answering about a village home", "What is this? This is a house."),
        Lesson("Unit 4 · Questions and Answers", "In the Classroom", "asking and answering about classroom objects", "What is this? This is a book."),
        Lesson("Unit 4 · Questions and Answers", "Review Lesson", "reviewing greetings, letters, numbers and questions", "Good morning. My name is Tuly. This is a pen."),
        Lesson("Unit 5 · Rhymes and Sounds", "Animal Sounds", "matching common animals with their sounds", "A cat says meow. A cow says moo."),
        Lesson("Unit 5 · Rhymes and Sounds", "Where Do You Live?", "asking and answering where someone lives", "Where do you live? I live in Bangladesh."),
        Lesson("Unit 5 · Rhymes and Sounds", "A Family Tree", "naming close family members", "This is my mother. This is my father.")
    )

    val topics: List<CurriculumTopic> = lessons.mapIndexed { index, lesson ->
        val phonics = Regex("^[a-z][A-Z]", RegexOption.IGNORE_CASE).containsMatchIn(lesson.title) || lesson.title.contains("Alphabet")
        val numberLesson = lesson.title.contains("Number") || lesson.title.contains("Counting")
        CurriculumTopic(
            id = "nctb2026_c1_english_${index + 1}",
            titleBangla = lesson.title,
            summaryBangla = "${lesson.unit}: ${lesson.focus}.",
            detailsBangla = if (phonics)
                "Look at each capital and small letter, listen to its sound, trace it from the correct starting point, and say a familiar word. বাংলা সহায়তা: বড় ও ছোট অক্ষর দেখে ধ্বনি বলো, তারপর লিখো।"
            else if (numberLesson)
                "Count each object once, say the number in English, match the numeral and word, and trace or write it. বাংলা সহায়তা: একটি করে গুনে ইংরেজিতে সংখ্যাটি বলো।"
            else "Listen first, repeat with correct meaning, practise with a partner, and use the expression in a real situation. বাংলা সহায়তা: আগে শোনো, তারপর জোড়ায় জোড়ায় বলো।",
            learningObjectives = listOf("Learners can ${lesson.focus}.", "Learners can listen and respond with a short meaningful expression.", "Learners can use the model with teacher support."),
            learningMethodology = "Look/listen → repeat together → practise in pairs → respond alone → use in a small game.",
            formulaOrRule = if (phonics) "See the letter → say the sound → say the word → trace and write." else "Listen to the whole expression before repeating; speak in a complete short sentence.",
            keyPoints = listOf(lesson.focus.replaceFirstChar { it.uppercase() }, lesson.model),
            exampleBangla = lesson.model,
            qaList = listOf(
                TopicQA("What do we learn in ${lesson.title}?", "We learn to ${lesson.focus}. Model: ${lesson.model}"),
                TopicQA("${lesson.title} সহজ করে বুঝিয়ে দাও।", "First listen and look at the picture. Say the model slowly: “${lesson.model}” Then practise it with a partner. বাংলা বা Banglish-এ আবার বুঝতে চাইলে লিখতে পারো।"),
                TopicQA("Give me a practice question from ${lesson.title}.", if (phonics) "Find the lesson letters, say their sounds, name one word for each, and write the capital and small forms." else if (numberLesson) "Count a small group of objects, say the number in English, and match it with the numeral." else "Use this model in a short pair dialogue: ${lesson.model}")
            ),
            parentTeachingTips = listOf("Say the expression naturally and let the child listen before repeating.", "Use pictures, gestures and familiar objects; do not translate every word before the child tries."),
            commonMistakes = if (phonics) listOf("Saying only the letter name but not noticing its sound", "Mixing capital and small letter shapes") else listOf("Repeating without understanding the situation", "Answering with a single memorised word instead of the short model sentence"),
            homeActivity = if (phonics) "Find two lesson letters on packets or books at home and say one word for each." else "Practise the model expression with a family member twice, changing the name or object.",
            catalogOnly = false
        )
    }
}
