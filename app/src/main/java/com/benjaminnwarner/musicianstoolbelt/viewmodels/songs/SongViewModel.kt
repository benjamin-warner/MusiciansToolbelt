package com.benjaminnwarner.musicianstoolbelt.viewmodels.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.benjaminnwarner.musicianstoolbelt.database.song.Song
import com.benjaminnwarner.musicianstoolbelt.database.song.SongRepository

class SongViewModel(songRepository: SongRepository) : ViewModel() {

    val songs: LiveData<List<Song>> = liveData {
        songRepository.getSongs()
    }
}
