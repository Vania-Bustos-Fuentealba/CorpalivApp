package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.data.model.OportunidadModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Define los 3 estados posibles de la pantalla
sealed interface OportunidadUiState {
    object Loading : OportunidadUiState
    data class Success(val oportunidades: List<OportunidadModel>) : OportunidadUiState
    data class Error(val mensaje: String) : OportunidadUiState
}

class OportunidadViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<OportunidadUiState>(OportunidadUiState.Loading)
    val uiState: StateFlow<OportunidadUiState> = _uiState.asStateFlow()

    init {
        cargarOportunidades()
    }

    fun cargarOportunidades() {
        viewModelScope.launch {
            _uiState.value = OportunidadUiState.Loading
            try {
                // Simulamos tiempo de espera de red (1 segundo)
                delay(1000)

                // Datos sintéticos anonimizados para CORPALIV
                val datosFicticios = listOf(
                    OportunidadModel(
                        id = 1,
                        titulo = "Taller de Preparación Laboral Alba Lab",
                        categoria = "RSE Empresarial",
                        descripcion = "Programa de capacitación en oficios adaptados para jóvenes con discapacidad múltiple.",
                        impactoSocial = "12 participantes capacitados para vinculación laboral este año.",
                        imagenUrl = "https://images.unsplash.com/photo-1531482615713-2afd69097998?w=600",
                        contactoEmail = "rse@corpaliv-demo.cl"
                    ),
                    OportunidadModel(
                        id = 2,
                        titulo = "Equipamiento Escuela Jean Van Dijk",
                        categoria = "Donación Directa",
                        descripcion = "Fondo destinado a la adquisición de material didáctico adaptado y sillas ergométricas.",
                        impactoSocial = "Beneficia directamente a 30 estudiantes en su desarrollo autónomo.",
                        imagenUrl = "https://images.unsplash.com/photo-1509062522246-3755977927d7?w=600",
                        contactoEmail = "donaciones@corpaliv-demo.cl"
                    )
                )

                _uiState.value = OportunidadUiState.Success(datosFicticios)
            } catch (e: Exception) {
                _uiState.value = OportunidadUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }
}