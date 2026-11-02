package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.compose.animation.AnimatedContent // Componente de animación que conmuta contenido con transiciones personalizadas
import androidx.compose.animation.AnimatedVisibility // Componente para animar la entrada y salida visual de composables
import androidx.compose.animation.animateColorAsState // Transición animada para cambios de color según el estado
import androidx.compose.animation.animateContentSize // Modificador que anima los cambios de tamaño de un contenedor
import androidx.compose.animation.core.Spring // Constantes de física de resortes para especificaciones de animación (bouncy, stiffness)
import androidx.compose.animation.core.animateFloatAsState // Transición animada para valores de tipo Float (escalas, opacidades)
import androidx.compose.animation.core.spring // Crea una especificación de animación basada en física de resortes
import androidx.compose.animation.core.tween // Especificación de animación basada en tiempo (duración en milisegundos)
import androidx.compose.animation.fadeIn // Animación de aparición con desvanecimiento (opacidad de 0 a 1)
import androidx.compose.animation.fadeOut // Animación de desaparición con desvanecimiento (opacidad de 1 a 0)
import androidx.compose.animation.scaleIn // Animación de entrada con escalamiento progresivo
import androidx.compose.animation.scaleOut // Animación de salida con reducción de escala progresiva
import androidx.compose.animation.slideInVertically // Animación de entrada con desplazamiento vertical
import androidx.compose.animation.slideOutVertically // Animación de salida con desplazamiento vertical
import androidx.compose.animation.togetherWith // Combina transiciones de entrada y salida para AnimatedContent
import androidx.compose.foundation.background // Modificador para aplicar un fondo de color o gradiente
import androidx.compose.foundation.layout.Arrangement // Importación para definir la distribución y espaciado de elementos
import androidx.compose.foundation.layout.Box // Contenedor Box para apilar o alinear elementos en el centro
import androidx.compose.foundation.layout.Column // Contenedor de diseño vertical Column
import androidx.compose.foundation.layout.Spacer // Componente Spacer para insertar espacios vacíos ajustables
import androidx.compose.foundation.layout.fillMaxSize // Modificador para expandir el tamaño al 100% de la pantalla
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.height // Modificador para fijar la altura de un componente
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o espacios de relleno interno
import androidx.compose.foundation.layout.size // Modificador para fijar dimensiones cuadradas de ancho y alto
import androidx.compose.foundation.shape.CircleShape // Forma circular para recortar superficies y fondos
import androidx.compose.material.icons.Icons // Objeto contenedor de íconos vectoriales de Material Design
import androidx.compose.material.icons.filled.Check // Ícono de verificación/check para el estado activo
import androidx.compose.material.icons.filled.Close // Ícono de cruz/cerrar para el estado inactivo
import androidx.compose.material3.Button // Componente Button de Material 3 para la acción principal
import androidx.compose.material3.ButtonDefaults // Configuración de colores y propiedades por defecto para botones
import androidx.compose.material3.Card // Componente de tarjeta de Material 3 para agrupar contenido descriptivo
import androidx.compose.material3.CircularProgressIndicator // Componente indicador de carga circular
import androidx.compose.material3.Icon // Componente Icon de Material 3 para desplegar vectores gráficos
import androidx.compose.material3.MaterialTheme // Acceso a la paleta de colores, tipografía y formas del tema Material
import androidx.compose.material3.Scaffold // Estructura base de pantalla que sostiene la barra superior y el contenido
import androidx.compose.material3.Text // Componente Text de Material 3 para renderizar cadenas de texto
import androidx.compose.material3.TextButton // Componente de botón plano de solo texto para acciones secundarias
import androidx.compose.runtime.Composable // Anotación que identifica la función como un componente de UI en Jetpack Compose
import androidx.compose.runtime.derivedStateOf // Optimización para recalcular valores derivados solo cuando cambia una dependencia
import androidx.compose.runtime.getValue // Delegado de propiedad para lectura directa del estado en Compose
import androidx.compose.runtime.mutableStateOf // Crea un estado mutable observable por Compose
import androidx.compose.runtime.remember // Función para memorizar valores entre recomposiciones del mismo composable
import androidx.compose.runtime.saveable.rememberSaveable // Memoriza valores resistiendo recreaciones de la actividad/rotaciones de pantalla
import androidx.compose.runtime.setValue // Delegado de propiedad para escritura directa del estado en Compose
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores
import androidx.compose.ui.Modifier // Modificador de UI base de Jetpack Compose
import androidx.compose.ui.draw.scale // Modificador para aplicar una escala gráfica al componente
import androidx.compose.ui.graphics.Color // Clase para representar y manipular colores gráficos
import androidx.compose.ui.text.style.TextAlign // Importación para especificar la alineación horizontal del texto
import androidx.compose.ui.unit.dp // Unidad de medida de densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un StateFlow de forma consciente del ciclo de vida
import androidx.lifecycle.viewmodel.compose.viewModel // Extensión para inyectar u obtener ViewModels en composables
import com.duocuc.corpalivapp.ui.components.AppTopBar // Componente de barra superior reutilizable con botón de retorno
import com.duocuc.corpalivapp.ui.viewmodel.EstadoViewModel // ViewModel que administra el estado persistente del Modo Especial

