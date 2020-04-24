package com.example.dev_bytes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.dev_bytes.database.VideosDatabase
import com.example.dev_bytes.database.asDomainModel
import com.example.dev_bytes.domain.Video
import com.example.dev_bytes.network.Network
import com.example.dev_bytes.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val database: VideosDatabase) {

    /**
     * A playlist of videos that can be shown on the screen.
     */
    val videos: LiveData<List<Video>> =
        Transformations.map(database.videoDao.getVideos()) {
            it.asDomainModel()
        }


    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO){
            val playlist = Network.devbytes.getPlaylist().await()
            //Note the asterisk * is the spread operator.
            // It allows you to pass in an array to a function that expects varargs.
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}