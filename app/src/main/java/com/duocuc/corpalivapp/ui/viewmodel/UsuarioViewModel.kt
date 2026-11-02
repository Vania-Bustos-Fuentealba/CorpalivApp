package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import androidx.lifecycle.ViewModel // Importación de la clase base ViewModel de Android Jetpack
import androidx.lifecycle.viewModelScope // Alcance de corrutinas acotado al ciclo de vida del ViewModel
import com.duocuc.corpalivapp.data.model.DatosEmpresa // Importación del modelo con los datos de formulario de tipo Empresa
import com.duocuc.corpalivapp.data.model.DatosPersona // Importación del modelo con los datos de formulario de tipo Persona
import com.duocuc.corpalivapp.data.model.ErroresEmpresa // Importación de la estructura de errores de validación para Empresa
import com.duocuc.corpalivapp.data.model.ErroresPersona // Importación de la estructura de errores de validación para Persona
import com.duocuc.corpalivapp.data.model.MOTIVO_APORTAR // Constante que identifica el motivo de donación/aporte
import com.duocuc.corpalivapp.data.model.RegistroGuardado // Modelo de datos para la persistencia del registro en DataStore
import com.duocuc.corpalivapp.data.model.TipoRegistro // Enum con los tipos de registro (PERSONA o EMPRESA)
import com.duocuc.corpalivapp.data.model.UsuarioUiState // Clase de datos con el estado global unificado de la UI del usuario
import com.duocuc.corpalivapp.util.esRutValido // Función utilitaria para validar el algoritmo del dígito verificador del RUT/RUN
import com.duocuc.corpalivapp.util.formatearRut // Función utilitaria para dar formato visual al RUT/RUN (puntos y guión)
import kotlinx.coroutines.Job // Clase que representa una corrutina cancelable o manejable
import kotlinx.coroutines.delay // Función de suspensión para simular tiempos de espera
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto a la vista
import kotlinx.coroutines.flow.asStateFlow // Transforma un MutableStateFlow en un StateFlow inmutable
import kotlinx.coroutines.flow.update // Función de extensión para actualizar estados atómicamente
import kotlinx.coroutines.launch // Extensión de corrutina para iniciar operaciones asíncronas

/**
- G11: LÓGICA DEL FORMULARIO (MVVM)

- Guarda el estado del formulario en un StateFlow y valida los datos.

- Maneja dos tipos de formulario (persona o empresa) en un mismo estado.

 */
class UsuarioViewModel : ViewModel() { // ViewModel que gestiona la captura, validación y envío simulado del formulario de registro

    // Estado interno mutable y estado expuesto de solo lectura para la UI
    private val _estado = MutableStateFlow(UsuarioUiState()) // Estado mutable privado con los datos y errores del formulario
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow() // Flujo observable público de solo lectura para la interfaz de usuario

    // Corrutina del envío simulado; se cancela si el formulario se reinicia
    private var envioJob: Job? = null // Referencia a la tarea asíncrona de envío para permitir su cancelación si se reinicia el flujo

    /** Cambia el tipo de formulario; lo escrito en cada tipo se conserva. */
    fun onTipoChange(tipo: TipoRegistro) { // Alterna entre el formulario de Persona y el de Empresa
        _estado.update { it.copy(tipo = tipo) } // Actualiza la propiedad tipo en el estado de la UI
    }

    // Validación en tiempo real: si un campo ya tenía error, se vuelve a validar
    // mientras el usuario escribe y el mensaje desaparece apenas queda correcto.

    // --- Campos de persona ---

    fun onNombreChange(valor: String) = cambiarPersona { it.copy(nombre = valor) } // Actualiza el nombre del usuario

    fun onApellidoChange(valor: String) = cambiarPersona { it.copy(apellido = valor) } // Actualiza el apellido del usuario

    fun onRunChange(valor: String) = cambiarPersona { it.copy(run = formatearRut(valor)) } // Actualiza y da formato al RUN de la persona

    fun onCorreoChange(valor: String) = cambiarPersona { it.copy(correo = valor) } // Actualiza el correo de la persona

    fun onTelefonoChange(valor: String) = // Actualiza el teléfono celular filtrando solo dígitos hasta el máximo permitido
        cambiarPersona { it.copy(telefono = soloDigitos(valor, DIGITOS_CELULAR)) }

    fun onMotivoChange(valor: String) = cambiarPersona { it.copy(motivo = valor) } // Actualiza el motivo de contacto seleccionado

    fun onFrecuenciaChange(valor: String) = cambiarPersona { it.copy(frecuenciaDonacion = valor) } // Actualiza la frecuencia de donación

    // --- Campos de empresa ---

    fun onRazonSocialChange(valor: String) = cambiarEmpresa { it.copy(razonSocial = valor) } // Actualiza la razón social de la empresa

