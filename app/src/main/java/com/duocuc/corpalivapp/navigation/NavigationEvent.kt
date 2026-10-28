package com.duocuc.corpalivapp.navigation

/**
 * G10:
 * NAVEGACIÓN ENTRE PANTALLAS. Así las pantallas no dependen del NavController.
 */
sealed class NavigationEvent {

    /**
     * Navegar a un destino específico.
     * @param route Destino tipado ([Screen]) al que se quiere ir.
     * @param popUpToRoute Destino hasta el cual se limpia la pila (null = no limpiar).
     * @param inclusive Si es true, [popUpToRoute] también se elimina de la pila.
     * @param singleTop Si es true, evita copias duplicadas del mismo destino en la
     * cima de la pila (útil para barra inferior y menú lateral).
     */
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    /** Volver a la pantalla anterior en la pila de navegación. */
    data object PopBackStack : NavigationEvent()

    /** Navegar "hacia arriba" en la jerarquía (equivale a volver si no hay grafo anidado). */
    data object NavigateUp : NavigationEvent()
}
