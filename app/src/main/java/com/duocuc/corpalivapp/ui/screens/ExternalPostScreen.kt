
package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duocuc.corpalivapp.ui.viewmodel.ExternalPostViewModel

@Composable
fun ExternalPostScreen(
    viewModel: ExternalPostViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "API externa - Publicaciones",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.cargarPublicaciones() }
        ) {
            Text("Actualizar publicaciones")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            estado.cargando -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            estado.error != null -> {
                Text(
                    text = "Error: ${estado.error}",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                Text(
                    text = "Publicaciones: ${estado.publicaciones.size}"
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(estado.publicaciones) { publicacion ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = publicacion.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(text = publicacion.body)
                            }
                        }
                    }
                }
            }
        }
    }
}
