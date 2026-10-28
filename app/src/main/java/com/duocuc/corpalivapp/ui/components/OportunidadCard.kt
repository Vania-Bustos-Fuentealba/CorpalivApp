package com.duocuc.corpalivapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.duocuc.corpalivapp.data.model.OportunidadModel

/**
 * G2 - COMPONENTE UI: OportunidadCard
 * G10: al tocar la tarjeta o el botón se avisa con [onConectar] para abrir el registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadCard(
    oportunidad: OportunidadModel,
    modifier: Modifier = Modifier,
    onConectar: () -> Unit = {}
) {
    Card(
        onClick = onConectar,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Carga dinámica de imagen con soporte seguro para URLs nulables en Coil
            AsyncImage(
                model = oportunidad.imagenUrl,
                contentDescription = oportunidad.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Categoría de la iniciativa
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = oportunidad.categoria,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Título del programa institucional
                Text(
                    text = oportunidad.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Descripción del proyecto
                Text(
                    text = oportunidad.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Impacto social directo en los beneficiarios de CORPALIV / Escuela Jean Van Dijk
                Text(
                    text = "Impacto: ${oportunidad.impactoSocial}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // G2 - CONTROL DE FLUJO:
                // Muestra la prioridad de vinculación (Ej: "Urgencia Alta", "Fase Final")
                // calculada mediante la expresión 'when' en el modelo de datos.
                // ---------------------------------------------------------------------
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = oportunidad.obtenerPrioridadVinculacion(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // G2 - OPERADORES ARITMÉTICOS Y VARIABLES NUMÉRICAS:
                Text(
                    text = "Meta: $${oportunidad.metaFinanciamiento} | Avance: ${oportunidad.calcularPorcentajeAvance()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Faltante: $${oportunidad.calcularMontoFaltante()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(4.dp))

                // GUÍA 2 - NULL SAFETY:
                // Invoca obtenerContactoSeguro() para asegurar que 'contactoEmail'
                // viene nulo desde la API, Y se renderice un correo seguro sin crashear.
                Text(
                    text = "Contacto: ${oportunidad.obtenerContactoSeguro()}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botón de interacción para empresas o donantes
                Button(
                    onClick = { /* Acción de contacto / vinculación */ onConectar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Conectar / Vincularse")
                }
            }
        }
    }
}