package com.salah.callblocker

import android.app.Application
import android.content.Context
import com.salah.callblocker.data.AppDatabase
import com.salah.callblocker.data.RuleRepository
import com.salah.callblocker.data.SettingsStore

class CallBlockerApp : Application() {

    val repository: RuleRepository by lazy {
        val db = AppDatabase.getInstance(this)
        RuleRepository(db.blockRuleDao(), db.blockedCallDao())
    }

    val settings: SettingsStore by lazy { SettingsStore(this) }

    companion object {
        fun repository(context: Context): RuleRepository =
            (context.applicationContext as CallBlockerApp).repository

        fun settings(context: Context): SettingsStore =
            (context.applicationContext as CallBlockerApp).settings
    }
}
