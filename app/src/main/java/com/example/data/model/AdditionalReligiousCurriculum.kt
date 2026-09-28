package com.example.data.model

/**
 * Official 2026 NCTB chapter structure for the optional religion books in Classes 3-5.
 *
 * These entries intentionally remain catalog-only until each printed exercise and answer has
 * completed editorial review. A table of contents proves structure, not answer accuracy.
 */
object AdditionalReligiousCurriculum {
    private data class Book(val id: String, val name: String, val chapters: List<String>)

    /**
     * PDF source-page ranges, not the printed folio numbers shown inside the books.
     * FullOfflineQuestionBank and the bundled page assets both use these source-page numbers.
     * The boundaries come from each supplied book's table of contents, translated from printed
     * folio numbers to the PDF source-page numbers used by the bundled assets.
     */
    private val sourcePageRangesByGrade = mapOf(
        3 to mapOf(
            "hindu" to listOf(6..21, 22..34, 35..48, 49..75, 76..94),
            "buddhist" to listOf(7..18, 19..24, 25..30, 31..38, 39..46, 47..54, 55..64, 65..76, 77..94),
            "christian" to listOf(8..27, 28..47, 48..75, 76..95, 96..114)
        ),
        4 to mapOf(
            "hindu" to listOf(6..18, 19..38, 39..60, 61..91, 92..110),
            "buddhist" to listOf(7..27, 28..40, 41..58, 59..81, 82..94),
            "christian" to listOf(8..27, 28..51, 52..93, 94..129, 130..150)
        ),
        5 to mapOf(
            "hindu" to listOf(6..25, 26..45, 46..60, 61..86, 87..102),
            "buddhist" to listOf(7..36, 37..47, 48..71, 72..89, 90..106),
            "christian" to listOf(8..33, 34..61, 62..89, 90..117, 118..150)
        )
    )

    private val booksByGrade = mapOf(
        3 to listOf(
            Book("hindu", "হিন্দুধর্ম ও নৈতিক শিক্ষা", listOf(
                "স্রষ্টা ও সৃষ্টি এবং উপাসনা ও প্রার্থনা",
                "আদর্শ জীবনচরিত",
                "নৈতিক ও মানবিক গুণাবলি",
                "ধর্মগ্রন্থ, পূজা-পার্বণ ও ধর্মীয় উৎসব এবং শান্তিপূর্ণ সহাবস্থান",
                "প্রকৃতি ও পরিবেশ এবং দেশপ্রেম"
            )),
            Book("buddhist", "বৌদ্ধধর্ম ও নৈতিক শিক্ষা", listOf(
                "সিদ্ধার্থ গৌতমের গৃহত্যাগ ও বুদ্ধত্ব লাভ",
                "ত্রিপিটক পরিচয়",
                "বন্দনা",
                "পঞ্চশীল",
                "সংঘদান",
                "আদর্শ জীবনচরিত",
                "পূজা ও উৎসব",
                "তীর্থস্থান",
                "জাতকে জীব ও প্রকৃতি"
            )),
            Book("christian", "খ্রিষ্টধর্ম ও নৈতিক শিক্ষা", listOf(
                "সিনাই পর্বতে ঈশ্বরের দশ আজ্ঞা",
                "যিশুর কর্মজীবন",
                "পরোপকার ও শ্রদ্ধাবোধ",
                "প্রার্থনা ও বিশ্বশান্তি",
                "মানুষ, প্রকৃতি ও জীবজগৎ"
            ))
        ),
        4 to listOf(
            Book("hindu", "হিন্দুধর্ম ও নৈতিক শিক্ষা", listOf(
                "স্রষ্টা ও সৃষ্টি এবং উপাসনা ও প্রার্থনা",
                "আদর্শ জীবনচরিত",
                "নৈতিক ও মানবিক গুণাবলি",
                "ধর্মগ্রন্থ, দেব-দেবী ও পূজা-পার্বণ, যোগব্যায়াম, মন্দির ও তীর্থক্ষেত্র",
                "জীবসেবা ও দেশপ্রেম"
            )),
            Book("buddhist", "বৌদ্ধধর্ম ও নৈতিক শিক্ষা", listOf(
                "গৌতম বুদ্ধের জীবন ও সূত্র পিটক",
                "আদর্শ জীবনচরিত",
                "নীতিশিক্ষা ও কর্ম",
                "পূজা-উৎসব ও ঐতিহাসিক স্থান",
                "জাতকে জীব ও প্রকৃতি"
            )),
            Book("christian", "খ্রিষ্টধর্ম ও নৈতিক শিক্ষা", listOf(
                "ঈশ্বরের দশ আজ্ঞা ও নৈতিক শিক্ষা",
                "যিশু ঈশ্বর ও মানুষ",
                "উদারতা ও ন্যায়-অন্যায়বোধ",
                "শান্তিপূর্ণ সহাবস্থান",
                "জলবায়ু পরিবর্তন ও খ্রিষ্টীয় দায়িত্ব"
            ))
        ),
        5 to listOf(
            Book("hindu", "হিন্দুধর্ম ও নৈতিক শিক্ষা", listOf(
                "সৃষ্টিকর্তা ও ঈশ্বরের স্বরূপ",
                "অবতার ও আদর্শ জীবনচরিত",
                "নৈতিক ও মানবিক গুণাবলি",
                "ধর্মগ্রন্থ, দেব-দেবী ও পূজা, মন্দির ও তীর্থক্ষেত্র",
                "জীবসেবা ও দেশপ্রেম"
            )),
            Book("buddhist", "বৌদ্ধধর্ম ও নৈতিক শিক্ষা", listOf(
                "গৌতম বুদ্ধের মহাজীবন ও শিক্ষা",
                "আদর্শ জীবনচরিত",
                "দশশীল, দান ও কর্মফল",
                "ধর্মীয় আচার-অনুষ্ঠান ও মহাতীর্থ",
                "জাতকে জীব ও প্রকৃতি"
            )),
            Book("christian", "খ্রিষ্টধর্ম ও নৈতিক শিক্ষা", listOf(
                "বিশ্বাসমন্ত্র ও খ্রিষ্টবিশ্বাসী জীবন",
                "যিশু খ্রিষ্টের ক্রুশীয় মৃত্যু ও পুনরুত্থান",
                "পরমতসহিষ্ণুতা ও সম্প্রীতি",
                "সাক্রামেন্ট ও ধর্মীয় মূল্যবোধ",
                "ঈশ্বরসৃষ্ট পৃথিবী আমাদের আবাসভূমি"
            ))
        )
    )

