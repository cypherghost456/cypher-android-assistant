package com.cypher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CypherOnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CypherTheme {
                OnboardingScreen(
                    onFinish = {
                        startActivity(Intent(this, CypherUIActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E1A))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AGENT CYPHER",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00E5FF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when (step) {
                1 -> "Welcome, Sumair. Cypher is your fully autonomous AI companion running natively on your Android device."
                2 -> "Cypher features rootless device control via Shizuku and UI automation via Accessibility Service."
                else -> "All systems online. Ready to execute complex multi-step tasks across apps and system controls."
            },
            fontSize = 16.sp,
            color = Color(0xFFE2E8F0),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                if (step < 3) step++ else onFinish()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (step < 3) "Next" else "Launch Cypher", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
