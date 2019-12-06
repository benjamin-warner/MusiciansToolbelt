package com.benjaminnwarner.musicianstoolbelt.views.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.injectors.RecordingRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingViewModel
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaPlayerWrapper
import kotlinx.android.synthetic.main.fragment_recording_details.view.*

class RecordingDetailFragment: Fragment(){

    private val args: RecordingDetailFragmentArgs by navArgs()

    private val recordingViewModel: RecordingViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingViewModelFactory(requireActivity(), args.id)
    }

    private val mediaPlayer = MediaPlayerWrapper()

    private var playing = false

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = layoutInflater.inflate(R.layout.fragment_recording_details, container, false)

        recordingViewModel.recording.observe(this, Observer {
            root.fragment_detail_filename.text = it.filename
        })

        root.fragment_detail_start_stop.setOnClickListener { onStartStopPlayback() }

        return root
    }

    private fun onStartStopPlayback() {
        playing = if(playing) {
            mediaPlayer.stop()
            false
        } else {
            mediaPlayer.startPlayingFrom("${context?.filesDir}/${recordingViewModel.recording.value?.filename}")
            true
        }
    }
}
