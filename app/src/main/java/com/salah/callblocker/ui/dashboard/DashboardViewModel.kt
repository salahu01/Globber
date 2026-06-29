package com.salah.callblocker.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salah.callblocker.CallBlockerApp
import com.salah.callblocker.data.BlockedCall
import com.salah.callblocker.data.RuleRepository
import com.salah.callblocker.ui.components.DeltaInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

data class DashboardUiState(
    val totalBlocked: Int = 0,
    val blockedToday: Int = 0,
    val blockedThisWeek: Int = 0,
    val weekDelta: DeltaInfo? = null,
    val todayTrack: Float = 0f,
    val activeRules: Int = 0,
    val totalRules: Int = 0,
    val topBlockedNumber: String? = null,
    val topBlockedCount: Int = 0,
    val recent: List<BlockedCall> = emptyList(),
)

class DashboardViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: RuleRepository = CallBlockerApp.repository(app)

    val state: StateFlow<DashboardUiState> =
        combine(repo.rules, repo.blockedCalls) { rules, calls ->
            val now = System.currentTimeMillis()
            val cal = Calendar.getInstance()
            cal.timeInMillis = now
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startOfToday = cal.timeInMillis
            cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
            val startOfWeek = cal.timeInMillis
            val startOfLastWeek = startOfWeek - 7L * 24 * 60 * 60 * 1000

            val thisWeek = calls.count { it.timestamp >= startOfWeek }
            val lastWeek = calls.count { it.timestamp in startOfLastWeek until startOfWeek }
            val today = calls.count { it.timestamp >= startOfToday }

            val weekDelta = when {
                thisWeek == 0 && lastWeek == 0 -> null
                lastWeek == 0 -> DeltaInfo("+$thisWeek", positive = true)
                else -> {
                    val diff = thisWeek - lastWeek
                    val pct = (diff * 100) / lastWeek
                    DeltaInfo((if (pct >= 0) "+$pct%" else "$pct%"), positive = pct >= 0)
                }
            }

            val top = calls.groupingBy { it.number }.eachCount().maxByOrNull { it.value }

            // Normalize today's count against the busiest day this week for the mini-track.
            val maxDaily = calls
                .filter { it.timestamp >= startOfWeek }
                .groupingBy { dayKey(it.timestamp) }
                .eachCount().values.maxOrNull() ?: 0
            val todayTrack = if (maxDaily > 0) today.toFloat() / maxDaily else 0f

            DashboardUiState(
                totalBlocked = calls.size,
                blockedToday = today,
                blockedThisWeek = thisWeek,
                weekDelta = weekDelta,
                todayTrack = todayTrack,
                activeRules = rules.count { it.enabled },
                totalRules = rules.size,
                topBlockedNumber = top?.key,
                topBlockedCount = top?.value ?: 0,
                recent = calls.sortedByDescending { it.timestamp }.take(5),
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DashboardUiState(),
        )

    private fun dayKey(ts: Long): Long {
        val c = Calendar.getInstance()
        c.timeInMillis = ts
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }
}
