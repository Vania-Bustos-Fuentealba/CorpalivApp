package com.duocuc.corpalivapp // Declaración del paquete al que pertenece este archivo dentro de la suite de pruebas unitarias locales

import org.junit.Test // Anotación del marco JUnit para definir y marcar una función como caso de prueba ejecutable

import org.junit.Assert.* // Importación estática de métodos de afirmación (assertEquals, assertTrue, etc.) para validar resultados esperados

/**
- Example local unit test, which will execute on the development machine (host).

 *
- See [testing documentation](http://d.android.com/tools/testing).

 */
class ExampleUnitTest { // Clase contenedor para las pruebas unitarias locales ejecutadas en la JVM de la máquina de desarrollo
    @Test // Marca la función subsecuente como un método de prueba unitaria independiente
    fun addition_isCorrect() { // Caso de prueba unitaria básico para verificar operaciones aritméticas elementales
        assertEquals(4, 2 + 2) // Afirma que la suma de 2 + 2 sea exactamente igual al valor esperado (4)
    }
}