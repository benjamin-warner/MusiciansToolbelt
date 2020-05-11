package com.benjaminnwarner.musicianstoolbelt.views.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.injectors.SongRepositoryInjector
import com.benjaminnwarner.musicianstoolbelt.viewmodels.songs.SongListViewModel

class SongsIndexFragment : Fragment(){

    private val songListViewModel: SongListViewModel by viewModels {
        SongRepositoryInjector.provideSongListViewModelFactory(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = layoutInflater.inflate(R.layout.fragment_songs_index, container, false)

        songListViewModel.songs.observe(viewLifecycleOwner, Observer {

        })

        return root
    }
}
