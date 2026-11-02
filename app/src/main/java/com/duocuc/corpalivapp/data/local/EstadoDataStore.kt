package com.duocuc.corpalivapp.data.local // Declaración del paquete al que pertenece este archivo dentro de la capa de datos locales

import android.content.Context // Importación de la clase Context de Android para acceder a recursos y al sistema de archivos
import androidx.datastore.preferences.core.booleanPreferencesKey // Importación para crear claves de tipo Booleano en DataStore
import androidx.datastore.preferences.core.edit // Importación de la función de suspensión que permite modificar los datos en DataStore
import androidx.datastore.preferences.preferencesDataStore // Importación del delegado que inicializa la instancia de Preferences DataStore
import kotlinx.coroutines.flow.Flow // Importación de la interfaz Flow para la emisión reactiva y asíncrona de streams de datos
import kotlinx.coroutines.flow.map // Importación del operador de transformación para flujos de corrutinas

// G12: Se declara la propiedad de extensión sobre Context para instanciar el DataStore con el nombre 'preferencias_usuario' de forma singleton
private val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

/**
- El valor sobrevive aunque se cierre la app o reinicie el dispositivo.
 */
class EstadoDataStore(private val context: Context) { // Definición de la clase gestora de la persistencia local que recibe el contexto de la aplicación

    // Definición y creación de la clave 'modo_activado' de tipo Booleano dentro del mapa de claves de DataStore
    private val estadoActivado = booleanPreferencesKey("modo_activado")

    /** Guarda el estado (activado o desactivado). */
    suspend fun guardarEstado(valor: Boolean) { // Función de suspensión que ejecuta I/O asíncrono para guardar un nuevo valor booleano
        context.dataStore.edit { preferencias -> // Abre un bloque de edición transaccional seguro contra hilos en DataStore
            preferencias[estadoActivado] = valor // Asigna el nuevo estado (true o false) a la clave 'modo_activado'
        } // Finaliza la transacción y persiste el cambio en el disco del dispositivo
    }

    /** Es null si no se guarda nada. */
    fun obtenerEstado(): Flow<Boolean?> { // Función que retorna un flujo reactivo Flow que emitirá el valor guardado o null si no se ha configurado
        return context.dataStore.data.map { preferencias -> // Obtiene el flujo de datos raw de DataStore y le aplica una transformación map
            preferencias[estadoActivado] // Extrae y retorna el valor booleano asociado a la clave 'modo_activado' (o null si la clave no existe aún)
        }
    }
}