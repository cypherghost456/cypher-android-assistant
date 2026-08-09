package com.codex.mobile

import android.content.Context
import android.content.Intent
import android.util.Log

object CypherCommandEngine {
    private const val TAG = "CypherCommandEngine"

    fun executeCommand(context: Context, rawCommand: String): String {
        val cmd = rawCommand.trim().lowercase()
        Log.i(TAG, "Executing Agent Cypher command: $cmd")

        return when {
            cmd.startsWith("open ") -> {
                val appName = cmd.removePrefix("open ").trim()
                openAppByName(context, appName)
            }
            cmd.startsWith("close ") -> {
                val pkgName = cmd.removePrefix("close ").trim()
                if (CypherShizukuManager.isShizukuAvailable()) {
                    CypherShizukuManager.forceStopApp(pkgName)
                    "Force stopped package: $pkgName via Shizuku"
                } else {
                    "Shizuku required for force-stop."
                }
            }
            cmd.startsWith("tap ") -> {
                val coords = cmd.removePrefix("tap ").split(",")
                if (coords.size == 2) {
                    val x = coords[0].trim().toFloatOrNull() ?: 0f
                    val y = coords[1].trim().toFloatOrNull() ?: 0f
                    val accessibility = CypherAccessibilityService.getInstance()
                    if (accessibility != null) {
                        val success = accessibility.clickAt(x, y)
                        if (success) "Tapped at ($x, $y)" else "Failed to dispatch tap gesture."
                    } else {
                        "Accessibility Service not enabled."
                    }
                } else {
                    "Invalid coordinates. Use format: tap x,y"
                }
            }
            cmd.startsWith("click ") -> {
                val text = cmd.removePrefix("click ").trim()
                val accessibility = CypherAccessibilityService.getInstance()
                if (accessibility != null) {
                    val success = accessibility.findAndClickText(text)
                    if (success) "Clicked UI element containing '$text'" else "UI element '$text' not found."
                } else {
                    "Accessibility Service not enabled."
                }
            }
            else -> {
                "Unknown command intent: $rawCommand. Routed to OpenClaw LLM backend."
            }
        }
    }

    private fun openAppByName(context: Context, appName: String): String {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(0)
        for (app in packages) {
            val label = pm.getApplicationLabel(app).toString().lowercase()
            if (label.contains(appName)) {
                val launchIntent = pm.getLaunchIntentForPackage(app.packageName)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    return "Opened application: ${app.packageName}"
                }
            }
        }
        return "Application matching '$appName' not found."
    }
}
