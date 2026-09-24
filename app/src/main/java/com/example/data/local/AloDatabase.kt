package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AloChatEntity::class, AloNoteEntity::class, AloStudyStatEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AloDatabase : RoomDatabase() {
    abstract fun aloDao(): AloDao

    companion object {
        @Volatile
        private var INSTANCE: AloDatabase? = null

        fun getDatabase(context: Context): AloDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AloDatabase::class.java,
                    "alopro_study_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
