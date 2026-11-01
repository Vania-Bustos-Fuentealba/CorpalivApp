package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.data.model.DatosEmpresa
import com.duocuc.corpalivapp.data.model.DatosPersona
import com.duocuc.corpalivapp.data.model.ErroresEmpresa
import com.duocuc.corpalivapp.data.model.ErroresPersona
import com.duocuc.corpalivapp.data.model.MOTIVO_APORTAR
import com.duocuc.corpalivapp.data.model.RegistroGuardado
import com.duocuc.corpalivapp.data.model.TipoRegistro
import com.duocuc.corpalivapp.data.model.UsuarioUiState
import com.duocuc.corpalivapp.util.esRutValido
import com.duocuc.corpalivapp.util.formatearRut
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
 * Maneja dos tipos de formulario (persona o empresa) en un mismo estado.
 */
class UsuarioViewModel : ViewModel() {

    // Estado interno mutable y estado expuesto de solo lectura para la UI
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    // Corrutina del envío simulado; se cancela si el formulario se reinicia
    private var envioJob: Job? = null

    /** Cambia el tipo de formulario; lo escrito en cada tipo se conserva. */
    fun onTipoChange(tipo: TipoRegistro) {
        _estado.update { it.copy(tipo = tipo) }
    }

    // Validación en tiempo real: si un campo ya tenía error, se vuelve a validar
    // mientras el usuario escribe y el mensaje desaparece apenas queda correcto.

    // --- Campos de persona ---

    fun onNombreChange(valor: String) = cambiarPersona { it.copy(nombre = valor) }

    fun onApellidoChange(valor: String) = cambiarPersona { it.copy(apellido = valor) }

    fun onRunChange(valor: String) = cambiarPersona { it.copy(run = formatearRut(valor)) }

    fun onCorreoChange(valor: String) = cambiarPersona { it.copy(correo = valor) }

    fun onTelefonoChange(valor: String) =
        cambiarPersona { it.copy(telefono = soloDigitos(valor, DIGITOS_CELULAR)) }

    fun onMotivoChange(valor: String) = cambiarPersona { it.copy(motivo = valor) }

    fun onFrecuenciaChange(valor: String) = cambiarPersona { it.copy(frecuenciaDonacion = valor) }

    // --- Campos de empresa ---

    fun onRazonSocialChange(valor: String) = cambiarEmpresa { it.copy(razonSocial = valor) }

    fun onRutEmpresaChange(valor: String) = cambiarEmpresa { it.copy(rut = formatearRut(valor)) }

    fun onGiroChange(valor: String) = cambiarEmpresa { it.copy(giro = valor) }

    fun onRepresentanteChange(valor: String) = cambiarEmpresa { it.copy(representante = valor) }

    fun onRutRepresentanteChange(valor: String) =
        cambiarEmpresa { it.copy(rutRepresentante = formatearRut(valor)) }

    fun onCorreoEmpresaChange(valor: String) = cambiarEmpresa { it.copy(correo = valor) }

    fun onTelefonoEmpresaChange(valor: String) =
        cambiarEmpresa { it.copy(telefono = soloDigitos(valor, DIGITOS_TELEFONO_EMPRESA)) }

    fun onDireccionEmpresaChange(valor: String) = cambiarEmpresa { it.copy(direccion = valor) }

    /** Guarda la oportunidad elegida y deja el formulario vacío para un registro nuevo. */
    fun seleccionarOportunidad(titulo: String) {
        envioJob?.cancel()
        _estado.value = UsuarioUiState(oportunidad = titulo)
    }

    /** Valida el formulario del tipo elegido, actualiza sus errores y retorna true si es válido. */
    fun validarFormulario(): Boolean {
        val actual = _estado.value

        return when (actual.tipo) {
            TipoRegistro.PERSONA -> {
                val errores = erroresPersona(actual.persona)
                _estado.update { it.copy(errores = it.errores.copy(persona = errores)) }

                listOfNotNull(
                    errores.nombre,
                    errores.apellido,
                    errores.run,
                    errores.correo,
                    errores.telefono,
                    errores.motivo,
                    errores.frecuencia
                ).isEmpty()
            }

            TipoRegistro.EMPRESA -> {
                val errores = erroresEmpresa(actual.empresa)
                _estado.update { it.copy(errores = it.errores.copy(empresa = errores)) }

                listOfNotNull(
                    errores.razonSocial,
                    errores.rut,
                    errores.giro,
                    errores.representante,
                    errores.rutRepresentante,
                    errores.correo,
                    errores.telefono,
                    errores.direccion
                ).isEmpty()
            }
        }
    }

