package com.benjaminnwarner.musicianstoolbelt.database

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    private val iso8601 =  "yyyy-MMM-dd HH:mm:ss'Z'"

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return SimpleDateFormat(iso8601).parse(value!!)
    }

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        val dateFormatter = SimpleDateFormat(iso8601)
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormatter.format(date!!)
    }
}
