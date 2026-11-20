package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import androidx.lifecycle.ViewModel // Importación de la clase base ViewModel de Android Jetpack
import androidx.lifecycle.viewModelScope // Alcance de corrutinas acotado al ciclo de vida del ViewModel
import com.duocuc.corpalivapp.data.model.OportunidadModel // Importación del modelo de oportunidad (se usa para editar una existente)
import com.duocuc.corpalivapp.data.model.OportunidadRequestDto // Importación del cuerpo de la petición para crear o actualizar
import com.duocuc.corpalivapp.data.repository.OportunidadesRepository // Repositorio que guarda la oportunidad en el servidor
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto a la vista
import kotlinx.coroutines.flow.asStateFlow // Transforma un MutableStateFlow en un StateFlow inmutable
import kotlinx.coroutines.flow.update // Función de extensión para actualizar el estado de forma atómica
import kotlinx.coroutines.launch // Extensión para iniciar corrutinas asíncronas

/**
- PARCIAL 4: estado del formulario de oportunidades (crear y editar).

- La meta y el monto se guardan como texto (solo dígitos) y se convierten a número al enviar.

 */
data class OportunidadFormUiState( // Data class con todo el estado de la pantalla del formulario
    val idEdicion: Int? = null, // Identificador de la oportunidad que se edita (null cuando se crea una nueva)
    val titulo: String = "", // Título del programa o proyecto
    val categoria: String = "", // Categoría del programa
    val descripcion: String = "", // Descripción de los objetivos
    val impactoSocial: String = "", // Impacto social generado
    val imagenUrl: String = "", // URL opcional de la imagen
    val contactoEmail: String = "", // Correo de contacto opcional
    val meta: String = "", // Meta de financiamiento (solo dígitos)
    val monto: String = "0", // Monto recaudado (solo dígitos)
    val errores: Map<String, String> = emptyMap(), // Mensajes de error de validación por campo
    val guardando: Boolean = false, // true mientras se espera la respuesta del servidor
    val errorEnvio: String? = null, // Mensaje cuando el servidor rechazó el guardado o no hubo conexión
    val guardado: Boolean = false // true cuando el servidor guardó la oportunidad (la pantalla vuelve al listado)
) {
    val esEdicion: Boolean // Propiedad calculada: indica si se está editando en lugar de crear
        get() = idEdicion != null // Hay un identificador solo cuando se edita una oportunidad existente
}

