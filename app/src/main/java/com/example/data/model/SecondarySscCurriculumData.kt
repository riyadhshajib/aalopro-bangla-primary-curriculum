package com.example.data.model

object SecondarySscCurriculumData {

    val levels: List<CurriculumClassLevel> = listOf(
        // =========================================================================
        // CLASS 9 (নবম শ্রেণি - এসএসসি পর্ব ১)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 9,
            classNameBangla = "নবম শ্রেণি (Class 9)",
            badgeBangla = "এসএসসি পর্ব - ১",
            subjects = listOf(
                CurriculumSubject(
                    id = "c9_physics",
                    nameBangla = "পদার্থবিজ্ঞান",
                    iconName = "physics",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_p1",
                            titleBangla = "গতির সমীকরণসমূহ ও মুক্তভাবে পড়ন্ত বস্তু",
                            summaryBangla = "v = u + at, s = ut + ½at², v² = u² + 2as এবং গ্যালিলিওর সূত্রাবলী।",
                            detailsBangla = "গতির ৪টি মৌলিক সমীকরণ। মুক্তভাবে পড়ন্ত বস্তুর ক্ষেত্রে ত্বরণ a এর স্থানে অভিকর্ষজ ত্বরণ g = ৯.৮ m/s² ব্যবহৃত হয়। সর্বোচ্চ উচ্চতায় বেগ শূন্য হয়।",
                            formulaOrRule = "v = u + at | s = ut + ½at² | v² = u² + 2as | s = ((u + v)/2) × t",
                            keyPoints = listOf("সমত্বরণ ও সুষম বেগ", "গ্যালিলিওর পড়ন্ত বস্তুর ৩টি সূত্র", "বেগ-সময় লেখচিত্রের ঢাল = ত্বরণ"),
                            exampleBangla = "স্থির অবস্থান থেকে (u=0) ২ m/s² ত্বরণে ৫ সেকেন্ডে অতিক্রান্ত দূরত্ব = ½ × ২ × ৫² = ২৫ মিটার।"
                        ),
                        CurriculumTopic(
                            id = "c9_p2",
                            titleBangla = "নিউটনের বলসূত্র, ভরবেগ ও সংরক্ষণশীলতার নীতি",
                            summaryBangla = "F = ma, ভরবেগের সংরক্ষণ সূত্র: m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂।",
                            detailsBangla = "বাহ্যিক বল শূন্য হলে কোনো ব্যবস্থার মোট ভরবেগ ধ্রুব থাকে। বন্দুক থেকে গুলি ছুড়লে বন্দুকের পশ্চাৎবেগ নির্ণয়ে এই সূত্র প্রযোজ্য।",
                            formulaOrRule = "F = ma | m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂ | পশ্চাৎবেগ: V = -(m/M)v",
                            keyPoints = listOf("বলের ঘাত = ভরবেগের পরিবর্তন (Ft = mv - mu)", "ঘর্ষণ বল ও ঘর্ষণের ৪টি প্রকার", "মহাকর্ষ সূত্র: F = G(m₁m₂)/d²")
                        ),
                        CurriculumTopic(
                            id = "c9_p3",
                            titleBangla = "কাজ, ক্ষমতা ও শক্তির রূপান্তর",
                            summaryBangla = "W = Fs, গতিশক্তি Ek = ½mv², বিভবশক্তি Ep = mgh এবং কর্মদক্ষতা।",
                            detailsBangla = "কাজ করার সামর্থ্যকে শক্তি বলে এবং কাজ করার হারকে ক্ষমতা বলে। যান্ত্রিক শক্তি দুই প্রকার: গতিশক্তি ও বিভবশক্তি। কর্মদক্ষতা (η) = (লভ্য কার্যকর শক্তি / মোট প্রদত্ত শক্তি) × ১০০%।",
                            formulaOrRule = "W = Fs cosθ | Ek = ½mv² | Ep = mgh | P = W/t",
                            keyPoints = listOf("শক্তির সংরক্ষণশীলতা নীতি", "১ অশ্বক্ষমতা (1 HP) = ৭৪৬ ওয়াট", "কাজের একক জুল (J)"),
                            exampleBangla = "৫০ কেজি ভরের বস্তুকে ১০ মিটার উপরে তুললে বিভবশক্তি = ৫০ × ৯.৮ × ১০ = ৪৯০০ জুল।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_chemistry",
                    nameBangla = "রসায়ন",
                    iconName = "chemistry",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_c1",
                            titleBangla = "পর্যায় সারণি ও মৌলসমূহের পর্যায়বৃত্ত ধর্ম",
                            summaryBangla = "মেন্ডেলিফের সারণি, আধুনিক পর্যায় সারণি (১৮টি গ্রুপ ও ৭টি পর্যায়) এবং ইলেকট্রন বিন্যাস।",
                            detailsBangla = "পর্যায়ে বাম থেকে ডানে পারমাণবিক ব্যাসার্ধ হ্রাস পায় এবং আয়নিকরণ শক্তি, তড়িৎ ঋণাত্মকতা ও ইলেকট্রন আসক্তি বৃদ্ধি পায়। গ্রুপে উপর থেকে নিচে পারমাণবিক ব্যাসার্ধ বৃদ্ধি পায়।",
                            formulaOrRule = "গ্রুপ সংখ্যা = বহিঃস্থ স্তরের ইলেকট্রন সংখ্যা | পর্যায় = প্রধান শক্তিস্তর সংখ্যা (n)",
                            keyPoints = listOf("ক্ষার ধাতু (গ্রুপ ১), মৃৎক্ষার ধাতু (গ্রুপ ২), হ্যালোজেন (গ্রুপ ১৭), নিষ্ক্রিয় গ্যাস (গ্রুপ ১৮)", "আয়নিকরণ শক্তি পর্যায়ক্রমিক পরিবর্তন", "ইলেকট্রন আসক্তির ব্যতিক্রম")
                        ),
                        CurriculumTopic(
                            id = "c9_c2",
                            titleBangla = "রাসায়নিক বন্ধন: আয়নিক, সমযোজী ও ধাতব বন্ধন",
                            summaryBangla = "অষ্টক নিয়ম, ইলেকট্রন ত্যাগ/গ্রহণ এবং ইলেকট্রন শেয়ারিং।",
                            detailsBangla = "ধাতু ও অধাতুর মধ্যে ইলেকট্রন আদান-প্রদানে আয়নিক বন্ধন গঠিত হয় (যেমন: NaCl)। দুটি অধাতুর মধ্যে ইলেকট্রন জোড় শেয়ারের মাধ্যমে সমযোজী বন্ধন গঠিত হয় (যেমন: H₂O, CH₄)।",
                            formulaOrRule = "আয়নিক: ধাতু ➔ ক্যাটায়ন (+), অধাতু ➔ অ্যানায়ন (-) | সমযোজী: শেয়ার্ড ইলেকট্রন জোড়",
                            keyPoints = listOf("আয়নিক যৌগের গলনাঙ্ক ও স্ফুটনাঙ্ক বেশি", "জলীয় দ্রবণে আয়নিক যৌগের বিদ্যুৎ পরিবাহিতা", "পোলার সমযোজী যৌগ ও হাইড্রোজেন বন্ধন")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_biology",
                    nameBangla = "জীববিজ্ঞান",
                    iconName = "biology",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_bio1",
                            titleBangla = "জীবকোষ ও টিস্যুর গঠন",
                            summaryBangla = "উদ্ভিদ ও প্রাণী কোষের অঙ্গাণু (প্লাস্টিড, মাইটোকন্ড্রিয়া, রাইবোসোম) ও ভাজক টিস্যু।",
                            detailsBangla = "মাইটোকন্ড্রিয়াকে কোষের শক্তিঘর (Powerhouse) বলা হয় কারণ এখানে শ্বসনের ক্রেবস চক্র সম্পন্ন হয় এবং বিপুল ATP উৎপন্ন হয়। প্লাস্টিড ৩ প্রকার: ক্লোরোপ্লাস্ট (সবুজ), ক্রোমোপ্লাস্ট (রঙিন) ও লিউকোপ্লাস্ট (বর্ণহীন)।",
                            formulaOrRule = "মাইটোকন্ড্রিয়া = কোষের শক্তিঘর | প্লাস্টিড ৩ প্রকার",
                            keyPoints = listOf("রাইবোসোমে প্রোটিন সংশ্লেষিত হয়", "লাইসোজোমকে আত্মঘাতী থলিকা (Suicide sac) বলে", "উদ্ভিদ কোষে কোষপ্রাচীর সেলুলোজ নির্মিত")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_math",
                    nameBangla = "সাধারণ গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_m1",
                            titleBangla = "সেট ও ফাংশন (Set & Function)",
                            summaryBangla = "তালিকা পদ্ধতি ও সেট গঠন পদ্ধতি, সংযোগ (∪), ছেদ (∩), উপসেট ও ডোমেন-রেঞ্জ।",
                            detailsBangla = "জর্জ ক্যান্টর সেটের জনক। শক্তি সেটের উপাদান সংখ্যা ২ⁿ (যেখানে n সেটের উপাদান সংখ্যা)। ভেনচিত্রের মাধ্যমে সেটের অপারেশন সহজে সমাধান করা যায়।",
                            formulaOrRule = "n(P(A)) = 2ⁿ | A ∪ B (সংযোগ) | A ∩ B (ছেদ) | A\\B (অন্তর)",
                            keyPoints = listOf("ফাঁকা সেট (∅), সার্বিক সেট (U)", "কার্তেসীয় গুণজ ও অন্বয় (Relation)", "ফাংশনের ডোমেন ও রেঞ্জ নির্ণয়"),
                            exampleBangla = "A = {1, 2}, P(A) = {∅, {1}, {2}, {1, 2}}। উপাদান সংখ্যা = 2² = 4।"
                        ),
                        CurriculumTopic(
                            id = "c9_m2",
                            titleBangla = "ত্রিকোণমিতিক অনুপাত ও অভেদাবলী (অধ্যায় ৯)",
                            summaryBangla = "sinθ, cosθ, tanθ, cotθ, secθ, cosecθ এর পারস্পরিক সম্পর্ক।",
                            detailsBangla = "সমকোণী ত্রিভুজের সাপেক্ষে sinθ = লম্ব/অতিভুজ, cosθ = ভূমি/অতিভুজ, tanθ = লম্ব/ভূমি। ৩টি মৌলিক অভেদ মুখস্থ রাখা আবশ্যক।",
                            formulaOrRule = "sin²θ + cos²θ = 1 | sec²θ - tan²θ = 1 | cosec²θ - cot²θ = 1",
                            keyPoints = listOf("ত্রিকোণমিতিক কোণের মান (0°, 30°, 45°, 60°, 90°)", "tanθ = sinθ / cosθ", "পূরক কোণের ত্রিকোণমিতিক অনুপাত"),
                            exampleBangla = "sin 30° = 1/2, cos 30° = √3/2, tan 45° = 1।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_higher_math",
                    nameBangla = "উচ্চতর গণিত",
                    iconName = "higher_math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_hm1",
                            titleBangla = "দ্বিপদী বিস্তৃতি ও প্যাসকেলের ত্রিভুজ",
                            summaryBangla = "(1 + y)ⁿ এর বিস্তৃতি, প্যাসকেলের সহগ ত্রিভুজ ও nCr সূত্র।",
                            detailsBangla = "দ্বিপদী রাশিতে ঘাত n হলে মোট পদের সংখ্যা হয় (n + 1)। প্যাসকেলের ত্রিভুজের সাহায্যে সহজে দ্বিপদী সহগগুলো নির্ণয় করা যায়।",
                            formulaOrRule = "(1+x)ⁿ = 1 + nx + [n(n-1)/2!]x² + ... | সাধারণ পদ Tr+1 = ⁿCr xⁿ⁻ʳ yʳ",
                            keyPoints = listOf("পদের সংখ্যা = ঘাত + ১", "প্যাসকেলের ত্রিভুজের প্রতি সারি গঠন", "মধ্যপদ নির্ণয়"),
                            exampleBangla = "(1 + x)³ = 1 + 3x + 3x² + x³।"
                        ),
                        CurriculumTopic(
                            id = "c9_hm2",
                            titleBangla = "স্থানাঙ্ক জ্যামিতি: বিন্দুর দূরত্ব ও ত্রিভুজের ক্ষেত্রফল",
                            summaryBangla = "কার্তেসীয় স্থানাঙ্ক (x, y), দুটি বিন্দুর দূরত্ব সূত্র ও ঢাল (Slope)।",
                            detailsBangla = "P(x₁, y₁) এবং Q(x₂, y₂) বিন্দুর মধ্যবর্তী দূরত্ব d = √((x₂ - x₁)² + (y₂ - y₁)²)। সরলরেখার ঢাল m = (y₂ - y₁) / (x₂ - x₁)।",
                            formulaOrRule = "d = √((x₂ - x₁)² + (y₂ - y₁)²) | ঢাল m = (y₂ - y₁) / (x₂ - x₁)",
                            keyPoints = listOf("মূলবিন্দু (০, ০) থেকে দূরত্ব", "তিনটি বিন্দু সমরেখ হওয়ার শর্ত", "শীর্ষবিন্দুর স্থানাঙ্ক দিয়ে ত্রিভুজের ক্ষেত্রফল"),
                            exampleBangla = "(১, ২) ও (৪, ৬) এর দূরত্ব = √((৪-১)² + (৬-২)²) = √(৯+১৬) = ৫ একক।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_bangla2",
                    nameBangla = "বাংলা ২য় পত্র",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_b2_1",
                            titleBangla = "কারক ও বিভক্তি নির্ণয়",
                            summaryBangla = "কর্তা, কর্ম, করণ, সম্প্রদান, অপাদান, অধিকরণ কারক এবং ৭টি বিভক্তি।",
                            detailsBangla = "বাক্যস্থিত ক্রিয়াপদের সাথে নামপদের যে সম্পর্ক তাকে কারক বলে। 'কে' দ্বারা প্রশ্নে কর্তা, 'কী/কাকে' প্রশ্নে কর্ম, 'কী দিয়ে' প্রশ্নে করণ, 'কোথা হতে' প্রশ্নে অপাদান, এবং 'কোথায়/কখন' প্রশ্নে অধিকরণ কারক হয়।",
                            formulaOrRule = "কারক ৬ প্রকার: কর্তৃ, কর্ম, করণ, সম্প্রদান, অপাদান ও অধিকরণ",
                            keyPoints = listOf("৭ প্রকার বিভক্তি (প্রথমা থেকে সপ্তমী)", "তিলে তৈল হয় (অপাদান) বনাম তিলে তৈল আছে (অধিকরণ)", "বুলবুলিতে ধান খেয়েছে (কর্তায় ৭মী)"),
                            exampleBangla = "কলম দিয়ে লিখি = করণ কারকে ৩য়া বিভক্তি।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_english2",
                    nameBangla = "English 2nd Paper",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_eng2_1",
                            titleBangla = "Conditional Sentences & Connectors",
                            summaryBangla = "Zero, 1st, 2nd, 3rd Conditionals, and linking words in paragraph writing.",
                            detailsBangla = "1st Conditional: If + Present ➔ Future (If it rains, we will stay home). 2nd: If + Past ➔ would + V1. 3rd: If + Past Perfect ➔ would have + V3.",
                            formulaOrRule = "1st: If + V1 ➔ will + V1 | 2nd: If + V2 ➔ would + V1 | 3rd: If + had + V3 ➔ would have + V3",
                            keyPoints = listOf("Unless = If not", "Connectors: Moreover, Therefore, On the other hand, Consequently", "Sentence structure flow"),
                            exampleBangla = "If I had known, I would have helped you (3rd Conditional)."
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_bgs",
                    nameBangla = "বাংলাদেশ ও বিশ্বপরিচয়",
                    iconName = "social",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_bgs1",
                            titleBangla = "পূর্ব বাংলার আন্দোলন ও জাতীয়তাবাদের উত্থান (১৯৪৭-১৯৭০)",
                            summaryBangla = "ভাষা আন্দোলন (১৯৫২), যুক্তফ্রন্ট নির্বাচন (১৯৫৪), ৬ দফা কর্মসূচি (১৯৬৬) ও ৬৯-এর গণঅভ্যুত্থান।",
                            detailsBangla = "১৯৫২ সালের ২১শে ফেব্রুয়ারি মাতৃভাষা বাংলার দাবিতে রফিক, শফিক, সালাম, বরকত শহীদ হন। ১৯৬৬ সালের ৫-৬ ফেব্রুয়ারি বঙ্গবন্ধু ঐতিহাসিক ৬ দফা কর্মসূচি ঘোষণা করেন যা ছিল বাঙালির মুক্তির সনদ (ম্যাগনাকার্টা)।",
                            formulaOrRule = "২১ ফেব্রুয়ারি আন্তর্জাতিক মাতৃভাষা দিবস | ৬ দফা = বাঙালির মুক্তির সনদ",
                            keyPoints = listOf("যুক্তফ্রন্টের ২১ দফা ও নির্বাচন জয়", "১৯৬৯ সালের গণঅভ্যুত্থানে আসাদ ও ড. শামসুজ্জোহার আত্মত্যাগ", "১৯৭০ সালের সাধারণ নির্বাচনে আওয়ামী লীগের নিরঙ্কুশ বিজয়")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c9_accounting",
                    nameBangla = "ব্যবসায় শিক্ষা ও হিসাববিজ্ঞান",
                    iconName = "commerce",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c9_acc1",
                            titleBangla = "হিসাব সমীকরণ ও দুতরফা দাখিলা পদ্ধতি",
                            summaryBangla = "A = L + E (সম্পদ = দায় + মালিকানাস্বত্ব), ডেবিট ও ক্রেডিট নির্ণয়ের নিয়ম।",
                            detailsBangla = "ইতালীয় গণিতবিদ লুকা প্যাসিওলি ১৪৯৪ সালে দুতরফা দাখিলা পদ্ধতির মূলনীতি ব্যাখ্যা করেন। প্রতিটি লেনদেনের দুটি পক্ষ থাকে—এক পক্ষ সুবিধা গ্রহণ করে (ডেবিট) এবং অন্য পক্ষ সুবিধা প্রদান করে (ক্রেডিট)।",
                            formulaOrRule = "A = L + E (Asset = Liabilities + Equity) | ডেবিট = ক্রেডিট",
                            keyPoints = listOf("সম্পদ ও ব্যয় বৃদ্ধি পেলে ডেবিট, কমলে ক্রেডিট", "দায়, আয় ও মূলধন বৃদ্ধি পেলে ক্রেডিট, কমলে ডেবিট", "জাবেদা হিসাবের প্রাথমিক বই"),
                            exampleBangla = "নগদে পণ্য ক্রয় ১০,০০০ টাকা ➔ ক্রয় হিসাব ডেবিট ১০,০০০; নগদান হিসাব ক্রেডিট ১০,০০০।"
                        )
                    )
                )
            )
        ),

        // =========================================================================
        // CLASS 10 (দশম শ্রেণি - পূর্ণাঙ্গ এসএসসি পাঠ্যক্রম)
        // =========================================================================
        CurriculumClassLevel(
            levelNumber = 10,
            classNameBangla = "দশম শ্রেণি (Class 10 - SSC)",
            badgeBangla = "এসএসসি প্রস্তুতি চূড়া",
            subjects = listOf(
                CurriculumSubject(
                    id = "c10_physics",
                    nameBangla = "পদার্থবিজ্ঞান",
                    iconName = "physics",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_p1",
                            titleBangla = "চলবিদ্যুৎ ও ওহমের সূত্র (Ohm's Law)",
                            summaryBangla = "V = IR, তুল্যরোধ (শ্রেণি ও সমান্তরাল), তড়িৎ ক্ষমতা ও বিদ্যুৎ বিল হিসাব।",
                            detailsBangla = "নির্দিষ্ট তাপমাত্রায় পরিবাহীর মধ্য দিয়ে প্রবাহিত তড়িৎপ্রবাহ দুই প্রান্তের বিভব পার্থক্যের সমানুপাতিক। শ্রেণি সমবায়ে Rₛ = R₁ + R₂ + ... এবং সমান্তরালে 1/Rₚ = 1/R₁ + 1/R₂ + ...।",
                            formulaOrRule = "V = IR | P = VI = I²R = V²/R | ব্যয়িত শক্তি W = (P × t) / 1000 kWh",
                            keyPoints = listOf("ওহমের সূত্র ও সীমাবদ্ধতা", "শ্রেণি ও সমান্তরাল সমবায়ের তুলনা", "বিদ্যুৎ বিলের ইউনিট (B.O.T ইউনিট বা kWh) গণনা"),
                            exampleBangla = "৬০ ওয়াটের ৫টি বাতি দৈনিক ৬ ঘণ্টা জ্বললে ৩০ দিনে ব্যয়িত বিদ্যুৎ = (৫ × ৬০ × ৬ × ৩০) / ১০০০ = ৫৪ ইউনিট।"
                        ),
                        CurriculumTopic(
                            id = "c10_p2",
                            titleBangla = "আলোর প্রতিসরণ ও লেন্সের সমীকরণ",
                            summaryBangla = "স্নেলের সূত্র (n = sin i / sin r), সংকট কোণ, পূর্ণ অভ্যন্তরীণ প্রতিফলন ও লেন্সের ক্ষমতা।",
                            detailsBangla = "আলো ঘন মাধ্যম থেকে হালকা মাধ্যমে প্রতিসরিত হলে অভিলম্ব থেকে দূরে সরে যায়। আপতন কোণ সংকট কোণের চেয়ে বড় হলে সম্পূর্ণ আলো প্রথম মাধ্যমে ফিরে আসে, একে পূর্ণ অভ্যন্তরীণ প্রতিফলন বলে (অপটিক্যাল ফাইবারে ব্যবহৃত)।",
                            formulaOrRule = "sin θ_c = 1/n | লেন্সের সমীকরণ: 1/f = 1/u + 1/v | ক্ষমতা P = 1/f (ডায়োপ্টার, D)",
                            keyPoints = listOf("উত্তল লেন্স ও অবতল লেন্সের প্রতিবিম্ব", "মরীচিকা ও অপটিক্যাল ফাইবারের কার্যপদ্ধতি", "চোখের ত্রুটি: হ্রস্বদৃষ্টি (Myopia) ও দূরদৃষ্টি (Hypermetropia)")
                        ),
                        CurriculumTopic(
                            id = "c10_p3",
                            titleBangla = "আধুনিক পদার্থবিজ্ঞান ও ইলেকট্রনিক্স",
                            summaryBangla = "তেজস্ক্রিয়তা (আলফা, বিটা, গামা রশ্মি), সেমিকন্ডাক্টর, ডায়োড ও ট্রানজিস্টর।",
                            detailsBangla = "অর্ধপরিবাহীতে ভেজাল মিশ্রণকে ডোপিং বলে (p-type ও n-type)। p-n জাংশন ডায়োড বিদ্যুৎ একমুখী করতে (রেকটিফায়ার হিসেবে) কাজ করে। এক্স-রে এবং সিটি স্ক্যানের চিকিৎসায় প্রয়োগ।",
                            keyPoints = listOf("তেজস্ক্রিয় অর্ধায়ু ও ক্ষয়সূত্র", "লজিক গেট (AND, OR, NOT)", "আইসিটি ও যোগাযোগ ব্যবস্থা")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_chemistry",
                    nameBangla = "রসায়ন",
                    iconName = "chemistry",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_c1",
                            titleBangla = "মোলের ধারণা ও রাসায়নিক গণনা (অধ্যায় ৬)",
                            summaryBangla = "মোল সংখ্যা n = W/M = V/22.4 = N/6.023×10²³, মোলার দ্রবণ ও শতকরা সংযুতি।",
                            detailsBangla = "১ মোল যেকোনো গ্যাসে প্রমাণ তাপমাত্রা ও চাপে (STP) আয়তন ২২.৪ লিটার এবং অণুর সংখ্যা অ্যাভোগাড্রো সংখ্যা (৬.০২৩ × ১০²³)। দ্রবণের মোলারিটি S = (W × 1000) / (M × V)।",
                            formulaOrRule = "n = W/M = N/N_A = V/22.4 | মোলারিটি S = (1000 × W) / (M × V_mL)",
                            keyPoints = listOf("লিমিটিং বিক্রিয়ক নির্ণয়ের ধাপ", "আণবিক ভর থেকে শতকরা সংযুতি", "স্থূল সংকেত ও আণবিক সংকেত বের করার নিয়ম"),
                            exampleBangla = "৯৮ গ্রাম H₂SO₄ এর মোল সংখ্যা = ৯৮ / ৯৮ = ১ মোল। অণু সংখ্যা = ৬.০২৩ × ১০²³ টি।"
                        ),
                        CurriculumTopic(
                            id = "c10_c2",
                            titleBangla = "এসিড-ক্ষার সমতা ও pH স্কেল",
                            summaryBangla = "pH = -log[H⁺], pH স্কেলের রেঞ্জ ০ থেকে ১৪, প্রশমন বিক্রিয়া ও নির্দেশক।",
                            detailsBangla = "pH = ৭ হলে দ্রবণ নিরপেক্ষ, pH < ৭ হলে এসিডীয় এবং pH > ৭ হলে ক্ষারীয়। মানবদেহের রক্তের স্বাভাবিক pH ৭.৪ (সামান্য ক্ষারীয়)। বৃষ্টিতে pH ৫.৬ এর নিচে নামলে এসিড বৃষ্টি বলে।",
                            formulaOrRule = "pH = -log[H⁺] | pH + pOH = 14",
                            keyPoints = listOf("লিটমাস কাগজ ও নির্দেশকের রঙ পরিবর্তন", "দৈনন্দিন জীবনে pH এর গুরুত্ব (কৃষি, প্রসাধন, হজম)", "লবণের আর্দ্রবিশ্লেষণ")
                        ),
                        CurriculumTopic(
                            id = "c10_c3",
                            titleBangla = "জীবাশ্ম জ্বালানি ও হাইড্রোকার্বন (জৈব রসায়ন)",
                            summaryBangla = "অ্যালকেন (CₙH₂ₙ₊₂), অ্যালকিন (CₙH₂ₙ), অ্যালকাইন (CₙH₂ₙ₋₂) ও পলিমারকরণ।",
                            detailsBangla = "জৈব যৌগের প্রধান শ্রেণি হাইড্রোকার্বন। অ্যালকেন হলো সম্পৃক্ত হাইড্রোকার্বন (যেমন: মিথেন CH₄, ইথেন C₂H₆)। অ্যালকিন ও অ্যালকাইন অসম্পৃক্ত। ব্রোমিন দ্রবণ পরীক্ষা দ্বারা অসম্পৃক্ততা প্রমাণ করা যায়।",
                            formulaOrRule = "অ্যালকেন: CₙH₂ₙ₊₂ | অ্যালকিন: CₙH₂ₙ | অ্যালকাইন: CₙH₂ₙ₋₂",
                            keyPoints = listOf("মিথেনের দহন ও হ্যালোজেন প্রতিস্থাপন", "ইথিলিন থেকে পলিথিন তৈরি (পলিমারকরণ)", "জৈব এসিড ও অ্যালকোহল প্রস্তুতি")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_biology",
                    nameBangla = "জীববিজ্ঞান",
                    iconName = "biology",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_b1",
                            titleBangla = "জীবনীশক্তি: সালোকসংশ্লেষণ ও শ্বসন (অধ্যায় ৪)",
                            summaryBangla = "আলোক পর্যায়, অন্ধকার পর্যায় (ক্যালভিন চক্র C₃) ও গ্লাইকোলাইসিস।",
                            detailsBangla = "সালোকসংশ্লেষণে সৌরশক্তি রাসায়নিক শক্তিতে রূপান্তরিত হয়। বিক্রিয়া: 6CO₂ + 12H₂O + আলো + ক্লোরোফিল ➔ C₆H₁₂O₆ + 6O₂ + 6H₂O। সবাত শ্বসনে ১ অণু গ্লুকোজ জারিত হয়ে মোট ৩৮টি এটিপি (ATP) তৈরি করে।",
                            formulaOrRule = "সালোকসংশ্লেষণ: 6CO₂ + 12H₂O ➔ C₆H₁₂O₆ + 6O₂ + 6H₂O | শ্বসনে 38 ATP উৎপন্ন",
                            keyPoints = listOf("ATP কে বায়োলজিক্যাল কয়েন বা জৈব মুদ্রা বলে", "C₃ ও C₄ উদ্ভিদের পার্থক্য", "সবাত শ্বসনের ৪টি ধাপ: গ্লাইকোলাইসিস, এসিটাইল কো-এ, ক্রেবস চক্র, ইটিএস")
                        ),
                        CurriculumTopic(
                            id = "c10_b2",
                            titleBangla = "জীবের বংশগতি ও ডিএনএ অনুলিপন (অধ্যায় ১২)",
                            summaryBangla = "ওয়াটসন ও ক্রিকের DNA ডাবল হেলিক্স মডেল, জিন, ক্রোমোজোম ও লিঙ্গ নির্ধারণ।",
                            detailsBangla = "মানুষের প্রতিটি কোষে ২৩ জোড়া (৪৬টি) ক্রোমোজোম থাকে (২২ জোড়া অটোসোম এবং ১ জোড়া সেক্স ক্রোমোজোম: পুরুষে XY, নারীতে XX)। মেন্ডেলের বংশগতির প্রথম ও দ্বিতীয় সূত্র।",
                            formulaOrRule = "DNA বেস পেয়ারিং: A = T (দ্বিবন্ধন), G ≡ C (ত্রিবন্ধন)",
                            keyPoints = listOf("সন্তানের লিঙ্গ নির্ধারণে পিতার ক্রোমোজোমের ভূমিকা", "জেনেটিক ডিসঅর্ডার: বর্ণান্ধতা (Color blindness), থ্যালাসেমিয়া", "DNA ফিঙ্গারপ্রিন্টিং এর গুরুত্ব")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_math",
                    nameBangla = "সাধারণ গণিত",
                    iconName = "math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_m1",
                            titleBangla = "দ্বিঘাত সমীকরণ ও নিশ্চয়ক",
                            summaryBangla = "ax² + bx + c = 0, নিশ্চয়ক D = b² - 4ac এবং মূলের প্রকৃতি।",
                            detailsBangla = "নিশ্চায়ক D > 0 হলে মূলদ্বয় বাস্তব ও অসমান; D = 0 হলে বাস্তব ও সমান; D < 0 হলে মূলদ্বয় অবাস্তব।",
                            formulaOrRule = "x = (-b ± √(b² - 4ac)) / (2a) | D = b² - 4ac",
                            keyPoints = listOf("উৎপাদক বিশ্লেষণের বিকল্প সাধারণ সূত্র", "বীজের যোগফল: α + β = -b/a", "বীজের গুণফল: αβ = c/a"),
                            exampleBangla = "x² - 5x + 6 = 0 এর জন্য a=1, b=-5, c=6। x = (5 ± √(25 - 24)) / 2 = (5 ± 1)/2 ➔ x = 3, 2।"
                        ),
                        CurriculumTopic(
                            id = "c10_m2",
                            titleBangla = "পরিমিতি ও ত্রিমাত্রিক জ্যামিতি (অধ্যায় ১৬)",
                            summaryBangla = "বেলন (সিলিন্ডার), গোলক ও কোনকের ক্ষেত্রফল ও আয়তন নির্ণয়।",
                            detailsBangla = "r ব্যাসার্ধ ও h উচ্চতাবিশিষ্ট বেলনের আয়তন = πr²h এবং বক্রতলের ক্ষেত্রফল = 2πrh। গোলকের পৃষ্ঠতলের ক্ষেত্রফল = 4πr² এবং আয়তন = 4/3 πr³।",
                            formulaOrRule = "বেলনের আয়তন = πr²h | গোলকের আয়তন = 4/3 πr³ | গোলকের ক্ষেত্রফল = 4πr²",
                            keyPoints = listOf("সমবৃত্তভূমিক কোণকের আয়তন = 1/3 πr²h", "আয়তাকার ঘনবস্তুর কর্ণের দৈর্ঘ্য = √(a² + b² + c²)", "ব্যবহারিক সমস্যা সমাধান"),
                            exampleBangla = "৭ সেমি ব্যাসার্ধ ও ১০ সেমি উচ্চতার সিলিন্ডারের আয়তন = ২২/৭ × ৭² × ১০ = ১৫৪০ ঘন সেমি।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_higher_math",
                    nameBangla = "উচ্চতর গণিত",
                    iconName = "higher_math",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_hm1",
                            titleBangla = "সম্ভাবনা (Probability) ও নমুনা ক্ষেত্র",
                            summaryBangla = "P(A) = অনুকূল ফলাফল সংখ্যা / সমগ্র সম্ভাব্য ফলাফল সংখ্যা। মুদ্রা ও ছক্কা নিক্ষেপ।",
                            detailsBangla = "নিশ্চিত ঘটনার সম্ভাবনা ১ এবং অসম্ভব ঘটনার সম্ভাবনা ০। যেকোনো ঘটনার সম্ভাবনা ০ থেকে ১ এর মধ্যে অবস্থান করে (০ ≤ P(E) ≤ ১)। প্রোবাবিলিটি ট্রি অঙ্কন।",
                            formulaOrRule = "P(A) = n(A) / n(S) | ০ ≤ P(A) ≤ ১ | নিশ্চিত ঘটনা P = ১",
                            keyPoints = listOf("একটি মুদ্রা দুইবার নিক্ষেপে নমুনা বিন্দু ৪টি {HH, HT, TH, TT}", "একটি ছক্কায় জোড় সংখ্যা ওঠার সম্ভাবনা = ৩/৬ = ১/২", "পরস্পর বর্জনশীল ঘটনা"),
                            exampleBangla = "একটি ছক্কা নিক্ষেপ করলে ৫ ওঠার সম্ভাবনা = ১/৬।"
                        ),
                        CurriculumTopic(
                            id = "c10_hm2",
                            titleBangla = "সমতলীয় ভেক্টর ও ভেক্টর যোগের ত্রিভুজ বিধি",
                            summaryBangla = "স্কেলার ও ভেক্টর রাশি, অবস্থান ভেক্টর, একক ভেক্টর ও ভেক্টরের সামান্তরিক সূত্র।",
                            detailsBangla = "যেসব রাশির মান ও দিক উভয়ই আছে তাদের ভেক্টর রাশি বলে (বেগ, ত্বরণ, বল)। কোনো ভেক্টরকে তার মান দিয়ে ভাগ করলে ওই দিকের একক ভেক্টর পাওয়া যায়।",
                            formulaOrRule = "একক ভেক্টর â = A / |A| | ত্রিভুজ বিধি: AB + BC = AC",
                            keyPoints = listOf("নাল ভেক্টর বা শূন্য ভেক্টর যার মান ০", "বিপরীত ভেক্টর", "জ্যামিতিক প্রমাণে ভেক্টরের ব্যবহার")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_bangla2",
                    nameBangla = "বাংলা ২য় পত্র",
                    iconName = "bangla",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_b2_1",
                            titleBangla = "সমাস ও ব্যাসবাক্য নির্ণয়",
                            summaryBangla = "দ্বন্দ্ব, দ্বিগু, কর্মধারয়, তৎপুরুষ, বহুব্রীহি ও অব্যয়ীভাব সমাসের নিয়ম।",
                            detailsBangla = "পরস্পর অর্থসঙ্গতিবিশিষ্ট একাধিক পদের এক পদে পরিণত হওয়াকে সমাস বলে। সমাসবদ্ধ পদকে সমস্তপদ এবং এর ব্যাখ্যামূলক বাক্যকে ব্যাসবাক্য বা বিগ্রহবাক্য বলে।",
                            formulaOrRule = "সমাস মূলত ৬ প্রকার: দ্বন্দ্ব, দ্বিগু, কর্মধারয়, তৎপুরুষ, বহুব্রীহি, অব্যয়ীভাব",
                            keyPoints = listOf("দ্বন্দ্ব সমাসে উভয় পদের অর্থ প্রধান (মা ও বাবা = মা-বাবা)", "দ্বিগু সমাসে সংখ্যাবাচক শব্দ পূর্বে থাকে (তেমাথা)", "বহুব্রীহি সমাসে অন্য কোনো তৃতীয় অর্থ বোঝায় (পীতাম্বর)"),
                            exampleBangla = "নীল যে পদ্ম = নীলপদ্ম (কর্মধারয়), দশ আনন যার = দশানন (বহুব্রীহি)।"
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_english2",
                    nameBangla = "English 2nd Paper",
                    iconName = "english",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_eng2_1",
                            titleBangla = "Modifiers, Connectors & Formal Letters",
                            summaryBangla = "Pre-modifiers, Post-modifiers, Sentence Linkers, and CV with Cover Letter format.",
                            detailsBangla = "A modifier qualifies or describes a noun or verb. A noun adjective modifies another noun (e.g. 'school bus'). Connectors ensure logical coherence across sentences.",
                            formulaOrRule = "Pre-modifier: Adjective / Participle + Noun | Post-modifier: Infinitive phrase / Appositive",
                            keyPoints = listOf("Present participle (Verb+ing) as adjective", "CV writing structure: Objectives, Education, Experience, References", "Formal tone in applications")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_bgs",
                    nameBangla = "বাংলাদেশ ও বিশ্বপরিচয়",
                    iconName = "social",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_bgs1",
                            titleBangla = "বাংলাদেশ সরকারের অঙ্গসমূহ ও টেকসই উন্নয়ন (SDG)",
                            summaryBangla = "আইন বিভাগ (জাতীয় সংসদ), শাসন বিভাগ ও বিচার বিভাগ। ১৭টি এসডিজি লক্ষ্যমাত্রা।",
                            detailsBangla = "বাংলাদেশের আইনসভা এক কক্ষবিশিষ্ট যার নাম জাতীয় সংসদ (৩৫০ আসন: ৩০০ সাধারণ + ৫০ সংরক্ষিত মহিলা)। শাসন বিভাগের প্রধান রাষ্ট্রপতি ও সরকার প্রধান প্রধানমন্ত্রী। ২০৩০ সালের মধ্যে অর্জনের লক্ষ্য ১৭টি SDG।",
                            formulaOrRule = "সরকারের ৩টি অঙ্গ: আইন বিভাগ, শাসন বিভাগ ও বিচার বিভাগ | SDG লক্ষ্য ১৭টি",
                            keyPoints = listOf("সংবিধান রাষ্ট্রের সর্বোচ্চ আইন", "সুপ্রিম কোর্টের দুটি বিভাগ: আপিল বিভাগ ও হাইকোর্ট বিভাগ", "দারিদ্র্য বিমোচন ও মানসম্মত শিক্ষা নিশ্চিতকরণ")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_ict",
                    nameBangla = "তথ্য ও যোগাযোগ প্রযুক্তি (ICT)",
                    iconName = "ict",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_ict1",
                            titleBangla = "ডাটাবেজ ম্যানেজমেন্ট সিস্টেম (DBMS) ও স্প্রেডশিট",
                            summaryBangla = "ফিল্ড, রেকর্ড, টেবিল, কুয়েরি এবং এক্সেলের মৌলিক ফর্মুলা।",
                            detailsBangla = "সম্পর্কযুক্ত বিপুল ডেটা সংরক্ষণ, অনুসন্ধান ও প্রক্রিয়াকরণে ডেটাবেজ ব্যবহৃত হয়। প্রাইমারি কি (Primary Key) প্রতিটি রেকর্ডকে অনন্যভাবে শনাক্ত করে। এক্সেলে =SUM(), =AVERAGE(), =IF() সূত্রের মাধ্যমে স্বয়ংক্রিয় হিসাব।",
                            formulaOrRule = "Excel: =SUM(A1:A10) | =AVERAGE(B1:B10) | =IF(C1>=33, \"Pass\", \"Fail\")",
                            keyPoints = listOf("রিলেশনাল ডেটাবেজ (RDBMS)", "সাইবার নিরাপত্তা ও পাসওয়ার্ড সুরক্ষা", "স্প্রেডশিটে চার্ট ও গ্রাফ তৈরি")
                        )
                    )
                ),
                CurriculumSubject(
                    id = "c10_finance",
                    nameBangla = "ফিন্যান্স ও ব্যাংকিং",
                    iconName = "finance",
                    topics = listOf(
                        CurriculumTopic(
                            id = "c10_fin1",
                            titleBangla = "অর্থের সময়মূল্য (Time Value of Money) ও ব্যাংকিং",
                            summaryBangla = "ভবিষ্যৎ মূল্য FV = PV(1 + r)ⁿ, বর্তমান মূল্য PV, বাণিজ্যিক ব্যাংক ও বাংলাদেশ ব্যাংক।",
                            detailsBangla = "সময়ের সাথে সাথে অর্থের মূল্যের পরিবর্তন হয়। আজকের ১০০ টাকা ভবিষ্যৎ ৫ বছর পরের ১০০ টাকার চেয়ে অধিক মূল্যবান। কেন্দ্রীয় ব্যাংক হিসেবে বাংলাদেশ ব্যাংক দেশের মুদ্রা তৈরি ও ব্যাংকিং ব্যবস্থার নিয়ন্ত্রক।",
                            formulaOrRule = "FV = PV(1 + i)ⁿ | বছরে একাধিকবার চক্রবৃদ্ধি হলে: FV = PV(1 + i/m)ⁿᵐ",
                            keyPoints = listOf("সুযোগ ব্যয় (Opportunity Cost)", "বাণিজ্যিক ব্যাংকের মূল উদ্দেশ্য মুনাফা অর্জন", "বাংলাদেশ ব্যাংক মুদ্রার অভিভাবক ও সরকারের ব্যাংক"),
                            exampleBangla = "১০% সুদে বর্তমান ১০০ টাকার ৩ বছর পরের ভবিষ্যৎ মূল্য = ১০০ × (১ + ০.১)³ = ১৩৩.১ টাকা।"
                        )
                    )
                )
            )
        )
    )
}
