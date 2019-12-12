package com.benjaminnwarner.musicianstoolbelt.injectors

import android.app.Application
import android.content.Context
import com.benjaminnwarner.musicianstoolbelt.database.AppDatabase
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingListViewModelFactory
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingViewModelFactory

object RecordingRepositoryInjector {

    private fun getRecordingRepository(context: Context): RecordingRepository {
        return RecordingRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).recordingDao())
    }

    fun provideRecordingViewModelFactory(
        context: Context,
        id: Long,
        application: Application
    ): RecordingViewModelFactory {
        return RecordingViewModelFactory(getRecordingRepository(context), id, application)
    }

    fun provideRecordingListViewModelFactory(
        context: Context
    ): RecordingListViewModelFactory {
        return RecordingListViewModelFactory(getRecordingRepository(context))
    }
}
