package com.benjaminnwarner.musicianstoolbelt.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaPlayerWrapper
import kotlinx.android.synthetic.main.widget_player.view.*


class Player(context: Context): LinearLayout(context) {

    private val player = MediaPlayerWrapper()
    private lateinit var animator: ObjectAnimator
    private var audioSource: String? = null
    private var audioDuration: Int = 0

    init {
        inflate(context, R.layout.widget_player, this)

        player.setOnCompletionListener(this::onPlaybackCompleted)
        widget_player_toggle.setOnCheckedChangeListener { _, active -> onPlaybackToggle(active) }
    }

    fun setupWithAudioSource(file: String) {
        audioSource = file
        val mmr = MediaMetadataRetriever().apply { setDataSource(audioSource) }
        audioDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
        initProgressbar()
    }

    private fun initProgressbar(){
        widget_player_progress.max = audioDuration
        widget_player_progress.progress = audioDuration

        animator = ObjectAnimator.ofInt(widget_player_progress,"progress", audioDuration, 0).apply {
            this.duration = audioDuration.toLong()
            interpolator = LinearInterpolator()
        }
    }

    private fun onPlaybackToggle(active: Boolean){
        if(active){
            player.startPlayingFrom(audioSource!!)
            animator.start()
        } else {
            player.stop()
            animator.cancel()
            widget_player_progress.progress = audioDuration
        }
    }

    private fun onPlaybackCompleted(){
        widget_player_toggle.isChecked = false
        widget_player_progress.progress = audioDuration
    }
}
