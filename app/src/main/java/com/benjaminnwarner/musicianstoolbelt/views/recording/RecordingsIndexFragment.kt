package com.benjaminnwarner.musicianstoolbelt.views.recording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording
import com.benjaminnwarner.musicianstoolbelt.injectors.RecordingRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.viewmodels.recording.RecordingListViewModel
import kotlinx.android.synthetic.main.fragment_recordings_index.view.*

class RecordingsIndexFragment : Fragment() {

    private val recordingListViewModel: RecordingListViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingListViewModelFactory(requireActivity())
    }

    private var recordingAdapter =
        RecordingAdapter(
            listOf(),
            this::onItemClicked
        )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_recordings_index, container, false)

        root.fab_new_recording.setOnClickListener {
            val action =
                RecordingsIndexFragmentDirections.navActionGalleryToRecording(
                    id = 0
                )
            findNavController().navigate(action)
        }

        root.recorder_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recordingAdapter
        }

        recordingListViewModel.recordings.observe(viewLifecycleOwner, Observer {
            recordingAdapter =
                RecordingAdapter(
                    it,
                    this::onItemClicked
                )
            root.recorder_recycler_view.swapAdapter(recordingAdapter, false)
        })

        return root
    }

    private fun onItemClicked(recording: Recording){
        val action =
            RecordingsIndexFragmentDirections.navActionGalleryToRecording(
                id = recording.id
            )
        findNavController().navigate(action)
    }
}
