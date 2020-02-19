package com.benjaminnwarner.musicianstoolbelt.wrappers

import android.media.MediaRecorder
import android.util.Log
import java.io.IOException

class MediaRecorderWrapper {
    private var recorder: MediaRecorder? = null
    private var onInterruptedListener: (() -> Unit)? = null

    var maxDuration = -1

    fun recordTo(filePath: String) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(64000)
            setAudioSamplingRate(44100)
            setMaxDuration(maxDuration)
            setOutputFile(filePath)
            setOnInfoListener { _, what, _ -> onInfo(what) }

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("startRecording", "prepare() failed")
            }

            start()
        }
    }

    private fun onInfo(what: Int) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            stop()
            onInterruptedListener?.invoke()
        }
    }

    fun stop(){
        recorder?.apply {
            stop()
            reset()
            release()
        }
        recorder = null
    }

    fun setDurationLimitListener(callback: (() -> Unit)){
        onInterruptedListener = callback
    }
}
