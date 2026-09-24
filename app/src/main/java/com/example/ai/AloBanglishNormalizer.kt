package com.example.ai

/**
 * AloBanglishNormalizer
 * Comprehensive offline engine for detecting, parsing, and converting Banglish
 * (Bengali written with Latin / English characters) into standardized Bangla academic queries.
 */
object AloBanglishNormalizer {

    // Common Banglish phrase / concept mappings to exact Bangla academic terms
    private val phraseMap = mapOf(
        // Science & Biology
        "salok songleshon" to "সালোকসংশ্লেষণ",
        "saloksongleshon" to "সালোকসংশ্লেষণ",
        "photosynthesis" to "সালোকসংশ্লেষণ",
        "kosh" to "কোষ",
        "kosh ki" to "কোষ কাকে বলে",
        "dna" to "ডিএনএ ও জেনেটিক্স",
        "rna" to "আরএনএ",
        "mitosis" to "মাইটোসিস কোষ বিভাজন",
        "meiosis" to "মিয়োসিস কোষ বিভাজন",
        "rokto" to "রক্ত ও রক্তকণিকা",
        "hritpindo" to "হৃদপিণ্ড ও রক্ত সঞ্চালন",
        "pani chokro" to "পানি চক্র",
        "water cycle" to "পানি চক্র",
        "khaddo sringkhol" to "খাদ্য শৃঙ্খল",
        "food chain" to "খাদ্য শৃঙ্খল",
        "poribesh dushon" to "পরিবেশ দূষণ",
        "pollution" to "পরিবেশ দূষণ",

        // Physics & Chemistry
        "laal alo" to "লাল আলো",
        "lal alo" to "লাল আলো",
        "red light" to "লাল আলো",
        "neel alo" to "নীল আলো",
        "nil alo" to "নীল আলো",
        "alor bikkhepon" to "আলোর বিক্ষেপণ",
        "alor protifolon" to "আলোর প্রতিফলন",
        "alor protishoron" to "আলোর প্রতিসরণ",
        "alo ki" to "আলোর প্রকৃতি ও বৈশিষ্ট্য",
        "newton er goti sutro" to "নিউটনের গতিসূত্র",
        "newton er gotisutra" to "নিউটনের গতিসূত্র",
        "newtoner gotisutro" to "নিউটনের গতিসূত্র",
        "newton 3rd law" to "নিউটনের তৃতীয় সূত্র",
        "gotisutro" to "গতিসূত্র",
        "goti sutro" to "গতিসূত্র",
        "ghorshon" to "ঘর্ষণ বল",
        "ghorshon ki" to "ঘর্ষণ বল কাকে বলে",
        "mahakorsho" to "মহাকর্ষ সূত্র",
        "ovikorsho" to "অভিকর্ষ ও অভিকর্ষজ ত্বরণ",
        "gravity" to "মহাকর্ষ ও অভিকর্ষ",
        "alor protifolon" to "আলোর প্রতিফলন",
        "alor protishoron" to "আলোর প্রতিসরণ",
        "poromanu" to "পরমাণুর গঠন",
        "electron" to "ইলেকট্রন প্রোটন ও নিউট্রন",
        "proton" to "প্রোটন",
        "neutron" to "নিউট্রন",
        "porjay sharoni" to "পর্যায় সারণি",
        "periodic table" to "পর্যায় সারণি",
        "acid khar" to "এসিড ও ক্ষারক",
        "acid" to "এসিড ও ক্ষারকের বৈশিষ্ট্য",

        // Math
        "jog" to "যোগের নিয়ম",
        "jog korar niyom" to "যোগ করার সহজ নিয়ম",
        "biyog" to "বিয়োগের নিয়ম",
        "biyog korar niyom" to "বিয়োগ করার সহজ নিয়ম",
        "gun" to "গুণ করার সহজ নিয়ম",
        "gun korar niyom" to "গুণ করার সহজ নিয়ম",
        "bhag" to "ভাগ করার নিয়ম",
        "namta" to "গুণের নামতা",
        "losagu" to "লসাগু নির্ণয়",
        "gosagu" to "গসাগু নির্ণয়",
        "losagu gosagu" to "লসাগু ও গসাগু নির্ণয়",
        "pithagoras" to "পিথাগোরাসের উপপাদ্য",
        "pythagoras" to "পিথাগোরাসের উপপাদ্য",
        "trikonmiti" to "ত্রিকোণমিতি সূত্র",
        "porimiti" to "পরিমিতি ও ক্ষেত্রফল",
        "vognangsho" to "ভগ্নাংশের যোগ বিয়োগ",
        "shongkha" to "সংখ্যার ধারণা ও জোড় বিজোড়",
        "jotil shongkha" to "জটিল সংখ্যা",
        "bijgonit" to "বীজগণিতের সূত্র",
        "bijgoniter sutro" to "বীজগণিতের সূত্র",
        "jyamiti" to "জ্যামিতির প্রাথমিক ধারণা ও কোণ",

        // Grammar (Bangla & English)
        "karok" to "কারক ও বিভক্তি",
        "karok kake bole" to "কারক কাকে বলে ও প্রকারভেদ",
        "shondhi" to "সন্ধি বিচ্ছেদ",
        "shondhi bicched" to "সন্ধি বিচ্ছেদ করার নিয়ম",
        "sondhi" to "সন্ধি বিচ্ছেদ",
        "sondhi bicched" to "সন্ধি বিচ্ছেদ",
        "shomash" to "সমাস",
        "shomash kake bole" to "সমাস কাকে বলে ও প্রকারভেদ",
        "dhoni" to "ধ্বনি ও বর্ণ",
        "bakko" to "বাক্য ও বাক্য রূপান্তর",
        "tense" to "Tense কাকে বলে ও গঠন",
        "tense kake bole" to "Tense কাকে বলে ও গঠন",
        "voice change" to "Voice Change এর নিয়ম",
        "narration" to "Narration পরিবর্তনের নিয়ম",
        "parts of speech" to "Parts of Speech এর শ্রেণিবিভাগ",

        // Technology & Modern Science
        "quantum computing" to "কোয়ান্টাম কম্পিউটিং",
        "quantum" to "কোয়ান্টাম কম্পিউটিং",
        "artificial intelligence" to "কৃত্রিম বুদ্ধিমত্তা",
        "ai" to "কৃত্রিম বুদ্ধিমত্তা",
        "machine learning" to "মেশিন লার্নিং",
        "cloud computing" to "ক্লাউড কম্পিউটিং",
        "cyber security" to "সাইবার নিরাপত্তা",

        // Bangladesh & General Knowledge
        "muktijuddho" to "বাংলাদেশের মুক্তিযুদ্ধ ও স্বাধীনতা",
        "mukti juddho" to "বাংলাদেশের মুক্তিযুদ্ধ ১৯৭১",
        "shadhinota dibosh" to "স্বাধীনতা দিবস ২৬ মার্চ",
        "bijoy dibosh" to "বিজয় দিবস ১৬ ডিসেম্বর",
        "padma setu" to "পদ্মা বহুমুখী সেতু",
        "bangladesh er rajdhani" to "বাংলাদেশের রাজধানী ঢাকা",
        "rajdhani" to "বাংলাদেশের রাজধানী ঢাকা",
        "shurjo" to "সূর্য ও সৌরজগৎ",
        "chad" to "চাঁদ ও চাঁদের কলা",
        "prithibi" to "পৃথিবীর গতি ও ঋতু পরিবর্তন",
        "rongdhanu" to "রংধনু কীভাবে সৃষ্টি হয়",
        "rainbow" to "রংধনু কীভাবে সৃষ্টি হয়",
        "porashona" to "পড়াশোনার সহজ নিয়ম ও কৌশল",
        "exam tips" to "পরীক্ষার প্রস্তুতি ও পড়া মনে রাখার কৌশল"
    )

