package com.duocuc.corpalivapp.ui.theme // Declaración del paquete al que pertenece este archivo dentro de la capa de tema y diseño de la UI

import androidx.compose.material3.Typography // Importación de la clase Typography de Material 3 para definir la escala tipográfica
import androidx.compose.ui.text.TextStyle // Importación de TextStyle para configurar la apariencia individual de los textos (fuente, tamaño, peso, etc.)
import androidx.compose.ui.text.font.FontFamily // Importación de FontFamily para seleccionar la familia tipográfica (Default, SansSerif, Serif, etc.)
import androidx.compose.ui.text.font.FontWeight // Importación de FontWeight para establecer el grosor o peso del texto (Normal, Bold, Medium, etc.)
import androidx.compose.ui.unit.sp // Unidad de medida en píxeles escalables (sp) utilizada para fuentes de texto responsivas

val Typography = Typography( // Objeto global de tipo Typography que define los estilos de texto para la paleta de Material Design 3
    bodyLarge = TextStyle( // Configuración del estilo por defecto para el cuerpo de texto principal (bodyLarge)
        fontFamily = FontFamily.Default, // Utiliza la familia tipográfica predeterminada del sistema operativo Android
        fontWeight = FontWeight.Normal, // Aplica un peso de fuente regular/normal
        fontSize = 16.sp, // Define el tamaño de letra principal en 16 sp
        lineHeight = 24.sp, // Asigna una altura de línea de 24 sp para garantizar una buena legibilidad entre renglones
        letterSpacing = 0.5.sp // Define un espaciado entre caracteres ligero de 0.5 sp
    )
)