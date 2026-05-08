package com.jenugumpu.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jenugumpu.app.data.dao.BatchDao
import com.jenugumpu.app.data.dao.HarvestLogDao
import com.jenugumpu.app.data.entity.Batch
import com.jenugumpu.app.data.entity.BatchLogCrossRef
import com.jenugumpu.app.data.entity.HarvestLog

/**
 * Room Database for Jenu-Gumpu app
 * All data persists locally for offline operation
 * Version 1 - Initial schema
 */
@Database(
    entities = [
        HarvestLog::class,
        Batch::class,
        BatchLogCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JenuGumpuDatabase : RoomDatabase() {

    abstract fun harvestLogDao(): HarvestLogDao
    abstract fun batchDao(): BatchDao

    companion object {
        @Volatile
        private var INSTANCE: JenuGumpuDatabase? = null

        /**
         * Singleton pattern to ensure only one database instance
         * Thread-safe implementation using synchronized
         */
        fun getDatabase(context: Context): JenuGumpuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JenuGumpuDatabase::class.java,
                    "jenugumpu_database"
                )
                    .fallbackToDestructiveMigration() // For development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
