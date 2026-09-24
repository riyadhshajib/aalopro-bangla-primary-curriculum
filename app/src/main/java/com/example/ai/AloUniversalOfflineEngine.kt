package com.example.ai

import com.example.util.AloTextSanitizer
import java.util.Locale

/**
 * Universal Offline AI Engine for General Chat.
 * 100% Offline academic & educational guidance in natural, clean, structured text.
 * No decorative symbols, asterisks, or emoji clutter.
 */
object AloUniversalOfflineEngine {

    data class UniversalResult(
        val responseText: String,
        val detectedLanguage: String,
        val subjectTag: String
    )

    fun generateUniversalResponse(query: String): UniversalResult {
        val q = query.trim()
        val qLower = q.lowercase(Locale.ROOT)

        val isEnglish = isPrimarilyEnglish(q)
        val isArabic = isArabicScript(q)
        val isSpanish = isSpanishText(qLower)
        val isHindi = isHindiScript(q)

        val rawResult = when {
            isArabic -> handleArabicQuery(q)
            isSpanish -> handleSpanishQuery(qLower, q)
            isHindi -> handleHindiQuery(q)
            isEnglish -> handleEnglishQuery(qLower, q)
            else -> handleBanglaUniversalQuery(qLower, q)
        }

        // Clean out any leftover markdown asterisks or clutter
        val cleanedText = AloTextSanitizer.cleanText(rawResult.responseText)
        return rawResult.copy(responseText = cleanedText)
    }

    private fun isPrimarilyEnglish(text: String): Boolean {
        var asciiCount = 0
        var totalLetters = 0
        for (char in text) {
            if (char.isLetter()) {
                totalLetters++
                if (char.code in 65..90 || char.code in 97..122) {
                    asciiCount++
                }
            }
        }
        return totalLetters > 0 && (asciiCount.toDouble() / totalLetters) > 0.65
    }

    private fun isArabicScript(text: String): Boolean {
        return text.any { char ->
            val block = Character.UnicodeBlock.of(char)
            block == Character.UnicodeBlock.ARABIC || block == Character.UnicodeBlock.ARABIC_SUPPLEMENT
        }
    }

    private fun isHindiScript(text: String): Boolean {
        return text.any { char ->
            val block = Character.UnicodeBlock.of(char)
            block == Character.UnicodeBlock.DEVANAGARI
        }
    }

    private fun isSpanishText(qLower: String): Boolean {
        val spanishKeywords = listOf("por qué", "como", "cómo", "que es", "qué es", "explicar", "fotosíntesis", "relatividad", "gravedad", "célula", "hola")
        return spanishKeywords.any { qLower.contains(it) }
    }

