package com.example.playlistmaker.domain.storage

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface FileStorageClient {
    fun createFile(content: Uri): Flow<Uri>
    fun getData(fileName: String): Flow<Uri?>
    suspend fun deleteFile(filePath: Uri?)
}