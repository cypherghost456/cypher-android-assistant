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
import java.io.BufferedReader
import java.io.InputStreamReader

class CypherTerminalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CypherTheme {
                TerminalScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen() {
    var commandInput by remember { mutableStateOf("") }
    val consoleOutput = remember { mutableStateListOf("Cypher Sandboxed Terminal [Version 1.0.0]", "Type safe shell commands below (e.g., 'uname -a', 'ls -la'):") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cypher Sandboxed Terminal", fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161B2E))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0A0E1A))
                .padding(12.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF161B2E))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(consoleOutput) { line ->
                    Text(
                        text = line,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = Color(0xFF00E5FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commandInput,
                    onValueChange = { commandInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter command…") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00E5FF),
                        unfocusedBorderColor = Color(0xFF334155)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val cmd = commandInput
                        if (cmd.isNotBlank()) {
                            consoleOutput.add("$ $cmd")
                            commandInput = ""
                            try {
                                val process = ProcessBuilder(cmd.split(" "))
                                    .redirectErrorStream(true)
                                    .start()
                                val reader = BufferedReader(InputStreamReader(process.inputStream))
                                val output = StringBuilder()
                                var line: String?
                                while (reader.readLine().also { line = it } != null) {
                                    output.append(line).append("\n")
                                }
                                process.waitFor()
                                val result = output.toString().trim()
                                consoleOutput.add(if (result.isNotBlank()) result else "Command executed with no output.")
                            } catch (e: Exception) {
                                consoleOutput.add("Error: ${e.message}")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                ) {
                    Text("Run", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
