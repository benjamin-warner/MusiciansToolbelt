package com.benjaminnwarner.musicianstoolbelt.ui.recorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.ui.permissions.PermissionFragment
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaPlayerWrapper
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaRecorderWrapper
import kotlinx.android.synthetic.main.fragment_recorder.view.*

class RecorderFragment : PermissionFragment(RecorderPermission) {

    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var filePath: String
    private var recorder = MediaRecorderWrapper()
    private var player = MediaPlayerWrapper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recorderViewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recorder, container, false)

        filePath = "${context?.filesDir.toString()}/recording.m4a"

        setUpListeners(root)
        setUpObservers(root)

        return root
    }

    private fun setUpListeners(root: View) {
        recorder.setOnInterruptedListener { recorderViewModel.recording.value = false }
        player.setOnCompletionListener { recorderViewModel.playing.value = false }

        root.start_stop_record_button.setOnClickListener { onStartStopRecordingButtonPress() }

        root.start_stop_playback_button.setOnClickListener { onStartStopPlaybackButtonPress() }
    }

    private fun setUpObservers(root: View) {
        recorderViewModel.recording.observe(this, Observer {
            root.start_stop_record_button.text =
                if (it) getString(R.string.stop) else getString(R.string.start)
        })

        recorderViewModel.playing.observe(this, Observer {
            root.start_stop_playback_button.text =
                if (it) getString(R.string.stop) else getString(R.string.play)
        })
    }


    private fun onStartStopPlaybackButtonPress() {
        if(recorderViewModel.playing.value == true) {
            player.stop()
            recorderViewModel.playing.value = false
        }
        else {
            player.startPlayingFrom(filePath)
            recorderViewModel.playing.value = true
        }
    }

    private fun onStartStopRecordingButtonPress() {
        when (recorderViewModel.recording.value) {
            true -> stopRecording()
            false -> startRecording()
        }
    }

    private fun startRecording() {
        recorder.recordTo(filePath)
        recorderViewModel.recording.value = true
    }

    private fun stopRecording() {
        recorder.stop()
        recorderViewModel.recording.value = false
    }
}
