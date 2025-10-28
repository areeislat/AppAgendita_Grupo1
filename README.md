**AppAgendita** es una aplicación móvil para **Android** diseñada para ser tu asistente personal digital.
Permite a los usuarios **gestionar notas, tareas y eventos** de manera eficiente, con un sistema de **autenticación segura** que garantiza la privacidad de los datos personales.

---

## 📋 Tabla de Contenidos

1. [Descripción General](#-descripción-general)
2. [Características Principales](#-características-principales)
3. [Tecnologías y Arquitectura](#-tecnologías-y-arquitectura)
4. [Estructura del Proyecto](#-estructura-del-proyecto)
5. [Instalación y Configuración](#-instalación-y-configuración)
6. [Uso de la Aplicación](#-uso-de-la-aplicación)
7. [Autores y Colaboradores](#-autores-y-colaboradores)

---

## 📝 Descripción General

**AppAgendita** es una aplicación de **agenda y productividad** desarrollada en **Kotlin nativo** para Android.
El objetivo principal es ofrecer una herramienta **intuitiva y segura** donde los usuarios puedan administrar sus notas, tareas y eventos personales.

Cada usuario cuenta con una cuenta propia; todos los datos creados (notas, tareas o eventos) están **asociados únicamente a su sesión**, garantizando la privacidad y protección de la información.

---

## ✨ Características Principales

* **Autenticación de Usuarios:** Sistema completo de registro e inicio de sesión.
* **Gestión de Notas:** Crear, visualizar, editar y eliminar notas personales.
* **Privacidad de Datos:** Cada usuario solo puede acceder a su propia información.
* **Interfaz Moderna:** Construida con **Jetpack Compose** siguiendo los principios de **Material Design 3**.
* **Persistencia Local:** Almacenamiento seguro con **Room (SQLite)**.
* **Gestión de Sesión:** Persistencia con **DataStore Preferences** incluso tras cerrar la app.
* **Arquitectura Limpia:** Basada en **MVVM** y **Clean Architecture** para escalabilidad y mantenibilidad.

---

## 🛠️ Tecnologías y Arquitectura

Este proyecto sigue un enfoque moderno, utilizando las tecnologías recomendadas por Google para el desarrollo Android:

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Arquitectura:**

  * **MVVM (Model-View-ViewModel):** separación clara entre lógica y presentación.
  * **Clean Architecture (simplificada):** capas de UI, ViewModel, Repositorio y Datos.
* **Base de Datos:** Room sobre SQLite
* **Asincronía:** Coroutines y Flow
* **Gestión de Sesión:** DataStore Preferences
* **Inyección de Dependencias (sugerida):** preparado para integrar Hilt o Koin
* **Navegación:** Navigation Compose

---

## 📂 Estructura del Proyecto

La organización del código sigue los principios de la arquitectura limpia, separando responsabilidades en diferentes capas:

```
app/src/main/java/com/example/appagendita_grupo1/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt        # Configuración de la base de datos Room
│   │   ├── note/
│   │   │   ├── NoteDao.kt        # Consultas SQL para las notas
│   │   │   └── NoteEntity.kt     # Modelo de datos de notas
│   │   └── user/
│   │       ├── UserDao.kt        # Consultas SQL para los usuarios
│   │       └── UserEntity.kt     # Modelo de datos de usuarios
│   ├── repository/
│   │   ├── NoteRepository.kt     # Acceso a los datos de notas
│   │   └── UserRepository.kt     # Acceso a los datos de usuarios
│   └── SessionManager.kt         # Gestión de sesión con DataStore
│
├── ui/
│   ├── screens/                  # Composables de cada pantalla (Login, Home, AddNote, etc.)
│   │   ├── home/
│   │   ├── login/
│   │   └── ...
│   └── theme/                    # Archivos de tema (Color.kt, Theme.kt, Type.kt)
│
├── viewmodel/
│   ├── LoginViewModel.kt         # Lógica de la pantalla de inicio de sesión
│   ├── AddNoteViewModel.kt       # Lógica de creación de notas
│   └── ...                       # Otros ViewModels
│
└── navigation/
    └── AppNavigation.kt          # Gestión del grafo de navegación
```

---

## 🚀 Instalación y Configuración

Sigue estos pasos para compilar y ejecutar el proyecto:

1. **Clona el repositorio:**

   ```bash
   git clone https://github.com/areeislat/AppAgendita_Grupo1.git
   ```

   *(Reemplaza la URL con la de tu repositorio real.)*

2. **Abre el proyecto en Android Studio:**

   * Se recomienda la versión **"Iguana" o superior**.
   * Ve a `File > Open` y selecciona la carpeta del proyecto clonado.

3. **Sincroniza Gradle:**

   * Android Studio debería hacerlo automáticamente.
   * Si no, haz clic en **Sync Project with Gradle Files**.

4. **Ejecuta la aplicación:**

   * Elige un emulador o conecta un dispositivo físico.
   * Haz clic en **Run 'app' (▶️)**.

---

## 📱 Uso de la Aplicación

1. **Registro:**
   Al abrir la app por primera vez, crea una cuenta con tu correo y contraseña.

2. **Inicio de Sesión:**
   Accede con tus credenciales; la sesión se mantendrá activa.

3. **Pantalla Principal (Home):**
   Muestra un resumen de tus notas, tareas y eventos.

4. **Crear Contenido:**
   Usa el botón flotante (+) o el menú para crear nuevas notas o tareas.

5. **Ver y Editar:**
   Toca una nota para ver sus detalles o modificarla.

---

## 👨‍💻 Autores y Colaboradores

Este proyecto fue desarrollado por los miembros del **Grupo 1**:

* **Domingo Velazquez** 
* **Areliz Isla** 
* **Matias Araos** 

---

¿Quieres que te lo deje en formato `.md` listo para descargar (con formato Markdown correcto y sangrías)? Puedo generarte el archivo directamente.
