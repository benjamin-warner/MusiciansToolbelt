package com.benjaminnwarner.musicianstoolbelt.database.song

import androidx.room.*

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY id")
    suspend fun getSongs(): List<Song>

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSong(id: Long): Song

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recording: Song): Long

    @Delete
    suspend fun delete(recording: Song)
}