package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import androidx.lifecycle.ViewModel // Importación de la clase base ViewModel de Android Jetpack para conservar estado entre cambios de configuración
import androidx.lifecycle.viewModelScope // Alcance de corrutinas vinculado y acotado al ciclo de vida de la instancia del ViewModel
import com.duocuc.corpalivapp.navigation.NavigationEvent // Sealed class que encapsula las acciones e intenciones de navegación
import com.duocuc.corpalivapp.navigation.Screen // Sealed class o enumeración que define los destinos y rutas de la aplicación
import kotlinx.coroutines.flow.MutableSharedFlow // Flujo caliente mutable para la emisión de eventos únicos de navegación
import kotlinx.coroutines.flow.SharedFlow // Flujo de estado compartido de solo lectura expuesto a la interfaz de usuario
import kotlinx.coroutines.flow.asSharedFlow // Transforma un MutableSharedFlow en un SharedFlow inmutable
import kotlinx.coroutines.launch // Extensión para iniciar la ejecución de corrutinas dentro de un scope

/**
- G10: LÓGICA DE NAVEGACIÓN EN EL VIEWMODEL (MVVM).

 *
- Centraliza las decisiones de navegación.

 */
class MainViewModel : ViewModel() { // ViewModel principal encargado de centralizar y desacoplar las órdenes de navegación de la UI

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>() // Flujo interno mutable para emitir eventos puntuales de navegación

    // Se expone como Flow de solo lectura: la UI observa, pero no puede emitir eventos.
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow() // Flujo de eventos público de solo lectura para ser consumido en la vista

    /** Se emite el evento para ir a [screen]. Los demás parámetros controlan la pila. */
    fun navigateTo( // Función pública para solicitar la navegación hacia una pantalla específica con opciones de control de la pila de retroceso
        screen: Screen, // Pantalla de destino a la que se desea navegar
        popUpToRoute: Screen? = null, // Pantalla opcional hasta la cual se removerán destinos anteriores en la pila (backstack)
        inclusive: Boolean = false, // Booleano para indicar si la ruta 'popUpToRoute' también debe ser removida de la pila
        singleTop: Boolean = false // Booleano para evitar duplicar la pantalla en la parte superior si ya está activa
    ) {
        emitir(NavigationEvent.NavigateTo(screen, popUpToRoute, inclusive, singleTop)) // Crea y emite el evento NavigateTo con sus respectivas configuraciones
    }

    /** Emite el evento para volver a la pantalla anterior. */
    fun navigateBack() { // Función pública para solicitar el retroceso a la pantalla anterior en la pila
        emitir(NavigationEvent.PopBackStack) // Emite el evento que desapila el destino actual (popBackStack)
    }

    /** Emite el evento para navegar hacia arriba en la jerarquía. */
    fun navigateUp() { // Función pública para solicitar la navegación ascendente en la jerarquía de la app
        emitir(NavigationEvent.NavigateUp) // Emite el evento NavigateUp
    }

    // Se usa viewModelScope: la corutina se cancela sola cuando el ViewModel se destruye.
    private fun emitir(evento: NavigationEvent) { // Método auxiliar privado para emitir eventos de navegación de forma asíncrona y segura
        viewModelScope.launch { _navigationEvents.emit(evento) } // Inicia una corrutina en el scope del ViewModel para emitir el evento en el SharedFlow
    }
}