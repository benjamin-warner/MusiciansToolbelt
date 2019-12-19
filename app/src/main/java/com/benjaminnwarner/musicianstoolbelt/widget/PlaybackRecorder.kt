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
    private var countdown: Countdown? = null
    private var player: Player? = null

    private var recordingWrittenCallback: (() -> Unit)? = null
    var audioSource: String = ""
    var recordingMaxDuration = -1
    var recordingPreRollDuration = -1L

    init {
        inflate(context, R.layout.widget_playback_recorder,this)
    }

    fun setRecordingWrittenCallback(callback: (() -> Unit)) {
        recordingWrittenCallback = callback
    }

    fun setRecord(){
        if(widget_playback_recorder_container.childCount != 0) {
            widget_playback_recorder_container.removeAllViews()
        }
        if(recorder == null) {
            recorder = Recorder(context).apply {
                setRecordingDurationLimit(recordingMaxDuration)
                audioSource = this@PlaybackRecorder.audioSource
                onRecordingWritten(this@PlaybackRecorder::onRecordingWritten)
            }
        } else {
            recorder?.reset()
        }
        widget_playback_recorder_container.addView(recorder)

        if(recordingPreRollDuration != -1L){
            initPreRoll()
        }
    }

    private fun onRecordingWritten(){
        recordingWrittenCallback?.invoke()
        setPlay()
    }

    private fun initPreRoll() {
        if(countdown == null) {
            countdown = Countdown(context).apply{
                setCompletedListener(this@PlaybackRecorder::removePreRoll)
                duration = recordingPreRollDuration
            }
        }
        widget_playback_recorder_container.addView(countdown)
    }

    private fun removePreRoll(){
        if(countdown != null) {
            widget_playback_recorder_container.removeViewAt(1)
            recorder?.record()
        }
    }

    fun setPlay() {
        if(widget_playback_recorder_container.childCount != 0) {
            widget_playback_recorder_container.removeAllViews()
        }
        if(player == null) {
            player = Player(context!!).apply {
                setupWithAudioSource(this@PlaybackRecorder.audioSource)
            }
        } else {
            player?.setupWithAudioSource(audioSource)
        }
        widget_playback_recorder_container.addView(player)
    }
}
