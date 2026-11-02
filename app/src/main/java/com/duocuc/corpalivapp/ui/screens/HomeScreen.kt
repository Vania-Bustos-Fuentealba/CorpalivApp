package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.compose.foundation.Image // Importación del componente Image para renderizar recursos gráficos locales
import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución y espaciado entre elementos
import androidx.compose.foundation.layout.Column // Importación del contenedor vertical Column
import androidx.compose.foundation.layout.fillMaxSize // Modificador para que el contenedor ocupe todo el espacio disponible
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al 100%
import androidx.compose.foundation.layout.height // Modificador para definir una altura fija a un componente
import androidx.compose.foundation.layout.padding // Modificador para añadir márgenes o espacio de relleno interno
import androidx.compose.material3.Button // Componente Button de Material 3 para acciones de clic
import androidx.compose.material3.ExperimentalMaterial3Api // Importación para habilitar APIs experimentales de Material 3
import androidx.compose.material3.MaterialTheme // Acceso al esquema de colores, formas y tipografías del tema Material
import androidx.compose.material3.Scaffold // Estructura base de diseño visual que provee ranuras para barras superiores e inferiores
import androidx.compose.material3.Text // Componente Text de Material 3 para mostrar cadenas de texto
import androidx.compose.material3.TopAppBar // Componente de la barra de navegación superior de Material 3
import androidx.compose.material3.TopAppBarDefaults // Configuración de colores y propiedades predeterminadas para TopAppBar
import androidx.compose.runtime.Composable // Anotación Composable que marca la función como un componente dibujeable de Compose
import androidx.compose.ui.Alignment // Importación para definir la alineación de componentes en la pantalla
import androidx.compose.ui.Modifier // Modificador de UI base para configurar el aspecto y comportamiento de composables
import androidx.compose.ui.layout.ContentScale // Define cómo se escala o ajusta el contenido visual dentro de su contenedor (ej: Fit)
import androidx.compose.ui.res.painterResource // Carga un recurso gráfico o vectorial drawable desde la carpeta res de Android
import androidx.compose.ui.text.font.FontWeight // Define el peso o grosor visual de la tipografía (Bold, Normal, etc.)
import androidx.compose.ui.text.style.TextAlign // Importación para especificar la alineación horizontal del texto
import androidx.compose.ui.tooling.preview.Preview // Anotación para previsualizar el composable en Android Studio
import androidx.compose.ui.unit.dp // Unidad de medida en densidad de píxeles (dp)
import com.duocuc.corpalivapp.R // Acceso a la clase R de recursos del proyecto
import kotlin.OptIn // Anotación del lenguaje Kotlin para aceptar el uso de APIs experimentales

/**
- G8 - CONSTRUCCIÓN DE PANTALLA:

- Scaffold, organizando jerárquicamente además de TopAppBar, Column, Text, Button e Image con estilos de Material3 y Modifier.

 */
@OptIn(ExperimentalMaterial3Api::class) // Consiente el uso de APIs experimentales requeridas por TopAppBar
@Composable
fun HomeScreen( // Declaración del composable para la pantalla de inicio estándar
    onNavigateToOportunidades: () -> Unit = {} // Lambda de callback ejecutada al presionar el botón para ir a Oportunidades
) {
    // G8 - Provee la barra superior y gestiona los márgenes seguros
    Scaffold( // Estructura contenedor principal
        topBar = { // Define la barra superior de la pantalla
            TopAppBar(
                title = { // Contenido del título en la barra superior
                    Text(
                        text = "CORPALIV - Inclusión & RSE", // Título de la app
                        style = MaterialTheme.typography.titleLarge, // Estilo tipográfico para título grande
                        fontWeight = FontWeight.Bold // Grosor de fuente en negrita
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors( // Configuración del esquema de colores para la TopAppBar
                    containerColor = MaterialTheme.colorScheme.primaryContainer, // Fondo usando el color primaryContainer
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer // Texto con el color contrastante adecuado
                )
            )
        }
    ) { innerPadding -> // Recibe el relleno interno provisto por Scaffold para evitar superposiciones con la barra
        // G8 - MARGENES:
        // Se aplica Modifier.padding(innerPadding) para respetar el Scaffold y Arrangement.spacedBy(20.dp)
        Column( // Contenedor vertical principal
            modifier = Modifier
                .fillMaxSize() // Ocupa toda la superficie de la pantalla
                .padding(innerPadding) // Aplica el padding del Scaffold
                .padding(16.dp), // Aplica un margen general de 16dp
            verticalArrangement = Arrangement.spacedBy(20.dp), // Define una separación de 20dp entre elementos
            horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
        ) {
            // G8: Encabezado principal de bienvenida
            Text( // Mensaje de bienvenida
                text = "¡Bienvenido a CORPALIV!",
                style = MaterialTheme.typography.headlineMedium, // Estilo tipográfico de titular mediano
                fontWeight = FontWeight.Bold, // Texto en negrita
                color = MaterialTheme.colorScheme.primary, // Color primario del tema
                textAlign = TextAlign.Center // Texto centrado
            )

            // G8: Bajada de contexto institucional
            Text( // Texto de descripción o bajada
                text = "Plataforma de vinculación para Escuela Especial Jean Van Dijk y Taller Ocupacional Alba Lab.",
                style = MaterialTheme.typography.bodyLarge, // Estilo tipográfico de cuerpo grande
                color = MaterialTheme.colorScheme.onBackground, // Color de texto principal para el fondo actual
                textAlign = TextAlign.Center // Texto centrado
            )

            // G8 - ELEMENTO IMAGE: Carga del logotipo
            Image( // Componente gráfico para mostrar el logotipo
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Carga la imagen desde recursos
                contentDescription = "Logo Institucional CORPALIV", // Descripción accesible para lectores de pantalla
                modifier = Modifier
                    .fillMaxWidth() // Expande el gráfico al ancho total
                    .height(150.dp), // Asigna una altura fija de 150dp
                contentScale = ContentScale.Fit // Ajusta la imagen dentro de los límites manteniendo la relación de aspecto
            )

            // G8 - Acción principal de navegación e interacción
            Button( // Botón de navegación principal
                onClick = { onNavigateToOportunidades() }, // Invoca el callback para navegar hacia la pantalla de oportunidades
                modifier = Modifier.fillMaxWidth() // Expande el botón a todo el ancho
            ) {
                Text( // Texto del botón
                    text = "Ver Oportunidades y Proyectos RSE",
                    style = MaterialTheme.typography.labelLarge // Estilo de etiqueta grande
                )
            }
        }
    }
}

// G8: Permite previsualizar la interfaz.
@Preview(showBackground = true) // Habilita la vista previa en el IDE con fondo blanco por defecto
@Composable
fun HomeScreenPreview() { // Composable auxiliar exclusivo para renderizar la previsualización
    HomeScreen() // Invoca la pantalla HomeScreen sin argumentos
}