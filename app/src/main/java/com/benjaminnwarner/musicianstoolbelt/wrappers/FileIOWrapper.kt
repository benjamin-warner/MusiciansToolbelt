package com.benjaminnwarner.musicianstoolbelt.wrappers

import android.content.Context
import com.benjaminnwarner.musicianstoolbelt.util.TimeHelpers
import java.io.File

class FileIOWrapper(context: Context) {

    private val filesDir = context.filesDir.absolutePath

    fun renameAsTimestamp(filename: String, extension: String): String {
        val file = File(filesDir, filename)
        val newName = "${TimeHelpers.unixToFileTimestamp(file.lastModified())}${extension}"
        val newFile = File(filesDir, newName)
        file.renameTo(newFile)
        return newFile.absolutePath
    }
}
