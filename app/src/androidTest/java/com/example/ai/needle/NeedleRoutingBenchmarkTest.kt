package com.example.ai.needle

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NeedleRoutingBenchmarkTest {
    @Test
    fun representativeMultilingualRoutes() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val system = context.assets.open("needle3/system.txt").bufferedReader().use { it.readText() }
        val tools = context.assets.open("needle3/tools.json").bufferedReader().use { it.readText() }
        check(AloNeedle3Native.loadAndInit(context.assets, system, tools).isNullOrBlank())

        val cases = listOf(
            "দ্বিতীয় শ্রেণির গণিত খোলো" to "open_curriculum",
            "class 3 English open koro" to "open_curriculum",
            "Open class 5 Science" to "open_curriculum",
            "এই পাঠ থেকে কুইজ দাও" to "start_quiz",
            "math quiz start koro" to "start_quiz",
            "Start a quiz from this lesson" to "start_quiz",
            "বুঝিনি সহজ করে বোঝাও" to "explain_again",
            "arekbar explain koro" to "explain_again",
            "Explain that again" to "explain_again",
            "বাচ্চাকে বাড়িতে কীভাবে পড়াব" to "parent_guide",
            "parent guide dekhao" to "parent_guide",
            "How can I teach my child at home" to "parent_guide",
            "শিক্ষকের পরামর্শ দেখাও" to "teacher_guide",
            "teacher guide dekhao" to "teacher_guide",
            "Give me a lesson plan" to "teacher_guide",
            "এই উত্তরটা নোটে রাখো" to "save_note",
            "eta save koro" to "save_note",
            "Save this answer as a note" to "save_note",
            "চাঁদ কেন দেখা যায়" to "general_online",
            "computer ki" to "general_online",
            "Who is the national poet of Bangladesh" to "general_online",
        )
        var correct = 0
        cases.forEach { (input, expected) ->
            AloNeedle3Native.reset()
            val raw = AloNeedle3Native.complete(input, 192)
            val root = JSONObject(raw)
            val call = root.optJSONArray("function_calls")?.optJSONObject(0)
            val action = call?.optJSONObject("arguments")?.optString("action").orEmpty()
            val actual = action.ifBlank { call?.optString("name").orEmpty() }
            println("NEEDLE_CASE expected=$expected actual=$actual input=$input raw=$raw")
            if (actual == expected) correct++
        }
        val accuracy = correct.toDouble() / cases.size
        println("NEEDLE_BENCHMARK correct=$correct cases=${cases.size} accuracy=$accuracy")
        assertTrue("Needle routing accuracy $accuracy is below 90%", accuracy >= 0.90)
    }
}
