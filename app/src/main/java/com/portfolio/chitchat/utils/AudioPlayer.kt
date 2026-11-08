package com.portfolio.chitchat.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Base64
import java.io.File
import java.io.FileOutputStream

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playBase64Audio(base64AudioStr: String) {
        try {
            stopPlayback()
            val tempFile = File(context.cacheDir, "temp_play.mp3")
            val audioBytes = Base64.decode(base64AudioStr, Base64.DEFAULT)
            FileOutputStream(tempFile).use { it.write(audioBytes) }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(tempFile.absolutePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopPlayback() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}
