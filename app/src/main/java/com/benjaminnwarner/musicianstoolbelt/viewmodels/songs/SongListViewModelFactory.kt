package com.benjaminnwarner.musicianstoolbelt.viewmodels.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benjaminnwarner.musicianstoolbelt.database.song.SongRepository


class SongListViewModelFactory(
    private val songRepository: SongRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongListViewModel(
            songRepository
        ) as T
    }
}
