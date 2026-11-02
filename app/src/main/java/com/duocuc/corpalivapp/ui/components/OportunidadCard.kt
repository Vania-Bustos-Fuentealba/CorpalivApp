package com.duocuc.corpalivapp.ui.components // Declaración del paquete al que pertenece este componente dentro de la capa de interfaz de usuario (UI)

import androidx.compose.foundation.layout.* // Importación genérica de modificadores de diseño (Box, Column, Row, Spacer, padding, fillMaxWidth, height, etc.)
import androidx.compose.material3.* // Importación genérica de componentes visuales de Material 3 (Card, Text, Button, Surface, MaterialTheme, etc.)
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de UI en Jetpack Compose
import androidx.compose.ui.Modifier // Modificador base para configurar la apariencia, diseño y comportamiento de composables
import androidx.compose.ui.layout.ContentScale // Define la escala de contenido para ajustar imágenes dentro de su contenedor (ej: Crop)
import androidx.compose.ui.text.font.FontWeight // Importación para modificar el peso o grosor visual de la fuente (Bold, SemiBold, etc.)
import androidx.compose.ui.unit.dp // Unidad de medida en densidad de píxeles (dp)
import coil.compose.AsyncImage // Componente de la librería Coil para la carga asíncrona de imágenes remotas mediante URL
import com.duocuc.corpalivapp.data.model.OportunidadModel // Importación del modelo de datos OportunidadModel que contiene la información a renderizar

/**
- G2 - COMPONENTE UI: OportunidadCard

- G10: al tocar la tarjeta o el botón se avisa con
[onConectar] para abrir el registro.
 */
