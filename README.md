# 💙 CORPALIV - Plataforma de Impacto Social y Vinculación Empresarial

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-green.svg)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/architecture)

Aplicación móvil orientada a centralizar y visibilizar las oportunidades de colaboración, programas de RSE empresarial y donaciones para la **Corporación de Padres y Amigos por el Limitado Visual (CORPALIV)**, la **Escuela Especial Jean Van Dijk** y la red sociolaboral **Alba Lab**.

---

## 📌 Problemática y Solución

* **Desafío:** La difusión de proyectos y alternativas de vinculación institucional se encontraba dispersa en múltiples canales (reuniones, presentaciones, redes sociales), lo que dificultaba que potenciales empresas aliadas y donantes comprendieran rápidamente el impacto social y las opciones de colaboración.
* **Solución:** Una plataforma móvil centralizada que unifica la propuesta de valor de la organización, destacando el **impacto social directo** de cada iniciativa y facilitando la vinculación directa con las distintas áreas.

---

## 🚀 Tecnologías y Herramientas

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) con **Material Design 3**
* **Arquitectura:** MVVM (*Model-View-ViewModel*) con estados reactivos (*StateFlow*)
* **Conexión de Red:** [Retrofit 2](https://square.github.io/retrofit/) + **Gson**
* **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/) (*Coroutine Image Loader*)
* **Concurrencia:** Kotlin Coroutines

---

## 📁 Estructura del Proyecto

```text
com.duocuc.corpalivapp/
├── data/
│   ├── local/          # Guía 12: EstadoDataStore (DataStore Preferences)
│   ├── model/          # OportunidadModel, Persona, Empleado... + Guía 11: UsuarioUiState, UsuarioErrores
│   └── network/        # Interfaces Retrofit y Cliente de API (ApiService, RetrofitClient)
├── assets/institucion/ # Textos en Markdown: Corpaliv (Inicio), Escuela Jan Van Dijk y Alba Lab (Quiénes somos)
├── navigation/         # Guías 10 y 11
│   ├── Screen.kt           # Sealed class con las rutas tipo-safe
│   ├── NavigationEvent.kt  # Eventos: NavigateTo, PopBackStack, NavigateUp
│   └── AppNavigation.kt    # NavController + NavHost + ViewModels compartidos
├── ui/
│   ├── components/     # OportunidadCard, AppTopBar, AppDrawerContent, AppBottomBar,
│   │                   # DestinosNavegacion, MarkdownText, ImagenInteligente (Guía 13)
│   ├── screens/        # HomeScreen, HomeScreenAdaptativas, OportunidadesScreen, InstitucionScreen,
│   │                   # RegistroScreen, ResumenScreen (G11), ModoEspecialScreen (G12), PerfilScreen (G13)
│   ├── theme/          # Paleta de colores, tipografía y tema (Material 3)
│   └── viewmodel/      # OportunidadViewModel, MainViewModel (navegación), UsuarioViewModel (G11),
│                       # EstadoViewModel (G12), PerfilViewModel (G13), InstitucionViewModel
├── util/               # WindowSizeUtils (WindowSizeClass), ArchivosUtils (FileProvider), RutUtils
└── MainActivity.kt     # Punto de entrada: llama a AppNavigation()
```

---

## 🧭 Guía 10 - Navegación y estructura visual

Flujo de navegación (MVVM): `Pantalla → MainViewModel (emite NavigationEvent) → AppNavigation (NavController) → destino`.

