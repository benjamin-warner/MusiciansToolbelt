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

    init {
        viewModelScope.launch {
            if (id == 0L) {
                val filename =
                    "${application.filesDir.absolutePath}/${RecordingConstants.DEFAULT_NEW_RECORDING_FILE}"
                _recording.postValue(Recording(id, Date(), "New Recording", filename))
            } else {
                _recording.postValue(recordingRepository.getRecording(id))
            }
        }
    }

    fun setName(name: String){
        _recording.postValue(_recording.value?.copy(name = name))
    }

    fun save() = when(id) {
        0L -> create()
        else -> update()
    }

    private fun update(){
        viewModelScope.launch {
            recordingRepository.saveRecording(recording.value!!)
        }
    }

    private fun create() {
        viewModelScope.launch {
            try {
                val fromFile = File(_recording.value!!.filename)
                val persistentFileName =
                    "${getApplication<Application>().filesDir.absolutePath}/" +
                    TimeHelpers.unixToFileTimestamp(fromFile.lastModified())
                val toFile = File(persistentFileName)
                fromFile.renameTo(toFile)
                recordingRepository.saveRecording(_recording.value!!.copy(filename = persistentFileName))
            } catch (e: IOException){

            }
        }
    }
}
