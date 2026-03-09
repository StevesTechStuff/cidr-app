package com.stevestechstuff.cidr.ui.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stevestechstuff.cidr.ui.CalculatorState
import com.stevestechstuff.cidr.ui.MainViewModel

@Composable
fun CalculatorTab(viewModel: MainViewModel) {
    val state by viewModel.calculatorState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.input,
            onValueChange = { viewModel.updateCalculatorInput(it) },
            label = { Text("Network / IP (e.g., 192.168.1.0/24)") },
            isError = !state.isValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (!state.isValid) {
            Text(
                text = "Invalid CIDR format",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        state.output?.let { output ->
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item { ResultRow("Network Address", output.networkAddress) }
                item { ResultRow("Subnet Mask", output.subnetMask) }
                item { ResultRow("First Usable IP", output.firstUsable) }
                item { ResultRow("Last Usable IP", output.lastUsable) }
                item { ResultRow("Broadcast Address", output.broadcastAddress) }
                item { ResultRow("Wildcard Mask", output.wildcardMask) }
                item { ResultRow("Total IPs", output.totalIps) }
                item { ResultRow("Usable Hosts", output.usableHosts) }
                
                if (output.isEdgeCase) {
                    item {
                        Text(
                            text = "Note: /31 and /32 are special edge cases (RFC 3021).",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                
                item { BinaryRepresentationSection(output.binaryIpAndMask) }
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    val clipboardManager = LocalClipboardManager.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontSize = 18.sp)
        }
        IconButton(onClick = { clipboardManager.setText(AnnotatedString(value)) }) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy $label", tint = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun BinaryRepresentationSection(binaryText: String) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Binary Representation", style = MaterialTheme.typography.titleMedium)
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Binary"
                )
            }
            
            AnimatedVisibility(visible = isExpanded) {
                Text(
                    text = binaryText,
                    style = MaterialTheme.typography.bodyMedium, // Monospace from theme
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
