package com.fegno.callblocker.service

import android.telecom.Call
import android.telecom.CallScreeningService
import com.fegno.callblocker.CallBlockerApp
import com.fegno.callblocker.data.BlockedCall
import com.fegno.callblocker.data.RuleAction
import com.fegno.callblocker.domain.RuleMatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CallBlockerScreeningService : CallScreeningService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val isIncoming = callDetails.callDirection == Call.Details.DIRECTION_INCOMING
        val raw = callDetails.handle?.schemeSpecificPart
        if (!isIncoming || raw.isNullOrBlank()) {
            respondToCall(callDetails, CallResponse.Builder().build())
            return
        }
        scope.launch {
            val ctx = applicationContext
            val settings = CallBlockerApp.settings(ctx)

            if (settings.allowContactsNow() && ContactsChecker.isKnownContact(ctx, raw)) {
                respondToCall(callDetails, CallResponse.Builder().build())
                return@launch
            }

            val match = RuleMatcher.firstMatch(raw, CallBlockerApp.repository(ctx).enabledRules())
            if (match == null) {
                respondToCall(callDetails, CallResponse.Builder().build())
                return@launch
            }

            val response = when (match.action) {
                RuleAction.REJECT ->
                    CallResponse.Builder()
                        .setDisallowCall(true)
                        .setRejectCall(true)
                        .setSkipCallLog(false)
                        .setSkipNotification(false)
                        .build()

                RuleAction.VOICEMAIL ->
                    CallResponse.Builder()
                        .setDisallowCall(true)
                        .setRejectCall(false)
                        .setSkipCallLog(false)
                        .setSkipNotification(false)
                        .build()

                RuleAction.SILENCE ->
                    CallResponse.Builder()
                        .setSilenceCall(true)
                        .build()
            }

            respondToCall(callDetails, response)

            CallBlockerApp.repository(ctx).logBlocked(
                BlockedCall(
                    number = raw,
                    matchedRuleId = match.id,
                    matchedPattern = match.pattern,
                    matchedType = match.type,
                    action = match.action,
                ),
            )

            if (settings.notifyOnBlockNow()) {
                Notifier.notifyBlocked(ctx, raw, match.pattern, match.action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
