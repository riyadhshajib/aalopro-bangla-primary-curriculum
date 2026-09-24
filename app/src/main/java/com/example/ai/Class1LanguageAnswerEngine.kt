package com.example.ai

/** High-confidence offline answers for common questions in the Class One language books. */
object Class1LanguageAnswerEngine {
    fun answer(chapter: String, raw: String): String? {
        val q = raw.lowercase().trim()
        return english(chapter, q) ?: bangla(chapter, q)
    }

    private fun english(chapter: String, q: String): String? = when {
        chapter.startsWith("Good Morning") && ("morning" in q || "সকাল" in q) ->
            "In the morning, say: “Good morning.” Example: Good morning, teacher."
        chapter.startsWith("How Are You") && ("how are" in q || "কেমন" in q || "উত্তর" in q) ->
            "Ask: “How are you?” Answer: “I am fine, thank you.” You can then ask, “And you?”"
        chapter == "Goodbye" && ("goodbye" in q || "বিদায়" in q || "bye" in q) ->
            "When leaving, say: “Goodbye.” With a friend you may also say, “Bye.”"
        chapter == "Self-introduction" && ("name" in q || "introdu" in q || "পরিচয়" in q) ->
            "Say: “Hello. My name is ____. I am a student.” Put your own name in the blank."
        chapter == "Where Do You Live?" && ("where" in q || "live" in q || "কোথায়" in q) ->
            "Ask: “Where do you live?” A safe answer is: “I live in Bangladesh.” Do not share a full home address with strangers."
        chapter == "A Family Tree" && ("family" in q || "mother" in q || "father" in q || "পরিবার" in q) ->
            "Use short sentences: “This is my mother. This is my father. This is my family.”"
        chapter.startsWith("Classroom Instructions") && ("stand" in q || "sit" in q || "book" in q || "instruction" in q || "মানে" in q) ->
            "Stand up = দাঁড়াও; Sit down = বসো; Open your book = বই খোলো; Close your book = বই বন্ধ করো; Listen = শোনো; Repeat = আবার বলো।"
        chapter == "Animal Sounds" && ("sound" in q || "cat" in q || "cow" in q || "dog" in q || "শব্দ" in q) ->
            "A cat says “meow”, a cow says “moo”, a dog says “woof”, and a duck says “quack”. Say each sound with the animal picture."
        chapter.contains("Number") || chapter == "Counting Cats" -> numberAnswer(q)
        Regex("^[a-z][A-Z]").containsMatchIn(chapter) || chapter.contains("Alphabet") -> letterAnswer(chapter, q)
        else -> null
    }

    private fun numberAnswer(q: String): String? {
        val names = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten")
        val digit = Regex("\\b(?:10|[0-9])\\b").find(q)?.value?.toIntOrNull()
        if (digit != null && digit in names.indices) return "$digit is written “${names[digit]}” in English. Count $digit objects once each and say ${names[digit]}."
        val named = names.indexOfFirst { it in q }
        return if (named >= 0) "“${names[named]}” means $named. Show it with $named object${if (named == 1) "" else "s"}." else null
    }

    private fun letterAnswer(chapter: String, q: String): String? {
        val examples = mapOf(
            'a' to "apple", 'b' to "ball", 'c' to "cat", 'd' to "dog", 'e' to "egg", 'f' to "fish",
            'g' to "goat", 'h' to "hen", 'i' to "ice cream", 'j' to "jug", 'k' to "kite", 'l' to "lion",
            'm' to "mango", 'n' to "nest", 'o' to "orange", 'p' to "pen", 'q' to "queen", 'r' to "rat",
            's' to "sun", 't' to "tiger", 'u' to "umbrella", 'v' to "van", 'w' to "watch", 'x' to "x-ray",
            'y' to "yo-yo", 'z' to "zebra"
        )
        val letter = Regex("(?:letter|অক্ষর)?\\s*([a-z])\\b").find(q)?.groupValues?.getOrNull(1)?.firstOrNull()
            ?: chapter.lowercase().firstOrNull { it in 'a'..'z' }
            ?: return null
        return "${letter.uppercaseChar()} is the capital letter and $letter is the small letter. Example: ${letter.uppercaseChar()} for ${examples[letter] ?: "a word"}. Look, say, trace, then write."
    }

    private fun bangla(chapter: String, q: String): String? = when {
        chapter == "স্বরবর্ণ" && ("কয়" in q || "কত" in q || "কী কী" in q) ->
            "বাংলা স্বরবর্ণ ১১টি: অ, আ, ই, ঈ, উ, ঊ, ঋ, এ, ঐ, ও, ঔ। একবার দেখে বলো, তারপর না দেখে বলার চেষ্টা করো।"
        chapter == "সাত দিনের কথা" && ("দিন" in q || "সপ্তাহ" in q) ->
            "সাত দিনে এক সপ্তাহ। দিনগুলো হলো: শনিবার, রবিবার, সোমবার, মঙ্গলবার, বুধবার, বৃহস্পতিবার ও শুক্রবার।"
        chapter == "পিঁপড়া ও পায়রার গল্প" && ("বাঁচ" in q || "গল্প" in q || "শিক্ষা" in q) ->
            "পায়রা পানিতে একটি পাতা ফেলে পিঁপড়াকে বাঁচিয়েছিল। পরে পিঁপড়া শিকারির পায়ে কামড় দিয়ে পায়রাকে সতর্ক করে। শিক্ষা: উপকার করলে উপকার ফিরে আসে।"
        chapter == "আমাদের দেশ" && ("দেশ" in q || "নাম" in q || "কেমন" in q) ->
            "আমাদের দেশের নাম বাংলাদেশ। এ দেশে নদী, সবুজ মাঠ, পাহাড়, মাছ ও পাখি আছে। আমাদের দেশ খুব সুন্দর।"
        chapter == "মাছের রাজা" && ("ইলিশ" in q || "জাতীয় মাছ" in q || "কোথায়" in q || "রাজা" in q) ->
            "ইলিশ বাংলাদেশের জাতীয় মাছ। এর রং রুপালি। এটি সাগরে থাকে এবং ডিম দেওয়ার সময় নদীতে আসে। পাঠে ইলিশকে মাছের রাজা বলা হয়েছে।"
        chapter == "আমাদের মুক্তিযুদ্ধ" && ("স্বাধীনতা" in q || "বিজয়" in q || "মুক্তিযুদ্ধ" in q || "১৯৭১" in q) ->
            "১৯৭১ সালে নয় মাসের মুক্তিযুদ্ধের মাধ্যমে বাংলাদেশ স্বাধীন হয়। ২৬ মার্চ স্বাধীনতা দিবস এবং ১৬ ডিসেম্বর বিজয় দিবস। দেশের জন্য যারা যুদ্ধ করেছেন তাঁরা মুক্তিযোদ্ধা।"
        chapter == "শব্দ নিয়ে খেলা" && ("খেলা" in q || "কীভাবে" in q || "কিভাবে" in q) ->
            "একটি শব্দ বলো, তারপর তার শেষ বর্ণ দিয়ে নতুন শব্দ বানাও। যেমন: কলম → ময়ূর → রাত → তাল।"
        chapter == "আমার ঠিকানা" && ("ঠিকানা" in q || "লিখ" in q) ->
            "শিক্ষক বা অভিভাবকের সহায়তায় নিজের গ্রাম/এলাকা, উপজেলা ও জেলার নাম লিখতে পারো। নিরাপত্তার জন্য অপরিচিত ব্যক্তি বা খোলা অনলাইন চ্যাটে পূর্ণ বাসার ঠিকানা দেবে না।"
        else -> null
    }
}
