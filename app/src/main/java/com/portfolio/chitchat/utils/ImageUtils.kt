package com.portfolio.chitchat.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImageUtils {

    // Aggressive downscaling and compression engine to fit Firestore 1MB limits safely
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

            // Decode with bounds only to calculate scale factors
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            // Calculate exact target sampling factor (Max 800px boundary box layout)
            val reqWidth = 800
            val reqHeight = 800
            var inSampleSize = 1
            if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
                val halfHeight = options.outHeight / 2
                val halfWidth = options.outWidth / 2
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            // Real generation pass with targeted sample sizes configurations
            val dynamicInputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val actualOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
                inJustDecodeBounds = false
            }
            val scaledBitmap = BitmapFactory.decodeStream(dynamicInputStream, null, actualOptions)
            dynamicInputStream?.close()

            if (scaledBitmap != null) {
                val outputStream = ByteArrayOutputStream()
                // Squashing quality directly down to 50% to stay strictly under 100KB-150KB
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                val byteArray = outputStream.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
