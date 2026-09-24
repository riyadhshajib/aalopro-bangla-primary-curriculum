package com.example.ai

import java.util.Locale
import kotlin.math.PI
import kotlin.math.sqrt

/**
 * Offline Computational Math Solver for AloPro.
 * Solves arithmetic, algebra, percentages, geometry, and interest with step-by-step Bangla explanations.
 */
object AloMathSolver {

    data class MathResult(
        val solved: Boolean,
        val title: String,
        val explanationBangla: String
    )

    fun trySolve(input: String): MathResult? {
        val normalized = normalizeBanglaDigits(input.trim().lowercase(Locale.ROOT))

        // Check Linear equation: e.g. "2x + 4 = 10" or "solve 3x - 6 = 15"
        val linearResult = solveLinearEquation(normalized)
        if (linearResult != null) return linearResult

        // Check Quadratic equation: e.g. "x^2 - 5x + 6 = 0"
        val quadResult = solveQuadraticEquation(normalized)
        if (quadResult != null) return quadResult

        // Check LCM / HCF: e.g. "লসাগু 12, 18, 24" or "lcm of 12, 18"
        val lcmGcdResult = solveLcmGcd(normalized)
        if (lcmGcdResult != null) return lcmGcdResult

        // Check Percentage: e.g. "500 এর 15%" or "15% of 500"
        val percentageResult = solvePercentage(normalized)
        if (percentageResult != null) return percentageResult

        // Check Simple Interest (মুনাফা): e.g. "আসল 5000, হার 8%, সময় 3 বছর" or "মুনাফা কত"
        val interestResult = solveInterest(normalized)
        if (interestResult != null) return interestResult

        // Check Geometry: e.g. "বৃত্তের ক্ষেত্রফল ব্যাসার্ধ 7" or "triangle area base 10 height 5"
        val geoResult = solveGeometry(normalized)
        if (geoResult != null) return geoResult

        // Check basic arithmetic expression like "25 * 4 + 50"
        val arithmeticResult = solveArithmetic(normalized)
        if (arithmeticResult != null) return arithmeticResult

        return null
    }

    fun normalizeBanglaDigits(text: String): String {
        val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        var result = text
        for (i in banglaDigits.indices) {
            result = result.replace(banglaDigits[i], englishDigits[i])
        }
        return result
    }

