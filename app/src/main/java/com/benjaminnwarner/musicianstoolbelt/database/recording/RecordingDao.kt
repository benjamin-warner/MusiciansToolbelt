package com.benjaminnwarner.musicianstoolbelt.database.recording

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecordingDao {
    @Query("SELECT * FROM recordings ORDER BY created_at")
    suspend fun getRecordings(): List<Recording>

    @Query("SELECT * FROM recordings WHERE id = :id")
    suspend fun getRecording(id: Long): Recording

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recording: Recording): Long

    @Delete
    suspend fun delete(recording: Recording)
}
