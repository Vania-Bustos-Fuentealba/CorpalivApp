package com.duocuc.corpalivapp.ui.components // Declaración del paquete al que pertenece este componente dentro de la capa de interfaz de usuario (UI)

import androidx.compose.material3.Icon // Importación del componente Icon de Material 3 para desplegar íconos vectoriales
import androidx.compose.material3.NavigationBar // Importación del contenedor NavigationBar de Material 3 para la barra de navegación inferior
import androidx.compose.material3.NavigationBarItem // Importación del elemento de ítem individual para la NavigationBar
import androidx.compose.material3.Text // Importación del componente Text de Material 3 para mostrar etiquetas de texto
import androidx.compose.runtime.Composable // Anotación que identifica la función como un componente dibujeable en Jetpack Compose
import com.duocuc.corpalivapp.navigation.Screen // Importación de la clase sellada Screen que representa las rutas de navegación

/**
- G10 - BARRA INFERIOR

 *
- Componente reutilizable: cada pantalla indica cuál es su destino actual

- ([selected]) y recibe el destino elegido por el usuario en [onNavigate].
- Ahora AppNavigation la muestra en todas las pantallas;
[selected] es null si la pantalla no pertenece a ninguna sección.
 */
@Composable
fun AppBottomBar( // Declaración del composable reutilizable para la barra de navegación inferior de la aplicación
    selected: Screen?, // Parámetro que indica la pantalla o sección activa seleccionada (puede ser null si la ruta actual no es de la barra)
    onNavigate: (Screen) -> Unit // Función callback lambda ejecutada cuando el usuario pulsa sobre un ítem de la barra
) {
    NavigationBar { // Contenedor principal de Material 3 que renderiza la barra inferior de navegación
        destinosNavegacion.forEach { destino -> // Itera sobre la lista predefinida de destinos de navegación de la barra
            NavigationBarItem( // Renderiza el ítem individual correspondiente a cada destino iterado
                selected = destino.screen == selected, // Evalúa si la pantalla actual coincide con la del ítem para aplicar el estilo activo
                onClick = { onNavigate(destino.screen) }, // Asigna la acción de clic invocando a la lambda con la pantalla destino seleccionada
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) }, // Renderiza el ícono del ítem especificando su etiqueta como descripción
                label = { Text(destino.etiqueta) } // Renderiza el texto con la etiqueta legíble del ítem debajo del ícono
            )
        }
    }
}
