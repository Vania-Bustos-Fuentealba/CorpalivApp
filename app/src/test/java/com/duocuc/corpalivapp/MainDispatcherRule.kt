package com.duocuc.corpalivapp // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import kotlinx.coroutines.Dispatchers // Importa Dispatchers: contiene los ejecutores de corrutinas, incluido el hilo principal.
import kotlinx.coroutines.ExperimentalCoroutinesApi // Importa ExperimentalCoroutinesApi: identifica las funciones de prueba que requieren aceptación explícita.
import kotlinx.coroutines.test.StandardTestDispatcher // Importa StandardTestDispatcher: pone las corrutinas en una cola controlada por el test.
import kotlinx.coroutines.test.TestDispatcher // Importa TestDispatcher: es el tipo de ejecutor controlable usado en las pruebas.
import kotlinx.coroutines.test.resetMain // Importa resetMain: restaura el ejecutor principal después de cada prueba.
import kotlinx.coroutines.test.setMain // Importa setMain: sustituye el ejecutor principal por el ejecutor de prueba.
import org.junit.rules.TestWatcher // Importa TestWatcher: permite ejecutar preparación y limpieza alrededor de cada test JUnit.
import org.junit.runner.Description // Importa Description: describe el test que está comenzando o terminando.

/** Controla Dispatchers.Main sin Android ni esperas reales. */
@OptIn(ExperimentalCoroutinesApi::class) // Acepta explícitamente las API experimentales usadas para controlar las corrutinas de prueba.
class MainDispatcherRule( // Define una regla reutilizable de preparación del hilo principal para JUnit.
    val dispatcher: TestDispatcher = StandardTestDispatcher() // Crea un ejecutor con tareas en cola, que cada test avanza explícitamente.
) : TestWatcher() { // Recibe avisos automáticos de JUnit al comenzar y terminar cada test.
    override fun starting(description: Description) { // Prepara el ejecutor antes de ejecutar el cuerpo del test.
        Dispatchers.setMain(dispatcher) // Sustituye Main; así viewModelScope puede ejecutarse sin un dispositivo Android.
    }

    override fun finished(description: Description) { // Limpia la sustitución cuando termina el test, aunque falle.
        Dispatchers.resetMain() // Evita que la configuración del test afecte otras pruebas.
    }
}
