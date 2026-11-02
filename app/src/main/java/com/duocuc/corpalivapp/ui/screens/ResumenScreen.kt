package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.activity.compose.BackHandler // Interceptor del evento/botón de retroceso del sistema en Compose
import androidx.compose.animation.core.Animatable // Clase para gestionar animaciones de valores numéricos de forma imperativa
import androidx.compose.animation.core.LinearEasing // Curva de animación con velocidad constante/lineal
import androidx.compose.animation.core.tween // Especificación de animación basada en duración en milisegundos
import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución y espaciado de los elementos
import androidx.compose.foundation.layout.Column // Contenedor de diseño vertical Column
import androidx.compose.foundation.layout.Spacer // Componente Spacer para insertar espacios vacíos ajustables
import androidx.compose.foundation.layout.fillMaxSize // Modificador para expandir el tamaño al 100% de la pantalla disponible
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.height // Modificador para definir una altura fija a un componente
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o espacios internos (padding)
import androidx.compose.foundation.layout.size // Modificador para asignar dimensiones cuadradas de ancho y alto
import androidx.compose.foundation.rememberScrollState // Función para crear y recordar el estado de desplazamiento
import androidx.compose.foundation.verticalScroll // Modificador que habilita el desplazamiento vertical en un contenedor
import androidx.compose.material.icons.Icons // Objeto contenedor global de íconos de Material Design
import androidx.compose.material.icons.filled.CheckCircle // Ícono de círculo con marca de verificación de éxito
import androidx.compose.material3.Button // Componente de botón principal de Material 3
import androidx.compose.material3.Card // Componente de tarjeta de Material 3 para agrupar contenido
import androidx.compose.material3.CardDefaults // Valores y configuraciones predeterminadas para tarjetas
import androidx.compose.material3.Icon // Componente Icon de Material 3 para renderizar vectores gráficos
import androidx.compose.material3.LinearProgressIndicator // Componente de barra de progreso lineal
import androidx.compose.material3.MaterialTheme // Acceso a la paleta de colores, tipografía y formas del tema Material
import androidx.compose.material3.Scaffold // Estructura base de pantalla que soporta la barra superior
import androidx.compose.material3.Text // Componente Text de Material 3 para renderizar cadenas de texto
import androidx.compose.material3.TextButton // Botón plano de solo texto para acciones secundarias
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de UI en Jetpack Compose
import androidx.compose.runtime.LaunchedEffect // Efecto de Compose que ejecuta corrutinas al entrar en la composición
import androidx.compose.runtime.getValue // Delegado de propiedad para la lectura simplificada de estados en Compose
import androidx.compose.runtime.remember // Función para memorizar objetos a través de recomposiciones
import androidx.compose.runtime.rememberUpdatedState // Memoriza y actualiza la referencia de un valor para efectos de larga duración
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores
import androidx.compose.ui.Modifier // Modificador de UI base para configurar el aspecto y comportamiento de composables
import androidx.compose.ui.semantics.LiveRegionMode // Enumeración para configurar el modo de anuncio en lectores de pantalla (TalkBack)
import androidx.compose.ui.semantics.liveRegion // Propiedad semántica de accesibilidad que anuncia cambios de contenido en voz alta
import androidx.compose.ui.semantics.semantics // Modificador para asignar propiedades de accesibilidad semántica
import androidx.compose.ui.text.font.FontWeight // Propiedad para especificar el peso de la tipografía (Bold, Normal, etc.)
import androidx.compose.ui.text.style.TextAlign // Importación para especificar la alineación del texto
import androidx.compose.ui.unit.dp // Unidad de medida de densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un StateFlow de forma consciente del ciclo de vida
import com.duocuc.corpalivapp.data.model.MOTIVO_APORTAR // Constante que identifica el motivo de donación/aporte
import com.duocuc.corpalivapp.data.model.TipoRegistro // Enum con los tipos de formulario (PERSONA o EMPRESA)
import com.duocuc.corpalivapp.data.model.motivosRegistro // Lista de opciones con descripciones y agradecimientos de motivos
import com.duocuc.corpalivapp.ui.components.AppTopBar // Componente de barra superior reutilizable
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel // ViewModel que comparte el estado del usuario recién registrado

/**
 * G11: PANTALLA DE RESUMEN
 * Observa el MISMO UsuarioViewModel que RegistroScreen y muestra los datos ingresados sin recibir argumentos por la ruta.
 * Agradece el apoyo o la postulación según el motivo elegido (o el interés de la empresa) e informa que la institución contactará a la persona.
 * G12: si el registro es un aporte, muestra por 5 segundos el mensaje de agradecimiento y vuelve solo al inicio.
 */