    // --- English Universal Handler ---
    private fun handleEnglishQuery(qLower: String, originalQuery: String): UniversalResult {
        if (qLower.contains("geo politic") || qLower.contains("geopolitic") || qLower.contains("international relation")) {
            return UniversalResult(
                responseText = """
                Geopolitics & International Relations
                Topic: $originalQuery

                1. Definition & Core Concept:
                Geopolitics is the study of how geographical factors (physical terrain, maritime waterways, borders, natural resources, and strategic depth) govern foreign policy, national sovereignty, and international power balance.

                2. Fundamental Pillars:
                • Maritime Choke Points: Critical passages such as the Strait of Malacca, Strait of Hormuz, Suez Canal, and Bab el-Mandeb that dictate global commerce and energy security.
                • Resource Geopolitics: Strategic access to hydrocarbons, critical rare earth minerals, fresh water, and semiconductor supply chains.
                • Alliances & Deterrence: Multilateral defense blocs (NATO, QUAD, AUKUS, BRICS, ASEAN) and spheres of influence.

                3. Major Theories:
                • Mackinder's Heartland Theory: Control of the Eurasian landmass enables dominance over global affairs.
                • Mahan's Sea Power Theory: Command of the oceans and maritime trade corridors guarantees geopolitical supremacy.
                • Spykman's Rimland Theory: The littoral coastal belts of Eurasia represent the pivotal balance of global power.
                """.trimIndent(),
                detectedLanguage = "English",
                subjectTag = "Geopolitics"
            )
        }

        if (qLower.contains("quantum") || qLower.contains("qubit") || qLower.contains("superposition")) {
            return UniversalResult(
                responseText = """
                Quantum Computing & Modern Physics
                Topic: $originalQuery

                Core Concept:
                Quantum computing leverages the fundamental principles of quantum mechanics—specifically Superposition and Entanglement—to process complex information exponentially faster than classical computers.

                Key Fundamentals:
                1. Qubits (Quantum Bits): Unlike classical bits (0 or 1), a qubit can exist in a linear combination of states |0⟩ and |1⟩ simultaneously.
                2. Superposition: Enables parallel computation across 2^n states for n qubits.
                3. Entanglement: Quantum correlation where the state of one particle instantly influences another, regardless of distance.
                4. Quantum Supremacy: The threshold where a quantum computer solves a problem impossible for the world's fastest supercomputers.

                Major Applications:
                • Cryptography (Shor's Algorithm breaking RSA encryption).
                • Molecular simulation for drug discovery and material design.
                • Financial modeling and combinatorial optimization.

                Study Tip:
                Focus on the distinction between deterministic classical logic gates (AND, OR) and unitary quantum gates (Hadamard, CNOT).
                """.trimIndent(),
                detectedLanguage = "English",
                subjectTag = "Quantum Computing"
            )
        }

        if (qLower.contains("calculus") || qLower.contains("derivative") || qLower.contains("integration") || qLower.contains("limit")) {
            return UniversalResult(
                responseText = """
                Advanced Mathematics: Calculus & Mathematical Analysis
                Topic: $originalQuery

                Core Concept:
                Calculus is the mathematical framework for analyzing continuous change. It rests on two fundamental operations:
                1. Differential Calculus: Measures the instantaneous rate of change (slope of tangents).
                2. Integral Calculus: Measures the accumulation of quantities (area under curves).
                These two branches are connected through the Fundamental Theorem of Calculus.

                Key Rules & Formulas:
                • Power Rule: d/dx [x^n] = n * x^(n-1)
                • Chain Rule: d/dx [f(g(x))] = f'(g(x)) * g'(x)
                • Product Rule: d/dx [u * v] = u'v + uv'
                • Integration by Parts: ∫ u dv = uv - ∫ v du

                Practical Application:
                Speed is the derivative of position with respect to time. Conversely, total distance traveled is the integral of speed over time.

                Exam Strategy:
                Always verify boundary conditions when evaluating definite integrals, and remember the integration constant (+C) for indefinite integrals.
                """.trimIndent(),
                detectedLanguage = "English",
                subjectTag = "Higher Math"
            )
        }

        if (qLower.contains("python") || qLower.contains("programming") || qLower.contains("algorithm") || qLower.contains("recursion")) {
            return UniversalResult(
                responseText = """
                Computer Science & Programming
                Topic: $originalQuery

                Core Concept:
                An algorithm is a finite sequence of unambiguous instructions for solving a problem. Code efficiency is measured through Time and Space Complexity (Big-O notation).

                Fundamental Principles:
                • Recursion: When a function solves a problem by calling smaller instances of itself until reaching a base condition.
                • Stack Frame: Each recursive call adds a memory frame to the call stack until the base case returns.
                • Big-O Complexity Hierarchy: O(1) < O(log n) < O(n) < O(n log n) < O(n^2).

                Best Practice:
                Always specify an explicit base case in recursive functions to avoid infinite loops and stack overflow errors.
                """.trimIndent(),
                detectedLanguage = "English",
                subjectTag = "Computer Science"
            )
        }

        // Standard English Academic Fallback
        return UniversalResult(
            responseText = """
            Academic Subject Overview
            Question: $originalQuery

            Core Explanation:
            1. Conceptual Definition: Clarifying the fundamental principles and scope of this topic.
            2. Underlying Mechanics: Examining the causes, logical progression, and relationships between components.
            3. Classification & Structure: Organizing the ideas into logical, easy-to-follow categories.

            Practical Context:
            Understanding the basic rules allows you to solve advanced theoretical and numerical questions easily.

            Study Guidance:
            • Focus on core definitions first before memorizing complex details.
            • Summarize the steps in your own words to ensure complete retention.
            """.trimIndent(),
            detectedLanguage = "English",
            subjectTag = "Universal AI"
        )
    }

