package com.example.dev_bytes.database

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.room.*


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
@Database(entities = [DatabaseVideo::class],version=1)
abstract  class  VideosDatabase: RoomDatabase(){
   abstract val  videoDao : VideoDao


}

private lateinit var INSTANCE: VideosDatabase

fun getDatabase(context: Context): VideosDatabase {
    synchronized(VideosDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                VideosDatabase::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}