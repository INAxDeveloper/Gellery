package com.example.gallery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gallery.model.Album
import com.example.gallery.model.MediaItem
import com.example.gallery.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val _mediaList = MutableLiveData<List<MediaItem>>()
    val mediaList: LiveData<List<MediaItem>> = _mediaList
    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    fun loadMedia(mediaType: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val media = MediaRepository.getMedia(getApplication(), mediaType)
            _mediaList.postValue(media)
        }
    }
    fun loadAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = MediaRepository.getImageAlbums(getApplication())
            _albums.postValue(data)
        }
    }
}
