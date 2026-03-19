package com.example.playlistmaker.domain.storage

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface FileStorageClient {
    fun createFile(fileName: String, content: Uri): Flow<Uri>
    fun getData(fileName: String): Flow<Uri?>
}