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
- La cuenta de administrador es **solo de demostración** (`SesionViewModel.kt`): las credenciales están en el código y la sesión vive en memoria. En una app real la autenticación debe hacerse en un servidor.
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
