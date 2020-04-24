package com.benjaminnwarner.musicianstoolbelt.database.song

class SongRepository private constructor(private val songDao: SongDao) {

    suspend fun getSongs() = songDao.getSongs()

    suspend fun getSong(id: Long) = songDao.getSong(id)

    suspend fun saveSong(recording: Song) = songDao.insert(recording)

    suspend fun delete(recording: Song) = songDao.delete(recording)

    companion object {
        @Volatile private var instance: SongRepository? = null

        fun getInstance(songDao: SongDao) =
            instance ?: synchronized(this) {
                instance ?: SongRepository(songDao).also { instance = it }
            }
    }
}
