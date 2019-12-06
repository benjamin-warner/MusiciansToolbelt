package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository

class RecordingListViewModelFactory(
    private val recordingRepository: RecordingRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecordingListViewModel(recordingRepository) as T
    }
}