@Composable
fun ResumenScreen( // Pantalla de confirmación y resumen del registro completado
    viewModel: UsuarioViewModel, // ViewModel compartido con la pantalla de registro
    onFinalizar: () -> Unit = {} // Callback para concluir el flujo y regresar a la pantalla de Inicio
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle() // Observa el estado del registro respetando el ciclo de vida
    val persona = estado.persona // Referencia rápida a los datos de persona
    val empresa = estado.empresa // Referencia rápida a los datos de empresa
    val esEmpresa = estado.tipo == TipoRegistro.EMPRESA // Evalúa si el tipo de registro fue de empresa

    // Se guarda al abrir la pantalla: al finalizar el formulario se limpia y la vista no debe cambiar
    val esAporte = remember { estado.esAporte } // Memoriza si el registro corresponde a una donación económica

    // Las empresas no eligen motivo: solo las personas tienen agradecimiento según su motivo
    val motivo = if (esEmpresa) null else motivosRegistro.find { it.etiqueta == persona.motivo } // Obtiene las plantillas de texto de la persona

    // El formulario ya se envió: volver atrás termina el flujo y lleva al inicio
    BackHandler { onFinalizar() } // Intercepta el botón de retroceso para ejecutar el callback de finalización

    Scaffold( // Estructura contenedor base de la pantalla
        topBar = { AppTopBar(titulo = "Registro completado", onBack = onFinalizar) } // Barra superior con título y acción de finalización
    ) { innerPadding -> // Recibe el espacio de reserva superior
        if (esAporte) { // Evalúa si el registro procesado es una donación directa
            MensajeAporte( // Muestra la vista con temporizador automático de 5 segundos para aportes
                onFinalizar = onFinalizar, // Pasa la lambda para finalizar
                modifier = Modifier
                    .fillMaxSize() // Ocupa toda la superficie de la pantalla
                    .padding(innerPadding) // Aplica el padding asignado por el Scaffold
            )
        } else {
            Column( // Contenedor vertical desplazable con el resumen estándar
                modifier = Modifier
                    .fillMaxSize() // Expande a la pantalla completa
                    .padding(innerPadding) // Aplica el padding del Scaffold
                    .verticalScroll(rememberScrollState()) // Activa el desplazamiento vertical
                    .padding(16.dp), // Margen general de 16dp
                verticalArrangement = Arrangement.spacedBy(16.dp), // Espaciado constante de 16dp entre elementos
                horizontalAlignment = Alignment.CenterHorizontally // Centra todos los elementos horizontalmente
            ) {
                Icon( // Ícono gráfico de éxito
                    imageVector = Icons.Default.CheckCircle, // Vector de círculo con check positivo
                    contentDescription = null, // Sin descripción de contenido por ser decorativo
                    tint = MaterialTheme.colorScheme.primary, // Aplica el color primario del tema
                    modifier = Modifier.size(96.dp) // Define un tamaño destacado de 96dp
                )

                Text( // Título principal de agradecimiento
                    text = if (esEmpresa) { // Evalúa el mensaje según el tipo de participante
                        "¡Gracias por tu interés en colaborar!"
                    } else {
                        motivo?.agradecimiento ?: "¡Gracias!"
                    },
                    style = MaterialTheme.typography.headlineMedium, // Estilo de titular mediano
                    fontWeight = FontWeight.Bold, // Texto en negrita
                    textAlign = TextAlign.Center // Texto centrado
                )

                val confirmacion = if (esEmpresa) { // Construye el texto de confirmación formateado
                    "Registramos la solicitud de «${empresa.razonSocial}» para colaborar con «${estado.oportunidad}»."
                } else {
                    motivo?.confirmacion?.format(estado.oportunidad)
                }
                confirmacion?.let { // Renderiza condicionalmente el texto de confirmación si no es nulo
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge, // Estilo de cuerpo grande
                        textAlign = TextAlign.Center // Texto centrado
                    )
                }

                // Aviso de contacto de la institución
                Text( // Texto destacado notificando el tiempo y canal de respuesta
                    text = "Serás contactado/a por la institución en un día hábil, vía WhatsApp o teléfono.",
                    style = MaterialTheme.typography.titleMedium, // Estilo de título mediano
                    color = MaterialTheme.colorScheme.primary, // Color primario para resaltado
                    textAlign = TextAlign.Center // Texto centrado
                )

                Card( // Tarjeta contenedora con la síntesis de los datos proporcionados
                    modifier = Modifier.fillMaxWidth(), // Ancho completo
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Elevación visual de 4dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp), // Margen interno de 16dp
                        verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado de 8dp entre filas de datos
                    ) {
                        Text( // Encabezado de la tarjeta
                            text = "Resumen de tu registro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (esEmpresa) { // Muestra los datos específicos de la empresa
                            Text("Razón social: ${empresa.razonSocial}")
                            Text("Representante legal: ${empresa.representante}")
                            Text("Correo: ${empresa.correo}")
                            Text("Teléfono: +56 ${empresa.telefono}")
                            Text("Oportunidad: ${estado.oportunidad}")
                        } else { // Muestra los datos de la persona
                            Text("Nombre: ${persona.nombre} ${persona.apellido}")
                            Text("Correo: ${persona.correo}")
                            Text("Teléfono: +569 ${persona.telefono}")
                            Text("Oportunidad: ${estado.oportunidad}")
                            Text("Motivo: ${persona.motivo}")
                            if (persona.motivo == MOTIVO_APORTAR) { // Si el motivo fue Aportar, muestra la frecuencia elegida
                                Text("Frecuencia de la donación: ${persona.frecuenciaDonacion}")
                            }
                        }
                    }
                }

                Button( // Botón principal para cerrar la pantalla y volver al inicio
                    onClick = onFinalizar, // Llama al callback para limpiar y volver
                    modifier = Modifier.fillMaxWidth() // Ancho completo
                ) {
                    Text("Volver al inicio") // Etiqueta del botón
                }
            }
        }
    }
}

