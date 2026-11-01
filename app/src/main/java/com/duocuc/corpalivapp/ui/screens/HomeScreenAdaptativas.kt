package com.duocuc.corpalivapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duocuc.corpalivapp.navigation.Screen
import com.duocuc.corpalivapp.ui.components.AppDrawerContent
import com.duocuc.corpalivapp.ui.components.MarkdownEnBloques
import com.duocuc.corpalivapp.util.obtenerWindowSizeClass
import kotlinx.coroutines.launch
import kotlin.OptIn

// 1. VISTA COMPACTA
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(
    contenido: String? = TEXTO_MUESTRA,
    onMenuClick: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CORPALIV Mobile", fontWeight = FontWeight.Bold) },
                navigationIcon = { BotonMenu(onMenuClick) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InformacionCorpaliv(contenido)
        }
    }
}

// ======================================================================================
// 2. VISTA MEDIANA (Tablets pequeñas / Dispositivos plegables)
// ======================================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana(
    contenido: String? = TEXTO_MUESTRA,
    onMenuClick: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CORPALIV - Vista Tablet / Plegable", fontWeight = FontWeight.Bold) },
                navigationIcon = { BotonMenu(onMenuClick) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding ->
        // El contenido se centra y no se estira más de 720 dp para que el texto sea legible
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InformacionCorpaliv(contenido)
            }
        }
    }
}

// ======================================================================================
// 3. VISTA EXPANDIDA (Tablets en horizontal / Pantallas grandes / Escritorio)
// ======================================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida(contenido: String? = TEXTO_MUESTRA) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CORPALIV - Panel de Administración Expandido", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            )
        }
    ) { innerPadding ->
        // Contenido centrado, con un ancho máximo para que el texto sea legible
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.widthIn(max = 960.dp).padding(32.dp)) {
                InformacionCorpaliv(contenido)
            }
        }
    }
}

// Información de Corpaliv en bloques; mientras el texto carga se muestra un indicador
@Composable
private fun InformacionCorpaliv(contenido: String?) {
    if (contenido == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        MarkdownEnBloques(markdown = contenido)
    }
}

// Texto de ejemplo para los @Preview (en la app el texto sale de assets/institucion/corpaliv.md)
private const val TEXTO_MUESTRA =
    "# CORPALIV\n\nCorporación de Padres y Amigos por el Limitado Visual.\n\n" +
        "## Misión\n\nAcompañar el desarrollo de niños, niñas, jóvenes y adultos con discapacidad múltiple.\n\n" +
        "## Visión\n\nUn país sin barreras para la discapacidad múltiple.\n\n" +
        "## Valores\n\n- Compromiso\n- Respeto\n- Empatía\n- Solidaridad"

// ======================================================================================
// 4. CONTENEDOR ADAPTATIVO PRINCIPAL (Guía 9 + Guía 10)
// ======================================================================================

/**
 * GUÍA 10 - Botón de menú (ícono de hamburguesa) de la TopAppBar.
 * Si [onMenuClick] es null (menú permanente en pantallas grandes) no se muestra nada.
 */
@Composable
private fun BotonMenu(onMenuClick: (() -> Unit)?) {
    if (onMenuClick != null) {
        IconButton(onClick = onMenuClick) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Abrir menú")
        }
    }
}

/**
 * GUÍA 10 - NavigationDrawer MODAL (Compact y Medium): el menú se abre desde
 * el ícono de la TopAppBar o deslizando desde el borde izquierdo.
 */
@Composable
private fun HomeConMenuModal(
    onNavigate: (Screen) -> Unit,
    fotoPerfil: Uri?,
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawerContent(
                    selected = Screen.Home,
                    fotoPerfil = fotoPerfil,
                    onItemClick = { destino ->
                        scope.launch { drawerState.close() }
                        onNavigate(destino)
                    }
                )
            }
        }
    ) {
        contenido { scope.launch { drawerState.open() } }
    }
}

/**
 * Contenedor adaptativo de la pantalla de inicio.
 * - Compact / Medium: menú lateral modal con botón de hamburguesa.
 * - Expanded: menú lateral permanente siempre visible.
 * La navegación se delega en [onNavigate] (MainViewModel -> NavController).
 */
@Composable
fun HomeScreenAdaptive(
    onNavigate: (Screen) -> Unit = {},
    fotoPerfil: Uri? = null,
    contenido: String? = null
) {
    val windowSizeClass = obtenerWindowSizeClass()

    // Evaluador del tamaño de ancho de ventana
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeConMenuModal(onNavigate, fotoPerfil) { abrirMenu ->
            HomeScreenCompacta(contenido = contenido, onMenuClick = abrirMenu)
        }

        WindowWidthSizeClass.Medium -> HomeConMenuModal(onNavigate, fotoPerfil) { abrirMenu ->
            HomeScreenMediana(contenido = contenido, onMenuClick = abrirMenu)
        }

        WindowWidthSizeClass.Expanded -> PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(modifier = Modifier.width(280.dp)) {
                    AppDrawerContent(
                        selected = Screen.Home,
                        fotoPerfil = fotoPerfil,
                        onItemClick = onNavigate
                    )
                }
            }
        ) {
            HomeScreenExpandida(contenido = contenido)
        }
    }
}

// ======================================================================================
// PREVIEWS PARA CADA TAMAÑO DE PANTALLA (Guía 9 - Requerimiento 5)
// ======================================================================================
@Preview(name = "Pantalla Compacta (Smartphone)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewCompact() {
    HomeScreenCompacta()
}

@Preview(name = "Pantalla Mediana (Tablet / Plegable)", widthDp = 700, heightDp = 800, showBackground = true)
@Composable
fun PreviewMedium() {
    HomeScreenMediana()
}

@Preview(name = "Pantalla Expandida (Desktop / Tablet Horizontal)", widthDp = 1000, heightDp = 800, showBackground = true)
@Composable
fun PreviewExpanded() {
    HomeScreenExpandida()
}