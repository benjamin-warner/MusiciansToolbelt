package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import kotlinx.coroutines.launch

class RecordingViewModel(
    private val recordingRepository: RecordingRepository,
    id: Long
) : ViewModel() {

    private val savedState = MutableLiveData<Boolean>().apply { value = false }
    val saved: LiveData<Boolean> = savedState

    val recording: LiveData<Recording> = recordingRepository.getRecording(id)

    fun createRecordingRecord(filename: String) {
        viewModelScope.launch {
            val id = recordingRepository.saveRecording(Recording(filename = filename))
            if(id != -1L){
                savedState.value = true
            }
        }
    }
}
