package com.example.ai.retrieval

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.data.model.FullOfflineQuestionBank
import com.example.data.model.VerifiedAnswerRetrievalResult
import kotlin.concurrent.thread

/** Debug-only end-to-end probe. It is absent from release builds. */
class E5DebugProofActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thread(name = "e5-main-proof") {
            val provider = E5VerifiedAnswerCandidateProvider.get(this)
            val bengali = FullOfflineQuestionBank.retrieveVerified(
                2,
                "আমিন শহরে কী দেখে অবাক হয়েছিল",
                provider,
            )
            val banglish = FullOfflineQuestionBank.retrieveVerified(
                2,
                "amin shohore ki dekhe obak hoyechilo",
                provider,
            )
            Log.i(TAG, "raw bn=$bengali banglish=$banglish")
            val bengaliMatch = bengali as? VerifiedAnswerRetrievalResult.Match
            check(bengaliMatch?.kind == VerifiedAnswerRetrievalResult.Kind.SEMANTIC)
            check(bengaliMatch.entry.question == "আমিন শহরে কী কী দেখে অবাক হয়েছে?")
            check(banglish is VerifiedAnswerRetrievalResult.Abstain)
            Log.i(
                TAG,
                "PASS bn=SEMANTIC question=${bengaliMatch.entry.question} " +
                    "banglish=ABSTAIN reason=${banglish.reason}",
            )
            finish()
        }
    }

    companion object { private const val TAG = "AALO_E5_PROOF" }
}
