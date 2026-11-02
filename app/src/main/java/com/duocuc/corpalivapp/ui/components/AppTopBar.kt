package com.duocuc.corpalivapp.ui.components // Declaración del paquete al que pertenece este componente dentro de la capa de interfaz de usuario (UI)

import androidx.compose.material.icons.Icons // Importación del contenedor principal de íconos de Material Design
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Importación del ícono flecha hacia atrás adaptado para direcciones de lectura (AutoMirrored)
import androidx.compose.material3.ExperimentalMaterial3Api // Importación de la anotación de API experimental de Material 3
import androidx.compose.material3.Icon // Importación del componente Icon para renderizar vectores gráficos
import androidx.compose.material3.IconButton // Importación del contenedor de botón especializado para albergar un ícono
import androidx.compose.material3.MaterialTheme // Importación de la clase del tema Material 3 para consultar colores y tipografías
import androidx.compose.material3.Text // Importación del componente Text para visualizar textos
import androidx.compose.material3.TopAppBar // Importación del componente TopAppBar de Material 3 para la barra superior
import androidx.compose.material3.TopAppBarDefaults // Importación de la configuración predeterminada para estilos y colores de TopAppBar
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de UI en Jetpack Compose

/**
- Barra superior reutilizable.

- La usan las pantallas de Registro, Resumen, Perfil y Modo especial.

 */
@OptIn(ExperimentalMaterial3Api::class) // Anotación que consiente el uso de APIs experimentales requeridas por TopAppBar en Material 3
@Composable
fun AppTopBar( // Declaración del composable reutilizable para la barra superior estándar con botón de retorno
    titulo: String, // Parámetro con el texto del título que se mostrará en el centro/izquierda de la barra
    onBack: () -> Unit // Función lambda de callback ejecutada al hacer clic en el botón de regresar
) {
    TopAppBar( // Renderiza el componente de la barra superior de la aplicación
        title = { Text(titulo) }, // Establece la ranura del título utilizando el composable Text con el título provisto
        navigationIcon = { // Define el ícono de navegación ubicado en el extremo izquierdo de la barra
            IconButton(onClick = onBack) { // Crea un botón de ícono que ejecuta el callback de retorno al presionarse
                Icon( // Dibuja el gráfico del ícono
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Utiliza el ícono estándar de flecha hacia atrás (ArrowBack)
                    contentDescription = "Volver" // Proporciona la descripción accesible para lectores de pantalla
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors( // Aplica el esquema de colores personalizado para la barra de navegación
            containerColor = MaterialTheme.colorScheme.primaryContainer // Define el color del fondo usando la variante primaryContainer del tema
        )
    )
}
