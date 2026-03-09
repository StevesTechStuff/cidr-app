package com.stevestechstuff.cidr.ui.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stevestechstuff.cidr.ui.MainViewModel

@Composable
fun CheatSheetTab(viewModel: MainViewModel, onCidrSelected: () -> Unit) {
    val items = viewModel.cheatSheet
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Table Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "CIDR", modifier = Modifier.weight(0.15f), fontWeight = FontWeight.Bold)
            Text(text = "Mask", modifier = Modifier.weight(0.35f), fontWeight = FontWeight.Bold)
            Text(text = "IPs", modifier = Modifier.weight(0.25f), fontWeight = FontWeight.Bold)
            Text(text = "Hosts", modifier = Modifier.weight(0.25f), fontWeight = FontWeight.Bold)
        }
        
        Divider(color = MaterialTheme.colorScheme.outline)

        // Table Body
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.updateCalculatorInput("192.168.1.0${item.cidr}") // Example network
                            onCidrSelected()
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item.cidr, modifier = Modifier.weight(0.15f), style = MaterialTheme.typography.bodyMedium)
                    Text(text = item.subnetMask, modifier = Modifier.weight(0.35f), style = MaterialTheme.typography.bodyMedium)
                    Text(text = item.totalIps, modifier = Modifier.weight(0.25f), style = MaterialTheme.typography.bodyMedium)
                    Text(text = item.usableHosts, modifier = Modifier.weight(0.25f), style = MaterialTheme.typography.bodyMedium)
                }
                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            }
        }
    }
}
