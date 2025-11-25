package com.example.playlistmaker.domain.storage

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
    fun clearData()
}