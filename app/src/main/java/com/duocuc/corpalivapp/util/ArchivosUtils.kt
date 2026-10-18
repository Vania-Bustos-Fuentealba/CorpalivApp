package com.duocuc.corpalivapp.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * G13 - ALMACENAMIENTO:
 * Crea un archivo vacío en la caché de la app y devuelve un Uri seguro (content://)
 * para que la cámara guarde ahí la foto. La autoridad debe coincidir con la
 * declarada en AndroidManifest.xml (${applicationId}.fileprovider).
 */
fun crearUriFoto(context: Context): Uri {
    val carpeta = File(context.cacheDir, "fotos").apply { mkdirs() }
    val archivo = File.createTempFile("perfil_", ".jpg", carpeta)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )
}
