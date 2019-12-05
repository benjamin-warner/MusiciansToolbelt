package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import kotlinx.coroutines.launch

class RecordingViewModel(
    private val recordingRepository: RecordingRepository,
    private val id: Int
) : ViewModel() {

    fun createRecordingRecord(filename: String) {
        viewModelScope.launch {
            recordingRepository.saveRecording(Recording(filename = filename))
        }
    }
}
