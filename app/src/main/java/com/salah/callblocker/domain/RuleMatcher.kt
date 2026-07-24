package com.salah.callblocker.domain

import com.salah.callblocker.data.BlockRule
import com.salah.callblocker.data.PatternType

object RuleMatcher {

    /** Length of a national significant number for the common case (e.g. IN, US). */
    private const val NSN_LEN = 10

    /**
     * Canonical E.164-ish form: keeps a single leading '+' and all digits.
     * "+91 (988) 123-4567" -> "+919881234567".
     */
    fun normalize(number: String): String {
        val trimmed = number.trim()
        val hasLeadingPlus = trimmed.startsWith("+")
        val digits = trimmed.filter { it.isDigit() }
        return if (hasLeadingPlus) "+$digits" else digits
    }

    /**
     * National significant number: strips the country code and any trunk-access
     * leading zero, so a number the user types the way they see it locally
     * ("9881234567") matches the caller ID the OS delivers ("+919881234567").
     *
     * Heuristic (no libphonenumber dependency): anything longer than [NSN_LEN]
     * digits is treated as international/trunk-prefixed and reduced to its last
     * [NSN_LEN] digits after dropping leading zeros. Shorter values (short codes,
     * local numbers) are kept as-is.
     */
    fun national(number: String): String {
        var digits = number.filter { it.isDigit() }
        if (digits.length > NSN_LEN) {
            digits = digits.trimStart('0')
            if (digits.length > NSN_LEN) digits = digits.takeLast(NSN_LEN)
        }
        return digits
    }

    fun matches(number: String, rule: BlockRule): Boolean {
        if (rule.type == PatternType.REGEX) {
            val n = normalize(number)
            return runCatching { Regex(rule.pattern).containsMatchIn(n) }.getOrDefault(false)
        }

        // Full (E.164) form and national form are both tried; a rule matches if
        // it matches either, so "+91…" prefix rules and bare national-number
        // rules both work regardless of how the caller ID is formatted.
        val n = normalize(number)
        val p = normalize(rule.pattern)
        val nn = national(number)
        val pn = national(rule.pattern)

        return when (rule.type) {
            PatternType.EXACT ->
                (p.isNotEmpty() && n == p) || (pn.isNotEmpty() && nn == pn)
            PatternType.STARTS_WITH ->
                (p.isNotEmpty() && n.startsWith(p)) || (pn.isNotEmpty() && nn.startsWith(pn))
            PatternType.CONTAINS ->
                (p.isNotEmpty() && n.contains(p)) || (pn.isNotEmpty() && nn.contains(pn))
            PatternType.ENDS_WITH ->
                (p.isNotEmpty() && n.endsWith(p)) || (pn.isNotEmpty() && nn.endsWith(pn))
            PatternType.REGEX -> false
        }
    }

    fun firstMatch(number: String, rules: List<BlockRule>): BlockRule? =
        rules.filter { it.enabled }.firstOrNull { matches(number, it) }
}
