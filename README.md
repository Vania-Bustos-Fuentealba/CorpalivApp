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
│   ├── model/          # Data Classes (OportunidadModel)
│   └── network/        # Interfaces Retrofit y Cliente de API (ApiService, RetrofitClient)
├── ui/
│   ├── components/     # Componentes reutilizables de Compose (OportunidadCard)
│   ├── screens/        # Pantallas principales de la interfaz (OportunidadesScreen)
│   ├── theme/          # Paleta de colores, tipografía y tema (Material 3)
│   └── viewmodel/      # Gestión del estado de la UI y lógica de negocio (OportunidadViewModel)
└── MainActivity.kt     # Punto de entrada de la aplicación
