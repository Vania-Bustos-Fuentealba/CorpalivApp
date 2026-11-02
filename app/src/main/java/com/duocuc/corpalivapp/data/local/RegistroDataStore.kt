package com.duocuc.corpalivapp.data.local // Declaración del paquete al que pertenece este archivo en la capa de datos local

import android.content.Context // Importación del Context de Android necesario para acceder a la caché y almacenamiento del sistema
import androidx.datastore.preferences.core.edit // Importación de la función de suspensión que transacciona la edición en DataStore
import androidx.datastore.preferences.core.stringPreferencesKey // Importación para crear claves de tipo String en Preferences DataStore
import androidx.datastore.preferences.preferencesDataStore // Importación del delegado por propiedad para instanciar DataStore
import com.duocuc.corpalivapp.data.model.RegistroGuardado // Importación del modelo de datos que representa cada registro guardado
import com.google.gson.Gson // Importación de la librería Gson para serialización/deserialización de JSON
import com.google.gson.reflect.TypeToken // Importación de TypeToken para preservar información de tipos genéricos en tiempo de ejecución (List<RegistroGuardado>)
import kotlinx.coroutines.flow.Flow // Importación del tipo Flow para la emisión reactiva asíncrona de listas de aportes
import kotlinx.coroutines.flow.map // Importación del operador de transformación map para flujos de corrutinas

// G12: segundo DataStore de la app (cada archivo debe tener un nombre distinto).
private val Context.registrosStore by preferencesDataStore(name = "registros_aportes") // Extensión de Context que crea el DataStore "registros_aportes" de forma única (singleton)

/**
- Guarda los aportes en el dispositivo como una lista JSON dentro de DataStore.

- Así sobreviven al cierre de la app y el Dashboard los puede mostrar.

 */
class RegistroDataStore(private val context: Context) { // Definición de la clase encargada de persistir la lista de registros en DataStore usando el contexto

    private val clave = stringPreferencesKey("aportes") // Clave de preferencia de tipo String con identificador "aportes" para almacenar el JSON
    private val gson = Gson() // Instancia de Gson usada para convertir listas a cadenas JSON y viceversa
    private val tipoLista = object : TypeToken<List<RegistroGuardado>>() {}.type // Representación genérica del tipo List<RegistroGuardado> requerida por Gson

    /** Agrega un aporte al inicio de la lista (los más recientes primero). */
    suspend fun guardar(registro: RegistroGuardado) { // Función de suspensión para agregar asíncronamente un nuevo registro
        context.registrosStore.edit { preferencias -> // Inicia una transacción de modificación en el DataStore de registros
            val actuales = leer(preferencias[clave]) // Lee y deserializa la lista actual existente en las preferencias (o lista vacía si no hay)
            preferencias[clave] = gson.toJson(listOf(registro) + actuales) // Agrega el nuevo registro al principio de la lista y la serializa a JSON
        } // Finaliza y aplica la transacción en disco
    }

    /** Lista de aportes como Flow reactivo: el Dashboard se actualiza solo. */
    fun obtenerTodos(): Flow<List<RegistroGuardado>> { // Retorna un flujo continuo que emite la lista actualizada de aportes cada vez que cambia
        return context.registrosStore.data.map { preferencias -> leer(preferencias[clave]) } // Lee la cadena de la clave "aportes" y la convierte en List<RegistroGuardado>
    }

    // Si el JSON está vacío o dañado se devuelve una lista vacía
    private fun leer(json: String?): List<RegistroGuardado> { // Función privada auxiliar para deserializar la cadena JSON de forma segura
        if (json.isNullOrBlank()) return emptyList() // Si la cadena es nula o está vacía, retorna directamente una lista vacía
        return runCatching { gson.fromJson<List<RegistroGuardado>>(json, tipoLista) } // Ejecuta la deserialización con Gson dentro de un bloque seguro contra excepciones
            .getOrNull() ?: emptyList() // Retorna la lista deserializada o una lista vacía si ocurrió un error al parsear el JSON
    }
}