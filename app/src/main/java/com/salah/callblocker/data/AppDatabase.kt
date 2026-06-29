package com.salah.callblocker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BlockRule::class, BlockedCall::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blockRuleDao(): BlockRuleDao
    abstract fun blockedCallDao(): BlockedCallDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "callblocker.db"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}
