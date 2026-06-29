package com.salah.callblocker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockRuleDao {
    @Query("SELECT * FROM block_rules ORDER BY position ASC, createdAt ASC")
    fun observeAll(): Flow<List<BlockRule>>

    @Query("SELECT * FROM block_rules WHERE enabled = 1 ORDER BY position ASC")
    suspend fun enabled(): List<BlockRule>

    @Query("SELECT * FROM block_rules ORDER BY position ASC")
    suspend fun getAll(): List<BlockRule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rule: BlockRule): Long

    @Delete
    suspend fun delete(rule: BlockRule)

    @Query("UPDATE block_rules SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean)

    @Query("UPDATE block_rules SET position = :position WHERE id = :id")
    suspend fun setPosition(id: Long, position: Int)

    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM block_rules")
    suspend fun nextPosition(): Int

    @Query("DELETE FROM block_rules")
    suspend fun clearRules()
}