    /**
     * Arma el aporte que se guarda para el Dashboard. Retorna null si el registro no es un aporte
     * (por ejemplo, una persona que solo quiere ingresar a un programa).
     */
    fun construirRegistro(ahora: Long = System.currentTimeMillis()): RegistroGuardado? {
        val actual = _estado.value
        if (!actual.esAporte) return null

        return when (actual.tipo) {
            TipoRegistro.PERSONA -> {
                val p = actual.persona
                RegistroGuardado(
                    id = ahora,
                    tipo = TipoRegistro.PERSONA.name,
                    oportunidad = actual.oportunidad,
                    nombre = "${p.nombre.trim()} ${p.apellido.trim()}",
                    rut = p.run,
                    correo = p.correo.trim(),
                    telefono = "+569 ${p.telefono}",
                    tipoAporte = p.frecuenciaDonacion
                )
            }

            TipoRegistro.EMPRESA -> {
                val e = actual.empresa
                RegistroGuardado(
                    id = ahora,
                    tipo = TipoRegistro.EMPRESA.name,
                    oportunidad = actual.oportunidad,
                    nombre = e.razonSocial.trim(),
                    rut = e.rut,
                    correo = e.correo.trim(),
                    telefono = "+56 ${e.telefono}",
                    tipoAporte = "Aporte empresarial",
                    direccion = e.direccion.trim(),
                    giro = e.giro.trim(),
                    representante = e.representante.trim(),
                    rutRepresentante = e.rutRepresentante
                )
            }
        }
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

    // Aplica el cambio a los datos de persona y revalida solo los campos que ya tenían error
    private fun cambiarPersona(cambio: (DatosPersona) -> DatosPersona) {
        _estado.update { estado ->
            val nueva = cambio(estado.persona)
            val previos = estado.errores.persona

            val errores = ErroresPersona(
                nombre = if (previos.nombre != null) validarNombre(nueva.nombre) else null,
                apellido = if (previos.apellido != null) validarApellido(nueva.apellido) else null,
                run = if (previos.run != null) validarRut(nueva.run, "RUN") else null,
                correo = if (previos.correo != null) validarCorreo(nueva.correo) else null,
                telefono = if (previos.telefono != null) {
                    validarTelefono(nueva.telefono, DIGITOS_CELULAR, "celular")
                } else null,
                motivo = if (previos.motivo != null) validarMotivo(nueva.motivo) else null,
                frecuencia = if (previos.frecuencia != null) validarFrecuencia(nueva) else null
            )

            estado.copy(persona = nueva, errores = estado.errores.copy(persona = errores))
        }
    }

    // Igual que cambiarPersona, pero para los datos de empresa
    private fun cambiarEmpresa(cambio: (DatosEmpresa) -> DatosEmpresa) {
        _estado.update { estado ->
            val nueva = cambio(estado.empresa)
            val previos = estado.errores.empresa

            val errores = ErroresEmpresa(
                razonSocial = if (previos.razonSocial != null) validarRazonSocial(nueva.razonSocial) else null,
                rut = if (previos.rut != null) validarRut(nueva.rut, "RUT") else null,
                giro = if (previos.giro != null) validarGiro(nueva.giro) else null,
                representante = if (previos.representante != null) {
                    validarRepresentante(nueva.representante)
                } else null,
                rutRepresentante = if (previos.rutRepresentante != null) {
                    validarRut(nueva.rutRepresentante, "RUT")
                } else null,
                correo = if (previos.correo != null) validarCorreo(nueva.correo) else null,
                telefono = if (previos.telefono != null) {
                    validarTelefono(nueva.telefono, DIGITOS_TELEFONO_EMPRESA, "teléfono")
                } else null,
                direccion = if (previos.direccion != null) validarDireccion(nueva.direccion) else null
            )

            estado.copy(empresa = nueva, errores = estado.errores.copy(empresa = errores))
        }
    }

    private fun erroresPersona(p: DatosPersona) = ErroresPersona(
        nombre = validarNombre(p.nombre),
        apellido = validarApellido(p.apellido),
        run = validarRut(p.run, "RUN"),
        correo = validarCorreo(p.correo),
        telefono = validarTelefono(p.telefono, DIGITOS_CELULAR, "celular"),
        motivo = validarMotivo(p.motivo),
        frecuencia = validarFrecuencia(p)
    )

    private fun erroresEmpresa(e: DatosEmpresa) = ErroresEmpresa(
        razonSocial = validarRazonSocial(e.razonSocial),
        rut = validarRut(e.rut, "RUT"),
        giro = validarGiro(e.giro),
        representante = validarRepresentante(e.representante),
        rutRepresentante = validarRut(e.rutRepresentante, "RUT"),
        correo = validarCorreo(e.correo),
        telefono = validarTelefono(e.telefono, DIGITOS_TELEFONO_EMPRESA, "teléfono"),
        direccion = validarDireccion(e.direccion)
    )

    // Deja solo los dígitos y corta al máximo permitido
    private fun soloDigitos(valor: String, maximo: Int): String =
        valor.filter { it.isDigit() }.take(maximo)

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

    // [nombre] es "RUN" para personas y "RUT" para empresas y representantes
    private fun validarRut(valor: String, nombre: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        !esRutValido(valor) -> "$nombre inválido"
        else -> null
    }

    private fun validarCorreo(valor: String): String? =
        if (!REGEX_CORREO.matches(valor.trim())) "Correo inválido" else null

    private fun validarTelefono(valor: String, digitos: Int, tipo: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.length != digitos -> "Ingresa los $digitos dígitos del $tipo"
        else -> null
    }

    private fun validarMotivo(valor: String): String? =
        if (valor.isBlank()) "Selecciona un motivo" else null

    // La frecuencia solo se pide cuando el motivo es "Aportar"
    private fun validarFrecuencia(p: DatosPersona): String? =
        if (p.motivo == MOTIVO_APORTAR && p.frecuenciaDonacion.isBlank()) {
            "Selecciona la frecuencia"
        } else null

    private fun validarRazonSocial(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarGiro(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarRepresentante(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarDireccion(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 5 -> "Ingresa una dirección completa"
        else -> null
    }

    private companion object {
        val REGEX_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        // Celular: +569 y 8 dígitos. Teléfono de empresa: +56 y 9 dígitos (celular o fijo)
        const val DIGITOS_CELULAR = 8
        const val DIGITOS_TELEFONO_EMPRESA = 9

        // El envío simulado dura 20 pasos de 100 ms (unos 2 segundos)
        const val PASOS_ENVIO = 20
        const val PAUSA_PASO_MS = 100L
    }
}
