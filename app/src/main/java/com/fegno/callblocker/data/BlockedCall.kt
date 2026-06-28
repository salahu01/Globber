package com.fegno.callblocker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_calls")
data class BlockedCall(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val number: String,
    val matchedRuleId: Long?,
    val matchedPattern: String,
    val matchedType: PatternType,
    val action: RuleAction,
    val timestamp: Long = System.currentTimeMillis(),
)
