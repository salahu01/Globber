package com.salah.callblocker.data

import kotlinx.coroutines.flow.Flow

class RuleRepository(private val ruleDao: BlockRuleDao, private val callDao: BlockedCallDao) {
    val rules: Flow<List<BlockRule>> = ruleDao.observeAll()
    val blockedCalls: Flow<List<BlockedCall>> = callDao.observeAll()

    suspend fun enabledRules(): List<BlockRule> = ruleDao.enabled()

    suspend fun addRule(pattern: String, type: PatternType, label: String, action: RuleAction): Long {
        val pos = ruleDao.nextPosition()
        return ruleDao.upsert(
            BlockRule(pattern = pattern, type = type, label = label, action = action, position = pos)
        )
    }

    suspend fun updateRule(rule: BlockRule): Long = ruleDao.upsert(rule)

    suspend fun deleteRule(rule: BlockRule) = ruleDao.delete(rule)

    suspend fun setEnabled(id: Long, enabled: Boolean) = ruleDao.setEnabled(id, enabled)

    suspend fun move(rule: BlockRule, up: Boolean) {
        val all = ruleDao.getAll()
        val idx = all.indexOfFirst { it.id == rule.id }
        if (idx < 0) return
        val swapIdx = if (up) idx - 1 else idx + 1
        if (swapIdx !in all.indices) return
        val other = all[swapIdx]
        ruleDao.setPosition(rule.id, other.position)
        ruleDao.setPosition(other.id, rule.position)
    }

    suspend fun logBlocked(call: BlockedCall) = callDao.insert(call)
    suspend fun clearLog() = callDao.clearAll()

    suspend fun exportJson(): String = RuleIo.export(ruleDao.getAll())

    suspend fun importJson(json: String, replace: Boolean) {
        val incoming = RuleIo.import(json)
        if (replace) ruleDao.clearRules()
        var pos = ruleDao.nextPosition()
        for (r in incoming) {
            ruleDao.upsert(r.copy(id = 0, position = pos))
            pos++
        }
    }
}