| Ruta | Pantalla | Componentes de navegación |
|------|----------|---------------------------|
| `home_page` | Inicio adaptativo (Guía 8 y 9) con la información de Corpaliv en bloques | `TopAppBar` + `ModalNavigationDrawer` (Compact/Medium) o `PermanentNavigationDrawer` (Expanded) |
| `oportunidades_page` | Oportunidades y proyectos RSE | `TopAppBar` con flecha de retorno |
| `institucion_page` | Quiénes somos: Escuela Jan Van Dijk y Alba Lab, leídos desde `.md` y mostrados en bloques | Selector de sección (una a cada costado, en azul) |
| `perfil_page` | Mi perfil: pide iniciar sesión y, con la cuenta de administrador, muestra la información general, el badge con el rol, la foto (cámara o galería), la opción Dashboard y cerrar sesión | Menú lateral, sección "Herramientas" |
| `dashboard_page` | Dashboard para administradores: aportes de personas y empresas con su botón "Contactar Whatsapp" | Botón "Dashboard" del perfil (requiere sesión de administrador) |

La `NavigationBar` inferior se declara una sola vez en `AppNavigation` y se muestra en **todas** las pantallas.

---

## 📝 Guía 11 - Formularios, validaciones y paso de información

- `UsuarioUiState` y `UsuarioErrores` modelan el estado del formulario (`data/model`).
- `UsuarioViewModel` guarda el estado en un `MutableStateFlow` y valida con `validarFormulario()`; los errores se corrigen en tiempo real mientras se escribe.
- `RegistroScreen` (`OutlinedTextField`, desplegable de motivo, `Button`, `isError` + `supportingText`) y `ResumenScreen` comparten el **mismo** `UsuarioViewModel`, sin pasar argumentos por la ruta.
- Flujo: `oportunidades_page` → clic en una oportunidad → `registro_page` → barra de carga → `resumen_page` (agradecimiento y aviso de contacto en un día hábil por WhatsApp o teléfono).
- `registro_page` tiene un selector **Persona / Empresa** que cambia los campos sin borrar lo ya escrito. Persona: nombre, apellido, RUN, correo, teléfono (+569) y motivo (aportar o ingresar a programa); si el motivo es "Aportar" se pide además la frecuencia de la donación (única, mensual o anual). Empresa: razón social, dirección comercial, RUT, giro, representante legal y su RUT, correo corporativo y teléfono.
- Al confirmar un aporte (persona que aporta o empresa) se muestra una pantalla de carga y luego, por 5 segundos, el mensaje de agradecimiento antes de volver al inicio. Esos aportes se guardan en el dispositivo (DataStore) y aparecen en el Dashboard.
- La pantalla de acceso usa `iniciarSesionRemoto()` contra `auth-service`; el token JWT y la sesión viven en memoria. El método local `iniciarSesion()` se conserva para las pruebas de la guía anterior y no es el acceso utilizado por la pantalla.
- El RUN y el RUT se validan por formato (`XXXXXXXX-X`); el dígito verificador queda desactivado por ahora (`VERIFICAR_DIGITO_VERIFICADOR` en `RutUtils.kt`).
- Pruebas unitarias en `UsuarioViewModelTest`, `RutUtilsTest` y `MarkdownParserTest`.

## ✨ Guía 12 - Animaciones y estados

- `ModoEspecialScreen`: interruptor persistente con **DataStore Preferences** (`EstadoDataStore` + `EstadoViewModel`).
- Estado derivado con `derivedStateOf`, panel con `rememberSaveable`, carga inicial con `CircularProgressIndicator`.
- Animaciones: `animateColorAsState`, `animateFloatAsState`, `AnimatedContent`, `AnimatedVisibility` y `animateContentSize`.

## 📷 Guía 13 - Recurso nativo: cámara y galería

- `PerfilScreen` usa `rememberLauncherForActivityResult` con `GetContent()` (galería) y `TakePicture()` (cámara) + permiso `CAMERA` en tiempo de ejecución.
- `FileProvider` (`res/xml/file_paths.xml` y `AndroidManifest.xml`) entrega a la cámara un `Uri` temporal seguro.
- `PerfilViewModel` guarda el `Uri?` en un `StateFlow`; `ImagenInteligente` lo muestra en círculo (o un ícono por defecto) y también se reutiliza en la cabecera del menú lateral.
- Probar en un **teléfono físico**; cancelar la galería o la cámara no cierra la app.

