package com.benjaminnwarner.musicianstoolbelt.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.wrappers.CountDownWrapper
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaRecorderWrapper
import kotlinx.android.synthetic.main.widget_recorder.view.*
import kotlin.math.ceil


class Recorder: FrameLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attributes: AttributeSet): super(context, attributes)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var filePath: String = ""
    private var preRollDuration: Long = -1

    private val recorder = MediaRecorderWrapper()
    private var animator: ObjectAnimator? = null
    private var preRoll: CountDownWrapper? = null

    private var recordingWrittenCallback: ((String) -> Unit)? = null

    init {
        inflate(context, R.layout.widget_recorder,this)

        recorder.setDurationLimitListener(this::onDurationLimitHit)

        widget_recorder_pre_roll.setOnClickListener { onStart() }
        widget_recorder_toggle.setOnCheckedChangeListener { _, active -> onRecordToggle(active) }
    }

    fun onRecordingWritten(callback: ((String) -> Unit)){
        this.recordingWrittenCallback = callback
    }

    fun setPreRollDuration(duration: Long){
        preRollDuration = duration
        widget_recorder_pre_roll.visibility = View.VISIBLE
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

    private fun onStart(){
        if(preRollDuration == -1L){
            widget_recorder_toggle.isChecked = true
        } else {
            startPreRoll()
        }
    }

    private fun startPreRoll(){
        if(preRoll == null) {
            widget_recorder_pre_roll.text = (preRollDuration / 1000).toString()

            preRoll = CountDownWrapper(preRollDuration, 1000, {
                widget_recorder_pre_roll.visibility = View.INVISIBLE
                widget_recorder_toggle.isChecked = true
                preRoll = null
            }, {
                widget_recorder_pre_roll.text = ceil(it / 1000F).toInt().toString()
            })
            preRoll?.start()
        } else {
            widget_recorder_pre_roll.text = null
            preRoll?.cancel()
            preRoll = null
        }
    }

    private fun onRecordToggle(active: Boolean){
        if(active){
            if(filePath == "") throw IllegalStateException("No audio source set; set via prop filePath.")
            recorder.recordTo(filePath)
            animator?.start()
        } else {
            animator?.cancel()
            recorder.stop()
            recordingWrittenCallback?.invoke(filePath)
        }
    }

    private fun onDurationLimitHit(){
        animator?.cancel()
        recordingWrittenCallback?.invoke(filePath)
    }
}
