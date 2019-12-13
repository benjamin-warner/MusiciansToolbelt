package com.benjaminnwarner.musicianstoolbelt.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.wrappers.CountDownWrapper
import kotlinx.android.synthetic.main.widget_countdown.view.*
import kotlin.math.ceil

class Countdown: FrameLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attributes: AttributeSet): super(context, attributes)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var preRoll: CountDownWrapper? = null
    private var completionListener: (() -> Unit)? = null
    var duration = -1L

    init {
        View.inflate(context, R.layout.widget_countdown, this)

        widget_countdown_display.setOnClickListener { start() }
    }

    fun setCompletedListener(listener: (() -> Unit)){
        completionListener = listener
    }

    private fun start(){
        if(preRoll == null) {
            widget_countdown_display.text = (duration / 1000).toString()

            preRoll = CountDownWrapper(duration, 1000, this::completed) {
                widget_countdown_display.text = ceil(it / 1000F).toInt().toString()
            }
            preRoll?.start()
        } else {
            widget_countdown_display.text = null
            preRoll?.cancel()
            preRoll = null
        }
    }

    private fun completed(){
        widget_countdown_display.text = null
        preRoll = null
        completionListener?.invoke()
    }
}
