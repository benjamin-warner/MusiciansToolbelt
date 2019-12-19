package com.benjaminnwarner.musicianstoolbelt.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.util.RecordingConstants
import com.benjaminnwarner.musicianstoolbelt.util.TimeHelpers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*


class RecordingViewModel(
    private val recordingRepository: RecordingRepository,
    private val id: Long,
    application: Application
) : AndroidViewModel(application) {

    private val _recording = MutableLiveData<Recording>()
    val recording: LiveData<Recording> = _recording
    val recordingPresent = MutableLiveData<Boolean>(false)
    private var recordingDirty = false

    private val filesDir = "${application.filesDir.absolutePath}/"
    private val tempRecordingPath = filesDir + RecordingConstants.DEFAULT_NEW_RECORDING_FILE

    init {
        viewModelScope.launch {
            if (id == 0L) {
                val filename = filesDir + TimeHelpers.unixToFileTimestamp(System.currentTimeMillis()) +
                        RecordingConstants.DEFAULT_RECORDING_EXTENSION
                val initial = Recording(id, Date(), "New Recording", filename)
                _recording.postValue(initial)
            } else {
                val initial = recordingRepository.getRecording(id)!!
                _recording.postValue(initial)
                recordingPresent.postValue(true)
            }
        }
    }

    fun setName(name: String){
        _recording.postValue(_recording.value?.copy(name = name))
    }

    fun setRecordingDirty(){
        recordingDirty = true
        recordingPresent.postValue(true)
    }

    fun save() = viewModelScope.launch {
        if(recordingDirty) {
            updateRecording()
        }
        recordingRepository.saveRecording(_recording.value!!)
    }

    private fun updateRecording(){
        try {
            val fromFile = File(tempRecordingPath)
            val persistentFile = File(_recording.value!!.filename)
            fromFile.renameTo(persistentFile)
        } catch(e: IOException){}
    }
}
