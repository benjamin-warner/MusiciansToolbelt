package com.benjaminnwarner.musicianstoolbelt.ui.recorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecorderViewModel : ViewModel() {
    val recording = MutableLiveData<Boolean>().apply {
        value = false
    }

    val playing = MutableLiveData<Boolean>().apply {
        value = false
    }
}
