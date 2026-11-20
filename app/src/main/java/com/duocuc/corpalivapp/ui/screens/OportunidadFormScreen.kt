package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.activity.compose.BackHandler // Intercepta el botón atrás del sistema mientras se guarda
import androidx.compose.foundation.layout.Arrangement // Importación para definir el espaciado entre elementos de una columna
import androidx.compose.foundation.layout.Column // Contenedor vertical de los campos del formulario
import androidx.compose.foundation.layout.consumeWindowInsets // Marca como consumido el margen que reservó el Scaffold
import androidx.compose.foundation.layout.fillMaxSize // Modificador para ocupar todo el espacio disponible
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para ocupar todo el ancho disponible
import androidx.compose.foundation.layout.imePadding // Sube el contenido cuando aparece el teclado virtual
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes
import androidx.compose.foundation.rememberScrollState // Estado de desplazamiento del formulario
import androidx.compose.foundation.text.KeyboardOptions // Opciones del teclado virtual (tipo de teclado)
import androidx.compose.foundation.verticalScroll // Permite desplazar el formulario cuando no cabe en pantalla
import androidx.compose.material3.Button // Botón principal de Material 3
import androidx.compose.material3.ExperimentalMaterial3Api // Anotación de API experimental de Material 3
import androidx.compose.material3.LinearProgressIndicator // Barra de progreso mientras se guarda
import androidx.compose.material3.MaterialTheme // Acceso a los colores y estilos del tema
import androidx.compose.material3.OutlinedTextField // Campo de texto con borde
import androidx.compose.material3.Scaffold // Estructura base con barra superior
import androidx.compose.material3.Text // Componente de texto
import androidx.compose.runtime.Composable // Anotación que identifica las funciones de UI en Jetpack Compose
import androidx.compose.runtime.LaunchedEffect // Ejecuta una acción cuando cambia un valor observado
import androidx.compose.runtime.getValue // Delegado de propiedad para leer estados en Compose
import androidx.compose.ui.Modifier // Modificador base de los composables
import androidx.compose.ui.text.input.KeyboardType // Tipos de teclado virtual (texto, número, correo)
import androidx.compose.ui.unit.dp // Unidad de medida en densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un StateFlow respetando el ciclo de vida
import com.duocuc.corpalivapp.ui.components.AppTopBar // Barra superior estándar con botón de retorno
import com.duocuc.corpalivapp.ui.viewmodel.OportunidadFormViewModel // ViewModel con la lógica del formulario

/**
- PARCIAL 4: FORMULARIO DE OPORTUNIDADES (solo administradores).

- Sirve para crear una oportunidad nueva y para modificar una existente. Al guardar con éxito vuelve al listado.

 */
