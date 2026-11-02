package com.duocuc.corpalivapp.navigation // Declaración del paquete al que pertenece este archivo dentro de la capa de navegación de la app

/**
- G10:

- NAVEGACIÓN ENTRE PANTALLAS. Así las pantallas no dependen del NavController.

 */
sealed class NavigationEvent { // Declaración de la clase sellada que encapsula todos los tipos de eventos de navegación posibles

    /**
    - Navegar a un destino específico.

    - @param route Destino tipado ([Screen]) al que se quiere ir.
    - @param popUpToRoute Destino hasta el cual se limpia la pila (null = no limpiar).
    - @param inclusive Si es true, [popUpToRoute] también se elimina de la pila.
    - @param singleTop Si es true, evita copias duplicadas del mismo destino en la
    - cima de la pila (útil para barra inferior y menú lateral).

     */
    data class NavigateTo( // Subclase de datos que representa una solicitud de navegación hacia un nuevo destino
        val route: Screen, // Propiedad de tipo Screen que especifica la pantalla de destino
        val popUpToRoute: Screen? = null, // Pantalla opcional hasta la cual se desapilarán los destinos previos en el backstack
        val inclusive: Boolean = false, // Indica si la pantalla definida en popUpToRoute también debe ser removida de la pila
        val singleTop: Boolean = false // Indica si se debe evitar reinstanciar la pantalla si esta ya está arriba en la pila
    ) : NavigationEvent() // Hereda de NavigationEvent

    /** Volver a la pantalla anterior en la pila de navegación. */
    data object PopBackStack : NavigationEvent() // Objeto singleton para solicitar la remoción de la pantalla actual y retornar a la anterior

    /** Navegar "hacia arriba" en la jerarquía (equivale a volver si no hay grafo anidado). */
    data object NavigateUp : NavigationEvent() // Objeto singleton para navegar ascendentemente en la jerarquía de pantallas de la app
}