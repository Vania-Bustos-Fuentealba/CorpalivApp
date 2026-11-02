package com.duocuc.corpalivapp // Declaración del paquete al que pertenece este archivo dentro de la suite de pruebas unitarias locales

import com.duocuc.corpalivapp.ui.components.BloqueMarkdown // Sealed class o modelo que representa los diferentes tipos de bloques formateados en Markdown
import com.duocuc.corpalivapp.ui.components.agruparEnSecciones // Función utilitaria que agrupa bloques de Markdown en listas organizadas por secciones
import com.duocuc.corpalivapp.ui.components.parsearMarkdown // Función utilitaria encargada de transformar una cadena de texto Markdown en una lista de objetos BloqueMarkdown
import org.junit.Assert.assertEquals // Importación de la aserción de JUnit para validar la igualdad entre el valor esperado y el obtenido
import org.junit.Test // Anotación del marco JUnit para marcar funciones como casos de prueba unitarios

/**
- Pruebas del lector de Markdown que se usa en "Quiénes somos".

 */
class MarkdownParserTest { // Clase contenedor para las pruebas unitarias del parser de Markdown utilizado en las pantallas informativas

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun reconoceTitulosParrafosYVinetas() { // Prueba la capacidad del parser para identificar títulos H1/H2, párrafos genéricos y listas con viñetas
        val bloques = parsearMarkdown( // Invoca la función de parseo con un texto sintético de prueba
            "# Título\n\nTexto de ejemplo.\n\n## Valores\n- Respeto\n- Empatía"
        )

        assertEquals( // Afirma que la estructura de objetos obtenida sea exactamente igual a la lista esperada
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

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun agrupaEnSecciones_laIntroduccionYCadaTituloDeNivel2() { // Prueba el correcto agrupamiento de bloques de Markdown según los encabezados de nivel 2 (##)
        val secciones = agruparEnSecciones( // Ejecuta el proceso de agrupamiento por secciones sobre la lista parseada
            parsearMarkdown("# Título\n\nIntroducción\n\n## Uno\n\nTexto uno\n\n## Dos\n\nPunto")
        )

        assertEquals(3, secciones.size) // Verifica que se hayan formado exactamente 3 secciones distintas
        assertEquals(2, secciones[0].size) // Comprueba que la primera sección (introducción/H1) contenga exactamente 2 elementos
        assertEquals(BloqueMarkdown.Titulo(2, "Uno"), secciones[1].first()) // Valida que el primer elemento de la segunda sección sea el título 'Uno'
        assertEquals(BloqueMarkdown.Titulo(2, "Dos"), secciones[2].first()) // Valida que el primer elemento de la tercera sección sea el título 'Dos'
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun unePorLineasSeguidasDeUnMismoParrafo() { // Prueba que las líneas de texto consecutivas dentro de un mismo bloque sean unidas en un único párrafo
        val bloques = parsearMarkdown("Primera línea\nsegunda línea") // Parseo de texto multilínea sin separación de párrafo doble

        assertEquals(listOf(BloqueMarkdown.Parrafo("Primera línea segunda línea")), bloques) // Afirma que ambas líneas resulten concatenadas en un solo Parrafo
    }

    @Test // Marca el método como un caso de prueba ejecutable por JUnit
    fun ignoraImagenes_yMideLaSangriaDeLasVinetas() { // Prueba el comportamiento del parser para omitir imágenes y medir la profundidad de sangría en viñetas
        val bloques = parsearMarkdown("![logo](https://ejemplo.cl/logo.png)\n- Uno\n  - Anidado")

        assertEquals( // Comprueba que la imagen se ignore y las viñetas contengan su nivel de sangría correspondiente
            listOf(
                BloqueMarkdown.Vineta(0, "Uno"),
                BloqueMarkdown.Vineta(1, "Anidado")
            ),
            bloques
        )
    }
}