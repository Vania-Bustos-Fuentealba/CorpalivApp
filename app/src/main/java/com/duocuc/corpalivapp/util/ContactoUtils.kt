package com.duocuc.corpalivapp.util // Declaración del paquete al que pertenece este archivo dentro de la capa de utilidades del proyecto

import android.content.ActivityNotFoundException // Excepción lanzada cuando no existe ninguna aplicación capaz de responder al Intent invocado
import android.content.Context // Importación de la clase Context de Android para iniciar actividades en el sistema
import android.content.Intent // Importación de la clase Intent para solicitar acciones externas del sistema operativo
import android.net.Uri // Importación de la clase Uri para formatear direcciones web o enlaces de esquemas personalizados

/**
- G13: abre el chat de WhatsApp con
[telefono] (con o sin +) y deja escrito el [mensaje].
- Usa el enlace wa.me: abre la app si está instalada o el navegador si no. No necesita permisos.

- Retorna false si no se pudo abrir.

 */
fun abrirWhatsApp(context: Context, telefono: String, mensaje: String): Boolean { // Función utilitaria global para iniciar una conversación de WhatsApp
    val numero = telefono.filter { it.isDigit() } // Filtra la cadena telefónica conservando únicamente los dígitos numéricos
    if (numero.isEmpty()) return false // Cancela la operación y retorna false si el número telefónico no contiene dígitos válidos

    val enlace = Uri.parse("https://wa.me/$numero?text=${Uri.encode(mensaje)}") // Parsea el enlace HTTPS wa.me codificando el mensaje para evitar errores de caracteres
    val intent = Intent(Intent.ACTION_VIEW, enlace).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Crea un Intent de visualización agregando la bandera para iniciar una tarea independiente

    return try {
        context.startActivity(intent) // Intenta iniciar la actividad asociada al Intent (app de WhatsApp o navegador web)
        true // Retorna true si la actividad se inició con éxito
    } catch (e: ActivityNotFoundException) {
        false // Captura la excepción si no hay ninguna aplicación que maneje la URI y retorna false
    }
}
