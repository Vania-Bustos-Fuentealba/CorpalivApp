package com.duocuc.corpalivapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duocuc.corpalivapp.navigation.AppNavigation
import com.duocuc.corpalivapp.ui.theme.CorpalivAppTheme

/**
 * Punto de entrada de la app. Toda la navegación incluida.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivAppTheme {
                AppNavigation()
            }
        }
    }
}
