package com.benjaminnwarner.musicianstoolbelt.viewmodels

import android.app.Application
import android.content.ContentValues
import android.provider.MediaStore
import androidx.lifecycle.*
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.util.RecordingConstants
import com.benjaminnwarner.musicianstoolbelt.util.TimeHelpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.*


class RecordingViewModel(
    private val recordingRepository: RecordingRepository,
    private val id: Long,
    application: Application
) : AndroidViewModel(application) {

    val recording: LiveData<Recording>
    private val mutableRecording = MutableLiveData<Recording>()
    private var recordingDirty = false

    private val _unsavedChanges = MediatorLiveData<Boolean>()
    val unsavedChanges: LiveData<Boolean> = _unsavedChanges

    private val filesDir = "${application.filesDir.absolutePath}/"
    private val tempRecordingPath = filesDir + RecordingConstants.DEFAULT_NEW_RECORDING_FILE

    init {
        recording = liveData {
            if (id == 0L) {
                val filename =
                    filesDir + TimeHelpers.unixToFileTimestamp(System.currentTimeMillis()) +
                            RecordingConstants.DEFAULT_RECORDING_EXTENSION
                val newRecording = Recording(id, Date(), "New Recording", filename)
                mutableRecording.value = newRecording
                emit(newRecording)
            } else {
                val existingRecording = recordingRepository.getRecording(id)
                mutableRecording.value = existingRecording
                emit(existingRecording)
            }
        }

        _unsavedChanges.addSource(mutableRecording){
            _unsavedChanges.value =
                if(id == 0L) recordingDirty
                else recordingDirty ||  it != recording.value
        }
    }

    fun setName(name: String){
        mutableRecording.postValue(mutableRecording.value?.copy(name = name))
    }

    fun setRecordingDirty(){
        recordingDirty = true
    }

    fun save() = viewModelScope.launch {
        if(recordingDirty) {
            updateRecording()
        }
        recordingRepository.saveRecording(mutableRecording.value!!)
    }

    fun backup() {
        val resolver = (getApplication() as Application).applicationContext.contentResolver

        val downloadsUri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val details = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "${mutableRecording.value!!.name}.m4a")
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4")
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        val backupUri = resolver?.insert(downloadsUri, details)

        viewModelScope.launch {
            val fIn = File(mutableRecording.value!!.filename).inputStream()
            val fOut = resolver?.openOutputStream(backupUri!!, "w")
            try {
                fIn.copyTo(fOut!!)
            } finally {
                withContext(Dispatchers.IO) {
                    fIn.close()
                    fOut?.close()
                }
            }
        }

        details.clear()
        details.put(MediaStore.Audio.Media.IS_PENDING, 0)
        resolver?.update(backupUri!!, details, null, null)
    }

    private fun updateRecording(){
        try {
            val fromFile = File(tempRecordingPath)
            val persistentFile = File(mutableRecording.value!!.filename)
            fromFile.renameTo(persistentFile)
        } catch(e: IOException){}
    }
}
