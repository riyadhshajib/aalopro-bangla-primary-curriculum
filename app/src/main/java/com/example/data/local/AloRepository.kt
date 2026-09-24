package com.example.data.local

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AloRepository(private val dao: AloDao) {
    val allChats: Flow<List<AloChatEntity>> = dao.getAllChats()
    val allNotes: Flow<List<AloNoteEntity>> = dao.getAllNotes()

    fun getChatsByCategory(category: String): Flow<List<AloChatEntity>> = dao.getChatsByCategory(category)

    suspend fun getChatById(id: Long): AloChatEntity? = dao.getChatById(id)

    suspend fun saveChat(
        query: String,
        response: String,
        subjectTag: String,
        classCategory: String = "CLASS_1",
        chapterTitle: String = "",
        parentChatId: Long? = null
    ): Long {
        val chat = AloChatEntity(
            query = query,
            responseBangla = response,
            subjectTag = subjectTag,
            classCategory = classCategory,
            chapterTitle = chapterTitle,
            parentChatId = parentChatId
        )
        val id = dao.insertChat(chat)
        recordQueryAnswered()
        return id
    }

    suspend fun deleteChat(id: Long) = dao.deleteChat(id)
    suspend fun clearChats() = dao.clearAllChats()
    suspend fun clearLocalStudyDataForNewProfile() {
        dao.clearAllChats()
        dao.clearAllNotes()
        dao.clearAllStats()
    }
    suspend fun clearChatsByCategory(category: String) = dao.clearChatsByCategory(category)

    suspend fun markChatHelpful(chat: AloChatEntity, isHelpful: Boolean) {
        dao.updateChat(chat.copy(isHelpful = isHelpful))
    }

    suspend fun saveChatAsNote(chat: AloChatEntity): Long {
        val note = AloNoteEntity(
            title = chat.query.take(60),
            content = chat.responseBangla,
            subjectTag = chat.subjectTag
        )
        dao.updateChat(chat.copy(isSavedToNotes = true))
        return dao.insertNote(note)
    }

    suspend fun createCustomNote(title: String, content: String, subjectTag: String): Long {
        val note = AloNoteEntity(
            title = title,
            content = content,
            subjectTag = subjectTag
        )
        return dao.insertNote(note)
    }

    suspend fun togglePinNote(note: AloNoteEntity) {
        dao.updateNote(note.copy(isPinned = !note.isPinned))
    }

    suspend fun deleteNote(id: Long) = dao.deleteNote(id)

    private fun getTodayDateKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    private suspend fun recordQueryAnswered() {
        val dateKey = getTodayDateKey()
        val current = dao.getStatsForDate(dateKey)
        if (current != null) {
            dao.insertOrUpdateStat(current.copy(queriesAnswered = current.queriesAnswered + 1))
        } else {
            dao.insertOrUpdateStat(AloStudyStatEntity(dateKey = dateKey, queriesAnswered = 1))
        }
    }

    suspend fun recordQuizResult(score: Int) {
        val dateKey = getTodayDateKey()
        val current = dao.getStatsForDate(dateKey)
        if (current != null) {
            dao.insertOrUpdateStat(
                current.copy(
                    quizzesCompleted = current.quizzesCompleted + 1,
                    quizScoreSum = current.quizScoreSum + score
                )
            )
        } else {
            dao.insertOrUpdateStat(
                AloStudyStatEntity(
                    dateKey = dateKey,
                    quizzesCompleted = 1,
                    quizScoreSum = score
                )
            )
        }
    }

    suspend fun getTodayStats(): AloStudyStatEntity {
        val dateKey = getTodayDateKey()
        return dao.getStatsForDate(dateKey) ?: AloStudyStatEntity(dateKey = dateKey)
    }
}
