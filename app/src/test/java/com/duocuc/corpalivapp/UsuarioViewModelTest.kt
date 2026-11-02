package com.duocuc.corpalivapp // Declaración del paquete al que pertenece este archivo dentro de la suite de pruebas unitarias locales

import com.duocuc.corpalivapp.data.model.TipoRegistro // Importación del enum con los tipos de registro (PERSONA o EMPRESA)
import com.duocuc.corpalivapp.ui.viewmodel.UsuarioViewModel // Importación del ViewModel del formulario a testear
import org.junit.Assert.assertEquals // Importación de aserción para verificar la igualdad entre dos valores
import org.junit.Assert.assertFalse // Importación de aserción para verificar que una condición sea falsa
import org.junit.Assert.assertNull // Importación de aserción para verificar que un objeto sea nulo
import org.junit.Assert.assertTrue // Importación de aserción para verificar que una condición sea verdadera
import org.junit.Test // Anotación de JUnit para identificar métodos que corresponden a casos de prueba

/**
- GUÍA 11 - Pruebas unitarias de las validaciones del formulario (JVM, sin emulador).

- Los datos son ficticios (RUN y RUT de ejemplo de uso común en pruebas).

 */
class UsuarioViewModelTest { // Clase de prueba unitaria local para comprobar las reglas de validación y estado de UsuarioViewModel

    private fun personaCompleta() = UsuarioViewModel().apply { // Función de prueba auxiliar que crea un ViewModel con los datos completos de una persona
        onNombreChange("Benjamín") // Asigna el nombre
        onApellidoChange("Pérez") // Asigna el apellido
        onRunChange("11.111.111-1") // Asigna un RUN válido por formato
        onCorreoChange("correo@ejemplo.cl") // Asigna un correo sintáctico correcto
        onTelefonoChange("12345678") // Asigna 8 dígitos para celular
        onMotivoChange("Ingresar a Programa") // Selecciona un motivo de registro
    }

