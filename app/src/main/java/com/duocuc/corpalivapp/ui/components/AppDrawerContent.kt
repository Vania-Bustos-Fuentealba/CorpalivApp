package com.duocuc.corpalivapp.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duocuc.corpalivapp.navigation.Screen

/**
 * GUÍA 10 - CONTENIDO DEL MENÚ LATERAL (NavigationDrawer)
 *
 * Se coloca dentro de un ModalDrawerSheet (pantallas compactas/medianas) o de un
 * PermanentDrawerSheet (pantallas expandidas). No conoce el NavController: solo
 * avisa qué destino eligió el usuario mediante [onItemClick].
 */
@Composable
fun AppDrawerContent(
    selected: Screen,
    onItemClick: (Screen) -> Unit
) {
    Text(
        text = "CORPALIV",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(12.dp))

    destinosNavegacion.forEach { destino ->
        NavigationDrawerItem(
            label = { Text(destino.etiqueta) },
            icon = { Icon(destino.icono, contentDescription = null) },
            selected = destino.screen == selected,
            onClick = { onItemClick(destino.screen) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
