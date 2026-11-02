package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.compose.foundation.layout.* // Importación genérica de modificadores de diseño (Box, Column, Spacer, padding, fillMaxSize, height, etc.)
import androidx.compose.foundation.lazy.LazyColumn // Importación del contenedor de lista desplazable optimizada LazyColumn
import androidx.compose.foundation.lazy.items // Importación de la función de extensión items para iterar listas dentro de LazyColumn
import androidx.compose.material.icons.Icons // Objeto contenedor de íconos vectoriales de Material Design
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Ícono de flecha de retorno adaptado a la dirección de lectura (AutoMirrored)
import androidx.compose.material3.* // Importación genérica de componentes visuales de Material 3 (Scaffold, TopAppBar, Text, Button, CircularProgressIndicator, etc.)
import androidx.compose.runtime.Composable // Anotación Composable que identifica las funciones de UI en Jetpack Compose
import androidx.compose.runtime.getValue // Delegado de propiedad para la lectura simplificada de estados en Compose
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores
import androidx.compose.ui.Modifier // Modificador base para configurar la apariencia, diseño y comportamiento de composables
import androidx.compose.ui.unit.dp // Unidad de medida en densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un StateFlow de forma consciente del ciclo de vida de la pantalla
import androidx.lifecycle.viewmodel.compose.viewModel // Extensión para inyectar u obtener ViewModels dentro de composables
import com.duocuc.corpalivapp.data.model.OportunidadModel // Importación del modelo de datos de Oportunidad
import com.duocuc.corpalivapp.ui.components.OportunidadCard // Componente de tarjeta reutilizable para renderizar cada oportunidad
import com.duocuc.corpalivapp.ui.viewmodel.OportunidadUiState // Sealed class que representa los estados de UI (Loading, Success, Error)
import com.duocuc.corpalivapp.ui.viewmodel.OportunidadViewModel // ViewModel encargado de gestionar la carga y el estado de las oportunidades

/**
- G10 PANTALLA 2: OPORTUNIDADES.

 */
@OptIn(ExperimentalMaterial3Api::class) // Anotación para permitir el uso de componentes experimentales de Material 3 (TopAppBar)
@Composable
fun OportunidadesScreen( // Pantalla principal del listado de proyectos y oportunidades RSE
    onBack: () -> Unit = {}, // Lambda de callback ejecutada al presionar el botón de regresar
    onConectar: (OportunidadModel) -> Unit = {}, // Lambda de callback ejecutada al hacer clic sobre una oportunidad para vincularse
    viewModel: OportunidadViewModel = viewModel() // Instancia del ViewModel inyectada por defecto
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle() // Observa el estado de la UI en tiempo real respetando el ciclo de vida

    Scaffold( // Estructura contenedora base con barra superior
        topBar = {
            TopAppBar( // Barra superior de navegación de Material 3
                title = { Text("CORPALIV - Impacto Social") }, // Título descriptivo de la barra superior
                navigationIcon = {
                    IconButton(onClick = onBack) { // Botón que ejecuta la acción de volver al presionar
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Ícono de flecha hacia atrás
                            contentDescription = "Volver" // Descripción accesible para lectores de pantalla
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer // Aplica el color del contenedor primario del tema
                )
            )
        }
    ) { paddingValues -> // Recibe las dimensiones del margen asignado por Scaffold
        Box( // Contenedor raíz que sostiene la vista según el estado de la UI
            modifier = Modifier
                .fillMaxSize() // Expande el contenedor a todo el espacio disponible
                .padding(paddingValues) // Aplica el relleno interno provisto por Scaffold
                .padding(horizontal = 16.dp) // Aplica un margen lateral de 16dp
        ) {
            when (val state = uiState) { // Manejo de estados de la UI mediante expresión 'when'
                is OportunidadUiState.Loading -> { // Estado 1: Carga de datos activa
                    CircularProgressIndicator( // Muestra la rueda animada de progreso en el centro
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is OportunidadUiState.Success -> { // Estado 2: Datos cargados exitosamente
                    LazyColumn( // Lista de desplazamiento vertical eficiente
                        modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio del Box
                        contentPadding = PaddingValues(vertical = 12.dp) // Margen interno vertical de 12dp para la lista
                    ) {
                        items(state.oportunidades) { oportunidad -> // Itera sobre la lista de oportunidades cargadas
                            OportunidadCard( // Renderiza el componente de tarjeta para cada oportunidad
                                oportunidad = oportunidad,
                                onConectar = { onConectar(oportunidad) } // Pasa el callback para conectar con la oportunidad seleccionada
                            )
                        }
                    }
                }
                is OportunidadUiState.Error -> { // Estado 3: Ocurrió un error durante la carga
                    Column( // Contenedor vertical centrado para mostrar el error y el botón de reintento
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text( // Muestra el mensaje de error descriptivo
                            text = state.mensaje,
                            color = MaterialTheme.colorScheme.error, // Utiliza el color de error del tema
                            style = MaterialTheme.typography.bodyLarge // Estilo tipográfico grande
                        )
                        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical de 16dp
                        Button(onClick = { viewModel.cargarOportunidades() }) { // Botón para volver a intentar la carga
                            Text("Reintentar") // Texto del botón de reintento
                        }
                    }
                }
            }
        }
    }
}