package com.duocuc.corpalivapp // Declaración del paquete al que pertenece este archivo dentro de la suite de pruebas instrumentadas

import androidx.test.platform.app.InstrumentationRegistry // Importación del registro de instrumentación de AndroidX para acceder al contexto del dispositivo/emulador
import androidx.test.ext.junit.runners.AndroidJUnit4 // Ejecutor de pruebas de JUnit 4 adaptado al entorno de Android

import org.junit.Test // Anotación de JUnit para identificar métodos que representan casos de prueba ejecutable
import org.junit.runner.RunWith // Anotación para especificar el ejecutor con el que se deben correr las pruebas

import org.junit.Assert.* // Importación de métodos de aserción (assertEquals, assertTrue, etc.) para validar resultados

/**
- Instrumented test, which will execute on an Android device.

 *
- See [testing documentation](http://d.android.com/tools/testing).

 */
@RunWith(AndroidJUnit4::class) // Especifica que esta clase de pruebas debe ejecutarse utilizando la plataforma AndroidJUnit4
class ExampleInstrumentedTest { // Clase de prueba instrumentada de ejemplo para verificar la configuración del entorno en el dispositivo
    @Test // Anotación que indica que el método subsecuente es un caso de prueba
    fun useAppContext() { // Caso de prueba para comprobar el contexto y el ID del paquete de la app
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext // Obtiene el contexto de la aplicación bajo prueba desde el dispositivo
        assertEquals("com.duocuc.corpalivapp", appContext.packageName) // Afirma que el paquete obtenido coincida exactamente con el de CorpalivApp
    }
}