package com.duocuc.corpalivapp.data.repository // Declaración del paquete al que pertenece este archivo dentro de la capa de repositorios

import com.duocuc.corpalivapp.data.model.LoginRequestDto // Importación del cuerpo de la petición de login
import com.duocuc.corpalivapp.data.model.SesionRemotaDto // Importación de la respuesta del login
import com.duocuc.corpalivapp.data.network.AuthApiService // Importación del servicio Retrofit de autenticación
import com.duocuc.corpalivapp.data.network.RetrofitClient // Importación del cliente Retrofit con los microservicios
import java.io.IOException // Importación de la excepción que lanza Retrofit cuando no hay conexión con el servidor
import retrofit2.HttpException // Importación de la excepción que lanza Retrofit cuando el servidor responde con error (401, 500, ...)

/**
- PARCIAL 4: REPOSITORIO DE AUTENTICACIÓN

- Es el único que habla con auth-service. Convierte las fallas de red en mensajes claros para la pantalla,
- así el ViewModel no necesita conocer Retrofit ni los códigos HTTP.

 */
class AuthRepository( // Repositorio que recibe el servicio de red (por defecto el real, o uno falso en las pruebas)
    private val api: AuthApiService = RetrofitClient.authApi // Servicio Retrofit de auth-service
) {

    /** Retorna la sesión remota si el login es correcto; si no, un fallo con el mensaje para mostrar al usuario. */
    suspend fun login(correo: String, clave: String): Result<SesionRemotaDto> { // Función de suspensión que ejecuta el login contra el servidor
        return try { // Intenta la petición de red
            Result.success(api.login(LoginRequestDto(correo = correo.trim(), clave = clave))) // Éxito: entrega la sesión con su token
        } catch (e: HttpException) { // El servidor respondió con un código de error
            val mensaje = if (e.code() == 401) { // 401: correo o contraseña incorrectos
                "Correo o contraseña incorrectos"
            } else { // Otro código: error del servidor
                "El servidor respondió con un error (${e.code()})"
            }
            Result.failure(Exception(mensaje)) // Falla con el mensaje correspondiente
        } catch (e: IOException) { // No hubo conexión: servicios apagados, sin red o dirección incorrecta
            Result.failure(Exception("No se pudo conectar con el servidor. Revisa que los servicios estén encendidos")) // Falla con aviso de conexión
        }
    }
}
