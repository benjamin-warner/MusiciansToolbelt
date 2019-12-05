package com.benjaminnwarner.musicianstoolbelt.injectors

import android.content.Context
import com.benjaminnwarner.musicianstoolbelt.database.AppDatabase
import com.benjaminnwarner.musicianstoolbelt.database.recording.RecordingRepository
import com.benjaminnwarner.musicianstoolbelt.viewmodels.RecordingViewModelFactory

object RecordingRepositoryInjector {

    private fun getRecordingRepository(context: Context): RecordingRepository {
        return RecordingRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).recordingDao())
    }

    fun provideRecordingViewModelFactory(
        context: Context,
        id: Int
    ): RecordingViewModelFactory {
        return RecordingViewModelFactory(getRecordingRepository(context), id)
    }
}
