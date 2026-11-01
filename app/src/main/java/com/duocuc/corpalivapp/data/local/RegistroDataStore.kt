package com.duocuc.corpalivapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.duocuc.corpalivapp.data.model.RegistroGuardado
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// G12: segundo DataStore de la app (cada archivo debe tener un nombre distinto).
private val Context.registrosStore by preferencesDataStore(name = "registros_aportes")

/**
 * Guarda los aportes en el dispositivo como una lista JSON dentro de DataStore.
 * Así sobreviven al cierre de la app y el Dashboard los puede mostrar.
 */
class RegistroDataStore(private val context: Context) {

    private val clave = stringPreferencesKey("aportes")
    private val gson = Gson()
    private val tipoLista = object : TypeToken<List<RegistroGuardado>>() {}.type

    /** Agrega un aporte al inicio de la lista (los más recientes primero). */
    suspend fun guardar(registro: RegistroGuardado) {
        context.registrosStore.edit { preferencias ->
            val actuales = leer(preferencias[clave])
            preferencias[clave] = gson.toJson(listOf(registro) + actuales)
        }
    }

    /** Lista de aportes como Flow reactivo: el Dashboard se actualiza solo. */
    fun obtenerTodos(): Flow<List<RegistroGuardado>> {
        return context.registrosStore.data.map { preferencias -> leer(preferencias[clave]) }
    }

    // Si el JSON está vacío o dañado se devuelve una lista vacía
    private fun leer(json: String?): List<RegistroGuardado> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching { gson.fromJson<List<RegistroGuardado>>(json, tipoLista) }
            .getOrNull() ?: emptyList()
    }
}
