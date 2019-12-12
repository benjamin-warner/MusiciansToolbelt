package com.benjaminnwarner.musicianstoolbelt.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.benjaminnwarner.musicianstoolbelt.R
import kotlinx.android.synthetic.main.widget_playback_recorder.view.*

class PlaybackRecorder: FrameLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attributes: AttributeSet): super(context, attributes)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var recorder: Recorder? = null
    private var player: Player? = null

    init {
        inflate(context, R.layout.widget_playback_recorder,this)

        recorder = Recorder(context).apply {
            onRecordingWritten(this@PlaybackRecorder::recordingWritten)
        }
        widget_playback_recorder_container.addView(recorder)
    }

    fun setAudioSource(file: String){
        recorder?.filePath = file
    }

    fun setRecordingDurationLimit(duration: Int) {
        recorder?.setRecordingDurationLimit(duration)
    }

    fun setRecordingPreRollDuration(duration: Long) {
        recorder?.setPreRollDuration(duration)
    }

    private fun recordingWritten(file: String){
        widget_playback_recorder_container.removeAllViews()
        player = Player(context!!).apply {
            setupWithAudioSource(file)
        }
        widget_playback_recorder_container.addView(player)
    }
}
