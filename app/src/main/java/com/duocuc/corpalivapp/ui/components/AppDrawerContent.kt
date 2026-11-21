
package com.duocuc.corpalivapp.ui.components // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import android.net.Uri // Importa Uri: representa la ubicación de la foto elegida por el usuario.
import androidx.compose.foundation.layout.Arrangement // Importa Arrangement: define la separación y distribución de elementos en filas o columnas.
import androidx.compose.foundation.layout.Row // Importa Row: coloca elementos de la interfaz en una fila horizontal.
import androidx.compose.foundation.layout.Spacer // Importa Spacer: reserva un espacio vacío entre componentes.
import androidx.compose.foundation.layout.height // Importa height: establece la altura de un componente.
import androidx.compose.foundation.layout.padding // Importa padding: agrega espacio alrededor del contenido.
import androidx.compose.material3.HorizontalDivider // Importa HorizontalDivider: dibuja una línea horizontal para separar secciones.
import androidx.compose.material3.Icon // Importa Icon: dibuja un icono dentro de la interfaz.
import androidx.compose.material3.MaterialTheme // Importa MaterialTheme: proporciona los colores y las tipografías del tema.
import androidx.compose.material3.NavigationDrawerItem // Importa NavigationDrawerItem: representa una opción seleccionable del menú lateral.
import androidx.compose.material3.NavigationDrawerItemDefaults // Importa NavigationDrawerItemDefaults: proporciona los márgenes estándar de las opciones del menú.
import androidx.compose.material3.Text // Importa Text: muestra texto en una pantalla Compose.
import androidx.compose.runtime.Composable // Importa Composable: marca una función que describe interfaz con Jetpack Compose.
import androidx.compose.ui.Alignment // Importa Alignment: define cómo alinear los elementos dentro de un contenedor.
import androidx.compose.ui.Modifier // Importa Modifier: encadena cambios de tamaño, separación y aspecto de la interfaz.
import androidx.compose.ui.text.font.FontWeight // Importa FontWeight: permite seleccionar el grosor de la tipografía.
import androidx.compose.ui.unit.dp // Importa dp: expresa tamaños en unidades independientes de la densidad de pantalla.
import com.duocuc.corpalivapp.navigation.Screen // Importa Screen: identifica los destinos de navegación de la aplicación.

/**
 * G10 - CONTENIDO DEL MENÚ LATERAL
 *
 * Se utiliza dentro de ModalDrawerSheet.
 * La navegación se comunica mediante onItemClick.
 *
 * GUÍA 13: la cabecera muestra la foto de perfil.
 */
