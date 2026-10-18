package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.duocuc.corpalivapp.data.model.UsuarioErrores
import com.duocuc.corpalivapp.data.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * G11: LÓGICA DEL FORMULARIO (MVVM)
 * Guarda el estado del formulario en un StateFlow y valida los datos.
 */
class UsuarioViewModel : ViewModel() {

    // Estado interno mutable y estado expuesto de solo lectura para la UI
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    // Validación en tiempo real: si un campo ya tenía error, se vuelve a validar
    // mientras el usuario escribe y el mensaje desaparece apenas queda correcto.

    fun onNombreChange(valor: String) {
        _estado.update {
            val error = if (it.errores.nombre != null) validarNombre(valor) else null
            it.copy(nombre = valor, errores = it.errores.copy(nombre = error))
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            val error = if (it.errores.correo != null) validarCorreo(valor) else null
            it.copy(correo = valor, errores = it.errores.copy(correo = error))
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            val error = if (it.errores.clave != null) validarClave(valor) else null
            it.copy(clave = valor, errores = it.errores.copy(clave = error))
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update {
            val error = if (it.errores.direccion != null) validarDireccion(valor) else null
            it.copy(direccion = valor, errores = it.errores.copy(direccion = error))
        }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(aceptaTerminos = valor, errores = it.errores.copy(terminos = null))
        }
    }

    /** Valida todo el formulario, actualiza los errores y retorna true si es válido. */
    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val errores = UsuarioErrores(
            nombre = validarNombre(actual.nombre),
            correo = validarCorreo(actual.correo),
            clave = validarClave(actual.clave),
            direccion = validarDireccion(actual.direccion),
            terminos = if (!actual.aceptaTerminos) "Debes aceptar los términos y condiciones" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion,
            errores.terminos
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    // G11: campos del interés de colaboración (mismo criterio de validación en tiempo real)

    fun onModalidadChange(valor: String) {
        _estado.update {
            it.copy(modalidad = valor, errores = it.errores.copy(modalidad = null))
        }
    }

    fun onTipoEntidadChange(valor: String) {
        _estado.update {
            it.copy(tipoEntidad = valor, errores = it.errores.copy(tipoEntidad = null))
        }
    }

    fun onMensajeChange(valor: String) {
        _estado.update {
            val error = if (it.errores.mensaje != null) validarMensaje(valor) else null
            it.copy(mensaje = valor, errores = it.errores.copy(mensaje = error))
        }
    }

    /** Valida el interés de colaboración y retorna true si es válido. */
    fun validarInteres(): Boolean {
        val actual = _estado.value
        val errores = actual.errores.copy(
            modalidad = validarModalidad(actual.modalidad),
            tipoEntidad = validarTipoEntidad(actual.tipoEntidad),
            mensaje = validarMensaje(actual.mensaje)
        )

        _estado.update { it.copy(errores = errores) }
        return listOfNotNull(errores.modalidad, errores.tipoEntidad, errores.mensaje).isEmpty()
    }

    /** Marca la solicitud como enviada; la pantalla muestra la confirmación. */
    fun enviar() {
        _estado.update { it.copy(enviado = true) }
    }

    /** Deja el formulario vacío (Lo usamos al finalizar el registro). */
    fun reiniciar() {
        _estado.value = UsuarioUiState()
    }

    // --- Reglas de validación por campo ---

    private fun validarNombre(valor: String): String? =
        if (valor.isBlank()) "Campo obligatorio" else null

    private fun validarCorreo(valor: String): String? =
        if (!REGEX_CORREO.matches(valor.trim())) "Correo inválido" else null

    private fun validarClave(valor: String): String? =
        if (valor.length < 6) "Debe tener al menos 6 caracteres" else null

    private fun validarDireccion(valor: String): String? =
        if (valor.isBlank()) "Campo obligatorio" else null

    private fun validarModalidad(valor: String): String? =
        if (valor.isBlank()) "Selecciona una modalidad" else null

    private fun validarTipoEntidad(valor: String): String? =
        if (valor.isBlank()) "Selecciona un tipo de entidad" else null

    private fun validarMensaje(valor: String): String? =
        if (valor.trim().length < 10) "Debe tener al menos 10 caracteres" else null

    private companion object {
        val REGEX_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
