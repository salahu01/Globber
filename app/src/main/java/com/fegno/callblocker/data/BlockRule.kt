package com.fegno.callblocker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class PatternType { EXACT, STARTS_WITH, CONTAINS, ENDS_WITH, REGEX }

enum class RuleAction { REJECT, SILENCE, VOICEMAIL }

@Entity(tableName = "block_rules")
data class BlockRule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pattern: String,
    val type: PatternType,
    val label: String = "",
    val enabled: Boolean = true,
    val action: RuleAction = RuleAction.REJECT,
    val position: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

class Converters {
    @TypeConverter fun fromType(t: PatternType): String = t.name
    @TypeConverter fun toType(s: String): PatternType = PatternType.valueOf(s)
    @TypeConverter fun fromAction(a: RuleAction): String = a.name
    @TypeConverter fun toAction(s: String): RuleAction = RuleAction.valueOf(s)
}
