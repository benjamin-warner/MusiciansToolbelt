package com.benjaminnwarner.musicianstoolbelt.views.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.benjaminnwarner.musicianstoolbelt.R

class SongsIndexFragment : Fragment(){

    private val songListViewModel: SongListViewModel by viewModels {
        RecordingRepositoryInjector.provideRecordingListViewModelFactory(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_songs_index, container, false)
    }
}
