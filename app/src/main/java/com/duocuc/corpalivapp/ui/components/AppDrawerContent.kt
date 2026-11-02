package com.duocuc.corpalivapp.ui.components // Declaración del paquete al que pertenece este componente dentro de la capa de interfaz de usuario (UI)

import android.net.Uri // Importación de la clase Uri para manejar las referencias a imágenes locales o guardadas
import androidx.compose.foundation.layout.Arrangement // Importación de Arrangement para definir la distribución de elementos en composables
import androidx.compose.foundation.layout.Row // Importación de Row para organizar elementos de forma horizontal
import androidx.compose.foundation.layout.Spacer // Importación de Spacer para insertar espacios vacíos ajustables entre elementos
import androidx.compose.foundation.layout.height // Modificador height para asignar una altura específica
import androidx.compose.foundation.layout.padding // Modificador padding para agregar márgenes internos
import androidx.compose.material3.HorizontalDivider // Importación de HorizontalDivider de Material 3 para dibujar una línea divisora horizontal
import androidx.compose.material3.Icon // Importación de Icon para renderizar íconos vectoriales
import androidx.compose.material3.MaterialTheme // Importación del tema de diseño Material 3 para acceder a colores y tipografías
import androidx.compose.material3.NavigationDrawerItem // Importación del componente de ítem individual para el menú lateral
import androidx.compose.material3.NavigationDrawerItemDefaults // Importación con los valores por defecto y paddings para ítems del menú lateral
import androidx.compose.material3.Text // Importación del componente Text para visualizar cadenas de texto
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de UI en Jetpack Compose
import androidx.compose.ui.Alignment // Importación de Alignment para alinear elementos vertical u horizontalmente
import androidx.compose.ui.Modifier // Modificador de UI para modificar aspectos visuales y de diseño
import androidx.compose.ui.text.font.FontWeight // Importación para modificar el grosor o peso de la fuente (bold, normal, etc.)
import androidx.compose.ui.unit.dp // Unidad de medida de densidad de píxeles (dp)
import com.duocuc.corpalivapp.navigation.Screen // Importación de la clase sellada Screen que representa las rutas de navegación

/**
- G10 - CONTENIDO DEL MENÚ LATERAL

 *
- Se coloca dentro de un ModalDrawerSheet, No conoce el NavController: solo

- avisa qué destino eligió el usuario mediante [onItemClick].
 *
- GUÍA 13: la cabecera reutiliza [ImagenInteligente] para mostrar la foto de perfil.
 */
@Composable
fun AppDrawerContent( // Declaración del composable que renderiza el contenido dentro del panel de navegación lateral (Drawer)
    selected: Screen, // Pantalla o sección actualmente seleccionada para marcar el ítem activo
    onItemClick: (Screen) -> Unit, // Callback lambda que notifica qué destino seleccionó el usuario al hacer clic
    fotoPerfil: Uri? = null // Uri opcional con la ubicación de la foto de perfil del usuario (null por defecto)
) {
    Row( // Contenedor horizontal para la cabecera del menú lateral con la foto de perfil y el título
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp), // Aplica padding horizontal de 28dp y vertical de 24dp
        verticalAlignment = Alignment.CenterVertically, // Alinea los elementos verticalmente al centro
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Asigna una separación horizontal fija de 16dp entre la foto y el texto
    ) {
        ImagenInteligente(uri = fotoPerfil, tamano = 56.dp) // Componente personalizado para mostrar la foto de perfil redondeada o ícono por defecto
        Text( // Renderiza el título o marca institucional en la cabecera
            text = "CORPALIV", // Texto de la cabecera
            style = MaterialTheme.typography.titleLarge, // Utiliza la tipografía de título grande del tema Material
            fontWeight = FontWeight.Bold, // Aplica grosor de letra negrita (Bold)
            color = MaterialTheme.colorScheme.primary // Aplica el color primario configurado en el tema
        )
    }
    HorizontalDivider() // Dibuja una línea de división horizontal debajo de la cabecera
    Spacer(modifier = Modifier.height(12.dp)) // Añade un espacio vertical de 12dp

    destinosNavegacion.forEach { destino -> // Itera sobre la lista de destinos de navegación principal
        ItemMenu(destino, destino.screen == selected, onItemClick) // Renderiza cada ítem principal del menú llamando al composable ItemMenu
    }

    Spacer(modifier = Modifier.height(8.dp)) // Añade un espacio vertical de 8dp
    HorizontalDivider(modifier = Modifier.padding(horizontal = 28.dp)) // Dibuja un divisor horizontal con margen lateral
    Text( // Renderiza el título del segundo grupo de opciones
        text = "Herramientas", // Texto que encabeza las utilidades adicionales
        style = MaterialTheme.typography.labelLarge, // Aplica el estilo de etiqueta grande
        color = MaterialTheme.colorScheme.onSurfaceVariant, // Aplica un color de variante para textos secundarios
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp) // Aplica márgenes alrededor de la etiqueta
    )

    destinosFuncionales.forEach { destino -> // Itera sobre la lista de destinos secundarios/funcionales (Perfil, Dashboard, Modo Especial)
        ItemMenu(destino, destino.screen == selected, onItemClick) // Renderiza cada ítem funcional llamando al composable ItemMenu
    }
}

@Composable
private fun ItemMenu( // Composable privado auxiliar para encapsular la estructura repetitiva de un ítem del menú
    destino: DestinoNavegacion, // Objeto con la información del destino (icono, etiqueta y pantalla)
    seleccionado: Boolean, // Booleano que indica si el ítem coincide con la pantalla seleccionada
    onItemClick: (Screen) -> Unit // Callback lambda ejecutado al presionar sobre este ítem
) {
    NavigationDrawerItem( // Componente Material 3 especializado para representar un elemento de menú de navegación lateral
        label = { Text(destino.etiqueta) }, // Muestra la etiqueta textual del destino
        icon = { Icon(destino.icono, contentDescription = null) }, // Muestra el ícono representativo sin descripción adicional
        selected = seleccionado, // Aplica el estado visual seleccionado o no seleccionado
        onClick = { onItemClick(destino.screen) }, // Invoca el callback de selección pasando la pantalla vinculada
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding) // Aplica los márgenes recomendados por defecto para ítems del Drawer
    )
}