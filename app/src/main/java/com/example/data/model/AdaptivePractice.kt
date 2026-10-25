package com.example.data.model

import android.content.SharedPreferences
import org.json.JSONObject
import org.json.JSONArray

/** Provisional Bayesian learner estimate. No network or model download. */
class AdaptivePractice(private val prefs: SharedPreferences) {
    data class State(val probability: Double = .2, val independent: Int = 0)
    private fun key(child: String, grade: Int) = "adaptive.v1.$child.$grade"
    private fun read(child: String, grade: Int): JSONObject {
        val raw = prefs.getString(key(child, grade), null) ?: return JSONObject()
        return runCatching { JSONObject(raw) }.getOrElse {
            prefs.edit().putString(key(child, grade) + ".recovery", raw).apply()
            JSONObject()
        }
    }
    fun state(child: String, grade: Int, skill: String): State {
        val row = read(child, grade).optJSONObject("skills")?.optJSONObject(skill) ?: return State()
        val p = row.optDouble("p", .2)
        return State(if (p.isFinite()) p.coerceIn(.01,.99) else .2, row.optInt("n",0).coerceAtLeast(0))
    }
    fun record(child: String, grade: Int, question: QuizQuestion, correct: Boolean, assisted: Boolean) {
        require(child.isNotBlank() && grade in 1..5)
        val data=read(child, grade)
        val seen=data.optJSONArray("seen") ?: JSONArray()
        if ((0 until seen.length()).any { seen.optInt(it)==question.id }) return
        seen.put(question.id); data.put("seen",seen)
        if (!assisted) {
            val old=state(child, grade, question.subject)
            val guess=1.0/question.options.size
            val known=old.probability * if(correct) .9 else .1
            val unknown=(1-old.probability) * if(correct) guess else (1-guess)
            val posterior=known/(known+unknown)
            val skills=data.optJSONObject("skills") ?: JSONObject()
            skills.put(question.subject, JSONObject().put("p",(posterior+(1-posterior)*.08).coerceIn(.01,.99)).put("n",old.independent+1))
            data.put("skills",skills)
        }
        prefs.edit().putString(key(child, grade),data.toString()).apply()
    }
    fun order(child: String, grade: Int, questions: List<QuizQuestion>): List<QuizQuestion> {
        val seen=read(child, grade).optJSONArray("seen") ?: JSONArray()
        val ids=(0 until seen.length()).map { seen.optInt(it) }.toSet()
        return questions.sortedWith(compareBy<QuizQuestion> { it.id in ids }
            .thenBy { state(child,grade,it.subject).probability })
    }
}
