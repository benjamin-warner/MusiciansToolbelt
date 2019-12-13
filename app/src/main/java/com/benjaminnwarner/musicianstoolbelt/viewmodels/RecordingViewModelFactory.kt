package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.wrappers.FileIOWrapper


class RecordingViewModelFactory(
    private val recordingRepository: RecordingRepository,
    private val id: Long,
    private val fileWriter: FileIOWrapper
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecordingViewModel(recordingRepository, id, fileWriter) as T
    }
}
