package com.benjaminnwarner.musicianstoolbelt.views.recording

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.injectors.RecordingRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.util.RecordingConstants
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
            recordingMaxDuration = RecordingConstants.DEFAULT_RECORDING_DURATION_LIMIT
            //recordingPreRollDuration = RecordingConstants.DEFAULT_PRE_ROLL_DURATION
            setRecordingWrittenCallback(this@RecordingFragment::onRecordingWritten)
        }

        recordingViewModel.recording.observe(this) { recording ->
            root.fragment_recording_name_input.setText(recording.name)
            root.fragment_recording_playback_recorder.audioSource = recording.filename

            if(recording.id == 0L){
                fragment_recording_playback_recorder.setRecord()
            } else {
                fragment_recording_re_record.isEnabled = true
                fragment_recording_playback_recorder.setPlay()
            }
        }

        recordingViewModel.recordingPresent.observe(this) { unsavedChanges ->
            root.fragment_recording_save.isEnabled = unsavedChanges
        }

        root.fragment_recording_re_record.setOnClickListener { onReRecord() }
        root.fragment_recording_save.setOnClickListener { save() }

        return root
    }

    private fun save() {
        view?.clearFocus()
        hideKeyboard()
        recordingViewModel.setName(fragment_recording_name_input.text.toString())
        recordingViewModel.save()
    }

    private fun hideKeyboard(){
        view?.let {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun onRecordingWritten(){
        fragment_recording_re_record.isEnabled = true
        fragment_recording_save.isEnabled = true
        recordingViewModel.setRecordingDirty()
    }

    private fun onReRecord(){
        fragment_recording_playback_recorder.audioSource =
            "${context?.filesDir?.absolutePath}/${RecordingConstants.DEFAULT_NEW_RECORDING_FILE}"
        fragment_recording_playback_recorder.setRecord()
    }
}
