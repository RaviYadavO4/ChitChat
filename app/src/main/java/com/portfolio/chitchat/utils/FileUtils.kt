package com.portfolio.chitchat.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {

    fun fileToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                // Safeguard check: If picked document is greater than 850KB, cancel dispatch
                if (bytes.size > 850 * 1024) {
                    return "OVERSIZE_ERROR"
                }
                Base64.encodeToString(bytes, Base64.DEFAULT)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun openBase64PdfDocument(context: Context, base64PdfStr: String) {
        try {
            val cacheFolder = File(context.cacheDir, "shared_pdfs")
            if (!cacheFolder.exists()) cacheFolder.mkdirs()
            val tempPdfFile = File(cacheFolder, "ChitChat_Doc_${System.currentTimeMillis()}.pdf")

            val pdfBytes = Base64.decode(base64PdfStr, Base64.DEFAULT)
            FileOutputStream(tempPdfFile).use { it.write(pdfBytes) }

            val fileUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempPdfFile)
            val pdfIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, "application/pdf")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(pdfIntent, "Open document via:"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
