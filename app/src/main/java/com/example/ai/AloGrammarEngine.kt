package com.example.ai

/**
 * Offline Bangla & English Grammar Knowledge Engine for AloPro.
 * Specially designed for Bangladeshi students (JSC, SSC, HSC, Admission).
 */
object AloGrammarEngine {

    data class GrammarResult(
        val matched: Boolean,
        val category: String,
        val responseBangla: String
    )

    // Sandhi Database
    private val sandhiMap = mapOf(
        "বিদ্যালয়" to "বিদ্যা + আলয় (স্বরসন্ধি: আ + আ = আ)",
        "রবীন্দ্র" to "রবি + ইন্দ্র (স্বরসন্ধি: ই + ই = ঈ)",
        "মহাশয়" to "মহা + আশয় (স্বরসন্ধি: আ + আ = আ)",
        "সতীশ" to "সতী + ঈশ (স্বরসন্ধি: ঈ + ঈ = ঈ)",
        "পরীক্ষা" to "পরি + ঈক্ষা (স্বরসন্ধি: ই + ঈ = ঈ)",
        "সূর্যোদয়" to "সূর্য + উদয় (স্বরসন্ধি: অ + উ = ও)",
        "দেবেশ" to "দেব + ঈশ (স্বরসন্ধি: অ + ঈ = এ)",
        "মহর্ষি" to "মহা + ঋষি (স্বরসন্ধি: আ + ঋ = অর্)",
        "সদৈব" to "সদা + এব (স্বরসন্ধি: আ + এ = ঐ)",
        "মহৌষধ" to "মহা + ঔষধ (স্বরসন্ধি: আ + ঔ = ঔ)",
        "স্বাগত" to "সু + আগত (স্বরসন্ধি: উ + আ = ব-ফলা)",
        "অন্বেষণ" to "অনু + এষণ (স্বরসন্ধি: উ + এ = ব-ফলা)",
        "গায়ক" to "গৈ + অক (স্বরসন্ধি: ঐ + অ = আয়)",
        "নায়ক" to "নৈ + অক (স্বরসন্ধি: ঐ + অ = আয়)",
        "পাবন" to "পৌ + অন (স্বরসন্ধি: ঔ + অ = আব)",
        "দিগন্ত" to "দিক্ + অন্ত (ব্যঞ্জনসন্ধি: ক্ + অ = গ)",
        "সচ্চিন্তা" to "সৎ + চিন্তা (ব্যঞ্জনসন্ধি: ত্ + চ = চ্চ)",
        "উজ্জ্বল" to "উৎ + জ্বল (ব্যঞ্জনসন্ধি: ত্ + জ = জ্জ)",
        "উচ্ছ্বাস" to "উৎ + শ্বাস (ব্যঞ্জনসন্ধি: ত্ + শ = চ্ছ)",
        "পদ্ধতি" to "পদ্ + হতি (ব্যঞ্জনসন্ধি)",
        "সংবাদ" to "সম্ + বাদ (ব্যঞ্জনসন্ধি: ম্ + ব্যঞ্জন = ং)",
        "সংসার" to "সম্ + সার (ব্যঞ্জনসন্ধি: ম্ + স = ং)",
        "নমস্কার" to "নমঃ + কার (বিসর্গসন্ধি)",
        "পুরস্কার" to "পুরঃ + কার (বিসর্গসন্ধি)",
        "শিরশ্ছেদ" to "শিরঃ + ছেদ (বিসর্গসন্ধি)",
        "মনোরম" to "মনঃ + রম (বিসর্গসন্ধি: ঃ + র = ও-কার)"
    )

    // Idioms / বাগধারা
    private val bagdharaMap = mapOf(
        "অগাধ জলের মাছ" to "খুব চালাক ব্যক্তি। (বাক্য: লোকটিকে সহজে বোঝা যাবে না, সে আসলে অগাধ জলের মাছ।)",
        "অগস্ত্য যাত্রা" to "চিরতরে প্রস্থান বা শেষ বিদায়।",
        "অনুরোধে ঢেঁকি গেলা" to "কারো অনুরোধে পড়ে কঠিন বা অসম্ভব কাজ করতে বাধ্য হওয়া।",
        "অন্ধের যষ্টি" to "একমাত্র অবলম্বন বা ভরসা। (বাক্য: একমাত্র ছেলেটিই বৃদ্ধ বাবার অন্ধের যষ্টি।)",
        "আকাশ কুসুম" to "অবাস্তব বা অসম্ভব কল্পনা।",
        "আমড়া কাঠের ঢেঁকি" to "অপদার্থ বা অকেজো লোক।",
        "ইঁদুর কপালে" to "নিতান্তই মন্দভাগ্য বা দুর্ভাগ্যবান।",
        "উভয় সংকট" to "দুই দিক থেকেই বিপদের সম্মুখীন হওয়া (শাঁখের করাত)।",
        "একাদশে বৃহস্পতি" to "সৌভাগ্যের বিষয় বা সুসময়।",
        "কলুর বলদ" to "একটানা একটানা বিশ্রামহীন পরিশ্রম করা ব্যক্তি।",
        "গড্ডলিকা প্রবাহ" to "অন্ধভাবে অন্যকে অনুসরণ করা।",
        "ডুমুরের ফুল" to "অদৃশ্য বা দুর্লভ বস্তু।",
        "তালপাতার সেপাই" to "অতিশয় রোগা ও দুর্বল ব্যক্তি।",
        "বিড়াল তপস্বী" to "ভণ্ড সাধু বা কপট ধার্মিক।",
        "ভূতের বেগার" to "বৃথা বা নিষ্ফল শ্রম।"
    )

