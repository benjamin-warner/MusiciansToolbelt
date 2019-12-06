package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository

class RecordingListViewModel(
    private val recordingRepository: RecordingRepository
) : ViewModel() {

    val recordings: LiveData<List<Recording>> = recordingRepository.getRecordings()
}
