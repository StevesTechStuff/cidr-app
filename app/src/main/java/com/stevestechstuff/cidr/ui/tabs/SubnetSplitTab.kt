package com.stevestechstuff.cidr.ui.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stevestechstuff.cidr.ui.MainViewModel

@Composable
fun SubnetSplitTab(viewModel: MainViewModel) {
    val subnets by viewModel.splitterState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (subnets.isNotEmpty()) {
            Text(
                text = "Adjacent Subnets:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = subnets, key = { it.network }) { subnet ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Network: ${subnet.network}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Range: ${subnet.firstUsable} - ${subnet.lastUsable}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Broadcast: ${subnet.broadcast}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Usable Hosts: ${subnet.usableHosts}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Enter a valid network in the Calculator tab to see a list of adjacent subnets.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
