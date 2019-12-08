package com.benjaminnwarner.musicianstoolbelt.wrappers

import android.os.CountDownTimer

class CountDownWrapper(
    duration: Long,
    interval: Long,
    private val finishCallback: (() -> Unit),
    private val onTickCallback: ((millisLeft: Long) -> Unit)
    ): CountDownTimer(duration, interval){

    override fun onFinish() { finishCallback.invoke()}
    override fun onTick(millisLeft: Long) { onTickCallback.invoke(millisLeft) }
}
