package com.example.playlistmaker.ui.util

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

// Glide спамит в логах каждый раз когда я пытаюсь загрузить null, при этом плейсхолдеры работают
// как надо, поэтому оставляю в логах только критические ошибки, у меня весь лог иначе забит

@GlideModule
class GlideStopSpam : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setLogLevel(Log.ERROR)
    }
}