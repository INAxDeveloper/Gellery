package com.example.gallery.model

import android.net.Uri

data class MediaItem(
    val uri: Uri,
    val displayName: String,
    val dateAdded: Long
)
