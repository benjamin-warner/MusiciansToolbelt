package com.benjaminnwarner.musicianstoolbelt.wrappers

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class AudioBackupService: Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                save(msg.data.getString("filename")!!)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Saving to downloads...", Toast.LENGTH_SHORT).show()
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            println(intent.getStringExtra("filename"))
            msg.data?.putString("filename", intent.getStringExtra("filename"))
            serviceHandler?.sendMessage(msg)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Toast.makeText(applicationContext, "Backup complete!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }


    private fun save(filename: String) {
        val resolver = applicationContext.contentResolver
        val from = File(filename)

        val downloadsUri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val details = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, from.name)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4")
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        val backupUri = resolver.insert(downloadsUri, details)!!

        val fIn = from.inputStream()
        val fOut= resolver.openOutputStream(backupUri, "w")
        write(fIn, fOut)

        details.clear()
        details.put(MediaStore.Audio.Media.IS_PENDING, 0)
        resolver.update(backupUri, details, null, null)
    }

    private fun write(fIn: InputStream?, fOut: OutputStream?) {
        try {
            fIn!!.copyTo(fOut!!)
        } catch(e: IOException) {
            throw e
        } finally {
            fIn?.close()
            fOut?.close()
        }
    }
}
