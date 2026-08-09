package com.cypher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class CypherUIActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CypherTheme {
                CypherSiriScreen(applicationContext)
            }
        }
    }
}

@Composable
fun CypherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF00E5FF),
            background = Color(0xFF050B14),
            surface = Color(0xFF0D1527),
            onSurface = Color(0xFFE2E8F0)
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CypherSiriScreen(context: android.content.Context) {
    var promptText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, String>>("Cypher" to "Cypher Siri/ClawCode OS online. Ready for complex device control and autonomous reasoning.") }
    val scope = rememberCoroutineScope()
    val agentLoop = remember { CypherAgentLoop(context) }
    val voiceEngine = remember { CypherVoiceEngine(context) { spoken -> promptText = spoken } }

    // Siri glowing orb animation pulse
    val infiniteTransition = rememberInfiniteTransition(label = "SiriOrbPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CYPHER — Siri & ClawCode Hybrid", fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1527))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF050B14))
        ) {
            // Siri Glowing Orb Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140px.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D1527), Color(0xFF050B14))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .scale(scale)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF00E5FF), Color(0xFF7C3AED), Color.Transparent)
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.first == "User"
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Surface(
                            color = if (isUser) Color(0xFF00E5FF) else Color(0xFF0D1527),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = msg.first,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUser) Color.Black else Color(0xFF00E5FF)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg.second,
                                    fontSize = 14.sp,
                                    color = if (isUser) Color.Black else Color.White
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D1527))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask Cypher (Siri + ClawCode mode)…") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00E5FF),
                        unfocusedBorderColor = Color(0xFF334155)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val task = promptText
                        if (task.isNotBlank()) {
                            messages.add("User" to task)
                            promptText = ""
                            scope.launch {
                                messages.add("Cypher" to "Thinking & executing via Siri/ClawCode engine…")
                                val response = agentLoop.executeAutonomousTask(task) { _ -> }
                                messages.removeAt(messages.size - 1)
                                messages.add("Cypher" to response)
                                voiceEngine.speak(response)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                ) {
                    Text("Ask", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
