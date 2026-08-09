package com.cypher

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class CypherSettingsManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "cypher_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_GCM,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var aiModel: String
        get() = prefs.getString("ai_model", "gpt-4o") ?: "gpt-4o"
        set(value) = prefs.edit().putString("ai_model", value).apply()

    var voiceEnabled: Boolean
        get() = prefs.getBoolean("voice_enabled", true)
        set(value) = prefs.edit().putBoolean("voice_enabled", value).apply()

    var shizukuEnabled: Boolean
        get() = prefs.getBoolean("shizuku_enabled", true)
        set(value) = prefs.edit().putBoolean("shizuku_enabled", value).apply()

    var localMode: Boolean
        get() = prefs.getBoolean("local_mode", false)
        set(value) = prefs.edit().putBoolean("local_mode", value).apply()

    var themeAccent: String
        get() = prefs.getString("theme_accent", "Neon Cyan") ?: "Neon Cyan"
        set(value) = prefs.edit().putString("theme_accent", value).apply()

    fun clearHistory() {
        prefs.edit().remove("chat_history").apply()
    }
}
