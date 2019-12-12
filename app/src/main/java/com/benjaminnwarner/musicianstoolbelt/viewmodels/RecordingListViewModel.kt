package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository

class RecordingListViewModel(
    recordingRepository: RecordingRepository
) : ViewModel() {

    val recordings: LiveData<List<Recording>> =  liveData {
        emit(recordingRepository.getRecordings())
    }
}
