package com.stevestechstuff.cidr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.stevestechstuff.cidr.ui.CidrApp
import com.stevestechstuff.cidr.ui.MainViewModel
import com.stevestechstuff.cidr.ui.theme.CidrProTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CidrProTheme {
                CidrApp(viewModel = viewModel)
            }
        }
    }
}
