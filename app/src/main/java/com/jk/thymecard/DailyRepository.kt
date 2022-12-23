package com.jk.thymecard

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlin.concurrent.thread

class DailyRepository(private val dailyDao: DailyDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: LiveData<List<Daily>> = dailyDao.getDailies()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(daily: Daily) {
        dailyDao.insert(daily)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun delete(daily: Daily) {
        thread(start=true) {
            dailyDao.delete(daily)
        }
    }
}