package com.benjaminnwarner.musicianstoolbelt.database.recording

class RecordingRepository private constructor(private val recordingDao: RecordingDao) {

    suspend fun getRecordings() = recordingDao.getRecordings()

    suspend fun getRecording(id: Int) = recordingDao.getRecording(id)

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
