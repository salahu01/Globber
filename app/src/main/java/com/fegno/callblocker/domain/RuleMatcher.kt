package com.fegno.callblocker.domain

import com.fegno.callblocker.data.BlockRule
import com.fegno.callblocker.data.PatternType

object RuleMatcher {

    fun normalize(number: String): String {
        val trimmed = number.trim()
        val hasLeadingPlus = trimmed.startsWith("+")
        val digits = trimmed.filter { it.isDigit() }
        return if (hasLeadingPlus) "+$digits" else digits
    }

    fun matches(number: String, rule: BlockRule): Boolean {
        val n = normalize(number)
        return when (rule.type) {
            PatternType.REGEX ->
                runCatching { Regex(rule.pattern).containsMatchIn(n) }.getOrDefault(false)
            else -> {
                val p = normalize(rule.pattern)
                when (rule.type) {
                    PatternType.EXACT -> n == p
                    PatternType.STARTS_WITH -> p.isNotEmpty() && n.startsWith(p)
                    PatternType.CONTAINS -> p.isNotEmpty() && n.contains(p)
                    PatternType.ENDS_WITH -> p.isNotEmpty() && n.endsWith(p)
                    PatternType.REGEX -> false
                }
            }
        }
    }

    fun firstMatch(number: String, rules: List<BlockRule>): BlockRule? =
        rules.filter { it.enabled }.firstOrNull { matches(number, it) }
}
