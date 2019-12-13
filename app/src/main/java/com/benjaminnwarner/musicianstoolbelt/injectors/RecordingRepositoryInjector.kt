package com.benjaminnwarner.musicianstoolbelt.injectors

import android.content.Context
import com.benjaminnwarner.musicianstoolbelt.database.AppDatabase
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingListViewModelFactory
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingViewModelFactory
import com.benjaminnwarner.musicianstoolbelt.wrappers.FileIOWrapper

object RecordingRepositoryInjector {

    private fun getRecordingRepository(context: Context): RecordingRepository {
        return RecordingRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).recordingDao())
    }

    fun provideRecordingViewModelFactory(
        context: Context,
        id: Long
    ): RecordingViewModelFactory {
        return RecordingViewModelFactory(getRecordingRepository(context), id, FileIOWrapper(context))
    }

    fun provideRecordingListViewModelFactory(
        context: Context
    ): RecordingListViewModelFactory {
        return RecordingListViewModelFactory(getRecordingRepository(context))
    }
}
