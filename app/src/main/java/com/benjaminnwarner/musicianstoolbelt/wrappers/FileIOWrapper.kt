package com.benjaminnwarner.musicianstoolbelt.wrappers

import android.content.Context
import android.os.Build
import com.benjaminnwarner.musicianstoolbelt.util.TimeHelpers
import java.io.File
import java.util.*

class FileIOWrapper(context: Context) {

    private val filesDir = context.filesDir.absolutePath
    private val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0)
    } else {
        context.resources.configuration.locale
    }

    fun renameAsTimestamp(filename: String, extension: String): String {
        val file = File(filesDir, filename)
        val newName = "${TimeHelpers.toTimestampForFile(file.lastModified(), locale)}${extension}"
        val newFile = File(filesDir, newName)
        file.renameTo(newFile)
        return newFile.absolutePath
    }
}
