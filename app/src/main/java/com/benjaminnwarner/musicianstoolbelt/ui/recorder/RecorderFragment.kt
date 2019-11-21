package com.benjaminnwarner.musicianstoolbelt.ui.recorder

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.ui.permissions.PermissionFragment
import kotlinx.android.synthetic.main.fragment_recorder.view.*
import java.io.IOException

class RecorderFragment : PermissionFragment(RecorderPermission) {

    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var filePath: String
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recorderViewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recorder, container, false)

        filePath = "${context?.filesDir.toString()}/recording.m4a"

        root.start_stop_record_button.setOnClickListener { onStartStopRecordingButtonPress() }

        recorderViewModel.recording.observe(this, Observer {
            root.start_stop_record_button.text =
                if (it) getString(R.string.stop) else getString(R.string.start)
        })

        root.start_stop_playback_button.setOnClickListener { onStartStopPlaybackButtonPress() }

        recorderViewModel.playing.observe(this, Observer {
            root.start_stop_playback_button.text =
                if (it) getString(R.string.stop) else getString(R.string.play)
        })

        return root
    }

    private fun onStartStopPlaybackButtonPress() {
        if(recorderViewModel.playing.value == true){
            stopPlaying()
        } else {
            startPlaying()
        }
    }

    private fun onStartStopRecordingButtonPress() {
        when (recorderViewModel.recording.value) {
            true -> stopRecording()
            false -> startRecording()
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setMaxDuration(10000)
            setOutputFile(filePath)
            setOnInfoListener { _, what, _ ->
                if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                 stopRecording()
                }
            }

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("startRecording", "prepare() failed")
            }

            start()
            recorderViewModel.recording.value = true
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            reset()
            release()
        }
        recorder = null
        recorderViewModel.recording.value = false
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("startPlaying", "prepare() failed")
            }
        }
        recorderViewModel.playing.value = true
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        recorderViewModel.playing.value = false
    }
}
