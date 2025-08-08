package com.example.gallery.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.gallery.model.Album
import com.example.gallery.model.MediaItem

object MediaRepository {

    fun getMedia(context: Context, mediaType: Int): List<MediaItem> {
        val items = mutableListOf<MediaItem>()
        val collection = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )
        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
        val selectionArgs = arrayOf(mediaType.toString())
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            collection, projection, selection, selectionArgs, sortOrder
        )

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val dateCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val name = it.getString(nameCol)
                val date = it.getLong(dateCol)
                val uri = ContentUris.withAppendedId(collection, id)

                items.add(MediaItem(uri, name, date))
            }
        }

        return items
    }
    fun getImageAlbums(context: Context): List<Album> {
        val albums = mutableListOf<Album>()
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media._ID
        )

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        val albumMap = linkedMapOf<String, MutableList<Long>>()

        cursor?.use {
            val bucketColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (it.moveToNext()) {
                val bucketName = it.getString(bucketColumn)
                val imageId = it.getLong(idColumn)

                if (!albumMap.containsKey(bucketName)) {
                    albumMap[bucketName] = mutableListOf()
                }

                albumMap[bucketName]?.add(imageId)
            }
        }

        albumMap.forEach { (name, ids) ->
            val thumbnailUri = ContentUris.withAppendedId(uri, ids.first())
            albums.add(Album(name, thumbnailUri, ids.size))
        }

        return albums
    }

}
