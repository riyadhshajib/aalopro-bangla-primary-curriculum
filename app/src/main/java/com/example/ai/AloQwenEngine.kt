package com.example.ai

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.BuildConfig
import com.example.ai.gemini.ChatTurn
import com.example.util.AloTextSanitizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale

/**
 * Legacy class name retained for callers. No Qwen model weights or inference
 * runtime are packaged in the app. Open questions use Gemini when available;
 * the older local reference helper is retained only for legacy callers/tests.
 */
object AloQwenEngine {

    private val englishQuestionCue = Regex(
        "\\b(what|why|how|when|where|which|who|explain|describe|define|tell|could|would|should|is|are|does|do)\\b",
        RegexOption.IGNORE_CASE
    )

    internal fun prefersBangla(query: String): Boolean {
        if (query.any { it in '\u0980'..'\u09FF' }) return true
        if (englishQuestionCue.containsMatchIn(query)) return false
        return AloBanglishNormalizer.normalizeBanglish(query).isBanglish
    }

    data class QwenResponse(
        val query: String,
        val text: String,
        val modelName: String = "অফলাইন সহায়ক",
        val subjectTag: String = "সাধারণ প্রশ্ন",
        val isOnline: Boolean = false
    )

    /**
     * Routes open questions to Gemini, with an honest offline/service fallback.
     */
    suspend fun answerWithQwen(context: Context, query: String, history: List<ChatTurn> = emptyList()): QwenResponse = withContext(Dispatchers.IO) {
        val cleanQuery = query.trim()
        val isBengaliOrBanglish = prefersBangla(cleanQuery)

        val online = hasValidatedInternet(context)
        // The Firebase SDK calls a managed proxy; no model API key is embedded.
        if (online && FirebaseAiTutor.isConfigured(context)) {
            try {
                val languageInstruction = if (isBengaliOrBanglish) {
                    "The user is asking in Bengali/Banglish. You MUST respond in pure, natural, fluent, and well-structured Bengali."
                } else {
                    "The CURRENT question is in English. Answer ONLY in English, regardless of the language in prior turns, using clear age-appropriate language."
                }
                // A stalled network/App Check request must never leave the child
                // watching an endless loading indicator.
                val text = withTimeoutOrNull(55_000L) {
                    FirebaseAiTutor.answer(cleanQuery, history, languageInstruction)
                }
                if (text == null && BuildConfig.DEBUG) {
                    Log.w("AaloProAI", "Firebase AI returned no text or timed out")
                }
                if (!text.isNullOrBlank()) {
                    val tag = if (isBengaliOrBanglish) "অনলাইন এআই শিক্ষক (বাংলা)" else "Online AI tutor (English)"
                    return@withContext QwenResponse(
                        query = cleanQuery,
                        text = AloTextSanitizer.cleanText(text),
                        modelName = "অনলাইন এআই শিক্ষক",
                        subjectTag = tag,
                        isOnline = true
                    )
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.w(
                        "AaloProAI",
                        "Firebase AI failed: ${e.javaClass.simpleName}: ${e.message}; " +
                            "cause=${e.cause?.javaClass?.simpleName}: ${e.cause?.message}",
                        e
                    )
                }
                // Do not substitute a loosely matched offline article for an open question.
            }
        }

        val message = when {
            !online && isBengaliOrBanglish -> "সাধারণ প্রশ্নের উত্তর পেতে ইন্টারনেট সংযোগ চালু করুন, তারপর আবার পাঠান।"
            !online -> "Turn on your internet connection and send the question again."
            isBengaliOrBanglish -> "অনলাইন AI এই মুহূর্তে উত্তর দিতে পারছে না। কিছুক্ষণ পরে আবার চেষ্টা করুন।"
            else -> "The online tutor is unavailable right now. Please try again shortly."
        }
        return@withContext QwenResponse(
            query = cleanQuery,
            text = message,
            subjectTag = "অনলাইন উত্তর পাওয়া যায়নি"
        )
    }

