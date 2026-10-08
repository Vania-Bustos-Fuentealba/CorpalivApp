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
 * COMPONENTE UI: OportunidadCard
 *
 * Integración de Requerimientos de la GUÍA 2 (DSY1105):
 * 1. Control de Flujo: Muestra el nivel de prioridad calculado mediante 'when'.
 * 2. Operadores Aritméticos: Despliega métricas financieras (avance % y faltante $).
 * 3. Null Safety: Presenta la información de contacto formateada sin riesgo de NullPointerException.
 */
@Composable
fun OportunidadCard(
    oportunidad: OportunidadModel,
    modifier: Modifier = Modifier
) {
    Card(
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
                // Categoría de la iniciativa (RSE Empresarial / Donación Directa)
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

                // ---------------------------------------------------------------------
                // GUÍA 2 - CONTROL DE FLUJO ('when'):
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

                // ---------------------------------------------------------------------
                // GUÍA 2 - OPERADORES ARITMÉTICOS Y VARIABLES NUMÉRICAS:
                // Muestra la meta (Double), el porcentaje calculado (%) mediante
                // multiplicación y división, y el saldo faltante ($) mediante resta.
                // ---------------------------------------------------------------------
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

                // ---------------------------------------------------------------------
                // GUÍA 2 - NULL SAFETY (Llamada segura ?. y valor por defecto):
                // Invoca obtenerContactoSeguro() para asegurar que si 'contactoEmail'
                // viene nulo desde la API, se renderice un correo seguro sin crashear.
                // ---------------------------------------------------------------------
                Text(
                    text = "Contacto: ${oportunidad.obtenerContactoSeguro()}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botón de interacción para empresas o donantes
                Button(
                    onClick = { /* Acción de contacto / vinculación */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Conectar / Vincularse")
                }
            }
        }
    }
}