/** PARCIAL 4: lógica del formulario de oportunidades. Valida los campos y los envía a oportunidades-service. */
class OportunidadFormViewModel( // ViewModel compartido a nivel de la Activity (igual que UsuarioViewModel)
    private val repositorio: OportunidadesRepository = OportunidadesRepository() // Repositorio de oportunidades (real o falso en pruebas)
) : ViewModel() {

    private val _estado = MutableStateFlow(OportunidadFormUiState()) // Estado mutable privado del formulario
    val estado: StateFlow<OportunidadFormUiState> = _estado.asStateFlow() // Flujo observable de solo lectura para la interfaz

    /** Deja el formulario vacío para crear una oportunidad nueva. */
    fun nueva() { // Se llama antes de abrir el formulario desde el botón "Nueva oportunidad"
        _estado.value = OportunidadFormUiState() // Estado inicial sin identificador de edición
    }

    /** Carga los datos de una oportunidad existente para modificarla. */
    fun editar(oportunidad: OportunidadModel) { // Se llama antes de abrir el formulario desde el botón "Editar"
        _estado.value = OportunidadFormUiState( // Copia los datos de la oportunidad al formulario
            idEdicion = oportunidad.id,
            titulo = oportunidad.titulo,
            categoria = oportunidad.categoria,
            descripcion = oportunidad.descripcion,
            impactoSocial = oportunidad.impactoSocial,
            imagenUrl = oportunidad.imagenUrl.orEmpty(), // Un valor nulo se muestra como campo vacío
            contactoEmail = oportunidad.contactoEmail.orEmpty(),
            meta = oportunidad.metaFinanciamiento.toString(),
            monto = oportunidad.montoRecaudado.toString()
        )
    }

    // Al escribir en un campo se borra su mensaje de error
    fun onTituloChange(valor: String) = cambiar { it.copy(titulo = valor, errores = it.errores - "titulo") } // Actualiza el título

    fun onCategoriaChange(valor: String) = cambiar { it.copy(categoria = valor, errores = it.errores - "categoria") } // Actualiza la categoría

    fun onDescripcionChange(valor: String) = cambiar { it.copy(descripcion = valor, errores = it.errores - "descripcion") } // Actualiza la descripción

    fun onImpactoChange(valor: String) = cambiar { it.copy(impactoSocial = valor, errores = it.errores - "impactoSocial") } // Actualiza el impacto social

    fun onImagenChange(valor: String) = cambiar { it.copy(imagenUrl = valor, errores = it.errores - "imagenUrl") } // Actualiza la URL de la imagen

    fun onCorreoChange(valor: String) = cambiar { it.copy(contactoEmail = valor, errores = it.errores - "contactoEmail") } // Actualiza el correo de contacto

    fun onMetaChange(valor: String) = // Actualiza la meta dejando solo dígitos (máximo 9 para no desbordar un Int)
        cambiar { it.copy(meta = valor.filter { c -> c.isDigit() }.take(MAX_DIGITOS), errores = it.errores - "meta") }

    fun onMontoChange(valor: String) = // Actualiza el monto recaudado dejando solo dígitos
        cambiar { it.copy(monto = valor.filter { c -> c.isDigit() }.take(MAX_DIGITOS), errores = it.errores - "monto") }

    /** Valida los campos con los mismos límites que el servidor. Retorna un mapa campo -> mensaje (vacío si todo está bien). */
    fun validar(datos: OportunidadFormUiState): Map<String, String> { // Reglas de validación del formulario
        val errores = mutableMapOf<String, String>() // Acumula los mensajes de error por campo

        when { // Título: obligatorio, de 3 a 200 caracteres
            datos.titulo.isBlank() -> errores["titulo"] = "Campo obligatorio"
            datos.titulo.trim().length < 3 -> errores["titulo"] = "Debe tener al menos 3 caracteres"
            datos.titulo.trim().length > 200 -> errores["titulo"] = "Máximo 200 caracteres"
        }
        when { // Categoría: obligatoria, hasta 80 caracteres
            datos.categoria.isBlank() -> errores["categoria"] = "Campo obligatorio"
            datos.categoria.trim().length > 80 -> errores["categoria"] = "Máximo 80 caracteres"
        }
        when { // Descripción: obligatoria, hasta 1000 caracteres
            datos.descripcion.isBlank() -> errores["descripcion"] = "Campo obligatorio"
            datos.descripcion.trim().length > 1000 -> errores["descripcion"] = "Máximo 1000 caracteres"
        }
        when { // Impacto social: obligatorio, hasta 500 caracteres
            datos.impactoSocial.isBlank() -> errores["impactoSocial"] = "Campo obligatorio"
            datos.impactoSocial.trim().length > 500 -> errores["impactoSocial"] = "Máximo 500 caracteres"
        }
        if (datos.imagenUrl.isNotBlank()) { // La imagen es opcional, pero si se escribe debe ser una dirección web
            val url = datos.imagenUrl.trim()
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                errores["imagenUrl"] = "Debe comenzar con http:// o https://"
            } else if (url.length > 500) {
                errores["imagenUrl"] = "Máximo 500 caracteres"
            }
        }
        if (datos.contactoEmail.isNotBlank() && !REGEX_CORREO.matches(datos.contactoEmail.trim())) { // El correo es opcional, pero si se escribe debe ser válido
            errores["contactoEmail"] = "Correo inválido"
        }
        if ((datos.meta.toIntOrNull() ?: 0) <= 0) { // La meta debe ser un número mayor a 0
            errores["meta"] = "Ingresa una meta mayor a 0"
        }
        return errores // Mapa vacío = formulario válido
    }

    /** Valida y envía la oportunidad: la crea si es nueva o la modifica si se está editando. */
    fun guardar() { // Se llama al pulsar "Guardar"
        val actual = _estado.value // Lee el estado actual del formulario
        if (actual.guardando) return // Evita envíos duplicados mientras hay uno en curso

        val errores = validar(actual) // Revisa todos los campos
        if (errores.isNotEmpty()) { // Si hay errores se muestran y no se envía nada
            _estado.update { it.copy(errores = errores, errorEnvio = null) }
            return
        }

        val solicitud = OportunidadRequestDto( // Arma el cuerpo de la petición con los textos limpios
            titulo = actual.titulo.trim(),
            categoria = actual.categoria.trim(),
            descripcion = actual.descripcion.trim(),
            impactoSocial = actual.impactoSocial.trim(),
            imagenUrl = actual.imagenUrl.trim().ifEmpty { null }, // Un campo vacío no se envía
            contactoEmail = actual.contactoEmail.trim().ifEmpty { null },
            metaFinanciamiento = actual.meta.toInt(), // Ya validado: es un número mayor a 0
            montoRecaudado = actual.monto.toIntOrNull() ?: 0 // Un monto vacío se envía como 0
        )

        _estado.update { it.copy(errores = emptyMap(), guardando = true, errorEnvio = null) } // Marca el envío en curso
        viewModelScope.launch { // Corrutina: la petición de red no bloquea la interfaz
            val idEdicion = actual.idEdicion // Identificador de la oportunidad editada (null si es nueva)
            val resultado = if (idEdicion == null) { // Crear o modificar según corresponda
                repositorio.crear(solicitud)
            } else {
                repositorio.actualizar(idEdicion, solicitud)
            }
            resultado
                .onSuccess { _ -> // El servidor guardó la oportunidad
                    _estado.update { it.copy(guardando = false, guardado = true) }
                }
                .onFailure { error -> // El servidor rechazó el guardado o no hubo conexión: se queda en el formulario
                    _estado.update { it.copy(guardando = false, errorEnvio = error.message ?: "No se pudo guardar la oportunidad") }
                }
        }
    }

    /** La pantalla ya volvió al listado: se apaga el aviso para no repetir la navegación. */
    fun confirmarGuardado() { // Limpia la bandera de guardado tras procesar la navegación
        _estado.update { it.copy(guardado = false) }
    }

    private fun cambiar(transformar: (OportunidadFormUiState) -> OportunidadFormUiState) { // Aplica un cambio al estado de forma atómica
        _estado.update(transformar)
    }

    private companion object { // Constantes del formulario
        val REGEX_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") // Misma expresión que el formulario de registro
        const val MAX_DIGITOS = 9 // Largo máximo de los montos para que quepan en un Int
    }
}
