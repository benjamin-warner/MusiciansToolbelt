package com.benjaminnwarner.musicianstoolbelt.database.recording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.benjaminnwarner.musicianstoolbelt.util.TimeHelpers
import java.util.*

@Entity(tableName = "recordings")
data class Recording(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = " local_filename")
    val filename: String
)
