package com.benjaminnwarner.musicianstoolbelt.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository


class RecordingViewModelFactory(
    private val recordingRepository: RecordingRepository,
    private val id: Long,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecordingViewModel(recordingRepository, id, application) as T
    }
}
