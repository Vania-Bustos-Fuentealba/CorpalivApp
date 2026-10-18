package com.duocuc.corpalivapp.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duocuc.corpalivapp.navigation.Screen

/**
 * G10 - CONTENIDO DEL MENÚ LATERAL
 *
 * Se coloca dentro de un ModalDrawerSheet, No conoce el NavController: solo
 * avisa qué destino eligió el usuario mediante [onItemClick].
 *
 * GUÍA 13: la cabecera reutiliza [ImagenInteligente] para mostrar la foto de perfil.
 */
@Composable
fun AppDrawerContent(
    selected: Screen,
    onItemClick: (Screen) -> Unit,
    fotoPerfil: Uri? = null
) {
    Row(
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ImagenInteligente(uri = fotoPerfil, tamano = 56.dp)
        Text(
            text = "CORPALIV",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
    HorizontalDivider()
    Spacer(modifier = Modifier.height(12.dp))

    destinosNavegacion.forEach { destino ->
        ItemMenu(destino, destino.screen == selected, onItemClick)
    }

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider(modifier = Modifier.padding(horizontal = 28.dp))
    Text(
        text = "Herramientas",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp)
    )

    destinosFuncionales.forEach { destino ->
        ItemMenu(destino, destino.screen == selected, onItemClick)
    }
}

@Composable
private fun ItemMenu(
    destino: DestinoNavegacion,
    seleccionado: Boolean,
    onItemClick: (Screen) -> Unit
) {
    NavigationDrawerItem(
        label = { Text(destino.etiqueta) },
        icon = { Icon(destino.icono, contentDescription = null) },
        selected = seleccionado,
        onClick = { onItemClick(destino.screen) },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
