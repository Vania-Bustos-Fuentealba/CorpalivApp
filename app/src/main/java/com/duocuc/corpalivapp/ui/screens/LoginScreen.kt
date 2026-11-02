package com.duocuc.corpalivapp.ui.screens // Declaración del paquete al que pertenece esta pantalla dentro de la capa de interfaz de usuario (UI)

import androidx.compose.foundation.layout.Arrangement // Importación para especificar la distribución y espaciado de los elementos
import androidx.compose.foundation.layout.Column // Importación del contenedor de diseño vertical Column
import androidx.compose.foundation.layout.consumeWindowInsets // Modificador para consumir insets de ventana y evitar superposiciones
import androidx.compose.foundation.layout.fillMaxSize // Modificador para expandir el tamaño al 100% de la pantalla disponible
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para expandir el ancho al máximo disponible
import androidx.compose.foundation.layout.imePadding // Modificador que desplaza el contenido hacia arriba cuando aparece el teclado en pantalla (IME)
import androidx.compose.foundation.layout.padding // Modificador para aplicar márgenes o espacios de relleno (padding)
import androidx.compose.foundation.layout.size // Modificador para asignar dimensiones fijas a componentes
import androidx.compose.foundation.rememberScrollState // Función para crear y recordar el estado de desplazamiento de una vista
import androidx.compose.foundation.text.KeyboardActions // Importación para definir acciones en las teclas de confirmación del teclado del sistema
import androidx.compose.foundation.text.KeyboardOptions // Configuración del tipo de teclado e interacción (email, password, imeAction, etc.)
import androidx.compose.foundation.verticalScroll // Modificador que habilita el desplazamiento vertical en un contenedor
import androidx.compose.material.icons.Icons // Importación del objeto contenedor global de íconos de Material Design
import androidx.compose.material.icons.filled.Lock // Importación del ícono de candado para representar la seguridad de inicio de sesión
import androidx.compose.material3.Button // Componente de botón estándar de Material 3
import androidx.compose.material3.Icon // Componente Icon de Material 3 para renderizar vectores gráficos
import androidx.compose.material3.MaterialTheme // Acceso a las propiedades del tema (colores, tipografía, formas)
import androidx.compose.material3.OutlinedTextField // Componente de campo de entrada de texto con borde delineado
import androidx.compose.material3.Scaffold // Estructura base de pantalla que gestiona la barra superior
import androidx.compose.material3.Text // Componente Text de Material 3 para renderizar cadenas de texto
import androidx.compose.runtime.Composable // Anotación que identifica la función como un componente de UI en Jetpack Compose
import androidx.compose.runtime.getValue // Delegado para la lectura simplificada del estado de Compose
import androidx.compose.ui.Alignment // Importación para definir alineaciones dentro de contenedores
import androidx.compose.ui.Modifier // Modificador base para alterar la apariencia y comportamiento de composables
import androidx.compose.ui.semantics.LiveRegionMode // Enumeración para configurar la urgencia de lectura en lectores de pantalla (TalkBack)
import androidx.compose.ui.semantics.liveRegion // Propiedad de accesibilidad que anuncia cambios dinámicos de texto en voz alta
import androidx.compose.ui.semantics.semantics // Modificador para asignar propiedades de accesibilidad semántica
import androidx.compose.ui.text.font.FontWeight // Propiedad para especificar el peso de la tipografía (Bold, Normal, etc.)
import androidx.compose.ui.text.input.ImeAction // Acción a mostrar en la tecla de confirmación del teclado (Next, Done, etc.)
import androidx.compose.ui.text.input.KeyboardType // Tipo de entrada de teclado virtual a solicitar (Email, Password, etc.)
import androidx.compose.ui.text.input.PasswordVisualTransformation // Transformación visual para enmascarar contraseñas con puntos
import androidx.compose.ui.text.style.TextAlign // Importación para definir la alineación horizontal del texto
import androidx.compose.ui.unit.dp // Unidad de densidad de píxeles (dp)
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recolecta un StateFlow de forma consciente del ciclo de vida
import com.duocuc.corpalivapp.ui.components.AppTopBar // Componente de barra superior reutilizable con botón de retorno
import com.duocuc.corpalivapp.ui.viewmodel.SesionViewModel // ViewModel que administra el inicio y estado de sesión del usuario

/**
- G13: PANTALLA DE INICIO DE SESIÓN

- Pide el correo y la contraseña de la cuenta de administrador. Al ingresar bien, la pantalla que la

- contiene (perfil o Dashboard) cambia sola a su contenido, porque observa la sesión del SesionViewModel.

 */
