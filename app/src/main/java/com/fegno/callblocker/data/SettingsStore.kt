package com.fegno.callblocker.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsStore(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _allowContacts =
        MutableStateFlow(prefs.getBoolean(KEY_ALLOW_CONTACTS, DEFAULT_ALLOW_CONTACTS))
    val allowContacts: StateFlow<Boolean> = _allowContacts.asStateFlow()

    private val _notifyOnBlock =
        MutableStateFlow(prefs.getBoolean(KEY_NOTIFY_ON_BLOCK, DEFAULT_NOTIFY_ON_BLOCK))
    val notifyOnBlock: StateFlow<Boolean> = _notifyOnBlock.asStateFlow()

    fun setAllowContacts(value: Boolean) {
        prefs.edit().putBoolean(KEY_ALLOW_CONTACTS, value).apply()
        _allowContacts.value = value
    }

    fun setNotifyOnBlock(value: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFY_ON_BLOCK, value).apply()
        _notifyOnBlock.value = value
    }

    fun allowContactsNow(): Boolean =
        prefs.getBoolean(KEY_ALLOW_CONTACTS, DEFAULT_ALLOW_CONTACTS)

    fun notifyOnBlockNow(): Boolean =
        prefs.getBoolean(KEY_NOTIFY_ON_BLOCK, DEFAULT_NOTIFY_ON_BLOCK)

    companion object {
        private const val PREFS_NAME = "callblocker_prefs"
        private const val KEY_ALLOW_CONTACTS = "allow_contacts"
        private const val KEY_NOTIFY_ON_BLOCK = "notify_on_block"
        private const val DEFAULT_ALLOW_CONTACTS = true
        private const val DEFAULT_NOTIFY_ON_BLOCK = true
    }
}
