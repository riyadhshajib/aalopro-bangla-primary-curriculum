package com.example.data.model

/**
 * Primary School (Class 1-5) Essential Formulas, Rules, and Principles.
 * Cohesive NCTB Primary Education Reference.
 */
object FormulaRepository {

    val subjects = listOf("সকল", "গণিত", "বিজ্ঞান", "বাংলা ব্যাকরণ", "ইংরেজি", "পরিবেশ ও সমাজ", "নৈতিক শিক্ষা")

    val formulas: List<StudyFormula> = listOf(
        // Primary Math
        StudyFormula(
            id = "m1",
            titleBangla = "ঐকিক নিয়মের মূল সূত্র",
            formulaSymbol = "১টির মান = মোট দাম ÷ মোট পরিমাণ",
            subject = "গণিত",
            explanationBangla = "প্রথমে ১টি একক বস্তুর দাম বা মান বের করে পরে কাঙ্ক্ষিত পরিমাণের মান নির্ণয়ের সহজ পদ্ধতি।",
            workedExampleBangla = "উদাহরণ: ৫টি খাতার দাম ১০০ টাকা হলে ৮টি খাতার দাম = (১০০ ÷ ৫) × ৮ = ২০ × ৮ = ১৬০ টাকা।",
            relatedQuery = "ঐকিক নিয়ম সমাধান উদাহরণসহ"
        ),
        StudyFormula(
            id = "m2",
            titleBangla = "ভাগের মৌলিক সূত্র (নিঃশেষে বিভাজ্য ও অবিভাজ্য)",
            formulaSymbol = "ভাজ্য = (ভাজক × ভাগফল) + ভাগশেষ",
            subject = "গণিত",
            explanationBangla = "ভাগের ক্ষেত্রে ভাগশেষ সর্বদা ভাজকের চেয়ে ছোট হতে হবে (ভাগশেষ < ভাজক)। নিঃশেষে বিভাজ্য হলে ভাগশেষ = ০।",
            workedExampleBangla = "উদাহরণ: ভাজক ৬, ভাগফল ৭ ও ভাগশেষ ৩ হলে ভাজ্য = (৬ × ৭) + ৩ = ৪২ + ৩ = ৪৫।",
            relatedQuery = "ভাগের সূত্র ভাজ্য ভাজক ভাগফল ভাগশেষ"
        ),
        StudyFormula(
            id = "m3",
            titleBangla = "গড় নির্ণয়ের সূত্র",
            formulaSymbol = "গড় = রাশিগুলোর যোগফল ÷ রাশির সংখ্যা",
            subject = "গণিত",
            explanationBangla = "কতগুলো রাশির মান একসাথে যোগ করে মোট পদসংখ্যা দিয়ে ভাগ করলে গড় পাওয়া যায়।",
            workedExampleBangla = "উদাহরণ: ১০, ১৫ ও ২০ এর গড় = (১০ + ১৫ + ২০) ÷ ৩ = ৪৫ ÷ ৩ = ১৫।",
            relatedQuery = "গড় নির্ণয়ের অংক ও নিয়ম"
        ),
        StudyFormula(
            id = "m4",
            titleBangla = "আয়তক্ষেত্র ও বর্গক্ষেত্রের ক্ষেত্রফল",
            formulaSymbol = "আয়তক্ষেত্র = দৈর্ঘ্য × প্রস্থ | বর্গক্ষেত্র = বাহু²",
            subject = "গণিত",
            explanationBangla = "আবদ্ধ ক্ষেত্রের ভেতরের পরিমাপকে ক্ষেত্রফল বলে। ক্ষেত্রফলের একক বর্গমিটার বা বর্গসেন্টিমিটার।",
            workedExampleBangla = "উদাহরণ: দৈর্ঘ্য ১০ মিটার ও প্রস্থ ৬ মিটার হলে ক্ষেত্রফল = ১০ × ৬ = ৬০ বর্গমিটার।",
            relatedQuery = "ক্ষেত্রফল নির্ণয়ের সূত্র আয়তক্ষেত্র বর্গক্ষেত্র"
        ),
        StudyFormula(
            id = "m5",
            titleBangla = "সরল মুনাফা ও শতকরা লাভ-ক্ষতি",
            formulaSymbol = "I = (P × r × n) / 100 | লাভ = বিক্রয়মূল্য - ক্রয়মূল্য",
            subject = "গণিত",
            explanationBangla = "I = মুনাফা, P = আসল, r = মুনাফার শতকরা হার, n = সময় (বছর)।",
            workedExampleBangla = "উদাহরণ: ১০০০ টাকার ৫% হারে ২ বছরের মুনাফা = (১০০০ × ৫ × ২) / ১০০ = ১০০ টাকা।",
            relatedQuery = "সরল মুনাফা ও লাভ ক্ষতির অংক"
        ),
        StudyFormula(
            id = "m6",
            titleBangla = "বৃত্তের ব্যাস ও ব্যাসার্ধের সম্পর্ক",
            formulaSymbol = "ব্যাস (d) = ২ × ব্যাসার্ধ (r) | পরিধি = ২πr",
            subject = "গণিত",
            explanationBangla = "বৃত্তের কেন্দ্র থেকে পরিধির দূরত্ব হলো ব্যাসার্ধ। পরিধির যে কোনো দুই বিন্দুর সংযোগকারী রেখা কেন্দ্র দিয়ে গেলে তাকে ব্যাস বলে।",
            workedExampleBangla = "উদাহরণ: একটি বৃত্তের ব্যাসার্ধ ৪ সেমি হলে তার ব্যাস = ২ × ৪ = ৮ সেমি।",
            relatedQuery = "বৃত্তের ব্যাস ও ব্যাসার্ধ নির্ণয়"
        ),

        // Science
        StudyFormula(
            id = "s1",
            titleBangla = "সালোকসংশ্লেষণের মৌলিক বিক্রিয়া",
            formulaSymbol = "CO₂ + পানি + সূর্যালোক (ক্লোরোফিল) ➔ গ্লুকোজ + O₂",
            subject = "বিজ্ঞান",
            explanationBangla = "সবুজ উদ্ভিদ সূর্যালোক ও ক্লোরোফিলের উপস্থিতিতে কার্বন ডাই-অক্সাইড ও পানি থেকে খাদ্য তৈরি করে ও অক্সিজেন নির্গত করে।",
            workedExampleBangla = "উদ্ভিদ পাতা দিয়ে আলো শোষণ করে এবং মূলরোম দিয়ে পানি টেনে নিয়ে খাদ্য প্রস্তুত করে।",
            relatedQuery = "সালোকসংশ্লেষণ প্রক্রিয়া পঞ্চম শ্রেণি"
        ),
        StudyFormula(
            id = "s2",
            titleBangla = "তাপ সঞ্চালনের তিন পদ্ধতি",
            formulaSymbol = "তাপ সঞ্চালন = পরিবহন (কঠিন) + পরিচলন (তরল/বায়ু) + বিকিরণ (শূন্য মাধ্যম)",
            subject = "বিজ্ঞান",
            explanationBangla = "কঠিন মাধ্যমে পরিবহন, তরল ও গ্যাসে পরিচলন এবং সূর্য থেকে পৃথিবীতে শূন্য মাধ্যমে আলো ও তাপ আসে বিকিরণ পদ্ধতিতে।",
            workedExampleBangla = "সূর্যের আলো ৮ মিনিট ২০ সেকেন্ডে বিকিরণ পদ্ধতিতে পৃথিবীতে পৌঁছায়।",
            relatedQuery = "তাপ সঞ্চালনের তিন পদ্ধতি বিজ্ঞান"
        ),
        StudyFormula(
            id = "s3",
            titleBangla = "খাদ্যের ৬টি প্রধান পুষ্টি উপাদান",
            formulaSymbol = "পুষ্টি = শর্করা + আমিষ + স্নেহ + ভিটামিন + খনিজ লবণ + পানি",
            subject = "বিজ্ঞান",
            explanationBangla = "শরীর সুস্থ রাখতে প্রতিদিনের আহারে এই ছয়টি পুষ্টি উপাদানের ভারসাম্যপূর্ণ সুষম খাদ্য গ্রহণ অপরিহার্য।",
            workedExampleBangla = "শর্করা (ভাত/রুটি) শক্তি দেয়, আমিষ (মাছ/ডিম) দেহ গঠন করে, ভিটামিন (ফল/সবজি) রোগ প্রতিরোধ করে।",
            relatedQuery = "খাদ্যের ৬টি উপাদান সুষম খাদ্য"
        ),

        // Bangla Grammar
        StudyFormula(
            id = "b1",
            titleBangla = "পদের প্রকারভেদ ও চেনার উপায়",
            formulaSymbol = "পদ ৫ প্রকার: বিশেষ্য, বিশেষণ, সর্বনাম, অব্যয়, ক্রিয়া",
            subject = "বাংলা ব্যাকরণ",
            explanationBangla = "বাক্যে ব্যবহৃত বিভক্তিযুক্ত প্রতিটি শব্দকে পদ বলে। বিশেষ্য = নাম, সর্বনাম = নামের বিকল্প, বিশেষণ = গুণ/অবস্থা, ক্রিয়া = কাজ।",
            workedExampleBangla = "উদাহরণ: 'রনি (বিশেষ্য) ভালো (বিশেষণ) ছেলে। সে (সর্বনাম) রোজ স্কুলে যায় (ক্রিয়া)।'",
            relatedQuery = "পদের শ্রেণিবিভাগ উদাহরণসহ"
        ),
        StudyFormula(
            id = "b2",
            titleBangla = "বিরামচিহ্নের সময় ও ব্যবহার",
            formulaSymbol = "কমা (,) = ১ বলার সময় | দাঁড়ি (।) = ১ সেকেন্ড | সেমিকোলন = ১ বলার দ্বিগুণ",
            subject = "বাংলা ব্যাকরণ",
            explanationBangla = "বাক্যের অর্থ স্পষ্ট ও শ্রুতিমধুর করার জন্য বাক্যের মাঝে বিভিন্ন প্রতীকে থামার নিয়ম।",
            workedExampleBangla = "উদাহরণ: 'আমরা স্কুলে যাই।' (পূর্ণ ভাব প্রকাশের পর দাঁড়ি)",
            relatedQuery = "বিরামচিহ্নের ব্যবহার ও থামার সময়"
        ),

        // English
        StudyFormula(
            id = "e1",
            titleBangla = "WH-Question গঠন প্রণালী",
            formulaSymbol = "WH-Word + Auxiliary Verb + Subject + Main Verb + Extension?",
            subject = "ইংরেজি",
            explanationBangla = "Who, What, Where, When, Why, How দিয়ে ইংরেজি প্রশ্ন তৈরির সাধারণ ব্যাকরণিক কাঠামো।",
            workedExampleBangla = "Statement: 'Raju lives in Dhaka.' ➔ Question: 'Where does Raju live?'",
            relatedQuery = "WH questions rules Class 5 English"
        ),
        StudyFormula(
            id = "e2",
            titleBangla = "Articles (A, An, The) ব্যবহারের নিয়ম",
            formulaSymbol = "A (Consonant sound) | An (Vowel sound) | The (Specific/Unique)",
            subject = "ইংরেজি",
            explanationBangla = "A, E, I, O, U বা স্বরবর্ণ উচ্চারণের পূর্বে 'An' বসে। নির্দিষ্ট ব্যক্তি বা বিশ্বব্রহ্মাণ্ডের অনন্য বস্তুর পূর্বে 'The' বসে।",
            workedExampleBangla = "An apple, An honest man (/ɒ/), A boy, The Sun, The Padma.",
            relatedQuery = "Articles rules A An The"
        ),

        // Social & Moral
        StudyFormula(
            id = "soc1",
            titleBangla = "বাংলাদেশের জাতীয় প্রতীকসমূহের মাপ",
            formulaSymbol = "পতাকার অনুপাত = ১০ : ৬ (দৈর্ঘ্য : প্রস্থ) | লাল বৃত্তের ব্যাসার্ধ = দৈর্ঘ্যের ১/৫ অংশ",
            subject = "পরিবেশ ও সমাজ",
            explanationBangla = "জাতীয় পতাকার গাঢ় সবুজ পটভূমির মাঝে লাল বৃত্ত উদীয়মান সূর্য ও শহীদের রক্তের প্রতীক।",
            workedExampleBangla = "পতাকার দৈর্ঘ্য ১০ ফুট হলে প্রস্থ হবে ৬ ফুট এবং লাল বৃত্তের ব্যাসার্ধ হবে ২ ফুট।",
            relatedQuery = "জাতীয় পতাকার অনুপাত ও মাপ"
        ),
        StudyFormula(
            id = "r1",
            titleBangla = "অজুর ৪টি ফরজ ও সালাতের স্তম্ভ",
            formulaSymbol = "অজুর ফরজ ৪টি: মুখ ধোয়া + হাত কনুইসহ ধোয়া + মাথা মাসেহ + পা টাখনুসহ ধোয়া",
            subject = "নৈতিক শিক্ষা",
            explanationBangla = "সালাতের পূর্বে পবিত্রতা অর্জনে এই ৪টি অঙ্গ ধৌত করা বাধ্যতামূলক। সালাতের বাইরে ৭টি ও ভেতরে ৬টি ফরজ রয়েছে।",
            workedExampleBangla = "দৈনিক ৫ ওয়াক্ত সালাত: ফজর, জোহর, আসর, মাগরিব ও এশা।",
            relatedQuery = "অজুর ৪ ফরজ ও সালাতের নিয়ম"
        )
    )
}