    private fun empresaCompleta() = UsuarioViewModel().apply { // Función auxiliar que crea un ViewModel con los datos completos de una empresa
        onTipoChange(TipoRegistro.EMPRESA) // Cambia la pestaña del formulario a EMPRESA
        onRazonSocialChange("Fundación Ejemplo SpA") // Asigna la razón social
        onRutEmpresaChange("76.543.210-3") // Asigna RUT de la empresa
        onGiroChange("Servicios de ejemplo") // Asigna el giro comercial
        onRepresentanteChange("Ana Ejemplo") // Asigna el nombre del representante legal
        onRutRepresentanteChange("12345678-5") // Asigna el RUT del representante
        onCorreoEmpresaChange("contacto@ejemplo.cl") // Asigna el correo corporativo
        onTelefonoEmpresaChange("212345678") // Asigna 9 dígitos de teléfono
        onDireccionEmpresaChange("Av. Providencia 1234") // Asigna la dirección
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun personaVacia_noEsValida_yMarcaCadaError() { // Verifica que un formulario de persona vacío falle la validación y marque los errores individuales
        val vm = UsuarioViewModel() // Instancia un ViewModel con campos por defecto vacíos

        assertFalse(vm.validarFormulario()) // Afirma que la validación del formulario retorne false

        val errores = vm.estado.value.errores.persona // Obtiene el objeto de errores de persona generado en el UiState
        assertEquals("Campo obligatorio", errores.nombre) // Confirma el mensaje de error en el campo nombre
        assertEquals("Campo obligatorio", errores.apellido) // Confirma el mensaje de error en el campo apellido
        assertEquals("Campo obligatorio", errores.run) // Confirma el mensaje de error en el campo RUN
        assertEquals("Correo inválido", errores.correo) // Confirma el mensaje de error de correo
        assertEquals("Campo obligatorio", errores.telefono) // Confirma el mensaje de error de teléfono
        assertEquals("Selecciona un motivo", errores.motivo) // Confirma el mensaje de error en la selección de motivo
        assertNull(errores.frecuencia) // Confirma que la frecuencia no sea exigida si no se ha marcado 'Aportar'
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun personaCompleta_esValida() { // Comprueba que el formulario de persona con datos válidos pase la validación
        val vm = personaCompleta() // Instancia el ViewModel cargado con persona completa

        assertTrue(vm.validarFormulario()) // Afirma que validarFormulario() retorne true
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun correoSinDominio_esInvalido() { // Verifica que un formato de correo incompleto cause error de validación
        val vm = personaCompleta() // Carga los datos base válidos
        vm.onCorreoChange("correo@") // Modifica el correo por una versión inválida

        assertFalse(vm.validarFormulario()) // Confirma que el formulario sea rechazado
        assertEquals("Correo inválido", vm.estado.value.errores.persona.correo) // Valida el mensaje de error devuelto para el correo
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun runConFormatoCorrecto_seAcepta_aunqueElDigitoNoCoincida() { // Valida que se acepte el RUN por formato cuando la comprobación Módulo 11 está inactiva
        val vm = personaCompleta() // Carga los datos válidos base
        vm.onRunChange("45678987-3") // Carga un RUN con formato válido

        assertTrue(vm.validarFormulario()) // Afirma verdadero
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun runConFormatoIncorrecto_esInvalido() { // Rechaza un RUN de longitud errónea
        val vm = personaCompleta() // Carga los datos válidos base
        vm.onRunChange("1234") // Asigna un RUN incompleto de 4 dígitos

        assertFalse(vm.validarFormulario()) // Afirma que la validación falle
        assertEquals("RUN inválido", vm.estado.value.errores.persona.run) // Verifica el mensaje de error asignado
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun runSeFormateaAlEscribir() { // Comprueba la aplicación automática de guion al tipear un RUN
        val vm = UsuarioViewModel() // Instancia ViewModel limpio

        vm.onRunChange("123456785") // Ingresa la cadena de sólo números

        assertEquals("12345678-5", vm.estado.value.persona.run) // Confirma que el valor en el estado contenga el guion separador
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun telefono_soloAceptaDigitos_yMaximoOchoEnPersona() { // Prueba el filtrado numérico y el recorte de longitud para celular
        val vm = UsuarioViewModel() // Instancia ViewModel limpio

        vm.onTelefonoChange("12ab34 5678 99") // Ingresa texto con letras, espacios y más de 8 dígitos

        assertEquals("12345678", vm.estado.value.persona.telefono) // Confirma que el valor almacenado contenga únicamente los primeros 8 dígitos
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun telefonoIncompleto_esInvalido() { // Comprueba el rechazo de un número de celular de menos de 8 dígitos
        val vm = personaCompleta() // Carga persona completa
        vm.onTelefonoChange("1234") // Modifica el teléfono a 4 dígitos

        assertFalse(vm.validarFormulario()) // Verifica que el formulario sea rechazado
        assertEquals("Ingresa los 8 dígitos del celular", vm.estado.value.errores.persona.telefono) // Comprueba el mensaje de error específico
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun aportar_exigeFrecuencia() { // Valida que el motivo 'Aportar' obligue a especificar la frecuencia de donación
        val vm = personaCompleta() // Carga persona válida
        vm.onMotivoChange("Aportar") // Cambia el motivo a Aportar

        assertFalse(vm.validarFormulario()) // Rechaza por no incluir la frecuencia
        assertEquals("Selecciona la frecuencia", vm.estado.value.errores.persona.frecuencia) // Revisa el mensaje de error de frecuencia

        vm.onFrecuenciaChange("Mensual") // Asigna la frecuencia requerida

        assertTrue(vm.validarFormulario()) // Afirma que tras completar la frecuencia el formulario vuelva a ser válido
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun ingresarAPrograma_noExigeFrecuencia() { // Verifica que el motivo 'Ingresar a Programa' no requiera frecuencia
        val vm = personaCompleta() // Carga persona completa con motivo 'Ingresar a Programa'

        assertTrue(vm.validarFormulario()) // Confirma que sea válido
        assertNull(vm.estado.value.errores.persona.frecuencia) // Confirma que el error de frecuencia sea nulo
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun alCambiarAIngresarAPrograma_seQuitaElErrorDeFrecuencia() { // Prueba la eliminación de errores de frecuencia si se cambia de motivo
        val vm = personaCompleta() // Carga persona válida
        vm.onMotivoChange("Aportar") // Cambia a Aportar
        vm.validarFormulario() // Genera el error de frecuencia en el UiState

        vm.onMotivoChange("Ingresar a Programa") // Cambia de opinión y selecciona 'Ingresar a Programa'

        assertNull(vm.estado.value.errores.persona.frecuencia) // Verifica que se limpie el error de frecuencia
        assertTrue(vm.validarFormulario()) // Verifica que la validación resulte exitosa
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun errorSeLimpiaEnTiempoReal_cuandoElCampoQuedaCorrecto() { // Verifica la corrección de errores en tiempo real al tipear
        val vm = UsuarioViewModel() // Instancia ViewModel limpio
        vm.validarFormulario() // Provoca la aparición de errores en el estado

        vm.onRunChange("12345678-5") // Corrige el campo del RUN con un valor válido

        assertNull(vm.estado.value.errores.persona.run) // Confirma que el error en el campo RUN pase de mensaje a null de forma inmediata
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun cambiarDeTipo_conservaLosDatosEscritosEnAmbos() { // Valida que alternar entre Persona y Empresa mantenga los datos ingresados en ambas pestañas
        val vm = UsuarioViewModel() // Instancia ViewModel limpio
        vm.onNombreChange("Benjamín") // Ingresa un nombre en la pestaña Persona

        vm.onTipoChange(TipoRegistro.EMPRESA) // Cambia a la pestaña Empresa
        vm.onRazonSocialChange("Fundación Ejemplo SpA") // Ingresa la razón social en la pestaña Empresa
        vm.onTipoChange(TipoRegistro.PERSONA) // Vuelve a cambiar a la pestaña Persona

        assertEquals("Benjamín", vm.estado.value.persona.nombre) // Verifica que el nombre ingresado previamente en persona persista
        assertEquals("Fundación Ejemplo SpA", vm.estado.value.empresa.razonSocial) // Verifica que la razón social ingresada en empresa persista
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun empresaVacia_noEsValida_yMarcaCadaError() { // Prueba las alertas de validación inicial para el formulario de empresa
        val vm = UsuarioViewModel() // Instancia ViewModel
        vm.onTipoChange(TipoRegistro.EMPRESA) // Cambia a tipo Empresa

        assertFalse(vm.validarFormulario()) // Afirma que la validación falle

        val errores = vm.estado.value.errores.empresa // Obtiene los errores de empresa
        assertEquals("Campo obligatorio", errores.razonSocial) // Comprueba error en razón social
        assertEquals("Campo obligatorio", errores.rut) // Comprueba error en RUT empresa
        assertEquals("Campo obligatorio", errores.giro) // Comprueba error en giro
        assertEquals("Campo obligatorio", errores.representante) // Comprueba error en nombre representante
        assertEquals("Campo obligatorio", errores.rutRepresentante) // Comprueba error en RUT representante
        assertEquals("Correo inválido", errores.correo) // Comprueba error de correo
        assertEquals("Campo obligatorio", errores.telefono) // Comprueba error en teléfono
        assertEquals("Campo obligatorio", errores.direccion) // Comprueba error en dirección
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun empresaCompleta_esValida_sinCompletarLosCamposDePersona() { // Verifica que un formulario de empresa válido no dependa de los campos de persona
        val vm = empresaCompleta() // Instancia empresa válida

        assertTrue(vm.validarFormulario()) // Afirma que la validación sea exitosa
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun rutDeEmpresaConFormatoIncorrecto_esInvalido() { // Rechaza un RUT empresarial mal formado
        val vm = empresaCompleta() // Carga empresa
        vm.onRutEmpresaChange("7654-4") // Asigna un RUT de empresa con longitud inválida

        assertFalse(vm.validarFormulario()) // Afirma que la validación falle
        assertEquals("RUT inválido", vm.estado.value.errores.empresa.rut) // Confirma el mensaje de error de RUT
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun rutDeEmpresaYDelRepresentante_seAceptanPorFormato() { // Comprueba la validación por formato en ambos campos de RUT del formulario de empresa
        val vm = empresaCompleta() // Carga empresa
        vm.onRutEmpresaChange("45678987-3") // Carga un RUT de empresa válido por formato
        vm.onRutRepresentanteChange("45678987-3") // Carga un RUT de representante válido por formato

        assertTrue(vm.validarFormulario()) // Afirma verdadero
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun telefonoDeEmpresa_exigeNueveDigitos() { // Valida que el teléfono de empresa exija exactamente 9 dígitos
        val vm = empresaCompleta() // Carga empresa
        vm.onTelefonoEmpresaChange("12345678") // Asigna sólo 8 dígitos (formato de celular de persona)

        assertFalse(vm.validarFormulario()) // Afirma que la validación falle
        assertEquals("Ingresa los 9 dígitos del teléfono", vm.estado.value.errores.empresa.telefono) // Verifica la advertencia requerida de 9 dígitos
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun personaQueAporta_esUnAporte_yGeneraSuRegistro() { // Prueba la detección de aporte y la construcción de la entidad RegistroGuardado para personas
        val vm = UsuarioViewModel() // Instancia ViewModel
        vm.seleccionarOportunidad("Taller de carpintería") // Selecciona la oportunidad objetivo
        vm.onNombreChange("Benjamín")
        vm.onApellidoChange("Pérez")
        vm.onRunChange("11.111.111-1")
        vm.onCorreoChange("correo@ejemplo.cl")
        vm.onTelefonoChange("12345678")
        vm.onMotivoChange("Aportar") // Marca el motivo 'Aportar'
        vm.onFrecuenciaChange("Mensual") // Elige frecuencia

        assertTrue(vm.estado.value.esAporte) // Verifica que la propiedad calculada esAporte sea true

        val registro = vm.construirRegistro(ahora = 1000L) // Genera el objeto RegistroGuardado fijando la marca de tiempo

        assertEquals(1000L, registro?.id) // Revisa el ID generado
        assertEquals("PERSONA", registro?.tipo) // Revisa el tipo de registro
        assertEquals("Taller de carpintería", registro?.oportunidad) // Revisa el título de la oportunidad
        assertEquals("Benjamín Pérez", registro?.nombre) // Revisa la concatenación de nombre y apellido
        assertEquals("+569 12345678", registro?.telefono) // Revisa el prefijo telefónico asignado (+569)
        assertEquals("Mensual", registro?.tipoAporte) // Revisa la frecuencia registrada
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun personaQueIngresaAPrograma_noEsUnAporte_yNoGeneraRegistro() { // Verifica que un registro que no sea un aporte económico devuelva un objeto nulo
        val vm = personaCompleta() // Carga persona con motivo 'Ingresar a Programa'

        assertFalse(vm.estado.value.esAporte) // Afirma que esAporte sea false
        assertNull(vm.construirRegistro()) // Confirma que construirRegistro() devuelva null
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun empresa_esUnAporte_yGeneraSuRegistroConTodosSusDatos() { // Valida la construcción completa del objeto de registro para empresas
        val vm = empresaCompleta() // Carga empresa válida

        assertTrue(vm.estado.value.esAporte) // Confirma que el registro empresarial cuente siempre como aporte

        val registro = vm.construirRegistro(ahora = 2000L) // Construye el registro

        assertEquals("EMPRESA", registro?.tipo) // Revisa que el tipo sea EMPRESA
        assertEquals("Fundación Ejemplo SpA", registro?.nombre) // Revisa la razón social
        assertEquals("+56 212345678", registro?.telefono) // Revisa el prefijo telefónico empresarial (+56)
        assertEquals("Aporte empresarial", registro?.tipoAporte) // Revisa la etiqueta fija del tipo de aporte
        assertEquals("Av. Providencia 1234", registro?.direccion) // Revisa la dirección
        assertEquals("Ana Ejemplo", registro?.representante) // Revisa el representante legal
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun seleccionarOportunidad_guardaElTituloYDejaElFormularioVacio() { // Comprueba que asignar una oportunidad reseteé los campos del formulario
        val vm = personaCompleta() // Carga datos en el formulario

        vm.seleccionarOportunidad("Taller de carpintería") // Selecciona una oportunidad

        assertEquals("Taller de carpintería", vm.estado.value.oportunidad) // Revisa que la oportunidad seleccionada quede asignada
        assertEquals("", vm.estado.value.persona.nombre) // Confirma que los datos de persona hayan sido limpiados
        assertEquals(TipoRegistro.PERSONA, vm.estado.value.tipo) // Confirma que el tipo por defecto se restablezca
    }

    @Test // Marca la función como un caso de prueba ejecutable por JUnit
    fun reiniciar_dejaElFormularioVacio() { // Prueba la acción de reinicio total del estado del ViewModel
        val vm = personaCompleta() // Carga datos en el formulario

        vm.reiniciar() // Invoca la función de reinicio

        assertEquals("", vm.estado.value.persona.nombre) // Confirma que el campo nombre vuelva a estar vacío
        assertEquals("", vm.estado.value.oportunidad) // Confirma que la oportunidad se limpie
        assertFalse(vm.estado.value.completado) // Confirma que el indicador de completado esté en false
    }
}