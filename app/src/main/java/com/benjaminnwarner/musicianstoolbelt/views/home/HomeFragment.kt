package com.benjaminnwarner.musicianstoolbelt.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.benjaminnwarner.musicianstoolbelt.R
import kotlinx.android.synthetic.main.fragment_home.view.*



class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recordingPath: String
    private val recordingDurationLimit = 10000
    private val recordingPreRollDuration = 3000L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        recordingPath = "${context?.filesDir?.absolutePath}/recording_temp.m4a"

        root.home_playback_recorder.apply {
            setRecordingDurationLimit(recordingDurationLimit)
            setRecordingPreRollDuration(recordingPreRollDuration)
            setAudioSource(recordingPath)
        }

        return root
    }
}
