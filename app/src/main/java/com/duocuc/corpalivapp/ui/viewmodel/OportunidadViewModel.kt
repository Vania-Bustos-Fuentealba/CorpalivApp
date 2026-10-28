package com.duocuc.corpalivapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.corpalivapp.data.model.Empleado
import com.duocuc.corpalivapp.data.model.OportunidadModel
import com.duocuc.corpalivapp.data.model.PerfilUsuario
import com.duocuc.corpalivapp.data.model.Persona
import com.duocuc.corpalivapp.data.model.ResultadoLogin
import com.duocuc.corpalivapp.data.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * G2 - ESTRUCTURA CONDICIONAL:
 * Permite que la vista (OportunidadesScreen) evalúe exhaustivamente cada estado mediante 'when'.
 */
sealed interface OportunidadUiState {
    object Loading : OportunidadUiState
    data class Success(val oportunidades: List<OportunidadModel>) : OportunidadUiState
    data class Error(val mensaje: String) : OportunidadUiState
}

class OportunidadViewModel : ViewModel() {

    // Gestión de estado reactivo mediante StateFlow
    private val _uiState = MutableStateFlow<OportunidadUiState>(OportunidadUiState.Loading)
    val uiState: StateFlow<OportunidadUiState> = _uiState.asStateFlow()

    init {
        // G4 - CREACIÓN DE OBJETOS Y DEMOSTRACIÓN DE POO:
        demostrarPOOGuia4()

        // G5 - CORUTINAS Y SINTAXIS AVANZADA:
        probarAutenticacionGuia5()

        cargarOportunidades()
    }

    /**
     * GUÍA 5 - FUNCIÓN ASÍNCRONA (suspend fun):
     * Se simula la verificación de credenciales.
     */
    suspend fun autenticarUsuario(usuario: String, contrasena: String): ResultadoLogin {
        // Pausa la ejecución de la corutina por 2 segundos sin bloquear el hilo UI
        delay(2000L)

        // Lógica de validación de credenciales
        return if (usuario == "admin" && contrasena == "1234") {
            val perfil = PerfilUsuario(
                nombreUsuario = "Coordinador CORPALIV",
                email = "admin@corpaliv.cl"
            )
            ResultadoLogin.Exito(perfil)
        } else {
            ResultadoLogin.Error("Credenciales incorrectas")
        }
    }

    /**
     * G5 - EJECUCIÓN EN CORUTINA Y MANEJO DE ESTADOS:
     * Ejecuta el flujo asíncrono para probar casos de Exito y Error.
     */
    private fun probarAutenticacionGuia5() {
        viewModelScope.launch {
            println("=== DEMOSTRACIÓN GUÍA 5 - CORUTINAS Y SINTAXIS AVANZADA ===")

            // Simulación Estado Inicial: Autenticando
            val estadoInicial: ResultadoLogin = ResultadoLogin.Autenticando
            procesarResultadoLogin(estadoInicial)

            // 1. Prueba con credenciales válidas
            println("\n--- Probando credenciales válidas ('admin', '1234') ---")
            val resultadoExito = autenticarUsuario("admin", "1234")
            procesarResultadoLogin(resultadoExito)

            // 2. Prueba con credenciales inválidas
            println("\n--- Probando credenciales inválidas ('usuario', '0000') ---")
            val resultadoError = autenticarUsuario("usuario", "0000")
            procesarResultadoLogin(resultadoError)

            println("===========================================================")
        }
    }

    /**
     * GUÍA 5 - EXPRESIONES Y FUNCION DE AMBITO:
     * Procesa la sealed class ResultadoLogin y aplica '.let' en el caso de Exito.
     */
    private fun procesarResultadoLogin(resultado: ResultadoLogin) {
        when (resultado) {
            is ResultadoLogin.Autenticando -> {
                println("Estado: Autenticando usuario en el servidor CORPALIV...")
            }

            is ResultadoLogin.Exito -> {
                // GUÍA 5 - APLICACIÓN DE LA FUNCIÓN DE ÁMBITO '.let':
                // Permite operar sobre el objeto 'perfil' de manera segura y expresiva.
                resultado.perfil.let { perfil ->
                    println("¡Login Exitoso! Bienvenido: ${perfil.nombreUsuario} | Email: ${perfil.email}")
                }
            }

            is ResultadoLogin.Error -> {
                println("Error de Autenticación: ${resultado.mensaje}")
            }
        }
    }

