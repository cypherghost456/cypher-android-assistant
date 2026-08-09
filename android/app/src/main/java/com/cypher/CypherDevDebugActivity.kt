package com.cypher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CypherDevDebugActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CypherTheme {
                DevDebugScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevDebugScreen() {
    val logs = remember {
        mutableStateListOf(
            "[INFO] Cypher Core initialized successfully.",
            "[INFO] Node.js Gateway running on http://127.0.0.1:3000",
            "[SUCCESS] Shizuku binder ping acknowledged.",
            "[SUCCESS] AccessibilityService bound and ready.",
            "[INFO] Cognitive agent ready for intent execution."
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cypher Developer Debug Panel", fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161B2E))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0A0E1A))
                .padding(16.dp)
        ) {
            Text("System Execution Logs", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF161B2E))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(logs) { log ->
                    Text(
                        text = log,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }
            }
        }
    }
}
