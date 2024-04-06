package com.example.mangareaderui.domain.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class BookmarkDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        fun getDatabase(context: Context): BookmarkDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java,
                    "bookmark_db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}