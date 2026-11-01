package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.duocuc.corpalivapp.data.model.RolUsuario
import com.duocuc.corpalivapp.data.model.UsuarioSesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// SOLO DEMOSTRACIÓN: la cuenta de administrador está escrita en el código para poder probar el flujo.
// En una app real la autenticación se hace en un servidor y las credenciales nunca van en el repositorio.
internal const val ADMIN_CORREO = "admin@corpaliv.cl"
internal const val ADMIN_CLAVE = "admin"
internal const val ADMIN_NOMBRE = "Administrador CORPALIV"

/** G13: lo que se escribe en el formulario de inicio de sesión y el mensaje de error, si hay. */
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val error: String? = null
)

/**
 * G13: SESIÓN DE LA CUENTA (MVVM)
 * Guarda quién inició sesión y valida el formulario de acceso. La sesión solo vive en memoria:
 * al cerrar la app hay que volver a ingresar. Se crea a nivel de la Activity para compartirla entre pantallas.
 */
class SesionViewModel : ViewModel() {

    // null = no hay sesión iniciada
    private val _usuario = MutableStateFlow<UsuarioSesion?>(null)
    val usuario: StateFlow<UsuarioSesion?> = _usuario.asStateFlow()

    private val _login = MutableStateFlow(LoginUiState())
    val login: StateFlow<LoginUiState> = _login.asStateFlow()

    // Al escribir se quita el mensaje de error anterior
    fun onCorreoChange(valor: String) {
        _login.update { it.copy(correo = valor, error = null) }
    }

    fun onClaveChange(valor: String) {
        _login.update { it.copy(clave = valor, error = null) }
    }

    /** Valida el acceso; si es correcto abre la sesión y retorna true. */
    fun iniciarSesion(ahora: Long = System.currentTimeMillis()): Boolean {
        val datos = _login.value
        val correo = datos.correo.trim()

        return when {
            correo.isEmpty() || datos.clave.isEmpty() -> {
                _login.update { it.copy(error = "Ingresa tu correo y tu contraseña") }
                false
            }

            // El correo no distingue mayúsculas; la contraseña sí
            correo.equals(ADMIN_CORREO, ignoreCase = true) && datos.clave == ADMIN_CLAVE -> {
                _usuario.value = UsuarioSesion(
                    nombre = ADMIN_NOMBRE,
                    correo = ADMIN_CORREO,
                    rol = RolUsuario.ADMINISTRADOR,
                    inicio = ahora
                )
                // La contraseña no se conserva en el estado
                _login.value = LoginUiState()
                true
            }

            else -> {
                _login.update { it.copy(clave = "", error = "Correo o contraseña incorrectos") }
                false
            }
        }
    }

    /** Cierra la sesión y deja el formulario de acceso vacío. */
    fun cerrarSesion() {
        _usuario.value = null
        _login.value = LoginUiState()
    }
}
