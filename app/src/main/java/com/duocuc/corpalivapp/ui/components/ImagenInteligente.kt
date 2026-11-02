package com.duocuc.corpalivapp.ui.components // Declaración del paquete al que pertenece este componente dentro de la capa de interfaz de usuario (UI)

import android.net.Uri // Importación de la clase Uri para referenciar recursos de imágenes locales o del sistema
import androidx.compose.foundation.background // Modificador para aplicar un color o patrón de fondo
import androidx.compose.foundation.border // Modificador para aplicar un borde con grosor, color y forma
import androidx.compose.foundation.layout.Box // Componente contenedor que apila sus elementos hijos unos sobre otros
import androidx.compose.foundation.layout.fillMaxSize // Modificador para que el elemento ocupe todo el espacio de su contenedor padre
import androidx.compose.foundation.layout.size // Modificador para definir dimensiones fijas de ancho y alto
import androidx.compose.foundation.shape.CircleShape // Forma predefinida en Compose para recortar o aplicar bordes circulares
import androidx.compose.material.icons.Icons // Importación del contenedor principal de íconos de Material
import androidx.compose.material.icons.filled.Person // Importación del ícono de persona por defecto cuando no hay foto disponible
import androidx.compose.material3.Icon // Componente Icon de Material 3 para desplegar íconos vectoriales
import androidx.compose.material3.MaterialTheme // Acceso al tema de la aplicación para consultar colores y estilos
import androidx.compose.runtime.Composable // Anotación Composable que identifica la función como UI en Jetpack Compose
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores de Compose
import androidx.compose.ui.Modifier // Modificador base para configurar apariencia, diseño y comportamiento de composables
import androidx.compose.ui.draw.clip // Modificador para recortar la vista usando una forma específica (CircleShape)
import androidx.compose.ui.layout.ContentScale // Define cómo se escala el contenido visual dentro de su contenedor (ej: Crop)
import androidx.compose.ui.unit.Dp // Clase para representar unidades de medida en densidad de píxeles
import androidx.compose.ui.unit.dp // Extensión para definir valores en densidad de píxeles (dp)
import coil.compose.AsyncImage // Componente de la librería Coil para cargar e integrar imágenes de manera asíncrona

/**
- G13: COMPONENTE REUTILIZABLE de ImagenInteligente

 *
- Muestra la imagen en forma circular. Si [uri] es null, muestra un ícono de perfil

- por defecto. Funciona con Uri de galería y de cámara.

 */
@Composable
fun ImagenInteligente( // Declaración del composable que renderiza una foto circular o un ícono por defecto
    uri: Uri?, // Dirección Uri de la imagen a cargar (cámara/galería); null si no hay imagen elegida
    modifier: Modifier = Modifier, // Modificador personalizable para el componente con valor por defecto neutro
    tamano: Dp = 160.dp, // Tamaño por defecto para el diámetro de la imagen circular (160dp)
    descripcion: String = "Foto de perfil" // Texto de accesibilidad para describir la imagen
) {
    Box( // Contenedor raíz que aplica la forma circular, bordes y fondo
        modifier = modifier
            .size(tamano) // Aplica el tamaño definido al contenedor
            .clip(CircleShape) // Recorta la superficie del Box para que sea completamente circular
            .background(MaterialTheme.colorScheme.surfaceVariant) // Asigna un color de fondo neutro de variante
            .border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape), // Agrega un borde primario de 2dp
        contentAlignment = Alignment.Center // Alinea el contenido (imagen o ícono) exactamente al centro del Box
    ) {
        if (uri != null) { // Evalúa si se recibió una Uri válida con una imagen
            AsyncImage( // Renderiza la imagen desde la Uri de forma asíncrona mediante Coil
                model = uri, // Fuente de la imagen a procesar
                contentDescription = descripcion, // Descripción accesible para lectores de pantalla
                contentScale = ContentScale.Crop, // Escala y recorta la imagen para llenar el contenedor sin deformarse
                modifier = Modifier.fillMaxSize() // Expande la imagen para cubrir todo el espacio del Box
            )
        } else {
            Icon( // Renderiza un ícono por defecto si la Uri es nula
                imageVector = Icons.Default.Person, // Ícono vectorial de silueta de persona
                contentDescription = "Sin foto de perfil", // Texto descriptivo para accesibilidad cuando no hay foto
                tint = MaterialTheme.colorScheme.onSurfaceVariant, // Aplica un tono de color contrastante para variantes
                modifier = Modifier.size(tamano * 0.6f) // Ajusta proporcionalmente el tamaño del ícono al 60% del contenedor
            )
        }
    }
}
