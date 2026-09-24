package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AloDao {
    // Chats
    @Query("SELECT * FROM alo_chats ORDER BY timestamp DESC")
    fun getAllChats(): Flow<List<AloChatEntity>>

    @Query("SELECT * FROM alo_chats WHERE classCategory = :category ORDER BY timestamp DESC")
    fun getChatsByCategory(category: String): Flow<List<AloChatEntity>>

    @Query("SELECT * FROM alo_chats WHERE id = :id LIMIT 1")
    suspend fun getChatById(id: Long): AloChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: AloChatEntity): Long

    @Query("DELETE FROM alo_chats WHERE id = :id")
    suspend fun deleteChat(id: Long)

    @Query("DELETE FROM alo_chats WHERE classCategory = :category")
    suspend fun clearChatsByCategory(category: String)

    @Query("DELETE FROM alo_chats")
    suspend fun clearAllChats()

    @Update
    suspend fun updateChat(chat: AloChatEntity)

    // Notes
    @Query("SELECT * FROM alo_notes ORDER BY isPinned DESC, createdAt DESC")
    fun getAllNotes(): Flow<List<AloNoteEntity>>

    @Query("SELECT * FROM alo_notes WHERE subjectTag = :subject ORDER BY isPinned DESC, createdAt DESC")
    fun getNotesBySubject(subject: String): Flow<List<AloNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: AloNoteEntity): Long

    @Update
    suspend fun updateNote(note: AloNoteEntity)

    @Query("DELETE FROM alo_notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query("DELETE FROM alo_notes")
    suspend fun clearAllNotes()

    // Stats
    @Query("SELECT * FROM alo_study_stats WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getStatsForDate(dateKey: String): AloStudyStatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStat(stat: AloStudyStatEntity)

    @Query("DELETE FROM alo_study_stats")
    suspend fun clearAllStats()
}
