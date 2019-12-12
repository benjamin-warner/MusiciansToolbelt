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
import kotlinx.android.synthetic.main.fragment_recording.view.*

class RecordingFragment: PermissionFragment(RecordingPermission){

    private val args: RecordingFragmentArgs by navArgs()
    private val recordingViewModel: RecordingViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingViewModelFactory(requireActivity(), args.id, requireActivity().application)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = layoutInflater.inflate(R.layout.fragment_recording, container, false)

        recordingViewModel.recording.observe(this, Observer { recording ->
            root.fragment_recording_filename.text = recording.filename
            root.fragment_recording_playback_recorder.apply {
                setRecordingDurationLimit(RecorderConstants.DEFAULT_RECORDING_DURATION_LIMIT)
                setRecordingPreRollDuration(RecorderConstants.DEFAULT_PRE_ROLL_DURATION)
                setAudioSource(recording.filename)
            }
        })

        return root
    }
}
