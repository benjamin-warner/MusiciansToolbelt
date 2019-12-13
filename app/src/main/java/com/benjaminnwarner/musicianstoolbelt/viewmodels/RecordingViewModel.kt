package com.benjaminnwarner.musicianstoolbelt.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val recording: LiveData<Recording> = recordingRepository.getRecording(id)

    fun save(){
        if(id == 0L){
            create()
        }
    }

    private fun create() {
        viewModelScope.launch {
            try {
                val persistentFilename =
                    fileWriter.renameAsTimestamp(RecordingConstants.DEFAULT_NEW_RECORDING_FILE,
                        RecordingConstants.DEFAULT_RECORDING_EXTENSION)
                val recording = Recording(id, Date(), persistentFilename)
                recordingRepository.saveRecording(recording)
            } catch (e: IOException){

            }
        }
    }
}