private val ColorActivo = Color(0xFF4CAF50) // Constante de color verde para representar el estado activado
private val ColorInactivo = Color(0xFFB0BEC5) // Constante de color gris neutro para representar el estado desactivado

/**
- G12: ESTADOS Y ANIMACIONES

 */
@Composable
fun ModoEspecialScreen( // Pantalla principal de la funcionalidad de Modo Especial con estados y animaciones de Compose
    onBack: () -> Unit = {}, // Lambda de callback ejecutada al presionar el botón de regresar
    viewModel: EstadoViewModel = viewModel() // Instancia del ViewModel inyectada por defecto
) {
    val activo by viewModel.activo.collectAsStateWithLifecycle() // Observa el estado del modo especial (true, false o null)
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsStateWithLifecycle() // Observa si se debe desplegar el mensaje de éxito

    // Estado local de UI que sobrevive a rotaciones
    var detallesVisibles by rememberSaveable { mutableStateOf(false) } // Estado local para alternar la visibilidad de la tarjeta de detalles

    // Textos derivados: se recalculan solo cuando cambia 'activo'.
    val textoEstado by remember { // Memoriza el texto del estado basándose en 'activo'
        derivedStateOf { if (activo == true) "Modo especial ACTIVADO" else "Modo especial DESACTIVADO" }
    }
    val textoBoton by remember { // Memoriza el texto de la etiqueta del botón basándose en 'activo'
        derivedStateOf { if (activo == true) "Desactivar" else "Activar" }
    }

    Scaffold( // Estructura contenedor base
        topBar = { AppTopBar(titulo = "Modo especial", onBack = onBack) } // Barra superior con título "Modo especial" y botón de retorno
    ) { innerPadding -> // Recibe el padding de margen reservado por el Scaffold
        val estadoActual = activo // Variable auxiliar para el chequeo de smart-cast

        if (estadoActual == null) { // Evalúa si la preferencia aún no se ha leído desde DataStore
            // Simulación de carga inicial antes de mostrar la UI principal
            Box( // Contenedor centrado para la pantalla de carga inicial
                modifier = Modifier
                    .fillMaxSize() // Ocupa toda la pantalla
                    .padding(innerPadding), // Respeta el espacio reservado por la barra superior
                contentAlignment = Alignment.Center // Centra el indicador de carga
            ) {
                Column( // Alinea verticalmente el indicador y el texto descriptivo
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado de 12dp
                ) {
                    CircularProgressIndicator() // Muestra la rueda animada de progreso
                    Text("Cargando preferencias…") // Texto informativo de carga
                }
            }
        } else {
            val colorAnimado by animateColorAsState( // Anima la transición del color de fondo entre verde y gris
                targetValue = if (estadoActual) ColorActivo else ColorInactivo, // Define el color destino según el estado
                animationSpec = tween(durationMillis = 500), // Duración de la animación en 500ms
                label = "colorModo" // Etiqueta identificadora para herramientas de inspección
            )
            val escalaIcono by animateFloatAsState( // Anima el factor de escala del contenedor para un efecto de rebote
                targetValue = if (estadoActual) 1.15f else 1f, // Aplica un 15% de ampliación al estar activo
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), // Aplica física de resorte con rebote medio
                label = "escalaIcono" // Etiqueta identificadora de animación
            )

            Column( // Contenedor vertical principal que sostiene todos los controles e indicadores
                modifier = Modifier
                    .fillMaxSize() // Ocupa toda la pantalla
                    .padding(innerPadding) // Aplica el padding del Scaffold
                    .padding(32.dp) // Margen externo general de 32dp
                    .animateContentSize(), // Anima suavemente los cambios de tamaño dentro del contenedor
                verticalArrangement = Arrangement.Center, // Centra el contenido verticalmente en la pantalla
                horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
            ) {
                // Indicador circular: cambia de color y alterna icono
                Box( // Círculo visual principal con color animado e ícono
                    modifier = Modifier
                        .size(120.dp) // Define un diámetro fijo de 120dp
                        .scale(escalaIcono) // Aplica la escala animada de resorte
                        .background(colorAnimado, CircleShape), // Aplica el color animado recortado en círculo
                    contentAlignment = Alignment.Center // Centra el ícono dentro del círculo
                ) {
                    AnimatedContent( // Realiza una transición fluida al conmutar entre el ícono de Check y Close
                        targetState = estadoActual, // Estado objetivo a observar
                        transitionSpec = { // Define la especificación combinada de desvanecimiento y escalamiento
                            (fadeIn(tween(300)) + scaleIn(initialScale = 0.6f)) togetherWith
                                    (fadeOut(tween(300)) + scaleOut(targetScale = 0.6f))
                        },
                        label = "iconoModo" // Etiqueta identificadora de animación
                    ) { encendido -> // Lambda con el estado evaluado actual
                        Icon( // Renderiza el ícono vectorial correspondiente
                            imageVector = if (encendido) Icons.Default.Check else Icons.Default.Close, // Selecciona Check o Close
                            contentDescription = if (encendido) "Modo activado" else "Modo desactivado", // Descripción de accesibilidad
                            tint = Color.White, // Color del ícono en blanco
                            modifier = Modifier.size(64.dp) // Asigna un tamaño de 64dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical de 24dp

                Text( // Texto que informa el estado actual (ACTIVADO / DESACTIVADO)
                    text = textoEstado, // Utiliza la variable derivada memorizada
                    style = MaterialTheme.typography.titleLarge, // Estilo tipográfico para título grande
                    textAlign = TextAlign.Center // Texto centrado
                )

                Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical de 24dp

                Button( // Botón de acción para conmutar el estado del modo especial
                    onClick = { viewModel.alternarEstado() }, // Invocación del método para alternar el estado en el ViewModel
                    colors = ButtonDefaults.buttonColors(containerColor = colorAnimado), // Asigna el color dinámico animado al botón
                    modifier = Modifier
                        .fillMaxWidth() // Ancho completo
                        .height(60.dp) // Altura de 60dp para facilitar el toque táctil
                ) {
                    Text(text = textoBoton, style = MaterialTheme.typography.titleLarge) // Muestra el texto "Activar" o "Desactivar"
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical de 16dp

                // Mensaje de éxito cada vez que el estado se guarda.
                AnimatedVisibility( // Muestra u oculta con animación el texto de confirmación
                    visible = mostrarMensaje, // Condición de visibilidad leída desde el ViewModel
                    enter = fadeIn(tween(300)) + slideInVertically { it / 2 }, // Animación de entrada: desvanecimiento + desplazamiento hacia arriba
                    exit = fadeOut(tween(300)) + slideOutVertically { it / 2 } // Animación de salida: desvanecimiento + desplazamiento hacia abajo
                ) {
                    Text( // Texto de confirmación
                        text = "¡Estado guardado exitosamente!", // Mensaje de éxito al guardar
                        color = ColorActivo, // Texto en verde activo
                        style = MaterialTheme.typography.bodyLarge // Estilo tipográfico de cuerpo grande
                    )
                }

                TextButton(onClick = { detallesVisibles = !detallesVisibles }) { // Botón plano para conmutar la visibilidad de la tarjeta informativa
                    Text(if (detallesVisibles) "Ocultar detalles" else "Ver detalles") // Alterna el texto del botón
                }

                AnimatedVisibility(visible = detallesVisibles) { // Muestra u oculta la tarjeta explicativa con animación por defecto
                    Card(modifier = Modifier.fillMaxWidth()) { // Tarjeta contenedora con ancho completo
                        Text( // Texto explicativo sobre la arquitectura de persistencia
                            text = "Esta preferencia se guarda en el dispositivo con DataStore " +
                                    "Preferences, por lo que se mantiene aunque cierres la app.", // Explicación técnica de DataStore
                            style = MaterialTheme.typography.bodyMedium, // Estilo tipográfico medio
                            modifier = Modifier.padding(16.dp) // Padding interno de 16dp dentro de la tarjeta
                        )
                    }
                }
            }
        }
    }
}