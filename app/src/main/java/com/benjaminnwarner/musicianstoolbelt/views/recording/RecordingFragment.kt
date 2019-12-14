package com.benjaminnwarner.musicianstoolbelt.views.recording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.injectors.RecordingRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.util.RecordingConstants
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingViewModel
import com.benjaminnwarner.musicianstoolbelt.views.permissions.PermissionFragment
import kotlinx.android.synthetic.main.fragment_recording.*
import kotlinx.android.synthetic.main.fragment_recording.view.*

class RecordingFragment: PermissionFragment(RecordingPermission){

    private val args: RecordingFragmentArgs by navArgs()
    private val recordingViewModel: RecordingViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingViewModelFactory(requireActivity(), args.id)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = layoutInflater.inflate(R.layout.fragment_recording, container, false)

        root.fragment_recording_playback_recorder.apply {
            recordingMaxDuration = RecordingConstants.DEFAULT_RECORDING_DURATION_LIMIT
            //recordingPreRollDuration = RecordingConstants.DEFAULT_PRE_ROLL_DURATION
            setRecordingWrittenCallback(::recordingWritten)
        }

        recordingViewModel.recording.observe(this, Observer { recording ->
            root.fragment_recording_name_input.setText(recording.name)
            if(recording.id == 0L){
                initForNew()
            } else {
                initForExisting(recording)
            }
        })

        root.fragment_recording_re_record.setOnClickListener { onReRecord() }
        root.fragment_recording_save.setOnClickListener { save() }

        return root
    }

    private fun save() {
        recordingViewModel.setName(fragment_recording_name_input.text.toString())
        recordingViewModel.save()
    }

    private fun initForNew() {
        fragment_recording_playback_recorder.audioSource =
            "${context?.filesDir?.absolutePath}/${RecordingConstants.DEFAULT_NEW_RECORDING_FILE}"
        fragment_recording_playback_recorder.setRecord()
    }

    private fun initForExisting(recording: Recording){
        fragment_recording_re_record.isEnabled = true
        fragment_recording_playback_recorder.audioSource = recording.filename
        fragment_recording_playback_recorder.setPlay()
    }

    private fun recordingWritten(){
        fragment_recording_re_record.isEnabled = true
        fragment_recording_save.isEnabled = true
    }

    private fun onReRecord(){
        fragment_recording_playback_recorder.setRecord()
    }
}
