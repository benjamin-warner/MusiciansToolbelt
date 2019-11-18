package com.benjaminnwarner.musicianstoolbelt.ui.recorder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.benjaminnwarner.musicianstoolbelt.R
import kotlinx.android.synthetic.main.fragment_recorder.view.*
import java.io.IOException

class RecorderFragment : Fragment() {

    private lateinit var recorderViewModel: RecorderViewModel

    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var filePath: String
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recorderViewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recorder, container, false)


        recorderViewModel.recordingPermissionGranted.value =
            ContextCompat.checkSelfPermission(context!!, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED


        if(recorderViewModel.recordingPermissionGranted.value == true) {
            filePath = "${activity?.externalCacheDir?.absolutePath}/audiorecordtest.3gp"

            recorderViewModel.recording.observe(this, Observer {
                root.start_stop_record_button.text = if(it) getString(R.string.stop) else getString(R.string.start)
            })

            root.start_stop_record_button.setOnClickListener { onStartStopRecordingButtonPress() }
        } else {
            requestPermissions(permissions, 200)
        }

        root.start_stop_playback_button.setOnClickListener { onStartStopPlaybackButtonPress() }

        recorderViewModel.playing.observe(this, Observer {
            root.start_stop_playback_button.text = if(it) getString(R.string.stop) else getString(R.string.play)
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
        if(recorderViewModel.recording.value == true){
            stopRecording()
        } else {
            startRecording()
        }
    }


    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recorderViewModel.recordingPermissionGranted.value = true
        } else {
            findNavController().popBackStack()
        }
    }
}
