package com.codex.mobile

import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object CypherFailsafeManager {
    private const val TAG = "CypherFailsafe"
    private const val MAX_COMMAND_ITERATIONS = 10
    private const val TIME_WINDOW_MS = 5000L

    private val executionCount = AtomicInteger(0)
    private var lastResetTimestamp = System.currentTimeMillis()
    val isKillSwitchActivated = AtomicBoolean(false)

    fun checkFailsafe(): Boolean {
        if (isKillSwitchActivated.get()) {
            Log.w(TAG, "Failsafe triggered: Automation Kill-Switch is ACTIVE.")
            return false
        }

        val now = System.currentTimeMillis()
        if (now - lastResetTimestamp > TIME_WINDOW_MS) {
            executionCount.set(0)
            lastResetTimestamp = now
        }

        val currentCount = executionCount.incrementAndGet()
        if (currentCount > MAX_COMMAND_ITERATIONS) {
            Log.e(TAG, "Infinite loop detected! Exceeded $MAX_COMMAND_ITERATIONS actions in ${TIME_WINDOW_MS}ms. Activating Kill-Switch.")
            isKillSwitchActivated.set(true)
            return false
        }
        return true
    }

    fun activateKillSwitch(activate: Boolean) {
        isKillSwitchActivated.set(activate)
        Log.i(TAG, "Automation kill-switch status set to: $activate")
    }

    fun reset() {
        executionCount.set(0)
        lastResetTimestamp = System.currentTimeMillis()
        isKillSwitchActivated.set(false)
    }
}
