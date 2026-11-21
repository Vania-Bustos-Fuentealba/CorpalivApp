
package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.data.network.ExternalPost
import com.duocuc.corpalivapp.data.repository.ExternalPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExternalPostUiState(
    val publicaciones: List<ExternalPost> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

class ExternalPostViewModel(
    private val repository: ExternalPostRepository =
        ExternalPostRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExternalPostUiState())

    val uiState: StateFlow<ExternalPostUiState> =
        _uiState.asStateFlow()

    init {
        cargarPublicaciones()
    }

    fun cargarPublicaciones() {
        viewModelScope.launch {
            _uiState.value = ExternalPostUiState(
                cargando = true
            )

            try {
                val publicaciones =
                    repository.obtenerPublicaciones()

                _uiState.value = ExternalPostUiState(
                    publicaciones = publicaciones,
                    cargando = false
                )
            } catch (e: Exception) {
                _uiState.value = ExternalPostUiState(
                    cargando = false,
                    error = e.message ?: "Error al cargar publicaciones"
                )
            }
        }
    }
}