    private fun hasValidatedInternet(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    /**
     * Returns prewritten answers for a small set of recognized topics.
     */
    fun generateLocalQwenResponse(query: String): QwenResponse {
        val qLower = query.lowercase(Locale.ROOT)
        val hasBengali = query.any { it in '\u0980'..'\u09FF' }
        val banglishInfo = AloBanglishNormalizer.normalizeBanglish(query)
        val isBangla = hasBengali || banglishInfo.isBanglish
        val normalizedLower = if (banglishInfo.isBanglish) banglishInfo.convertedBanglaQuery.lowercase(Locale.ROOT) else qLower

        // =========================================================================
        // 1. GEOPOLITICS & INTERNATIONAL RELATIONS
        // =========================================================================
        if (qLower.contains("geo politic") || qLower.contains("geopolitic") || normalizedLower.contains("ভূরাজনীতি") || query.contains("ভূরাজনীতি") || qLower.contains("geo-politic")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    ভূরাজনীতি (Geopolitics): বিশদ বিশ্লেষণ ও ধারণা

                    ১. ভূরাজনীতি কী? (Definition):
                    ভূরাজনীতি হলো এমন একটি শাস্ত্র যা কোনো রাষ্ট্রের ভৌগোলিক অবস্থান, আয়তন, প্রাকৃতিক সম্পদ, সীমানা ও সমুদ্রপথ কীভাবে তার আন্তর্জাতিক রাজনীতি, পররাষ্ট্রনীতি এবং বৈশ্বিক ক্ষমতার ভারসাম্যকে প্রভাবিত করে তা পর্যালোচনা করে।
                    • ১৮৯৯ সালে সুইডিশ রাষ্ট্রবিজ্ঞানী রুডলফ জেলেন (Rudolf Kjellén) প্রথম 'Geopolitics' শব্দটি ব্যবহার করেন।

                    ২. ভূরাজনীতির ৪টি প্রধান স্তম্ভ (Core Pillars):
                    • ১. ভৌগোলিক অবস্থান ও সীমানা: কোনো দেশ স্থলবেষ্টিত (Landlocked) নাকি উপকূলীয়, প্রতিবেশী দেশগুলোর সামরিক শক্তি ও প্রাকৃতিক বাধা (পাহাড়, নদী, মরুভূমি)।
                    • ২. কৌশলগত সামুদ্রিক চোখপয়েন্ট (Strategic Choke Points):
                       - মালাক্কা প্রণালী (Strait of Malacca): ভারত মহাসাগর ও প্রশান্ত মহাসাগরের সংযোগস্থল (বিশ্ব বাণিজ্যের ২৫%+)।
                       - হরমুজ প্রণালী (Strait of Hormuz): বিশ্বের মোট পেট্রোলিয়াম বাণিজ্যের প্রায় ২০% এর মধ্য দিয়ে যায়।
                       - সুয়েজ খাল ও পানামা খাল: মহাদেশগুলোর মধ্যকার সংক্ষিপ্ততম বাণিজ্য রুট।
                    • ৩. প্রাকৃতিক সম্পদ ও জ্বালানি নিরাপত্তা: খনিজ তেল, গ্যাস, বিরল খনিজ (Rare Earth Elements), সুপেয় পানি ও কৃষিজমি নিয়ন্ত্রণ।
                    • ৪. ক্ষমতার বিস্তার ও জোটবদ্ধতা (Alliances): ন্যাটো (NATO), ব্রিকস (BRICS), কোয়াড (QUAD), আসিয়ান (ASEAN) ইত্যাদি সামরিক ও অর্থনৈতিক জোট।

                    ৩. প্রধান ভূরাজনৈতিক তত্ত্বসমূহ (Foundational Theories):
                    • ম্যাকিন্ডারের হার্টল্যান্ড তত্ত্ব (Heartland Theory - 1904): "যিনি পূর্ব ইউরোপ শাসন করবেন তিনি হার্টল্যান্ড (ইউরেশিয়ার কেন্দ্র) নিয়ন্ত্রণ করবেন; আর যিনি হার্টল্যান্ড নিয়ন্ত্রণ করবেন তিনি সমগ্র বিশ্বকে নিয়ন্ত্রণ করবেন।"
                    • মাহানের সামুদ্রিক শক্তি তত্ত্ব (Sea Power Theory - 1890): আলফ্রেড থায়ার মাহানের মতে, সামুদ্রিক বাণিজ্য পথ ও নৌবাহিনীর নিয়ন্ত্রণই বিশ্ব নেতৃত্বের মূল চাবিকাঠি।
                    • স্পাইকম্যানের রিমল্যান্ড তত্ত্ব (Rimland Theory - 1942): নিকোলাস স্পাইকম্যানের মতে, ইউরেশিয়ার উপকূলবর্তী অঞ্চল (রিমল্যান্ড) নিয়ন্ত্রণকারী শক্তিই বিশ্ব নিয়ন্ত্রণ করবে।

                    ৪. সমসাময়িক প্রেক্ষাপট ও বাংলাদেশ:
                    • ইন্দো-প্যাসিফিক অঞ্চল ও বঙ্গোপসাগর: ভূকৌশলগতভাবে বাংলাদেশ দক্ষিণ এশিয়া ও দক্ষিণ-পূর্ব এশিয়ার সেতুবন্ধন। মাতারবাড়ি গভীর সমুদ্রবন্দর ও ব্লু-ইকোনমি বাংলাদেশের ভূরাজনৈতিক গুরুত্ব বহুগুণ বাড়িয়ে দিয়েছে।
                    • ইউক্রেন ও মধ্যপ্রাচ্য সংঘাত: জ্বালানি সরবরাহ রুট ও কৌশলগত বাফার জোনের দ্বন্দ্ব ভূরাজনীতির স্পষ্ট উদাহরণ।

                    সারসংক্ষেপ:
                    ভূরাজনীতি প্রমাণ করে যে প্রযুক্তি ও আদর্শের পরিবর্তন ঘটলেও পৃথিবীর প্রাকৃতিক ভূগোলই রাষ্ট্রসমূহের চূড়ান্ত ভাগ্য ও বৈদেশিক নীতি নির্ধারণ করে।
                    """.trimIndent(),
                    subjectTag = "আন্তর্জাতিক সম্পর্ক ও ভূরাজনীতি (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    Understanding Geopolitics: A Comprehensive Overview

                    1. Definition & Core Premise:
                    Geopolitics is the study of how Earth's geography—physical territory, spatial location, natural resources, oceans, mountain ranges, and international borders—influences foreign policy, state sovereignty, international relations, and global power dynamics.
                    • The term was coined in 1899 by Swedish political scientist Rudolf Kjellén and later developed by scholars like Halford Mackinder, Alfred Thayer Mahan, and Nicholas Spykman.

                    2. The Four Pillars of Geopolitics:
                    • Physical Geography & Spatial Position: Whether a nation is landlocked, insular (island nation), or littoral; its natural defensive barriers (mountain ranges, rivers) and strategic depth.
                    • Maritime Choke Points (Strategic Waterways):
                      - Strait of Malacca: Critical artery connecting the Indian Ocean to the Pacific; handles >25% of global maritime commerce.
                      - Strait of Hormuz: Vital passage for approximately 20% of global petroleum transit.
                      - Suez & Panama Canals: Artificial corridors that drastically reduce intercontinental transit distances.
                      - Bab el-Mandeb & the Turkish Straits (Bosphorus/Dardanelles).
                    • Resource Geopolitics & Critical Supply Chains: Control over crude oil, natural gas, rare earth minerals, fresh water aquifers, and semiconductor manufacturing nodes.
                    • Alliances & Power Projection: Strategic pacts (e.g., NATO, QUAD, AUKUS, BRICS, SCO, ASEAN) and forward military postures.

                    3. Foundational Geopolitical Theories:
                    • Halford Mackinder's Heartland Theory (1904):
                      "Who rules East Europe commands the Heartland; who rules the Heartland commands the World-Island; who rules the World-Island commands the world." Emphasizes the dominance of the Eurasian landmass.
                    • Alfred Thayer Mahan's Sea Power Theory (1890):
                      Argued that national greatness is directly tied to maritime commerce, sea lanes of communication (SLOCs), and naval supremacy.
                    • Nicholas Spykman's Rimland Theory (1942):
                      Posited that the maritime fringes of Eurasia (Western Europe, Middle East, South Asia, East Asia) are the true fulcrum of global balance of power.

                    4. Contemporary Geopolitical Flashpoints:
                    • The Indo-Pacific & South China Sea: Rising contestation over freedom of navigation, island chains, and critical trade arteries.
                    • Eurasia & Eastern Europe: Geopolitical friction surrounding strategic buffer zones, Black Sea access, and regional security architectures.
                    • The Bay of Bengal & South Asia: Bangladesh's strategic position as the littoral bridge linking South Asia with ASEAN markets.

                    Summary Takeaway:
                    While political regimes and technologies shift rapidly, physical geography remains the permanent stage upon which the grand strategy of nations is contested.
                    """.trimIndent(),
                    subjectTag = "Geopolitics & Strategic Studies"
                )
            }
        }

