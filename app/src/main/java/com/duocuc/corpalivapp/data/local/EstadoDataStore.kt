package com.duocuc.corpalivapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// G12: Se declara el DataStore con el nombre 'preferencias_usuario'.
private val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

/**
 * El valor sobrevive aunque se cierre la app o reinicie el dispositivo.
 */
class EstadoDataStore(private val context: Context) {

    // Clave usada para guardar el estado
    private val estadoActivado = booleanPreferencesKey("modo_activado")

    /** Guarda el estado (activado o desactivado). */
    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[estadoActivado] = valor
        }
    }

    /** Es null si no se guarda nada. */
    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[estadoActivado]
        }
    }
}