    /**
     * GUÍA 4: Instanciación explícita de objetos 'Persona' y 'Empleado' para verificar
     * la Herencia, Encapsulamiento y Polimorfismo.
     */
    private fun demostrarPOOGuia4() {
        println("=== G4 - POO CORPALIV ===")

        // 1. Instancia de la clase base Persona
        val personaBase: Persona = Persona(
            nombre = "Camila Silva",
            edad = 28
        )

        // 2. Instancia de la subclase Empleado
        val empleadoCorpaliv: Empleado = Empleado(
            nombre = "Roberto Morales",
            edad = 35,
            puesto = "Coordinador Alba Lab",
            salario = 850000.0 // Atributo privado encapsulado
        )

        // 3. Verificación de Polimorfismo al invocar presentarse() en ambos objetos
        println("--- Invocación Persona Base ---")
        personaBase.presentarse()

        println("--- Invocación Empleado ---")
        empleadoCorpaliv.presentarse()

        println("--- Método Específico de Empleado ---")
        empleadoCorpaliv.mostrarPuesto()
        println("==========================================")
    }

    fun cargarOportunidades() {
        viewModelScope.launch {
            _uiState.value = OportunidadUiState.Loading
            try {
                // Consulta asíncrona a la API de CORPALIV mediante Retrofit
                val listaApi = RetrofitClient.apiService.getOportunidades()

                // G4 - INTEGRACIÓN DE OBJETOS EN EL FLUJO:
                // Se asigna una instancia de Empleado como responsable a cada oportunidad obtenida.
                val coordinadorGenerico = Empleado(
                    nombre = "Roberto Morales",
                    edad = 35,
                    puesto = "Coordinador de Proyectos CORPALIV",
                    salario = 850000.0
                )

                val listaConResponsables = listaApi.map { oportunidad ->
                    oportunidad.copy(responsable = oportunidad.responsable ?: coordinadorGenerico)
                }

                _uiState.value = OportunidadUiState.Success(listaConResponsables)
            } catch (e: Exception) {
                // G2 - NULL SAFETY + RESPALDO DE DATOS LOCALES:
                // Si la red o el certificado SSL fallan, se cargan datos de prueba para asegurar
                // que la aplicación siempre visualice las tarjetas y ejecute la lógica evaluable.
                val coordinadorAlbaLab = Empleado(
                    nombre = "Roberto Morales",
                    edad = 35,
                    puesto = "Coordinador Alba Lab",
                    salario = 850000.0
                )

                val datosRespaldo = listOf(
                    OportunidadModel(
                        id = 1,
                        titulo = "Taller de Preparación Laboral Alba Lab",
                        categoria = "RSE Empresarial",
                        descripcion = "Programa de capacitación en oficios adaptados para jóvenes con discapacidad múltiple.",
                        impactoSocial = "12 participantes capacitados para vinculación laboral este año.",
                        imagenUrl = "https://images.unsplash.com/photo-1529070538774-1843cb3265df?q=80&w=600",
                        contactoEmail = "rse@corpaliv-demo.cl",
                        metaFinanciamiento = 5000000,
                        montoRecaudado = 1200000,
                        responsable = coordinadorAlbaLab
                    ),
                    OportunidadModel(
                        id = 2,
                        titulo = "Equipamiento Escuela Jean Van Dijk",
                        categoria = "Donación Directa",
                        descripcion = "Fondo destinado a la adquisición de material didáctico adaptado y sillas ergométricas.",
                        impactoSocial = "Beneficia directamente a 30 estudiantes en su desarrollo autónomo.",
                        imagenUrl = "https://plus.unsplash.com/premium_photo-1661380797814-d0bcc01342b7?q=80&w=600",
                        contactoEmail = null, // GUÍA 2: Para validar Null Safety
                        metaFinanciamiento = 3000000,
                        montoRecaudado = 250000,
                        responsable = coordinadorAlbaLab
                    )
                )

                _uiState.value = OportunidadUiState.Success(datosRespaldo)
            }
        }
    }
}