@OptIn(ExperimentalMaterial3Api::class) // Permite usar la barra superior de Material 3
@Composable
fun OportunidadFormScreen( // Pantalla del formulario de oportunidades
    viewModel: OportunidadFormViewModel, // ViewModel compartido que entrega el estado y recibe los cambios
    onBack: () -> Unit = {}, // Callback para volver al listado sin guardar
    onGuardado: () -> Unit = {} // Callback que se ejecuta cuando el servidor guardó la oportunidad
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle() // Observa el estado del formulario respetando el ciclo de vida

    LaunchedEffect(estado.guardado) { // Cuando el servidor confirma el guardado, se vuelve al listado
        if (estado.guardado) {
            viewModel.confirmarGuardado() // Apaga la bandera para no repetir la navegación
            onGuardado() // Vuelve al listado, que se recarga solo
        }
    }

    BackHandler(enabled = estado.guardando) { } // Mientras se guarda, el botón atrás del sistema no hace nada

    Scaffold( // Estructura base con barra superior
        topBar = {
            AppTopBar( // Barra superior con el título según el modo
                titulo = if (estado.esEdicion) "Editar oportunidad" else "Nueva oportunidad",
                onBack = { if (!estado.guardando) onBack() } // No se puede salir mientras se guarda
            )
        }
    ) { innerPadding ->
        Column( // Columna desplazable con todos los campos
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding() // El teclado no tapa los campos
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoOportunidad( // Título del programa
                valor = estado.titulo, onValorChange = viewModel::onTituloChange,
                etiqueta = "Título", error = estado.errores["titulo"], enabled = !estado.guardando
            )
            CampoOportunidad( // Categoría del programa
                valor = estado.categoria, onValorChange = viewModel::onCategoriaChange,
                etiqueta = "Categoría", error = estado.errores["categoria"], enabled = !estado.guardando,
                ayuda = "Ej: RSE Empresarial, Donación Directa"
            )
            CampoOportunidad( // Descripción del programa
                valor = estado.descripcion, onValorChange = viewModel::onDescripcionChange,
                etiqueta = "Descripción", error = estado.errores["descripcion"], enabled = !estado.guardando,
                variasLineas = true
            )
            CampoOportunidad( // Impacto social del programa
                valor = estado.impactoSocial, onValorChange = viewModel::onImpactoChange,
                etiqueta = "Impacto social", error = estado.errores["impactoSocial"], enabled = !estado.guardando,
                variasLineas = true
            )
            CampoOportunidad( // Meta de financiamiento en pesos
                valor = estado.meta, onValorChange = viewModel::onMetaChange,
                etiqueta = "Meta de financiamiento ($)", error = estado.errores["meta"], enabled = !estado.guardando,
                tipoTeclado = KeyboardType.Number
            )
            CampoOportunidad( // Monto recaudado hasta la fecha
                valor = estado.monto, onValorChange = viewModel::onMontoChange,
                etiqueta = "Monto recaudado ($)", error = estado.errores["monto"], enabled = !estado.guardando,
                tipoTeclado = KeyboardType.Number
            )
            CampoOportunidad( // URL de la imagen (opcional)
                valor = estado.imagenUrl, onValorChange = viewModel::onImagenChange,
                etiqueta = "URL de la imagen (opcional)", error = estado.errores["imagenUrl"], enabled = !estado.guardando,
                tipoTeclado = KeyboardType.Uri
            )
            CampoOportunidad( // Correo de contacto (opcional)
                valor = estado.contactoEmail, onValorChange = viewModel::onCorreoChange,
                etiqueta = "Correo de contacto (opcional)", error = estado.errores["contactoEmail"], enabled = !estado.guardando,
                tipoTeclado = KeyboardType.Email
            )

            estado.errorEnvio?.let { mensaje -> // Aviso cuando el servidor rechazó el guardado o no hubo conexión
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (estado.guardando) { // Barra de progreso mientras se espera al servidor
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Button( // Guarda la oportunidad (crea o modifica según el modo)
                onClick = { viewModel.guardar() },
                enabled = !estado.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (estado.guardando) "Guardando..." else "Guardar")
            }
        }
    }
}

/** Campo de texto del formulario: muestra el mensaje de error debajo si existe (o una ayuda). */
@Composable
private fun CampoOportunidad( // Componente auxiliar para construir campos OutlinedTextField estandarizados
    valor: String, // Valor actual del texto
    onValorChange: (String) -> Unit, // Callback para cambios de texto
    etiqueta: String, // Nombre o label del campo
    error: String?, // Mensaje de error de validación (null si es válido)
    enabled: Boolean, // Control de habilitación (se apaga mientras se guarda)
    ayuda: String? = null, // Texto auxiliar de ayuda
    tipoTeclado: KeyboardType = KeyboardType.Text, // Tipo de teclado virtual
    variasLineas: Boolean = false // true para textos largos (descripción e impacto)
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = error != null, // Estilo de error si hay mensaje
        supportingText = { (error ?: ayuda)?.let { Text(it) } }, // El error tiene prioridad sobre la ayuda
        singleLine = !variasLineas,
        minLines = if (variasLineas) 3 else 1,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = Modifier.fillMaxWidth()
    )
}