    // Word level dictionary mapping
    private val wordMap = mapOf(
        "ki" to "কী",
        "kee" to "কী",
        "kivabe" to "কীভাবে",
        "kibhabe" to "কীভাবে",
        "kobe" to "কবে",
        "kothay" to "কোথায়",
        "keno" to "কেন",
        "kake" to "কাকে",
        "bole" to "বলে",
        "karon" to "কারণ",
        "koto" to "কত",
        "koy" to "কয়",
        "prokar" to "প্রকার",
        "shomadhan" to "সমাধান",
        "somadhan" to "সমাধান",
        "sutra" to "সূত্র",
        "shutra" to "সূত্র",
        "sutro" to "সূত্র",
        "shutro" to "সূত্র",
        "niyom" to "নিয়ম",
        "upaye" to "উপায়",
        "upay" to "উপায়",
        "bujhiye" to "বুঝিয়ে",
        "dao" to "দাও",
        "bolo" to "বলো",
        "likho" to "লিখো",
        "shikho" to "শেখো",
        "koro" to "করো",
        "kore" to "করে",
        "hoy" to "হয়",
        "hoyeche" to "হয়েছে",
        "hoyechilo" to "হয়েছিল",
        "somporke" to "সম্পর্কে",
        "bistarito" to "বিস্তারিত",
        "shohoj" to "সহজ",
        "sohoj" to "সহজ",
        "gonit" to "গণিত",
        "podartho" to "পদার্থ",
        "rosayon" to "রসায়ন",
        "jibon" to "জীবন",
        "biddut" to "বিদ্যুৎ",
        "alo" to "আলো",
        "laal" to "লাল",
        "lal" to "লাল",
        "neel" to "নীল",
        "nil" to "নীল",
        "shobuj" to "সবুজ",
        "sobuj" to "সবুজ",
        "holud" to "হলুদ",
        "beguni" to "বেগুনি",
        "shada" to "সাদা",
        "kalo" to "কালো",
        "chumbok" to "চুম্বক"
    )

