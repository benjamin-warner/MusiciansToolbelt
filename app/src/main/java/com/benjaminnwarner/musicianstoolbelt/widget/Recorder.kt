package com.benjaminnwarner.musicianstoolbelt.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.wrappers.CountDownWrapper
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaRecorderWrapper
import kotlinx.android.synthetic.main.widget_recorder.view.*
import kotlin.math.ceil


const val TEMP_RECORDING_FILENAME = "recording_temp.m4a"
const val MAX_RECORDING_DURATION = 10000
const val PRE_ROLL_DURATION = 3000L

class Recorder(context: Context): LinearLayout(context) {

    private val recorder = MediaRecorderWrapper()
    private lateinit var animator: ObjectAnimator

    private val filePath: String = "${context.filesDir.absolutePath}/${TEMP_RECORDING_FILENAME}"

    private var recordingWrittenCallback: ((String) -> Unit)? = null
    private var preRoll: CountDownWrapper? = null

    init {
        inflate(context, R.layout.widget_recorder,this)

        recorder.setDurationLimitListener(this::onDurationLimitHit)

        widget_recorder_pre_roll.setOnClickListener { startPreRoll() }
        widget_recorder_toggle.setOnCheckedChangeListener { _, active -> onRecordToggle(active) }

        initProgressbar()
    }

    fun onRecordingWritten(callback: ((String) -> Unit)){
        this.recordingWrittenCallback = callback
    }

    private fun initProgressbar(){
        widget_recorder_progress.max = MAX_RECORDING_DURATION
        widget_recorder_progress.progress = MAX_RECORDING_DURATION
        animator = ObjectAnimator.ofInt(widget_recorder_progress, "progress", MAX_RECORDING_DURATION, 0).apply {
            this.duration = MAX_RECORDING_DURATION.toLong()
            interpolator = LinearInterpolator()
        }
    }

    private fun startPreRoll(){
        if(preRoll == null) {
            widget_recorder_pre_roll.text = (PRE_ROLL_DURATION / 1000).toString()

            preRoll = CountDownWrapper(PRE_ROLL_DURATION, 1000, {
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
            recorder.recordTo(filePath)
            animator.start()
        } else {
            animator.cancel()
            recorder.stop()
            recordingWrittenCallback?.invoke(filePath)
        }
    }

    private fun onDurationLimitHit(){
        animator.cancel()
        recordingWrittenCallback?.invoke(filePath)
    }
}
