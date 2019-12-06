package com.benjaminnwarner.musicianstoolbelt.views.recorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class RecorderViewModel : ViewModel() {
    val recording = MutableLiveData<Boolean>().apply {
        value = false
    }

    val playing = MutableLiveData<Boolean>().apply {
        value = false
    }

    val unsavedChanges = MutableLiveData<Boolean>().apply {
        value = false
    }
}
