package com.example.ai

/** Deterministic, offline Class One maths answers (2026 NCTB scope, 0–100). */
object Class1MathAnswerEngine {
    private val chapters = setOf(
        "তুলনা করি", "গণনা", "সংখ্যা (১ থেকে ১০)", "যোগের ধারণা", "বিয়োগের ধারণা",
        "সংখ্যা ১১ থেকে ২০", "যোগ (১১ থেকে ২০)", "বিয়োগ (১১ থেকে ২০)",
        "সংখ্যা (২১ থেকে ৪০)", "স্থানীয় মান", "নিজে করি (১)", "জ্যামিতি", "প্যাটার্ন",
        "সংখ্যা (৪১ থেকে ১০০)", "যোগ", "বিয়োগ", "বাংলাদেশি মুদ্রা", "নিজে করি (২)"
    )
    private val bnDigits = "০১২৩৪৫৬৭৮৯"

    fun supports(chapter: String) = chapter in chapters

    fun answer(chapter: String, rawQuery: String): String? {
        if (!supports(chapter)) return null
        val query = normalize(rawQuery)
        arithmetic(query)?.let { return it }
        placeValue(query)?.let { return it }
        comparison(query)?.let { return it }
        sequence(query)?.let { return it }
        geometry(query)?.let { return it }
        currency(query)?.let { return it }
        return null
    }

    private fun arithmetic(q: String): String? {
        val match = Regex("(\\d{1,3})\\s*([+\\-])\\s*(\\d{1,3})")
            .find(q) ?: return null
        val a = match.groupValues[1].toIntOrNull() ?: return null
        val op = match.groupValues[2]
        val b = match.groupValues[3].toIntOrNull() ?: return null
        if (a !in 0..100 || b !in 0..100) return null
        return if (op == "+") {
            val result = a + b
            if (result > 100) return null
            val method = if (a < 20 && b <= 10) {
                val steps = (1..b).map { a + it }.joinToString(", ") { bn(it) }
                if (steps.isBlank()) "কিছু যোগ হয়নি।" else "${bn(a)}-এর পর ${bn(b)} ধাপ সামনে গুনি: $steps।"
            } else {
                "দশক: ${bn(a / 10 * 10)} + ${bn(b / 10 * 10)} = ${bn((a / 10 + b / 10) * 10)}; একক দুটিও যোগ করি।"
            }
            "$method তাই ${bn(a)} + ${bn(b)} = ${bn(result)}।"
        } else {
            if (b > a) return "প্রথম শ্রেণির এই পাঠে বড় সংখ্যা থেকে ছোট সংখ্যা বিয়োগ করি। এখানে ${bn(b)} হলো ${bn(a)}-এর চেয়ে বড়; সংখ্যা দুটো আবার দেখো।"
            val result = a - b
            val steps = if (a <= 20 && b <= 10) (1..b).map { a - it }.joinToString(", ") { bn(it) } else ""
            val method = if (steps.isNotBlank()) "${bn(a)} থেকে ${bn(b)} ধাপ পেছাই: $steps। " else "একক থেকে একক ও দশক থেকে দশক বাদ দিই। "
            "${method}তাই ${bn(a)} − ${bn(b)} = ${bn(result)}। যাচাই: ${bn(result)} + ${bn(b)} = ${bn(a)}।"
        }
    }

    private fun placeValue(q: String): String? {
        if (listOf("স্থানীয় মান", "স্থানীয় মান", "দশক", "একক", "sthanio man", "doshok", "ekok").none(q::contains)) return null
        val n = Regex("\\d{2,3}").find(q)?.value?.toIntOrNull() ?: return null
        if (n !in 10..100) return null
        if (n == 100) return "১০০ হলো ১০টি দশক। এটি ১ শতকও বলা হয়।"
        val tens = n / 10
        val ones = n % 10
        return "${bn(n)}-এ ${bn(tens)} দশক ও ${bn(ones)} একক। তাই ${bn(n)} = ${bn(tens * 10)} + ${bn(ones)}।"
    }

