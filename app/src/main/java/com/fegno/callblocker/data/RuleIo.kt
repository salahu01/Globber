package com.fegno.callblocker.data

import org.json.JSONArray
import org.json.JSONObject

object RuleIo {
    fun export(rules: List<BlockRule>): String {
        val array = JSONArray()
        for (rule in rules) {
            val obj = JSONObject()
            obj.put("pattern", rule.pattern)
            obj.put("type", rule.type.name)
            obj.put("label", rule.label)
            obj.put("enabled", rule.enabled)
            obj.put("action", rule.action.name)
            obj.put("position", rule.position)
            array.put(obj)
        }
        return array.toString()
    }

    fun import(json: String): List<BlockRule> {
        val result = mutableListOf<BlockRule>()
        runCatching {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                runCatching {
                    val obj = array.getJSONObject(i)
                    val pattern = obj.getString("pattern")
                    val type = runCatching {
                        PatternType.valueOf(obj.optString("type", PatternType.EXACT.name))
                    }.getOrDefault(PatternType.EXACT)
                    val label = obj.optString("label", "")
                    val enabled = obj.optBoolean("enabled", true)
                    val action = runCatching {
                        RuleAction.valueOf(obj.optString("action", RuleAction.REJECT.name))
                    }.getOrDefault(RuleAction.REJECT)
                    val position = obj.optInt("position", 0)
                    result.add(
                        BlockRule(
                            id = 0,
                            pattern = pattern,
                            type = type,
                            label = label,
                            enabled = enabled,
                            action = action,
                            position = position,
                        )
                    )
                }
            }
        }
        return result
    }
}
