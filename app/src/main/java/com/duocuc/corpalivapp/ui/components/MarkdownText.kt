package com.duocuc.corpalivapp.ui.components // Declaración del paquete al que pertenece este archivo dentro de los componentes de UI de la app

import androidx.compose.foundation.layout.Arrangement // Importación para definir la distribución y espaciado en contenedores
import androidx.compose.foundation.layout.Column // Importación del contenedor vertical Column
import androidx.compose.foundation.layout.Row // Importación del contenedor horizontal Row
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.padding // Modificador para agregar márgenes internos/externos
import androidx.compose.foundation.layout.width // Modificador para establecer un ancho específico
import androidx.compose.material3.MaterialTheme // Acceso al tema Material 3 para tipografía y esquema de colores
import androidx.compose.material3.OutlinedCard // Componente de tarjeta con borde definido de Material 3
import androidx.compose.material3.Text // Componente Text de Material 3 para despliegue de cadenas
import androidx.compose.runtime.Composable // Anotación Composable para funciones de UI en Jetpack Compose
import androidx.compose.runtime.remember // Función para recordar valores calculados a través de recomposiciones
import androidx.compose.ui.Modifier // Modificador de UI base de Jetpack Compose
import androidx.compose.ui.semantics.heading // Propiedad semántica para identificar encabezados en lectores de pantalla
import androidx.compose.ui.semantics.semantics // Modificador para asignar propiedades de accesibilidad semántica
import androidx.compose.ui.text.AnnotatedString // Clase para representar cadenas de texto enriquecidas con estilos aplicados
import androidx.compose.ui.text.SpanStyle // Clase para aplicar estilos de texto a rangos específicos dentro de un AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString // DSL para construir un AnnotatedString de forma imperativa
import androidx.compose.ui.text.font.FontWeight // Importación para modificar el peso visual de las fuentes (negrita, regular, etc.)
import androidx.compose.ui.text.withStyle // Función helper para aplicar un estilo a un bloque de texto durante su construcción
import androidx.compose.ui.unit.dp // Unidad de densidad de píxeles (dp)

/**
- Bloques que entiende el lector: títulos (#), párrafos y viñetas (- o *).

 */
sealed class BloqueMarkdown { // Clase sellada base para representar los nodos/bloques parseados de Markdown
    data class Titulo(val nivel: Int, val texto: String) : BloqueMarkdown() // Nodo para títulos de nivel 1 a 6 con su contenido
    data class Parrafo(val texto: String) : BloqueMarkdown() // Nodo para bloques de párrafos de texto continuo
    data class Vineta(val nivel: Int, val texto: String) : BloqueMarkdown() // Nodo para ítems de lista o viñetas con nivel de sangría
}

// Enlaces [texto](url): se muestra solo el texto
private val REGEX_ENLACE = Regex("\\[([^\\]]+)]\\([^)]*\\)") // Expresión regular para extraer el texto visible de enlaces Markdown

/**
- Convierte el texto Markdown en bloques. Las imágenes y las líneas vacías no generan bloques;

- las líneas seguidas de un mismo párrafo se unen con un espacio.

 */