    // --- Arabic Handler ---
    private fun handleArabicQuery(query: String): UniversalResult {
        return UniversalResult(
            responseText = """
            المعلم الذكي الشامل
            السؤال: $query

            المفهوم الأساسي والملخص:
            أهلاً بك! هذا السؤال يتناول مفهوماً علمياً ودراسياً هاماً. سنقوم بشرحه بشكل تحليلي منظم:

            التحليل العلمي والتفصيلي:
            ١. التعريف العلمي: تحديد المفهوم بدقة وفقاً للقوانين العلمية والأكاديمية المعتمدة.
            ٢. الآلية والأسباب: كيف ولماذا تحدث هذه الظاهرة؟ دراسة العلاقات السببية والنتائج.
            ٣. التطبيقات العملية: ربط هذا المفهوم بالواقع العملي والمسائل الدراسية.

            نصيحة دراسية:
            ركز دائماً على فهم القواعد الأساسية وخطوات الحل بدلاً من الحفظ المجرد لضمان التفوق في الامتحانات.
            """.trimIndent(),
            detectedLanguage = "العربية",
            subjectTag = "التعليم العام"
        )
    }

    // --- Spanish Handler ---
    private fun handleSpanishQuery(qLower: String, originalQuery: String): UniversalResult {
        return UniversalResult(
            responseText = """
            Tutor Global de Aprendizaje
            Consulta: $originalQuery

            Concepto Principal:
            Esta pregunta aborda un concepto académico fundamental. A continuación presentamos una explicación analítica paso a paso:

            Desglose Paso a Paso:
            1. Definición Teórica: Comprensión de los principios fundamentales que rigen este tema.
            2. Causa y Efecto: Análisis de los mecanismos de acción y las variables involucradas.
            3. Aplicación Práctica: Cómo se manifiesta este principio en el mundo real y en la resolución de problemas.

            Consejo de Estudio:
            Comprende los fundamentos paso a paso para dominar exámenes y proyectos académicos.
            """.trimIndent(),
            detectedLanguage = "Español",
            subjectTag = "Educación General"
        )
    }

    // --- Hindi Handler ---
    private fun handleHindiQuery(query: String): UniversalResult {
        return UniversalResult(
            responseText = """
            आलो ग्लोबल एआई शिक्षक
            प्रश्न: $query

            मुख्य अवधारणा एवं सारांश:
            यहाँ इस शैक्षणिक विषय का सरल एवं स्पष्ट विवरण दिया गया है:

            गहन विश्लेषणात्मक व्याख्या:
            १. मूल परिभाषा: विषय के आधारभूत नियमों और सिद्धांतों का स्पष्ट परिचय।
            २. कारण एवं प्रभाव: यह घटना या नियम किस प्रकार कार्य करता है और इसके वैज्ञानिक कारण क्या हैं।
            ३. व्यावहारिक अनुप्रयोग: वास्तविक जीवन में इसका उपयोग और महत्व।

            परीक्षा उपयोगी सुझाव:
            रटने के स्थान पर अवधारणा के "क्यों और कैसे" को समझें।
            """.trimIndent(),
            detectedLanguage = "हिन्दी",
            subjectTag = "ग्लोबल एजुकेशन"
        )
    }