    private fun comparison(q: String): String? {
        if (listOf("বড়", "ছোট", "বেশি", "কম", "তুলনা", "boro", "choto", "beshi", "kom").none(q::contains)) return null
        val values = Regex("\\d{1,3}").findAll(q).mapNotNull { it.value.toIntOrNull() }.take(2).toList()
        if (values.size != 2 || values.any { it !in 0..100 }) return null
        val (a, b) = values
        return when {
            a > b -> "${bn(a)} বড় এবং ${bn(b)} ছোট। কারণ ${bn(a)} সংখ্যাটি সংখ্যার রেখায় ${bn(b)}-এর পরে আসে। তাই ${bn(a)} > ${bn(b)}।"
            a < b -> "${bn(b)} বড় এবং ${bn(a)} ছোট। কারণ ${bn(b)} সংখ্যাটি সংখ্যার রেখায় ${bn(a)}-এর পরে আসে। তাই ${bn(a)} < ${bn(b)}।"
            else -> "${bn(a)} ও ${bn(b)} সমান। তাই ${bn(a)} = ${bn(b)}।"
        }
    }

    private fun sequence(q: String): String? {
        val number = Regex("\\d{1,3}").find(q)?.value?.toIntOrNull() ?: return null
        if (number !in 0..100) return null
        return when {
            listOf("আগে", "আগের", "before", "ager").any(q::contains) && number > 0 -> "${bn(number)}-এর আগের সংখ্যা ${bn(number - 1)}।"
            listOf("পরে", "পরের", "after", "porer").any(q::contains) && number < 100 -> "${bn(number)}-এর পরের সংখ্যা ${bn(number + 1)}।"
            else -> null
        }
    }

    private fun geometry(q: String): String? = when {
        "ত্রিভুজ" in q || "triangle" in q -> "ত্রিভুজের ৩টি সোজা বাহু ও ৩টি কোণ। ঘুরিয়ে রাখলেও এটি ত্রিভুজই থাকে।"
        "বর্গ" in q || "square" in q -> "বর্গের ৪টি সমান বাহু ও ৪টি কোণ।"
        "আয়তক্ষেত্র" in q || "আয়তক্ষেত্র" in q || "rectangle" in q -> "আয়তক্ষেত্রের ৪টি বাহু ও ৪টি কোণ; বিপরীত বাহু দুটি সমান।"
        "বৃত্ত" in q || "circle" in q -> "বৃত্ত গোল; এর কোনো সোজা বাহু বা কোণ নেই।"
        else -> null
    }

    private fun currency(q: String): String? {
        if (listOf("টাকা", "৳", "মূল্য", "দাম", "coin", "note", "taka").none(q::contains)) return null
        val values = Regex("\\d{1,3}").findAll(q).mapNotNull { it.value.toIntOrNull() }.toList()
        if (values.isEmpty() || values.any { it !in 0..100 }) return null
        if (values.size == 1) return "এটি ${bn(values.first())} টাকা। নোট বা কয়েনে লেখা সংখ্যাই তার মূল্য; শুধু কয়েন বা নোটের সংখ্যা গুনবে না।"
        val total = values.sum()
        if (total > 100) return null
        return "মূল্যগুলো যোগ করি: ${values.joinToString(" + ") { bn(it) }} = ${bn(total)} টাকা।"
    }

    private fun normalize(text: String): String = buildString {
        text.lowercase().forEach { c ->
            val index = bnDigits.indexOf(c)
            append(if (index >= 0) ('0'.code + index).toChar() else c)
        }
    }.replace('–', '-').replace('−', '-').trim()

    private fun bn(number: Int): String = number.toString().map { digit ->
        if (digit in '0'..'9') bnDigits[digit - '0'] else digit
    }.joinToString("")
}