internal fun parsearMarkdown(markdown: String): List<BloqueMarkdown> { // Función utilitaria interna para analizar texto Markdown y transformarlo en lista de BloqueMarkdown
    val bloques = mutableListOf<BloqueMarkdown>() // Lista acumuladora de bloques reconocidos
    val parrafo = StringBuilder() // Buffer de texto para construir párrafos multilínea

    // Cierra el párrafo en curso (si hay) y lo agrega a la lista
    fun cerrarParrafo() { // Función interna para empaquetar el párrafo actual si no está vacío
        if (parrafo.isNotBlank()) bloques.add(BloqueMarkdown.Parrafo(parrafo.toString().trim())) // Añade el párrafo limpio a la lista de bloques
        parrafo.clear() // Vacía el acumulador del párrafo
    }

    for (lineaCruda in markdown.lines()) { // Itera sobre cada línea del texto Markdown recibido
        val linea = lineaCruda.trimEnd() // Elimina espacios en blanco al final de la línea
        val sinSangria = linea.trimStart() // Elimina espacios en blanco al inicio de la línea

        when {
            sinSangria.isEmpty() -> cerrarParrafo() // Si la línea está vacía, finaliza el párrafo en curso

            // Imágenes: no se muestran
            sinSangria.startsWith("![") -> cerrarParrafo() // Descarta la sintaxis de imágenes finalizando cualquier párrafo abierto

            sinSangria.startsWith("#") -> { // Detecta encabezados o títulos que inician con el carácter #
                cerrarParrafo() // Cierra el párrafo en curso
                val nivel = sinSangria.takeWhile { it == '#' }.length // Cuenta la cantidad de caracteres # para determinar el nivel del título
                bloques.add(BloqueMarkdown.Titulo(nivel, sinSangria.drop(nivel).trim())) // Agrega el objeto Titulo con su nivel y texto limpio
            }

            sinSangria.startsWith("- ") || sinSangria.startsWith("* ") -> { // Detecta elementos de lista o viñeta
                cerrarParrafo() // Cierra el párrafo previo
                val nivel = (linea.length - sinSangria.length) / 2 // Calcula el nivel de anidamiento/sangría según espacios iniciales
                bloques.add(BloqueMarkdown.Vineta(nivel, sinSangria.drop(2).trim())) // Agrega el objeto Vineta descartando el prefijo "- " o "* "
            }

            else -> { // Si es texto normal de párrafo
                if (parrafo.isNotEmpty()) parrafo.append(' ') // Inserta un espacio de separación si el buffer ya contenía texto previo
                parrafo.append(sinSangria) // Acumula la línea actual dentro del párrafo
            }
        }
    }
    cerrarParrafo() // Procesa y cierra cualquier párrafo pendiente tras completar el bucle
    return bloques // Retorna la lista resultante de bloques procesados
}

/**
- Agrupa los bloques en secciones: la primera llega hasta el primer título de nivel 2

- y cada título de nivel 2 abre una sección nueva.

 */
internal fun agruparEnSecciones(bloques: List<BloqueMarkdown>): List<List<BloqueMarkdown>> { // Función que divide los bloques en secciones basadas en títulos H2
    val secciones = mutableListOf<List<BloqueMarkdown>>() // Lista contenedora de listas de bloques (secciones)
    var actual = mutableListOf<BloqueMarkdown>() // Acumulador para los bloques de la sección activa

    for (bloque in bloques) { // Recorre la secuencia total de bloques
        if (bloque is BloqueMarkdown.Titulo && bloque.nivel == 2 && actual.isNotEmpty()) { // Si encuentra un H2 y la sección actual no está vacía
            secciones.add(actual) // Guarda la sección previamente acumulada
            actual = mutableListOf() // Inicia una nueva lista para la siguiente sección
        }
        actual.add(bloque) // Agrega el bloque actual a la sección activa
    }
    if (actual.isNotEmpty()) secciones.add(actual) // Agrega la última sección si contiene elementos

    return secciones // Devuelve las secciones divididas
}

// Quita los enlaces y aplica negrita a lo que va entre ** **
private fun textoEnriquecido(texto: String): AnnotatedString { // Convierte formato básico de Markdown (**negrita** y enlaces) a AnnotatedString
    val limpio = REGEX_ENLACE.replace(texto) { it.groupValues[1] } // Remueve las URLs dejando solo el texto del enlace
    return buildAnnotatedString { // Construye la cadena con estilos aplicados
        var negrita = false // Flag para conmutar la aplicación del estilo de negrita
        for (parte in limpio.split("**")) { // Divide la cadena utilizando la marca de negrita ** como delimitador
            if (negrita) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(parte) } // Aplica fuente en negrita a las secciones intercaladas
            } else {
                append(parte) // Añade el texto con estilo normal
            }
            negrita = !negrita // Invierte el estado para el siguiente segmento
        }
    }
}

/**
- Muestra un texto en Markdown con los estilos de MaterialTheme.

- Soporta títulos, párrafos, viñetas (con sangría) y negrita.

 */
@Composable
fun MarkdownText( // Composable público para renderizar texto procesado desde sintaxis Markdown
    markdown: String, // Cadena de texto en formato Markdown a renderizar
    modifier: Modifier = Modifier // Modificador de estilo para el contenedor
) {
    val bloques = remember(markdown) { parsearMarkdown(markdown) } // Memoriza el parseo del texto para evitar reprocesarlo en cada recomposición
    BloquesMarkdown(bloques = bloques, modifier = modifier) // Invoca la función de dibujo pasando los bloques generados
}

/**
- Muestra el Markdown separado en bloques: la introducción va sin marco y cada sección

- (título de nivel 2) va dentro de su propia tarjeta con borde, con un estilo sobrio.

 */
