# AppAgendita 📅

**AppAgendita** es una aplicación móvil integral diseñada para la gestión de productividad personal. Permite a los usuarios administrar notas, tareas y eventos de manera eficiente, respaldada por una arquitectura robusta de microservicios y un sistema *offline-first*.

---

## 👥 Desarrollado por

| Nombre | Rol |
| :--- | :--- |
| **Domingo Velazquez** | Desarrollador Full Stack |
| **Areliz Isla** | Desarrolladora Full Stack |
| **Matias Araos** | Desarrollador Full Stack |

---

## ✨ Funcionalidades

* **Autenticación Segura:** Sistema completo de Registro y Login conectado a un microservicio dedicado, utilizando Tokens JWT y UUIDs.
* **Gestión de Notas Multimedia:**
    * Creación, edición y listado de notas.
    * **Integración Nativa de Cámara:** Permite tomar fotos y adjuntarlas a las notas.
* **Gestión de Tareas:** Organización de tareas con prioridades y categorías.
* **Sincronización Híbrida (Offline-First):** La app funciona sin internet guardando datos en SQLite (Room) y se sincroniza con el servidor (MySQL) cuando se restablece la conexión.
* **Privacidad de Datos:** Arquitectura diseñada para que cada usuario acceda únicamente a su propia información.
* **Interfaz Moderna:** UI construida 100% con **Jetpack Compose** y Material Design 3.

---

## 🛠️ Tecnologías y Arquitectura

El proyecto está dividido en dos grandes repositorios: Cliente (Android) y Servidor (Spring Boot).

### 📱 Cliente Android
* **Lenguaje:** Kotlin.
* **UI:** Jetpack Compose (Material 3).
* **Inyección de Dependencias:** **Hilt** (Dagger).
* **Base de Datos Local:** Room (SQLite) para persistencia offline.
* **Red:** **Retrofit** + Gson para consumo de APIs.
* **Seguridad:** EncryptedSharedPreferences (SessionManager).
* **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Architecture.

### ☁️ Backend (Microservicios)
* **Framework:** Spring Boot 3.
* **Lenguaje:** Java 17.
* **Base de Datos:** MySQL (bases de datos independientes por servicio).
* **Ecosistema Spring Cloud:**
    * **Config Server:** Configuración centralizada.
    * **Eureka:** Service Discovery.
    * **API Gateway:** Enrutamiento unificado y seguridad.
* **Seguridad:** Spring Security + JWT.

---

## 🔗 Endpoints Utilizados

La aplicación móvil se conecta a un **API Gateway** (puerto 8080) que distribuye las peticiones a los microservicios correspondientes.

| Microservicio | Método HTTP | Endpoint | Descripción |
| :--- | :--- | :--- | :--- |
| **User / Auth** | `POST` | `/api/auth/login` | Autenticación de usuario (retorna Token y UUID). |
| **User / Auth** | `POST` | `/api/users` | Registro de un nuevo usuario. |
| **Notes** | `GET` | `/api/notes/user/{userId}` | Obtiene todas las notas de un usuario. |
| **Notes** | `POST` | `/api/notes` | Crea una nueva nota (título, descripción, imagen). |
| **Notes** | `DELETE` | `/api/notes/{noteId}/user/{userId}` | Elimina una nota específica. |
| **Tasks** | `POST` | `/api/tasks` | Crea una nueva tarea con prioridad y categoría. |
| **Tasks** | `GET` | `/api/tasks/user/{userId}` | Obtiene las tareas del usuario. |
| **Events** | `GET` | `/api/events` | (Implementado) Gestión de calendario. |

---

## 📂 Estructura del Proyecto (Android)

```text
com.example.appagendita_grupo1
├── data/
│   ├── local/          # Persistencia Room (Dao, Entity)
│   ├── remote/         # Retrofit (ApiService, Request, Response)
│   └── repository/     # Lógica de sincronización (Offline-First)
├── di/                 # Módulos de Hilt (NetworkModule, DatabaseModule)
├── model/              # Modelos de UI y Estado
├── navigation/         # Grafo de navegación y Rutas
├── ui/                 # Pantallas y Componentes (Compose)
├── utils/              # SessionManager (Preferencias Encriptadas)
└── viewmodel/          # ViewModels (HiltViewModel)


##**Captura del APK firmado y .jks:**








![Captura del APK firmado y el jks](https://github.com/user-attachments/assets/6fbadf3a-4ef9-44e3-9311-a148e04662ec)


![Captura del APK firmado y el jks](https://github.com/user-attachments/assets/ee4e0c76-d395-4afc-9c48-1baf3067b253)

