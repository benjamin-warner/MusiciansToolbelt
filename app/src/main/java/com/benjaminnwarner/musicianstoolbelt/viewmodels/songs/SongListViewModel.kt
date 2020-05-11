package com.benjaminnwarner.musicianstoolbelt.viewmodels.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.fx.IO
import arrow.fx.extensions.fx
import com.benjaminnwarner.musicianstoolbelt.database.song.Song
import com.benjaminnwarner.musicianstoolbelt.database.song.SongRepository

class SongListViewModel(songRepository: SongRepository) : ViewModel() {

    private val getSongsTask: IO<List<Song>> = IO.fx {
        !effect { songRepository.getSongs() }
    }

    private val _songs: MutableLiveData<List<Song>> = MutableLiveData()
    val songs: LiveData<List<Song>> = _songs

    init {
        getSongsTask.unsafeRunAsync { result ->
            result.fold({ throw RuntimeException("Room DB Error") }, { _songs.postValue(it) })
        }
    }
}
