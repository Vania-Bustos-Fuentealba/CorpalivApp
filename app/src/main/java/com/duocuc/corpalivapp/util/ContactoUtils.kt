package com.duocuc.corpalivapp.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * G13: abre el chat de WhatsApp con [telefono] (con o sin +) y deja escrito el [mensaje].
 * Usa el enlace wa.me: abre la app si está instalada o el navegador si no. No necesita permisos.
 * Retorna false si no se pudo abrir.
 */
fun abrirWhatsApp(context: Context, telefono: String, mensaje: String): Boolean {
    val numero = telefono.filter { it.isDigit() }
    if (numero.isEmpty()) return false

    val enlace = Uri.parse("https://wa.me/$numero?text=${Uri.encode(mensaje)}")
    val intent = Intent(Intent.ACTION_VIEW, enlace).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return try {
        context.startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        false
    }
}