    fun onRutEmpresaChange(valor: String) = cambiarEmpresa { it.copy(rut = formatearRut(valor)) } // Actualiza y da formato al RUT de la empresa

    fun onGiroChange(valor: String) = cambiarEmpresa { it.copy(giro = valor) } // Actualiza el giro comercial de la empresa

    fun onRepresentanteChange(valor: String) = cambiarEmpresa { it.copy(representante = valor) } // Actualiza el nombre del representante legal

    fun onRutRepresentanteChange(valor: String) = // Actualiza y da formato al RUT del representante legal
        cambiarEmpresa { it.copy(rutRepresentante = formatearRut(valor)) }

    fun onCorreoEmpresaChange(valor: String) = cambiarEmpresa { it.copy(correo = valor) } // Actualiza el correo corporativo de la empresa

    fun onTelefonoEmpresaChange(valor: String) = // Actualiza el teléfono de la empresa filtrando solo dígitos
        cambiarEmpresa { it.copy(telefono = soloDigitos(valor, DIGITOS_TELEFONO_EMPRESA)) }

    fun onDireccionEmpresaChange(valor: String) = cambiarEmpresa { it.copy(direccion = valor) } // Actualiza la dirección de la empresa

    /** Guarda la oportunidad elegida y deja el formulario vacío para un registro nuevo. */
    fun seleccionarOportunidad(titulo: String) { // Asigna la oportunidad seleccionada y resetea el formulario
        envioJob?.cancel() // Cancela cualquier corrutina de envío en curso
        _estado.value = UsuarioUiState(oportunidad = titulo) // Asigna un nuevo estado base asociando el título de la oportunidad
    }

    /** Valida el formulario del tipo elegido, actualiza sus errores y retorna true si es válido. */
    fun validarFormulario(): Boolean { // Ejecuta la validación exhaustiva de los campos según el tipo de registro activo
        val actual = _estado.value // Obtiene el estado actual

        return when (actual.tipo) {
            TipoRegistro.PERSONA -> { // Flujo de validación para Persona
                val errores = erroresPersona(actual.persona) // Evalúa los errores de cada campo de persona
                _estado.update { it.copy(errores = it.errores.copy(persona = errores)) } // Asigna la estructura de errores calculada al estado

                listOfNotNull( // Verifica que no exista ningún mensaje de error en la lista
                    errores.nombre,
                    errores.apellido,
                    errores.run,
                    errores.correo,
                    errores.telefono,
                    errores.motivo,
                    errores.frecuencia
                ).isEmpty() // Retorna true si la lista de errores está totalmente vacía
            }

            TipoRegistro.EMPRESA -> { // Flujo de validación para Empresa
                val errores = erroresEmpresa(actual.empresa) // Evalúa los errores de cada campo de empresa
                _estado.update { it.copy(errores = it.errores.copy(empresa = errores)) } // Asigna la estructura de errores calculada al estado

                listOfNotNull( // Verifica que no exista ningún mensaje de error en la lista
                    errores.razonSocial,
                    errores.rut,
                    errores.giro,
                    errores.representante,
                    errores.rutRepresentante,
                    errores.correo,
                    errores.telefono,
                    errores.direccion
                ).isEmpty() // Retorna true si la lista de errores está totalmente vacía
            }
        }
    }