---

Ramas y commits de evidencia: `feature/navegacion-menu`, `feature/registro-formulario`, `feature/estado-animaciones` y `feature/imagen-inteligente`.

## Pruebas, cobertura y entrega

El proyecto usa Gradle Wrapper 9.6.0, Android Gradle Plugin 9.3.0 y el JDK 25 definido
en `gradle/gradle-daemon-jvm.properties`. Configura la ruta del SDK en `local.properties`
o desde Android Studio. Las pruebas unitarias usan MockK y no requieren un backend ni emulador.

Desde la raíz del proyecto, en PowerShell:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:jacocoTestReport :app:lintDebug
```

En macOS/Linux, usa `bash ./gradlew` en lugar de `.\gradlew.bat`.

- Pruebas: `app/build/reports/tests/testDebugUnitTest/index.html`.
- Cobertura HTML: `app/build/reports/jacoco/jacocoTestReport/html/index.html`.
- Cobertura XML: `app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml`.
- Análisis estático: `app/build/reports/lint-results-debug.html`.

`ExternalPostViewModelTest` cubre carga, éxito, respuesta vacía, errores, reintentos,
actualización, peticiones simultáneas y cancelación. `SesionRemotaViewModelTest`
cubre el flujo remoto con respuestas simuladas, el token, la limpieza del formulario
y el cierre de sesión. Se mantienen las pruebas anteriores, incluidas las tres del
Repository externo. JaCoCo analiza las clases Kotlin generadas por AGP 9 y las clases
Java; no se excluyen pantallas ni lógica de negocio para aumentar el porcentaje.

La API externa está separada en `data/model/ExternalPost`, `data/network/ExternalApiService`,
`data/network/ExternalApiClient` y `data/repository/ExternalPostRepository`.
Su cliente no comparte el interceptor JWT del backend.
La opción «API externa» del menú lateral está registrada en `AppNavigation`,
por lo que su ruta abre `ExternalPostScreen` dentro del grafo de navegación.

Los archivos de la API externa, su pantalla y ViewModel, el menú lateral, las
pruebas nuevas y la configuración de compilación incluyen notas en español
junto a las instrucciones. Estas explican los datos, las acciones y las
comprobaciones para facilitar el estudio del proyecto. Se conservan también
los comentarios de las guías anteriores en el resto de las clases.

### APK firmado

1. Conserva la clave `.jks` en una carpeta privada fuera del repositorio.
2. Copia `keystore.properties.example` a `keystore.properties` y completa la ruta,
   el alias y las contraseñas localmente. Este archivo y los almacenes de claves
   están ignorados por Git.
3. Ejecuta `.\gradlew.bat :app:assembleRelease`.
4. El resultado firmado queda en `app/build/outputs/apk/release/app-release.apk`.

Si aún no existe una clave, se puede crear con `keytool` (solicita la contraseña
en la terminal):

```text
keytool -genkeypair -keystore /ruta/privada/corpaliv-release.jks -storetype JKS -alias corpaliv-release -keyalg RSA -keysize 3072 -validity 10000
```

Guarda una copia privada de la clave y sus contraseñas para poder firmar futuras
actualizaciones. La compilación de release exige la configuración de firma;
las pruebas unitarias pueden ejecutarse sin ella.

### Validación de la integración

La entrega está configurada para un **emulador de Android**, con los microservicios
del computador en `10.0.2.2`: oportunidades en el puerto 8081, aportes en 8082 y
autenticación en 8083. El manifiesto permite HTTP para este entorno local.
El APK necesita esos servicios encendidos para completar los flujos del backend.

Antes de entregar, verificar en el emulador: inicio y navegación; publicaciones
externas y actualización; login remoto correcto e incorrecto; oportunidades;
registro/aportes; perfil, cámara/galería y cierre de sesión. Las pruebas con mocks
no sustituyen esta comprobación contra los servicios reales.
