package com.example.data.model

/**
 * Primary Education (Class 1-5) Practice Quizzes.
 * Covers Math, Science, Bangla, English, BGS, and Moral Education.
 */
object QuizRepository {

    val quizSubjects = listOf("সকল", "প্রাথমিক গণিত", "প্রাথমিক বিজ্ঞান", "আমার বাংলা বই", "English for Today", "বাংলাদেশ ও বিশ্বপরিচয়", "ধর্ম ও নৈতিক শিক্ষা")

    val questions: List<QuizQuestion> = listOf(
        // Primary Math
        QuizQuestion(
            id = 1,
            subject = "প্রাথমিক গণিত",
            questionBangla = "ভাজক ৫, ভাগফল ৮ এবং ভাগশেষ ২ হলে ভাজ্য কত?",
            options = listOf("৪০", "৪২", "৩৮", "৪৫"),
            correctIndex = 1,
            explanationBangla = "ভাজ্য = (ভাজক × ভাগফল) + ভাগশেষ = (৫ × ৮) + ২ = ৪০ + ২ = ৪২।"
        ),
        QuizQuestion(
            id = 2,
            subject = "প্রাথমিক গণিত",
            questionBangla = "একটি বৃত্তের ব্যাসার্ধ ৩.৫ সেমি হলে এর ব্যাস কত?",
            options = listOf("৩.৫ সেমি", "৭ সেমি", "১৪ সেমি", "১০.৫ সেমি"),
            correctIndex = 1,
            explanationBangla = "ব্যাস = ২ × ব্যাসার্ধ = ২ × ৩.৫ = ৭ সেমি।"
        ),
        QuizQuestion(
            id = 3,
            subject = "প্রাথমিক গণিত",
            questionBangla = "১২ ও ১৮ এর গসাগু (গরিষ্ঠ সাধারণ গুণনীয়ক) কত?",
            options = listOf("৩", "৬", "১২", "৩৬"),
            correctIndex = 1,
            explanationBangla = "১২ এর গুণনীয়ক: ১, ২, ৩, ৪, ৬, ১২। ১৮ এর গুণনীয়ক: ১, ২, ৩, ৬, ৯, ১৮। সাধারণ গুণনীয়কের মধ্যে সবচেয়ে বড়টি হলো ৬।"
        ),
        QuizQuestion(
            id = 4,
            subject = "প্রাথমিক গণিত",
            questionBangla = "৮টি ডিমের দাম ৭২ টাকা হলে ১৫টি ডিমের দাম কত?",
            options = listOf("১২০ টাকা", "১৩৫ টাকা", "১৪৪ টাকা", "১০০ টাকা"),
            correctIndex = 1,
            explanationBangla = "ঐকিক নিয়ম: ১টি ডিমের দাম = ৭২ ÷ ৮ = ৯ টাকা। সুতরাং ১৫টি ডিমের দাম = ১৫ × ৯ = ১৩৫ টাকা।"
        ),
        QuizQuestion(
            id = 5,
            subject = "প্রাথমিক গণিত",
            questionBangla = "ত্রিভুজের তিন কোণের সমষ্টি কত ডিগ্রি?",
            options = listOf("৯০°", "১২০°", "১৮০°", "৩৬০°"),
            correctIndex = 2,
            explanationBangla = "যেকোনো ত্রিভুজের তিন কোণের যোগফল সর্বদা ১৮০° (দুই সমকোণ)।"
        ),

        // Primary Science
        QuizQuestion(
            id = 6,
            subject = "প্রাথমিক বিজ্ঞান",
            questionBangla = "উদ্ভিদ সালোকসংশ্লেষণ প্রক্রিয়ায় খাদ্য তৈরির সময় কোন গ্যাস গ্রহণ করে?",
            options = listOf("অক্সিজেন", "কার্বন ডাই-অক্সাইড", "নাইট্রোজেন", "হাইড্রোজেন"),
            correctIndex = 1,
            explanationBangla = "উদ্ভিদ বাতাস থেকে কার্বন ডাই-অক্সাইড গ্রহণ করে এবং খাদ্য তৈরির পর বাতাসে অক্সিজেন ত্যাগ করে।"
        ),
        QuizQuestion(
            id = 7,
            subject = "প্রাথমিক বিজ্ঞান",
            questionBangla = "কোন ভিটামিনের অভাবে রাতকানা রোগ হয়?",
            options = listOf("ভিটামিন এ (A)", "ভিটামিন বি (B)", "ভিটামিন সি (C)", "ভিটামিন ডি (D)"),
            correctIndex = 0,
            explanationBangla = "ভিটামিন 'এ'-এর অভাবে রাতকানা রোগ হয়। গাজর, ছোট মাছ ও সবুজ শাকসবজিতে প্রচুর ভিটামিন 'এ' থাকে।"
        ),
        QuizQuestion(
            id = 8,
            subject = "প্রাথমিক বিজ্ঞান",
            questionBangla = "সূর্য থেকে পৃথিবীতে তাপ কোন পদ্ধতিতে সঞ্চালিত হয়?",
            options = listOf("পরিবহন", "পরিচলন", "বিকিরণ", "প্রতিফলন"),
            correctIndex = 2,
            explanationBangla = "কোনো জড় মাধ্যম ছাড়াই তাপ এক স্থান থেকে অন্য স্থানে সঞ্চালিত হওয়ার পদ্ধতি হলো বিকিরণ। সূর্য থেকে আলো ও তাপ বিকিরণ পদ্ধতিতে আসে।"
        ),
        QuizQuestion(
            id = 9,
            subject = "প্রাথমিক বিজ্ঞান",
            questionBangla = "পৃথিবীর কোন গতির কারণে দিন ও রাত্রি সংঘটিত হয়?",
            options = listOf("বার্ষিক গতি", "আহ্নিক গতি", "ঘূর্ণন গতি", "চন্দ্র গতি"),
            correctIndex = 1,
            explanationBangla = "পৃথিবী নিজ অক্ষের চারদিকে ২৪ ঘণ্টায় একবার ঘুরে আসাকে আহ্নিক গতি বলে। এর ফলেই পৃথিবীতে দিন ও রাত হয়।"
        ),

        // Bangla
        QuizQuestion(
            id = 10,
            subject = "আমার বাংলা বই",
            questionBangla = "'ক + ষ' যুক্ত হয়ে কোন যুক্তবর্ণটি গঠিত হয়?",
            options = listOf("জ্ঞ", "ক্ষ", "ক্ত", "ষ্ণ"),
            correctIndex = 1,
            explanationBangla = "ক + ষ = ক্ষ (যেমন: শিক্ষক, ক্ষমা, পরীক্ষা)।"
        ),
        QuizQuestion(
            id = 11,
            subject = "আমার বাংলা বই",
            questionBangla = "'হিমেল একজন সৎ বালক।' এই বাক্যে 'সৎ' কোন পদ?",
            options = listOf("বিশেষ্য", "বিশেষণ", "সর্বনাম", "ক্রিয়া"),
            correctIndex = 1,
            explanationBangla = "যে পদ বিশেষ্য বা সর্বনামের দোষ, গুণ, অবস্থা বা সংখ্যা প্রকাশ করে তাকে বিশেষণ পদ বলে। 'সৎ' বালকের গুণ প্রকাশ করছে।"
        ),
        QuizQuestion(
            id = 12,
            subject = "আমার বাংলা বই",
            questionBangla = "বাংলা ভাষায় স্বরবর্ণের সংখ্যা কয়টি?",
            options = listOf("৯টি", "১০টি", "১১টি", "৩৯টি"),
            correctIndex = 2,
            explanationBangla = "বাংলা ভাষায় স্বরবর্ণ মোট ১১টি (অ, আ, ই, ঈ, উ, ঊ, ঋ, এ, ঐ, ও, ঔ) এবং ব্যঞ্জনবর্ণ ৩৯টি।"
        ),

        // English
        QuizQuestion(
            id = 13,
            subject = "English for Today",
            questionBangla = "Fill in the blank: 'He is ___ honest teacher.'",
            options = listOf("a", "an", "the", "no article"),
            correctIndex = 1,
            explanationBangla = "'Honest' starts with a silent 'h' and vowel sound /ɒ/, hence 'an' is the correct article."
        ),
        QuizQuestion(
            id = 14,
            subject = "English for Today",
            questionBangla = "Which WH-word is used to ask about a place?",
            options = listOf("Who", "When", "Where", "Why"),
            correctIndex = 2,
            explanationBangla = "'Where' is used to inquire about location or place (e.g., 'Where do you live?')."
        ),

        // BGS
        QuizQuestion(
            id = 15,
            subject = "বাংলাদেশ ও বিশ্বপরিচয়",
            questionBangla = "বাংলাদেশের জাতীয় পতাকার দৈর্ঘ্য ও প্রস্থের অনুপাত কত?",
            options = listOf("১০ : ৫", "১০ : ৬", "১০ : ৭", "১০ : ৮"),
            correctIndex = 1,
            explanationBangla = "বাংলাদেশের জাতীয় পতাকার দৈর্ঘ্য ও প্রস্থের অনুপাত ১০ : ৬ (বা ৫ : ৩)।"
        ),
        QuizQuestion(
            id = 16,
            subject = "বাংলাদেশ ও বিশ্বপরিচয়",
            questionBangla = "১৯৭১ সালের মুজিবনগর সরকার কবে শপথ গ্রহণ করে?",
            options = listOf("২৬শে মার্চ", "১০ই এপ্রিল", "১৭ই এপ্রিল", "১৬ই ডিসেম্বর"),
            correctIndex = 2,
            explanationBangla = "১৯৭১ সালের ১০ই এপ্রিল মুজিবনগর সরকার গঠিত হয় এবং ১৭ই এপ্রিল মেহেরপুরের বৈদ্যনাথতলায় (মুজিবনগর) শপথ গ্রহণ করে।"
        ),

        // Religion & Moral
        QuizQuestion(
            id = 17,
            subject = "ধর্ম ও নৈতিক শিক্ষা",
            questionBangla = "অজুর মোট ফরজ কয়টি?",
            options = listOf("৩টি", "৪টি", "৫টি", "৬টি"),
            correctIndex = 1,
            explanationBangla = "অজুর ফরজ ৪টি: ১. সমস্ত মুখ ধোয়া, ২. দুই হাত কনুইসহ ধোয়া, ৩. মাথার চারভাগের একভাগ মাসেহ করা, ৪. দুই পা টাখনুসহ ধোয়া।"
        )
    )
}
