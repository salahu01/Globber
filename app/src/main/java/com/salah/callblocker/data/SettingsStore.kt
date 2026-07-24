package com.salah.callblocker.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** How the app picks its light/dark palette. */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK;

    companion object {
        fun fromName(name: String?): ThemeMode =
            entries.firstOrNull { it.name == name } ?: SYSTEM
    }
}

class SettingsStore(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _notifyOnBlock =
        MutableStateFlow(prefs.getBoolean(KEY_NOTIFY_ON_BLOCK, DEFAULT_NOTIFY_ON_BLOCK))
    val notifyOnBlock: StateFlow<Boolean> = _notifyOnBlock.asStateFlow()

    private val _blockUnknown =
        MutableStateFlow(prefs.getBoolean(KEY_BLOCK_UNKNOWN, DEFAULT_BLOCK_UNKNOWN))
    val blockUnknown: StateFlow<Boolean> = _blockUnknown.asStateFlow()

    private val _themeMode =
        MutableStateFlow(ThemeMode.fromName(prefs.getString(KEY_THEME_MODE, null)))
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setNotifyOnBlock(value: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFY_ON_BLOCK, value).apply()
        _notifyOnBlock.value = value
    }

    fun setBlockUnknown(value: Boolean) {
        prefs.edit().putBoolean(KEY_BLOCK_UNKNOWN, value).apply()
        _blockUnknown.value = value
    }

    fun setThemeMode(value: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, value.name).apply()
        _themeMode.value = value
    }

    fun notifyOnBlockNow(): Boolean =
        prefs.getBoolean(KEY_NOTIFY_ON_BLOCK, DEFAULT_NOTIFY_ON_BLOCK)

    fun blockUnknownNow(): Boolean =
        prefs.getBoolean(KEY_BLOCK_UNKNOWN, DEFAULT_BLOCK_UNKNOWN)

    companion object {
        private const val PREFS_NAME = "callblocker_prefs"
        private const val KEY_NOTIFY_ON_BLOCK = "notify_on_block"
        private const val KEY_BLOCK_UNKNOWN = "block_unknown"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val DEFAULT_NOTIFY_ON_BLOCK = true
        private const val DEFAULT_BLOCK_UNKNOWN = false
    }
}