@Composable
fun LoginScreen( // Componente composable de la pantalla de inicio de sesión vinculado a SesionViewModel
    viewModel: SesionViewModel, // Instancia del ViewModel que maneja las credenciales y autenticación
    onBack: () -> Unit = {} // Lambda de callback ejecutada al presionar el botón de regresar
) {
    val estado by viewModel.login.collectAsStateWithLifecycle() // Observa el estado del formulario de login respetando el ciclo de vida

    Scaffold( // Estructura contenedor base
        topBar = { AppTopBar(titulo = "Iniciar sesión", onBack = onBack) } // Barra superior con título "Iniciar sesión" y botón de retorno
    ) { innerPadding -> // Recibe el padding asignado por el Scaffold
        Column( // Contenedor vertical principal que agrupa los campos y elementos del formulario
            modifier = Modifier
                .fillMaxSize() // Expande la columna a toda la pantalla
                .padding(innerPadding) // Respeta el área reservada por el Scaffold
                .consumeWindowInsets(innerPadding) // Marca los insets consumidos para evitar superposición de márgenes
                .imePadding() // Añade padding dinámico equivalente a la altura del teclado virtual cuando este emerge
                .verticalScroll(rememberScrollState()) // Permite el desplazamiento vertical si el teclado cubre los campos
                .padding(24.dp), // Aplica un margen general de 24dp alrededor de todo el contenido
            verticalArrangement = Arrangement.spacedBy(12.dp), // Asigna una separación fija de 12dp entre cada componente
            horizontalAlignment = Alignment.CenterHorizontally // Centra todos los componentes horizontalmente
        ) {
            Icon( // Ícono representativo de seguridad
                imageVector = Icons.Default.Lock, // Utiliza el vector de candado
                contentDescription = null, // Sin descripción de contenido ya que es decorativo
                tint = MaterialTheme.colorScheme.primary, // Aplica el color primario del tema
                modifier = Modifier.size(72.dp) // Define un tamaño grande de 72dp
            )

            Text( // Título del formulario
                text = "Acceso de administradores", // Encabezado principal
                style = MaterialTheme.typography.titleLarge, // Estilo tipográfico para título grande
                fontWeight = FontWeight.Bold, // Texto en negrita
                textAlign = TextAlign.Center // Centrado horizontalmente
            )

            Text( // Subtítulo o texto explicativo
                text = "Ingresa con tu cuenta de administrador para ver tu perfil y revisar los aportes registrados.", // Instrucciones de acceso
                style = MaterialTheme.typography.bodyMedium, // Estilo de cuerpo medio
                textAlign = TextAlign.Center // Centrado horizontalmente
            )

            OutlinedTextField( // Campo de entrada de texto para el correo electrónico
                value = estado.correo, // Valor actual del correo provisto por el estado
                onValueChange = viewModel::onCorreoChange, // Notifica cada cambio de texto al ViewModel
                label = { Text("Correo electrónico") }, // Etiqueta flotante del campo
                isError = estado.error != null, // Muestra el campo en estado de error visual si existe un mensaje de falla
                singleLine = true, // Fuerza a que la entrada sea de una sola línea
                keyboardOptions = KeyboardOptions( // Configuración de opciones del teclado
                    keyboardType = KeyboardType.Email, // Solicita un teclado con acceso directo a arroba (@) y dominio
                    imeAction = ImeAction.Next // Configura el botón del teclado para pasar al siguiente campo (Siguiente)
                ),
                modifier = Modifier.fillMaxWidth() // Expande el campo a todo el ancho disponible
            )

            // La contraseña se oculta al escribir; el botón Listo del teclado también ingresa
            OutlinedTextField( // Campo de entrada de texto para la contraseña
                value = estado.clave, // Valor actual de la clave provisto por el estado
                onValueChange = viewModel::onClaveChange, // Notifica los cambios de contraseña al ViewModel
                label = { Text("Contraseña") }, // Etiqueta flotante del campo
                isError = estado.error != null, // Activa el estilo de error visual si hay un fallo
                singleLine = true, // Restringe la entrada a una sola línea
                visualTransformation = PasswordVisualTransformation(), // Enmascara los caracteres con puntos de privacidad
                keyboardOptions = KeyboardOptions( // Configuración del teclado virtual
                    keyboardType = KeyboardType.Password, // Solicita el teclado optimizado para contraseñas
                    imeAction = ImeAction.Done // Asigna la acción de "Listo/Finalizado" a la tecla de confirmación
                ),
                keyboardActions = KeyboardActions(onDone = { viewModel.iniciarSesion() }), // Ejecuta el intento de inicio de sesión al pulsar Listo
                modifier = Modifier.fillMaxWidth() // Expande el campo a todo el ancho
            )

            // liveRegion: los lectores de pantalla anuncian el error al aparecer
            estado.error?.let { mensaje -> // Renderiza condicionalmente el mensaje de error si existe en el estado
                Text(
                    text = mensaje, // Texto con el detalle de la falla de autenticación
                    color = MaterialTheme.colorScheme.error, // Aplica el color de error del tema
                    style = MaterialTheme.typography.bodyMedium, // Estilo tipográfico para cuerpo medio
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa todo el ancho
                        .semantics { liveRegion = LiveRegionMode.Polite } // Anuncia cortésmente el texto al lector de pantalla cuando aparece
                )
            }

            Button( // Botón para autenticarse
                onClick = { viewModel.iniciarSesion() }, // Invoca el método de inicio de sesión del ViewModel al hacer clic
                modifier = Modifier.fillMaxWidth() // Expande el botón a todo el ancho horizontal
            ) {
                Text("Ingresar") // Etiqueta de texto del botón
            }
        }
    }
}