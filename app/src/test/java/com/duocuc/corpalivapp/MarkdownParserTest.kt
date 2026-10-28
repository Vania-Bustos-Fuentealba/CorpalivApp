package com.duocuc.corpalivapp

import com.duocuc.corpalivapp.ui.components.BloqueMarkdown
import com.duocuc.corpalivapp.ui.components.parsearMarkdown
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pruebas del lector de Markdown que se usa en "Quiénes somos".
 */
class MarkdownParserTest {

    @Test
    fun reconoceTitulosParrafosYVinetas() {
        val bloques = parsearMarkdown(
            "# Título\n\nTexto de ejemplo.\n\n## Valores\n- Respeto\n- Empatía"
        )

        assertEquals(
            listOf(
                BloqueMarkdown.Titulo(1, "Título"),
                BloqueMarkdown.Parrafo("Texto de ejemplo."),
                BloqueMarkdown.Titulo(2, "Valores"),
                BloqueMarkdown.Vineta(0, "Respeto"),
                BloqueMarkdown.Vineta(0, "Empatía")
            ),
            bloques
        )
    }

    @Test
    fun unePorLineasSeguidasDeUnMismoParrafo() {
        val bloques = parsearMarkdown("Primera línea\nsegunda línea")

        assertEquals(listOf(BloqueMarkdown.Parrafo("Primera línea segunda línea")), bloques)
    }

    @Test
    fun ignoraImagenes_yMideLaSangriaDeLasVinetas() {
        val bloques = parsearMarkdown("![logo](https://ejemplo.cl/logo.png)\n- Uno\n  - Anidado")

        assertEquals(
            listOf(
                BloqueMarkdown.Vineta(0, "Uno"),
                BloqueMarkdown.Vineta(1, "Anidado")
            ),
            bloques
        )
    }
}
