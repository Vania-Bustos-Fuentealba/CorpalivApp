package com.duocuc.corpalivapp.data.network // Declaración del paquete al que pertenece este archivo dentro de la capa de red (Network)

/**
- PARCIAL 4: guarda en memoria el token JWT de la sesión activa.

- El cliente HTTP lo agrega a cada petición; al cerrar sesión o cerrar la app se pierde (no se escribe en disco).

 */
object SesionToken { // Objeto Singleton con el token de la sesión (null si no hay sesión iniciada)
    @Volatile // Garantiza que todos los hilos (UI y red) vean siempre el valor más reciente
    var valor: String? = null // Token JWT entregado por auth-service al iniciar sesión
}