    // --- Bangla Universal Handler (Clean, Structured, Professional) ---
    private fun handleBanglaUniversalQuery(qLower: String, originalQuery: String): UniversalResult {
        // University / BCS / Competitive Exams / Programming
        if (qLower.contains("বিশ্ববিদ্যালয়") || qLower.contains("university") || qLower.contains("কোডিং") || qLower.contains("প্রোগ্রামিং") || qLower.contains("বিসিএস") || qLower.contains("আইইএলটিএস") || qLower.contains("ielts")) {
            return UniversalResult(
                responseText = """
                উচ্চশিক্ষা ও প্রতিযোগিতামূলক প্রস্তুতি
                বিষয়: $originalQuery

                মূল ধারণা ও সারসংক্ষেপ:
                উচ্চশিক্ষা এবং প্রতিযোগিতামূলক পরীক্ষার ক্ষেত্রে তাত্ত্বিক স্পষ্টতা এবং নিয়মিত অনুশীলনের ধারাবাহিকতা সবচেয়ে গুরুত্বপূর্ণ উপাদান।

                প্রস্তুতির মূল ধাপসমূহ:
                ১. মৌলিক ভিত্তি মজবুত করা: সংশ্লিষ্ট বিষয়ের প্রতিটি অধ্যায়ের মূল সংজ্ঞা ও নিয়ম নির্ভুলভাবে আয়ত্ত করা।
                ২. যৌক্তিক বিশ্লেষণ: যেকোনো সমস্যার পেছনে থাকা কার্যকারণ এবং লজিক্যাল যুক্তি বিশ্লেষণ করা।
                ৩. বিগত বছরের প্রশ্ন সমাধান: বিগত প্রশ্নমালা অনুশীলন করে প্রশ্নের ধরন ও সময় ব্যবস্থাপনায় দক্ষতা অর্জন করা।

                পরীক্ষার জন্য পরামর্শ:
                নিয়মিত মক টেস্ট দিন এবং যে অংশগুলোতে ভুল হয় তা আলাদা খাতায় নোট করে বারবার রিভিশন দিন।
                """.trimIndent(),
                detectedLanguage = "বাংলা",
                subjectTag = "উচ্চশিক্ষা"
            )
        }

        // History & World Knowledge
        if (qLower.contains("বিশ্বযুদ্ধ") || qLower.contains("ইতিহাস") || qLower.contains("জাতিসংঘ") || qLower.contains("ভূগোল") || qLower.contains("মহাসাগর") || qLower.contains("সৌরজগৎ") || qLower.contains("মুক্তিযুদ্ধ") || qLower.contains("বাংলাদেশ")) {
            return UniversalResult(
                responseText = """
                ইতিহাস ও সাধারণ জ্ঞান
                বিষয়: $originalQuery

                মূল প্রেক্ষাপট:
                ঐতিহাসিক ঘটনা এবং আন্তর্জাতিক প্রেক্ষাপট অধ্যয়নের সময় ঘটনার সময়কাল, কার্যকারণ এবং দীর্ঘমেয়াদী প্রভাব গভীরভাবে পর্যালোচনা করতে হয়।

                বিশ্লেষণাত্মক রূপরেখা:
                • পটভূমি ও কারণ: রাজনৈতিক, সামাজিক এবং অর্থনৈতিক প্রেক্ষাপট যা ঘটনাটির সূত্রপাত ঘটিয়েছিল।
                • প্রধান ঘটনাপ্রবাহ: মূল ঘটনা, গুরুত্বপূর্ণ তারিখ এবং সংশ্লিষ্ট নেতৃবৃন্দের ভূমিকা।
                • সুদূরপ্রসারী ফলাফল: ঘটনা পরবর্তী জাতীয় বা আন্তর্জাতিক নীতি, চুক্তি ও সমাজ ব্যবস্থার পরিবর্তন।

                মনে রাখার সহজ কৌশল:
                একটি টাইমলাইন তৈরি করে সাল এবং ঘটনার অনুক্রম সাজিয়ে রাখলে তথ্য সহজে মনে থাকে।
                """.trimIndent(),
                detectedLanguage = "বাংলা",
                subjectTag = "ইতিহাস ও জ্ঞান"
            )
        }

        // General Multi-Class Universal Fallback
        return UniversalResult(
            responseText = """
            শিক্ষামূলক আলোচনা ও সমাধান
            বিষয়: $originalQuery

            সংক্ষিপ্ত সারসংক্ষেপ:
            আপনার প্রশ্নের মূল শিক্ষণীয় বিষয়বস্তু নিচে সহজ ও সুশৃঙ্খলভাবে উপস্থাপন করা হলো।

            মূল ব্যাখ্যা ও পয়েন্টসমূহ:
            ১. মৌলিক ধারণা: বিষয়টি যে বৈজ্ঞানিক বা শিক্ষাক্রমিক নিয়মের ওপর প্রতিষ্ঠিত তার সুস্পষ্ট ব্যাখ্যা।
            ২. কার্যকারণ: কীভাবে প্রক্রিয়াটি সম্পন্ন হয় এবং এর বিভিন্ন উপাদানের পারস্পরিক সম্পর্ক।
            ৩. প্রয়োগ ও সমাধান: পরীক্ষায় বা ব্যবহারিক ক্ষেত্রে এই নিয়মটি ব্যবহারের সঠিক পদ্ধতি।

            বাস্তব উদাহরণ:
            প্রতিদিনের জীবন বা সহজ উদাহরণের সাথে সম্পর্কিত করে বিষয়টি পড়লে ধারণা পরিষ্কার ও দীর্ঘস্থায়ী হয়।

            পড়াশোনার টিপস:
            মূল পয়েন্টগুলো নিজের ভাষায় নোট খাতায় লিখে রাখুন এবং সূত্রের ব্যবহার বারবার অনুশীলন করুন।
            """.trimIndent(),
            detectedLanguage = "বাংলা",
            subjectTag = "সাধারণ এআই"
        )
    }
}
