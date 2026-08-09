package com.cypher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CypherSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = CypherSettingsManager(this)
        setContent {
            CypherTheme {
                SettingsScreen(settings)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settings: CypherSettingsManager) {
    var voiceEnabled by remember { mutableStateOf(settings.voiceEnabled) }
    var shizukuEnabled by remember { mutableStateOf(settings.shizukuEnabled) }
    var localMode by remember { mutableStateOf(settings.localMode) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cypher System Settings", fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161B2E))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0A0E1A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Voice & Wake Word", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Always-On Voice Assistant", color = Color.White)
                Switch(
                    checked = voiceEnabled,
                    onCheckedChange = {
                        voiceEnabled = it
                        settings.voiceEnabled = it
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00E5FF))
                )
            }

            Divider(color = Color(0xFF334155))

            Text("Device Control & Permissions", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Enable Shizuku Rootless Control", color = Color.White)
                Switch(
                    checked = shizukuEnabled,
                    onCheckedChange = {
                        shizukuEnabled = it
                        settings.shizukuEnabled = it
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00E5FF))
                )
            }

            Divider(color = Color(0xFF334155))

            Text("Privacy & Security", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Strict Local-Only Processing", color = Color.White)
                Switch(
                    checked = localMode,
                    onCheckedChange = {
                        localMode = it
                        settings.localMode = it
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00E5FF))
                )
            }
        }
    }
}