    fun toBanglaDigits(number: Any): String {
        val str = number.toString()
        val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val sb = StringBuilder()
        for (ch in str) {
            if (ch in '0'..'9') {
                sb.append(banglaDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    private fun solveLinearEquation(text: String): MathResult? {
        val clean = text.replace(" ", "")

        // Handle expression ending with "=?" or asking value without right side: e.g. "2x+5=?" or "2x+5"
        val questionPattern = Regex("""([+-]?\s*\d*\.?\d*)\s*x\s*([+-]\s*\d+\.?\d*)?\s*=\s*(?:\?|কত)?""")
        if (clean.contains("=?") || clean.contains("=কত") || (clean.contains("x") && (text.contains("মান কত") || text.contains("x = ?")))) {
            val qMatch = questionPattern.find(clean)
            if (qMatch != null) {
                val (aStr, bStr) = qMatch.destructured
                val a = if (aStr.isEmpty() || aStr == "+") 1.0 else if (aStr == "-") -1.0 else aStr.toDoubleOrNull() ?: 1.0
                val b = if (bStr.isNotEmpty()) bStr.toDoubleOrNull() ?: 0.0 else 0.0
                val aBn = toBanglaDigits(a)
                val bBn = if (b >= 0) "+ " + toBanglaDigits(b) else "- " + toBanglaDigits(-b)

                // If equated to 0 or 15
                val sampleC = 15.0
                val sampleX = (sampleC - b) / a
                val sampleXFormatted = if (sampleX % 1.0 == 0.0) sampleX.toLong().toString() else String.format(Locale.US, "%.2f", sampleX)

                val explanation = """
                    📐 **বীজগণিতীয় রাশি ও সমীকরণ সমাধান:**
                    আপনি জানতে চেয়েছেন: **${aBn}x $bBn = ?**

                    💡 **মূল গাণিতিক ধারণা:**
                    - `${aBn}x $bBn` হলো একটি এক চলকবিশিষ্ট দ্বিপদী বীজগণিতীয় রাশি।
                    - চলক **x** এর নির্দিষ্ট সাংখ্যিক মান নির্ণয় করতে সমান চিহ্নের ডানপাশে একটি নির্দিষ্ট মান (যেমন: ax + b = c) থাকতে হয়।

                    📝 **উদাহরণ ও সমাধান পদ্ধতি:**
                    ১. যদি সমীকরণটি হয় **${aBn}x $bBn = ${toBanglaDigits(sampleC)}**:
                       - পক্ষান্তর করে: ${aBn}x = ${toBanglaDigits(sampleC)} ${if (b >= 0) "- " + toBanglaDigits(b) else "+ " + toBanglaDigits(-b)} = ${toBanglaDigits(sampleC - b)}
                       - অতএব: **x = ${toBanglaDigits(sampleXFormatted)}**

                    ২. যদি সমীকরণটি শূন্যের সমান ধরা হয় (**${aBn}x $bBn = 0**):
                       - ${aBn}x = ${toBanglaDigits(-b)}
                       - অতএব: **x = ${toBanglaDigits(if ((-b / a) % 1.0 == 0.0) (-b / a).toLong().toString() else String.format(Locale.US, "%.2f", -b / a))}**

                    ✨ *টিপস: আপনি সরাসরি পূর্ণ সমীকরণ যেমন `2x + 5 = 15` অথবা `3x - 6 = 12` লিখলে আলোপ্রো তৎক্ষণাৎ ধাপভিত্তিক সমাধান করে দেবে!*
                """.trimIndent()

                return MathResult(
                    solved = true,
                    title = "বীজগণিতীয় সমীকরণ বিশ্লেষণ",
                    explanationBangla = explanation
                )
            }
        }

        // Matches forms like ax + b = c, ax - b = c, or ax = c
        // regex for ax (+/-) b = c
        val pattern = Regex("""(?:solve\s*)?([+-]?\s*\d*\.?\d*)\s*x\s*([+-]\s*\d+\.?\d*)?\s*=\s*([+-]?\s*\d+\.?\d*)""")
        val match = pattern.find(clean) ?: return null

        val (aStr, bStr, cStr) = match.destructured

        var a = when {
            aStr.isEmpty() || aStr == "+" -> 1.0
            aStr == "-" -> -1.0
            else -> aStr.replace(" ", "").toDoubleOrNull() ?: 1.0
        }
        if (a == 0.0) a = 1.0

        val b = if (bStr.isNotEmpty()) bStr.replace(" ", "").toDoubleOrNull() ?: 0.0 else 0.0
        val c = cStr.replace(" ", "").toDoubleOrNull() ?: return null

        // ax + b = c  =>  ax = c - b  =>  x = (c - b) / a
        val rightSide = c - b
        val x = rightSide / a

        val formattedX = if (x % 1.0 == 0.0) x.toLong().toString() else String.format(Locale.US, "%.2f", x)
        val xBn = toBanglaDigits(formattedX)

        val explanation = """
            📐 **সরল সমীকরণ সমাধান:**
            দেওয়া আছে: ${toBanglaDigits(a)}x ${if (b >= 0) "+ " + toBanglaDigits(b) else "- " + toBanglaDigits(-b)} = ${toBanglaDigits(c)}

            **ধাপ ১:** উভয় পাশ থেকে পক্ষান্তর করি:
            ${toBanglaDigits(a)}x = ${toBanglaDigits(c)} ${if (b >= 0) "- " + toBanglaDigits(b) else "+ " + toBanglaDigits(-b)}
            ${toBanglaDigits(a)}x = ${toBanglaDigits(rightSide)}

            **ধাপ ২:** উভয় পক্ষকে ${toBanglaDigits(a)} দ্বারা ভাগ করি:
            x = ${toBanglaDigits(rightSide)} ÷ ${toBanglaDigits(a)}
            **x = $xBn**

            ✅ **নির্ণেয় সমাধান:** x = $xBn
        """.trimIndent()

        return MathResult(
            solved = true,
            title = "সরল সমীকরণ সমাধান",
            explanationBangla = explanation
        )
    }

    private fun solveQuadraticEquation(text: String): MathResult? {
        // e.g. x^2 - 5x + 6 = 0 or 2x^2 + 3x - 2 = 0
        if (!text.contains("x^2") && !text.contains("x²")) return null

        val clean = text.replace("x²", "x^2").replace(" ", "")
        val regex = Regex("""([+-]?\d*\.?\d*)x\^2([+-]\d*\.?\d*)x([+-]\d+\.?\d*)=0""")
        val match = regex.find(clean) ?: return null

        val (aStr, bStr, cStr) = match.destructured
        var a = when {
            aStr.isEmpty() || aStr == "+" -> 1.0
            aStr == "-" -> -1.0
            else -> aStr.toDoubleOrNull() ?: 1.0
        }
        val b = when {
            bStr == "+" -> 1.0
            bStr == "-" -> -1.0
            else -> bStr.toDoubleOrNull() ?: 0.0
        }
        val c = cStr.toDoubleOrNull() ?: 0.0

        val d = (b * b) - (4 * a * c)

        val explanation = if (d < 0) {
            """
                📐 **দ্বিঘাত সমীকরণ সমাধান:**
                সমীকরণ: ${toBanglaDigits(a)}x² + (${toBanglaDigits(b)})x + (${toBanglaDigits(c)}) = 0
                
                **ধাপ ১ (নিশ্চায়ক D নির্ণয়):**
                D = b² - 4ac
                D = (${toBanglaDigits(b)})² - 4 × (${toBanglaDigits(a)}) × (${toBanglaDigits(c)})
                D = ${toBanglaDigits(d)}
                
                যেহেতু D < 0, তাই সমীকরণটির কোনো বাস্তব মূল নেই (জটিল বা কাল্পনিক মূল বিদ্যমান)।
            """.trimIndent()
        } else {
            val root1 = (-b + sqrt(d)) / (2 * a)
            val root2 = (-b - sqrt(d)) / (2 * a)
            val r1Str = if (root1 % 1.0 == 0.0) root1.toLong().toString() else String.format(Locale.US, "%.2f", root1)
            val r2Str = if (root2 % 1.0 == 0.0) root2.toLong().toString() else String.format(Locale.US, "%.2f", root2)

            """
                📐 **দ্বিঘাত সমীকরণ সমাধান:**
                সমীকরণ: ${toBanglaDigits(a)}x² + (${toBanglaDigits(b)})x + (${toBanglaDigits(c)}) = 0
                
                **ধাপ ১ (নিশ্চায়ক নির্ণয়):**
                D = b² - 4ac
                D = (${toBanglaDigits(b)})² - 4 × (${toBanglaDigits(a)}) × (${toBanglaDigits(c)}) = ${toBanglaDigits(d)}
                
                **ধাপ ২ (শ্রীধর আচার্যের দ্বিঘাত সূত্র প্রয়োগ):**
                x = (-b ± √D) / 2a
                x₁ = (-(${toBanglaDigits(b)}) + √${toBanglaDigits(d)}) / (2 × ${toBanglaDigits(a)}) = ${toBanglaDigits(r1Str)}
                x₂ = (-(${toBanglaDigits(b)}) - √${toBanglaDigits(d)}) / (2 × ${toBanglaDigits(a)}) = ${toBanglaDigits(r2Str)}
                
                ✅ **নির্ণেয় মূলদ্বয়:** x = ${toBanglaDigits(r1Str)}, ${toBanglaDigits(r2Str)}
            """.trimIndent()
        }

        return MathResult(
            solved = true,
            title = "দ্বিঘাত সমীকরণ সমাধান",
            explanationBangla = explanation
        )
    }

    private fun solveLcmGcd(text: String): MathResult? {
        val isLcm = text.contains("লসাগু") || text.contains("lcm")
        val isGcd = text.contains("গসাগু") || text.contains("gcd") || text.contains("hcf")
        if (!isLcm && !isGcd) return null

        val numbers = Regex("""\d+""").findAll(text).map { it.value.toLong() }.toList()
        if (numbers.size < 2) return null

        fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
        fun lcm(a: Long, b: Long): Long = if (a == 0L || b == 0L) 0L else (a * b) / gcd(a, b)

        var ansGcd = numbers[0]
        var ansLcm = numbers[0]
        for (i in 1 until numbers.size) {
            ansGcd = gcd(ansGcd, numbers[i])
            ansLcm = lcm(ansLcm, numbers[i])
        }

        val numStr = numbers.joinToString(", ") { toBanglaDigits(it) }

        val explanation = if (isLcm && !isGcd) {
            """
                🔢 **লঘিষ্ঠ সাধারণ গুণিতক (ল.সা.গু):**
                সংখ্যাগুলো: $numStr
                
                **ব্যাখ্যা:**
                ল.সা.গু হলো এমন ক্ষুদ্রতম সংখ্যা যাকে প্রদত্ত সংখ্যাগুলো দ্বারা নিঃশেষে ভাগ করা যায়।
                
                ✅ **নির্ণেয় ল.সা.গু = ${toBanglaDigits(ansLcm)}**
                (অতিরিক্ত তথ্য: এই সংখ্যাগুলোর গ.সা.গু = ${toBanglaDigits(ansGcd)})
            """.trimIndent()
        } else {
            """
                🔢 **গরিষ্ঠ সাধারণ গুণনীয়ক (গ.সা.গু):**
                সংখ্যাগুলো: $numStr
                
                **ব্যাখ্যা:**
                গ.সা.গু হলো এমন বৃহত্তম সংখ্যা যা দ্বারা প্রদত্ত সংখ্যাগুলোকে নিঃশেষে ভাগ করা যায়।
                
                ✅ **নির্ণেয় গ.সা.গু = ${toBanglaDigits(ansGcd)}**
                (অতিরিক্ত তথ্য: এই সংখ্যাগুলোর ল.সা.গু = ${toBanglaDigits(ansLcm)})
            """.trimIndent()
        }

        return MathResult(
            solved = true,
            title = if (isLcm && !isGcd) "ল.সা.গু নির্ণয়" else "গ.সা.গু নির্ণয়",
            explanationBangla = explanation
        )
    }

    private fun solvePercentage(text: String): MathResult? {
        // e.g. "500 এর 15%" or "15% of 500" or "800 টাকার 25% কত"
        val pattern1 = Regex("""(\d+\.?\d*)\s*(?:টাকার|এর)?\s*(\d+\.?\d*)\s*%""")
        val pattern2 = Regex("""(\d+\.?\d*)\s*%\s*(?:of|এর)?\s*(\d+\.?\d*)""")

        val match = pattern1.find(text) ?: pattern2.find(text) ?: return null
        val num1 = match.groupValues[1].toDoubleOrNull() ?: return null
        val num2 = match.groupValues[2].toDoubleOrNull() ?: return null

        val (total, percent) = if (text.contains(match.groupValues[2] + "%")) {
            Pair(num1, num2)
        } else {
            Pair(num2, num1)
        }

        val result = (total * percent) / 100.0
        val resFormatted = if (result % 1.0 == 0.0) result.toLong().toString() else String.format(Locale.US, "%.2f", result)

        val explanation = """
            📊 **শতকরা (Percentage) হিসাব:**
            দেওয়া আছে: মোট মান = ${toBanglaDigits(total)}, শতকরা হার = ${toBanglaDigits(percent)}%
            
            **সূত্র:**
            মান = (মোট মান × শতকরা হার) ÷ ১০০
            
            **হিসাব:**
            = (${toBanglaDigits(total)} × ${toBanglaDigits(percent)}) ÷ ১০০
            = ${toBanglaDigits(total * percent)} ÷ ১০০
            = ${toBanglaDigits(resFormatted)}
            
            ✅ **উত্তর:** ${toBanglaDigits(total)} এর ${toBanglaDigits(percent)}% হলো **${toBanglaDigits(resFormatted)}**
        """.trimIndent()

        return MathResult(
            solved = true,
            title = "শতকরা নির্ণয়",
            explanationBangla = explanation
        )
    }

    private fun solveInterest(text: String): MathResult? {
        // Simple interest I = P * r * n
        if (!text.contains("সুদ") && !text.contains("মুনাফা") && !text.contains("interest")) return null

        val numbers = Regex("""\d+\.?\d*""").findAll(text).map { it.value.toDouble() }.toList()
        if (numbers.size < 3) return null

        // Assume first large number is Principal (P), second is rate (r), third is time in years (n)
        val sorted = numbers.sortedDescending()
        val p = sorted[0]
        val r = sorted.firstOrNull { it in 1.0..50.0 && it != p } ?: 8.0
        val n = sorted.firstOrNull { it in 1.0..30.0 && it != p && it != r } ?: 1.0

        val interest = (p * r * n) / 100.0
        val totalAmount = p + interest

        val explanation = """
            💰 **সরল মুনাফা (Simple Interest) নির্ণয়:**
            দেওয়া আছে:
            - আসল (P) = ${toBanglaDigits(p.toLong())} টাকা
            - মুনাফার হার (r) = ${toBanglaDigits(r)}%
            - সময় (n) = ${toBanglaDigits(n.toLong())} বছর
            
            **সূত্র:**
            মুনাফা (I) = P × r × n ÷ ১০০
            
            **হিসাব:**
            I = (${toBanglaDigits(p.toLong())} × ${toBanglaDigits(r)} × ${toBanglaDigits(n.toLong())}) ÷ ১০০
            I = ${toBanglaDigits(interest.toLong())} টাকা
            
            **মুনাফা-আসল (A) = আসল + মুনাফা**
            = ${toBanglaDigits(p.toLong())} + ${toBanglaDigits(interest.toLong())}
            = **${toBanglaDigits(totalAmount.toLong())} টাকা**
            
            ✅ **মোট মুনাফা:** ${toBanglaDigits(interest.toLong())} টাকা
        """.trimIndent()

        return MathResult(
            solved = true,
            title = "সরল মুনাফা নির্ণয়",
            explanationBangla = explanation
        )
    }

    private fun solveGeometry(text: String): MathResult? {
        val num = Regex("""\d+\.?\d*""").find(text)?.value?.toDoubleOrNull()

        if ((text.contains("বৃত্ত") || text.contains("circle")) && num != null) {
            val r = num
            val area = PI * r * r
            val circum = 2 * PI * r
            val areaStr = String.format(Locale.US, "%.2f", area)
            val circumStr = String.format(Locale.US, "%.2f", circum)

            val explanation = """
                ⭕ **বৃত্তের পরিমিতি সমাধান:**
                দেওয়া ব্যাসার্ধ (r) = ${toBanglaDigits(r)} একক
                
                **১. ক্ষেত্রফল (Area):**
                সূত্র: A = πr²
                = ৩.১৪১৬ × (${toBanglaDigits(r)})²
                = **${toBanglaDigits(areaStr)} বর্গ একক**
                
                **২. পরিধি (Circumference):**
                সূত্র: C = 2πr
                = ২ × ৩.১৪১৬ × ${toBanglaDigits(r)}
                = **${toBanglaDigits(circumStr)} একক**
                
                ✅ **উত্তর:** ক্ষেত্রফল ≈ ${toBanglaDigits(areaStr)} বর্গ একক
            """.trimIndent()

            return MathResult(
                solved = true,
                title = "বৃত্তের ক্ষেত্রফল ও পরিধি",
                explanationBangla = explanation
            )
        }

        if (text.contains("পিথাগোরাস") || text.contains("pythagoras")) {
            val numbers = Regex("""\d+\.?\d*""").findAll(text).map { it.value.toDouble() }.toList()
            if (numbers.size >= 2) {
                val a = numbers[0]
                val b = numbers[1]
                val c = sqrt(a * a + b * b)
                val cStr = if (c % 1.0 == 0.0) c.toLong().toString() else String.format(Locale.US, "%.2f", c)

                val explanation = """
                    📐 **পিথাগোরাসের উপপাদ্য সমাধান:**
                    সমকোণী ত্রিভুজের লম্ব ও ভূমি দেওয়া আছে: a = ${toBanglaDigits(a)}, b = ${toBanglaDigits(b)}
                    
                    **সূত্র:**
                    অতিভুজ² = লম্ব² + ভূমি²
                    c² = a² + b²
                    c = √(a² + b²)
                    c = √(${toBanglaDigits(a)}² + ${toBanglaDigits(b)}²)
                    c = √(${toBanglaDigits(a * a + b * b)})
                    c = **${toBanglaDigits(cStr)}**
                    
                    ✅ **অতিভুজের দৈর্ঘ্য:** ${toBanglaDigits(cStr)} একক
                """.trimIndent()

                return MathResult(
                    solved = true,
                    title = "পিথাগোরাসের উপপাদ্য",
                    explanationBangla = explanation
                )
            }
        }

        return null
    }

    private fun solveArithmetic(text: String): MathResult? {
        val clean = text.replace(Regex("\\s+"), "")
            .replace('×', '*').replace('÷', '/')
        if (!clean.matches(Regex("[0-9.+*/^()\\-]+")) ||
            clean.none { it in "+-*/^" }) return null
        val steps = mutableListOf<String>()
        val answer = try { ArithmeticParser(clean, steps).parse() } catch (_: IllegalArgumentException) { return null }
        if (!answer.isFinite()) return null
        val displayExpression = clean.replace('*', '×').replace('/', '÷')
        val explanation = buildString {
            append("🧮 **পাটিগণিত গণনা:**\n")
            append("হিসাব: ").append(toBanglaDigits(displayExpression)).append("\n\n")
            if (steps.isNotEmpty()) {
                append("ধাপগুলো:\n")
                steps.forEachIndexed { index, step -> append(index + 1).append(". ").append(toBanglaDigits(step)).append("\n") }
                append("\n")
            }
            append("✅ **ফলাফল = ").append(toBanglaDigits(formatArithmetic(answer))).append("**")
        }

        return MathResult(
            solved = true,
            title = "পাটিগণিত গণনা",
            explanationBangla = explanation
        )
    }

    private fun formatArithmetic(value: Double): String =
        if (value % 1.0 == 0.0 && value <= Long.MAX_VALUE && value >= Long.MIN_VALUE) {
            value.toLong().toString()
        } else String.format(Locale.US, "%.4f", value).trimEnd('0').trimEnd('.')

    private class ArithmeticParser(
        private val source: String,
        private val steps: MutableList<String>
    ) {
        private var position = 0

        fun parse(): Double {
            val value = addSub()
            require(position == source.length)
            return value
        }

        private fun addSub(): Double {
            var left = mulDiv()
            while (peek() == '+' || peek() == '-') {
                val op = source[position++]
                val right = mulDiv()
                val result = if (op == '+') left + right else left - right
                record(left, op, right, result)
                left = result
            }
            return left
        }

        private fun mulDiv(): Double {
            var left = unary()
            while (peek() == '*' || peek() == '/') {
                val op = source[position++]
                val right = unary()
                require(op != '/' || right != 0.0)
                val result = if (op == '*') left * right else left / right
                record(left, op, right, result)
                left = result
            }
            return left
        }

        private fun power(): Double {
            val left = primary()
            if (peek() != '^') return left
            position++
            val right = unary()
            val result = Math.pow(left, right)
            record(left, '^', right, result)
            return result
        }

        private fun unary(): Double = when (peek()) {
            '+' -> { position++; unary() }
            '-' -> { position++; -unary() }
            else -> power()
        }

        private fun primary(): Double {
            if (peek() == '(') {
                position++
                val value = addSub()
                require(peek() == ')')
                position++
                return value
            }
            val start = position
            while (peek()?.isDigit() == true || peek() == '.') position++
            require(position > start)
            return source.substring(start, position).toDoubleOrNull()
                ?: throw IllegalArgumentException("Invalid number")
        }

        private fun peek(): Char? = source.getOrNull(position)

        private fun record(left: Double, operator: Char, right: Double, result: Double) {
            require(result.isFinite())
            val symbol = when (operator) { '*' -> '×'; '/' -> '÷'; else -> operator }
            steps += "${formatArithmetic(left)} $symbol ${formatArithmetic(right)} = ${formatArithmetic(result)}"
        }
    }
}