    // Ek Kothay Prokash / এককথায় প্রকাশ
    private val ekKothayMap = mapOf(
        "অক্ষির সমীপে" to "সমক্ষ",
        "অক্ষির অগোচরে" to "পরোক্ষ",
        "উপকারের উপকার স্বীকার করে যে" to "কৃতজ্ঞ",
        "উপকারের অপকার করে যে" to "কৃতঘ্ন",
        "যা পূর্বে দেখা যায়নি" to "অদৃষ্টপূর্ব",
        "যা সহজে লাভ করা যায় না" to "দুর্লভ",
        "যা চিরস্থায়ী নয়" to "নশ্বর",
        "যার কোনো উপায় নেই" to "নিরুপায়",
        "যে ভবিষ্যৎ না ভেবে কাজ করে" to "অবিমৃশ্যকারী",
        "ফল পাকলে যে গাছ মরে যায়" to "ওষধি",
        "মৃত্যুর মতো অবস্থা যার" to "মুমূর্ষু"
    )

    fun tryParseGrammar(query: String): GrammarResult? {
        val q = query.lowercase().trim()

        // Sandhi check
        for ((word, rule) in sandhiMap) {
            if (q.contains(word)) {
                return GrammarResult(
                    matched = true,
                    category = "বাংলা ব্যাকরণ (সন্ধি)",
                    responseBangla = """
                        📖 **সন্ধি বিচ্ছেদ:**
                        **শব্দ:** $word
                        **সন্ধি বিচ্ছেদ:** $rule
                        
                        💡 **মনে রাখার টিপস:**
                        বাংলা ভাষায় সন্ধি মূলত ধ্বনির মিলন। এটি তিন প্রকার: স্বরসন্ধি, ব্যঞ্জনসন্ধি এবং বিসর্গসন্ধি।
                    """.trimIndent()
                )
            }
        }

        // Idiom check
        for ((idiom, meaning) in bagdharaMap) {
            if (q.contains(idiom)) {
                return GrammarResult(
                    matched = true,
                    category = "বাংলা ব্যাকরণ (বাগধারা)",
                    responseBangla = """
                        📜 **বাগধারা ও ভাবার্থ:**
                        **বাগধারা:** $idiom
                        **অর্থ:** $meaning
                        
                        💡 **পরীক্ষার টিপস:** বাগধারা সরাসরি অর্থ প্রকাশ না করে বিশেষ রূপক অর্থ প্রকাশ করে।
                    """.trimIndent()
                )
            }
        }

        // Ek Kothay Prokash check
        for ((phrase, word) in ekKothayMap) {
            if (q.contains(phrase) || q.contains(word)) {
                return GrammarResult(
                    matched = true,
                    category = "বাংলা ব্যাকরণ (এককথায় প্রকাশ)",
                    responseBangla = """
                        ✍️ **এককথায় প্রকাশ (বাক্য সংকোচন):**
                        **বাক্যাংশ:** $phrase
                        **সংকুচিত রূপ:** **$word**
                    """.trimIndent()
                )
            }
        }

        // Samas general check
        if (q.contains("সমাস") || q.contains("samas")) {
            return GrammarResult(
                matched = true,
                category = "বাংলা ব্যাকরণ (সমাস)",
                responseBangla = explainSamas(q)
            )
        }

        // Karok general check
        if (q.contains("কারক") || q.contains("karok") || q.contains("বিভক্তি")) {
            return GrammarResult(
                matched = true,
                category = "বাংলা ব্যাকরণ (কারক ও বিভক্তি)",
                responseBangla = explainKarok(q)
            )
        }

        // English Tense check
        if (q.contains("tense") || q.contains("টেনস") || q.contains("কাল")) {
            return GrammarResult(
                matched = true,
                category = "ইংরেজি গ্রামার (Tense)",
                responseBangla = explainTense(q)
            )
        }

        // English Voice Change check
        if (q.contains("voice") || q.contains("বাচ্য") || q.contains("active to passive")) {
            return GrammarResult(
                matched = true,
                category = "ইংরেজি গ্রামার (Voice Change)",
                responseBangla = explainVoiceChange()
            )
        }

        // English Narration
        if (q.contains("narration") || q.contains("উক্তি") || q.contains("direct to indirect")) {
            return GrammarResult(
                matched = true,
                category = "ইংরেজি গ্রামার (Narration)",
                responseBangla = explainNarration()
            )
        }

        return null
    }

