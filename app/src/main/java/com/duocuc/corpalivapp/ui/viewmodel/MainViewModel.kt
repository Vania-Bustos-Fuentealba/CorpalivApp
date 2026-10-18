package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.navigation.NavigationEvent
import com.duocuc.corpalivapp.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * G10: LÓGICA DE NAVEGACIÓN EN EL VIEWMODEL (MVVM).
 *
 * Centraliza las decisiones de navegación.
 */
class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    // Se expone como Flow de solo lectura: la UI observa, pero no puede emitir eventos.
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    /** Se emite el evento para ir a [screen]. Los demás parámetros controlan la pila. */
    fun navigateTo(
        screen: Screen,
        popUpToRoute: Screen? = null,
        inclusive: Boolean = false,
        singleTop: Boolean = false
    ) {
        emitir(NavigationEvent.NavigateTo(screen, popUpToRoute, inclusive, singleTop))
    }

    /** Emite el evento para volver a la pantalla anterior. */
    fun navigateBack() {
        emitir(NavigationEvent.PopBackStack)
    }

    /** Emite el evento para navegar hacia arriba en la jerarquía. */
    fun navigateUp() {
        emitir(NavigationEvent.NavigateUp)
    }

    // Se usa viewModelScope: la corutina se cancela sola cuando el ViewModel se destruye.
    private fun emitir(evento: NavigationEvent) {
        viewModelScope.launch { _navigationEvents.emit(evento) }
    }
}
