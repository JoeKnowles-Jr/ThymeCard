package com.jk.thymecard

import android.content.Context
import androidx.room.*

@Database(
    entities = [Daily::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DailyDatabase : RoomDatabase() {

    abstract fun dailyDao(): DailyDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DailyDatabase? = null

        fun getDatabase(
            context: Context
        ): DailyDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailyDatabase::class.java,
                    "expense_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}