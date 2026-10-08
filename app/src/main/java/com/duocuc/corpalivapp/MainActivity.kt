package com.duocuc.corpalivapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.duocuc.corpalivapp.ui.screens.HomeScreenAdaptive
import com.duocuc.corpalivapp.ui.screens.OportunidadesScreen
import com.duocuc.corpalivapp.ui.theme.CorpalivAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivAppTheme {
                // Estado para conmutar entre la pantalla de inicio adaptativa (Guía 9)
                // y la lista de oportunidades (Guías 2, 4 y 5)
                var mostrarOportunidades by remember { mutableStateOf(false) }

                if (mostrarOportunidades) {
                    OportunidadesScreen()
                } else {
                    // GUÍA 9 - Interfaz adaptativa que se ajusta a Compact, Medium o Expanded
                    HomeScreenAdaptive(
                        onNavigateToOportunidades = {
                            mostrarOportunidades = true
                        }
                    )
                }
            }
        }
    }
}