// Mensaje que se muestra al confirmar un aporte (dura 5 segundos)
private const val MENSAJE_APORTE = // Constante con el texto de agradecimiento para donaciones
    "Gracias por tu aporte, ayudarás a la causa que elegiste y harás que este mundo pueda dar un lugar a más personas"
private const val DURACION_MENSAJE_MS = 5_000 // Constante con el tiempo de espera en milisegundos (5000ms)

/**
 * G12: mensaje de agradecimiento de un aporte. La barra avanza durante 5 segundos
 * y al terminar se vuelve al inicio; el botón permite volver antes.
 */
@Composable
private fun MensajeAporte( // Composable privado para la pantalla de agradecimiento temporal de donaciones
    onFinalizar: () -> Unit, // Callback para volver al inicio al expirar el tiempo
    modifier: Modifier = Modifier // Modificador de diseño
) {
    val avance = remember { Animatable(0f) } // Estado animable para la barra de progreso (0.0f a 1.0f)
    val finalizar by rememberUpdatedState(onFinalizar) // Mantiene la referencia más reciente del callback onFinalizar

    LaunchedEffect(Unit) { // Inicia el temporizador de 5 segundos de forma asíncrona
        avance.animateTo( // Anima la barra del 0% al 100% de forma constante
            targetValue = 1f,
            animationSpec = tween(durationMillis = DURACION_MENSAJE_MS, easing = LinearEasing) // Duración exacta de 5000ms
        )
        finalizar() // Llama al callback para navegar al inicio al completar los 5 segundos
    }

    Column( // Contenedor vertical centrado desplazable
        modifier = modifier
            .verticalScroll(rememberScrollState()) // Permite el desplazamiento si la pantalla es pequeña
            .padding(24.dp), // Margen general de 24dp
        verticalArrangement = Arrangement.Center, // Centra verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
    ) {
        Icon( // Ícono de éxito destacado
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp)
        )

        Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical de 24dp

        // liveRegion: los lectores de pantalla anuncian el mensaje al aparecer
        Text( // Texto destacado de agradecimiento
            text = MENSAJE_APORTE,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite } // Anuncio accesible para lectores de pantalla
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical de 16dp

        // Aviso de contacto de la institución
        Text( // Texto informativo sobre el contacto posterior
            text = "Serás contactado/a por la institución en un día hábil, vía WhatsApp o teléfono.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp)) // Espacio vertical de 32dp

        LinearProgressIndicator( // Indicador visual de la barra de progreso de 5 segundos
            progress = { avance.value }, // Lee el valor de la animación en tiempo real
            modifier = Modifier.fillMaxWidth() // Ancho completo
        )
        Text( // Texto informativo debajo de la barra
            text = "Volviendo al inicio…",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        TextButton(onClick = onFinalizar) { // Permite omitir la espera e ir al inicio inmediatamente
            Text("Ir al inicio ahora") // Etiqueta del botón de omisión
        }
    }
}