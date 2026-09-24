package com.example.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/** Aggregate product events only. Never pass names, notes, questions or answers. */
object AaloAnalytics {
    const val SCHEMA_VERSION = "1"

    object Events {
        const val ONBOARDING_COMPLETED = "onboarding_completed"
        const val AUDIENCE_SELECTED = "audience_selected"
        const val TUTOR_QUESTION = "tutor_question"
        const val TUTOR_ANSWER = "tutor_answer"
        const val TUTOR_ERROR = "tutor_error"
        const val CLASS_SELECTED = "class_selected"
        const val TOPIC_OPENED = "topic_opened"
        const val TOPIC_COMPLETED = "topic_completed"
        const val NOTE_CREATED = "note_created"
        const val QUIZ_ANSWERED = "quiz_answered"
        const val QUIZ_COMPLETED = "quiz_completed"
    }

    private var firebaseAnalytics: FirebaseAnalytics? = null

    /**
     * Every AaloPro app must call this with a stable product id. Never change an id after release.
     * Firebase automatically adds app id, app version, platform and device dimensions.
     */
    fun initialize(context: Context, productId: String) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context.applicationContext).also { analytics ->
            analytics.setDefaultEventParameters(Bundle().apply {
                putString("product_family", "aalopro")
                putString("product_id", productId)
                putString("metrics_schema", SCHEMA_VERSION)
            })
        }
    }

    fun screen(name: String) {
        firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
        })
    }

    fun event(name: String, vararg dimensions: Pair<String, String>) {
        require(name in supportedEvents) { "Unsupported AaloPro Analytics event: $name" }
        firebaseAnalytics?.logEvent(name, Bundle().apply {
            dimensions.forEach { (key, value) -> putString(key, value) }
        })
    }

    private val supportedEvents = setOf(
        Events.ONBOARDING_COMPLETED,
        Events.AUDIENCE_SELECTED,
        Events.TUTOR_QUESTION,
        Events.TUTOR_ANSWER,
        Events.TUTOR_ERROR,
        Events.CLASS_SELECTED,
        Events.TOPIC_OPENED,
        Events.TOPIC_COMPLETED,
        Events.NOTE_CREATED,
        Events.QUIZ_ANSWERED,
        Events.QUIZ_COMPLETED,
    )
}
