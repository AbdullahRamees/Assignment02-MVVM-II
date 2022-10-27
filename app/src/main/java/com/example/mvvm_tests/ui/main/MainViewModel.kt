package com.example.mvvm_tests.ui.main


import android.util.Log
import androidx.lifecycle.*
import com.example.mvvm_tests.ui.main.models.DevByteVideo
import com.example.mvvm_tests.ui.main.models.DatabaseVideo
import com.example.mvvm_tests.ui.main.models.DatabaseVideoContainer
import com.example.mvvm_tests.ui.main.models.asDomainModel
import kotlinx.coroutines.launch
import java.io.IOException



class MainViewModel : ViewModel() {


    private var videoDao:VideoDao? = null

    private val _playlist = MutableLiveData<List<DevByteVideo>>()
    val playlist: LiveData<List<DevByteVideo>>
        get() = _playlist

    private val _databasePlaylist = MutableLiveData<List<DatabaseVideo>?>()
    val databasePlaylist: LiveData<List<DatabaseVideo>?>
        get() = _databasePlaylist


    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
        refreshDataFromDatabase()
    }

    private fun refreshDataFromDatabase() = viewModelScope.launch {

        Log.e("Database","Try")
        val  databasePlaylistLocal =videoDao?.getVideos()
        Log.e("Database",databasePlaylistLocal?.size.toString())
        _databasePlaylist.postValue(databasePlaylistLocal)
    }

    fun addtoDataBase(Videos:List<DevByteVideo>) = viewModelScope.launch {
        videoDao!!.insertAll(DatabaseVideoContainer(Videos).asDomainModel())
        refreshDataFromDatabase()
    }



    private fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            val playlist = DevByteNetwork.devbytes.getPlaylist()
            _playlist.postValue(playlist.asDomainModel())
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        } catch (networkError: IOException) {
            _eventNetworkError.value = true
        }
    }

    fun SetDao(videoDao: VideoDao?){
        this.videoDao = videoDao
    }

}


