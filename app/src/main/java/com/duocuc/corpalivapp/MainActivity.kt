package com.duocuc.corpalivapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duocuc.corpalivapp.ui.screens.OportunidadesScreen
import com.duocuc.corpalivapp.ui.theme.CorpalivAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivAppTheme {
                OportunidadesScreen()
            }
        }
    }
}