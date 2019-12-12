package com.benjaminnwarner.musicianstoolbelt.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.util.RecorderConstants
import kotlinx.coroutines.launch


class RecordingViewModel(
    private val recordingRepository: RecordingRepository,
    private val id: Long,
    application: Application
) : AndroidViewModel(application) {

    private val recordingState = MutableLiveData<Recording>()
    val recording: LiveData<Recording> = recordingState

    init {
        if(id == -1L) {
            val defaultFilename = "${application.filesDir.absolutePath}/${RecorderConstants.DEFAULT_NEW_RECORDING_FILENAME}"
            recordingState.value = Recording(id = id, filename = defaultFilename)
        } else {
            viewModelScope.launch {
                recordingState.value = recordingRepository.getRecording(id)
            }
        }
    }

    fun update(filename: String) {

    }
}
