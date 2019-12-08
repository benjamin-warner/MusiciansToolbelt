package com.benjaminnwarner.musicianstoolbelt.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaPlayerWrapper
import com.benjaminnwarner.musicianstoolbelt.wrappers.MediaRecorderWrapper
import kotlinx.android.synthetic.main.widget_recorder.view.*
import java.lang.Exception


const val TEMP_RECORDING_FILENAME = "recording_temp.m4a"
const val MAX_RECORDING_DURATION = 10000

class Recorder(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    private val recorder = MediaRecorderWrapper()
    private val player = MediaPlayerWrapper()
    private lateinit var animator: ObjectAnimator

    private var filename: String? = null
    private var duration: Long = 0
    private val filePath: String get() ="${context.filesDir.absolutePath}/${filename ?: TEMP_RECORDING_FILENAME}"

    private var reRecordCallback: (() -> Unit)? = null

    init {
        inflate(context, R.layout.widget_recorder,this)

        recorder.setOnInterruptedListener(this::onRecordingInterrupted)
        player.setOnCompletionListener(this::onPlaybackCompleted)

        if(filename == null){
            setRecordingMode()
        } else {
            setPlaybackMode()
        }

        widget_recorder_playback_toggle.setOnCheckedChangeListener{ _, s -> onPlaybackToggle(s) }
        widget_recorder_record_toggle.setOnCheckedChangeListener { _, s -> onRecordToggle(s) }
        widget_recorder_re_record.setOnClickListener {
            if(reRecordCallback == null){
                setRecordingMode()
            } else {
                this.reRecordCallback?.invoke()
            }
        }
    }

    fun setSourceFile(filename: String){
        this.filename = filename
        val mmr = MediaMetadataRetriever().apply{ setDataSource(filePath) }
        duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        setPlaybackMode()
    }

    fun setReRecordListener(callback: (() -> Unit)){
        this.reRecordCallback = callback
    }

    private fun initProgressbar(max: Int, color: Int){
        setProgressbarColor(color)

        widget_recorder_progress.max = max
        widget_recorder_progress.progress = max
        animator = ObjectAnimator.ofInt(widget_recorder_progress,"progress", max, 0).apply {
            duration = max.toLong()
            interpolator = LinearInterpolator()
        }
    }

    fun setRecordingMode(){
        initProgressbar(MAX_RECORDING_DURATION, ContextCompat.getColor(context, R.color.colorRecorderRed))
        widget_recorder_record_toggle.isChecked = false
        widget_recorder_record_toggle.visibility = View.VISIBLE
        widget_recorder_playback_toggle.visibility = View.GONE
        widget_recorder_re_record.visibility = View.GONE
    }

    private fun setPlaybackMode(){
        initProgressbar(duration.toInt(), ContextCompat.getColor(context, R.color.colorPlayerBlue))
        widget_recorder_record_toggle.visibility = View.GONE
        widget_recorder_playback_toggle.visibility = View.VISIBLE
        widget_recorder_re_record.visibility = View.VISIBLE
    }

    private fun onRecordToggle(active: Boolean){
        if(active){
            recorder.recordTo(filePath)
            animator.start()
            duration = System.currentTimeMillis()
        } else {
            animator.cancel()
            recorder.stop()
            duration = System.currentTimeMillis() - duration
            setSourceFile(TEMP_RECORDING_FILENAME)
            setPlaybackMode()
        }
    }

    private fun onPlaybackToggle(active: Boolean){
        if(active){
            player.startPlayingFrom(filePath)
            animator.start()
        } else {
            player.stop()
            animator.cancel()
            widget_recorder_progress.progress = duration.toInt()
        }
    }

    @Suppress("DEPRECATION")
    private fun setProgressbarColor(color: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            widget_recorder_progress.progressDrawable.setTint(color)
        } else {
            widget_recorder_progress.progressDrawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun onRecordingInterrupted(){
        animator.cancel()
        setPlaybackMode()
    }

    private fun onPlaybackCompleted(){
        widget_recorder_playback_toggle.isChecked = false
        widget_recorder_progress.progress = duration.toInt()
    }
}
