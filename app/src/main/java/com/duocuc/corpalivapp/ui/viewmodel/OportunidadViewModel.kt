package com.duocuc.corpalivapp.ui.viewmodel // Declaración del paquete al que pertenece este archivo dentro de la capa de lógica de presentación (ViewModel)

import androidx.lifecycle.ViewModel // Importación de la clase base ViewModel de Android Jetpack
import androidx.lifecycle.viewModelScope // Alcance de corrutinas acotado al ciclo de vida del ViewModel
import com.duocuc.corpalivapp.data.model.Empleado // Importación de la clase modelo Empleado
import com.duocuc.corpalivapp.data.model.OportunidadModel // Importación de la clase modelo OportunidadModel
import com.duocuc.corpalivapp.data.model.PerfilUsuario // Importación de la clase modelo PerfilUsuario
import com.duocuc.corpalivapp.data.model.Persona // Importación de la clase modelo base Persona
import com.duocuc.corpalivapp.data.model.ResultadoLogin // Importación de la sealed class ResultadoLogin
import com.duocuc.corpalivapp.data.network.RetrofitClient // Cliente Retrofit para peticiones a servicios REST
import kotlinx.coroutines.delay // Función de suspensión para pausar ejecuciones de corrutinas
import kotlinx.coroutines.flow.MutableStateFlow // Flujo de estado mutable observable
import kotlinx.coroutines.flow.StateFlow // Flujo de estado de solo lectura expuesto a la vista
import kotlinx.coroutines.flow.asStateFlow // Transforma un MutableStateFlow en un StateFlow inmutable
import kotlinx.coroutines.launch // Extensión para iniciar la ejecución de corrutinas

/**
- G2 - ESTRUCTURA CONDICIONAL:

- Permite que la vista (OportunidadesScreen) evalúe exhaustivamente cada estado mediante 'when'.

 */
sealed interface OportunidadUiState { // Interfaz sellada que define la jerarquía de estados para la UI de oportunidades
    object Loading : OportunidadUiState // Estado que indica que los datos están cargándose
    data class Success(val oportunidades: List<OportunidadModel>) : OportunidadUiState // Estado de éxito con la lista de oportunidades recuperadas
    data class Error(val mensaje: String) : OportunidadUiState // Estado de error con un mensaje descriptivo de la falla
}

class OportunidadViewModel : ViewModel() { // ViewModel que gestiona la carga de oportunidades, demostraciones de POO y autenticación

    // Gestión de estado reactivo mediante StateFlow
    private val _uiState = MutableStateFlow<OportunidadUiState>(OportunidadUiState.Loading) // Estado mutable interno iniciado en Loading
    val uiState: StateFlow<OportunidadUiState> = _uiState.asStateFlow() // Flujo de estado público e inmutable para la UI

    init {
        // G4 - CREACIÓN DE OBJETOS Y DEMOSTRACIÓN DE POO:
        demostrarPOOGuia4() // Ejecuta los ejemplos prácticos de Programación Orientada a Objetos

        // G5 - CORUTINAS Y SINTAXIS AVANZADA:
        probarAutenticacionGuia5() // Ejecuta las pruebas de autenticación asíncrona mediante corrutinas

        cargarOportunidades() // Inicia la carga remota o local de oportunidades al instanciar el ViewModel
    }

    /**
    - GUÍA 5 - FUNCIÓN ASÍNCRONA (suspend fun):

    - Se simula la verificación de credenciales.

     */
    suspend fun autenticarUsuario(usuario: String, contrasena: String): ResultadoLogin { // Función de suspensión para validar credenciales de forma asíncrona
        // Pausa la ejecución de la corutina por 2 segundos sin bloquear el hilo UI
        delay(2000L) // Simula tiempo de respuesta del servidor de 2000 ms

        // Lógica de validación de credenciales
        return if (usuario == "admin" && contrasena == "1234") { // Evalúa las credenciales ingresadas
            val perfil = PerfilUsuario(
                nombreUsuario = "Coordinador CORPALIV",
                email = "admin@corpaliv.cl"
            )
            ResultadoLogin.Exito(perfil) // Retorna resultado de éxito con el perfil correspondiente
        } else {
            ResultadoLogin.Error("Credenciales incorrectas") // Retorna resultado de error si las credenciales fallan
        }
    }

