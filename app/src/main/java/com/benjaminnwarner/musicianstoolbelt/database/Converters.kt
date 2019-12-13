package com.benjaminnwarner.musicianstoolbelt.database

import androidx.room.TypeConverter
import com.benjaminnwarner.musicianstoolbelt.util.TimeHelpers
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return TimeHelpers.iso8601ToDate(value!!)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return TimeHelpers.dateToIso8601(date!!)
    }
}
