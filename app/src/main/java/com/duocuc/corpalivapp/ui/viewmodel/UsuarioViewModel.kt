package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.data.model.UsuarioErrores
import com.duocuc.corpalivapp.data.model.UsuarioUiState
import com.duocuc.corpalivapp.util.esRutValido
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * G11: LÓGICA DEL FORMULARIO (MVVM)
 * Guarda el estado del formulario en un StateFlow y valida los datos.
 */
class UsuarioViewModel : ViewModel() {

    // Estado interno mutable y estado expuesto de solo lectura para la UI
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    // Corrutina del envío simulado; se cancela si el formulario se reinicia
    private var envioJob: Job? = null

    // Validación en tiempo real: si un campo ya tenía error, se vuelve a validar
    // mientras el usuario escribe y el mensaje desaparece apenas queda correcto.

    fun onNombreChange(valor: String) {
        _estado.update {
            val error = if (it.errores.nombre != null) validarNombre(valor) else null
            it.copy(nombre = valor, errores = it.errores.copy(nombre = error))
        }
    }

    fun onApellidoChange(valor: String) {
        _estado.update {
            val error = if (it.errores.apellido != null) validarApellido(valor) else null
            it.copy(apellido = valor, errores = it.errores.copy(apellido = error))
        }
    }

    fun onRutChange(valor: String) {
        _estado.update {
            val error = if (it.errores.rut != null) validarRut(valor) else null
            it.copy(rut = valor, errores = it.errores.copy(rut = error))
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            val error = if (it.errores.correo != null) validarCorreo(valor) else null
            it.copy(correo = valor, errores = it.errores.copy(correo = error))
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update {
            val error = if (it.errores.direccion != null) validarDireccion(valor) else null
            it.copy(direccion = valor, errores = it.errores.copy(direccion = error))
        }
    }

    fun onMotivoChange(valor: String) {
        _estado.update {
            it.copy(motivo = valor, errores = it.errores.copy(motivo = null))
        }
    }

    /** Guarda la oportunidad elegida y deja el formulario vacío para un registro nuevo. */
    fun seleccionarOportunidad(titulo: String) {
        envioJob?.cancel()
        _estado.value = UsuarioUiState(oportunidad = titulo)
    }

    /** Valida todo el formulario, actualiza los errores y retorna true si es válido. */
    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val errores = UsuarioErrores(
            nombre = validarNombre(actual.nombre),
            apellido = validarApellido(actual.apellido),
            rut = validarRut(actual.rut),
            correo = validarCorreo(actual.correo),
            direccion = validarDireccion(actual.direccion),
            motivo = validarMotivo(actual.motivo)
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.apellido,
            errores.rut,
            errores.correo,
            errores.direccion,
            errores.motivo
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    /** G12: simula el envío avanzando la barra de carga y avisa a la pantalla cuando termina. */
    fun enviarRegistro() {
        if (_estado.value.enviando) return

        envioJob = viewModelScope.launch {
            _estado.update { it.copy(enviando = true, progreso = 0f) }

            for (paso in 1..PASOS_ENVIO) {
                delay(PAUSA_PASO_MS)
                _estado.update { it.copy(progreso = paso / PASOS_ENVIO.toFloat()) }
            }

            _estado.update { it.copy(enviando = false, completado = true) }
        }
    }

    /** La pantalla ya navegó: se apaga el aviso para no repetir la navegación. */
    fun confirmarNavegacion() {
        _estado.update { it.copy(completado = false) }
    }

    /** Deja el formulario vacío (Lo usamos al finalizar el registro). */
    fun reiniciar() {
        envioJob?.cancel()
        _estado.value = UsuarioUiState()
    }

    // --- Reglas de validación por campo ---

    private fun validarNombre(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarApellido(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 2 -> "Debe tener al menos 2 caracteres"
        else -> null
    }

    private fun validarRut(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        !esRutValido(valor) -> "RUT inválido"
        else -> null
    }

    private fun validarCorreo(valor: String): String? =
        if (!REGEX_CORREO.matches(valor.trim())) "Correo inválido" else null

    private fun validarDireccion(valor: String): String? =
        if (valor.isBlank()) "Campo obligatorio" else null

    private fun validarMotivo(valor: String): String? =
        if (valor.isBlank()) "Selecciona un motivo" else null

    private companion object {
        val REGEX_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        // El envío simulado dura 20 pasos de 100 ms (unos 2 segundos)
        const val PASOS_ENVIO = 20
        const val PAUSA_PASO_MS = 100L
    }
}
