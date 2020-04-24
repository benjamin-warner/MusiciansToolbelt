package com.benjaminnwarner.musicianstoolbelt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.benjaminnwarner.musicianstoolbelt.database.migrations.MIGRATION_1_2
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingDao
import com.benjaminnwarner.musicianstoolbelt.database.song.Song
import com.benjaminnwarner.musicianstoolbelt.database.song.SongDao


const val DATABASE_NAME = "musicians-toolbelt-db"

@Database(entities = [Recording::class, Song::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordingDao(): RecordingDao
    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
        }
    }
}
