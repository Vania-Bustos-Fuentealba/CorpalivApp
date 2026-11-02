package com.duocuc.corpalivapp.util // Declaración del paquete al que pertenece este archivo dentro de la capa de utilidades del proyecto

import android.content.Context // Importación de la clase Context de Android para acceder al directorio de caché y recursos de la app
import android.net.Uri // Importación de la clase Uri para representar direcciones unificadas de recursos
import androidx.core.content.FileProvider // Componente de AndroidX para compartir archivos mediante esquemas de URI seguros (content://)
import java.io.File // Importación de la clase File de Java para la creación y manipulación de archivos físicos en el sistema de almacenamiento

/**
- G13 - ALMACENAMIENTO:

- Crea un archivo vacío en la caché de la app y devuelve un Uri seguro (content://)

- para que la cámara guarde ahí la foto. La autoridad debe coincidir con la

- declarada en AndroidManifest.xml (${applicationId}.fileprovider).

 */
fun crearUriFoto(context: Context): Uri { // Función utilitaria global para generar una Uri temporal destinada a capturas de la cámara
    val carpeta = File(context.cacheDir, "fotos").apply { mkdirs() } // Crea o recupera el directorio "fotos" en la memoria caché interna de la app
    val archivo = File.createTempFile("perfil_", ".jpg", carpeta) // Genera un archivo temporal único con prefijo "perfil_" y extensión ".jpg" dentro de la carpeta
    return FileProvider.getUriForFile( // Genera y retorna una URI segura del tipo content:// utilizando el FileProvider configurado
        context, // Contexto de la aplicación
        "${context.packageName}.fileprovider", // Autoridad del FileProvider basada dinámicamente en el ID del paquete de la app
        archivo // Archivo físico previamente creado para el cual se solicita el URI
    )
}