@Composable // Indica que esta función describe parte de la interfaz Compose.
fun AppDrawerContent( // Define el contenido del menú lateral compartido por la aplicación.
    selected: Screen, // Recibe la pantalla actual para resaltar su opción del menú.
    onItemClick: (Screen) -> Unit, // Recibe la acción que comunica a la navegación el destino elegido.
    fotoPerfil: Uri? = null // Permite mostrar una foto si existe, o el recurso alternativo si es null.
) {

    // Cabecera del menú
    Row( // Coloca la foto y el nombre de Corpaliv en una misma fila.
        modifier = Modifier.padding( // Configura el espacio alrededor del componente.
            horizontal = 28.dp, // Deja margen a izquierda y derecha del contenido.
            vertical = 24.dp // Deja espacio arriba y abajo de la cabecera.
        ),
        verticalAlignment = Alignment.CenterVertically, // Centra verticalmente la foto y el texto de la cabecera.
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Separa la foto y el texto por 16 dp.
    ) {
        ImagenInteligente( // Reutiliza el componente que carga fotos y muestra una imagen alternativa.
            uri = fotoPerfil, // Entrega al componente la ubicación de la foto seleccionada.
            tamano = 56.dp // Define el tamaño de la imagen de la cabecera.
        )

        Text( // Muestra un bloque de texto con los parámetros indicados.
            text = "CORPALIV", // Muestra el nombre de la aplicación en el menú.
            style = MaterialTheme.typography.titleLarge, // Utiliza el estilo de título grande del tema.
            fontWeight = FontWeight.Bold, // Destaca el nombre de la aplicación con negrita.
            color = MaterialTheme.colorScheme.primary // Utiliza el color principal de la identidad visual.
        )
    }

    HorizontalDivider() // Separa la cabecera de las opciones de navegación.

    Spacer(modifier = Modifier.height(12.dp)) // Separa verticalmente las secciones por 12 dp.

    // Opciones principales de navegación
    destinosNavegacion.forEach { destino -> // Recorre los destinos principales definidos para el menú.
        ItemMenu( // Dibuja una opción usando el componente reutilizable del menú.
            destino = destino, // Entrega el icono, la etiqueta y la pantalla de esta opción.
            seleccionado = destino.screen == selected, // Resalta la opción si corresponde a la pantalla actualmente abierta.
            onItemClick = onItemClick // Propaga el callback de navegación recibido por el menú.
        )
    }

    Spacer(modifier = Modifier.height(8.dp)) // Añade 8 dp de separación vertical.

    HorizontalDivider( // Agrega una línea divisoria antes de la sección de herramientas.
        modifier = Modifier.padding(horizontal = 28.dp) // Deja márgenes laterales en el divisor.
    )

    // Sección Herramientas
    Text( // Muestra un bloque de texto con los parámetros indicados.
        text = "Herramientas", // Muestra el encabezado de las opciones funcionales.
        style = MaterialTheme.typography.labelLarge, // Aplica el estilo de etiqueta grande del tema.
        color = MaterialTheme.colorScheme.onSurfaceVariant, // Usa el color secundario de texto para el encabezado.
        modifier = Modifier.padding( // Configura el espacio alrededor del componente.
            horizontal = 28.dp, // Deja margen a izquierda y derecha del contenido.
            vertical = 12.dp // Separa el encabezado de los componentes superior e inferior.
        )
    )

    // Opciones funcionales existentes
    destinosFuncionales.forEach { destino -> // Recorre las opciones funcionales, como perfil y otras herramientas.
        ItemMenu( // Dibuja una opción usando el componente reutilizable del menú.
            destino = destino, // Entrega el icono, la etiqueta y la pantalla de esta opción.
            seleccionado = destino.screen == selected, // Resalta la opción si corresponde a la pantalla actualmente abierta.
            onItemClick = onItemClick // Propaga el callback de navegación recibido por el menú.
        )
    }

    // NUEVA OPCIÓN: API EXTERNA
    NavigationDrawerItem( // Crea una opción que puede seleccionarse dentro del menú lateral.
        label = { // Define el contenido de texto de la opción.
            Text("API externa") // Muestra la etiqueta que abre las publicaciones de JSONPlaceholder.
        },
        selected = selected == Screen.ApiExterna, // Resalta esta opción cuando la pantalla externa está abierta.
        onClick = { // Define la acción que se ejecuta al pulsar la opción.
            onItemClick(Screen.ApiExterna) // Solicita navegar a la pantalla de la API externa.
        },
        modifier = Modifier.padding( // Configura el espacio alrededor del componente.
            NavigationDrawerItemDefaults.ItemPadding // Aplica los márgenes estándar de Material para una opción de menú.
        )
    )
}

/**
 * Componente reutilizable para los elementos
 * existentes del menú lateral.
 */
@Composable // Indica que esta función describe parte de la interfaz Compose.
private fun ItemMenu( // Extrae el diseño común para no repetirlo en cada destino del menú.
    destino: DestinoNavegacion, // Recibe los datos visuales y la pantalla de destino.
    seleccionado: Boolean, // Indica si esta opción corresponde a la pantalla activa.
    onItemClick: (Screen) -> Unit // Recibe el callback que abrirá el destino seleccionado.
) {
    NavigationDrawerItem( // Crea una opción que puede seleccionarse dentro del menú lateral.
        label = { // Define el contenido de texto de la opción.
            Text(destino.etiqueta) // Muestra el nombre legible del destino.
        },
        icon = { // Define el icono que acompaña a la etiqueta.
            Icon( // Dibuja el vector gráfico configurado para este destino.
                imageVector = destino.icono, // Selecciona el icono asociado al destino.
                contentDescription = null // Evita repetir en accesibilidad la etiqueta que ya describe la opción.
            )
        },
        selected = seleccionado, // Aplica visualmente el estado de selección recibido.
        onClick = { // Define la acción que se ejecuta al pulsar la opción.
            onItemClick(destino.screen) // Comunica a la navegación la pantalla asociada a esta opción.
        },
        modifier = Modifier.padding( // Configura el espacio alrededor del componente.
            NavigationDrawerItemDefaults.ItemPadding // Aplica los márgenes estándar de Material para una opción de menú.
        )
    )
}
