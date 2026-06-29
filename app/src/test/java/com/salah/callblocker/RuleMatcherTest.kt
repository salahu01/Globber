package com.salah.callblocker

import com.salah.callblocker.data.BlockRule
import com.salah.callblocker.data.PatternType
import com.salah.callblocker.domain.RuleMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleMatcherTest {

    private fun rule(
        pattern: String,
        type: PatternType,
        id: Long = 0,
        enabled: Boolean = true,
        label: String = "",
    ): BlockRule = BlockRule(
        id = id,
        pattern = pattern,
        type = type,
        label = label,
        enabled = enabled,
    )

    @Test
    fun normalize_stripsFormattingAndKeepsSingleLeadingPlus() {
        assertEquals("+919881234567", RuleMatcher.normalize("+91 (988) 123-4567"))
        assertEquals("0442233", RuleMatcher.normalize("044-2233"))
        assertEquals("0442233", RuleMatcher.normalize("  044 . 22 . 33  "))
        // a '+' that is not leading does not survive
        assertEquals("123456", RuleMatcher.normalize("12+3456"))
        // single leading '+' only
        assertEquals("+123456", RuleMatcher.normalize("+1.2.3.4.5.6"))
    }

    @Test
    fun exact_matchesOnlySameNormalizedNumber() {
        val r = rule("+91 988 123 4567", PatternType.EXACT)
        assertTrue(RuleMatcher.matches("+919881234567", r))
        assertTrue(RuleMatcher.matches("+91-988-123-4567", r))
        assertFalse(RuleMatcher.matches("+919881234568", r))
    }

    @Test
    fun startsWith_matchesPrefixAndEmptyNeverMatches() {
        val r = rule("+9199", PatternType.STARTS_WITH)
        assertTrue(RuleMatcher.matches("+91 99 88 77 66", r))
        assertFalse(RuleMatcher.matches("+91 88 77 66 55", r))

        val empty = rule("", PatternType.STARTS_WITH)
        assertFalse(RuleMatcher.matches("+919988776655", empty))
    }

    @Test
    fun contains_matchesSubstringAndRejectsNonSubstring() {
        val r = rule("8888", PatternType.CONTAINS)
        assertTrue(RuleMatcher.matches("+91 98 88 88 12 34", r))
        assertFalse(RuleMatcher.matches("+91 98 12 34 56 70", r))

        val empty = rule("", PatternType.CONTAINS)
        assertFalse(RuleMatcher.matches("+919888881234", empty))
    }

    @Test
    fun endsWith_matchesSuffixAndRejectsOthers() {
        val r = rule("4567", PatternType.ENDS_WITH)
        assertTrue(RuleMatcher.matches("+91 988 123 4567", r))
        assertFalse(RuleMatcher.matches("+91 988 123 4568", r))

        val empty = rule("", PatternType.ENDS_WITH)
        assertFalse(RuleMatcher.matches("+919881234567", empty))
    }

    @Test
    fun regex_validMatchesAndInvalidReturnsFalse() {
        val valid = rule("^\\+91", PatternType.REGEX)
        assertTrue(RuleMatcher.matches("+91 988 123 4567", valid))
        assertFalse(RuleMatcher.matches("+1 555 0100", valid))

        val invalid = rule("[", PatternType.REGEX)
        assertFalse(RuleMatcher.matches("+919881234567", invalid))
    }

    @Test
    fun firstMatch_skipsDisabledAndReturnsFirstEnabledInOrder() {
        val disabled = rule("+91", PatternType.STARTS_WITH, id = 1, enabled = false)
        val firstEnabled = rule("9881", PatternType.CONTAINS, id = 2, enabled = true)
        val secondEnabled = rule("4567", PatternType.ENDS_WITH, id = 3, enabled = true)
        val rules = listOf(disabled, firstEnabled, secondEnabled)

        val result = RuleMatcher.firstMatch("+91 988 123 4567", rules)
        assertSame(firstEnabled, result)
    }

    @Test
    fun firstMatch_returnsNullWhenNoneMatch() {
        val rules = listOf(
            rule("0000", PatternType.CONTAINS, id = 1),
            rule("+1", PatternType.STARTS_WITH, id = 2),
        )
        assertNull(RuleMatcher.firstMatch("+91 988 123 4567", rules))
    }

    @Test
    fun firstMatch_returnsNullWhenOnlyMatchIsDisabled() {
        val rules = listOf(
            rule("+91", PatternType.STARTS_WITH, id = 1, enabled = false),
        )
        assertNull(RuleMatcher.firstMatch("+919881234567", rules))
    }
}