    /**
    - Arma el aporte que se guarda para el Dashboard. Retorna null si el registro no es un aporte

    - (por ejemplo, una persona que solo quiere ingresar a un programa).

     */
    fun construirRegistro(ahora: Long = System.currentTimeMillis()): RegistroGuardado? { // Construye la entidad de datos para persistir en DataStore
        val actual = _estado.value // Lee el estado actual
        if (!actual.esAporte) return null // Si no califica como un aporte económico o empresarial, no genera objeto de registro

        return when (actual.tipo) {
            TipoRegistro.PERSONA -> { // Mapea los datos de persona a un RegistroGuardado
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

            TipoRegistro.EMPRESA -> { // Mapea los datos de empresa a un RegistroGuardado
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
    fun enviarRegistro() { // Ejecuta el proceso de envío asíncrono con progreso incremental
        if (_estado.value.enviando) return // Evita ejecuciones duplicadas si ya se está enviando

        envioJob = viewModelScope.launch { // Inicia corrutina de envío
            _estado.update { it.copy(enviando = true, progreso = 0f) } // Establece el flag enviando en true e inicia el progreso en 0

            for (paso in 1..PASOS_ENVIO) { // Itera progresivamente simulando la transferencia de datos
                delay(PAUSA_PASO_MS) // Retardo entre cada paso
                _estado.update { it.copy(progreso = paso / PASOS_ENVIO.toFloat()) } // Actualiza el porcentaje de progreso (0.0 a 1.0)
            }

            _estado.update { it.copy(enviando = false, completado = true) } // Finaliza la simulación marcando enviando en false y completado en true
        }
    }

    /** La pantalla ya navegó: se apaga el aviso para no repetir la navegación. */
    fun confirmarNavegacion() { // Limpia la bandera de finalización de envío tras procesar la navegación
        _estado.update { it.copy(completado = false) }
    }

    /** Deja el formulario vacío (Lo usamos al finalizar el registro). */
    fun reiniciar() { // Cancela tareas activas y reestablece el estado del formulario a su valor por defecto
        envioJob?.cancel()
        _estado.value = UsuarioUiState()
    }

    // Aplica el cambio a los datos de persona y revalida solo los campos que ya tenían error
    private fun cambiarPersona(cambio: (DatosPersona) -> DatosPersona) { // Función auxiliar para actualizar persona y revalidar en tiempo real si existían errores previas
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
    private fun cambiarEmpresa(cambio: (DatosEmpresa) -> DatosEmpresa) { // Función auxiliar para actualizar empresa y revalidar en tiempo real si existían errores previas
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

    private fun erroresPersona(p: DatosPersona) = ErroresPersona( // Genera la evaluación completa de errores para el formulario de persona
        nombre = validarNombre(p.nombre),
        apellido = validarApellido(p.apellido),
        run = validarRut(p.run, "RUN"),
        correo = validarCorreo(p.correo),
        telefono = validarTelefono(p.telefono, DIGITOS_CELULAR, "celular"),
        motivo = validarMotivo(p.motivo),
        frecuencia = validarFrecuencia(p)
    )

    private fun erroresEmpresa(e: DatosEmpresa) = ErroresEmpresa( // Genera la evaluación completa de errores para el formulario de empresa
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
    private fun soloDigitos(valor: String, maximo: Int): String = // Filtra caracteres no numéricos y recorta al tamaño indicado
        valor.filter { it.isDigit() }.take(maximo)

    // --- Reglas de validación por campo ---

    private fun validarNombre(valor: String): String? = when { // Regla de validación para el nombre
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarApellido(valor: String): String? = when { // Regla de validación para el apellido
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 2 -> "Debe tener al menos 2 caracteres"
        else -> null
    }

    // [nombre] es "RUN" para personas y "RUT" para empresas y representantes
    private fun validarRut(valor: String, nombre: String): String? = when { // Regla de validación para RUT / RUN chileno
        valor.isBlank() -> "Campo obligatorio"
        !esRutValido(valor) -> "$nombre inválido"
        else -> null
    }

    private fun validarCorreo(valor: String): String? = // Regla de validación de formato de correo mediante expresión regular
        if (!REGEX_CORREO.matches(valor.trim())) "Correo inválido" else null

    private fun validarTelefono(valor: String, digitos: Int, tipo: String): String? = when { // Regla de validación de longitud de teléfono
        valor.isBlank() -> "Campo obligatorio"
        valor.length != digitos -> "Ingresa los $digitos dígitos del $tipo"
        else -> null
    }

    private fun validarMotivo(valor: String): String? = // Regla de validación para selección de motivo
        if (valor.isBlank()) "Selecciona un motivo" else null

    // La frecuencia solo se pide cuando el motivo es "Aportar"
    private fun validarFrecuencia(p: DatosPersona): String? = // Regla de validación condicional para frecuencia de aporte
        if (p.motivo == MOTIVO_APORTAR && p.frecuenciaDonacion.isBlank()) {
            "Selecciona la frecuencia"
        } else null

    private fun validarRazonSocial(valor: String): String? = when { // Regla de validación para razón social
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarGiro(valor: String): String? = when { // Regla de validación para giro comercial
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarRepresentante(valor: String): String? = when { // Regla de validación para nombre del representante legal
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    private fun validarDireccion(valor: String): String? = when { // Regla de validación para dirección de la empresa
        valor.isBlank() -> "Campo obligatorio"
        valor.trim().length < 5 -> "Ingresa una dirección completa"
        else -> null
    }

    private companion object { // Objeto compañero con expresiones regulares y constantes de configuración
        val REGEX_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") // Expresión regular estándar para correos electrónicos

        // Celular: +569 y 8 dígitos. Teléfono de empresa: +56 y 9 dígitos (celular o fijo)
        const val DIGITOS_CELULAR = 8 // Constante de longitud para celulares de personas
        const val DIGITOS_TELEFONO_EMPRESA = 9 // Constante de longitud para teléfonos de empresa

        // El envío simulado dura 20 pasos de 100 ms (unos 2 segundos)
        const val PASOS_ENVIO = 20 // Cantidad de iteraciones en la simulación de envío
        const val PAUSA_PASO_MS = 100L // Retardo en milisegundos por iteración
    }
}