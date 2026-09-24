package com.example.data.model

object SecondaryJuniorCurriculumData {

    val levels: List<CurriculumClassLevel> = listOf(
        // =========================================================================
        // CLASS 6 (ষষ্ঠ শ্রেণি)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 6,
            classNameBangla = "ষষ্ঠ শ্রেণি (Class 6)",
            badgeBangla = "মাধ্যমিক সূচনালগ্ন",
            subjects = listOf(
                CurriculumSubject(
                    id = "c6_math",
                    nameBangla = "গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_m1",
                            titleBangla = "স্বাভাবিক সংখ্যা, ভগ্নাংশ ও লসাগু-গসাগু",
                            summaryBangla = "অঙ্কপাতন, দেশীয় ও আন্তর্জাতিক সংখ্যা গণনা রীতি এবং ইউক্লিডীয় পদ্ধতিতে লসাগু।",
                            detailsBangla = "স্বাভাবিক সংখ্যার অঙ্কপাতনে ০ থেকে ৯ পর্যন্ত প্রতীক ব্যবহৃত হয়। দেশীয় রীতিতে একক, দশক, শতক, হাজার, অযুত, লক্ষ, নিযুত, কোটি। আন্তর্জাতিক রীতিতে মিলিয়ন ও বিলিয়ন।",
                            formulaOrRule = "১ মিলিয়ন = ১০ লক্ষ | ১ বিলিয়ন = ১০০ কোটি | লসাগু × গসাগু = সংখ্যার গুণফল",
                            keyPoints = listOf("মৌলিক উৎপাদকে বিশ্লেষণ", "ইউক্লিডীয় প্রক্রিয়ায় গসাগু নির্ণয়", "ভগ্নাংশের সমহরকরণ"),
                            exampleBangla = "২৪ ও ৩৬ এর গসাগু = ১২, লসাগু = ৭২।"
                        ),
                        CurriculumTopic(
                            id = "c6_m2",
                            titleBangla = "বীজগণিতীয় রাশি, চলক ও সূচক",
                            summaryBangla = "বীজগণিতে অক্ষর প্রতীক (চলক x, y, z), প্রক্রিয়া চিহ্ন ও সহগ।",
                            detailsBangla = "পাটিগণিতে কেবল নির্দিষ্ট সংখ্যা থাকে, বীজগণিতে চলক প্রতীক ব্যবহারের মাধ্যমে সার্বজনীন নিয়ম ও সমীকরণ প্রকাশ করা হয়। 5x + 3y একটি দ্বিপদী রাশি যেখানে x এর সহগ ৫ এবং y এর সহগ ৩।",
                            formulaOrRule = "xᵃ × xᵇ = xᵃ⁺ᵇ | xᵃ ÷ xᵇ = xᵃ⁻ᵇ | সদৃশ পদ একত্রীকরণ",
                            keyPoints = listOf("চলকের মান পরিবর্তনশীল", "সাংখ্যিক সহগ ও আক্ষরিক সহগ", "বীজগণিতীয় যোগ ও বিয়োগ"),
                            exampleBangla = "(3x + 2y) + (5x - y) = 8x + y।"
                        ),
                        CurriculumTopic(
                            id = "c6_m3",
                            titleBangla = "মৌলিক জ্যামিতিক ধারণা ও কোণ",
                            summaryBangla = "বিন্দু, রেখা, রেখাংশ, রশ্মি, সন্নিহিত কোণ, বিপ্রতীপ কোণ ও পূরক-সম্পূরক কোণ।",
                            detailsBangla = "দুটি কোণের সমষ্টি ৯০° হলে তাদের একটিকে অপরটির পূরক কোণ বলে। সমষ্টি ১৮০° হলে একটিকে অপরটির সম্পূরক কোণ বলে। দুটি সরলরেখা পরস্পর ছেদ করলে উৎপন্ন বিপরীত কোণদ্বয় পরস্পর সমান (বিপ্রতীপ কোণ)।",
                            formulaOrRule = "পূরক কোণদ্বয়ের যোগফল = ৯০° | সম্পূরক কোণদ্বয়ের যোগফল = ১৮০°",
                            keyPoints = listOf("রেখার কোনো প্রান্তবিন্দু নেই, রেখাংশের ২টি, রশ্মির ১টি", "সমকোণ = ৯০°", "বিপ্রতীপ কোণদ্বয় পরস্পর সমান"),
                            exampleBangla = "৫০° কোণের পূরক কোণ = ৯০° - ৫০° = ৪০°। সম্পূরক কোণ = ১৮০° - ৫০° = ১৩০°।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c6_science",
                    nameBangla = "বিজ্ঞান",
                    iconName = "science",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_s1",
                            titleBangla = "বৈজ্ঞানিক প্রক্রিয়া ও পরিমাপের এসআই (SI) একক",
                            summaryBangla = "মৌলিক রাশি, লব্ধ রাশি এবং দৈর্ঘ্যের (m), ভরের (kg) ও সময়ের (s) আন্তর্জাতিক একক।",
                            detailsBangla = "বিজ্ঞান হলো প্রকৃতি ও প্রাকৃতিক ঘটনা সম্পর্কে পরীক্ষা-নিরীক্ষা ও পর্যবেক্ষণলব্ধ জ্ঞান। আন্তর্জাতিকভাবে স্বীকৃত ৭টি মৌলিক রাশি ও তাদের এসআই একক।",
                            formulaOrRule = "ক্ষেত্রফল = দৈর্ঘ্য × প্রস্থ (m²) | আয়তন = দৈর্ঘ্য × প্রস্থ × উচ্চতা (m³)",
                            keyPoints = listOf("দৈর্ঘ্যের একক মিটার (m)", "ভরের একক কিলোগ্রাম (kg)", "তাপমাত্রার একক কেলভিন (K)"),
                            exampleBangla = "১ ইঞ্চি = ২.৫৪ সেমি, ১ গজ = ৩ ফুট = ৩৬ ইঞ্চি।"
                        ),
                        CurriculumTopic(
                            id = "c6_s2",
                            titleBangla = "জীবজগতের গঠন ও উদ্ভিদের অঙ্গসংস্থান",
                            summaryBangla = "একটি আদর্শ উদ্ভিদের ৫টি অংশ (মূল, কাণ্ড, পাতা, ফুল ও ফল) এবং কোষের গঠন।",
                            detailsBangla = "উদ্ভিদ কোষ ও প্রাণী কোষের প্রধান পার্থক্য হলো উদ্ভিদ কোষে জড় কোষপ্রাচীর ও ক্লোরোপ্লাস্ট থাকে, প্রাণী কোষে থাকে না। মূল উদ্ভিদের দৃঢ়তা দেয় এবং পানি শোষণ করে।",
                            keyPoints = listOf("উদ্ভিদ কোষে কোষপ্রাচীর বিদ্যমান", "ক্লোরোপ্লাস্ট খাদ্য তৈরি করে", "রবার্ট হুক ১৬৬৫ সালে কোষ আবিষ্কার করেন")
                        ),
                        CurriculumTopic(
                            id = "c6_s3",
                            titleBangla = "সংবেদী অঙ্গ: চোখ ও কানের গঠন ও যত্ন",
                            summaryBangla = "কর্নিয়া, চোখের তারা, লেন্স, রেটিনা এবং অন্তকর্ণ ও শ্রুতিযন্ত্র।",
                            detailsBangla = "রেটিনায় রড ও কোণ কোষ থাকে যা আলো শনাক্ত করে। চোখে ধুলোবালি পড়লে পরিষ্কার পানি দিয়ে ধুতে হয়। উচ্চ শব্দে গান শোনা থেকে বিরত থাকা কানের স্বাস্থ্যের জন্য জরুরি।",
                            keyPoints = listOf("রেটিনায় বস্তুর উল্টো প্রতিবিম্ব গঠিত হয়", "চোখের লেন্স দ্বিউত্তল", "রাতকানা রোগ প্রতিরোধে ভিটামিন এ সমৃদ্ধ খাবার")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c6_bangla",
                    nameBangla = "বাংলা",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_b1",
                            titleBangla = "ধ্বনি ও বর্ণ এবং বিরামচিহ্নের ব্যবহার",
                            summaryBangla = "স্বরধ্বনি, ব্যঞ্জনধ্বনি, উচ্চারণের স্থান এবং দাড়ি, কমা, সেমিকোলন, উদ্ধৃতি চিহ্নের নিয়ম।",
                            detailsBangla = "মানুষের বাক-প্রত্যঙ্গের সাহায্যে উচ্চারিত আওয়াজকে ধ্বনি বলে এবং ধ্বনির লিখিত রূপকে বর্ণ বলে। ক-বর্গীয় ধ্বনি কণ্ঠধ্বনি, ত-বর্গীয় ধ্বনি দন্ত্যধ্বনি। বিরামচিহ্ন বাক্যের অর্থের স্পষ্টতা রক্ষা করে।",
                            formulaOrRule = "দাঁড়ি (.) = ১ সেকেন্ড থামতে হয় | কমা (,) = ১ গণনা সময় থামতে হয়",
                            keyPoints = listOf("কণ্ঠ, তালু, মূর্ধা, দন্ত ও ওষ্ঠ্য ধ্বনি", "উদ্ধৃতি চিহ্ন (\" \") বক্তার হুবহু বক্তব্য প্রকাশে ব্যবহৃত হয়", "যতি চিহ্নের গুরুত্ব")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c6_english",
                    nameBangla = "English",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_e1",
                            titleBangla = "Parts of Speech & Simple Tenses",
                            summaryBangla = "Noun, Pronoun, Adjective, Verb, Adverb, Preposition, Conjunction, Interjection.",
                            detailsBangla = "Every single word used in a sentence belongs to one of the 8 parts of speech. Simple present tense describes universal truths and daily habits, while simple past describes completed past events.",
                            formulaOrRule = "Present Simple: S + V1 (s/es for 3rd person singular) | Past Simple: S + V2",
                            keyPoints = listOf("Articles A, An, The rules", "Singular and Plural nouns", "Subject-verb agreement"),
                            exampleBangla = "The sun rises in the east (Present Simple). He visited the museum yesterday (Past Simple)."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c6_social",
                    nameBangla = "ইতিহাস ও সামাজিক বিজ্ঞান",
                    iconName = "history",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_soc1",
                            titleBangla = "প্রাচীন বাংলার জনপদ ও প্রত্নতাত্ত্বিক ঐতিহ্য",
                            summaryBangla = "মহাস্থানগড় (পুন্ড্রনগর), উয়ারী-বটেশ্বর, পাহাড়পুর বৌদ্ধ বিহার ও প্রাচীন সভ্যতা।",
                            detailsBangla = "প্রাচীনকালে বাংলা একক কোনো রাষ্ট্র ছিল না; বঙ্গ, পুন্ড্র, গৌড়, সমতট, হরিকেল প্রভৃতি স্বাধীন জনপদে বিভক্ত ছিল। উয়ারী-বটেশ্বরে প্রাপ্ত আড়াই হাজার বছরের প্রাচীন সিলমোহর ও ধাতব মুদ্রা সমৃদ্ধ বাণিজ্যের সাক্ষ্য দেয়।",
                            keyPoints = listOf("পুন্ড্রনগর বগুড়ার মহাস্থানগড়ে অবস্থিত", "পাহাড়পুর সোমপুর মহাবিহার ইউনেস্কো বিশ্ব ঐতিহ্য", "প্রাচীন নদীভিত্তিক সভ্যতা")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c6_ict",
                    nameBangla = "ডিজিটাল প্রযুক্তি (ICT)",
                    iconName = "ict",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_ict1",
                            titleBangla = "তথ্য নিরাপত্তা, শক্তিশালী পাসওয়ার্ড ও অ্যালগরিদম",
                            summaryBangla = "কম্পিউটার হার্ডওয়্যার-সফটওয়্যার, ইন্টারনেটে সঠিক তথ্য যাচাই ও পাসওয়ার্ড তৈরি।",
                            detailsBangla = "শক্তিশালী পাসওয়ার্ডে বড় হাত, ছোট হাত, সংখ্যা ও বিশেষ চিহ্নের মিশ্রণ থাকতে হয়। কোনো সমস্যার ধাপে ধাপে সমাধান প্রক্রিয়াকে অ্যালগরিদম বলে। ইন্টারনেটে গুজব থেকে সতর্ক থাকার নিয়ম।",
                            formulaOrRule = "অ্যালগরিদম ➔ ফ্লোচার্ট (প্রবাহচিত্র) ➔ প্রোগ্রামিং কোড",
                            keyPoints = listOf("পাসওয়ার্ড কাউকে শেয়ার না করা", "ইনপুট ডিভাইস: কীবোর্ড, মাউস | আউটপুট: মনিটর, প্রিন্টার", "তথ্য যাচাইয়ের বিশ্বস্ত সূত্র")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c6_health",
                    nameBangla = "স্বাস্থ্য সুরক্ষা ও জীবন জীবিকা",
                    iconName = "health",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_h1",
                            titleBangla = "কৈশোরকালীন পরিবর্তন ও পারিবারিক বাজেট",
                            summaryBangla = "বয়ঃসন্ধিকালের শারীরিক ও মানসিক যত্ন, পুষ্টিকর খাদ্য ও পরিবারের আয়-ব্যয় পরিকল্পনা।",
                            detailsBangla = "বয়ঃসন্ধিকালে দ্রুত শারীরিক বৃদ্ধি ঘটে, পুষ্টির চাহিদা বাড়ে। নিজের কাজ নিজে করার অভ্যাস, সঞ্চয় এবং পরিবারের বাজেট তৈরিতে সচেতন অংশগ্রহণ।",
                            keyPoints = listOf("কৈশোরের পরিবর্তন স্বাভাবিক ও প্রাকৃতিক", "রোজ সুষম খাবার ও খেলাধুলা", "পারিবারিক সঞ্চয় মানসিকতা")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c6_religion",
                    nameBangla = "ইসলাম ও নৈতিক শিক্ষা",
                    iconName = "religion",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c6_r1",
                            titleBangla = "আল কুরআন ও তাজবিদ এবং সালাতের গুরুত্ব",
                            summaryBangla = "মাখরাজ (১৭টি উচ্চারণের স্থান), গুনাহ পরিহার ও সমাজে সৎকাজের আদেশ।",
                            detailsBangla = "তাজবিদ হলো কুরআন মাজিদের হরফসমূহ বিশুদ্ধভাবে মাখরাজ ও সিফাত অনুযায়ী তিলাওয়াতের নিয়ম। সালাত মানুষকে অশ্লীল ও মন্দ কাজ থেকে বিরত রাখে।",
                            formulaOrRule = "মাখরাজ মোট ১৭টি | হরফে মাদ ৩টি (আলিফ, ওয়াও, ইয়া)",
                            keyPoints = listOf("কুরআন তিলাওয়াতের আদব", "ওয়াক্তমত সালাত আদায়ের গুরুত্ব", "সমাজসেবা ও সত্যবাদিতা")
                        )
                    )
                )
            )
        ),

        // =========================================================================
        // CLASS 7 (সপ্তম শ্রেণি)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 7,
            classNameBangla = "সপ্তম শ্রেণি (Class 7)",
            badgeBangla = "দক্ষতা সম্প্রসারণ",
            subjects = listOf(
                CurriculumSubject(
                    id = "c7_math",
                    nameBangla = "গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c7_m1",
                            titleBangla = "মূলদ ও অমূলদ সংখ্যা এবং বর্গমূল নির্ণয়",
                            summaryBangla = "যেসব সংখ্যাকে দুটি পূর্ণসংখ্যার অনুপাত আকারে প্রকাশ করা যায় তা মূলদ। ভাগের সাহায্যে বর্গমূল।",
                            detailsBangla = "পূর্ণবর্গ সংখ্যার বর্গমূল মূলদ সংখ্যা। যেসব সংখ্যার বর্গমূল অনাবৃত অসীম দশমিক (যেমন: √২, √৩) তারা অমূলদ সংখ্যা। ভাগের প্রক্রিয়ায় বড় সংখ্যার বর্গমূল নির্ণয়ের ধাপ।",
                            formulaOrRule = "মূলদ সংখ্যা = p/q (q ≠ 0) | অমূলদ সংখ্যা যেমন √২ = ১.৪১৪২১...",
                            keyPoints = listOf("উৎপাদকের সাহায্যে বর্গমূল", "ভাগের সাহায্যে বর্গমূল", "দশমিক ভগ্নাংশের বর্গমূল"),
                            exampleBangla = "১৪৪ এর বর্গমূল = ১২, ৬২৫ এর বর্গমূল = ২৫।"
                        ),
                        CurriculumTopic(
                            id = "c7_m2",
                            titleBangla = "বীজগণিতীয় বর্গসূত্র ও মান নির্ণয়",
                            summaryBangla = "(a + b)² = a² + 2ab + b², (a - b)² = a² - 2ab + b² এবং a² - b² = (a+b)(a-b)।",
                            detailsBangla = "সপ্তম শ্রেণিতে বীজগণিতের বর্গের মৌলিক সূত্রাবলি ও তাদের অনুসিদ্ধান্ত শেখানো হয়। বিভিন্ন সংখ্যার বর্গ নির্ণয়ে এবং বীজগণিতীয় রাশির মান বের করতে এই সূত্রগুলো সরাসরি প্রয়োগ করা হয়।",
                            formulaOrRule = "(a+b)² = a² + 2ab + b² | (a-b)² = a² - 2ab + b² | a² + b² = (a+b)² - 2ab",
                            keyPoints = listOf("বর্গের জ্যামিতিক প্রমাণ", "মান নির্ণয়ের অনুসিদ্ধান্ত প্রয়োগ", "সূত্রের সাহায্যে দ্রুত গুণফল"),
                            exampleBangla = "a + 1/a = 4 হলে, a² + 1/a² = (a + 1/a)² - 2 = 4² - 2 = 14।"
                        ),
                        CurriculumTopic(
                            id = "c7_m3",
                            titleBangla = "সরল সমীকরণ ও সমীকরণের সমাধান",
                            summaryBangla = "এক চলকবিশিষ্ট সরল সমীকরণ, পক্ষান্তর বিধি, বর্জন বিধি ও প্রতিসাম্য বিধি।",
                            detailsBangla = "সমান চিহ্নের একপাশ থেকে অন্যপাশে কোনো পদ স্থানান্তরিত হলে তার চিহ্ন পরিবর্তিত হয় (পক্ষান্তর)। সমীকরণের উভয়পক্ষে একই রাশি যোগ, বিয়োগ, গুণ বা ভাগ করা যায়।",
                            formulaOrRule = "ax + b = c ➔ ax = c - b ➔ x = (c - b) / a",
                            keyPoints = listOf("বামপক্ষ = ডানপক্ষ নিশ্চিতকরণ", "বীজ বা মূল (Root of equation)", "বাস্তব সমস্যার সমীকরণ তৈরি"),
                            exampleBangla = "৩x + ৫ = ১৭ ➔ ৩x = ১৭ - ৫ = ১২ ➔ x = ১২ ÷ ৩ = ৪।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c7_science",
                    nameBangla = "বিজ্ঞান",
                    iconName = "science",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c7_s1",
                            titleBangla = "পদার্থের গঠন ও পরমাণুর মডেল",
                            summaryBangla = "মৌলিক ও যৌগিক পদার্থ, পরমাণুর স্থায়ী মূল কণিকা (ইলেকট্রন, প্রোটন, নিউট্রন)।",
                            detailsBangla = "পরমাণুর কেন্দ্রে থাকে নিউক্লিয়াস যাতে প্রোটন (ধনাত্মক চার্জ) ও নিউট্রন (চার্জহীন) থাকে। নিউক্লিয়াসের বাইরে নির্দিষ্ট কক্ষপথে ইলেকট্রন (ঋণাত্মক চার্জ) ঘোরে। প্রোটন সংখ্যাকে পারমাণবিক সংখ্যা বলে।",
                            formulaOrRule = "ভরসংখ্যা (A) = প্রোটন সংখ্যা (Z) + নিউট্রন সংখ্যা (N)",
                            keyPoints = listOf("পরমাণু সামগ্রিকভাবে চার্জ নিরপেক্ষ", "ইলেকট্রন বিন্যাসের সূত্র: 2n²", "রাসায়নিক প্রতীক ও সংকেত লিখন")
                        ),
                        CurriculumTopic(
                            id = "c7_s2",
                            titleBangla = "আলোর প্রতিফলন ও পেরিস্কোপের গঠন",
                            summaryBangla = "প্রতিফলনের সূত্র, আপতন কোণ = প্রতিফলন কোণ এবং সমতল দর্পণ।",
                            detailsBangla = "আলো যখন এক মাধ্যম থেকে অন্য মাধ্যমে গিয়ে প্রথম মাধ্যমে ফিরে আসে তাকে প্রতিফলন বলে। পেরিস্কোপে দুটি সমতল দর্পণ একে অপরের সাথে ৪৫° কোণে বসানো হয়। সাবমেরিনে পেরিস্কোপ ব্যবহৃত হয়।",
                            formulaOrRule = "আপতন কোণ (i) = প্রতিফলন কোণ (r)",
                            keyPoints = listOf("নিয়মিত ও ব্যাপ্ত প্রতিফলন", "সমতল দর্পণে প্রতিবিম্বের দূরত্ব লক্ষ্যবস্তুর সমান", "পেরিস্কোপের ব্যবহার")
                        ),
                        CurriculumTopic(
                            id = "c7_s3",
                            titleBangla = "রক্ত সংবহনতন্ত্র ও হৃদপিণ্ডের কার্যপ্রণালী",
                            summaryBangla = "রক্তের উপাদান (রক্তরস ও ৩ ধরনের রক্তকণিকা) এবং হৃদপিণ্ডের চার প্রকোষ্ঠ।",
                            detailsBangla = "রক্তের তিনটি রক্তকণিকা: লোহিত রক্তকণিকা (অক্সিজেন পরিবহন করে), শ্বেত রক্তকণিকা (জীবাণু ধ্বংস করে দেহের সৈনিক হিসেবে কাজ করে), এবং অণুচক্রিকা (রক্ত জমাট বাঁধতে সাহায্য করে)। হৃদপিণ্ডের অলিন্দ ও নিলয় রক্ত পাম্প করে।",
                            keyPoints = listOf("হিমোগ্লোবিনের কারণে রক্ত লাল দেখায়", "ধমনি অক্সিজেনযুক্ত ও শিরা কার্বন ডাই অক্সাইডযুক্ত রক্ত বহন করে", "রক্তচাপ স্বাভাবিক রাখা")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c7_bangla",
                    nameBangla = "বাংলা",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c7_b1",
                            titleBangla = "সন্ধি: স্বরসন্ধি ও ব্যঞ্জনসন্ধি",
                            summaryBangla = "পাশাপাশি দুটি ধ্বনির মিলনকে সন্ধি বলে। নিয়ম ও বিশ্লেষণ।",
                            detailsBangla = "সন্ধির ফলে নতুন শব্দ সৃষ্টি হয় এবং উচ্চারণ সহজ ও শ্রুতিমধুর হয়। স্বরধ্বনির সাথে স্বরধ্বনির মিলনকে স্বরসন্ধি এবং ব্যঞ্জনধ্বনির সাথে স্বর বা ব্যঞ্জনের মিলনকে ব্যঞ্জনসন্ধি বলে।",
                            formulaOrRule = "অ + আ = আ (হিম + আলয় = হিমালয়) | বিদ্যা + আলয় = বিদ্যালয়",
                            keyPoints = listOf("স্বরসন্ধির নিয়মাবলী", "ব্যঞ্জনসন্ধির নিয়মাবলী", "নিপাতনে সিদ্ধ সন্ধি")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c7_english",
                    nameBangla = "English",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c7_e1",
                            titleBangla = "Tenses, Prepositions & Degrees of Comparison",
                            summaryBangla = "Present Perfect, Past Perfect, Prepositions of place & time, Positive, Comparative, Superlative.",
                            detailsBangla = "Degrees of adjectives show comparison (tall, taller, tallest). Prepositions like 'in', 'on', 'at', 'into' indicate relationships between words.",
                            formulaOrRule = "Present Perfect: S + have/has + V3 | Comparative: Adj + er / more + Adj",
                            keyPoints = listOf("Irregular verbs forms", "As... as comparison structure", "Writing formal letters to headteachers"),
                            exampleBangla = "Rahim is taller than Karim. He has finished his homework."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c7_social",
                    nameBangla = "ইতিহাস ও সামাজিক বিজ্ঞান",
                    iconName = "history",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c7_soc1",
                            titleBangla = "মধ্যযুগীয় বাংলা: সুলতানি ও মুঘল আমল",
                            summaryBangla = "ইলিয়াস শাহী বংশ, আলাউদ্দিন হোসেন শাহ, বারো ভূঁইয়া ও সুবাদার ইসলাম খান।",
                            detailsBangla = "ফখরুদ্দিন মুবারক শাহের মাধ্যমে বাংলায় স্বাধীন সুলতানি আমল শুরু হয়। বারো ভূঁইয়ারা মুঘলদের বিরুদ্ধে বীরত্বপূর্ণ প্রতিরোধ গড়ে তুলেছিলেন। ১৬১০ সালে সুবাদার ইসলাম খান ঢাকাকে বাংলার রাজধানী ঘোষণা করেন এবং নাম দেন জাহাঙ্গীরনগর।",
                            keyPoints = listOf("১৩৩৮ থেকে ১৫৩৮ সাল স্বাধীন সুলতানি আমল", "ঈসা খাঁ বারো ভূঁইয়াদের নেতা ছিলেন", "ঢাকার নামকরণ ও প্রাচীন স্মৃতিচিহ্ন")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c7_ict",
                    nameBangla = "ডিজিটাল প্রযুক্তি (ICT)",
                    iconName = "ict",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c7_ict1",
                            titleBangla = "কম্পিউটার নেটওয়ার্ক ও সাইবার নিরাপত্তা",
                            summaryBangla = "LAN, WAN, ইন্টারনেট, ক্লাউড স্টোরেজ এবং সাইবার বুলিং প্রতিরোধ।",
                            detailsBangla = "একাধিক কম্পিউটারের পারস্পরিক সংযোগকে নেটওয়ার্ক বলে। লোকাল এরিয়া নেটওয়ার্ক (LAN) ছোট অঞ্চলে এবং ওয়াইড এরিয়া নেটওয়ার্ক (WAN) বিশ্বব্যাপী বিস্তৃত। সামাজিক যোগাযোগ মাধ্যমে ব্যক্তিগত তথ্য সুরক্ষার গুরুত্ব।",
                            keyPoints = listOf("সার্ভার ও ক্লায়েন্ট কম্পিউটার", "টু-ফ্যাক্টর অথেনটিকেশন (2FA)", "সাইবার সিকিউরিটি সতর্কতা")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c7_health",
                    nameBangla = "স্বাস্থ্য সুরক্ষা ও জীবন জীবিকা",
                    iconName = "health",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c7_h1",
                            titleBangla = "প্রাথমিক চিকিৎসা (First Aid) ও মানসিক স্বাস্থ্য",
                            summaryBangla = "কেটে যাওয়া, পুড়ে যাওয়া ও মচকে যাওয়ার তাৎক্ষণিক সেবা এবং স্ট্রেস ম্যানেজমেন্ট।",
                            detailsBangla = "আগুনে পুড়ে গেলে অন্তত ২০ মিনিট সাধারণ ঠান্ডা পানি ঢালতে হয়, বরফ দেওয়া যাবে না। বিষণ্নতা বা মানসিক চাপে সহপাঠী ও শিক্ষকের সাথে মন খুলে কথা বলা এবং খেলাধুলা করা।",
                            keyPoints = listOf("ফার্স্ট এইড কিটের প্রয়োজনীয় উপকরণ", "পুড়ে গেলে পরিষ্কার পানির ব্যবহার", "শারীরিক ব্যায়াম ও ধ্যান")
                        )
                    )
                )
            )
        ),

        // =========================================================================
        // CLASS 8 (অষ্টম শ্রেণি)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 8,
            classNameBangla = "অষ্টম শ্রেণি (Class 8)",
            badgeBangla = "জেএসসি ও ভিত্তি স্তর",
            subjects = listOf(
                CurriculumSubject(
                    id = "c8_math",
                    nameBangla = "গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c8_m1",
                            titleBangla = "প্যাটার্ন (Pattern) ও জাদুবর্গ গঠন",
                            summaryBangla = "বীজগণিতীয় রাশি দিয়ে প্যাটার্ন প্রকাশ, পদ সংখ্যা ও পদের যোগফল সূত্র।",
                            detailsBangla = "৩, ৭, ১১, ১৫... ক্রমটির বীজগণিতীয় রাশি হলো ৪ক - ১। ৩ ক্রমের জাদুবর্গের ম্যাজিক সংখ্যা ১৫ এবং ৪ ক্রমের ম্যাজিক সংখ্যা ৩৪। প্রথম n সংখ্যক স্বাভাবিক সংখ্যার যোগফল সূত্র প্রয়োগ।",
                            formulaOrRule = "যোগফল = [(প্রথম পদ + শেষ পদ) × পদসংখ্যা] / ২ | ম্যাজিক সংখ্যা = [n(n² + 1)] / 2",
                            keyPoints = listOf("ফিবোনাচ্চি ধারা: ০, ১, ১, ২, ৩, ৫, ৮, ১৩...", "৩ ক্রমের ম্যাজিক সংখ্যা ১৫", "n-তম পদ নির্ণয়"),
                            exampleBangla = "১ থেকে ১০০ পর্যন্ত সংখ্যার যোগফল = [(১ + ১০০) × ১০০] / ২ = ১০১ × ৫০ = ৫০৫০।"
                        ),
                        CurriculumTopic(
                            id = "c8_m2",
                            titleBangla = "ঘনফল নির্ণয়ের সূত্রাবলী ও উৎপাদক বিশ্লেষণ",
                            summaryBangla = "(a + b)³, (a - b)³ এবং a³ ± b³ এর প্রমাণ ও প্রয়োগ।",
                            detailsBangla = "অষ্টম শ্রেণিতে বীজগণিতীয় ঘনফলের সূত্র ও উৎপাদকের গভীর নিয়ম শেখানো হয়। মিডল টার্ম (মধ্যপদ বিভাজন) পদ্ধতি আয়ত্ত করা।",
                            formulaOrRule = "(a + b)³ = a³ + 3a²b + 3ab² + b³ | a³ + b³ = (a + b)(a² - ab + b²)",
                            keyPoints = listOf("মান নির্ণয়ের অনুসিদ্ধান্ত: a³+b³ = (a+b)³ - 3ab(a+b)", "মধ্যপদ ভাঙার নিয়ম (Middle Term)", "ল.সা.গু ও গ.সা.গু নির্ণয়"),
                            exampleBangla = "x + 1/x = 3 হলে x³ + 1/x³ = 3³ - 3(3) = 27 - 9 = 18।"
                        ),
                        CurriculumTopic(
                            id = "c8_m3",
                            titleBangla = "পিথাগোরাসের উপপাদ্য (Pythagorean Theorem)",
                            summaryBangla = "সমকোণী ত্রিভুজের অতিভুজের উপর অঙ্কিত বর্গক্ষেত্রের ক্ষেত্রফল অপর দুই বাহুর সমষ্টির সমান।",
                            detailsBangla = "সমকোণী ত্রিভুজের সমকোণের বিপরীত বাহুকে অতিভুজ বলে। পিথাগোরাসের উপপাদ্য অনুসারে, অতিভুজ² = ভূমি² + লম্ব²। পিথাগোরিয়ান ট্রিপলেট: (৩, ৪, ৫), (৫, ১২, ১৩)।",
                            formulaOrRule = "অতিভুজ² = লম্ব² + ভূমি² (c² = a² + b²)",
                            keyPoints = listOf("পিথাগোরাসের মূল উপপাদ্য প্রমাণ", "বিপরীত পিথাগোরাস উপপাদ্য", "ব্যবহারিক জীবনে গাছের উচ্চতা ও নদীর বিস্তার নির্ণয়"),
                            exampleBangla = "লম্ব = ৩ সেমি, ভূমি = ৪ সেমি হলে অতিভুজ = √(৩² + ৪²) = √(৯ + ১৬) = √২৫ = ৫ সেমি।"
                        ),
                        CurriculumTopic(
                            id = "c8_m4",
                            titleBangla = "চক্রবৃদ্ধি মুনাফা ও চক্রবৃদ্ধি মূলধন (Compound Interest)",
                            summaryBangla = "সবৃদ্ধিমূলধন (C) = P(1 + r)ⁿ এবং চক্রবৃদ্ধি মুনাফা = C - P।",
                            detailsBangla = "প্রতি বছর বৃদ্ধির পর মূলধনের সাথে মুনাফা যুক্ত হয়ে নতুন মূলধন গঠনকে চক্রবৃদ্ধি মূলধন বলে। ব্যাংকিং ও অর্থনীতির ক্ষেত্রে এই সূত্র অত্যাবশ্যক।",
                            formulaOrRule = "C = P(1 + r)ⁿ | চক্রবৃদ্ধি মুনাফা = C - P = P(1 + r)ⁿ - P",
                            keyPoints = listOf("P = প্রারম্ভিক মূলধন", "r = মুনাফার হার (শতকরা)", "n = সময়কাল (বছর)"),
                            exampleBangla = "১০,০০০ টাকার ১০% হারে ২ বছরের চক্রবৃদ্ধি মূলধন = ১০,০০০ × (১ + ০.১)² = ১২,১০০ টাকা। চক্রবৃদ্ধি মুনাফা = ২,১০০ টাকা।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c8_science",
                    nameBangla = "বিজ্ঞান",
                    iconName = "science",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c8_s1",
                            titleBangla = "প্রাণিজগতের শ্রেণিবিন্যাস (Taxonomy)",
                            summaryBangla = "অ্যানিম্যালিয়া জগতের ৮টি অমেরুদণ্ডী পর্ব এবং ১টি কর্ডাটা পর্ব।",
                            detailsBangla = "প্রাণিজগতকে ৯টি পর্বে ভাগ করা হয়েছে: পরিফেরা, নিডারিয়া, প্লাটিহেলমিনথিস, নেমাটোডা, অ্যানেলিডা, আর্থ্রোপোডা, মলাস্কা, একাইনোডার্মাটা ও কর্ডাটা। কর্ডাটার ৩টি উপপর্ব এবং মেরুদণ্ডীর ৭টি শ্রেণি।",
                            formulaOrRule = "কর্ডাটার ৩টি বৈশিষ্ট্য: নটোকর্ড, পৃষ্ঠীয় ফাঁপা স্নায়ুরজ্জু ও গলবিলীয় ফুলকারন্ধ্র",
                            keyPoints = listOf("আর্থ্রোপোডা সর্ববৃহৎ পর্ব (সন্ধিপদী প্রাণী)", "দ্বিপদ নামকরণের জনক ক্যারোলাস লিনিয়াস", "শীতল ও উষ্ণ রক্তের প্রাণীর পার্থক্য")
                        ),
                        CurriculumTopic(
                            id = "c8_s2",
                            titleBangla = "কোষ বিভাজন: অ্যামাইটোসিস, মাইটোসিস ও মিয়োসিস",
                            summaryBangla = "মাইটোসিসের ৫টি ধাপ (প্রোফেজ থেকে টেলোফেজ) এবং মিয়োসিসের গুরুত্ব।",
                            detailsBangla = "মাইটোসিস হলো সমীকরণিক বিভাজন যা দেহকোষে ঘটে এবং ক্রোমোজোম সংখ্যা সমান থাকে (2n ➔ 2n)। মিয়োসিস হলো হ্রাসমূলক বিভাজন যা জননকোষে ঘটে এবং ক্রোমোজোম সংখ্যা অর্ধেক হয়ে যায় (2n ➔ n)।",
                            formulaOrRule = "মাইটোসিস = সমীকরণিক বিভাজন (2n ➔ 2n) | মিয়োসিস = হ্রাসমূলক বিভাজন (2n ➔ n)",
                            keyPoints = listOf("প্রোফেজ, প্রো-মেটাফেজ, মেটাফেজ, অ্যানাফেজ, টেলোফেজ", "অনিয়ন্ত্রিত মাইটোসিসের ফলে টিউমার ও ক্যান্সার হয়", "মিয়োসিস প্রজন্মে ক্রোমোজোম সংখ্যা ধ্রুব রাখে")
                        ),
                        CurriculumTopic(
                            id = "c8_s3",
                            titleBangla = "রাসায়নিক বিক্রিয়া ও সমতাকরণ",
                            summaryBangla = "সংযোজন, বিয়োজন, দহন ও প্রতিস্থাপন বিক্রিয়া। প্রতীক, যোজনী ও সমীকরণ।",
                            detailsBangla = "ল অব কনজারভেশন অব মাস (ভরের নিত্যতা সূত্র) অনুযায়ী বিক্রিয়কের মোট ভর এবং উৎপাদের মোট ভর সমান থাকে। তাই রাসায়নিক সমীকরণ সমতাকরণ জরুরি।",
                            formulaOrRule = "2H₂ + O₂ ➔ 2H₂O | Zn + H₂SO₄ ➔ ZnSO₄ + H₂↑",
                            keyPoints = listOf("মৌলের যোজনী ব্যবহার করে সংকেত তৈরি", "রাসায়নিক সমতাকরণের নিয়মাবলী", "তাপোৎপাদী ও তাপহারী বিক্রিয়ার পার্থক্য")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c8_bangla",
                    nameBangla = "বাংলা ব্যাকরণ ও নির্মিতি",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c8_b1",
                            titleBangla = "ধ্বনি পরিবর্তন, ণ-ত্ব ও ষ-ত্ব বিধান",
                            summaryBangla = "স্বরভক্তি (বিপ্রকর্ষ), অপিনিহিতি, অভিশ্রুতি এবং তৎসম শব্দে ণ ও ষ ব্যবহারের নিয়ম।",
                            detailsBangla = "তৎসম শব্দের বানানে ণ ও ষ এর সঠিক ব্যবহারের নিয়মকে ণ-ত্ব ও ষ-ত্ব বিধান বলে। ঋ, র, ষ এর পর সর্বদা মূর্ধন্য-ণ হয়। স্বরভক্তির উদাহরণ: রত্ন ➔ রতন (মধ্যস্বরগম)।",
                            formulaOrRule = "ঋ, র, ষ + ণ (যেমন: ঋণ, বর্ণ, কৃষ্ণ) | ই, উ কারান্ত উপসর্গের পর স্ ➔ ষ্ (যেমন: অভিষেক)",
                            keyPoints = listOf("তদ্ভব, দেশি ও বিদেশি শব্দে ণ ও ষ হয় না", "স্বভাবতই মূর্ধন্য-ণ যুক্ত শব্দ: চাণক্য মাণিক্য...", "বানান শুদ্ধিকরণ")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c8_english",
                    nameBangla = "English Grammar",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c8_e1",
                            titleBangla = "Right Forms of Verbs & Voice Change",
                            summaryBangla = "Subject-verb agreement, active to passive rules, modal auxiliaries, and tag questions.",
                            detailsBangla = "Rules for verbs: universal truths take simple present; 'while' followed by verb takes -ing; active to passive structure: object becomes subject, appropriate be-verb, V3, by + subject.",
                            formulaOrRule = "Passive: Obj + be-verb + V3 + by + Sub | Tag: Positive statement ➔ Negative tag",
                            keyPoints = listOf("Conditionals: If + Present ➔ Future", "Neither... nor takes singular verb", "Direct to Indirect speech conversion"),
                            exampleBangla = "Active: 'He plays cricket.' ➔ Passive: 'Cricket is played by him.'"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c8_social",
                    nameBangla = "বাংলাদেশ ও বিশ্বপরিচয়",
                    iconName = "social",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c8_soc1",
                            titleBangla = "ঔপনিবেশিক যুগ ও স্বাধীনতা সংগ্রাম",
                            summaryBangla = "পলাশীর যুদ্ধ (১৭৫৭), সিপাহী বিদ্রোহ (১৮৫৭), বঙ্গভঙ্গ (১৯০৫) ও ১৯৪৭ এর দেশভাগ।",
                            detailsBangla = "১৭৫৭ সালের ২৩ জুন পলাশীর আম্রকাননে নবাব সিরাজউদ্দৌলার পরাজয়ের মাধ্যমে বাংলায় ব্রিটিশ শাসনের সূচনা হয়। ১৮৫৭ সালে সিপাহী বিদ্রোহের পর শাসনভার ব্রিটিশ রাজের হাতে যায়।",
                            keyPoints = listOf("লর্ড ক্লাইভ ও দ্বৈত শাসন", "ফরায়েজি আন্দোলন ও হাজী শরীয়তুল্লাহ", "তিতুমীরের বাঁশের কেল্লা")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c8_ict",
                    nameBangla = "তথ্য ও যোগাযোগ প্রযুক্তি (ICT)",
                    iconName = "ict",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c8_ict1",
                            titleBangla = "নেটওয়ার্ক টপোলজি ও ম্যালওয়্যার প্রতিরোধ",
                            summaryBangla = "বাস, স্টার, রিং, মেশ ও ট্রি টপোলজি। কম্পিউটার ভাইরাস ও অ্যান্টিভাইরাস।",
                            detailsBangla = "স্টার টপোলজিতে কেন্দ্রীয় হাব বা সুইচের সাথে সব কম্পিউটার যুক্ত থাকে। এটি সবচেয়ে জনপ্রিয়। ভাইরাসের পূর্ণরূপ: Vital Information Resources Under Siege।",
                            formulaOrRule = "টপোলজির প্রকারভেদ: Bus, Star, Ring, Mesh, Tree, Hybrid",
                            keyPoints = listOf("স্টার টপোলজিতে একটি কম্পিউটার নষ্ট হলে পুরো নেটওয়ার্ক অচল হয় না", "অ্যান্টিভাইরাস নিয়মিত আপডেট করা", "পাসওয়ার্ড ও ফায়ারওয়াল")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c8_religion",
                    nameBangla = "ইসলাম ও নৈতিক শিক্ষা",
                    iconName = "religion",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c8_r1",
                            titleBangla = "নিফাক ও শিরক বর্জন এবং আখলাকে জামিমা",
                            summaryBangla = "মুনাফিকের ৩টি আলামত, শিরকের পরিণতি, গিবত ও হিংসা পরিহার।",
                            detailsBangla = "মুখে ঈমানের দাবি করে অন্তরে কুফর গোপন রাখাকে নিফাক বলে। হাদিস অনুযায়ী মুনাফিকের তিনটি নিদর্শন: কথা বললে মিথ্যা বলে, ওয়াদা করলে ভঙ্গ করে, এবং আমানত রাখলে খেয়ানত করে।",
                            formulaOrRule = "মুনাফিকের আলামত: মিথ্যা বলা, ওয়াদা ভঙ্গ করা, আমানতের খেয়ানত করা",
                            keyPoints = listOf("শিরক ক্ষমার অযোগ্য পাপ", "গিবত করা মৃত ভাইয়ের গোশত খাওয়ার সমান", "ভ্রাতৃত্ব ও সহমর্মিতা")
                        )
                    )
                )
            )
        )
    )
}
