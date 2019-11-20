package com.benjaminnwarner.musicianstoolbelt.ui.recorder

import android.Manifest
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.ui.permissions.PermissionCheckingFragment
import kotlinx.android.synthetic.main.fragment_recorder.view.*
import java.io.File
import java.io.IOException


class RecorderFragment : PermissionCheckingFragment() {

    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var filePath: String
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recorderViewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recorder, container, false)


        filePath = "${context?.filesDir}/audiorecordtest.m4a"

        recorderViewModel.recording.observe(this, Observer {
            root.start_stop_record_button.text =
                if (it) getString(R.string.stop) else getString(R.string.start)
        })

        root.start_stop_record_button.setOnClickListener { onStartStopRecordingButtonPress() }
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
        checkPermissions(Manifest.permission.RECORD_AUDIO)
//        if(recorderViewModel.recording.value == true){
//            stopRecording()
//            displayRecordingInfo()
//        } else {
//            startRecording()
//        }
    }

    private fun displayRecordingInfo() {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(filePath)
        val file = File(filePath)
        val bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
        val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        Log.d("bitrate", bitrate)
        Log.d("duration", duration)
        Log.d("mime", mime)
        Log.d("length", "${ file.length()/1000 }kb")
    }


    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFile(filePath)

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

    override fun handlePermissionDenial() {
        view?.findNavController()?.popBackStack()
    }
}
