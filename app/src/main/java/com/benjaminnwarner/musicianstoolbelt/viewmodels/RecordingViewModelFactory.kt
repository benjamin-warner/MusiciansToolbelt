package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository


class RecordingViewModelFactory(
    private val recordingRepository: RecordingRepository,
    private val id: Long
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecordingViewModel(recordingRepository, id) as T
    }
}