    /**
     * Checks if the query contains Latin text that likely represents Banglish.
     */
    fun isBanglishOrLatin(query: String): Boolean {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return false

        var latinCount = 0
        var totalLetters = 0
        for (c in trimmed) {
            if (c.isLetter()) {
                totalLetters++
                if ((c in 'a'..'z') || (c in 'A'..'Z')) {
                    latinCount++
                }
            }
        }
        return totalLetters > 0 && (latinCount.toDouble() / totalLetters) >= 0.5
    }

    /**
     * Normalizes Banglish queries to pure Bangla queries and semantic keywords.
     */
    fun normalizeBanglish(query: String): BanglishResult {
        val qLower = query.lowercase().trim()

        // 1. Direct match with academic phrase dictionary
        for ((phrase, banglaEquivalent) in phraseMap) {
            if (qLower.contains(phrase)) {
                return BanglishResult(
                    originalQuery = query,
                    convertedBanglaQuery = "$banglaEquivalent সম্পর্কে পূর্ণাঙ্গ বিশ্লেষণ",
                    detectedSubject = detectSubjectFromPhrase(phrase),
                    isBanglish = true,
                    matchedAcademicTopic = banglaEquivalent
                )
            }
        }

        // 2. Word-by-word token substitution
        val tokens = qLower.split(Regex("""\s+"""))
        val convertedWords = mutableListOf<String>()
        var foundAnyMapping = false

        for (token in tokens) {
            val cleanToken = token.replace(Regex("""[^a-zA-Z0-9]"""), "")
            val mapped = wordMap[cleanToken]
            if (mapped != null) {
                convertedWords.add(mapped)
                foundAnyMapping = true
            } else {
                convertedWords.add(cleanToken)
            }
        }

        val convertedText = convertedWords.joinToString(" ")
        return BanglishResult(
            originalQuery = query,
            convertedBanglaQuery = if (foundAnyMapping) convertedText else query,
            detectedSubject = "সাধারণ",
            isBanglish = foundAnyMapping,
            matchedAcademicTopic = null
        )
    }

    private fun detectSubjectFromPhrase(phrase: String): String {
        return when {
            phrase.contains("jog") || phrase.contains("biyog") || phrase.contains("gun") || phrase.contains("bhag") ||
            phrase.contains("losagu") || phrase.contains("pithagoras") || phrase.contains("trikonmiti") || phrase.contains("bijgonit") -> "গণিত"

            phrase.contains("newton") || phrase.contains("goti") || phrase.contains("mahakorsho") || phrase.contains("alor") || phrase.contains("quantum") -> "পদার্থবিজ্ঞান"

            phrase.contains("poromanu") || phrase.contains("electron") || phrase.contains("acid") || phrase.contains("porjay") -> "রসায়ন"

            phrase.contains("salok") || phrase.contains("kosh") || phrase.contains("dna") || phrase.contains("mitosis") || phrase.contains("rokto") -> "জীববিজ্ঞান"

            phrase.contains("karok") || phrase.contains("shondhi") || phrase.contains("shomash") || phrase.contains("dhoni") -> "বাংলা ব্যাকরণ"

            phrase.contains("tense") || phrase.contains("voice") || phrase.contains("narration") || phrase.contains("parts of speech") -> "English Grammar"

            phrase.contains("ai") || phrase.contains("cloud") || phrase.contains("cyber") -> "কম্পিউটার ও প্রযুক্তি"

            phrase.contains("muktijuddho") || phrase.contains("padma") || phrase.contains("rajdhani") -> "বাংলাদেশ ও বিশ্বপরিচয়"

            else -> "সাধারণ শিক্ষা"
        }
    }

    data class BanglishResult(
        val originalQuery: String,
        val convertedBanglaQuery: String,
        val detectedSubject: String,
        val isBanglish: Boolean,
        val matchedAcademicTopic: String?
    )
}
