package com.example.data.model

// Imported selected practice from AaloApp; not exhaustive textbook coverage.
object GradeQuizBank {
 val questions = listOf(
QuizQuestion(10000, "বাংলা", "বাংলা স্বরবর্ণ কয়টি?", listOf("১০টি","১১টি","১২টি"), 1, "অ থেকে ঔ পর্যন্ত গুনলে ১১টি স্বরবর্ণ পাই।", 1, "AaloApp নির্বাচিত অনুশীলন; বইয়ের হুবহু প্রশ্ন নয়"),
QuizQuestion(10001, "গণিত", "২টি আমের সঙ্গে ৩টি আম মেলালে কয়টি হয়?", listOf("৪টি","৫টি","৬টি"), 1, "একসঙ্গে গুনি: ১, ২, ৩, ৪, ৫। তাই ২ + ৩ = ৫।", 1, "AaloApp নির্বাচিত অনুশীলন; বইয়ের হুবহু প্রশ্ন নয়"),
QuizQuestion(10002, "গণিত", "৩টি দলে ২টি করে আম। মোট কয়টি?", listOf("৫টি","৬টি","৮টি"), 1, "২ + ২ + ২ = ৬।", 2, "AaloApp নির্বাচিত অনুশীলন; বইয়ের হুবহু প্রশ্ন নয়"),
QuizQuestion(10003, "ইংরেজি", "কোনটি Proper Noun বা নির্দিষ্ট নাম?", listOf("city","Dhaka","girl"), 1, "Dhaka একটি নির্দিষ্ট শহরের নাম।", 2, "AaloApp নির্বাচিত অনুশীলন; বইয়ের হুবহু প্রশ্ন নয়"),
QuizQuestion(10004, "গণিত", "১/৪ মানে কী?", listOf("এক ভাগের এক ভাগ","চার ভাগের এক ভাগ","দুই ভাগের এক ভাগ","চার ভাগ"), 1, "নেওয়া অংশ ১, মোট সমান অংশ ৪। তাই ১/৪।", 3, "AaloApp নির্বাচিত অনুশীলন; বইয়ের হুবহু প্রশ্ন নয়"),
QuizQuestion(10005, "বিজ্ঞান", "সবুজ গাছের খাবার মূলত কোথায় তৈরি হয়?", listOf("পাতায়","মাটিতে","ফুলদানিতে"), 0, "পাতায় সূর্যের আলো ব্যবহার করে খাবার তৈরি হয়।", 3, "AaloApp নির্বাচিত অনুশীলন; বইয়ের হুবহু প্রশ্ন নয়"),
QuizQuestion(10006, "গণিত", "৬টি আপেল বিক্রির পরে ৮টি বাকি। প্রথমে কতটি ছিল জানতে কোন হিসাব করব?", listOf("৮ − ৬","৮ + ৬","৬ − ৮"), 1, "বিক্রি করা ৬টি ও বাকি ৮টি মিলিয়ে প্রথমে ১৪টি ছিল।", 3, "Class 3 Mathematics, chapter 4, source page 43; page_043.webp SHA-256 c1ed6db095b9e9133ab3ebd07d2637bfb72b9bbd543c78bbd5cad8c55d623b29"),
QuizQuestion(10007, "বাংলা", "“জামা” কোন ধরনের শব্দ?", listOf("নাম-শব্দ","কাজ-শব্দ","সংখ্যা","বিরামচিহ্ন"), 0, "জামা একটি জিনিসের নাম, তাই এটি নাম-শব্দ।", 4, "VerifiedAgentQuestionBank.kt:748; class 4; bangla; page 18"),
QuizQuestion(10008, "ইংরেজি", "She found ___ egg. শূন্যস্থানে কী বসবে?", listOf("a","an","the","কোনোটিই নয়"), 1, "Egg-এর শুরুতে vowel sound বা স্বরধ্বনি আছে, তাই an egg হবে।", 4, "VerifiedAgentQuestionBank.kt:710; class 4; english; page 14"),
QuizQuestion(10009, "গণিত", "৫৩২৪ সংখ্যায় ৩-এর স্থানীয় মান কত?", listOf("৩","৩০","৩০০","৩০০০"), 2, "৩ শতকের ঘরে আছে, তাই স্থানীয় মান ৩০০।", 4, "VerifiedAgentQuestionBank.kt:675; class 4; math; page 2"),
QuizQuestion(10010, "গণিত", "১ অযুত, ৩ হাজার, ৪ শতক, ২ দশক ও ৫ একক মিলে কোন সংখ্যা হয়?", listOf("১৩,২৪৫","১৩,৪২৫","১৪,৩২৫","১০,৩৪৫"), 1, "১০,০০০ + ৩,০০০ + ৪০০ + ২০ + ৫ = ১৩,৪২৫।", 4, "VerifiedAgentQuestionBank.kt:729; class 4; math; page 5"),
QuizQuestion(10011, "বিজ্ঞান", "জীবের কাজ করার শক্তি প্রধানত কোথা থেকে আসে?", listOf("খাদ্য","বালি","পাথর","প্লাস্টিক"), 0, "জীব খাদ্য থেকে পাওয়া শক্তি দিয়ে চলাফেরা, বৃদ্ধি ও শরীরের কাজ করে।", 4, "VerifiedAgentQuestionBank.kt:712; class 4; science; page 11"),
QuizQuestion(10012, "বাংলাদেশ ও বিশ্বপরিচয়", "কোন কাজটি পরিবেশ রক্ষায় সহায়ক?", listOf("নদীতে ময়লা ফেলা","গাছের যত্ন নেওয়া","বন উজাড় করা","পানি দূষিত করা"), 1, "গাছের যত্ন নেওয়া পরিবেশ রক্ষায় সহায়তা করে।", 4, "VerifiedAgentQuestionBank.kt:695; class 4; bgs; page 4"),
QuizQuestion(10013, "বাংলা", "নদী মানুষের কোন কাজে লাগে?", listOf("যাতায়াত, কৃষি ও মাছ ধরা","শুধু খেলাধুলা","শুধু ঘুমানো","কোনো কাজেই নয়"), 0, "নদী যাতায়াত, কৃষি, মাছ ধরা ও জীবিকার সঙ্গে যুক্ত।", 5, "books/class5/bangla/page_014.webp; printed page 5"),
QuizQuestion(10014, "ইংরেজি", "Which word is a collective noun?", listOf("team","run","blue","quickly"), 0, "Team একটি দলের নাম, তাই এটি collective noun।", 5, "books/class5/english/page_015.webp; printed page 9"),
QuizQuestion(10015, "গণিত", "২৪০০ ÷ ৬ কত?", listOf("৪০","৪০০","৪০০০","২৪০"), 1, "২৪০০ ÷ ৬ = ৪০০; যাচাই: ৪০০ × ৬ = ২৪০০।", 5, "books/class5/math/page_012.webp; printed page 6"),
QuizQuestion(10016, "গণিত", "৬৬৭৪ ÷ ২১৪ করলে ভাগশেষ কত?", listOf("৩১","৪০","২১৪","৬৬৩৪"), 1, "২১৪ × ৩১ = ৬৬৩৪ এবং ৬৬৭৪ − ৬৬৩৪ = ৪০।", 5, "books/class5/math/page_016.webp; printed page 9"),
QuizQuestion(10017, "বিজ্ঞান", "আবাসস্থল থেকে একটি জীব কী পায়?", listOf("খাদ্য, পানি ও আশ্রয়","শুধু আলো","শুধু খেলনা","কোনোটিই নয়"), 0, "আবাসস্থল জীবকে খাদ্য, পানি, আশ্রয় ও বাঁচার উপযোগী পরিবেশ দেয়।", 5, "books/class5/science/page_007.webp; printed page 1"),
QuizQuestion(10018, "বাংলাদেশ ও বিশ্বপরিচয়", "সমতাভিত্তিক শিক্ষণ পরিবেশে কারা প্রশ্ন করার সুযোগ পাবে?", listOf("সব শিশু","শুধু ছেলেরা","শুধু মেয়েরা","কেউ নয়"), 0, "প্রত্যেক শিশুর শেখা ও নিজের ভাবনা বলার সমান সুযোগ থাকা উচিত।", 5, "books/class5/bgs/page_019.webp; printed page 14")
 )
 fun forGrade(grade:Int) = questions.filter { it.grade == grade }
}