    private fun explainSamas(q: String): String {
        return """
            📘 **সমাস (Compound Words) নির্ণয়ের সহজ কৌশল:**
            
            সমাস প্রধানত ৬ প্রকার:
            
            ১. **দ্বন্দ্ব সমাস:** উভয় পদের অর্থ প্রধান থাকে এবং মাঝে 'ও, এবং, আর' থাকে।
               - যেমন: মা-বাবা = মা ও বাবা; ভাই-বোন = ভাই ও বোন।
            
            ২. **দ্বিগু সমাস:** প্রথমে সংখ্যাবাচক শব্দ থাকে এবং সমাহার বুঝায়। পরপদের অর্থ প্রধান।
               - যেমন: চৌরাস্তা = চার রাস্তার সমাহার; শতাব্দী = শত অব্দের সমাহার।
            
            ৩. **কর্মধারয় সমাস:** বিশেষণ ও বিশেষ্য মিলে হয়, পরপদের অর্থ প্রধান থাকে।
               - যেমন: নীলপদ্ম = যা নীল তা পদ্ম; মহারাজ = যিনি মহান তিনি রাজা।
            
            ৪. **তৎপুরুষ সমাস:** পূর্বপদের বিভক্তি লোপ পেয়ে যে সমাস হয়।
               - যেমন: বিপদাপন্ন = বিপদকে আপন্ন (২য়া); হস্তশিল্প = হস্তের শিল্প (৬ষ্ঠী)।
            
            ৫. **বহুব্রীহি সমাস:** পূর্বপদ বা পরপদ কোনোটির অর্থ না বুঝিয়ে অন্য নতুন অর্থ বোঝায়।
               - যেমন: দশানন = দশ আনন যার (রাবণ); পীতাম্বর = পীত অম্বর যার (শ্রীকৃষ্ণ)।
            
            ৬. **অব্যয়ীভাব সমাস:** পূর্বপদে অব্যয় যুক্ত হয়ে সমাস গঠিত হয়।
               - যেমন: উপকূল = কূলের সমীপে; যথারীতি = রীতিকে অতিক্রম না করে।
        """.trimIndent()
    }

    private fun explainKarok(q: String): String {
        return """
            🔍 **কারক ও বিভক্তি চেনার সহজ শর্টকাট কৌশল:**
            
            ক্রিয়াকে প্রশ্ন করে সহজেই কারক নির্ণয় করুন:
            
            ১. **কর্তৃকারক:** কে বা কারা দিয়ে প্রশ্ন করলে কর্তৃকারক পাওয়া যায়।
               - উদাহরণ: *রফিক* বই পড়ে। (কে পড়ে? = রফিক)
            
            ২. **কর্মকারক:** কি বা কাকে দিয়ে প্রশ্ন করলে কর্মকারক পাওয়া যায়।
               - উদাহরণ: ডাক্তার *রোগীকে* দেখছেন। (কাকে দেখছেন? = রোগীকে)
            
            ৩. **করণকারক:** কি দিয়ে বা কিসের দ্বারা প্রশ্ন করলে করণকারক পাওয়া যায়। (করণ অর্থ যন্ত্র বা মাধ্যম)
               - উদাহরণ: আমরা *কলম দিয়ে* লিখি। (কি দিয়ে? = কলম দিয়ে)
            
            ৪. **সম্প্রদান কারক:** নিঃস্বার্থভাবে দান করা বোঝালে সম্প্রদান কারক হয়।
               - উদাহরণ: *ভিক্ষুককে* ভিক্ষা দাও। (স্বার্থহীন দান)
            
            ৫. **অপাদান কারক:** যা থেকে কিছু চ্যুত, চলিত, ভীত, উৎপন্ন বা আরম্ভ হয় (কোথা হতে?)।
               - উদাহরণ: *গাছ থেকে* পাতা পড়ে; *তিলে* তৈল হয়।
            
            ৬. **অধিকরণ কারক:** স্থান, কাল বা সময় বোঝালে (কোথায়? কখন?)।
               - উদাহরণ: *পুকুরে* মাছ আছে (স্থান); *সকালে* সূর্য ওঠে (সময়)।
        """.trimIndent()
    }

