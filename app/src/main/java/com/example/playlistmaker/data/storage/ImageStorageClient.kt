package com.example.playlistmaker.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.storage.FileStorageClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ImageStorageClient(
    private val context: Context,
    dirName: String
) : FileStorageClient {
    private val filePath =
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dirName)

    init {
        if (!filePath.exists()) {
            filePath.mkdir()
        }
    }

    override fun createFile(content: Uri): Flow<Uri> = flow {
        val fileName = UUID.randomUUID().toString()

        val file = File(filePath, "$fileName.jpg")
        if (!file.exists()) {
            file.createNewFile()
        }

        loadImage(file, content)
        emit(file.toUri())
    }
        .flowOn(Dispatchers.IO)

    override suspend fun deleteFile(filePath: Uri?) {
        if (filePath != null) {
            val file = File(filePath.path!!)
            file.delete()
        }
    }

    override fun getData(fileName: String): Flow<Uri?> = flow {
        val file = File(filePath, "$fileName.jpg")
        if (!file.exists()) {
            emit(null)
        } else {
            emit(file.toUri())
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun loadImage(file: File, content: Uri) {
        withContext(Dispatchers.IO) {

            val inputStream = context.contentResolver.openInputStream(content)
            val outputStream = FileOutputStream(file)

            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }
    }
}
