package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duocuc.corpalivapp.data.model.OportunidadModel
import com.duocuc.corpalivapp.ui.components.OportunidadCard
import com.duocuc.corpalivapp.ui.viewmodel.OportunidadUiState
import com.duocuc.corpalivapp.ui.viewmodel.OportunidadViewModel

/**
 * G10 PANTALLA 2: OPORTUNIDADES.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadesScreen(
    onBack: () -> Unit = {},
    onConectar: (OportunidadModel) -> Unit = {},
    viewModel: OportunidadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CORPALIV - Impacto Social") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (val state = uiState) {
                is OportunidadUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is OportunidadUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(state.oportunidades) { oportunidad ->
                            OportunidadCard(
                                oportunidad = oportunidad,
                                onConectar = { onConectar(oportunidad) }
                            )
                        }
                    }
                }
                is OportunidadUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarOportunidades() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}