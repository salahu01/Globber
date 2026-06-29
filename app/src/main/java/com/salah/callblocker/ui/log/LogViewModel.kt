package com.salah.callblocker.ui.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salah.callblocker.CallBlockerApp
import com.salah.callblocker.data.BlockedCall
import com.salah.callblocker.data.RuleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: RuleRepository = CallBlockerApp.repository(app)

    val calls: StateFlow<List<BlockedCall>> = repo.blockedCalls.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList(),
    )

    fun clear() {
        viewModelScope.launch {
            repo.clearLog()
        }
    }
}
