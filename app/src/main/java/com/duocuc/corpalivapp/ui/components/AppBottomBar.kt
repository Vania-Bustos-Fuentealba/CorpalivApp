package com.duocuc.corpalivapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.duocuc.corpalivapp.navigation.Screen

/**
 * G10 - BARRA INFERIOR
 *
 * Componente reutilizable: cada pantalla indica cuál es su destino actual
 * ([selected]) y recibe el destino elegido por el usuario en [onNavigate].
 */
@Composable
fun AppBottomBar(
    selected: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar {
        destinosNavegacion.forEach { destino ->
            NavigationBarItem(
                selected = destino.screen == selected,
                onClick = { onNavigate(destino.screen) },
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                label = { Text(destino.etiqueta) }
            )
        }
    }
}
