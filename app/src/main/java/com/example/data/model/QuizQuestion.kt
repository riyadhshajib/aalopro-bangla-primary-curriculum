package com.example.data.model

data class QuizQuestion(
    val id: Int,
    val subject: String,
    val questionBangla: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationBangla: String,
    val grade: Int = 0,
    val source: String = ""
)
