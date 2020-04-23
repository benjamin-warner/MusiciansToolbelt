package com.benjaminnwarner.musicianstoolbelt.database.song

class SongRepository private constructor(private val recordingDao: SongDao) {

    suspend fun getSongs() = recordingDao.getSongs()

    suspend fun getSong(id: Long) = recordingDao.getSong(id)

    suspend fun saveSong(recording: Song) = recordingDao.insert(recording)

    suspend fun delete(recording: Song) = recordingDao.delete(recording)

    companion object {
        @Volatile private var instance: SongRepository? = null

        fun getInstance(songDao: SongDao) =
            instance ?: synchronized(this) {
                instance ?: SongRepository(songDao).also { instance = it }
            }
    }
}
