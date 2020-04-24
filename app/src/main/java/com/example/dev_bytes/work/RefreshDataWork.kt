package com.example.dev_bytes.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException
import com.example.dev_bytes.database.getDatabase
import com.example.dev_bytes.repository.VideosRepository
import java.io.IOException


class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)
        try {
            repository.refreshVideos()
            return Result.success()

        } catch (e: HttpException) {
            return Result.retry()
        }
        catch (e: IOException) {
            return Result.failure()
        }
    }
}