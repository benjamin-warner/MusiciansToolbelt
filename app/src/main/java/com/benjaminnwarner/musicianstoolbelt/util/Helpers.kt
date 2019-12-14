package com.benjaminnwarner.musicianstoolbelt.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TimeHelpers {
    private const val ISO_8601 = "yyyy-MMM-dd HH:mm:ss'Z'"
    private const val FILE_TIMESTAMP = "yyyy-MM-dd HH-mm-ss"

    @SuppressLint("SimpleDateFormat")
    fun dateToIso8601(date: Date): String {
        val dateFormatter = SimpleDateFormat(ISO_8601)
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormatter.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun iso8601ToDate(iso: String): Date {
        return SimpleDateFormat(ISO_8601).parse(iso)!!
    }

    @SuppressLint("SimpleDateFormat")
    fun unixToFileTimestamp(epoch: Long): String {
        val dateFormatter = SimpleDateFormat(FILE_TIMESTAMP)
        val date = Date(epoch)
        return dateFormatter.format(date)
    }
}
