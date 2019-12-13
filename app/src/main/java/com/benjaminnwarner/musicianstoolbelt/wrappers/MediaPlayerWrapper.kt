package com.benjaminnwarner.musicianstoolbelt.wrappers

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

class MediaPlayerWrapper {
    private var player: MediaPlayer? = null
    private var onCompletionListener: (() -> Unit)? = null

    fun startPlayingFrom(filePath: String) {
        player = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                setOnCompletionListener { onCompletionListener?.invoke() }
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("startPlaying", "prepare() failed")
            }
        }
    }

    fun stop() {
        player?.stop()
        player?.reset()
        player?.release()
        player = null
    }

    fun setOnCompletionListener(callback: (() -> Unit )){
        onCompletionListener = callback
    }
}
