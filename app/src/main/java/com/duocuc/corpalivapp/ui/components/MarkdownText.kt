package com.duocuc.corpalivapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

/**
 * Bloques que entiende el lector: títulos (#), párrafos y viñetas (- o *).
 */
sealed class BloqueMarkdown {
    data class Titulo(val nivel: Int, val texto: String) : BloqueMarkdown()
    data class Parrafo(val texto: String) : BloqueMarkdown()
    data class Vineta(val nivel: Int, val texto: String) : BloqueMarkdown()
}

// Enlaces [texto](url): se muestra solo el texto
private val REGEX_ENLACE = Regex("\\[([^\\]]+)]\\([^)]*\\)")

/**
 * Convierte el texto Markdown en bloques. Las imágenes y las líneas vacías no generan bloques;
 * las líneas seguidas de un mismo párrafo se unen con un espacio.
 */
internal fun parsearMarkdown(markdown: String): List<BloqueMarkdown> {
    val bloques = mutableListOf<BloqueMarkdown>()
    val parrafo = StringBuilder()

    // Cierra el párrafo en curso (si hay) y lo agrega a la lista
    fun cerrarParrafo() {
        if (parrafo.isNotBlank()) bloques.add(BloqueMarkdown.Parrafo(parrafo.toString().trim()))
        parrafo.clear()
    }

    for (lineaCruda in markdown.lines()) {
        val linea = lineaCruda.trimEnd()
        val sinSangria = linea.trimStart()

        when {
            sinSangria.isEmpty() -> cerrarParrafo()

            // Imágenes: no se muestran
            sinSangria.startsWith("![") -> cerrarParrafo()

            sinSangria.startsWith("#") -> {
                cerrarParrafo()
                val nivel = sinSangria.takeWhile { it == '#' }.length
                bloques.add(BloqueMarkdown.Titulo(nivel, sinSangria.drop(nivel).trim()))
            }

            sinSangria.startsWith("- ") || sinSangria.startsWith("* ") -> {
                cerrarParrafo()
                val nivel = (linea.length - sinSangria.length) / 2
                bloques.add(BloqueMarkdown.Vineta(nivel, sinSangria.drop(2).trim()))
            }

            else -> {
                if (parrafo.isNotEmpty()) parrafo.append(' ')
                parrafo.append(sinSangria)
            }
        }
    }
    cerrarParrafo()
    return bloques
}

/**
 * Agrupa los bloques en secciones: la primera llega hasta el primer título de nivel 2
 * y cada título de nivel 2 abre una sección nueva.
 */
internal fun agruparEnSecciones(bloques: List<BloqueMarkdown>): List<List<BloqueMarkdown>> {
    val secciones = mutableListOf<List<BloqueMarkdown>>()
    var actual = mutableListOf<BloqueMarkdown>()

    for (bloque in bloques) {
        if (bloque is BloqueMarkdown.Titulo && bloque.nivel == 2 && actual.isNotEmpty()) {
            secciones.add(actual)
            actual = mutableListOf()
        }
        actual.add(bloque)
    }
    if (actual.isNotEmpty()) secciones.add(actual)

    return secciones
}

// Quita los enlaces y aplica negrita a lo que va entre ** **
private fun textoEnriquecido(texto: String): AnnotatedString {
    val limpio = REGEX_ENLACE.replace(texto) { it.groupValues[1] }
    return buildAnnotatedString {
        var negrita = false
        for (parte in limpio.split("**")) {
            if (negrita) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(parte) }
            } else {
                append(parte)
            }
            negrita = !negrita
        }
    }
}

/**
 * Muestra un texto en Markdown con los estilos de MaterialTheme.
 * Soporta títulos, párrafos, viñetas (con sangría) y negrita.
 */
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val bloques = remember(markdown) { parsearMarkdown(markdown) }
    BloquesMarkdown(bloques = bloques, modifier = modifier)
}

/**
 * Muestra el Markdown separado en bloques: la introducción va sin marco y cada sección
 * (título de nivel 2) va dentro de su propia tarjeta con borde, con un estilo sobrio.
 */
@Composable
fun MarkdownEnBloques(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val secciones = remember(markdown) { agruparEnSecciones(parsearMarkdown(markdown)) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        secciones.forEach { seccion ->
            val primero = seccion.first()
            val esIntroduccion = !(primero is BloqueMarkdown.Titulo && primero.nivel == 2)

            if (esIntroduccion) {
                BloquesMarkdown(bloques = seccion)
            } else {
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    BloquesMarkdown(
                        bloques = seccion,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

// Dibuja cada bloque con los estilos de MaterialTheme
@Composable
private fun BloquesMarkdown(
    bloques: List<BloqueMarkdown>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        bloques.forEach { bloque ->
            when (bloque) {
                is BloqueMarkdown.Titulo -> Text(
                    text = textoEnriquecido(bloque.texto),
                    style = when (bloque.nivel) {
                        1 -> MaterialTheme.typography.headlineMedium
                        2 -> MaterialTheme.typography.titleLarge
                        3 -> MaterialTheme.typography.titleMedium
                        else -> MaterialTheme.typography.titleSmall
                    },
                    fontWeight = FontWeight.Bold,
                    color = if (bloque.nivel <= 2) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    // Los lectores de pantalla lo anuncian como encabezado
                    modifier = Modifier
                        .padding(top = if (bloque.nivel <= 2) 8.dp else 4.dp)
                        .semantics { heading() }
                )

                is BloqueMarkdown.Parrafo -> Text(
                    text = textoEnriquecido(bloque.texto),
                    style = MaterialTheme.typography.bodyMedium
                )

                is BloqueMarkdown.Vineta -> Row(
                    modifier = Modifier.padding(start = (16 * bloque.nivel).dp)
                ) {
                    Text(text = "•", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = textoEnriquecido(bloque.texto),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