    fun mergeInto(levels: List<CurriculumClassLevel>): List<CurriculumClassLevel> = levels.map { level ->
        val books = booksByGrade[level.levelNumber] ?: return@map level
        val additions = books.map { book ->
            CurriculumSubject(
                id = "c${level.levelNumber}_${book.id}",
                nameBangla = book.name,
                iconName = "religion",
                topics = book.chapters.mapIndexed { index, title ->
                    val reviewedRange = sourcePageRangesByGrade[level.levelNumber]
                        ?.get(book.id)
                        ?.getOrNull(index)
                    val reviewedEntries = reviewedRange?.let { range ->
                        FullOfflineQuestionBank.entries.filter {
                            it.grade == level.levelNumber && it.book == book.id && it.page in range
                        }
                    }.orEmpty()
                    val reviewedQa = reviewedEntries.map { TopicQA(it.question, it.answer) }
                    val reviewedVisuals = reviewedEntries.groupBy { it.page }.map { (page, entries) ->
                        VisualExercise(
                            questionBangla = "পৃষ্ঠা $page-এর প্রশ্নগুলো দেখে উত্তর দাও।",
                            answerBangla = "এই পৃষ্ঠার প্রতিটি প্রশ্নের বইভিত্তিক যাচাইকৃত উত্তর নিচে দেওয়া আছে।",
                            sourceBangla = "২০২৬ NCTB ${book.name}, পৃষ্ঠা $page",
                            imageAssetPath = "books/class${level.levelNumber}/${book.id}/page_${page.toString().padStart(3, '0')}.webp",
                            pageQuestions = entries.map { TopicQA(it.question, it.answer) }
                        )
                    }
                    CurriculumTopic(
                        id = "nctb2026_c${level.levelNumber}_${book.id}_${index + 1}",
                        titleBangla = title,
                        summaryBangla = "২০২৬ সালের ${level.classNameBangla} ${book.name} বইয়ের ${index + 1} নম্বর অধ্যায়।",
                        detailsBangla = if (reviewedQa.isNotEmpty())
                            "এই অধ্যায়ের ${reviewedQa.size}টি প্রশ্ন ও উত্তর সরকারি বইয়ের দৃশ্যমান পাঠের সঙ্গে মিলিয়ে যাচাই করা হয়েছে।"
                        else "অধ্যায়ের ছাপা প্রশ্ন ও উত্তর সম্পাদকীয়ভাবে যাচাই চলছে। যাচাই শেষ না হওয়া পর্যন্ত অনুমানভিত্তিক উত্তর দেখানো হবে না।",
                        learningObjectives = listOf("অধ্যায়ের মূল বিষয় চেনা", "বইয়ের তথ্য থেকে উত্তর খোঁজা", "শ্রদ্ধা ও সহাবস্থানের আচরণ শেখা"),
                        qaList = reviewedQa,
                        visualExercises = reviewedVisuals,
                        parentTeachingTips = listOf("শিশুকে তার নিজের ধর্মীয় বই ও শিক্ষকের নির্দেশনা অনুযায়ী শেখান।", "অন্য ধর্ম ও বিশ্বাস সম্পর্কে সম্মানজনক ভাষা ব্যবহার করতে উৎসাহ দিন।"),
                        catalogOnly = reviewedQa.isEmpty()
                    )
                }
            )
        }
        val additionIds = additions.mapTo(mutableSetOf()) { it.id }
        level.copy(subjects = level.subjects.filterNot { it.id in additionIds } + additions)
    }
}
