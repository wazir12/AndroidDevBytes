package com.example.dev_bytes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface  VideoDao{
//if we return only List<Database Video>, the ui thread will be blocked but
// if we return the Live Data, the ui thread
//will not block and the Data will be fetched in the background and
// once new data is available the room will do update for you also
    //LiveData will also watch for changes int he Database
    @Query("Select * from databasevideo" )
    fun  getVideos():LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg video: DatabaseVideo)
}