        // --- International Relations & Global Organizations (UN, NATO, BRICS, Diplomacy) ---
        if (qLower.contains("united nations") || qLower.contains("un ") || qLower.contains("nato") || qLower.contains("brics") || qLower.contains("diplomacy") || qLower.contains("cold war") || normalizedLower.contains("জাতিসংঘ") || normalizedLower.contains("কূটনীতি") || normalizedLower.contains("ন্যাটো") || normalizedLower.contains("স্নায়ুযুদ্ধ")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    আন্তর্জাতিক সম্পর্ক, বিশ্বসংস্থা ও কূটনীতি:

                    ১. জাতিসংঘ (United Nations - UN):
                    • প্রতিষ্ঠা: ২৪ অক্টোবর ১৯৪৫ (দ্বিতীয় বিশ্বযুদ্ধের পর বিশ্বশান্তি রক্ষার লক্ষ্যে)। সদর দপ্তর: নিউ ইয়র্ক, যুক্তরাষ্ট্র।
                    • নিরাপত্তা পরিষদ (UNSC): ১৫টি সদস্য রাষ্ট্র—৫টি স্থায়ী (ভেটো ক্ষমতাপ্রাপ্ত: যুক্তরাষ্ট্র, যুক্তরাজ্য, ফ্রান্স, রাশিয়া, চীন) এবং ১০টি অস্থায়ী।
                    • সাধারণ পরিষদ (UNGA): সকল সদস্য রাষ্ট্রের সমমর্যাদার ফোরাম (১৯৩টি সদস্য দেশ)।

                    ২. প্রধান বৈশ্বিক জোট ও কাঠামো:
                    • ন্যাটো (NATO): ১৯৪৯ সালে গঠিত উত্তর আটলান্টিক সামরিক জোট। মূল নীতি: যৌথ প্রতিরক্ষা (Article 5 - একজনের ওপর আক্রমণ সবার ওপর আক্রমণ)।
                    • ব্রিকস (BRICS): ব্রাজিল, রাশিয়া, ভারত, চীন, দক্ষিণ আফ্রিকা সহ বর্ধিত অর্থনৈতিক জোট যা বহুমুখী বিশ্বব্যবস্থা (Multipolar World) গঠনে কাজ করে।
                    • জোটনিরপেক্ষ আন্দোলন (NAM): স্নায়ুযুদ্ধের সময় পরাশক্তিদের সামরিক জোটে যোগ না দিয়ে নিরপেক্ষ থাকার আন্দোলন।

                    ৩. কূটনীতি (Diplomacy) ও সফট পাওয়ার:
                    • কূটনীতি: যুদ্ধ বা সংঘাত ছাড়া আলাপ-আলোচনা ও চুক্তির মাধ্যমে জাতীয় স্বার্থ হাসিল করার শিল্প।
                    • হার্ড পাওয়ার বনাম সফট পাওয়ার: হার্ড পাওয়ার হলো সামরিক ও অর্থনৈতিক চাপ; সফট পাওয়ার হলো সংস্কৃতি, মূল্যবোধ ও আন্তর্জাতিক সুখ্যাতি।
                    """.trimIndent(),
                    subjectTag = "আন্তর্জাতিক সম্পর্ক (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    International Relations, Global Governance & Diplomacy

                    1. The United Nations (UN):
                    • Established: October 24, 1945, following World War II to maintain international peace and security.
                    • Security Council (UNSC): Consists of 15 members—5 Permanent members with Veto Power (P5: USA, UK, France, Russia, China) and 10 rotating non-permanent members.
                    • General Assembly (UNGA): Universal policymaking body representing all 193 member states.

                    2. Major Strategic Alliances:
                    • NATO (North Atlantic Treaty Organization): Formed in 1949 as a collective defense pact (Article 5: an armed attack against one is considered an attack against all).
                    • BRICS: An economic bloc (Brazil, Russia, India, China, South Africa, and expanded members) championing multipolarity and alternative global financial mechanisms.
                    • Non-Aligned Movement (NAM): Historical bloc of states refusing alignment with either Western or Eastern superpowers during the Cold War.

                    3. Diplomacy, Soft Power & Deterrence:
                    • Diplomacy: The practice of conducting negotiations, treaties, and foreign policy without recourse to direct armed conflict.
                    • Hard vs. Soft Power: Hard power relies on military coercion and economic sanctions; Soft power (Joseph Nye) attracts through cultural prestige, democratic ideals, and moral authority.
                    • Smart Power: Strategic combination of hard and soft power tools.
                    """.trimIndent(),
                    subjectTag = "International Relations & Diplomacy"
                )
            }
        }

        // =========================================================================
        // 2. POLITICAL SCIENCE & GOVERNANCE
        // =========================================================================
        if (qLower.contains("democracy") || qLower.contains("constitution") || qLower.contains("separation of powers") || qLower.contains("socialism") || qLower.contains("capitalism") || normalizedLower.contains("গণতন্ত্র") || normalizedLower.contains("সংবিধান") || normalizedLower.contains("সমাজতন্ত্র") || normalizedLower.contains("পুঁজিবাদ")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    রাষ্ট্রবিজ্ঞান, সরকার ব্যবস্থা ও অর্থনীতি তত্ত্ব:

                    ১. গণতন্ত্র (Democracy):
                    • সংজ্ঞা: আব্রাহাম লিংকনের মতে—"Government of the people, by the people, for the people" (জনগণের দ্বারা, জনগণের জন্য, জনগণের সরকার)।
                    • প্রত্যক্ষ বনাম পরোক্ষ গণতন্ত্র: প্রাচীন এথেন্সে নাগরিকরা সরাসরি সিদ্ধান্ত নিত (প্রত্যক্ষ); আধুনিক রাষ্ট্রে নির্বাচিত প্রতিনিধিদের মাধ্যমে রাষ্ট্র পরিচালিত হয় (সংসদীয়/রাষ্ট্রপতি শাসিত)।

                    ২. ক্ষমতার স্বতন্ত্রীকরণ নীতি (Separation of Powers):
                    ফরাসি দার্শনিক মন্টেস্কু প্রবর্তিত রাষ্ট্রের তিনটি প্রধান অঙ্গ:
                    • আইন বিভাগ (Legislature): আইন প্রণয়ন ও জাতীয় বাজেট অনুমোদন করে।
                    • শাসন বিভাগ (Executive): আইন বাস্তবায়ন ও রাষ্ট্র পরিচালনা করে।
                    • বিচার বিভাগ (Judiciary): আইনের ব্যাখ্যা ও নিরপেক্ষ ন্যায়বিচার নিশ্চিত করে।

                    ৩. পুঁজিবাদ বনাম সমাজতন্ত্র:
                    • পুঁজিবাদ (Capitalism): মুক্ত বাজার, ব্যক্তিগত মালিকানা, মুনাফা অর্জন ও প্রতিযোগিতার ওপর প্রতিষ্ঠিত (অ্যাডাম স্মিথ)।
                    • সমাজতন্ত্র (Socialism): উৎপাদনের উপাদানসমূহের ওপর রাষ্ট্রীয়/সামষ্টিক মালিকানা ও সমবণ্টনের নীতি (কার্ল মার্ক্স)।
                    """.trimIndent(),
                    subjectTag = "রাষ্ট্রবিজ্ঞান ও সুশাসন (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    Political Systems, Constitutionalism & Economic Models

                    1. Democracy & Constitutional Rule:
                    • Definition: A system of government where supreme power is vested in the people and exercised through free, fair, and periodic elections.
                    • Rule of Law: All citizens and institutions are accountable to publicly promulgated, equally enforced laws.

                    2. Separation of Powers (Trias Politica):
                    Formulated by Baron de Montesquieu to prevent tyranny:
                    • Legislative Branch: Enacts legislation and controls the national budget.
                    • Executive Branch: Administers public policy and enforces laws.
                    • Judicial Branch: Independently interprets the constitution and resolves legal disputes.
                    • Checks and Balances: Each branch holds constitutional mechanisms to limit excesses of the other two.

                    3. Comparative Economic Ideologies:
                    • Capitalism: Private ownership of capital, market-driven price discovery via supply and demand, and profit incentive.
                    • Socialism: Social or state ownership of key means of production and centralized planning to ensure equitable resource distribution.
                    • Mixed Economy: Blends market mechanisms with public sector regulation and social safety nets.
                    """.trimIndent(),
                    subjectTag = "Political Science & Governance"
                )
            }
        }

        // =========================================================================
        // 3. ECONOMICS & MACROFINANCE
        // =========================================================================
        if (qLower.contains("inflation") || qLower.contains("gdp") || qLower.contains("monetary policy") || qLower.contains("fiscal policy") || qLower.contains("supply and demand") || normalizedLower.contains("মুদ্রাস্ফীতি") || normalizedLower.contains("জিডিপি") || normalizedLower.contains("মুদ্রানীতি") || normalizedLower.contains("চাহিদা ও যোগান")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    অর্থনীতি ও সামষ্টিক অর্থনৈতিক সূচক:

                    ১. মুদ্রাস্ফীতি (Inflation):
                    • সংজ্ঞা: বাজারে পণ্যের দামের সাধারণ বৃদ্ধি এবং এর ফলে মুদ্রার ক্রয়ক্ষমতা কমে যাওয়া।
                    • প্রকারভেদ: চাহিদা বৃদ্ধিজনিত (Demand-Pull), ব্যয় বৃদ্ধিজনিত (Cost-Push) ও অন্তর্নির্মিত মুদ্রাস্ফীতি।
                    • নিয়ন্ত্রণের উপায়: কেন্দ্রীয় ব্যাংক কর্তৃক সুদের হার বাড়ানো এবং মুদ্রানীতি সংকুচিত করা।

                    ২. মোট দেশজ উৎপাদন (GDP - Gross Domestic Product):
                    • সংজ্ঞা: একটি নির্দিষ্ট বছরে দেশের ভৌগোলিক সীমানার ভেতরে উৎপাদিত সকল চূড়ান্ত পণ্য ও সেবার মোট আর্থিক মূল্য।
                    • সূত্র: GDP = C + I + G + (X − M)
                      - C = ভোগ ব্যয় (Consumption)
                      - I = বেসরকারি বিনিয়োগ (Investment)
                      - G = সরকারি ব্যয় (Government Spending)
                      - X − M = নিট রপ্তানি (রপ্তানি − আমদানি)

                    ৩. মুদ্রানীতি বনাম রাজস্বনীতি:
                    • মুদ্রানীতি (Monetary Policy): কেন্দ্রীয় ব্যাংক প্রণয়ন করে (টাকার প্রবাহ ও সুদের হার নিয়ন্ত্রণ)।
                    • রাজস্বনীতি (Fiscal Policy): সরকার প্রণয়ন করে (ট্যাক্স/কর এবং সরকারি বাজেট ব্যয়ের মাধ্যমে)।
                    """.trimIndent(),
                    subjectTag = "অর্থনীতি ও বাণিজ্য (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    Macroeconomics: Inflation, GDP & Policy Instruments

                    1. Inflation Dynamics:
                    • Definition: A sustained, generalized rise in the price level of goods and services over time, eroding currency purchasing power.
                    • Causes:
                      - Demand-Pull: Aggregate demand outpaces aggregate productive capacity.
                      - Cost-Push: Surging input costs (energy, labor, supply chain shocks).
                    • Measurement: Consumer Price Index (CPI) and GDP Deflator.

                    2. Gross Domestic Product (GDP):
                    • Definition: The total monetary value of all final goods and services produced within a country's borders during a specific period.
                    • Expenditure Formula: GDP = C + I + G + (X - M)
                      - C = Household Consumption
                      - I = Gross Private Investment
                      - G = Government Expenditure
                      - (X - M) = Net Exports (Exports minus Imports)

                    3. Macroeconomic Policy Tools:
                    • Monetary Policy: Conducted by the Central Bank to regulate liquidity, interest rates, and bank reserve requirements.
                    • Fiscal Policy: Executed by the government via taxation schedules and public infrastructure spending.
                    """.trimIndent(),
                    subjectTag = "Macroeconomics & Finance"
                )
            }
        }

        // =========================================================================
        // 4. WORLD HISTORY & CIVILIZATIONS
        // =========================================================================
        if (qLower.contains("world war") || qLower.contains("industrial revolution") || qLower.contains("french revolution") || qLower.contains("renaissance") || normalizedLower.contains("বিশ্বযুদ্ধ") || normalizedLower.contains("শিল্প বিপ্লব") || normalizedLower.contains("ফরাসি বিপ্লব") || normalizedLower.contains("রেনেসাঁ")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    বিশ্ব ইতিহাস ও সভ্যতার মাইলফলক:

                    ১. প্রথম বিশ্বযুদ্ধ (১৯১৪–১৯১৮):
                    • কারণ: অস্ট্রিয়ার আর্চডিউক ফ্রাঞ্জ ফার্ডিনান্ডের হত্যাকাণ্ড, উগ্র জাতীয়তাবাদ ও সাম্রাজ্যবাদী সামরিক জোট।
                    • ফলাফল: অটোমান, অস্ট্রো-হাঙ্গেরিয়ান, রাশিয়ান ও জার্মান সাম্রাজ্যের পতন এবং লীগ অব নেশনস প্রতিষ্ঠা।

                    ২. দ্বিতীয় বিশ্বযুদ্ধ (১৯৩৯–১৯৪৫):
                    • কারণ: ফ্যাসিবাদ ও নাৎসিবাদ (হিটলারের পোল্যান্ড আক্রমণ), ভার্সাই চুক্তির ক্ষোভ।
                    • প্রধান পক্ষ: অক্ষশক্তি (জার্মানি, ইতালি, জাপান) বনাম মিত্রশক্তি (যুক্তরাষ্ট্র, যুক্তরাজ্য, সোভিয়েত ইউনিয়ন, ফ্রান্স)।
                    • সমাপ্তি: হিরোশিমা ও নাগাসাকিতে পারমাণবিক বোমাবর্ষণ, জাতিসংঘ প্রতিষ্ঠা ও স্নায়ুযুদ্ধের সূচনা।

                    ৩. শিল্প বিপ্লব ও আধুনিক যুগ:
                    • প্রথম শিল্প বিপ্লব (১৮শ শতক): বাষ্পীয় ইঞ্জিন, বস্ত্রশিল্পের যান্ত্রিকীকরণ ও কয়লা ভিত্তিক উৎপাদন (যুক্তরাজ্য)।
                    • চতুর্থ শিল্প বিপ্লব (বর্তমান): কৃত্রিম বুদ্ধিমত্তা (AI), রোবোটিক্স, ইন্টারনেট অফ থিংস (IoT) ও বায়োটেকনোলজি।
                    """.trimIndent(),
                    subjectTag = "বিশ্ব ইতিহাস (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    World History: Epochs, Conflicts & Transformations

                    1. World War I (1914–1918):
                    • Catalysts: Assassination of Archduke Franz Ferdinand, militarism, secret alliance treaties, and imperial rivalries.
                    • Key Consequences: Collapse of four empires (Russian, German, Ottoman, Austro-Hungarian), Treaty of Versailles, and birth of the League of Nations.

                    2. World War II (1939–1945):
                    • Axis Powers (Nazi Germany, Fascist Italy, Imperial Japan) vs. Allied Powers (USA, USSR, Great Britain, France, China).
                    • Turning Points: Battle of Stalingrad, Battle of Midway, D-Day Normandy Landings.
                    • Culmination: Atomic bombings of Hiroshima and Nagasaki, establishment of the United Nations (1945), and the onset of the US-Soviet Cold War.

                    3. The Industrial Revolutions:
                    • 1st (Steam & Mechanization - late 1700s): Transition from agrarian crafts to factory manufacturing.
                    • 2nd (Electricity & Mass Production - late 1800s): Steel, assembly lines, telegraph.
                    • 3rd (Digital & Computing - mid 1900s): Microprocessors, internet, personal computers.
                    • 4th (Cyber-Physical & AI - 21st century): Autonomous systems, generative AI, biotechnology.
                    """.trimIndent(),
                    subjectTag = "World History & Civilization"
                )
            }
        }

        // =========================================================================
        // 5. COMPUTER SCIENCE, AI, DATA & NETWORKS
        // =========================================================================
        if (qLower.contains("qwen") || qLower.contains("llm") || qLower.contains("transformer") || qLower.contains("machine learning") || qLower.contains("neural network") || qLower.contains("deep learning")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    কৃত্রিম বুদ্ধিমত্তা, লার্জ ল্যাঙ্গুয়েজ মডেল (LLM) ও ট্রান্সফরমার আর্কিটেকচার:

                    ১. কুয়েন (Qwen 2.5) মডেল সম্পর্কে:
                    কুয়েন হলো আলিবাবা ক্লাউড কর্তৃক নির্মিত বিশ্বের অন্যতম শীর্ষস্থানীয় ফাউন্ডেশনাল এলএলএম পরিবার। এটি কোডিং, উচ্চতর গণিত এবং বহুভাষিক যুক্তিবোধে অত্যন্ত শক্তিশালী।

                    ২. ট্রান্সফরমার আর্কিটেকচার (Transformer Network):
                    ২০১৭ সালে গুগলের "Attention Is All You Need" পেপারের মাধ্যমে ট্রান্সফরমারের যাত্রা শুরু হয়:
                    • সেলফ-অ্যাটেনশন মেকানিজম (Self-Attention): একটি বাক্যের প্রতিটি শব্দের সাথে অন্যান্য সকল শব্দের সম্পর্ক ও ওজন (Weight) যুগপৎভাবে পরিমাপ করে।
                    • মাল্টি-হেড অ্যাটেনশন (Multi-Head Attention): বিভিন্ন দৃষ্টিকোণ থেকে শব্দের অর্থ ও ব্যাকরণিক অন্বয় ধারণ করে।
                    • টোকেনাইজেশন: পাঠ্যকে ছোট ছোট সংখ্যাসূচক টোকেনে রূপান্তর করে প্রসেস করা হয়।

                    ৩. এলএলএম কীভাবে শেখে?
                    • প্রি-ট্রেনিং (Pre-training): বিলিয়ন বিলিয়ন টেক্সট থেকে পরবর্তী শব্দ পূর্বাভাস (Next Token Prediction) করার মাধ্যমে বিশ্বজ্ঞান অর্জন।
                    • ফাইন-টিউনিং (SFT): সুনির্দিষ্ট নির্দেশাবলী অনুসরণের জন্য স্পেশাল ট্রেনিং।
                    • RLHF (মানবিক প্রতিক্রিয়া থেকে শিক্ষা): নিরাপদ, সহায়ক ও নির্ভুল উত্তর নিশ্চিত করার প্রক্রিয়া।
                    """.trimIndent(),
                    subjectTag = "কম্পিউটার বিজ্ঞান ও এআই (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    Qwen 2.5 & Large Language Model (LLM) Architecture

                    1. About Qwen 2.5:
                    Qwen is Alibaba Cloud's flagship open-weights foundation model suite, renowned for state-of-the-art coding, multilingual fluency, and dense chain-of-thought mathematical reasoning.

                    2. The Transformer Mechanism (Vaswani et al., 2017):
                    • Self-Attention: Simultaneously calculates contextual relevance scores between every token in a prompt using Query (Q), Key (K), and Value (V) matrices:
                      Attention(Q, K, V) = softmax(Q K^T / √d_k) * V
                    • Positional Encodings: Injects order information into the parallelized architecture.
                    • Multi-Layer Perceptrons & LayerNorm: Expands non-linear expressivity and stabilizes backpropagation.

                    3. Training Lifecycle:
                    • Self-Supervised Pretraining: Next-token prediction across trillions of tokens to establish broad world knowledge.
                    • Supervised Fine-Tuning (SFT): Aligns raw representations to multi-turn conversational dialog.
                    • Alignment (RLHF / DPO): Direct preference optimization ensuring helpful, truthful, and safety-aligned outputs.
                    """.trimIndent(),
                    subjectTag = "AI & Deep Learning"
                )
            }
        }

        // --- Quantum Computing ---
        if (qLower.contains("quantum computing") || qLower.contains("qubit") || qLower.contains("superposition") || normalizedLower.contains("কোয়ান্টাম কম্পিউটিং")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    কোয়ান্টাম কম্পিউটিং (Quantum Computing):

                    ১. মূল ধারণা:
                    সাধারণ কম্পিউটার যেখানে বিট (০ অথবা ১) ব্যবহার করে, কোয়ান্টাম কম্পিউটার সেখানে কোয়ান্টাম মেকানিক্সের ওপর ভিত্তি করে 'কিউবিট' (Qubit) ব্যবহার করে।

                    ২. প্রধান ৩টি বৈশিষ্ট্য:
                    • সুপারপজিশন (Superposition): একটি কিউবিট একই সময়ে ০ এবং ১ উভয় অবস্থায় অবস্থান করতে পারে (|ψ⟩ = α|0⟩ + β|1⟩)।
                    • এন্ট্যাঙ্গেলমেন্ট (Entanglement): একাধিক কিউবিটের মধ্যে তাৎক্ষণিক মহাজাগতিক সংযোগ, যার ফলে একটির অবস্থা বদলালে অন্যটির অবস্থাও সাথে সাথে পরিবর্তিত হয়।
                    • কোয়ান্টাম ইন্টারফেয়ারেন্স: সঠিক উত্তরের সম্ভাব্যতা বৃদ্ধি করে ভুল উত্তরগুলোকে বাতিল করা।

                    ৩. বাস্তব প্রয়োগ:
                    • ড্রাগ ডিসকভারি ও আণবিক সিমুলেশন।
                    • জটিল ক্রিপ্টোগ্রাফি ও অপ্টিমাইজেশন অ্যালগরিদম (Shor's Algorithm, Grover's Algorithm)।
                    """.trimIndent(),
                    subjectTag = "উচ্চতর পদার্থ ও প্রযুক্তি (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    Quantum Computing & Quantum Information Theory

                    1. Core Fundamentals:
                    Classical computing encodes data into binary bits (0 or 1). Quantum computers exploit quantum mechanical principles to process data through Quantum Bits (Qubits).

                    2. Foundational Pillars:
                    • Superposition: A qubit exists in a linear combination of basis states (|0⟩ and |1⟩) until measured: |ψ⟩ = α|0⟩ + β|1⟩ where |α|² + |β|² = 1.
                    • Quantum Entanglement: Non-local correlations where the quantum state of one particle cannot be described independently of another.
                    • Quantum Interference: Directs computational amplitudes toward constructively interfering on correct solutions while destructively canceling incorrect paths.

                    3. Revolutionary Impact:
                    • Cryptanalysis: Shor's algorithm provides polynomial-time prime factorization, impacting RSA encryption.
                    • Molecular Simulation: Simulates quantum chemistry for catalyst design and breakthrough pharmaceuticals.
                    • Combinatorial Optimization: Solves complex logistics and grid distribution challenges.
                    """.trimIndent(),
                    subjectTag = "Quantum Computing"
                )
            }
        }

        // =========================================================================
        // 6. PHYSICS, ASTRONOMY & RELATIVITY
        // =========================================================================
        // --- Speed of Light (c) ---
        if (qLower.contains("speed of light") || query.contains("আলোর বেগ") || query.contains("আলোর গতি") || normalizedLower.contains("alor beg") || qLower.contains("value of c")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    আলোর বেগ (Speed of Light - c):

                    ১. সুনির্দিষ্ট মান:
                    • নির্ভুল মান: ২৯৯,৭৯২,৪৫৮ মিটার/সেকেন্ড (m/s)
                    • সাধারণ হিসাব: প্রায় ৩ × ১০⁸ মি./সে. বা প্রায় ৩,০০,০০০ কিলোমিটার/সেকেন্ড।
                    • মাইল হিসেবে: প্রায় ১,৮৬,২৮২ মাইল প্রতি সেকেন্ড।

                    ২. মহাজাগতিক তাৎপর্য:
                    • মহাবিশ্বের সর্বোচ্চ গতির সীমা: আলবার্ট আইনস্টাইনের বিশেষ আপেক্ষিকতা তত্ত্ব অনুসারে, মহাবিশ্বে ভরযুক্ত কোনো বস্তু বা তথ্য আলোর চেয়ে দ্রুত গতিতে চলতে পারে না।
                    • ভর-শক্তি সমীকরণ: বিখ্যাত সমীকরণ E = mc², যেখানে c হলো ভরকে শক্তিতে রূপান্তরের গুণক।
                    • অপরিবর্তনশীলতা: শূন্যস্থানে পর্যবেক্ষকের গতি যাই হোক না কেন, আলোর বেগ সর্বদা ধ্রুব থাকে।
                    """.trimIndent(),
                    subjectTag = "পদার্থবিজ্ঞান (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    The Speed of Light in Vacuum (c)

                    1. Exact Numerical Value:
                    c = 299,792,458 meters per second (m/s) (defined exactly in SI units since 1983).
                    • Approximation: ~3.00 × 10⁸ m/s, or 300,000 km/s (186,282 miles/second).

                    2. Relativistic Significance:
                    • Cosmic Speed Limit: According to Special Relativity, no information or mass-bearing particle can accelerate to or exceed c in a vacuum.
                    • Mass-Energy Equivalence: Expressed in Einstein's E = mc², where c² represents the tremendous energy density bound within physical matter.
                    • Universal Invariance: The speed of light is invariant in all inertial reference frames, serving as the foundational postulate for spacetime geometry.
                    """.trimIndent(),
                    subjectTag = "Physics & Astrophysics"
                )
            }
        }

        // --- Black Holes ---
        if (qLower.contains("black hole") || query.contains("ব্ল্যাক হোল") || query.contains("কৃষ্ণগহ্বর") || normalizedLower.contains("black hole")) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    ব্ল্যাক হোল বা কৃষ্ণগহ্বর:

                    ১. ব্ল্যাক হোল কী?
                    ব্ল্যাক হোল হলো মহাশূন্যের এমন একটি অতি ঘন অঞ্চল, যার মহাকর্ষীয় আকর্ষণ এতটাই শক্তিশালী যে আলো সহ কোনো কণা বা তথ্য এর ভেতর থেকে বের হতে পারে না।

                    ২. প্রধান অংশসমূহ:
                    • ইভেন্ট হরাইজন (ঘটনা দিগন্ত): এটি ব্ল্যাক হোলের চারপাশের অদৃশ্য সীমানা। একবার এই সীমানা পার হলে কোনো কিছুই আর ফিরে আসতে পারে না।
                    • সিঙ্গুলারিটি (Singularity): ব্ল্যাক হোলের একেবারে কেন্দ্রবিন্দু, যেখানে সমস্ত ভর একটি শূন্য আয়তনের অতি ক্ষুদ্র বিন্দুতে সংকুচিত থাকে এবং ঘনত্ব অসীম।
                    • অ্যাক্রিশন ডিস্ক: ব্ল্যাক হোলের মহাকর্ষে আটকে পড়া গ্যাস ও ধূলিকণার ঘূর্ণায়মান উজ্জ্বল বলয়, যা থেকে তীব্র এক্স-রে বিকিরিত হয়।

                    ৩. কীভাবে তৈরি হয়?
                    বিশাল ভরের কোনো নক্ষত্র (সূর্যের ভরের তুলনায় বহু গুণ বড়) যখন তার জীবনের শেষ প্রান্তে জ্বালানি শেষ করে ফেলে, তখন এটি সুপারনোভা বিস্ফোরণের মধ্য দিয়ে নিজের মহাকর্ষের কারণে সংকুচিত হয়ে ব্ল্যাক হোলে পরিণত হয়।
                    """.trimIndent(),
                    subjectTag = "জ্যোতির্বিজ্ঞান (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    Black Holes: General Relativity & Astrophysics

                    1. What is a Black Hole?
                    A black hole is a region of spacetime where gravity is so intense that nothing—neither particles nor electromagnetic waves such as light—can escape its gravitational pull.

                    2. Anatomical Components:
                    • Event Horizon: The mathematical boundary beyond which escape velocity exceeds the speed of light.
                    • Singularity: The gravitational center where matter is crushed into theoretical infinite density and zero volume.
                    • Accretion Disk: Superheated gas, plasma, and debris orbiting the black hole at relativistic velocities, emitting intense X-ray spectra.
                    • Relativistic Jets: High-energy particle beams ejected along the rotational axis.

                    3. Classification by Mass:
                    • Stellar Black Holes: Formed by the core-collapse supernova of massive stars (5–50 solar masses).
                    • Supermassive Black Holes (SMBHs): Anchored at the centers of galaxies (millions to billions of solar masses, e.g., Sagittarius A* at the Milky Way core).
                    """.trimIndent(),
                    subjectTag = "Astrophysics & Cosmology"
                )
            }
        }

        // =========================================================================
        // 7. MATHEMATICAL CONSTANTS & CALCULUS
        // =========================================================================
        // --- Mathematical Constant Pi (π) ---
        val asksAboutPi = Regex("(^|[^a-z])pi($|[^a-z])").containsMatchIn(qLower) ||
            qLower.contains("π") ||
            Regex("(^|[\\s(])পাই($|[\\s?।),-])").containsMatchIn(normalizedLower)
        if (asksAboutPi) {
            if (isBangla) {
                return QwenResponse(
                    query = query,
                    text = """
                    পাই (π)-এর মান ও গাণিতিক তাৎপর্য:

                    ১. সাংখ্যিক মান:
                    • দশমিক মান: π ≈ ৩.১৪১৫৯২৬৫৩৫৮৯৭৯৩... (সাধারণ হিসেবে ৩.১৪১৬ ব্যবহার করা হয়)
                    • সাধারণ ভগ্নাংশ মান: ২২ / ৭ (প্রায় ৩.১৪২৮৫৭)
                    • অধিকতর নির্ভুল ভগ্নাংশ: ৩৫৫ / ১১৩ (দশমিকের পর ৬ ঘর পর্যন্ত শতভাগ সঠিক)

                    ২. গাণিতিক সংজ্ঞা:
                    যেকোনো আকারের বৃত্তের পরিধি (C) এবং তার ব্যাস (d)-এর অনুপাত সর্বদা একটি নির্দিষ্ট ধ্রুবক, যাকে গ্রিক অক্ষর পাই (π) বলা হয়:
                    π = পরিধি ÷ ব্যাস (C / d)

                    ৩. গাণিতিক বৈশিষ্ট্য:
                    • অমূলদ সংখ্যা (Irrational Number): পাই-এর মানকে দুটি পূর্ণসংখ্যার ভগ্নাংশ আকারে নিখুঁতভাবে প্রকাশ করা যায় না; এর দশমিক অঙ্কগুলো অসীম পর্যন্ত কোনো পুনরাবৃত্তি ছাড়াই চলতে থাকে।
                    • তুরীয় সংখ্যা (Transcendental): এটি মূলত কোনো বীজগাণিতিক সমীকরণের মূল বা সমাধান হতে পারে না।

                    ৪. বাস্তব প্রয়োগ:
                    • জ্যামিতি ও ত্রিকোণমিতি: বৃত্তের ক্ষেত্রফল (A = πr²), গোলকের আয়তন (V = ৪/৩ πr³)।
                    • আধুনিক বিজ্ঞান: মহাকাশযান নেভিগেশন, কোয়ান্টাম বলবিদ্যা, অল্টারনেটিং কারেন্ট (AC) সার্কিট ও কম্পিউটার পারফরম্যান্স টেস্টিং।
                    """.trimIndent(),
                    subjectTag = "গণিত (অফলাইন তথ্য)"
                )
            } else {
                return QwenResponse(
                    query = query,
                    text = """
                    The Value and Significance of Pi (π)

                    1. Numerical Values & Approximations:
                    • Decimal Expansion: π ≈ 3.14159265358979323846...
                    • Standard Value: 3.1416
                    • Fractional Form: 22 / 7 (≈ 3.142857)
                    • High-Precision Fraction: 355 / 113 (accurate to 6 decimal places)

                    2. Mathematical Definition:
                    Pi (π) is the fundamental mathematical constant representing the ratio of a circle's circumference (C) to its diameter (d):
                    π = C / d

                    3. Foundational Properties:
                    • Irrational: Cannot be expressed as a ratio of integers; its decimal places extend infinitely without a repeating pattern.
                    • Transcendental: Not the root of any non-zero polynomial equation with rational coefficients (proven by Ferdinand von Lindemann, 1882).

                    4. Applications across Science:
                    • Geometry: Circular area (A = πr²), sphere volume (V = 4/3 πr³).
                    • Engineering & Physics: Fourier transforms, wave mechanics, alternating currents, and gravitational wave modeling.
                    """.trimIndent(),
                    subjectTag = "Mathematics"
                )
            }
        }

        // =========================================================================
        // 8. GENERAL INTELLIGENT SYNTHESIZER (DEEP FACTUAL REASONING)
        // =========================================================================
        return synthesizeGeneralQwenAnswer(query, qLower)
    }

    private fun synthesizeGeneralQwenAnswer(query: String, qLower: String): QwenResponse {
        val hasBengali = query.any { it in '\u0980'..'\u09FF' }
        val banglishInfo = AloBanglishNormalizer.normalizeBanglish(query)
        val isBangla = hasBengali || banglishInfo.isBanglish
        // A local keyword router is not a language model. Never fabricate a
        // polished answer for a fact it cannot verify.
        return if (isBangla) {
            QwenResponse(
                query = query,
                text = "এই প্রশ্নটির জন্য এই অফলাইন সংস্করণে যাচাইকৃত উত্তর নেই। প্রশ্নটি পাঠ্যবইয়ের অধ্যায়, বিষয় বা নির্দিষ্ট তথ্যসহ আবার লিখুন। অনলাইন এআই শিক্ষক চালু থাকলে আমি সেটির সাহায্যে উত্তর দিতে পারব।",
                modelName = "অফলাইন সহায়ক",
                subjectTag = "যাচাইকৃত উত্তর প্রয়োজন"
            )
        } else {
            QwenResponse(
                query = query,
                text = "This offline version does not have a verified answer for that question. Please add the subject or textbook context, or use the online AI tutor.",
                modelName = "Offline helper",
                subjectTag = "Verified answer needed"
            )
        }
        val domain = determineQueryDomain(qLower, isBangla)

        if (isBangla) {
            val response = """
            ${domain}-বিষয়ক একাডেমিক বিশ্লেষণ ও তথ্য:
            প্রশ্ন: $query

            ১. মূল ধারণা ও সংজ্ঞা (Core Concept & Definition):
            $query—বিষয়টি ${domain}-এর একটি অত্যন্ত গুরুত্বপূর্ণ মৌলিক প্রত্যয়। এটি সংশ্লিষ্ট বৈজ্ঞানিক, ঐতিহাসিক বা সামাজিক নিয়মাবলীর ওপর প্রতিষ্ঠিত এবং এর সুনির্দিষ্ট তাত্ত্বিক ভিত্তি রয়েছে।

            ২. মূল স্তম্ভ ও কার্যপ্রণালী (Key Components & Mechanics):
            • গঠন ও কাঠামো: কোনো একক সূত্রের ওপর নির্ভর না করে এটি একাধিক আন্তঃসম্পর্কিত চলক ও প্রক্রিয়ার সমন্বয়ে পরিচালিত হয়।
            • কার্যকারণ সম্পর্ক: বাহ্যিক পরিবর্তন, পরিবেশগত প্রভাব এবং সুনির্দিষ্ট নিয়মের মিথস্ক্রিয়ার মাধ্যমে এই ধারণাটি কাজ করে।
            • তুলনামূলক প্রাসঙ্গিকতা: সাধারণ ধারণার তুলনায় আধুনিক প্রাতিষ্ঠানিক গবেষণায় এর প্রভাব সুস্পষ্ট ও সুদূরপ্রসারী।

            ৩. বাস্তব প্রয়োগ ও বৈশ্বিক গুরুত্ব:
            • বাস্তব জীবনের ক্ষেত্রে এটি নীতি নির্ধারণ, সমস্যা সমাধান এবং উদ্ভাবনী চিন্তায় অপরিহার্য ভূমিকা রাখে।
            • জাতীয় ও আন্তর্জাতিক পরিমণ্ডলে এই বিষয়ের অনুধাবন ভবিষ্যৎ গবেষণার নতুন পথ উন্মোচন করে।

            ৪. সারসংক্ষেপ (Summary):
            $query সম্পর্কিত যেকোনো সুনির্দিষ্ট সমীকরণ, ঐতিহাসিক ঘটনা বা সম্পূরক প্রশ্ন জানতে চাইলে নির্দ্বিধায় লিখুন।
            """.trimIndent()

            return QwenResponse(
                query = query,
                text = response,
                subjectTag = "$domain (অফলাইন তথ্য)"
            )
        }

        val response = """
        Academic Breakdown by Qwen 2.5
        Topic: $query

        1. Definition & Fundamental Concept:
        "$query" is a foundational topic within the domain of $domain. It represents a structured discipline governed by established theoretical principles, empirical evidence, and systematic frameworks.

        2. Core Components & Operational Mechanics:
        • Structural Foundations: The subject relies on defined axioms, verifiable cause-and-effect relationships, and institutional methodologies.
        • Dynamic Interactions: Rather than functioning in isolation, it continually interacts with broader environmental, technological, or socioeconomic systems.
        • Analytical Rigor: Evaluating this topic requires distinguishing between theoretical models and observed real-world manifestations.

        3. Real-World Applications & Global Significance:
        • Practical Impact: Deployed globally across strategic planning, scientific research, and policy formulation.
        • Contemporary Relevance: Informs critical decision-making in modern technology, governance, and academic scholarship.

        4. Summary & Further Exploration:
        Understanding "$query" provides essential context in $domain. Feel free to ask about specific sub-theories, case studies, or mathematical equations related to this topic.
        """.trimIndent()

        return QwenResponse(
            query = query,
            text = response,
            subjectTag = "$domain (offline reference)"
        )
    }

    private fun determineQueryDomain(qLower: String, isBangla: Boolean = false): String {
        if (isBangla) {
            return when {
                qLower.contains("রাজনীতি") || qLower.contains("রাষ্ট্র") || qLower.contains("যুদ্ধ") || qLower.contains("কূটনীতি") -> "আন্তর্জাতিক সম্পর্ক ও ভূরাজনীতি"
                qLower.contains("ইতিহাস") || qLower.contains("আন্দোলন") || qLower.contains("বিপ্লব") -> "বিশ্ব ইতিহাস ও ঐতিহ্য"
                qLower.contains("অর্থনীতি") || qLower.contains("টাকা") || qLower.contains("ব্যাংক") || qLower.contains("মুদ্রা") -> "অর্থনীতি ও বাণিজ্য"
                qLower.contains("ভূগোল") || qLower.contains("নদী") || qLower.contains("মহাসাগর") || qLower.contains("পাহাড়") -> "ভূগোল ও পরিবেশবিদ্যা"
                qLower.contains("কম্পিউটার") || qLower.contains("সফটওয়্যার") || qLower.contains("প্রোগ্রাম") || qLower.contains("এআই") -> "কম্পিউটার বিজ্ঞান ও তথ্যপ্রযুক্তি"
                qLower.contains("পদার্থ") || qLower.contains("শক্তি") || qLower.contains("মহাকাশ") || qLower.contains("আলো") -> "পদার্থবিজ্ঞান ও মহাকাশবিদ্যা"
                qLower.contains("জীব") || qLower.contains("কোষ") || qLower.contains("মানবদেহ") || qLower.contains("উদ্ভিদ") -> "জীববিজ্ঞান ও চিকিৎসাবিজ্ঞান"
                else -> "উন্মুক্ত বৈশ্বিক জ্ঞান"
            }
        }
        return when {
            qLower.contains("politic") || qLower.contains("war") || qLower.contains("state") || qLower.contains("diplomacy") || qLower.contains("nation") -> "Geopolitics & Strategic Studies"
            qLower.contains("history") || qLower.contains("revolution") || qLower.contains("empire") || qLower.contains("civilization") -> "World History & Civilization"
            qLower.contains("economy") || qLower.contains("finance") || qLower.contains("market") || qLower.contains("money") || qLower.contains("bank") -> "Economics & Global Finance"
            qLower.contains("geography") || qLower.contains("ocean") || qLower.contains("mountain") || qLower.contains("river") || qLower.contains("climate") -> "Geography & Earth Sciences"
            qLower.contains("code") || qLower.contains("software") || qLower.contains("algorithm") || qLower.contains("computer") || qLower.contains("ai") || qLower.contains("data") -> "Computer Science & Artificial Intelligence"
            qLower.contains("physics") || qLower.contains("energy") || qLower.contains("space") || qLower.contains("gravity") || qLower.contains("light") -> "Physics & Astrophysics"
            qLower.contains("biology") || qLower.contains("dna") || qLower.contains("cell") || qLower.contains("brain") || qLower.contains("body") -> "Biology & Life Sciences"
            else -> "General Knowledge & Academic Studies"
        }
    }
}
