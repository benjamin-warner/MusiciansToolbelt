package com.benjaminnwarner.musicianstoolbelt.database.recording

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RecordingDao {
    @Query("SELECT * FROM recordings ORDER BY id")
    fun getRecordings(): LiveData<List<Recording>>

    @Query("SELECT * FROM recordings WHERE id = :id")
    suspend fun getRecording(id: Long): Recording

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recording: Recording): Long

    @Delete
    suspend fun delete(recording: Recording)
}
