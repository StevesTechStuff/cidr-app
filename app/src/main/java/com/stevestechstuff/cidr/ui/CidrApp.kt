package com.stevestechstuff.cidr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.stevestechstuff.cidr.ui.tabs.CalculatorTab
import com.stevestechstuff.cidr.ui.tabs.CheatSheetTab
import com.stevestechstuff.cidr.ui.tabs.SubnetSplitTab

@Composable
fun CidrApp(viewModel: MainViewModel) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Calculator", "Subnet Split", "Cheat Sheet")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            
            when (selectedTabIndex) {
                0 -> CalculatorTab(viewModel)
                1 -> SubnetSplitTab(viewModel)
                2 -> CheatSheetTab(viewModel = viewModel, onCidrSelected = { selectedTabIndex = 0 })
            }
        }
    }
}
