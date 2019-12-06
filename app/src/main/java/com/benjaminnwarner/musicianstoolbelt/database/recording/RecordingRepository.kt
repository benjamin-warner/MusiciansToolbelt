package com.benjaminnwarner.musicianstoolbelt.database.recording

class RecordingRepository private constructor(private val recordingDao: RecordingDao) {

    fun getRecordings() = recordingDao.getRecordings()

    fun getRecording(id: Long) = recordingDao.getRecording(id)

    suspend fun saveRecording(recording: Recording) = recordingDao.insert(recording)

    suspend fun delete(recording: Recording) = recordingDao.delete(recording)

    companion object {
        @Volatile private var instance: RecordingRepository? = null

        fun getInstance(recordingDao: RecordingDao) =
            instance ?: synchronized(this) {
                instance ?: RecordingRepository(recordingDao).also { instance = it }
            }
    }
}
