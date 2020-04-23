package com.benjaminnwarner.musicianstoolbelt.database.song

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SongDao {
    @Query("SELECT * FROM recordings ORDER BY id")
    suspend fun getSongs(): LiveData<List<Song>>

    @Query("SELECT * FROM recordings WHERE id = :id")
    suspend fun getSong(id: Long): Song

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recording: Song): Long

    @Delete
    suspend fun delete(recording: Song)
}