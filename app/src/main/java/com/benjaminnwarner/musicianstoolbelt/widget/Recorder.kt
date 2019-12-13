package com.benjaminnwarner.musicianstoolbelt.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaRecorderWrapper
import kotlinx.android.synthetic.main.widget_recorder.view.*


class Recorder: FrameLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attributes: AttributeSet): super(context, attributes)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val recorder = MediaRecorderWrapper()
    private var animator: ObjectAnimator? = null

    var audioSource: String = ""
    private var recordingWrittenCallback: (() -> Unit)? = null

    init {
        inflate(context, R.layout.widget_recorder,this)

        recorder.setDurationLimitListener(this::onDurationLimitHit)

        widget_recorder_toggle.setOnCheckedChangeListener { _, active -> onRecordToggle(active) }
    }

    fun record(){
        widget_recorder_toggle.isChecked = true
    }

    fun onRecordingWritten(callback: (() -> Unit)){
        this.recordingWrittenCallback = callback
    }

    fun setRecordingDurationLimit(duration: Int){
        recorder.maxDuration = duration
        initProgressbar()
    }

    private fun initProgressbar(){
        widget_recorder_progress.max = recorder.maxDuration
        widget_recorder_progress.progress = recorder.maxDuration
        animator = ObjectAnimator.ofInt(widget_recorder_progress, "progress", recorder.maxDuration, 0).apply {
            this.duration = recorder.maxDuration.toLong()
            interpolator = LinearInterpolator()
        }
    }

    private fun onRecordToggle(active: Boolean){
        if(active){
            if(audioSource == "") throw IllegalStateException("No audio source set; set via prop filePath.")
            recorder.recordTo(audioSource)
            animator?.start()
        } else {
            animator?.cancel()
            recorder.stop()
            recordingWrittenCallback?.invoke()
        }
    }

    private fun onDurationLimitHit(){
        animator?.cancel()
        recordingWrittenCallback?.invoke()
    }
}