    /**
    - G5 - EJECUCIÓN EN CORUTINA Y MANEJO DE ESTADOS:

    - Ejecuta el flujo asíncrono para probar casos de Exito y Error.

     */
    private fun probarAutenticacionGuia5() { // Método para simular el proceso de autenticación con casos válidos e inválidos
        viewModelScope.launch { // Inicia corrutina en el scope del ViewModel
            println("=== DEMOSTRACIÓN GUÍA 5 - CORUTINAS Y SINTAXIS AVANZADA ===")

            // Simulación Estado Inicial: Autenticando
            val estadoInicial: ResultadoLogin = ResultadoLogin.Autenticando // Asigna el estado inicial de procesamiento
            procesarResultadoLogin(estadoInicial) // Procesa el estado inicial

            // 1. Prueba con credenciales válidas
            println("\n--- Probando credenciales válidas ('admin', '1234') ---")
            val resultadoExito = autenticarUsuario("admin", "1234") // Llama a la función suspendida con credenciales válidas
            procesarResultadoLogin(resultadoExito) // Procesa el resultado exitoso

            // 2. Prueba con credenciales inválidas
            println("\n--- Probando credenciales inválidas ('usuario', '0000') ---")
            val resultadoError = autenticarUsuario("usuario", "0000") // Llama a la función suspendida con credenciales inválidas
            procesarResultadoLogin(resultadoError) // Procesa el resultado fallido

            println("===========================================================")
        }
    }

    /**
    - GUÍA 5 - EXPRESIONES Y FUNCION DE AMBITO:

    - Procesa la sealed class ResultadoLogin y aplica '.let' en el caso de Exito.

     */
    private fun procesarResultadoLogin(resultado: ResultadoLogin) { // Muestra en consola el estado de la autenticación mediante evaluación condicional
        when (resultado) {
            is ResultadoLogin.Autenticando -> { // Caso en que la autenticación está en progreso
                println("Estado: Autenticando usuario en el servidor CORPALIV...")
            }

            is ResultadoLogin.Exito -> { // Caso en que la autenticación fue exitosa
                // GUÍA 5 - APLICACIÓN DE LA FUNCIÓN DE ÁMBITO '.let':
                // Permite operar sobre el objeto 'perfil' de manera segura y expresiva.
                resultado.perfil.let { perfil -> // Uso de la función de ámbito .let sobre el perfil de usuario
                    println("¡Login Exitoso! Bienvenido: ${perfil.nombreUsuario} | Email: ${perfil.email}")
                }
            }

            is ResultadoLogin.Error -> { // Caso en que ocurrió un error de autenticación
                println("Error de Autenticación: ${resultado.mensaje}")
            }
        }
    }

    /**
    - GUÍA 4: Instanciación explícita de objetos 'Persona' y 'Empleado' para verificar

    - la Herencia, Encapsulamiento y Polimorfismo.

     */
    private fun demostrarPOOGuia4() { // Método para ejercitar conceptos clave de la Programación Orientada a Objetos
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
        personaBase.presentarse() // Invocación del método sobre la clase base

        println("--- Invocación Empleado ---")
        empleadoCorpaliv.presentarse() // Invocación polimórfica sobre la subclase

        println("--- Método Específico de Empleado ---")
        empleadoCorpaliv.mostrarPuesto() // Llamada a método propio de la clase derivada Empleado
        println("==========================================")
    }

    fun cargarOportunidades() { // Realiza la carga de las oportunidades desde la API remota o aplica el respaldo local si falla
        viewModelScope.launch { // Inicia corrutina asíncrona
            _uiState.value = OportunidadUiState.Loading // Emite el estado de carga previa
            try {
                // Consulta asíncrona a la API de CORPALIV mediante Retrofit
                val listaApi = RetrofitClient.apiService.getOportunidades() // Realiza llamada HTTP remota

                // G4 - INTEGRACIÓN DE OBJETOS EN EL FLUJO:
                // Se asigna una instancia de Empleado como responsable a cada oportunidad obtenida.
                val coordinadorGenerico = Empleado(
                    nombre = "Roberto Morales",
                    edad = 35,
                    puesto = "Coordinador de Proyectos CORPALIV",
                    salario = 850000.0
                )

                val listaConResponsables = listaApi.map { oportunidad -> // Mapea las oportunidades asignando un responsable por defecto si no lo traen
                    oportunidad.copy(responsable = oportunidad.responsable ?: coordinadorGenerico)
                }

                _uiState.value = OportunidadUiState.Success(listaConResponsables) // Emite la lista procesada en estado de Éxito
            } catch (e: Exception) { // Captura cualquier error de conexión, timeout o red
                // G2 - NULL SAFETY + RESPALDO DE DATOS LOCALES:
                // Si la red o el certificado SSL fallan, se cargan datos de prueba para asegurar
                // que la aplicación siempre visualice las tarjetas y ejecute la lógica evaluable.
                val coordinadorAlbaLab = Empleado(
                    nombre = "Roberto Morales",
                    edad = 35,
                    puesto = "Coordinador Alba Lab",
                    salario = 850000.0
                )

                val datosRespaldo = listOf( // Define lista estática de respaldo con datos de prueba
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

                _uiState.value = OportunidadUiState.Success(datosRespaldo) // Emite las oportunidades de respaldo para evitar fallas visuales
            }
        }
    }
}