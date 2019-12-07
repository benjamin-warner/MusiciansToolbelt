package com.benjaminnwarner.musicianstoolbelt.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.benjaminnwarner.musicianstoolbelt.R
import kotlinx.android.synthetic.main.widget_recorder.view.*


class Recorder constructor(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    init {
        inflate(context, R.layout.widget_recorder,this)
        widget_recorder_progress.progress = 50
    }
}
