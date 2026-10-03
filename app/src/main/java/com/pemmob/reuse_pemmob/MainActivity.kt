package com.pemmob.reuse_pemmob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pemmob.reuse_pemmob.ui.navigation.MainAppNavigation
import com.pemmob.reuse_pemmob.ui.theme.ReUse_pemmobTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReUse_pemmobTheme {
                MainAppNavigation()
            }
        }
    }
}