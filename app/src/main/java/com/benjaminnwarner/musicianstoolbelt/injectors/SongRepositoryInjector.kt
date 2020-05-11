package com.benjaminnwarner.musicianstoolbelt.injectors

import android.app.Application
import android.content.Context
import com.benjaminnwarner.musicianstoolbelt.database.AppDatabase
import com.benjaminnwarner.musicianstoolbelt.database.song.SongRepository
import com.benjaminnwarner.musicianstoolbelt.viewmodels.songs.SongListViewModelFactory


object SongRepositoryInjector {

    private fun getSongRepository(context: Context): SongRepository {
        return SongRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).songDao()
        )
    }

    fun provideSongListViewModelFactory(
        context: Context
    ): SongListViewModelFactory {
        return SongListViewModelFactory(
            getSongRepository(context)
        )
    }
}