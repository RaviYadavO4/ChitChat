package com.portfolio.chitchat.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File

class AudioRecorder(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var currentOutputFile: File? = null

    fun startRecording(): String? {
        return try {
            currentOutputFile = File(context.cacheDir, "ChitChat_Audio_${System.currentTimeMillis()}.mp3")

            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                // Aggressive Audio compression constraints to strictly avoid Firestore size blocks
                setAudioSamplingRate(16000) // Lower sampling frequency band
                setAudioEncodingBitRate(32000) // 32 kbps encoding bit depth mapping

                setOutputFile(currentOutputFile?.absolutePath)
                prepare()
                start()
            }
            Log.d("ChitChat_Audio", "MediaRecorder Compression engine live at: ${currentOutputFile?.absolutePath}")
            currentOutputFile?.absolutePath
        } catch (e: Exception) {
            Log.e("ChitChat_Audio", "MediaRecorder instantiation fail parameters error: ${e.message}")
            null
        }
    }

    fun stopRecording(): File? {
        return try {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
            mediaRecorder = null
            currentOutputFile
        } catch (e: Exception) {
            mediaRecorder = null
            null
        }
    }
}