@Composable
fun MarkdownEnBloques( // Composable para presentar el Markdown agrupado en secciones dentro de tarjetas visuales
    markdown: String, // Cadena de texto en formato Markdown a procesar
    modifier: Modifier = Modifier // Modificador de estilo aplicable al contenedor externo
) {
    val secciones = remember(markdown) { agruparEnSecciones(parsearMarkdown(markdown)) } // Analiza y agrupa el Markdown en secciones memorizadas

    Column( // Contenedor vertical principal para las tarjetas o bloques de introducción
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp) // Asigna una separación vertical de 12dp entre cada sección
    ) {
        secciones.forEach { seccion -> // Recorre cada una de las secciones obtenidas
            val primero = seccion.first() // Evalúa el primer bloque de la sección
            val esIntroduccion = !(primero is BloqueMarkdown.Titulo && primero.nivel == 2) // Determina si la sección es una introducción preliminar

            if (esIntroduccion) {
                BloquesMarkdown(bloques = seccion) // Dibuja la introducción directamente sin envoltorio de tarjeta
            } else {
                OutlinedCard(modifier = Modifier.fillMaxWidth()) { // Envuelve las secciones con título H2 en una tarjeta con borde (OutlinedCard)
                    BloquesMarkdown(
                        bloques = seccion,
                        modifier = Modifier.padding(16.dp) // Aplica padding de 16dp al contenido interno de la tarjeta
                    )
                }
            }
        }
    }
}

// Dibuja cada bloque con los estilos de MaterialTheme
@Composable
private fun BloquesMarkdown( // Composable privado que transforma la lista de nodos BloqueMarkdown en componentes de UI
    bloques: List<BloqueMarkdown>, // Lista de bloques a renderizar
    modifier: Modifier = Modifier // Modificador de estilo
) {
    Column( // Agrupa los bloques verticalmente
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp) // Aplica un espacio de 8dp entre bloque y bloque
    ) {
        bloques.forEach { bloque -> // Itera sobre la colección de bloques
            when (bloque) {
                is BloqueMarkdown.Titulo -> Text( // Renderiza el componente de texto si el bloque es un Título
                    text = textoEnriquecido(bloque.texto), // Asigna el texto con formato rico
                    style = when (bloque.nivel) { // Selecciona la jerarquía tipográfica según el nivel del título
                        1 -> MaterialTheme.typography.headlineMedium // Nivel 1 usa Headline Medium
                        2 -> MaterialTheme.typography.titleLarge // Nivel 2 usa Title Large
                        3 -> MaterialTheme.typography.titleMedium // Nivel 3 usa Title Medium
                        else -> MaterialTheme.typography.titleSmall // Niveles inferiores usan Title Small
                    },
                    fontWeight = FontWeight.Bold, // Aplica grosor de negrita
                    color = if (bloque.nivel <= 2) {
                        MaterialTheme.colorScheme.primary // Aplica color primario para títulos H1 y H2
                    } else {
                        MaterialTheme.colorScheme.onSurface // Aplica el color de texto por defecto de la superficie para niveles menores
                    },
                    // Los lectores de pantalla lo anuncian como encabezado
                    modifier = Modifier
                        .padding(top = if (bloque.nivel <= 2) 8.dp else 4.dp) // Aplica margen superior proporcional al nivel
                        .semantics { heading() } // Marca el elemento semánticamente como un encabezado para accesibilidad
                )

                is BloqueMarkdown.Parrafo -> Text( // Renderiza el componente de texto si el bloque es un Párrafo
                    text = textoEnriquecido(bloque.texto), // Asigna el texto formateado
                    style = MaterialTheme.typography.bodyMedium // Utiliza la tipografía Body Medium del tema
                )

                is BloqueMarkdown.Vineta -> Row( // Renderiza un contenedor horizontal si el bloque es una Viñeta
                    modifier = Modifier.padding(start = (16 * bloque.nivel).dp) // Asigna una sangría proporcional al nivel de anidamiento
                ) {
                    Text(text = "•", style = MaterialTheme.typography.bodyMedium) // Renderiza el símbolo de punto o viñeta
                    Text(
                        text = textoEnriquecido(bloque.texto), // Asigna el texto procesado de la viñeta
                        style = MaterialTheme.typography.bodyMedium, // Aplica el estilo Body Medium
                        modifier = Modifier.padding(start = 8.dp) // Añade un espacio de 8dp entre la viñeta y el texto
                    )
                }
            }
        }
    }
}
