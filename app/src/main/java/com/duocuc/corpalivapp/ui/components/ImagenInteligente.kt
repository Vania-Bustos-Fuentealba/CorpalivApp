package com.duocuc.corpalivapp.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * G13: COMPONENTE REUTILIZABLE de ImagenInteligente
 *
 * Muestra la imagen en forma circular. Si [uri] es null, muestra un ícono de perfil
 * por defecto. Funciona con Uri de galería y de cámara.
 */
@Composable
fun ImagenInteligente(
    uri: Uri?,
    modifier: Modifier = Modifier,
    tamano: Dp = 160.dp,
    descripcion: String = "Foto de perfil"
) {
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            AsyncImage(
                model = uri,
                contentDescription = descripcion,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Sin foto de perfil",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(tamano * 0.6f)
            )
        }
    }
}
