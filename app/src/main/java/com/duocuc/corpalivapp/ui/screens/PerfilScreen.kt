package com.duocuc.corpalivapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.components.ImagenInteligente
import com.duocuc.corpalivapp.ui.viewmodel.PerfilViewModel
import com.duocuc.corpalivapp.util.crearUriFoto
import kotlinx.coroutines.launch

/**
 * GUÍA 13 - PARTE 4: PANTALLA DE PERFIL
 *
 * Integra dos recursos nativos con la API:
 * - Galería: GetContent()
 * - Cámara: TakePicture() + FileProvider (requiere el permiso CAMERA, que se solicita).
 * Si el usuario cancela cualquiera de las dos acciones, la app no se cae.
 */
@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagenInteligente(uri = imagenUri)

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
        }
    }
}
