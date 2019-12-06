package com.benjaminnwarner.musicianstoolbelt.views.recorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.injectors.RecordingRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingViewModel
import com.benjaminnwarner.musicianstoolbelt.views.permissions.PermissionFragment
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaPlayerWrapper
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaRecorderWrapper
import kotlinx.android.synthetic.main.fragment_recorder.view.*
import java.io.File

class RecorderFragment : PermissionFragment(RecorderPermission) {

    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var recordingDirectory: String
    private val recorder = MediaRecorderWrapper()
    private val tempRecordingFile = "recording.m4a"
    private val player = MediaPlayerWrapper()

    private val recordingViewModel: RecordingViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingViewModelFactory(requireActivity(), 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recorderViewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recorder, container, false)

        recordingDirectory = context?.filesDir.toString()

        setUpListeners(root)
        setUpObservers(root)

        return root
    }

    private fun setUpListeners(root: View) {
        recorder.setOnInterruptedListener { recorderViewModel.recording.value = false }
        player.setOnCompletionListener { recorderViewModel.playing.value = false }

        root.start_stop_record_button.setOnClickListener { onStartStopRecordingButtonPress() }
        root.start_stop_playback_button.setOnClickListener { onStartStopPlaybackButtonPress() }

        root.cancel_recording_button.setOnClickListener { onCancel() }
        root.save_recording_button.setOnClickListener { onSave() }
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

        recorderViewModel.unsavedChanges.observe(this, Observer {
            if(it) {
                root.save_recording_button.isEnabled = true
                root.start_stop_playback_button.isEnabled = true
            }
        })
    }

    private fun onStartStopPlaybackButtonPress() {
        if(recorderViewModel.playing.value == true) {
            player.stop()
            recorderViewModel.playing.value = false
        }
        else {
            player.startPlayingFrom("${recordingDirectory}/${tempRecordingFile}")
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
        recorder.recordTo("${recordingDirectory}/${tempRecordingFile}")
        recorderViewModel.recording.value = true
    }

    private fun stopRecording() {
        recorder.stop()
        recorderViewModel.recording.value = false
        recorderViewModel.unsavedChanges.value = true
    }

    private fun onCancel(){
        if(recorderViewModel.unsavedChanges.value == true) {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setMessage(getString(R.string.abandon_unsaved))
                    setPositiveButton(R.string.back) { dialog, _ ->
                        dialog.dismiss()
                    }
                    setNegativeButton(R.string.discard) { dialog, _ ->
                        dialog.dismiss()
                        findNavController().popBackStack()
                    }
                }
                builder.create()
            }
            alertDialog?.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun onSave() {
        val temp = File(recordingDirectory, tempRecordingFile)
        val filename = "${temp.lastModified()}.m4a"
        val persistent = File(recordingDirectory, filename)
        temp.renameTo(persistent)
        recordingViewModel.createRecordingRecord(filename)
    }
}