@OptIn(ExperimentalMaterial3Api::class) // Anotación para consentir el uso de APIs experimentales requeridas por Card de Material 3
@Composable
fun OportunidadCard( // Declaración del composable reutilizable para renderizar una tarjeta de oportunidad o iniciativa de RSE
    oportunidad: OportunidadModel, // Modelo de datos con la información detallada de la oportunidad
    modifier: Modifier = Modifier, // Modificador personalizable para la tarjeta con valor por defecto neutro
    onConectar: () -> Unit = {} // Callback lambda que se ejecuta al presionar la tarjeta o el botón para iniciar la vinculación
) {
    Card( // Tarjeta contenedora de Material 3 con elevación y soporte de clic
        onClick = onConectar, // Asigna el callback onConectar al evento de toque sobre toda la superficie de la tarjeta
        modifier = modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible del contenedor padre
            .padding(vertical = 8.dp), // Aplica un margen vertical de 8dp entre tarjetas
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Define una sombra/elevación por defecto de 4dp
    ) {
        Column { // Contenedor vertical para apilar la imagen en la parte superior y el contenido descriptivo abajo
            // Carga dinámica de imagen con soporte seguro para URLs nulables en Coil
            AsyncImage( // Componente de Coil que descarga y muestra la imagen de la oportunidad
                model = oportunidad.imagenUrl, // URL remota proveniente del modelo de datos
                contentDescription = oportunidad.titulo, // Texto alternativo accesible utilizando el título de la oportunidad
                modifier = Modifier
                    .fillMaxWidth() // Expande la imagen a todo el ancho de la tarjeta
                    .height(180.dp), // Define una altura fija de 180dp para mantener la consistencia visual
                contentScale = ContentScale.Crop // Recorta y ajusta la imagen para llenar la superficie del contenedor sin deformarse
            )

            Column(modifier = Modifier.padding(16.dp)) { // Contenedor vertical interno para los textos y acciones con un margen interno de 16dp
                // Categoría de la iniciativa
                Surface( // Etiqueta o pill visual para destacar la categoría del proyecto
                    color = MaterialTheme.colorScheme.primaryContainer, // Asigna el color de contenedor primario del tema
                    shape = MaterialTheme.shapes.small // Aplica esquinas redondeadas de tamaño pequeño
                ) {
                    Text( // Texto de la categoría
                        text = oportunidad.categoria, // Muestra el nombre de la categoría provisto por el modelo
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), // Márgenes internos del texto dentro del Surface
                        style = MaterialTheme.typography.labelMedium, // Aplica el estilo de etiqueta mediana
                        color = MaterialTheme.colorScheme.onPrimaryContainer // Usa el color contrastante adecuado para el contenedor primario
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacio vertical de 8dp

                // Título del programa institucional
                Text( // Título principal de la oportunidad
                    text = oportunidad.titulo, // Muestra el título de la iniciativa
                    style = MaterialTheme.typography.titleMedium, // Aplica tipografía de título mediano
                    fontWeight = FontWeight.Bold // Resalta el título con texto en negrita
                )

                Spacer(modifier = Modifier.height(4.dp)) // Espacio vertical de 4dp

                // Descripción del proyecto
                Text( // Cuerpo del texto con la descripción detallada
                    text = oportunidad.descripcion, // Muestra el texto descriptivo del modelo
                    style = MaterialTheme.typography.bodyMedium // Utiliza la tipografía Body Medium
                )

                Spacer(modifier = Modifier.height(8.dp)) // Espacio vertical de 8dp

                // Impacto social directo en los beneficiarios de CORPALIV / Escuela Jean Van Dijk
                Text( // Información sobre el impacto comunitario
                    text = "Impacto: ${oportunidad.impactoSocial}", // Muestra la descripción del impacto mediante interpolación de cadenas
                    style = MaterialTheme.typography.bodySmall, // Aplica estilo de texto pequeño
                    color = MaterialTheme.colorScheme.secondary, // Utiliza el color secundario del esquema de colores del tema
                    fontWeight = FontWeight.SemiBold // Aplica un peso de fuente semi-negrita
                )

                Spacer(modifier = Modifier.height(8.dp)) // Espacio vertical de 8dp

                // G2 - CONTROL DE FLUJO:
                // Muestra la prioridad de vinculación (Ej: "Urgencia Alta", "Fase Final")
                // calculada mediante la expresión 'when' en el modelo de datos.
                // ---------------------------------------------------------------------
                Surface( // Etiqueta visual para representar el nivel de prioridad
                    color = MaterialTheme.colorScheme.tertiaryContainer, // Asigna el color de contenedor terciario del tema
                    shape = MaterialTheme.shapes.extraSmall // Aplica bordes levemente redondeados (extra pequeños)
                ) {
                    Text( // Texto del nivel de prioridad
                        text = oportunidad.obtenerPrioridadVinculacion(), // Evalúa la prioridad llamando al método del modelo
                        style = MaterialTheme.typography.labelSmall, // Aplica estilo de etiqueta pequeña
                        color = MaterialTheme.colorScheme.onTertiaryContainer, // Usa el color contrastante para contenedor terciario
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp) // Márgenes internos reducidos
                    )
                }

                Spacer(modifier = Modifier.height(6.dp)) // Espacio vertical de 6dp

                // G2 - OPERADORES ARITMÉTICOS Y VARIABLES NUMÉRICAS:
                Text( // Texto informativo con la meta económica y el porcentaje de avance
                    text = "Meta: $${oportunidad.metaFinanciamiento} | Avance: ${oportunidad.calcularPorcentajeAvance()}%", // Muestra la meta monetaria y el porcentaje calculado
                    style = MaterialTheme.typography.bodySmall, // Aplica estilo de texto pequeño
                    fontWeight = FontWeight.Bold // Utiliza formato en negrita
                )

                Text( // Texto con el cálculo del monto restante pendiente
                    text = "Faltante: $${oportunidad.calcularMontoFaltante()}", // Muestra la diferencia calculada por el método del modelo
                    style = MaterialTheme.typography.bodySmall, // Aplica estilo de texto pequeño
                    color = MaterialTheme.colorScheme.outline // Usa el color outline (grisáceo/neutro) para la cifra faltante
                )

                Spacer(modifier = Modifier.height(4.dp)) // Espacio vertical de 4dp

                // GUÍA 2 - NULL SAFETY:
                // Invoca obtenerContactoSeguro() para asegurar que 'contactoEmail'
                // viene nulo desde la API, Y se renderice un correo seguro sin crashear.
                Text( // Texto con el correo electrónico de contacto seguro
                    text = "Contacto: ${oportunidad.obtenerContactoSeguro()}", // Muestra el correo o un valor de respaldo sin lanzar excepciones por valores nulos
                    style = MaterialTheme.typography.bodySmall // Aplica estilo de texto pequeño
                )

                Spacer(modifier = Modifier.height(12.dp)) // Espacio vertical de 12dp

                // Botón de interacción para empresas o donantes
                Button( // Botón de acción principal dentro de la tarjeta
                    onClick = { /* Acción de contacto / vinculación */ onConectar() }, // Ejecuta el callback onConectar al hacer clic en el botón
                    modifier = Modifier.fillMaxWidth() // Expande el botón para que ocupe todo el ancho disponible
                ) {
                    Text("Conectar / Vincularse") // Etiqueta de texto del botón
                }
            }
        }
    }
}