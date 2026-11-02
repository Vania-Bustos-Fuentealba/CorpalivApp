package com.duocuc.corpalivapp.ui.theme // Declaración del paquete al que pertenece este archivo dentro de la capa de tema y diseño de la UI

import android.app.Activity // Importación de la clase Activity de Android para interactuar con la ventana del sistema
import android.os.Build // Importación para verificar la versión del sistema operativo Android en tiempo de ejecución
import androidx.compose.foundation.isSystemInDarkTheme // Función de Compose para detectar si el sistema operativo tiene el modo oscuro activado
import androidx.compose.material3.MaterialTheme // Contenedor principal de Material Design 3 que aplica la paleta de colores, tipografía y formas
import androidx.compose.material3.darkColorScheme // Función creadora de un esquema de colores optimizado para modo oscuro
import androidx.compose.material3.dynamicDarkColorScheme // Función para generar una paleta de colores oscuros dinámicos basada en el fondo de pantalla (Android 12+)
import androidx.compose.material3.dynamicLightColorScheme // Función para generar una paleta de colores claros dinámicos basada en el fondo de pantalla (Android 12+)
import androidx.compose.material3.lightColorScheme // Función creadora de un esquema de colores optimizado para modo claro
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de construcción de UI en Jetpack Compose
import androidx.compose.ui.platform.LocalContext // Proporciona el contexto actual de la aplicación Android en Compose

private val DarkColorScheme = darkColorScheme( // Declaración del esquema de colores predeterminado para el tema oscuro
    primary = Purple80, // Asigna el color púrpura claro como color primario del tema oscuro
    secondary = PurpleGrey80, // Asigna el tono púrpura grisáceo claro como color secundario
    tertiary = Pink80 // Asigna el tono rosa claro como color terciario/acento
)

private val LightColorScheme = lightColorScheme( // Declaración del esquema de colores predeterminado para el tema claro
    primary = Purple40, // Asigna el color púrpura oscuro como color primario del tema claro
    secondary = PurpleGrey40, // Asigna el tono púrpura grisáceo medio como color secundario
    tertiary = Pink40 // Asigna el tono rosa oscuro como color terciario/acento

)

@Composable
fun CorpalivAppTheme( // Composable contenedor del tema global de la aplicación CorpalivApp
    darkTheme: Boolean = isSystemInDarkTheme(), // Parámetro para forzar o detectar automáticamente el modo oscuro del sistema
    dynamicColor: Boolean = true, // Parámetro para habilitar o deshabilitar colores dinámicos (Material You)
    content: @Composable () -> Unit // Lambda con el contenido visual que heredará la configuración del tema
) {
    val colorScheme = when { // Expresión when para determinar el esquema de colores a aplicar según el entorno y versión de Android
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> { // Verifica si el color dinámico está activo y si la API es Android 12 (S) o superior
            val context = LocalContext.current // Obtiene la referencia del contexto actual de Android
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context) // Aplica la paleta dinámica de colores en modo oscuro o claro según corresponda
        }

        darkTheme -> DarkColorScheme // Si no usa colores dinámicos pero el modo oscuro está activo, aplica DarkColorScheme
        else -> LightColorScheme // En cualquier otro caso, aplica el esquema de colores claros (LightColorScheme)
    }

    MaterialTheme( // Proveedor del tema Material 3 para todos los elementos hijos en la jerarquía
        colorScheme = colorScheme, // Asigna el esquema de colores calculado previamente
        typography = Typography, // Asigna la configuración de estilos de tipografía global
        content = content // Renderiza el contenido envolviéndolo con las especificaciones del tema
    )
}