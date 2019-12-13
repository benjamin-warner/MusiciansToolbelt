package com.benjaminnwarner.musicianstoolbelt.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.util.RecorderConstants
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*


class RecordingViewModel(
    private val recordingRepository: RecordingRepository,
    private val id: Long,
    application: Application
) : AndroidViewModel(application) {

    private val recordingState = MutableLiveData<Recording>()
    val recording: LiveData<Recording> = recordingState

    init {
        if(id == 0L) {
            val localTempFile = "${application.filesDir.absolutePath}/${RecorderConstants.DEFAULT_NEW_RECORDING_FILENAME}"
            recordingState.value = Recording(id = id, filename = localTempFile)
        } else {
            viewModelScope.launch {
                recordingState.value = recordingRepository.getRecording(id)
            }
        }
    }

    fun update() {
        if(recordingState.value?.id == 0L) {
            viewModelScope.launch {
                try {
                    val temp = File(recordingState.value?.filename!!)
                    val filename = "${temp.lastModified()}.m4a"
                    val persistent =
                        File(getApplication<Application>().filesDir.absolutePath, filename)
                    temp.renameTo(persistent)
                    val newRecordingId = recordingRepository.saveRecording(
                        Recording(
                            recordingState.value!!.id,
                            Date(),
                            persistent.absolutePath
                        )
                    )
                    recordingState.value = recordingRepository.getRecording(newRecordingId)
                } catch (e: IOException) {
                    throw e
                }
            }
        }
    }
}
