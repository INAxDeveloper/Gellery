package com.example.gallery.model

import android.net.Uri

data class Album(
    val name: String,
    val thumbnailUri: Uri,
    val itemCount: Int
)
