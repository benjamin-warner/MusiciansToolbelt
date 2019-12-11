package com.benjaminnwarner.musicianstoolbelt.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.widget.Player
import com.benjaminnwarner.musicianstoolbelt.widget.Recorder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var recorder: Recorder? = null
    private var player: Player? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        recorder = Recorder(context!!).apply {
            onRecordingWritten(this@HomeFragment::recordingWritten)
        }
        root.home_playback_recorder.addView(recorder)

        return root
    }

    private fun recordingWritten(file: String){
        home_playback_recorder.removeAllViews()
        player = Player(context!!).apply {
            setupWithAudioSource(file)
        }
        home_playback_recorder.addView(player)
    }
}
