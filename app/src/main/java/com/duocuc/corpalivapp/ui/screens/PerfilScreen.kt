package com.duocuc.corpalivapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.data.model.RolUsuario
import com.duocuc.corpalivapp.data.model.UsuarioSesion
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.components.ImagenInteligente
import com.duocuc.corpalivapp.ui.theme.AzulSeleccion
import com.duocuc.corpalivapp.ui.viewmodel.PerfilViewModel
import com.duocuc.corpalivapp.util.crearUriFoto
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * GUÍA 13 - PARTE 4: PANTALLA DE PERFIL
 *
 * Integra dos recursos nativos con la API:
 * - Galería: GetContent()
 * - Cámara: TakePicture() + FileProvider (requiere el permiso CAMERA, que se solicita).
 * Si el usuario cancela cualquiera de las dos acciones, la app no se cae.
 * G13: es el perfil de la cuenta con sesión iniciada: muestra su información general, el badge con su rol,
 * la opción Dashboard (solo administradores) y el botón para cerrar sesión.
 */
@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    sesion: UsuarioSesion,
    onDashboard: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Fecha y hora en que se inició la sesión
    val inicioSesion = remember(sesion.inicio) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")).format(Date(sesion.inicio))
    }

    // Uri donde la cámara guardará la foto; se conserva si se rota la pantalla.
    var uriCamaraPendiente by rememberSaveable { mutableStateOf<Uri?>(null) }

    fun avisar(mensaje: String) {
        scope.launch { snackbarHostState.showSnackbar(mensaje) }
    }

    // Lanzador de galería: devuelve null si el usuario cancela
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) viewModel.actualizarDesdeGaleria(uri)
    }

    // Lanzador de cámara: devuelve true solo si el usuario aceptó la foto
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        val uri = uriCamaraPendiente
        if (exito && uri != null) viewModel.actualizarDesdeCamara(uri)
        uriCamaraPendiente = null
    }

    fun abrirCamara() {
        try {
            val uri = crearUriFoto(context)
            uriCamaraPendiente = uri
            camaraLauncher.launch(uri)
        } catch (e: Exception) {
            avisar("No se pudo abrir la cámara en este dispositivo")
        }
    }

    // Lanzador del permiso de la cámara
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) abrirCamara() else avisar("Permiso de cámara denegado")
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Mi perfil", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagenInteligente(uri = imagenUri)

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de la cuenta y, justo debajo, el badge con su rol
            Text(
                text = sesion.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = AzulSeleccion
            ) {
                Text(
                    text = sesion.rol.etiqueta,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información general de la cuenta
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Información general",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    DatoCuenta("Correo", sesion.correo)
                    DatoCuenta("Rol", sesion.rol.etiqueta)
                    DatoCuenta("Organización", "CORPALIV")
                    DatoCuenta("Sesión iniciada", inicioSesion)
                }
            }

            // Solo los administradores ven la opción para revisar los datos de los usuarios
            if (sesion.rol == RolUsuario.ADMINISTRADOR) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDashboard,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dashboard")
                }
                Text(
                    text = "Revisa los datos de los usuarios que aportaron.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (imagenUri == null) "Aún no has elegido una foto" else "Tu foto de perfil",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { galeriaLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Elegir de la galería")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val permisoConcedido = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (permisoConcedido) {
                        abrirCamara()
                    } else {
                        permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto con la cámara")
            }

            if (imagenUri != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { viewModel.quitarImagen() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Quitar foto")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onCerrarSesion) {
                Text("Cerrar sesión")
            }
        }
    }
}

/** Fila "Etiqueta: valor" de la información de la cuenta. */
@Composable
private fun DatoCuenta(etiqueta: String, valor: String) {
    Row {
        Text(
            text = "$etiqueta: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}
