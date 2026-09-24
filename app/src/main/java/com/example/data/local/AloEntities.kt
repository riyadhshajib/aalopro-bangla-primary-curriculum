package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alo_chats")
data class AloChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val responseBangla: String,
    val subjectTag: String = "সাধারণ",
    val classCategory: String = "CLASS_1", // CLASS_1, CLASS_2, CLASS_3, CLASS_4, CLASS_5, UNIVERSAL, ALL
    val chapterTitle: String = "",
    val parentChatId: Long? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isSavedToNotes: Boolean = false,
    val isHelpful: Boolean? = null
)

@Entity(tableName = "alo_notes")
data class AloNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val subjectTag: String = "সাধারণ",
    val createdAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)

@Entity(tableName = "alo_study_stats")
data class AloStudyStatEntity(
    @PrimaryKey val dateKey: String, // YYYY-MM-DD
    val queriesAnswered: Int = 0,
    val quizzesCompleted: Int = 0,
    val quizScoreSum: Int = 0
)
