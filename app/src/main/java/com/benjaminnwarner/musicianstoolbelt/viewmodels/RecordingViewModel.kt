package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.*
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.util.RecordingConstants
import com.benjaminnwarner.musicianstoolbelt.wrappers.FileIOWrapper
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class RecordingViewModel(
    private val recordingRepository: RecordingRepository,
    private val id: Long,
    private val fileWriter: FileIOWrapper
) : ViewModel() {

    private val _recording = MutableLiveData<Recording>()
    val recording: LiveData<Recording> = _recording

    init {
        viewModelScope.launch {
            val res = recordingRepository.getRecording(id)
            @Suppress("SENSELESS_COMPARISON")
            if(res == null){
                _recording.postValue(Recording(0, Date(), "", ""))
            } else {
                _recording.postValue(res)
            }
        }
    }

    fun setName(name: String){
        _recording.postValue(_recording.value?.copy(name = name))
    }

    fun save(){
        if(id == 0L){
            create()
        } else {
            viewModelScope.launch {
                recordingRepository.saveRecording(recording.value!!)
            }
        }
    }

    private fun create() {
        viewModelScope.launch {
            try {
                val persistentFilename =
                    fileWriter.renameAsTimestamp(RecordingConstants.DEFAULT_NEW_RECORDING_FILE,
                        RecordingConstants.DEFAULT_RECORDING_EXTENSION)
                val toSave = _recording.value!!.copy(filename = persistentFilename)
                recordingRepository.saveRecording(toSave)
            } catch (e: IOException){

            }
        }
    }
}
