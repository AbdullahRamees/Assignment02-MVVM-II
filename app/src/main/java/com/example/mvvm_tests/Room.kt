package com.example.mvvm_tests.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvm_tests.ui.main.models.DatabaseVideo
import com.example.mvvm_tests.ui.main.models.DevByteVideo

@Dao
interface VideoDao {
    @Query("SELECT * FROM  video_table")
    fun getVideos(): List<DatabaseVideo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos:List<DatabaseVideo>)
}

@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase: RoomDatabase() {
    abstract val videoDao: VideoDao

    companion object{
        private lateinit var INSTANCE: VideosDatabase

        fun getDatabase(context: Context): VideosDatabase {
            synchronized(VideosDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        VideosDatabase::class.java,
                        "videos").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }
}

