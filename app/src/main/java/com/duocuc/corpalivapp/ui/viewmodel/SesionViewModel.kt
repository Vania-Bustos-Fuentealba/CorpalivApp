package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import androidx.lifecycle.ViewModel // Importación de la clase base ViewModel de Android Jetpack
import com.duocuc.corpalivapp.data.model.RolUsuario // Enum con los roles de usuario (ADMINISTRADOR, VISITANTE, etc.)
import com.duocuc.corpalivapp.data.model.UsuarioSesion // Modelo de datos para almacenar la información de la sesión activa
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto a la interfaz de usuario
import kotlinx.coroutines.flow.asStateFlow // Transforma un MutableStateFlow en un StateFlow inmutable
import kotlinx.coroutines.flow.update // Función de extensión para actualizar de forma atómica el estado en un MutableStateFlow

// SOLO DEMOSTRACIÓN: la cuenta de administrador está escrita en el código para poder probar el flujo.
// En una app real la autenticación se hace en un servidor y las credenciales nunca van en el repositorio.
internal const val ADMIN_CORREO = "admin@corpaliv.cl" // Constante interna que define el correo estático del administrador de prueba
internal const val ADMIN_CLAVE = "admin" // Constante interna que define la contraseña estática de prueba
internal const val ADMIN_NOMBRE = "Administrador CORPALIV" // Constante interna con el nombre visible del administrador

/** G13: lo que se escribe en el formulario de inicio de sesión y el mensaje de error, si hay. */
data class LoginUiState( // Clase de datos que encapsula el estado visual del formulario de login
    val correo: String = "", // Campo con el correo electrónico ingresado
    val clave: String = "", // Campo con la contraseña ingresada
    val error: String? = null // Mensaje de error de validación o autenticación (null si no hay error)
)

/**
- G13: SESIÓN DE LA CUENTA (MVVM)

- Guarda quién inició sesión y valida el formulario de acceso. La sesión solo vive en memoria:

- al cerrar la app hay que volver a ingresar. Se crea a nivel de la Activity para compartirla entre pantallas.

 */
class SesionViewModel : ViewModel() { // ViewModel para administrar el estado global de la sesión del usuario y el login

    // null = no hay sesión iniciada
    private val _usuario = MutableStateFlow<UsuarioSesion?>(null) // Estado mutable interno con la información del usuario en sesión o null
    val usuario: StateFlow<UsuarioSesion?> = _usuario.asStateFlow() // Flujo observable público con el usuario con sesión activa

    private val _login = MutableStateFlow(LoginUiState()) // Estado mutable interno con los datos del formulario de ingreso
    val login: StateFlow<LoginUiState> = _login.asStateFlow() // Flujo observable público con el estado del formulario de login

    // Al escribir se quita el mensaje de error anterior
    fun onCorreoChange(valor: String) { // Callback invocado al modificar el campo de correo
        _login.update { it.copy(correo = valor, error = null) } // Actualiza el correo y limpia cualquier mensaje de error existente
    }

    fun onClaveChange(valor: String) { // Callback invocado al modificar el campo de contraseña
        _login.update { it.copy(clave = valor, error = null) } // Actualiza la contraseña y limpia cualquier mensaje de error existente
    }

    /** Valida el acceso; si es correcto abre la sesión y retorna true. */
    fun iniciarSesion(ahora: Long = System.currentTimeMillis()): Boolean { // Método para validar credenciales e iniciar la sesión activa
        val datos = _login.value // Obtiene los datos actuales del formulario
        val correo = datos.correo.trim() // Remueve espacios en blanco innecesarios al inicio y final del correo

        return when {
            correo.isEmpty() || datos.clave.isEmpty() -> { // Evalúa si alguno de los campos obligatorios está vacío
                _login.update { it.copy(error = "Ingresa tu correo y tu contraseña") } // Establece el mensaje de error correspondiente
                false // Retorna false indicando que el login falló
            }

            // El correo no distingue mayúsculas; la contraseña sí
            correo.equals(ADMIN_CORREO, ignoreCase = true) && datos.clave == ADMIN_CLAVE -> { // Compara las credenciales ingresadas con las de administración
                _usuario.value = UsuarioSesion( // Crea e instala la sesión activa con el rol ADMINISTRADOR
                    nombre = ADMIN_NOMBRE,
                    correo = ADMIN_CORREO,
                    rol = RolUsuario.ADMINISTRADOR,
                    inicio = ahora
                )
                // La contraseña no se conserva en el estado
                _login.value = LoginUiState() // Limpia el formulario de login tras un inicio exitoso por seguridad
                true // Retorna true indicando que el inicio de sesión fue exitoso
            }

            else -> { // En caso de que las credenciales no coincidan con la cuenta administrada
                _login.update { it.copy(clave = "", error = "Correo o contraseña incorrectos") } // Limpia la contraseña y muestra error
                false // Retorna false indicando que el login falló
            }
        }
    }

    /** Cierra la sesión y deja el formulario de acceso vacío. */
    fun cerrarSesion() { // Destruye la sesión activa
        _usuario.value = null // Restablece la sesión a null
        _login.value = LoginUiState() // Reinicia los campos del formulario de login
    }
}