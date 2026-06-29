package com.salah.callblocker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedCallDao {
    @Query("SELECT * FROM blocked_calls ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<BlockedCall>>

    @Insert
    suspend fun insert(call: BlockedCall): Long

    @Query("DELETE FROM blocked_calls")
    suspend fun clearAll()
}
