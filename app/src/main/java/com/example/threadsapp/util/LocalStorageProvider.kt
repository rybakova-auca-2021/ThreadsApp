package com.example.threadsapp.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object LocalStorageProvider {
    fun getFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        var fileName: String? = null

        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                fileName = cursor.getString(columnIndex)
            }
        }

        if (fileName.isNullOrEmpty()) {
            fileName = "temp_image.jpg"
        }

        val file = File(context.filesDir, fileName)
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(4 * 1024) // 4k buffer
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                return file
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
