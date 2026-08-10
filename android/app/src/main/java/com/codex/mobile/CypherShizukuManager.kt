package com.codex.mobile

import android.content.pm.PackageManager
import android.util.Log
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object CypherShizukuManager {
    private const val TAG = "CypherShizuku"

    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
    }

    fun hasPermission(): Boolean {
        return try {
            if (Shizuku.isPreV11()) {
                false
            } else {
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
            }
        } catch (e: Exception) {
            false
        }
    }

    fun requestPermission(requestCode: Int) {
        try {
            if (!hasPermission() && !Shizuku.isPreV11()) {
                Shizuku.requestPermission(requestCode)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to request Shizuku permission", e)
        }
    }

    fun executeShellCommand(command: String): String {
        if (!isShizukuAvailable()) {
            return "Error: Shizuku is not running or available."
        }
        return try {
            // Use reflection because newProcess is private in Shizuku 13.1.5
            val newProcessMethod = Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            )
            newProcessMethod.isAccessible = true
            val process = newProcessMethod.invoke(
                null,
                arrayOf("sh", "-c", command),
                null,
                null
            ) as Process

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            process.waitFor()
            output.toString().trim()
        } catch (e: Exception) {
            Log.e(TAG, "Shizuku shell execution failed", e)
            "Error: ${e.message}"
        }
    }

    fun installApp(apkPath: String): String {
        return executeShellCommand("pm install -r \"$apkPath\"")
    }

    fun uninstallApp(packageName: String): String {
        return executeShellCommand("pm uninstall \"$packageName\"")
    }

    fun forceStopApp(packageName: String): String {
        return executeShellCommand("am force-stop \"$packageName\"")
    }
}
