
package com.duocuc.corpalivapp.ui.screens // Ubica este archivo dentro del paquete que agrupa su responsabilidad en la aplicación.

import androidx.compose.foundation.layout.* // Importa los contenedores, espacios y modificadores de distribución de Compose.
import androidx.compose.foundation.lazy.LazyColumn // Importa LazyColumn: muestra una lista vertical creando los elementos conforme se necesitan.
import androidx.compose.foundation.lazy.items // Importa items: agrega a una lista los elementos de una colección.
import androidx.compose.material3.* // Importa textos, botones, tarjetas e indicadores de Material 3.
import androidx.compose.runtime.Composable // Importa Composable: marca una función que describe interfaz con Jetpack Compose.
import androidx.compose.runtime.collectAsState // Importa collectAsState: convierte el flujo del ViewModel en un estado observado por Compose.
import androidx.compose.runtime.getValue // Importa getValue: habilita la lectura delegada con by del estado de Compose.
import androidx.compose.ui.Alignment // Importa Alignment: define cómo alinear los elementos dentro de un contenedor.
import androidx.compose.ui.Modifier // Importa Modifier: encadena cambios de tamaño, separación y aspecto de la interfaz.
import androidx.compose.ui.unit.dp // Importa dp: expresa tamaños en unidades independientes de la densidad de pantalla.
import androidx.lifecycle.viewmodel.compose.viewModel // Importa viewModel: obtiene un ViewModel asociado al propietario actual de la pantalla.
import com.duocuc.corpalivapp.ui.viewmodel.ExternalPostViewModel // Importa ExternalPostViewModel: expone el estado y las acciones de la pantalla de publicaciones.

@Composable // Indica que esta función describe parte de la interfaz Compose.
fun ExternalPostScreen( // Define la pantalla de consulta de publicaciones externas.
    viewModel: ExternalPostViewModel = viewModel() // Obtiene el ViewModel de la pantalla o permite recibir uno desde fuera.
) {
    val estado by viewModel.uiState.collectAsState() // Observa los cambios del flujo para actualizar automáticamente la interfaz.

    Column( // Organiza los componentes hijos verticalmente.
        modifier = Modifier // Comienza la configuración visual del contenedor.
            .fillMaxSize() // Ocupa todo el espacio que permite el contenedor padre.
            .padding(16.dp) // Deja un margen interior de 16 dp alrededor del contenido.
    ) {
        Text( // Muestra un bloque de texto con los parámetros indicados.
            text = "API externa - Publicaciones", // Define el título visible de esta pantalla.
            style = MaterialTheme.typography.headlineSmall // Aplica el estilo de título del tema de la aplicación.
        )

        Spacer(modifier = Modifier.height(12.dp)) // Separa verticalmente las secciones por 12 dp.

        Button( // Crea el botón que solicita una nueva carga.
            onClick = { viewModel.cargarPublicaciones() }, // Delegación del clic al ViewModel, que se encarga de consultar los datos.
            enabled = !estado.cargando // Deshabilita el botón mientras hay una petición pendiente.
        ) {
            Text("Actualizar publicaciones") // Muestra el texto del botón de actualización.
        }

        Spacer(modifier = Modifier.height(16.dp)) // Agrega separación antes de mostrar el resultado de la consulta.

        when { // Elige qué contenido mostrar según el estado actual.
            estado.cargando -> { // Muestra la alternativa de espera mientras llega la respuesta.
                Box( // Permite ubicar el indicador en el centro del área disponible.
                    modifier = Modifier.fillMaxSize(), // Extiende el contenedor a todo el espacio disponible.
                    contentAlignment = Alignment.Center // Centra el contenido horizontal y verticalmente.
                ) {
                    CircularProgressIndicator() // Dibuja el indicador circular de operación en curso.
                }
            }

            estado.error != null -> { // Muestra la alternativa de error cuando existe un mensaje.
                Text( // Muestra un bloque de texto con los parámetros indicados.
                    text = "Error: ${estado.error}", // Incluye el mensaje que preparó el ViewModel.
                    color = MaterialTheme.colorScheme.error // Destaca el error con el color semántico del tema.
                )
            }

            else -> { // Muestra el resultado cuando no hay carga ni error.
                Text( // Muestra un bloque de texto con los parámetros indicados.
                    text = "Publicaciones: ${estado.publicaciones.size}" // Informa cuántas publicaciones se recibieron.
                )

                Spacer(modifier = Modifier.height(8.dp)) // Añade 8 dp de separación vertical.

                LazyColumn( // Presenta una lista desplazable que compone los elementos según se necesitan.
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Separa las tarjetas de la lista por 8 dp.
                ) {
                    items(estado.publicaciones) { publicacion -> // Dibuja una tarjeta para cada publicación recibida.
                        Card( // Agrupa visualmente el título y el contenido de una publicación.
                            modifier = Modifier.fillMaxWidth() // Hace que la tarjeta use todo el ancho disponible.
                        ) {
                            Column( // Organiza los componentes hijos verticalmente.
                                modifier = Modifier.padding(16.dp) // Agrega espacio interior al contenido de la tarjeta.
                            ) {
                                Text( // Muestra un bloque de texto con los parámetros indicados.
                                    text = publicacion.title, // Muestra el título devuelto por la API.
                                    style = MaterialTheme.typography.titleMedium // Aplica el tamaño y peso de un título de tarjeta.
                                )

                                Spacer(modifier = Modifier.height(6.dp)) // Separa el título del cuerpo de la publicación.

                                Text(text = publicacion.body) // Muestra el contenido de la publicación.
                            }
                        }
                    }
                }
            }
        }
    }
}
