package com.benjaminnwarner.musicianstoolbelt.views.recording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.injectors.RecordingRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.util.RecorderConstants
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingViewModel
import com.benjaminnwarner.musicianstoolbelt.views.permissions.PermissionFragment
import kotlinx.android.synthetic.main.fragment_recording.*
import kotlinx.android.synthetic.main.fragment_recording.view.*

class RecordingFragment: PermissionFragment(RecordingPermission){

    private val args: RecordingFragmentArgs by navArgs()
    private val recordingViewModel: RecordingViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingViewModelFactory(requireActivity(), args.id, requireActivity().application)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = layoutInflater.inflate(R.layout.fragment_recording, container, false)

        root.fragment_recording_playback_recorder.apply {
            recordingMaxDuration = RecorderConstants.DEFAULT_RECORDING_DURATION_LIMIT
            recordingPreRollDuration = 1000
        }

        recordingViewModel.recording.observe(this, Observer { recording ->
            root.fragment_recording_filename.text = recording.filename

            root.fragment_recording_playback_recorder.apply {
                audioSource = recording.filename
                setRecordingWrittenCallback(::recordingWritten)
            }

            if(recording.id == -1L) {
                setRecord()
            } else {
                setPlay()
            }
        })

        root.fragment_recording_re_record.setOnClickListener{ setRecord() }
        root.fragment_recording_save.setOnClickListener{ save() }

        return root
    }

    private fun setPlay(){
        fragment_recording_re_record.isEnabled = true
        fragment_recording_save.isEnabled = true
        fragment_recording_playback_recorder.setPlay()
    }

    private fun recordingWritten(){
        fragment_recording_re_record.isEnabled = true
        fragment_recording_save.isEnabled = true
    }

    private fun setRecord(){
        fragment_recording_re_record.isEnabled = false
        fragment_recording_save.isEnabled = false
        fragment_recording_playback_recorder.setRecord()
    }

    private fun save(){

    }
}