    private fun explainTense(q: String): String {
        return """
            ⏰ **ইংরেজি ১২টি Tense-এর গঠন ও উদাহরণ (সহজ বাংলায়):**
            
            🟢 **Present Tense (বর্তমান কাল):**
            1. **Present Indefinite:** Subject + Verb1 (s/es) + Object.
               - *I eat rice.* / *He plays cricket.*
            2. **Present Continuous:** Subject + am/is/are + Verb+ing + Object.
               - *I am reading a book.* (আমি বই পড়ছি)
            3. **Present Perfect:** Subject + have/has + Verb3 (past participle) + Object.
               - *I have finished my homework.* (আমি কাজ শেষ করেছি)
            4. **Present Perfect Continuous:** Subject + have/has been + Verb+ing + for/since + Time.
               - *It has been raining since morning.* (সকাল থেকে বৃষ্টি হচ্ছে)
            
            🔵 **Past Tense (অতীত কাল):**
            1. **Past Indefinite:** Subject + Verb2 (past form) + Object.
               - *He went to school.* (সে স্কুলে গিয়েছিল)
            2. **Past Continuous:** Subject + was/were + Verb+ing + Object.
               - *They were playing football.* (তারা ফুটবল খেলছিল)
            3. **Past Perfect:** Subject + had + Verb3 + Object (Before / After).
               - *The train had left before we reached.*
            4. **Past Perfect Continuous:** Subject + had been + Verb+ing + for/since.
            
            🟠 **Future Tense (ভবিষ্যত কাল):**
            1. **Future Indefinite:** Subject + will/shall + Verb1 + Object.
               - *I will help you.* (আমি তোমাকে সাহায্য করব)
            2. **Future Continuous:** Subject + will be + Verb+ing + Object.
               - *She will be singing.* (সে গান গাইতে থাকবে)
            3. **Future Perfect:** Subject + will have + Verb3.
               - *They will have arrived by tomorrow.*
        """.trimIndent()
    }

    private fun explainVoiceChange(): String {
        return """
            🔄 **Active to Passive Voice রূপান্তরের ৫টি সহজ নিয়ম:**
            
            ১. Active বাক্যের **Object** টি Passive-এ **Subject** হয়।
            ২. Tense ও Subject এর Person অনুযায়ী সাহায্যকারী ক্রিয়াপদ (Auxiliary Verb) বসে:
               - Present Indefinite: am/is/are
               - Continuous: being যোগ হয় (is being, are being)
               - Perfect: been যোগ হয় (have been, has been)
               - Past Indefinite: was/were
               - Future: will be / will have been
            ৩. মূল Verb টির সর্বদা **Past Participle (V3)** রূপ বসে।
            ৪. সাধারণত প্রিপজিশন হিসেবে **'by'** বসে (কখনও to, with, at)।
            ৫. Active বাক্যের **Subject** টি Passive-এ **Object** হিসেবে বসে।
            
            📝 **উদাহরণ:**
            - Active: *He plays football.*
              Passive: *Football is played by him.*
            - Active: *She is writing a letter.*
              Passive: *A letter is being written by her.*
            - Active: *We have won the match.*
              Passive: *The match has been won by us.*
        """.trimIndent()
    }

    private fun explainNarration(): String {
        return """
            🗣️ **Narration (Direct to Indirect Speech) এর সহজ নিয়ম:**
            
            ১. **Reporting Verb পরিবর্তন:**
               - Assertive: said to -> told / said that
               - Interrogative: said to -> asked (if / wh-word)
               - Imperative: ordered / requested / advised
            
            ২. **Tense পরিবর্তন (যদি Reporting verb Past হয়):**
               - Present Indefinite ➔ Past Indefinite
               - Present Continuous ➔ Past Continuous
               - Present Perfect ➔ Past Perfect
               - Past Indefinite ➔ Past Perfect
               - will/shall ➔ would
               - can ➔ could / may ➔ might
            
            ৩. **নিকটবর্তী শব্দকে দূরবর্তী শব্দে রূপান্তর:**
               - this ➔ that, now ➔ then, today ➔ that day, yesterday ➔ the previous day, tomorrow ➔ the next day.
            
            📝 **উদাহরণ:**
            - Direct: He said, "I am ill."
            - Indirect: He said that he was ill.
        """.trimIndent()
    }
}
