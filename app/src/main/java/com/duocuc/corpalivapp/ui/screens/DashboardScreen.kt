package com.duocuc.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.corpalivapp.data.model.RegistroGuardado
import com.duocuc.corpalivapp.data.model.TipoRegistro
import com.duocuc.corpalivapp.ui.components.AppTopBar
import com.duocuc.corpalivapp.ui.theme.coloresChipAzul
import com.duocuc.corpalivapp.ui.viewmodel.DashboardViewModel
import com.duocuc.corpalivapp.ui.viewmodel.FiltroDashboard
import com.duocuc.corpalivapp.util.abrirWhatsApp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * G12: DASHBOARD (administradores)
 * Muestra los aportes registrados (personas que aportan y empresas) con todos los datos que entregaron
 * y un botón para contactarlos por WhatsApp. Los datos se leen del DashboardViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onBack: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { AppTopBar(titulo = "Dashboard", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Aportes registrados",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Totales por tipo de aporte
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TarjetaTotal("Total", estado.totalPersonas + estado.totalEmpresas, Modifier.weight(1f))
                    TarjetaTotal("Personas", estado.totalPersonas, Modifier.weight(1f))
                    TarjetaTotal("Empresas", estado.totalEmpresas, Modifier.weight(1f))
                }
            }

            // Filtros: el elegido se resalta en azul
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FiltroDashboard.entries.forEach { filtro ->
                        val elegido = filtro == estado.filtro

                        FilterChip(
                            selected = elegido,
                            onClick = { viewModel.cambiarFiltro(filtro) },
                            label = {
                                Text(
                                    text = filtro.etiqueta,
                                    fontWeight = if (elegido) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = coloresChipAzul()
                        )
                    }
                }
            }

            when {
                estado.cargando -> item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                estado.registros.isEmpty() -> item {
                    Text(
                        text = "Aún no hay aportes registrados.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(32.dp)
                    )
                }

                else -> items(estado.registros, key = { it.id }) { registro ->
                    TarjetaAporte(
                        registro = registro,
                        onContactar = {
                            val mensaje = "Hola ${registro.nombre}, te escribimos desde CORPALIV por tu " +
                                "registro en «${registro.oportunidad}». ¡Gracias por querer colaborar!"

                            if (!abrirWhatsApp(contexto, registro.telefono, mensaje)) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("No se pudo abrir WhatsApp")
                                }
                            }
                        }
                    )
                }
            }

            item {
                Text(
                    text = "Los aportes se guardan solo en este dispositivo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

/** Número grande con su etiqueta (total, personas o empresas). */
@Composable
private fun TarjetaTotal(
    etiqueta: String,
    valor: Int,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = etiqueta, style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** Un aporte con todos sus datos; las empresas muestran además los datos legales y comerciales. */
@Composable
private fun TarjetaAporte(
    registro: RegistroGuardado,
    onContactar: () -> Unit
) {
    val esEmpresa = registro.tipo == TipoRegistro.EMPRESA.name
    val fecha = remember(registro.id) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")).format(Date(registro.id))
    }

    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = registro.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = if (esEmpresa) "Empresa" else "Persona",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Dato("Oportunidad", registro.oportunidad)
            Dato("Tipo de aporte", registro.tipoAporte)
            Dato(if (esEmpresa) "RUT de la empresa" else "RUN", registro.rut)

            if (esEmpresa) {
                Dato("Giro", registro.giro)
                Dato("Dirección comercial", registro.direccion)
                Dato("Representante legal", registro.representante)
                Dato("RUT del representante", registro.rutRepresentante)
            }

            Dato("Correo", registro.correo)
            Dato("Teléfono", registro.telefono)
            Dato("Registrado", fecha)

            Button(
                onClick = onContactar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Contactar Whatsapp")
            }
        }
    }
}

/** Fila "Etiqueta: valor". */
@Composable
private fun Dato(etiqueta: String, valor: String) {
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
