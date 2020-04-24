package com.benjaminnwarner.musicianstoolbelt.views.recording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.injectors.RecordingRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.util.RecordingConstants
import com.benjaminnwarner.musicianstoolbelt.viewmodels.recording.RecordingViewModel
import com.benjaminnwarner.musicianstoolbelt.views.permissions.PermissionFragment
import kotlinx.android.synthetic.main.fragment_recording.*
import kotlinx.android.synthetic.main.fragment_recording.view.*

class RecordingFragment: PermissionFragment(RecordingPermission) {

    private val args: RecordingFragmentArgs by navArgs()
    private val recordingViewModel: RecordingViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingViewModelFactory(
            requireActivity(),
            args.id,
            requireActivity().application
        )
    }

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = dispatcher.addCallback(this) {
            callback.isEnabled = false
            dispatcher.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = layoutInflater.inflate(R.layout.fragment_recording, container, false)

        root.fragment_recording_playback_recorder.apply {
            recordingMaxDuration = RecordingConstants.DEFAULT_RECORDING_DURATION_LIMIT
            recordingPreRollDuration = RecordingConstants.DEFAULT_PRE_ROLL_DURATION
            setRecordingWrittenCallback(this@RecordingFragment::onRecordingWritten)
        }

        recordingViewModel.recording.observe(viewLifecycleOwner) { recording ->
            root.fragment_recording_name_input.setText(recording.name)
            root.fragment_recording_created_at.text = recording.createdAt.toString()
            root.fragment_recording_updated_at.text = recording.updatedAt.toString()

            if (recording.id == 0L) {
                root.fragment_recording_playback_recorder.audioSource =
                    "${context?.filesDir?.absolutePath}/${RecordingConstants.DEFAULT_NEW_RECORDING_FILE}"
                root.fragment_recording_playback_recorder.setRecord()
            } else {
                root.fragment_recording_playback_recorder.audioSource = recording.filename
                root.fragment_recording_re_record.isEnabled = true
                root.fragment_recording_playback_recorder.setPlay()
            }
        }

        recordingViewModel.unsavedChanges.observe(this) { unsavedChanges ->
            root.fragment_recording_save.isEnabled = unsavedChanges
        }

        root.fragment_recording_name_input.doAfterTextChanged {
            recordingViewModel.setName(it.toString())
        }

        root.fragment_recording_re_record.setOnClickListener { onReRecord() }
        root.fragment_recording_save.setOnClickListener { recordingViewModel.save() }
        root.fragment_recording_backup.setOnClickListener { recordingViewModel.backup() }

        return root
    }

    private fun onRecordingWritten() {
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
