package com.example.data.model

/**
 * Earlier hand-authored primary learning examples for classes 1–5.
 * This is not a complete or textbook-verified NCTB transcription.
 * The MVP presents a small selection through MvpFeaturedCurriculum.
 */
object PrimaryCurriculumData {

    val levels: List<CurriculumClassLevel> = listOf(
        // =========================================================================
        // CLASS 1 (প্রথম শ্রেণি)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 1,
            classNameBangla = "প্রথম শ্রেণি (Class 1)",
            badgeBangla = "প্রারম্ভিক ভিত্তি",
            subjects = listOf(
                CurriculumSubject(
                    id = "c1_math",
                    nameBangla = "প্রাথমিক গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c1_m1",
                            titleBangla = "১ থেকে ৫০ পর্যন্ত গণনা ও স্থানীয় মান",
                            summaryBangla = "সংখ্যা চেনা, কথায় লেখা, দাগ টেনে গণনা এবং একক-দশক ধারণা।",
                            detailsBangla = "প্রথম শ্রেণির গণিতে ১ থেকে ৫০ পর্যন্ত সংখ্যা অঙ্ক এবং কথায় (এক, দুই, তিন... পঞ্চাশ) নির্ভুলভাবে শেখানো হয়। বস্তুর সংখ্যা গণনা করে তুলনা করা (কম ও বেশি) এবং একক ও দশকের সহজ ধারণা দেওয়া হয়।",
                            formulaOrRule = "১ দশক = ১০ একক | ৩ দশক ৫ একক = ৩৫",
                            keyPoints = listOf("সংখ্যা প্রতীক চেনা (১-৫০)", "কথায় ও অঙ্কে মেলানো", "ছোট থেকে বড় সাজানো", "বস্তু গুনে যোগ"),
                            exampleBangla = "প্রশ্ন: ৫টি আম ও ৩টি আম একসাথে রাখলে মোট কতটি আম হয়?\nউত্তর: (৫ + ৩) = ৮টি আম।"
                        ),
                        CurriculumTopic(
                            id = "c1_m2",
                            titleBangla = "সহজ যোগ ও বিয়োগের ধারণা (১-২০)",
                            summaryBangla = "বস্তুর মাধ্যমে একত্র করা (যোগ) এবং বাদ দেওয়া (বিয়োগ)।",
                            detailsBangla = "ছবি দেখে মার্বেল, পাতা বা ফল গুণে যোগ এবং বিয়োগ করা। বাস্তব উপকরণের মাধ্যমে একত্র করলে বাড়ে (যোগ) এবং সরিয়ে নিলে কমে (বিয়োগ) এই বোধ তৈরি করা।",
                            formulaOrRule = "যোগ চিহ্ন (+), বিয়োগ চিহ্ন (-), সমান চিহ্ন (=)",
                            keyPoints = listOf("৭ + ৪ = ১১", "১০ - ৩ = ৭", "হাতে না রেখে সহজ যোগ-বিয়োগ", "শূন্য (০) যোগ করলে সংখ্যা একই থাকে"),
                            exampleBangla = "প্রশ্ন: তোমার কাছে ৭টি পেন্সিল ছিল, ভাইয়া ২টি নিয়ে গেল। কয়টি রইল?\nউত্তর: ৭ - ২ = ৫টি পেন্সিল।"
                        ),
                        CurriculumTopic(
                            id = "c1_m3",
                            titleBangla = "জোড় ও বিজোড় সংখ্যা এবং তুলনা",
                            summaryBangla = "জোড়ায় জোড়ায় সাজিয়ে জোড় (২, ৪, ৬, ৮...) ও বিজোড় (১, ৩, ৫, ৭...) চেনা।",
                            detailsBangla = "যেসব সংখ্যাকে দুটি দুটি করে সমান জোড়া করা যায় সেগুলো জোড় সংখ্যা; আর একটি অবশিষ্ট থাকলে বিজোড় সংখ্যা।",
                            formulaOrRule = "জোড় সংখ্যা: ২, ৪, ৬, ৮, ১০, ১২... | বিজোড় সংখ্যা: ১, ৩, ৫, ৭, ৯, ১১...",
                            keyPoints = listOf("জোড় সংখ্যায় এককের ঘরে ০, ২, ৪, ৬, ৮ থাকে", "বিজোড় সংখ্যায় এককের ঘরে ১, ৩, ৫, ৭, ৯ থাকে", "তুলনা চিহ্ন: ছোট (<), বড় (>)"),
                            exampleBangla = "প্রশ্ন: ৬ জোড় নাকি বিজোড়?\nউত্তর: ৬ হলো জোড় সংখ্যা (৩ জোড়া)।"
                        ),
                        CurriculumTopic(
                            id = "c1_m4",
                            titleBangla = "মৌলিক জ্যামিতিক আকৃতি ও পরিমাপ",
                            summaryBangla = "গোল (বৃত্ত), চারকোনা (বর্গ/আয়ত), ও তিনকোনা (ত্রিভুজ) চেনা।",
                            detailsBangla = "বাস্তব জীবনের থালা ও চাঁদের গোল আকৃতি, বই ও দরজার চারকোনা আকৃতি এবং সিঙ্গাড়া বা পাহাড়ের তিনকোনা আকৃতি পর্যবেক্ষণ করে জ্যামিতির প্রাথমিক পরিচয়।",
                            formulaOrRule = "গোল = বৃত্ত | তিনকোনা = ত্রিভুজ | চারকোনা = চতুর্ভুজ",
                            keyPoints = listOf("গোল = বৃত্তাকার", "তিনকোনা = ত্রিভুজাকার", "চারকোনা = চারকোণা/বর্গাকার", "ভারী ও হালকা তুলনা"),
                            exampleBangla = "প্রশ্ন: ফুটবল কোন আকৃতির?\nউত্তর: ফুটবল গোলকাকার। এর সমতল ছবি বৃত্তের মতো দেখা যায়।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c1_bangla",
                    nameBangla = "আমার বাংলা বই",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c1_b1",
                            titleBangla = "স্বরবর্ণ ও ব্যঞ্জনবর্ণের উচ্চারণ ও লিখন",
                            summaryBangla = "অ থেকে ঔ (১১টি স্বরবর্ণ) এবং ক থেকে ঁ (৩৯টি ব্যঞ্জনবর্ণ)।",
                            detailsBangla = "বাংলা লিপির ভিত্তি হলো ৫০টি বর্ণমালা। সঠিক উচ্চারণ, বর্ণ দিয়ে শব্দ তৈরি (যেমন: অ-তে অজগর, আ-তে আম, ই-তে ইঁদুর) এবং সুন্দর হাতের লেখার প্রাথমিক অভ্যাস গড়ে তোলা।",
                            formulaOrRule = "স্বরবর্ণ ১১টি + ব্যঞ্জনবর্ণ ৩৯টি = মোট ৫০টি",
                            keyPoints = listOf("স্বরবর্ণ: অ, আ, ই, ঈ, উ, ঊ, ঋ, এ, ঐ, ও, ঔ", "ব্যঞ্জনবর্ণ: ক-বর্গ, চ-বর্গ, ট-বর্গ, ত-বর্গ, প-বর্গ", "কারচিহ্ন ১০টি (া, ি, ী, ু, ূ, ৃ, ে, ৈ, ো, ৌ)"),
                            exampleBangla = "প্রশ্ন: বাংলা ভাষায় স্বরবর্ণ কয়টি ও কী কী?\nউত্তর: স্বরবর্ণ মোট ১১টি (অ, আ, ই, ঈ, উ, ঊ, ঋ, এ, ঐ, ও, ঔ)।"
                        ),
                        CurriculumTopic(
                            id = "c1_b2",
                            titleBangla = "কারচিহ্ন ও সহজ শব্দ গঠন",
                            summaryBangla = "আ-কার (া), ই-কার (ি), ঈ-কার (ী) দিয়ে শব্দ তৈরি।",
                            detailsBangla = "স্বরবর্ণের সংক্ষিপ্ত রূপকে কারচিহ্ন বলে। ব্যঞ্জনবর্ণের সাথে কারচিহ্ন যোগ করে শব্দ গঠন করা হয়। অ বর্ণের কোনো নিজস্ব কারচিহ্ন নেই।",
                            formulaOrRule = "ক + া = কা (কাকা) | ম + া = মা (মামা) | ব + ি = বি (বিড়াল)",
                            keyPoints = listOf("কারচিহ্ন মোট ১০টি", "অ বর্ণের কোনো কারচিহ্ন নেই", "সহজ দুই অক্ষরের শব্দ: বাবা, মা, ফুল, ফল"),
                            exampleBangla = "প্রশ্ন: 'বই' বানানটিতে কী কী বর্ণ আছে?\nউত্তর: ব + ই = বই।"
                        ),
                        CurriculumTopic(
                            id = "c1_b3",
                            titleBangla = "ছড়া ও কবিতা আবৃত্তি",
                            summaryBangla = "'আতা গাছে তোতা পাখি', 'ভোর হলো দোর খোলো' ইত্যাদি ছন্দময় ছড়া।",
                            detailsBangla = "ছন্দের তালে তালে শব্দ উচ্চারণ, সাবলীল কথা বলা এবং শব্দভাণ্ডার সমৃদ্ধ করার লক্ষ্যে ক্লাসিক বাংলা ছড়া ও ছোট গল্প পাঠ।",
                            keyPoints = listOf("তাল বজায় রেখে পাঠ", "স্পষ্ট উচ্চারণে ভাব প্রকাশ", "নতুন শব্দের অর্থ অনুধাবন"),
                            exampleBangla = "ছড়া: 'ভোর হলো দোর খোলো, খুকুমণি ওঠ রে!'"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c1_english",
                    nameBangla = "English for Today",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c1_e1",
                            titleBangla = "English Alphabet & Phonics (Aa to Zz)",
                            summaryBangla = "Capital & small letters, phonics sounds, and basic word association.",
                            detailsBangla = "Learning letter forms (Aa, Bb, Cc... Zz), letter sounds (Phonics), and early vocabulary like Apple, Ball, Cat, Doll, Elephant, Fish.",
                            formulaOrRule = "Letter Sound: /æ/ for A, /b/ for B, /k/ for C",
                            keyPoints = listOf("Alphabet recognition A-Z & a-z", "26 letters (5 Vowels: A, E, I, O, U)", "Simple 3-letter CVC words (cat, bat, pen)"),
                            exampleBangla = "Question: What is the first letter of 'Apple'?\nAnswer: Letter 'A'."
                        ),
                        CurriculumTopic(
                            id = "c1_e2",
                            titleBangla = "Greetings, Introduction & Numbers (1 to 20)",
                            summaryBangla = "Hello, Good morning, What is your name?, Numbers in English.",
                            detailsBangla = "Basic conversational greetings: 'Hello! I am Rahi', 'How are you? I am fine, thank you.' Counting numbers 1 to 20 in English.",
                            formulaOrRule = "Greeting: Hello / Good morning / Goodbye",
                            keyPoints = listOf("Greetings: Good morning, Good afternoon, Goodbye", "Self-introduction: My name is...", "Numbers: One (1), Two (2), Three (3)... Twenty (20)"),
                            exampleBangla = "Question: How do you say 'শুভ সকাল' in English?\nAnswer: Good morning."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c1_health",
                    nameBangla = "সহায়ক: পরিবেশ ও স্বাস্থ্যবিধি",
                    iconName = "health",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c1_h1",
                            titleBangla = "পরিচ্ছন্নতা ও স্বাস্থ্যকর অভ্যাস",
                            summaryBangla = "খাওয়ার আগে ও পরে সাবান দিয়ে হাত ধোয়া, দাঁত মাজা ও নখ কাটা।",
                            detailsBangla = "সুস্থ থাকার মূল শর্ত হলো ব্যক্তিগত পরিষ্কার-পরিচ্ছন্নতা বজায় রাখা। দিনে দুবার দাঁত ব্রাশ করা, নিয়মিত সাবান দিয়ে গোসল এবং পরিষ্কার পোশাক পরিধান।",
                            formulaOrRule = "২০ সেকেন্ড সাবান দিয়ে হাত ধোয়া | দিনে ২ বার দাঁত ব্রাশ",
                            keyPoints = listOf("খাওয়ার আগে সাবান দিয়ে হাত ধোয়া", "নিয়মিত নখ ছোট রাখা", "নিরাপদ ও ফুটানো পানি পান করা"),
                            exampleBangla = "প্রশ্ন: কখন কখন হাত ধুতে হবে?\nউত্তর: খাওয়ার আগে ও শৌচাগার ব্যবহারের পর সাবান দিয়ে ভালোভাবে হাত ধুতে হবে।"
                        ),
                        CurriculumTopic(
                            id = "c1_h2",
                            titleBangla = "আমাদের চারপাশের গাছপালা ও পশুপাখি",
                            summaryBangla = "আম, জাম, কাঁঠাল গাছ চেনা; গরু, ছাগল, হাঁস, মুরগি ও পাখির পরিচয়।",
                            detailsBangla = "আমাদের বাড়ির চারপাশে নানা গাছপালা ও গৃহপালিত পশু বাস করে। উদ্ভিদের ফুল ও ফল হয়। পশুপাখিকে ভালোবাসা ও যত্ন নেওয়া উচিত।",
                            keyPoints = listOf("গাছপালা আমাদের অক্সিজেন ও ফলমূল দেয়", "গরু ও ছাগল গৃহপালিত পশু", "জাতীয় পাখি দোয়েল"),
                            exampleBangla = "প্রশ্ন: আমাদের জাতীয় পাখির নাম কী?\nউত্তর: দোয়েল।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c1_moral",
                    nameBangla = "সহায়ক: ইসলাম ও নৈতিক শিক্ষা",
                    iconName = "religion",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c1_mo1",
                            titleBangla = "সত্যবাদিতা ও বড়দের সম্মান",
                            summaryBangla = "সবসময় সত্য কথা বলা, মা-বাবা ও শিক্ষককে সালাম দেওয়া।",
                            detailsBangla = "নৈতিক চরিত্রের ভিত্তি শৈশবে গড়ে ওঠে। সহপাঠীদের সাথে মিলেমিশে থাকা, মিথ্যা না বলা এবং গুরুজনদের শ্রদ্ধা করা।",
                            keyPoints = listOf("সর্বদা সত্য কথা বলা", "বড়দের সালাম ও সম্মান করা", "সহপাঠীদের সাহায্য করা"),
                            exampleBangla = "প্রশ্ন: ভালো মানুষের প্রধান গুণ কী?\nউত্তর: সত্য কথা বলা এবং সৎ পথে চলা।"
                        )
                    )
                )
            )
        ),

        // =========================================================================
        // CLASS 2 (দ্বিতীয় শ্রেণি)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 2,
            classNameBangla = "দ্বিতীয় শ্রেণি (Class 2)",
            badgeBangla = "মৌলিক দক্ষতা",
            subjects = listOf(
                CurriculumSubject(
                    id = "c2_math",
                    nameBangla = "প্রাথমিক গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c2_m1",
                            titleBangla = "১ থেকে ১০ এর নামতা ও গুণের ধারণা",
                            summaryBangla = "যোগের সংক্ষিপ্ত রূপ গুণ এবং ১ থেকে ১০ এর নামতার মুখস্থ ও প্রয়োগ।",
                            detailsBangla = "গুণ হলো একই সংখ্যা বারবার যোগ করার সহজ নিয়ম। ক্লাসে ২, ৩, ৪, ৫ ও ১০ এর নামতা শেখানো হয় এবং বাস্তব সমস্যা সমাধানে নামতা ব্যবহার করা হয়।",
                            formulaOrRule = "গুণফল = গুণ্য × গুণক | ৫ + ৫ + ৫ = ৫ × ৩ = ১৫",
                            keyPoints = listOf("২ এর নামতা: ২×১=২, ২×২=৪, ২×৩=৬...", "৫ এর নামতা: ৫, ১০, ১৫, ২০, ২৫...", "১০ এর নামতা: ১০, ২০, ৩০, ৪০..."),
                            exampleBangla = "প্রশ্ন: ৪ জন ছাত্রের প্রত্যেকে ৩টি করে খাতা পেলে মোট কতটি খাতা হবে?\nউত্তর: ৪ × ৩ = ১২টি খাতা।"
                        ),
                        CurriculumTopic(
                            id = "c2_m2",
                            titleBangla = "হাতে রেখে যোগ ও বিয়োগ (দুই অঙ্কের সংখ্যা)",
                            summaryBangla = "এককের ঘরের যোগফল ১০ বা বেশি হলে দশক ঘরে হাতে রাখা নিয়ম।",
                            detailsBangla = "যেমন ৩৮ + ২৫ যোগ করার সময় এককের ঘরে ৮ + ৫ = ১৩, ৩ লিখে হাতে ১ থাকে। দশকের ঘরে ৩ + ২ + ১ = ৬, মোট উত্তর ৬৩। বিয়োগেও ধার করার পদ্ধতি স্পষ্ট করা।",
                            formulaOrRule = "একক + একক, দশক + দশক (হাতে রাখা যোগ ও বিয়োগ)",
                            keyPoints = listOf("স্থানীয় মান সাজিয়ে যোগ", "হাতে রাখা ১ দশকে যোগ", "ধার নিয়ে বিয়োগ"),
                            exampleBangla = "প্রশ্ন: ৪৮ + ৩৭ = কত?\nউত্তর: ৪৮ + ৩৭ = ৮৫।"
                        ),
                        CurriculumTopic(
                            id = "c2_m3",
                            titleBangla = "সহজ ভাগের প্রাথমিক ধারণা",
                            summaryBangla = "সমান ভাগে ভাগ করে দেওয়া এবং নামতার সাহায্যে ভাগ নির্ণয়।",
                            detailsBangla = "ভাগ হলো সমান অংশে বণ্টন করা। এটি গুণের বিপরীত প্রক্রিয়া। যেমন ১২টি মার্বেল ৩ জনের মধ্যে সমানভাবে দিলে প্রত্যেকে ৪টি করে পায় (১২ ÷ ৩ = ৪)।",
                            formulaOrRule = "ভাজ্য ÷ ভাজক = ভাগফল | ১২ ÷ ৩ = ৪ (কারণ ৩ × ৪ = ১২)",
                            keyPoints = listOf("ভাগ চিহ্ন হলো ÷", "সমান ভাগে ভাগ করা", "নামতা দিয়ে সরাসরি ভাগ"),
                            exampleBangla = "প্রশ্ন: ১৫টি লিচু ৫ জন শিশুর মাঝে সমান ভাগ করলে প্রত্যেকে কয়টি পাবে?\nউত্তর: ১৫ ÷ ৫ = ৩টি।"
                        ),
                        CurriculumTopic(
                            id = "c2_m4",
                            titleBangla = "বাংলাদেশি মুদ্রা ও ভগ্নাংশের ধারণা",
                            summaryBangla = "পয়সা ও টাকা চেনা (১, ২, ৫, ১০, ২০, ৫০, ১০০ টাকা নোট) এবং অর্ধেক (১/২)।",
                            detailsBangla = "মুদ্রা বিনিময়, সহজ কেনাকাটার হিসাব এবং একটি বস্তুকে সমান দুই বা চার ভাগে ভাগ করার ধারণা।",
                            formulaOrRule = "১০০ পয়সা = ১ টাকা | অর্ধেক = ১/২, চার ভাগের এক ভাগ = ১/৪",
                            keyPoints = listOf("কাগজের নোট ও ধাতব মুদ্রা", "দৈনন্দিন কেনাকাটার হিসাব", "ভগ্নাংশের চিত্রাঙ্কন"),
                            exampleBangla = "প্রশ্ন: ৫০ টাকার ২টি নোটে মোট কত টাকা হয়?\nউত্তর: ৫০ + ৫০ = ১০০ টাকা।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c2_bangla",
                    nameBangla = "আমার বাংলা বই",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c2_b1",
                            titleBangla = "যুক্তবর্ণ গঠন ও শব্দ প্রয়োগ",
                            summaryBangla = "ক+ত=ক্ত, স+ত=স্ত, ন+দ=ন্দ, শ+র=শ্র ইত্যাদি যুক্তবর্ণ চেনা ও ভাঙা।",
                            detailsBangla = "বাংলা বানান শুদ্ধ করার মূল চাবিকাঠি হলো যুক্তবর্ণের সঠিক বিশ্লেষণ ও প্রয়োগ শেখা।",
                            formulaOrRule = "ক + ষ = ক্ষ | জ + ঞ = জ্ঞ | ত + ত = ত্ত | শ + র = শ্র",
                            keyPoints = listOf("যুক্তবর্ণ ভাঙা ও লেখা", "শব্দ তৈরি: রক্ত, সুন্দর, পরীক্ষা", "বাক্য গঠন"),
                            exampleBangla = "প্রশ্ন: 'ক্ষ' যুক্তবর্ণটি কীভাবে গঠিত?\nউত্তর: ক + ষ = ক্ষ (যেমন: শিক্ষক, ক্ষমা)।"
                        ),
                        CurriculumTopic(
                            id = "c2_b2",
                            titleBangla = "বিপরীত শব্দ ও সমার্থক শব্দ",
                            summaryBangla = "সকাল-সন্ধ্যা, ভালো-মন্দ, দিন-রাত এবং সহজ বাক্য প্রয়োগ।",
                            detailsBangla = "শব্দভাণ্ডার বৃদ্ধি এবং বিপরীত অর্থ প্রকাশের মাধ্যমে ভাষার প্রকাশভঙ্গি সমৃদ্ধ করা।",
                            keyPoints = listOf("উঁচু - নিচু", "সততা - অসততা", "আলো - অন্ধকার", "পানি = জল = নীর"),
                            exampleBangla = "প্রশ্ন: 'আলো' শব্দের বিপরীত শব্দ কী?\nউত্তর: অন্ধকার।"
                        ),
                        CurriculumTopic(
                            id = "c2_b3",
                            titleBangla = "বাক্য রচনা ও ছোট গল্প পাঠ",
                            summaryBangla = "শব্দ দিয়ে অর্থপূর্ণ বাক্য তৈরি এবং অনুচ্ছেদ পড়ে প্রশ্নোত্তর।",
                            detailsBangla = "ছোট ছোট শব্দ সাজিয়ে সুন্দর বাক্য গঠন করা (যেমন: জাতীয় পাখি, আমাদের বিদ্যালয়, বর্ষাকাল)।",
                            keyPoints = listOf("শব্দের সঠিক বিন্যাস", "দাঁড়ি (।) ও প্রশ্নবোধক (?) চিহ্ন ব্যবহার", "পড়ে বোঝার ক্ষমতা"),
                            exampleBangla = "বাক্য রচনা: নদী ➔ আমাদের দেশে অনেক নদী আছে।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c2_english",
                    nameBangla = "English for Today",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c2_e1",
                            titleBangla = "Days of the Week & Months",
                            summaryBangla = "Saturday to Friday, 12 months, and daily routine sentences.",
                            detailsBangla = "Students learn the 7 days of the week, months of the year, ordering numbers, and forming short sentences like 'Today is Sunday', 'I go to school'.",
                            formulaOrRule = "7 Days: Saturday, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday",
                            keyPoints = listOf("Spelling of 7 days", "12 months in English", "Capital letters for days and months"),
                            exampleBangla = "Question: What day comes after Monday?\nAnswer: Tuesday."
                        ),
                        CurriculumTopic(
                            id = "c2_e2",
                            titleBangla = "Simple Sentences & Prepositions (in, on, under)",
                            summaryBangla = "Understanding positions: in the box, on the table, under the tree.",
                            detailsBangla = "Describing locations of objects using simple prepositions like 'in', 'on', 'under'. Framing short sentences with Subject + Verb.",
                            formulaOrRule = "Prepositions: in (ভিতরে), on (উপরে), under (নিচে)",
                            keyPoints = listOf("The cat is under the table", "The book is on the desk", "Action verbs: read, write, play, jump"),
                            exampleBangla = "Question: Where is the ball? (ছবিতে টেবিলের উপর)\nAnswer: The ball is on the table."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c2_social",
                    nameBangla = "সহায়ক: পরিবেশ ও প্রাথমিক বিজ্ঞান",
                    iconName = "science",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c2_s1",
                            titleBangla = "আমাদের পরিবেশ ও বিভিন্ন পেশাজীবী",
                            summaryBangla = "কৃষক, জেলে, কামার, কুমার, তাঁতি, ডাক্তার ও শিক্ষকের অবদান।",
                            detailsBangla = "সমাজে একে অপরের ওপর নির্ভরশীলতা এবং প্রতিটি কাজের মর্যাদা বোঝা। পরিবেশের গাছপালা ও পশুপাখির যত্ন নেওয়া।",
                            keyPoints = listOf("শ্রমের মর্যাদা", "গাছ লাগানো ও পরিচর্যা", "পরিবারের বয়োজ্যেষ্ঠদের সম্মান করা"),
                            exampleBangla = "প্রশ্ন: কৃষক কীভাবে আমাদের সাহায্য করেন?\nউত্তর: কৃষক জমিতে ফসল ও খাদ্য উৎপাদন করে আমাদের বাঁচিয়ে রাখেন।"
                        ),
                        CurriculumTopic(
                            id = "c2_s2",
                            titleBangla = "ঋতু ও আবহাওয়া (ছয় ঋতু)",
                            summaryBangla = "গ্রীষ্ম, বর্ষা, শরৎ, হেমন্ত, শীত ও বসন্ত - ঋতুর বৈশিষ্ট্য।",
                            detailsBangla = "বাংলাদেশে দুই মাস পর পর ঋতু পরিবর্তন হয়। গ্রীষ্মে গরম ও রসালো ফল, বর্ষায় বৃষ্টি, শীতে ঠান্ডা ও কুয়াশা, বসন্তে ফুল ফোটে।",
                            formulaOrRule = "৬ ঋতু: গ্রীষ্ম, বর্ষা, শরৎ, হেমন্ত, শীত, বসন্ত",
                            keyPoints = listOf("বৈশাখ-জ্যৈষ্ঠ = গ্রীষ্মকাল", "আষাঢ়-শ্রাবণ = বর্ষাকাল", "পৌষ-মাঘ = শীতকাল"),
                            exampleBangla = "প্রশ্ন: কোন ঋতুকে ঋতুরাজ বলা হয়?\nউত্তর: বসন্ত ঋতুকে।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c2_moral",
                    nameBangla = "সহায়ক: ইসলাম ও নৈতিক শিক্ষা",
                    iconName = "religion",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c2_r1",
                            titleBangla = "ভালো অভ্যাস ও সৃষ্টির সেবা",
                            summaryBangla = "নিয়মমাফিক চলা, পরিষ্কার থাকা, সত্য কথা বলা এবং দুর্বলকে সাহায্য করা।",
                            detailsBangla = "ধর্মীয় অনুশাসন আমাদের ভালো মানুষ হতে শেখায়। পিতা-মাতা, শিক্ষক ও প্রতিবেশীর সাথে উত্তম ব্যবহার করা এবং অহংকার না করা।",
                            keyPoints = listOf("প্রতিদিনের দোয়া ও প্রার্থনা", "সৃষ্টির সেবা ও পশুপাখিকে খাবার দেওয়া", "সহমর্মিতা ও ক্ষমা"),
                            exampleBangla = "প্রশ্ন: সৃষ্টির সেবা কাকে বলে?\nউত্তর: মানুষ, গাছপালা ও পশুপাখির যত্ন নেওয়া ও সাহায্য করাকে সৃষ্টির সেবা বলে।"
                        )
                    )
                )
            )
        ),

        // =========================================================================
        // CLASS 3 (তৃতীয় শ্রেণি)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 3,
            classNameBangla = "তৃতীয় শ্রেণি (Class 3)",
            badgeBangla = "ধারণাগত বিকাশ",
            subjects = listOf(
                CurriculumSubject(
                    id = "c3_math",
                    nameBangla = "প্রাথমিক গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c3_m1",
                            titleBangla = "চার প্রক্রিয়া ও ভাগের সূত্র",
                            summaryBangla = "চার অঙ্কের সংখ্যার হিসাব ও নিঃশেষে বিভাজ্য ও অবিভাজ্য ভাগের নিয়ম।",
                            detailsBangla = "তৃতীয় শ্রেণিতে চার অঙ্কের যোগ-বিয়োগ এবং পূর্ণাঙ্গ গুণ ও ভাগের প্রক্রিয়া শেখানো হয়। ভাগের অংশগুলো: ভাজ্য, ভাজক, ভাগফল ও ভাগশেষ।",
                            formulaOrRule = "ভাজ্য = (ভাজক × ভাগফল) + ভাগশেষ | ভাগশেষ < ভাজক",
                            keyPoints = listOf("ভাগশেষ সর্বদা ভাজকের চেয়ে ছোট হয়", "কোনো সংখ্যাকে ০ দিয়ে গুণ করলে ০ হয়", "দৈনন্দিন ভাষার সমস্যা সমাধান"),
                            exampleBangla = "প্রশ্ন: ভাজক ৪, ভাগফল ৬ ও ভাগশেষ ১ হলে ভাজ্য কত?\nউত্তর: ভাজ্য = (৪ × ৬) + ১ = ২৫।"
                        ),
                        CurriculumTopic(
                            id = "c3_m2",
                            titleBangla = "সাধারণ ভগ্নাংশ ও সমতুল ভগ্নাংশ",
                            summaryBangla = "লব ও হর এর ধারণা, প্রকৃত ভগ্নাংশ, সমহর ভগ্নাংশের যোগ-বিয়োগ।",
                            detailsBangla = "ভগ্নাংশের উপরের সংখ্যাকে লব এবং নিচের সংখ্যাকে হর বলে। হর একই থাকলে লবগুলোর সরাসরি যোগ বা বিয়োগ হয়। লব ও হরকে একই সংখ্যা দিয়ে গুণ বা ভাগ করলে সমতুল ভগ্নাংশ পাওয়া যায়।",
                            formulaOrRule = "ভগ্নাংশ = লব / হর | সমহর যোগ: a/c + b/c = (a+b)/c",
                            keyPoints = listOf("লব < হর হলে প্রকৃত ভগ্নাংশ", "১/২ ও ২/৪ হলো সমতুল ভগ্নাংশ", "ভগ্নাংশের তুলনা"),
                            exampleBangla = "প্রশ্ন: ২/৫ + ১/৫ = কত?\nউত্তর: (২ + ১)/৫ = ৩/৫।"
                        ),
                        CurriculumTopic(
                            id = "c3_m3",
                            titleBangla = "পরিমাপ: দৈর্ঘ্য, ওজন ও সময় (ঘড়ির কাঁটা)",
                            summaryBangla = "মিটার, সেন্টিমিটার, কিলোগ্রাম, গ্রাম, ঘন্টা, মিনিট ও সেকেন্ড।",
                            detailsBangla = "দৈনন্দিন জীবনের স্কেল দিয়ে মাপা, দাঁড়িপাল্লায় বাটখারা চেনা এবং ঘড়ির কাঁটা দেখে সময় বলা ও সময় রূপান্তর।",
                            formulaOrRule = "১ মিটার = ১০০ সেমি | ১ কেজি = ১০০০ গ্রাম | ১ ঘন্টা = ৬০ মিনিট = ৩৬০০ সেকেন্ড",
                            keyPoints = listOf("দৈর্ঘ্যের একক মিটার", "ওজনের একক গ্রাম/কিলোগ্রাম", "সময়ের একক সেকেন্ড/মিনিট"),
                            exampleBangla = "প্রশ্ন: ৩ কেজি সমান কত গ্রাম?\nউত্তর: ৩ × ১০০০ = ৩০০০ গ্রাম।"
                        ),
                        CurriculumTopic(
                            id = "c3_m4",
                            titleBangla = "জ্যামিতি: কোণ, ত্রিভুজ ও চতুর্ভুজ",
                            summaryBangla = "সমকোণ, সূক্ষ্মকোণ, স্থূলকোণ এবং ৩ ও ৪ বাহুবিশিষ্ট জ্যামিতিক চিত্র।",
                            detailsBangla = "দুটি রেখা মিলিত হলে কোণ তৈরি হয়। ৯০° কোণকে সমকোণ বলে। ৩টি বাহু দ্বারা সীমাবদ্ধ ক্ষেত্রকে ত্রিভুজ এবং ৪টি বাহু দ্বারা সীমাবদ্ধ ক্ষেত্রকে চতুর্ভুজ বলে।",
                            formulaOrRule = "সমকোণ = ৯০° | সূক্ষ্মকোণ < ৯০° | স্থূলকোণ > ৯০°",
                            keyPoints = listOf("ত্রিভুজের ৩টি কোণ ও ৩টি বাহু থাকে", "চতুর্ভুজের ৪টি বাহু ও ৪টি কোণ থাকে", "বর্গক্ষেত্রের ৪টি কোণ সমকোণ"),
                            exampleBangla = "প্রশ্ন: যে কোণের মান ৯০° তাকে কী কোণ বলে?\nউত্তর: সমকোণ।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c3_science",
                    nameBangla = "প্রাথমিক বিজ্ঞান",
                    iconName = "science",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c3_s1",
                            titleBangla = "জীব ও জড়: মেরুদণ্ডী ও অমেরুদণ্ডী প্রাণী",
                            summaryBangla = "জীব ও জড়ের পার্থক্য, উদ্ভিদের বিভিন্ন অংশ এবং প্রাণীর শ্রেণিবিন্যাস।",
                            detailsBangla = "যাদের জীবন আছে তারা জীব (উদ্ভিদ ও প্রাণী), যাদের জীবন নেই তারা জড়। মেরুদণ্ডী প্রাণীদের ৫টি শ্রেণি: মাছ, উভচর, সরীসৃপ, পাখি ও স্তন্যপায়ী।",
                            formulaOrRule = "মেরুদণ্ডী প্রাণীর শ্রেণি: মাছ, উভচর (ব্যাঙ), সরীসৃপ (টিকটিকি), পাখি, স্তন্যপায়ী (মানুষ)",
                            keyPoints = listOf("জীবের বৃদ্ধি আছে ও বংশবৃদ্ধি করে", "উদ্ভিদ মূল দিয়ে মাটি আঁকড়ে থাকে ও পাতা দিয়ে খাদ্য বানায়", "মেরুদণ্ডী: মানুষ, গরু, মাছ | অমেরুদণ্ডী: কেঁচো, মশা, চিংড়ি"),
                            exampleBangla = "প্রশ্ন: কেঁচো কি মেরুদণ্ডী প্রাণী?\nউত্তর: না, কেঁচো অমেরুদণ্ডী প্রাণী।"
                        ),
                        CurriculumTopic(
                            id = "c3_s2",
                            titleBangla = "সুষম খাদ্য ও পুষ্টি উপাদান",
                            summaryBangla = "খাদ্যের প্রধান উপাদান, ভিটামিন ও খনিজ লবণের গুরুত্ব।",
                            detailsBangla = "দেহ সুস্থ ও সবল রাখার জন্য প্রয়োজন পুষ্টিকর সুষম খাদ্য। শর্করা আমাদের শক্তি দেয়, আমিষ দেহ গঠন ও ক্ষয়পূরণ করে, ভিটামিন রোগ প্রতিরোধ করে।",
                            formulaOrRule = "খাদ্যের ৩টি প্রধান কাজ: শক্তি প্রদান, দেহ গঠন, রোগ প্রতিরোধ",
                            keyPoints = listOf("ভাত, রুটি, আলু = শর্করা", "মাছ, মাংস, ডাল, ডিম = আমিষ", "শাকসবজি ও ফলমূল = ভিটামিন ও খনিজ লবণ"),
                            exampleBangla = "প্রশ্ন: আমিষ জাতীয় খাবারের প্রধান কাজ কী?\nউত্তর: শরীরের বৃদ্ধি সাধন ও ক্ষয়পূরণ করা।"
                        ),
                        CurriculumTopic(
                            id = "c3_s3",
                            titleBangla = "মাটি ও বায়ুর ব্যবহার এবং দূষণ",
                            summaryBangla = "বেলে, এঁটেল ও দোআঁশ মাটির বৈশিষ্ট্য এবং বিশুদ্ধ বাতাসের গুরুত্ব।",
                            detailsBangla = "মাটি তিন প্রকার: বেলে মাটি (বালুকাময়), এঁটেল মাটি (আঠালো) এবং দোআঁশ মাটি (ফসল চাষের জন্য সবচেয়ে উপযোগী)। বায়ুতে অক্সিজেন, কার্বন ডাই-অক্সাইড ও নাইট্রোজেন থাকে।",
                            keyPoints = listOf("দোআঁশ মাটি ফসল চাষের সেরা মাটি", "প্রাণী শ্বাস নিতে অক্সিজেন গ্রহণ করে", "গাছ বায়ুর CO₂ শোষণ করে পরিবেশ নির্মল রাখে"),
                            exampleBangla = "প্রশ্ন: কোন মাটিতে ফসল সবচেয়ে ভালো হয়?\nউত্তর: দোআঁশ মাটিতে।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c3_bangla",
                    nameBangla = "আমার বাংলা বই",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c3_b1",
                            titleBangla = "ভাষা আন্দোলন ও দেশের গান",
                            summaryBangla = "১৯৫২ সালের ২১শে ফেব্রুয়ারি, ভাষা শহীদ ও অমর একুশের গান।",
                            detailsBangla = "মাতৃভাষা বাংলার মর্যাদার জন্য সালাম, বরকত, রফিক, জব্বার প্রমুখ জীবন দেন। ২১শে ফেব্রুয়ারি এখন আন্তর্জাতিক মাতৃভাষা দিবস।",
                            keyPoints = listOf("১৯৫২ সালের ২১শে ফেব্রুয়ারি রাষ্ট্রভাষা আন্দোলন", "শহীদ মিনার ঢাকার প্রাণকেন্দ্রে অবস্থিত", "একুশের গান: 'আমার ভাইয়ের রক্তে রাঙানো একুশে ফেব্রুয়ারি'"),
                            exampleBangla = "প্রশ্ন: আন্তর্জাতিক মাতৃভাষা দিবস কবে পালিত হয়?\nউত্তর: প্রতি বছর ২১শে ফেব্রুয়ারি।"
                        ),
                        CurriculumTopic(
                            id = "c3_b2",
                            titleBangla = "ব্যাকরণ: পদ পরিচয় (বিশেষ্য ও সর্বনাম)",
                            summaryBangla = "নামবাচক শব্দ (বিশেষ্য) এবং নামের পরিবর্তে ব্যবহৃত শব্দ (সর্বনাম)।",
                            detailsBangla = "কোনো ব্যক্তি, স্থান, বস্তু বা গুণের নামকে বিশেষ্য পদ বলে (যেমন: ঢাকা, নদী, গোলাপ)। নামের পরিবর্তে যা বসে তাকে সর্বনাম পদ বলে (যেমন: সে, তিনি, তারা, আমরা)।",
                            formulaOrRule = "বিশেষ্য = নাম | সর্বনাম = নামের বিকল্প (সে, তুমি, আমি)",
                            keyPoints = listOf("বিশেষ্যের উদাহরণ: বাংলাদেশ, হিমেল, বই", "সর্বনামের উদাহরণ: তিনি, আমরা, তোমরা", "বাক্যে সঠিক পদ নির্বাচন"),
                            exampleBangla = "প্রশ্ন: 'সাকিব ভালো খেলে। সে একজন ক্রিকেটার।' এখানে সর্বনাম কোনটি?\nউত্তর: 'সে' হলো সর্বনাম পদ।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c3_english",
                    nameBangla = "English for Today",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c3_e1",
                            titleBangla = "Punctuation, Capitalization & Pronouns",
                            summaryBangla = "Full stop, comma, question mark, and capital letter usage.",
                            detailsBangla = "Every sentence starts with a Capital letter and ends with a full stop (.) or question mark (?). Names of people and places always start with a Capital letter.",
                            formulaOrRule = "Sentence Rule: Capital letter at start + Punctuation at end",
                            keyPoints = listOf("Full stop (.) for statement", "Question mark (?) for question", "Capital letters for I, names, countries, days"),
                            exampleBangla = "Question: Correct the sentence: 'where do you live'\nAnswer: 'Where do you live?'"
                        ),
                        CurriculumTopic(
                            id = "c3_e2",
                            titleBangla = "Present Tense & Wh-Questions Basics",
                            summaryBangla = "Simple Present sentences and asking questions with Who, What, Where.",
                            detailsBangla = "Forming everyday routines: 'I wake up early', 'He reads a book'. Asking basic questions with 'What is this?', 'Who is he?', 'Where is the school?'.",
                            formulaOrRule = "Subject + Verb(s/es for He/She/It) | What (কী), Who (কে), Where (কোথায়)",
                            keyPoints = listOf("I play / He plays", "What is your father?", "Where do you read?"),
                            exampleBangla = "Question: Translate to English: 'সে স্কুলে যায়।'\nAnswer: He goes to school."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c3_social",
                    nameBangla = "বাংলাদেশ ও বিশ্বপরিচয়",
                    iconName = "social",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c3_soc1",
                            titleBangla = "আমাদের সমাজ, পরিবার ও জাতীয় প্রতীকসমূহ",
                            summaryBangla = "পরিবারের দায়িত্ব, সমাজের বিভিন্ন পেশাজীবী এবং জাতীয় পতাকা ও সঙ্গীত।",
                            detailsBangla = "আমাদের জাতীয় পতাকা লাল-সবুজের আয়তাকার (অনুপাত ১০:৬ বা ৫:৩)। জাতীয় ফুল শাপলা, জাতীয় ফল কাঁঠাল, জাতীয় পশু রয়েল বেঙ্গল টাইগার এবং জাতীয় পাখি দোয়েল।",
                            formulaOrRule = "জাতীয় পতাকার অনুপাত ১০ : ৬ (বা ৫ : ৩)",
                            keyPoints = listOf("পতাকার লাল বৃত্তের ব্যাসার্ধ দৈর্ঘ্যের ৫ ভাগের ১ ভাগ", "জাতীয় সংগীত: 'আমার সোনার বাংলা' (প্রথম ১০ চরণ)", "বিভিন্ন পেশা: কৃষক, জেলে, শিক্ষক, ডাক্তার"),
                            exampleBangla = "প্রশ্ন: বাংলাদেশের জাতীয় পতাকার অনুপাত কত?\nউত্তর: ১০ : ৬ (বা ৫ : ৩)।"
                        ),
                        CurriculumTopic(
                            id = "c3_soc2",
                            titleBangla = "মহাদেশ ও মহাসাগরের পরিচয়",
                            summaryBangla = "পৃথিবীর ৭টি মহাদেশ ও ৫টি মহাসাগরের প্রাথমিক পরিচয়।",
                            detailsBangla = "পৃথিবীতে ৭টি মহাদেশ রয়েছে: এশিয়া, আফ্রিকা, ইউরোপ, উত্তর আমেরিকা, দক্ষিণ আমেরিকা, অস্ট্রেলিয়া ও অ্যান্টার্কটিকা। বাংলাদেশ এশিয়া মহাদেশে অবস্থিত।",
                            formulaOrRule = "৭ মহাদেশ ও ৫ মহাসাগর (বৃহত্তম: প্রশান্ত মহাসাগর)",
                            keyPoints = listOf("এশিয়া পৃথিবীর বৃহত্তম মহাদেশ", "প্রশান্ত মহাসাগর বৃহত্তম মহাসাগর", "দক্ষিণে বঙ্গোপসাগর অবস্থিত"),
                            exampleBangla = "প্রশ্ন: বাংলাদেশ কোন মহাদেশে অবস্থিত?\nউত্তর: এশিয়া মহাদেশে।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c3_religion",
                    nameBangla = "ইসলাম ও নৈতিক শিক্ষা",
                    iconName = "religion",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c3_r1",
                            titleBangla = "ঈমান ও আকাইদ: তাওহিদ ও রিসালাত",
                            summaryBangla = "আল্লাহ তায়ালার একত্ববাদ, কালিমা তাইয়্যেবা ও রাসূলগণের প্রতি বিশ্বাস।",
                            detailsBangla = "ঈমানের মূল ভিত্তি হলো আল্লাহ এক ও অদ্বিতীয়, তাঁর কোনো শরিক নেই। কালিমা তাইয়্যেবা: লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ।",
                            keyPoints = listOf("আল্লাহর গুণবাচক নামসমূহ", "পবিত্রতা ও অজু করার নিয়ম", "পিতা-মাতা ও শিক্ষকের প্রতি সম্মান"),
                            exampleBangla = "প্রশ্ন: কালিমা তাইয়্যেবার অর্থ কী?\nউত্তর: আল্লাহ ছাড়া কোনো উপাস্য নেই, মুহাম্মদ (সা.) আল্লাহর রাসূল।"
                        ),
                        CurriculumTopic(
                            id = "c3_r2",
                            titleBangla = "সালাতের গুরুত্ব ও অজু করার নিয়ম",
                            summaryBangla = "দৈনিক ৫ ওয়াক্ত সালাত এবং অজুর ৪টি ফরজ।",
                            detailsBangla = "সালাত জান্নাতের চাবিকাঠি। সালাতের পূর্বে পবিত্রতা অর্জনের জন্য অজু করা আবশ্যক। অজুর ৪টি ফরজ সঠিকভাবে আদায় করতে হয়।",
                            formulaOrRule = "অজুর ৪ ফরজ: ১. মুখ ধোয়া, ২. কনুইসহ হাত ধোয়া, ৩. মাথা মাসেহ করা, ৪. টাখনুসহ পা ধোয়া",
                            keyPoints = listOf("৫ ওয়াক্ত সালাত: ফজর, জোহর, আসর, মাগরিব, এশা", "সালাত আদায়ে শৃঙ্খলা আসে", "পবিত্রতা ঈমানের অঙ্গ"),
                            exampleBangla = "প্রশ্ন: অজুর ফরজ কয়টি?\nউত্তর: অজুর ফরজ মোট ৪টি।"
                        )
                    )
                )
            )
        ),

        // =========================================================================
        // CLASS 4 (চতুর্থ শ্রেণি)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 4,
            classNameBangla = "চতুর্থ শ্রেণি (Class 4)",
            badgeBangla = "দক্ষতা সংহতকরণ",
            subjects = listOf(
                CurriculumSubject(
                    id = "c4_math",
                    nameBangla = "প্রাথমিক গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c4_m1",
                            titleBangla = "গুণনীয়ক, গুণিতক, লসাগু ও গসাগু",
                            summaryBangla = "লসাগু (লঘিষ্ঠ সাধারণ গুণিতক) ও গসাগু (গরিষ্ঠ সাধারণ গুণনীয়ক) নির্ণয়।",
                            detailsBangla = "যেসব সংখ্যার ১ এবং ওই সংখ্যা ছাড়া অন্য কোনো গুণনীয়ক নেই তাদের মৌলিক সংখ্যা বলে (২, ৩, ৫, ৭, ১১...)। লসাগু হলো প্রদত্ত সংখ্যাগুলোর সবচেয়ে ছোট সাধারণ গুণিতক।",
                            formulaOrRule = "২ হলো একমাত্র জোড় মৌলিক সংখ্যা | লসাগু × গসাগু = সংখ্যাদ্বয়ের গুণফল",
                            keyPoints = listOf("১ মৌলিক সংখ্যা নয়", "মৌলিক উৎপাদকে বিশ্লেষণ", "১২ এর গুণনীয়ক: ১, ২, ৩, ৪, ৬, ১২"),
                            exampleBangla = "প্রশ্ন: ১২ ও ১৮ এর গসাগু এবং লসাগু কত?\nউত্তর: গসাগু = ৬ এবং লসাগু = ৩৬।"
                        ),
                        CurriculumTopic(
                            id = "c4_m2",
                            titleBangla = "দশমিক ভগ্নাংশ ও সাধারণ ভগ্নাংশের রূপান্তর",
                            summaryBangla = "দশমিক বিন্দু, স্থানীয় মান (দশাংশ, শতাংশ, সহস্রাংশ) এবং দশমিকের যোগ-বিয়োগ।",
                            detailsBangla = "দশমিক সংখ্যায় দশমিক বিন্দুর ঠিক পরের স্থানটি দশাংশ (১/১০), তার পরেরটি শতাংশ (১/১০০)। যোগ ও বিয়োগের সময় দশমিক বিন্দু বরাবর সোজা রেখে অঙ্ক বসাতে হয়।",
                            formulaOrRule = "০.৫ = ৫/১০ = ১/২ | ০.২৫ = ২৫/১০০ = ১/৪ | ০.৭৫ = ৩/৪",
                            keyPoints = listOf("দশমিক স্থানীয় মান ছক", "দশমিকের ডানে শূন্যের ভূমিকা", "দৈনন্দিন টাকা ও পরিমাপে ব্যবহার"),
                            exampleBangla = "প্রশ্ন: ৩.৪৫ + ২.৭ = কত?\nউত্তর: ৩.৪৫ + ২.৭০ = ৬.১৫।"
                        ),
                        CurriculumTopic(
                            id = "c4_m3",
                            titleBangla = "পরিমাপ ও ক্ষেত্রফল (আয়তক্ষেত্র ও বর্গক্ষেত্র)",
                            summaryBangla = "ক্ষেত্রফলের একক বর্গমিটার/বর্গসেন্টিমিটার এবং সূত্রের সাহায্যে ক্ষেত্রফল নির্ণয়।",
                            detailsBangla = "কোনো আবদ্ধ ক্ষেত্র যে পরিমাণ স্থান দখল করে তাকে তার ক্ষেত্রফল বলে। আয়তক্ষেত্রের ক্ষেত্রফল = দৈর্ঘ্য × প্রস্থ। বর্গক্ষেত্রের ক্ষেত্রফল = এক বাহুর দৈর্ঘ্য × এক বাহুর দৈর্ঘ্য।",
                            formulaOrRule = "আয়তক্ষেত্রের ক্ষেত্রফল = দৈর্ঘ্য × প্রস্থ | বর্গক্ষেত্রের ক্ষেত্রফল = বাহু² | পরিসীমা = ২ × (দৈর্ঘ্য + প্রস্থ)",
                            keyPoints = listOf("দৈর্ঘ্য ও প্রস্থের একক একই হতে হবে", "পরিসীমা = চার বাহুর যোগফল", "বর্গক্ষেত্রের ৪টি বাহু সমান ও ৪টি কোণ সমকোণ"),
                            exampleBangla = "প্রশ্ন: ৮ মিটার দৈর্ঘ্য ও ৫ মিটার প্রস্থবিশিষ্ট মেঝের ক্ষেত্রফল কত?\nউত্তর: ৮ × ৫ = ৪০ বর্গমিটার।"
                        ),
                        CurriculumTopic(
                            id = "c4_m4",
                            titleBangla = "জ্যামিতি: ত্রিভুজের প্রকারভেদ ও কোণ",
                            summaryBangla = "সমবাহু, সমদ্বিবাহু ও বিষমবাহু ত্রিভুজ; রেখা, রেখাংশ ও রশ্মি।",
                            detailsBangla = "বাহুভেদে ত্রিভুজ ৩ প্রকার: ১. সমবাহু (৩ বাহু সমান), ২. সমদ্বিবাহু (২ বাহু সমান), ৩. বিষমবাহু (কোনো বাহু সমান নয়)। রেখাংশের নির্দিষ্ট দৈর্ঘ্য আছে কিন্তু রেখার কোনো প্রান্তবিন্দু নেই।",
                            formulaOrRule = "ত্রিভুজের ৩ কোণের সমষ্টি = ১৮০° | সরলকোণ = ১৮০°",
                            keyPoints = listOf("রেখাংশের ২টি প্রান্তবিন্দু থাকে", "রশ্মির ১টি প্রান্তবিন্দু থাকে", "সমবাহু ত্রিভুজের প্রতিটি কোণ ৬০°"),
                            exampleBangla = "প্রশ্ন: ত্রিভুজের তিন কোণের যোগফল কত ডিগ্রি?\nউত্তর: ১৮০ ডিগ্রি।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c4_science",
                    nameBangla = "প্রাথমিক বিজ্ঞান",
                    iconName = "science",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c4_s1",
                            titleBangla = "উদ্ভিদ ও প্রাণীর খাদ্য এবং সালোকসংশ্লেষণ",
                            summaryBangla = "উদ্ভিদ কীভাবে খাদ্য তৈরি করে এবং পুষ্টি চক্রের মাধ্যমে প্রাণীর বেঁচে থাকা।",
                            detailsBangla = "উদ্ভিদ মূলরোম দিয়ে মাটি থেকে পানি ও খনিজ লবণ শোষণ করে এবং পাতার ক্লোরোফিলের সাহায্যে সূর্যালোক ব্যবহার করে শর্করা জাতীয় খাদ্য প্রস্তুত করে। এই প্রক্রিয়ায় অক্সিজেন নির্গত হয়।",
                            formulaOrRule = "কার্বন ডাই-অক্সাইড + পানি + সূর্যালোক (ক্লোরোফিল) ➔ গ্লুকোজ + অক্সিজেন",
                            keyPoints = listOf("ক্লোরোফিল পাতার সবুজ প্লাস্টিড", "অক্সিজেন প্রাণী শ্বাসকার্যে ব্যবহার করে", "খাদ্য তৈরিতে কার্বন ডাই-অক্সাইড প্রয়োজন"),
                            exampleBangla = "প্রশ্ন: উদ্ভিদ খাদ্য তৈরির সময় কোন গ্যাস গ্রহণ ও কোন গ্যাস ত্যাগ করে?\nউত্তর: কার্বন ডাই-অক্সাইড গ্রহণ করে এবং অক্সিজেন ত্যাগ করে।"
                        ),
                        CurriculumTopic(
                            id = "c4_s2",
                            titleBangla = "পদার্থের তিন অবস্থা (কঠিন, তরল ও বায়বীয়)",
                            summaryBangla = "বরফ, পানি ও জলীয় বাষ্পের মাধ্যমে অবস্থার পরিবর্তন।",
                            detailsBangla = "যার ভর আছে এবং স্থান দখল করে তাকে পদার্থ বলে। পদার্থ তিন অবস্থায় থাকতে পারে: কঠিন (নির্দিষ্ট আকার ও আয়তন আছে), তরল (নির্দিষ্ট আয়তন আছে কিন্তু আকার নেই), বায়বীয় (নির্দিষ্ট আকার বা আয়তন নেই)।",
                            formulaOrRule = "কঠিন (বরফ) + তাপ ➔ তরল (পানি) + তাপ ➔ বায়বীয় (বাষ্প)",
                            keyPoints = listOf("পদার্থের বৈশিষ্ট্য: ভর, ওজন ও আয়তন", "তাপ প্রয়োগে অবস্থার পরিবর্তন ঘটে", "জলীয় বাষ্প ঠান্ডা হয়ে মেঘ ও বৃষ্টি হয়"),
                            exampleBangla = "প্রশ্ন: পানির তিনটি অবস্থা কী কী?\nউত্তর: বরফ (কঠিন), পানি (তরল) ও জলীয় বাষ্প (বায়বীয়)।"
                        ),
                        CurriculumTopic(
                            id = "c4_s3",
                            titleBangla = "পানি বিশুদ্ধকরণ ও পানিবাহিত রোগ",
                            summaryBangla = "ফুটিয়ে, ফিটকিরি ও ফিল্টারের মাধ্যমে পানি বিশুদ্ধকরণ। ডায়রিয়া ও কলেরা প্রতিরোধ।",
                            detailsBangla = "নিরাপদ পানি সুস্বাস্থ্যের ভিত্তি। পানি ২০ মিনিট ভালোভাবে ফুটালে জীবাণুমুক্ত হয়। ওরস্যালাইন তৈরির নিয়ম: আধ লিটার নিরাপদ পানিতে এক চিমটি লবণ ও এক মুঠ গুড় বা চিনি।",
                            formulaOrRule = "পানীয় জল ২০ মিনিট ফোটাতে হয় | ওরস্যালাইন: ৫০০ মিলি পানি + ১ চিমটি লবণ + ১ মুঠ গুড়",
                            keyPoints = listOf("আর্সেনিকমুক্ত নলকূপ সবুজ চিহ্নিত", "পানিবাহিত রোগ: ডায়রিয়া, কলেরা, টাইফয়েড, আমাশয়", "মলত্যাগের পর সাবান দিয়ে হাত ধোয়া"),
                            exampleBangla = "প্রশ্ন: ঘরে ওরস্যালাইন তৈরির সঠিক মাপ কী?\nউত্তর: আধা লিটার (৫০০ মিলি) বিশুদ্ধ পানিতে এক চিমটি লবণ ও এক মুঠো গুড়/চিনি।"
                        ),
                        CurriculumTopic(
                            id = "c4_s4",
                            titleBangla = "মহাবিশ্ব ও সৌরজগতের গ্রহসমূহ",
                            summaryBangla = "সূর্যকে কেন্দ্র করে ৮টি গ্রহের ঘূর্ণন এবং দিন-রাত্রির কারণ।",
                            detailsBangla = "সৌরজগতের কেন্দ্রে সূর্য অবস্থিত। সূর্যকে ঘিরে ৮টি গ্রহ ঘুরছে: বুধ, শুক্র, পৃথিবী, মঙ্গল, বৃহস্পতি, শনি, ইউরেনাস ও নেপচুন। চাঁদ পৃথিবীর একমাত্র প্রাকৃতিক উপগ্রহ।",
                            formulaOrRule = "সূর্য থেকে দূরত্বের ক্রম: বুধ < শুক্র < পৃথিবী < মঙ্গল < বৃহস্পতি < শনি < ইউরেনাস < নেপচুন",
                            keyPoints = listOf("বৃহস্পতি সৌরজগতের বৃহত্তম গ্রহ", "বুধ সূর্যের নিকটতম গ্রহ", "পৃথিবীর আহ্নিক গতির কারণে দিন-রাত্রি হয়"),
                            exampleBangla = "প্রশ্ন: সৌরজগতের সবচেয়ে বড় গ্রহ কোনটি?\nউত্তর: বৃহস্পতি (Jupiter)।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c4_bangla",
                    nameBangla = "আমার বাংলা বই",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c4_b1",
                            titleBangla = "পদ প্রকরণ ও লিঙ্গ পরিবর্তন",
                            summaryBangla = "বিশেষ্য, বিশেষণ, সর্বনাম, ক্রিয়া ও অব্যয় এবং পুংলিঙ্গ-স্ত্রীলিঙ্গ।",
                            detailsBangla = "বাক্যে ব্যবহৃত প্রত্যেকটি শব্দকে পদ বলে। পদ ৫ প্রকার। বিশেষণ পদ কোনো কিছুর দোষ, গুণ বা অবস্থা প্রকাশ করে। পুরুষ ও স্ত্রীবাচক শব্দ পরিবর্তনের সুনির্দিষ্ট নিয়ম।",
                            formulaOrRule = "পদ ৫ প্রকার: বিশেষ্য, বিশেষণ, সর্বনাম, অব্যয়, ক্রিয়া",
                            keyPoints = listOf("বিশেষণ: ভালো, লাল, দ্রুত", "ক্রিয়া: পড়ে, লেখে, যায়", "লিঙ্গান্তর: বালক-বালিকা, শিক্ষক-শিক্ষিকা, মামা-মামী"),
                            exampleBangla = "প্রশ্ন: 'হিমেল দ্রুত দৌড়ায়।' এখানে 'দ্রুত' কোন পদ?\nউত্তর: ক্রিয়া-বিশেষণ পদ।"
                        ),
                        CurriculumTopic(
                            id = "c4_b2",
                            titleBangla = "বিরামচিহ্ন ও আবেদনপত্র লিখন",
                            summaryBangla = "দাঁড়ি, কমা, প্রশ্নবোধক চিহ্ন এবং ছুটির দরখাস্তের কাঠামো।",
                            detailsBangla = "বাক্যের অর্থ সুস্পষ্ট করার জন্য বিরামচিহ্ন অপরিহার্য। প্রধান শিক্ষকের নিকট ছুটির আবেদনপত্রের অংশগুলো: তারিখ, প্রাপক, বিষয়, সম্বোধন, মূল বক্তব্য ও বিনীত নিবেদক।",
                            keyPoints = listOf("কমা (,) = ১ বলার সময় থামা", "দাঁড়ি (।) = ১ সেকেন্ড থামা", "দরখাস্তের কাঠামো নির্ভুল রাখা"),
                            exampleBangla = "প্রশ্ন: প্রধান শিক্ষকের কাছে দরখাস্তে সম্বোধনে কী লিখতে হয়?\nউত্তর: 'জনাব' বা 'মহোদয়'।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c4_english",
                    nameBangla = "English for Today",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c4_e1",
                            titleBangla = "Tenses (Present, Past, Future) & Articles",
                            summaryBangla = "Simple Present, Past & Future forms; A, An, The usage rules.",
                            detailsBangla = "Tense expresses the time of action: Present (I play), Past (I played), Future (I will play). 'A' is used before consonant sounds, 'An' before vowel sounds, and 'The' for specific/unique items.",
                            formulaOrRule = "Articles: A boy, An apple, The Sun | Past form of go = went, eat = ate",
                            keyPoints = listOf("Vowel sounds take 'An' (an umbrella, an hour)", "Unique things take 'The' (The Moon, The Padma)", "Simple Past shows completed action"),
                            exampleBangla = "Question: Fill in the blank: 'He is ___ honest man.'\nAnswer: 'an' (because 'honest' starts with an /ɒ/ vowel sound)."
                        ),
                        CurriculumTopic(
                            id = "c4_e2",
                            titleBangla = "Reading Comprehension & Paragraph Writing",
                            summaryBangla = "Answering questions from a story, writing 5 sentences on 'My Family' / 'My School'.",
                            detailsBangla = "Students learn to read unseen short paragraphs, find answers to questions, and write a cohesive 5-sentence short composition with correct spelling and punctuation.",
                            keyPoints = listOf("Locating key facts in text", "Topic sentence, supporting details", "Grammatically correct sentences"),
                            exampleBangla = "Question: Write one sentence about your school.\nAnswer: My school is one of the best primary schools in our upazila."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c4_social",
                    nameBangla = "বাংলাদেশ ও বিশ্বপরিচয়",
                    iconName = "social",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c4_soc1",
                            titleBangla = "বাংলাদেশের ভূপ্রকৃতি ও আটটি বিভাগ",
                            summaryBangla = "ঢাকা, চট্টগ্রাম, রাজশাহী, খুলনা, বরিশাল, সিলেট, রংপুর ও ময়মনসিংহ বিভাগের রূপরেখা।",
                            detailsBangla = "বাংলাদেশ নদীমাতৃক ও সমতল বদ্বীপ দেশ। প্রধান তিনটি নদী পদ্মা, মেঘনা ও যমুনা। আটটি প্রশাসনিক বিভাগ এবং ৬৪টি জেলা রয়েছে। সুন্দরবন পৃথিবীর বৃহত্তম ম্যানগ্রোভ বন।",
                            formulaOrRule = "বাংলাদেশের আয়তন ১,৪৭,৫৭০ বর্গকিমি | ৮টি বিভাগ ও ৬৪টি জেলা",
                            keyPoints = listOf("পদ্মা, মেঘনা, যমুনা ও কর্ণফুলী প্রধান নদী", "কক্সবাজার পৃথিবীর দীর্ঘতম প্রাকৃতিক সমুদ্র সৈকত (১২০ কিমি)", "সুন্দরবন খুলনা বিভাগে অবস্থিত"),
                            exampleBangla = "প্রশ্ন: বাংলাদেশের বৃহত্তম ম্যানগ্রোভ বনের নাম কী?\nউত্তর: সুন্দরবন।"
                        ),
                        CurriculumTopic(
                            id = "c4_soc2",
                            titleBangla = "প্রাকৃতিক দুর্যোগ ও দুর্যোগ ব্যবস্থাপনা",
                            summaryBangla = "বন্যা, ঘূর্ণিঝড়, খরা ও কালবৈশাখী ঝড়ের সময় নিরাপত্তা প্রস্তুতি।",
                            detailsBangla = "বাংলাদেশে বর্ষাকালে বন্যা ও বৈশাখ-জ্যৈষ্ঠে কালবৈশাখী ঝড় ও ঘূর্ণিঝড় দেখা দেয়। দুর্যোগের সময় শুকনা খাবার ও বিশুদ্ধ পানি সংরক্ষণ এবং জরুরি আশ্রয়কেন্দ্রে আশ্রয় নেওয়া প্রয়োজন।",
                            keyPoints = listOf("ঘূর্ণিঝড়ের সংকেত মেনে চলা", "উঁচু স্থানে নিরাপদ আশ্রয় নেওয়া", "জরুরি হেল্পলাইন নম্বর ৯৯৯"),
                            exampleBangla = "প্রশ্ন: দুর্যোগের সময় কেমন খাবার প্রস্তুত রাখতে হয়?\nউত্তর: শুকনো খাবার (যেমন: চিড়া, মুড়ি, গুড়) ও বিশুদ্ধ খাবার পানি।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c4_religion",
                    nameBangla = "ইসলাম ও নৈতিক শিক্ষা",
                    iconName = "religion",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c4_r1",
                            titleBangla = "সালাতের আরকান, আহকাম ও নৈতিক শিক্ষা",
                            summaryBangla = "সালাতের ভেতরের ও বাইরের ফরজসমূহ, সত্যবাদিতা ও ওয়াদা পালন।",
                            detailsBangla = "সালাত হলো ইসলামের দ্বিতীয় স্তম্ভ। সালাতের বাইরে ৭টি ফরজ (আহকাম) এবং ভেতরে ৬টি ফরজ (আরকান)। সত্যবাদিতা মানুষকে মুক্তি দেয় আর মিথ্যা ধ্বংস করে।",
                            formulaOrRule = "সালাতের ফরজ মোট ১৩টি (আহকাম ৭টি + আরকান ৬টি)",
                            keyPoints = listOf("সালাতের শর্তাবলী ও সময়ানুবর্তিতা", "ওয়াদা বা প্রতিশ্রুতি রক্ষা করা", "প্রতিবেশীর হক আদায়"),
                            exampleBangla = "প্রশ্ন: সালাতের মোট ফরজ কয়টি?\nউত্তর: মোট ১৩টি (বাইরে ৭টি আহকাম এবং ভেতরে ৬টি আরকান)।"
                        )
                    )
                )
            )
        ),

        // =========================================================================
        // CLASS 5 (পঞ্চম শ্রেণি - প্রাথমিক সমাপনী / PEC)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 5,
            classNameBangla = "পঞ্চম শ্রেণি (Class 5)",
            badgeBangla = "প্রাথমিক সমাপনী স্তর",
            subjects = listOf(
                CurriculumSubject(
                    id = "c5_math",
                    nameBangla = "প্রাথমিক গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c5_m1",
                            titleBangla = "ঐকিক নিয়ম ও চার প্রক্রিয়া সম্পর্কিত সমস্যা",
                            summaryBangla = "প্রথমে ১টির মান বের করে নির্দিষ্ট পরিমাণের হিসাব করার কৌশল ও বন্ধনীর নিয়ম।",
                            detailsBangla = "হিসাব সহজ করার একটি কার্যকর পদ্ধতি হলো ঐকিক নিয়ম। ১টির মান বের করতে ভাগ করতে হয়, আর বেশি পরিমাণের মান বের করতে গুণ করতে হয়। বন্ধনীযুক্ত রাশিতে বন্ধনীর ক্রম: ১ম বন্ধনী (), ২য় বন্ধনী {}, ৩য় বন্ধনী []।",
                            formulaOrRule = "১টির মান = মোট দাম ÷ সংখ্যা | নির্দিষ্ট মান = ১টির মান × সংখ্যা | BODMAS ক্রম",
                            keyPoints = listOf("বন্ধনী ক্রম: () ➔ {} ➔ []", "প্রক্রিয়ার ক্রম: এর ➔ ভাগ ➔ গুণ ➔ যোগ ➔ বিয়োগ", "কাজের ঐকিক নিয়মে লোক বাড়লে সময় কমে"),
                            exampleBangla = "প্রশ্ন: ৮টি ডিমের দাম ৭২ টাকা হলে ১৫টি ডিমের দাম কত?\nউত্তর: ১টি ডিমের দাম = ৭২ ÷ ৮ = ৯ টাকা। ১৫টি ডিমের দাম = ৯ × ১৫ = ১৩৫ টাকা।"
                        ),
                        CurriculumTopic(
                            id = "c5_m2",
                            titleBangla = "ভগ্নাংশ ও দশমিকের গুণ ও ভাগ",
                            summaryBangla = "বিপরীত ভগ্নাংশ দিয়ে গুণ এবং দশমিক স্থানান্তরের নিয়ম।",
                            detailsBangla = "ভগ্নাংশকে ভগ্নাংশ দিয়ে ভাগ করতে হলে দ্বিতীয় ভগ্নাংশটিকে বিপরীত ভগ্নাংশ বানিয়ে গুণ করতে হয়। কোনো সংখ্যাকে ১০, ১০০ দিয়ে গুণ করলে দশমিক ডানে সরে; ভাগ করলে বামে সরে।",
                            formulaOrRule = "(a/b) ÷ (c/d) = (a/b) × (d/c) | ১০ দিয়ে গুণ করলে দশমিক ১ ঘর ডানে যায়",
                            keyPoints = listOf("লঘিষ্ঠ আকারে প্রকাশ (কাটাকাটি)", "মিশ্র ভগ্নাংশকে অপ্রকৃত ভগ্নাংশে রূপান্তর", "ভগ্নাংশের সরলীকরণ"),
                            exampleBangla = "প্রশ্ন: ৩/৪ ÷ ৯/১৬ = কত?\nউত্তর: ৩/৪ × ১৬/৯ = ৪/৩ = ১ সমস্ত ১/৩।"
                        ),
                        CurriculumTopic(
                            id = "c5_m3",
                            titleBangla = "গড় ও শতকরা (লাভ-ক্ষতি ও সরল মুনাফা)",
                            summaryBangla = "গড় = রাশিগুলোর যোগফল ÷ রাশির সংখ্যা। শতকরা (%), মুনাফা = (P × r × n)/১০০।",
                            detailsBangla = "শতকরা হলো এমন একটি ভগ্নাংশ যার হর সর্বদা ১০০। মুনাফার সূত্রে আসল (P), মুনাফার হার (r%), সময় (n বছর) এবং মোট মুনাফা (I) পরস্পরের সাথে সম্পর্কিত।",
                            formulaOrRule = "গড় = যোগফল ÷ সংখ্যা | শতকরা লাভ = (মোট লাভ ÷ ক্রয়মূল্য) × ১০০ | মুনাফা I = (P × r × n) / 100",
                            keyPoints = listOf("শতকরা প্রতীক %", "লাভ = বিক্রয়মূল্য - ক্রয়মূল্য", "ক্ষতি = ক্রয়মূল্য - বিক্রয়মূল্য"),
                            exampleBangla = "প্রশ্ন: ৫০০ টাকার ১০% হারে ২ বছরের মুনাফা কত?\nউত্তর: I = (৫০০ × ১০ × ২) / ১০০ = ১০০ টাকা।"
                        ),
                        CurriculumTopic(
                            id = "c5_m4",
                            titleBangla = "জ্যামিতি: সামান্তরিক, রম্বস, ট্রাপিজিয়াম ও বৃত্ত",
                            summaryBangla = "চতুর্ভুজের বৈশিষ্ট্য, বৃত্তের কেন্দ্র, ব্যাস, ব্যাসার্ধ, জ্যা ও পরিধি।",
                            detailsBangla = "সামান্তরিকের বিপরীত বাহুগুলো সমান ও সমান্তরাল। রম্বসের চার বাহু সমান কিন্তু কোণগুলো সমকোণ নয়। বৃত্তের কেন্দ্র থেকে পরিধির দূরত্ব হলো ব্যাসার্ধ (r) এবং ব্যাস হলো ব্যাসার্ধের দ্বিগুণ (d = 2r)। বৃত্তের বৃহত্তম জ্যা হলো ব্যাস।",
                            formulaOrRule = "ব্যাস = ২ × ব্যাসার্ধ | পরিধি = ২πr | সামান্তরিকের ক্ষেত্রফল = ভূমি × উচ্চতা | ত্রিভুজের ক্ষেত্রফল = ½ × ভূমি × উচ্চতা",
                            keyPoints = listOf("রম্বসের কর্ণদ্বয় পরস্পরকে সমকোণে সমদ্বিখণ্ডিত করে", "বৃত্তের পরিধি ও ব্যাসের অনুপাত π (পাই ≈ ৩.১৪১৬)", "আয়তক্ষেত্রের ক্ষেত্রফল = দৈর্ঘ্য × প্রস্থ"),
                            exampleBangla = "প্রশ্ন: একটি বৃত্তের ব্যাসার্ধ ৩.৫ সেমি হলে তার ব্যাস কত?\nউত্তর: ব্যাস = ২ × ৩.৫ = ৭ সেমি।"
                        ),
                        CurriculumTopic(
                            id = "c5_m5",
                            titleBangla = "পরিমাপ ও উপাত্ত বিন্যস্তকরণ (স্তম্ভলেখ)",
                            summaryBangla = "মেট্রিক এককাবলি রূপান্তর, ক্ষেত্রফল ও জনসংখ্যা ঘনত্ব, স্তম্ভলেখ অঙ্কন।",
                            detailsBangla = "দৈর্ঘ্যের একক মিটার, ওজনের একক গ্রাম, আয়তনের একক লিটার। জনসংখ্যা ঘনত্ব = মোট জনসংখ্যা ÷ মোট ক্ষেত্রফল। উপাত্তকে চাক্ষুষ উপস্থাপনের জন্য স্তম্ভলেখ ব্যবহৃত হয়।",
                            formulaOrRule = "১ কিমি = ১০০০ মিটার | ১ কেজি = ১০০০ গ্রাম | জনসংখ্যা ঘনত্ব = জনসংখ্যা / ক্ষেত্রফল (বর্গকিমি)",
                            keyPoints = listOf("১ হেক্টর = ১০,০০০ বর্গমিটার", "ট্যালি চিহ্ন দিয়ে উপাত্ত গণনা", "স্তম্ভলেখের উল্লম্ব ও অনুভূমিক অক্ষ"),
                            exampleBangla = "প্রশ্ন: একটি গ্রামের আয়তন ৫ বর্গকিমি এবং জনসংখ্যা ২৫০০ জন হলে ঘনত্ব কত?\nউত্তর: ২৫০০ ÷ ৫ = ৫০০ জন/বর্গকিমি।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c5_science",
                    nameBangla = "প্রাথমিক বিজ্ঞান",
                    iconName = "science",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c5_s1",
                            titleBangla = "আমাদের পরিবেশ, বাস্তুসংস্থান ও খাদ্যশৃঙ্খল",
                            summaryBangla = "খাদ্য শৃঙ্খল, খাদ্য জাল, শক্তির রূপান্তর এবং নবায়নযোগ্য শক্তি।",
                            detailsBangla = "বাস্তুসংস্থানে উৎপাদক (সবুজ উদ্ভিদ) থেকে শুরু করে প্রথম স্তর, দ্বিতীয় স্তর ও সর্বোচ্চ স্তরের খাদক এবং বিয়োজকের মাধ্যমে খাদ্য শক্তি স্থানান্তরিত হয়। সূর্য সকল শক্তির মূল উৎস।",
                            formulaOrRule = "ঘাস (উৎপাদক) ➔ ঘাসফড়িং (১ম স্তর) ➔ ব্যাঙ (২য় স্তর) ➔ সাপ (৩য় স্তর) ➔ ঈগল (সর্বোচ্চ স্তর)",
                            keyPoints = listOf("সৌরশক্তি, বায়ুশক্তি ও জলবিদ্যুৎ নবায়নযোগ্য শক্তি", "জীবাশ্ম জ্বালানি পোড়ালে বায়ুমণ্ডলে CO₂ বাড়ে", "গ্রিনহাউস প্রভাব ও বৈশ্বিক উষ্ণায়ন"),
                            exampleBangla = "প্রশ্ন: খাদ্যশৃঙ্খলে শক্তির প্রধান উৎস কোনটি?\nউত্তর: সূর্য।"
                        ),
                        CurriculumTopic(
                            id = "c5_s2",
                            titleBangla = "সুস্থ জীবনের জন্য খাদ্য ও সংক্রামক রোগ",
                            summaryBangla = "জাঙ্ক ফুড বর্জন, সুষম খাদ্যের ৬টি উপাদান এবং সংক্রামক রোগের বিস্তার রোধ।",
                            detailsBangla = "খাদ্যের ৬টি উপাদান: শর্করা, আমিষ, স্নেহ, ভিটামিন, খনিজ লবণ ও পানি। জাঙ্ক ফুডে উচ্চ চর্বি ও লবণ থাকে যা স্বাস্থ্যের ক্ষতি করে। বায়ুবাহিত ও পানিবাহিত সংক্রামক রোগের প্রতিরোধ।",
                            formulaOrRule = "৬টি পুষ্টি উপাদান: শর্করা + আমিষ + স্নেহ + ভিটামিন + খনিজ লবণ + পানি",
                            keyPoints = listOf("ভিটামিন A রাতকানা রোগ প্রতিরোধ করে", "ভিটামিন C স্কার্ভি রোধ করে ও রোগ প্রতিরোধ ক্ষমতা বাড়ায়", "ভিটামিন D হাড় ও দাঁত মজবুত করে"),
                            exampleBangla = "প্রশ্ন: কোন ভিটামিনের অভাবে রাতকানা রোগ হয়?\nউত্তর: ভিটামিন 'এ' এর অভাবে।"
                        ),
                        CurriculumTopic(
                            id = "c5_s3",
                            titleBangla = "পদার্থ ও শক্তি: গতিশক্তি ও তাপ সঞ্চালন",
                            summaryBangla = "পরিবহন, পরিচলন ও বিকিরণ পদ্ধতিতে তাপ সঞ্চালন এবং আলোর প্রতিফলন।",
                            detailsBangla = "শক্তি কোনো কাজ করার সামর্থ্য। শক্তির বিভিন্ন রূপ: যান্ত্রিক, তাপ, আলোক, শব্দ, বিদ্যুৎ ও রাসায়নিক শক্তি। তাপ তিন পদ্ধতিতে সঞ্চালিত হয়: কঠিন পদার্থে পরিবহন, তরল ও বায়বীয় পদার্থে পরিচলন এবং শূন্য মাধ্যমে বিকিরণ।",
                            formulaOrRule = "তাপ সঞ্চালনের ৩ পদ্ধতি: ১. পরিবহন (কঠিন), ২. পরিচলন (তরল ও গ্যাস), ৩. বিকিরণ (সূর্য থেকে পৃথিবীতে)",
                            keyPoints = listOf("শক্তির সৃষ্টি বা ধ্বংস নেই, কেবল রূপান্তর ঘটে", "সূর্যের আলো বিকিরণ পদ্ধতিতে পৃথিবীতে আসে", "আলোর প্রতিফলনের সূত্র"),
                            exampleBangla = "প্রশ্ন: সূর্য থেকে তাপ কোন পদ্ধতিতে পৃথিবীতে আসে?\nউত্তর: বিকিরণ পদ্ধতিতে।"
                        ),
                        CurriculumTopic(
                            id = "c5_s4",
                            titleBangla = "মহাবিশ্ব, দিন-রাত্রি ও ঋতু পরিবর্তন",
                            summaryBangla = "আহ্নিক গতি ও বার্ষিক গতি, চাঁদের দশাসমূহ ও সৌরজগৎ।",
                            detailsBangla = "পৃথিবী তার নিজ অক্ষে ২৪ ঘণ্টায় একবার ঘুরে আসে—একে আহ্নিক গতি বলে; এর ফলে দিন ও রাত হয়। পৃথিবী সূর্যের চারদিকে ৩৬৫ দিন ৬ ঘণ্টায় একবার ঘুরে আসে—একে বার্ষিক গতি বলে; এর ফলে ঋতু পরিবর্তন হয়।",
                            formulaOrRule = "আহ্নিক গতি (২৪ ঘণ্টা) ➔ দিন-রাত | বার্ষিক গতি (৩৬৫ দিন ৬ ঘণ্টা) ➔ ঋতু পরিবর্তন",
                            keyPoints = listOf("পৃথিবীর অক্ষটি ২৩.৫ ডিগ্রি হেলে থাকে", "চাঁদের ২৮ দিনের দশাচক্র (অমাবস্যা ও পূর্ণিমা)", "সৌরজগতের ৮টি গ্রহ"),
                            exampleBangla = "প্রশ্ন: পৃথিবীর কোন গতির কারণে দিন ও রাত্রি হয়?\nউত্তর: আহ্নিক গতির কারণে।"
                        ),
                        CurriculumTopic(
                            id = "c5_s5",
                            titleBangla = "তথ্য প্রযুক্তি ও আমাদের জীবন",
                            summaryBangla = "ইন্টারনেট, তথ্যের আদান-প্রদান, সংরক্ষণ ও অপব্যবহার প্রতিরোধ।",
                            detailsBangla = "আইসিটি (ICT) আমাদের যোগাযোগ ও শিক্ষাকে সহজ করেছে। তথ্যের উৎস: বই, সংবাদপত্র, ইন্টারনেট ও টেলিভিশন। তথ্য সংরক্ষণে মেমোরি কার্ড, পেনড্রাইভ ও ক্লাউড ব্যবহৃত হয়।",
                            keyPoints = listOf("সার্চ ইঞ্জিন: Google, Bing, Pipilika", "সঠিক তথ্য যাচাই ও সাইবার নিরাপত্তা", "অনলাইনে ব্যক্তিগত তথ্যের গোপনীয়তা রক্ষা"),
                            exampleBangla = "প্রশ্ন: ইন্টারনেট ব্যবহারের একটি প্রধান সুবিধা কী?\nউত্তর: মুহূর্তের মধ্যে বিশ্বজুড়ে প্রয়োজনীয় তথ্য সংগ্রহ ও যোগাযোগ করা যায়।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c5_bangla",
                    nameBangla = "আমার বাংলা বই",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c5_b1",
                            titleBangla = "স্মরণীয় যারা চিরদিন ও শহীদ তিতুমীর",
                            summaryBangla = "বুদ্ধিজীবী হত্যা, বাঁশের কেল্লা ও স্বাধীনতা সংগ্রামের গৌরবোজ্জ্বল ইতিহাস।",
                            detailsBangla = "১৯৭১ সালের ১৪ই ডিসেম্বর পাকিস্তানি হানাদার বাহিনী বাংলাদেশের শ্রেষ্ঠ সন্তান বুদ্ধিজীবীদের হত্যা করে। শহীদ তিতুমীর ব্রিটিশ শাসনের বিরুদ্ধে বাঁশের কেল্লা নির্মাণ করে প্রতিরোধ গড়ে তুলেছিলেন।",
                            keyPoints = listOf("১৪ই ডিসেম্বর শহীদ বুদ্ধিজীবী দিবস", "তিতুমীরের বাঁশের কেল্লা নারিকেলবাড়িয়ায় নির্মিত", "দেশপ্রেম ও আত্মত্যাগ"),
                            exampleBangla = "প্রশ্ন: শহীদ বুদ্ধিজীবী দিবস কবে পালিত হয়?\nউত্তর: প্রতি বছর ১৪ই ডিসেম্বর।"
                        ),
                        CurriculumTopic(
                            id = "c5_b2",
                            titleBangla = "সুন্দরবনের প্রাণী ও পরিবেশ সংরক্ষণ",
                            summaryBangla = "রয়েল বেঙ্গল টাইগার, চিত্রা হরিণ, বিলুপ্তপ্রায় প্রাণী ও জীববৈচিত্র্য।",
                            detailsBangla = "সুন্দরবন বাংলাদেশের দক্ষিণে অবস্থিত পৃথিবীর বৃহত্তম ম্যানগ্রোভ বন। এখানে সুন্দরী, গেওয়া, গরান গাছ ও রয়েল বেঙ্গল টাইগার বাস করে। অবৈধ শিকার ও বন ধ্বংস রোধ করা জাতীয় দায়িত্ব।",
                            keyPoints = listOf("সুন্দরবন ইউনেস্কো বিশ্ব ঐতিহ্যবাহী স্থান", "গোলপাতা ও মধুর উৎস", "জীববৈচিত্র্য সংরক্ষণ"),
                            exampleBangla = "প্রশ্ন: সুন্দরবনের বিখ্যাত বাঘের নাম কী?\nউত্তর: রয়েল বেঙ্গল টাইগার।"
                        ),
                        CurriculumTopic(
                            id = "c5_b3",
                            titleBangla = "ব্যাকরণ: পদ প্রকরণ, বিরামচিহ্ন, যুক্তবর্ণ ও ফরম পূরণ",
                            summaryBangla = "শব্দার্থ, বাক্য সংকোচন, সমার্থক-বিপরীত শব্দ, অনুচ্ছেদ ও ফরম পূরণের নিয়ম।",
                            detailsBangla = "সমাপনী পরীক্ষার পূর্ণাঙ্গ ব্যাকরণ অংশ: পদ ৫ প্রকার, বাক্য সংকোচন (এককথায় প্রকাশ), বিরামচিহ্ন সঠিক স্থানে বসানো, যুক্তবর্ণ ভেঙে বাক্য রচনা এবং পাঠাগার/ভর্তি ফরম পূরণ।",
                            formulaOrRule = "এককথায় প্রকাশ: যা বলা হয়নি = অনুক্ত | যা চিরকাল থাকবে = চিরস্থায়ী | যা সহজে লাভ করা যায় = সুলভ",
                            keyPoints = listOf("বিরামচিহ্ন: কমা, সেমিকোলন, দাঁড়ি, উদ্ধরণ চিহ্ন", "যুক্তবর্ণ দিয়ে বাক্য গঠন", "ফরম পূরণে সঠিক তথ্য প্রদান"),
                            exampleBangla = "প্রশ্ন: 'যা জলে চরে' এককথায় কী হবে?\nউত্তর: জলচর।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c5_english",
                    nameBangla = "English for Today",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c5_e1",
                            titleBangla = "WH-Questions Framing & Sentence Structure",
                            summaryBangla = "Who, What, Where, When, Why, How দিয়ে নির্ভুল প্রশ্ন তৈরির নিয়ম।",
                            detailsBangla = "Statements are transformed into WH-questions using appropriate auxiliary verbs (do, does, did, am, is, are, was, were). Rule: WH-word + Aux Verb + Subject + Main Verb + Extension?",
                            formulaOrRule = "Structure: WH-word + do/does/did + Sub + Verb(base)? | Sima goes to school ➔ Where does Sima go?",
                            keyPoints = listOf("Who for person, Where for place, When for time, Why for reason", "How many for countable, How much for uncountable", "Proper question mark at end"),
                            exampleBangla = "Question: Frame WH-Question for 'Raju is a firefighter.'\nAnswer: 'What is Raju?' or 'Who is a firefighter?'"
                        ),
                        CurriculumTopic(
                            id = "c5_e2",
                            titleBangla = "Unseen Passage Comprehension & Letter Writing",
                            summaryBangla = "True/False, Fill in blanks, Short questions, Informal letter writing.",
                            detailsBangla = "PEC English model includes reading unseen informative texts, matching words with definitions, answering comprehensive questions, and writing an informal letter to a friend or parents with all 6 parts.",
                            formulaOrRule = "Letter's 6 Parts: 1. Heading (Date & Address), 2. Salutation, 3. Body, 4. Subscription, 5. Signature, 6. Superscription (Envelope)",
                            keyPoints = listOf("Reading for gist and specific facts", "Proper format of personal letters", "5-sentence paragraph writing"),
                            exampleBangla = "Question: What are the greetings used in an informal letter?\nAnswer: 'Dear [Friend's Name],' or 'Dear Father,'."
                        ),
                        CurriculumTopic(
                            id = "c5_e3",
                            titleBangla = "Grammar: Prepositions, Conjunctions & Modals",
                            summaryBangla = "In, on, at, by, with, and, but, or, can, may, must usage.",
                            detailsBangla = "Correct preposition usage in sentences: time (at 7 AM, in 2026, on Sunday), place (in Dhaka, at school). Conjunctions join words or clauses. Modals express ability or obligation.",
                            formulaOrRule = "Time: At (specific time) | On (days & dates) | In (months & years)",
                            keyPoints = listOf("He lives at Mirpur in Dhaka", "Can for ability, May for permission, Must for duty", "Subject-Verb Agreement"),
                            exampleBangla = "Question: Fill in the blank: 'We celebrate our Victory Day ___ 16 December.'\nAnswer: 'on' (used before dates)."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c5_social",
                    nameBangla = "বাংলাদেশ ও বিশ্বপরিচয়",
                    iconName = "social",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c5_bgs1",
                            titleBangla = "আমাদের মুক্তিযুদ্ধ ১৯৭১ ও মুজিবনগর সরকার",
                            summaryBangla = "৭ই মার্চের ভাষণ, ২৫শে মার্চ কালরাত, মুজিবনগর সরকার এবং ১৬ই ডিসেম্বর বিজয়।",
                            detailsBangla = "১৯৭১ সালের ২৬শে মার্চ জাতির পিতা বঙ্গবন্ধু শেখ মুজিবুর রহমান স্বাধীনতা ঘোষণা করেন। ১০ই এপ্রিল মুজিবনগর সরকার গঠিত হয় এবং ১৭ই এপ্রিল শপথ গ্রহণ করে। রাষ্ট্রপতি ছিলেন বঙ্গবন্ধু শেখ মুজিবুর রহমান এবং অস্থায়ী রাষ্ট্রপতি সৈয়দ নজরুল ইসলাম, প্রধানমন্ত্রী তাজউদ্দীন আহমদ। নয় মাসের রক্তক্ষয়ী যুদ্ধে ৩০ লক্ষ শহীদের আত্মত্যাগে ১৬ই ডিসেম্বর বিজয় অর্জিত হয়।",
                            formulaOrRule = "২৬শে মার্চ স্বাধীনতা দিবস | ১৬ই ডিসেম্বর বিজয় দিবস | ২১শে ফেব্রুয়ারি শহীদ দিবস | ১৭ই এপ্রিল মুজিবনগর সরকার শপথ দিবস",
                            keyPoints = listOf("বঙ্গবন্ধুর ঐতিহাসিক ৭ই মার্চের ভাষণ ইউনেস্কো বিশ্ব ঐতিহ্যে অন্তর্ভুক্ত", "মুক্তিবাহিনীর প্রধান সেনাপতি ছিলেন জেনারেল আতাউল গনি ওসমানী", "১১টি সেক্টরে সারা দেশকে ভাগ করে যুদ্ধ পরিচালিত হয়"),
                            exampleBangla = "প্রশ্ন: মুজিবনগর সরকার কবে গঠিত এবং কবে শপথ গ্রহণ করে?\nউত্তর: ১৯৭১ সালের ১০ই এপ্রিল গঠিত হয় এবং ১৭ই এপ্রিল মেহেরপুরের বৈদ্যনাথতলায় শপথ নেয়।"
                        ),
                        CurriculumTopic(
                            id = "c5_bgs2",
                            titleBangla = "ব্রিটিশ শাসন ও বাংলার ঐতিহাসিক স্থানসমূহ",
                            summaryBangla = "১৭৫৭ সালের পলাশীর যুদ্ধ, তিতুমীর, মহাস্থানগড়, ময়নামতি, পাহাড়পুর ও আহসান মঞ্জিল।",
                            detailsBangla = "১৭৫৭ সালের ২৩শে জুন পলাশীর যুদ্ধে নবাব সিরাজউদ্দৌলার পতনের মাধ্যমে বাংলায় প্রায় ২০০ বছরের ব্রিটিশ শাসনের সূচনা হয়। বাংলাদেশের সমৃদ্ধ ইতিহাসের সাক্ষী বগুড়ার মহাস্থানগড়, নওগাঁর সোমপুর মহাবিহার (পাহাড়পুর) ও কুমিল্লার শালবন বিহার।",
                            keyPoints = listOf("পলাশীর যুদ্ধ: ২৩শে জুন ১৭৫৭", "মহাস্থানগড় করতোয়া নদীর তীরে অবস্থিত (প্রাচীন পুণ্ড্রনগর)", "সোমপুর বিহার নওগাঁর পাহাড়পুরে রাজা ধর্মপাল তৈরি করেন"),
                            exampleBangla = "প্রশ্ন: মহাস্থানগড় কোন নদীর তীরে অবস্থিত?\nউত্তর: করতোয়া নদীর তীরে।"
                        ),
                        CurriculumTopic(
                            id = "c5_bgs3",
                            titleBangla = "মানবাধিকার, শিশুশ্রম প্রতিরোধ ও নাগরিক দায়িত্ব",
                            summaryBangla = "জাতিসংঘের মানবাধিকার সনদ, শিশু অধিকার ও গণতান্ত্রিক মনোভাব।",
                            detailsBangla = "১৯৪৮ সালের ১০ই ডিসেম্বর জাতিসংঘ সর্বজনীন মানবাধিকার ঘোষণা অনুমোদন করে। সকল মানুষের সমান অধিকার ও খাদ্য, বস্ত্র, বাসস্থান, শিক্ষা ও চিকিৎসার অধিকার নিশ্চিত করা। শিশুশ্রম বন্ধ করা এবং সিদ্ধান্ত গ্রহণে গণতান্ত্রিক পরমতসহিষ্ণুতা বজায় রাখা।",
                            keyPoints = listOf("১০ই ডিসেম্বর আন্তর্জাতিক মানবাধিকার দিবস", "শিশু অধিকার সনদ (CRC)", "নিয়মিত কর দেওয়া ও রাষ্ট্রের আইন মেনে চলা"),
                            exampleBangla = "প্রশ্ন: আন্তর্জাতিক মানবাধিকার দিবস কবে পালিত হয়?\nউত্তর: প্রতি বছর ১০ই ডিসেম্বর।"
                        ),
                        CurriculumTopic(
                            id = "c5_bgs4",
                            titleBangla = "জলবায়ু পরিবর্তন, দুর্যোগ ব্যবস্থাপনা ও জনসংখ্যা",
                            summaryBangla = "বৈশ্বিক উষ্ণায়ন, বন্যা-ঘূর্ণিঝড় মোকাবিলা এবং জনসংখ্যার সাথে সম্পদের ভারসাম্য।",
                            detailsBangla = "অতিরিক্ত কার্বন নির্গমনের ফলে সমুদ্রপৃষ্ঠের উচ্চতা বৃদ্ধি পাচ্ছে ও বাংলাদেশ ঝুঁকিতে রয়েছে। ঘূর্ণিঝড় প্রস্তুতি কর্মসূচি (CPP) ও আশ্রয়কেন্দ্রের ব্যবহার। পরিবার পরিকল্পনা ও সুশিক্ষার মাধ্যমে জনসম্পদে রূপান্তর।",
                            keyPoints = listOf("গ্রিনহাউস প্রভাব ও নদীভাঙন", "ঘূর্ণিঝড়ের মহাবিপদ সংকেত ও পূর্বপ্রস্তুতি", "দক্ষ জনশক্তি গড়ে তোলা"),
                            exampleBangla = "প্রশ্ন: জলবায়ু পরিবর্তনের একটি ক্ষতিকর প্রভাব কী?\nউত্তর: সমুদ্রপৃষ্ঠের উচ্চতা বৃদ্ধি এবং অতিবৃষ্টি ও অনাবৃষ্টির মতো প্রাকৃতিক দুর্যোগের প্রকোপ বৃদ্ধি।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c5_religion",
                    nameBangla = "ইসলাম ও নৈতিক শিক্ষা",
                    iconName = "religion",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c5_r1",
                            titleBangla = "আকাইদ: তাওহিদ, রিসালাত ও আখিরাত",
                            summaryBangla = "আল্লাহর গুণাবলি, আসমানি কিতাব, নবী-রাসূলগণের দায়িত্ব ও পরকালের জবাবদিহিতা।",
                            detailsBangla = "ইসলামের মূল বিশ্বাসের বিষয়গুলো হলো আকাইদ। তাওহিদ (একত্ববাদ), রিসালাত (নবী-রাসূলগণ আল্লাহর বার্তা মানুষের কাছে পৌঁছেছেন) এবং আখিরাত (মৃত্যুর পরবর্তী অনন্ত জীবন)। মানুষকে তার সকল কাজের হিসাব দিতে হবে।",
                            formulaOrRule = "ঈমানের ৭টি মূল স্তম্ভ: আল্লাহ, ফেরেশতা, কিতাব, রাসূল, আখিরাত, তকদির, পুনরুত্থানে বিশ্বাস",
                            keyPoints = listOf("আসমানি কিতাব মোট ১০৪টি (বড় ৪টি: তাওরাত, যাবুর, ইঞ্জিল ও কুরআন)", "কুরআন মজিদ সর্বশেষ ও সর্বশ্রেষ্ঠ কিতাব", "মিজান ও পুলসিরাতের বিশ্বাস"),
                            exampleBangla = "প্রশ্ন: প্রধান চারখানা আসমানি কিতাব কী কী?\nউত্তর: ১. তাওরাত, ২. যাবুর, ৩. ইঞ্জিল এবং ৪. আল-কুরআন।"
                        ),
                        CurriculumTopic(
                            id = "c5_r2",
                            titleBangla = "ইবাদত: সালাত, সাওম, জাকাত ও হাজ্জ",
                            summaryBangla = "ইসলামের স্তম্ভসমূহ ও সামাজিক সাম্য এবং আত্মশুদ্ধির গুরুত্ব।",
                            detailsBangla = "সালাত দৈনিক ৫ বার আনুগত্য প্রকাশ করে। সাওম (রোজা) তাকওয়া ও সহমর্মিতা শেখায়। জাকাত ধনীদের সম্পদ থেকে নির্দিষ্ট অংশ (২.৫%) দরিদ্রদের মাঝে বণ্টন করে অর্থনৈতিক বৈষম্য দূর করে। হাজ্জ বিশ্ব মুসলিমের ঐক্য সম্মেলন।",
                            formulaOrRule = "জাকাতের হার: নিসাব পরিমাণ সম্পদের শতকরা ২.৫% (বা ১/৪০ অংশ)",
                            keyPoints = listOf("সালাত জান্নাতের চাবি", "রমজান মাসের রোজা ফরজ", "জাকাত দিলে সম্পদ পবিত্র হয়"),
                            exampleBangla = "প্রশ্ন: জাকাত শতকরা কত হারে প্রদান করতে হয়?\nউত্তর: শতকরা ২.৫ টাকা (২.৫%) হারে।"
                        ),
                        CurriculumTopic(
                            id = "c5_r3",
                            titleBangla = "আখলাক ও হযরত মুহাম্মদ (সা.)-এর মহান আদর্শ",
                            summaryBangla = "সততা, পিতা-মাতার সেবা, প্রতিবেশীর অধিকার, পরোপকার ও সৃষ্টির সেবা।",
                            detailsBangla = "সুন্দর আচরণই শ্রেষ্ঠ ইবাদত। হযরত মুহাম্মদ (সা.) বাল্যকাল থেকেই 'আল-আমিন' (বিশ্বস্ত) উপাধিতে ভূষিত হন। হিলফুল ফুজুল গঠন করে নির্যাতিতদের সাহায্য করেন। মদিনা সনদের মাধ্যমে সকল ধর্মের মানুষের সমান নাগরিক অধিকার প্রতিষ্ঠা করেন।",
                            keyPoints = listOf("নবী করিম (সা.)-এর উপাধি ছিল 'আল-আমিন'", "মদিনা সনদ পৃথিবীর প্রথম লিখিত সংবিধান", "মাতা-পিতার পদতলে সন্তানের জান্নাত"),
                            exampleBangla = "প্রশ্ন: নবীজি (সা.) যৌবনে আর্তমানবতার সেবায় কোন সমিতি গঠন করেছিলেন?\nউত্তর: হিলফুল ফুজুল (শান্তি সংঘ)।"
                        )
                    )
                )
            )
        )
    )
}
