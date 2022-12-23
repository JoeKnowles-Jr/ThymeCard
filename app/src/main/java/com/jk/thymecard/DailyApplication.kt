package com.jk.thymecard

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DailyApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { DailyDatabase.getDatabase(this) }
    val repository by lazy { DailyRepository(database.dailyDao()) }
}