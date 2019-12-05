package com.benjaminnwarner.musicianstoolbelt.database.recording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "recordings")
data class Recording(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "filename")
    val filename: String
)
