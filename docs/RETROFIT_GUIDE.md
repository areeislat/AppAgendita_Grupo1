# Guía Completa de Retrofit en AppAgendita 📡

Esta guía explica detalladamente cómo funciona Retrofit en el proyecto, el propósito de cada archivo creado y cómo fluye la comunicación entre la app Android y los microservicios backend.

---

## 📚 Índice

1. [¿Qué es Retrofit?](#qué-es-retrofit)
2. [Dependencias Necesarias](#dependencias-necesarias)
3. [Arquitectura del Flujo de Red](#arquitectura-del-flujo-de-red)
4. [Archivos Creados y su Propósito](#archivos-creados-y-su-propósito)
5. [Flujo de Creación Paso a Paso](#flujo-de-creación-paso-a-paso)
6. [Integración con Hilt (Inyección de Dependencias)](#integración-con-hilt)
7. [Patrón Repository](#patrón-repository)
8. [Ejemplo Práctico: Login de Usuario](#ejemplo-práctico-login-de-usuario)
9. [Manejo de Errores](#manejo-de-errores)
10. [Buenas Prácticas](#buenas-prácticas)

---

## ¿Qué es Retrofit?

**Retrofit** es una librería de cliente HTTP para Android desarrollada por Square. Permite convertir una API REST en una interfaz de Kotlin/Java de manera declarativa. En términos simples:

- **Convierte llamadas HTTP en funciones de Kotlin**
- **Serializa/deserializa JSON automáticamente** usando convertidores como Gson
- **Maneja operaciones asíncronas** de forma nativa con Coroutines

### ¿Por qué usamos Retrofit?

| Ventaja | Descripción |
|---------|-------------|
| **Simplicidad** | Define endpoints como métodos de una interfaz |
| **Type Safety** | Respuestas tipadas con data classes de Kotlin |
| **Coroutines** | Soporte nativo para `suspend functions` |
| **Interceptors** | Logging, autenticación, headers personalizados |
| **Conversión** | Gson convierte JSON ↔ Objetos automáticamente |

---

## Dependencias Necesarias

Las dependencias se encuentran en `app/build.gradle.kts`:

```kotlin
// --- RETROFIT ---
implementation("com.squareup.retrofit2:retrofit:2.9.0")          // Librería base
implementation("com.squareup.retrofit2:converter-gson:2.9.0")    // Convertidor JSON
implementation("com.squareup.okhttp3:okhttp:4.12.0")             // Cliente HTTP
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Logs de red
```

### Explicación de cada dependencia:

| Dependencia | Propósito |
|-------------|-----------|
| `retrofit:2.9.0` | **Núcleo de Retrofit** - Convierte interfaces en clientes HTTP |
| `converter-gson:2.9.0` | **Serialización JSON** - Usa Gson para convertir JSON a objetos Kotlin y viceversa |
| `okhttp:4.12.0` | **Cliente HTTP** - Motor que ejecuta las peticiones HTTP reales |
| `logging-interceptor:4.12.0` | **Debugging** - Muestra en Logcat las peticiones/respuestas HTTP completas |

### Permiso de Internet

En `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## Arquitectura del Flujo de Red

```
┌─────────────┐     ┌──────────────┐     ┌───────────────┐     ┌─────────────┐
│   UI/View   │────▶│  ViewModel   │────▶│  Repository   │────▶│  ApiService │
│  (Compose)  │     │ (Hilt)       │     │ (Hilt)        │     │  (Retrofit) │
└─────────────┘     └──────────────┘     └───────────────┘     └──────┬──────┘
                                                                      │
                                                                      ▼
                                                              ┌───────────────┐
                                                              │ RetrofitClient│
                                                              │ (OkHttp)      │
                                                              └───────┬───────┘
                                                                      │
                                                                      ▼
                                                              ┌───────────────┐
                                                              │   Backend     │
                                                              │(Microservicios)│
                                                              └───────────────┘
```

---

## Archivos Creados y su Propósito

### 📁 Estructura de archivos relacionados con Retrofit:

```
app/src/main/java/com/example/appagendita_grupo1/
├── data/
│   └── remote/                          # Todo lo relacionado con red
│       ├── ApiService.kt                # Interfaz con endpoints
│       ├── RetrofitClient.kt            # Configuración de Retrofit
│       ├── request/                     # DTOs de entrada (enviar al servidor)
│       │   ├── LoginRequest.kt
│       │   ├── RegisterRequest.kt
│       │   ├── NoteRequest.kt
│       │   ├── EventRequest.kt
│       │   └── TaskRequest.kt
│       └── response/                    # DTOs de salida (recibir del servidor)
│           ├── LoginResponse.kt
│           ├── UserResponse.kt
│           ├── NoteResponse.kt
│           └── EventResponse.kt
├── di/
│   └── NetworkModule.kt                 # Módulo Hilt para inyección
└── data/repository/                     # Repositorios que usan ApiService
    ├── UserRepository.kt
    ├── NoteRepository.kt
    └── EventRepository.kt
```

---

## Flujo de Creación Paso a Paso

### Paso 1: Crear los Request DTOs (Data Transfer Objects)

Los **Request DTOs** representan los datos que **enviamos al servidor**. Se ubican en `data/remote/request/`.

**Ejemplo: `LoginRequest.kt`**
```kotlin
package com.example.appagendita_grupo1.data.remote.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("usernameOrEmail") val usernameOrEmail: String,
    @SerializedName("password") val password: String
)
```

#### ¿Para qué sirve `@SerializedName`?

Esta anotación de Gson indica el nombre exacto del campo en el JSON:

```kotlin
@SerializedName("usernameOrEmail") val usernameOrEmail: String
```

**Resultado JSON enviado:**
```json
{
    "usernameOrEmail": "usuario@email.com",
    "password": "miContraseña123"
}
```

Si el nombre del campo Kotlin coincide exactamente con el JSON, la anotación es opcional.

---

### Paso 2: Crear los Response DTOs

Los **Response DTOs** representan los datos que **recibimos del servidor**. Se ubican en `data/remote/response/`.

**Ejemplo: `LoginResponse.kt`**
```kotlin
package com.example.appagendita_grupo1.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserResponse
)
```

**Ejemplo: `UserResponse.kt`**
```kotlin
data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String = "",
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("profileImageUrl") val profileImageUrl: String? = null,
    @SerializedName("role") val role: String = "USER",
    @SerializedName("active") val active: Boolean = true,
    @SerializedName("emailVerified") val emailVerified: Boolean = false,
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = ""
)
```

> **Nota:** Los valores por defecto (`= ""`, `= null`) permiten que Gson no falle si el servidor no envía esos campos.

---

### Paso 3: Crear la Interfaz ApiService

El **ApiService** es la interfaz que define **todos los endpoints** de la API. Retrofit genera automáticamente la implementación.

**Ubicación:** `data/remote/ApiService.kt`

```kotlin
package com.example.appagendita_grupo1.data.remote

import com.example.appagendita_grupo1.data.remote.request.*
import com.example.appagendita_grupo1.data.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ============ AUTENTICACIÓN ============
    
    @POST("api/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/users")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<UserResponse>

    // ============ NOTAS ============
    
    @GET("api/notes/user/{userId}")
    suspend fun getUserNotes(@Path("userId") userId: String): Response<List<NoteResponse>>

    @POST("api/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @DELETE("api/notes/{noteId}/user/{userId}")
    suspend fun deleteNote(
        @Path("noteId") noteId: String,
        @Path("userId") userId: String
    ): Response<Map<String, String>>

    @PUT("api/notes/{noteId}/user/{userId}")
    suspend fun updateNote(
        @Path("noteId") noteId: String,
        @Path("userId") userId: String,
        @Body noteRequest: NoteRequest
    ): Response<NoteResponse>

    // ============ EVENTOS ============
    
    @POST("api/events")
    suspend fun createEvent(@Body eventRequest: EventRequest): Response<EventResponse>

    @GET("api/events/user/{ownerId}")
    suspend fun getUserEvents(@Path("ownerId") ownerId: String): Response<List<EventResponse>>

    // ============ TAREAS ============
    
    @POST("api/tasks")
    suspend fun createTask(@Body taskRequest: TaskRequest): Response<Unit>
}
```

### Anotaciones HTTP de Retrofit:

| Anotación | Método HTTP | Uso |
|-----------|-------------|-----|
| `@GET` | GET | Obtener datos (lectura) |
| `@POST` | POST | Crear recursos nuevos |
| `@PUT` | PUT | Actualizar recursos existentes |
| `@DELETE` | DELETE | Eliminar recursos |

### Parámetros de las funciones:

| Anotación | Propósito | Ejemplo |
|-----------|-----------|---------|
| `@Body` | Envía objeto como JSON en el cuerpo | `@Body request: LoginRequest` |
| `@Path` | Reemplaza `{placeholder}` en la URL | `@Path("userId") userId: String` |
| `@Query` | Añade parámetro de query string | `@Query("page") page: Int` → `?page=1` |
| `@Header` | Añade header personalizado | `@Header("Authorization") token: String` |

### ¿Por qué `suspend fun`?

Las funciones son `suspend` porque Retrofit soporta **Coroutines** de Kotlin. Esto permite:
- Llamadas HTTP asíncronas
- No bloquean el hilo principal (UI)
- Se pueden usar con `viewModelScope.launch { }`

### ¿Por qué `Response<T>`?

Usamos `Response<T>` de Retrofit en lugar de solo `T` para:
- Verificar si la llamada fue exitosa: `response.isSuccessful`
- Obtener el código HTTP: `response.code()` (200, 404, 500...)
- Leer errores: `response.errorBody()?.string()`
- Obtener el cuerpo: `response.body()`

---

### Paso 4: Configurar RetrofitClient

El **RetrofitClient** es el objeto singleton que configura y crea las instancias de Retrofit.

**Ubicación:** `data/remote/RetrofitClient.kt`

```kotlin
package com.example.appagendita_grupo1.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // URLs de cada microservicio
    private const val USER_BASE_URL = "https://msvc-user-749990022458.us-central1.run.app/"
    private const val TASK_BASE_URL = "https://msvc-task-749990022458.us-central1.run.app/"
    private const val NOTE_BASE_URL = "https://msvc-note-749990022458.us-central1.run.app/"
    private const val EVENT_BASE_URL = "https://msvc-event-749990022458.us-central1.run.app/"

    // Interceptor para ver logs de red en Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente OkHttp configurado
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)     // Añade logging
        .connectTimeout(30, TimeUnit.SECONDS)   // Timeout de conexión
        .readTimeout(30, TimeUnit.SECONDS)      // Timeout de lectura
        .writeTimeout(30, TimeUnit.SECONDS)     // Timeout de escritura
        .build()

    // Instancias de ApiService para cada microservicio
    val userApi: ApiService by lazy {
        createRetrofit(USER_BASE_URL)
    }

    val taskApi: ApiService by lazy {
        createRetrofit(TASK_BASE_URL)
    }

    val noteApi: ApiService by lazy {
        createRetrofit(NOTE_BASE_URL)
    }

    val eventApi: ApiService by lazy {
        createRetrofit(EVENT_BASE_URL)
    }

    private fun createRetrofit(baseUrl: String): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)                            // URL base del microservicio
            .client(okHttpClient)                        // Cliente HTTP configurado
            .addConverterFactory(GsonConverterFactory.create()) // Convertidor JSON
            .build()
            .create(ApiService::class.java)              // Genera implementación
    }
}
```

### Componentes explicados:

| Componente | Propósito |
|------------|-----------|
| `object RetrofitClient` | **Singleton usando declaración `object` de Kotlin** - Garantiza una sola instancia en toda la app de forma automática (no requiere implementación manual del patrón Singleton) |
| `BASE_URL` | URL raíz del servidor (los endpoints se concatenan sin `/` inicial) |
| `HttpLoggingInterceptor` | Muestra en Logcat: Headers, Body, URL de cada request |
| `OkHttpClient.Builder()` | Configura timeouts, interceptors, certificados SSL |
| `GsonConverterFactory` | Convierte JSON ↔ Objetos Kotlin automáticamente |
| `by lazy` | Inicialización perezosa - Solo se crea cuando se usa |
| `.create(ApiService::class.java)` | Retrofit genera la implementación de la interfaz |

> **📝 Nota sobre URLs:** Las URLs base deben terminar con `/` y los endpoints NO deben comenzar con `/`. Retrofit concatena automáticamente: `https://api.ejemplo.com/` + `api/users` = `https://api.ejemplo.com/api/users`

### Niveles de Logging (HttpLoggingInterceptor):

```kotlin
level = HttpLoggingInterceptor.Level.BODY    // Completo (Headers + Body)
level = HttpLoggingInterceptor.Level.HEADERS // Solo headers
level = HttpLoggingInterceptor.Level.BASIC   // Solo URL y código de respuesta
level = HttpLoggingInterceptor.Level.NONE    // Sin logs (producción)
```

### 🔐 Configuración Segura de Logging (Recomendado):

Para que el logging se desactive automáticamente en builds de producción:

```kotlin
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    // Usa BuildConfig.DEBUG para desactivar logs automáticamente en release
    level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
}
```

> **⚠️ Importante:** NUNCA dejar logs en producción - exponen datos sensibles como tokens y contraseñas.

### 🌐 Gestión de URLs por Ambiente (Mejores Prácticas):

En lugar de hardcodear URLs, se recomienda usar `BuildConfig` o un archivo de configuración:

**Opción 1: BuildConfig (en `build.gradle.kts`):**
```kotlin
android {
    buildTypes {
        debug {
            buildConfigField("String", "USER_BASE_URL", "\"https://dev-api.ejemplo.com/\"")
        }
        release {
            buildConfigField("String", "USER_BASE_URL", "\"https://api.ejemplo.com/\"")
        }
    }
}
```

**Uso en RetrofitClient:**
```kotlin
private const val USER_BASE_URL = BuildConfig.USER_BASE_URL
```

> **📝 Nota:** El proyecto actual usa URLs hardcodeadas por simplicidad. Para producción real, se recomienda usar BuildConfig.

---

## Integración con Hilt

### ¿Por qué usar Hilt?

Hilt maneja la **inyección de dependencias**, evitando crear instancias manualmente. Permite:
- Código desacoplado y testeable
- Singleton automático de ApiService
- Fácil cambio de implementaciones (mocks para tests)

### NetworkModule

**Ubicación:** `di/NetworkModule.kt`

```kotlin
package com.example.appagendita_grupo1.di

import com.example.appagendita_grupo1.data.remote.ApiService
import com.example.appagendita_grupo1.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("userApi")
    fun provideUserApiService(): ApiService = RetrofitClient.userApi

    @Provides
    @Singleton
    @Named("taskApi")
    fun provideTaskApiService(): ApiService = RetrofitClient.taskApi

    @Provides
    @Singleton
    @Named("noteApi")
    fun provideNoteApiService(): ApiService = RetrofitClient.noteApi

    @Provides
    @Singleton
    @Named("eventApi")
    fun provideEventApiService(): ApiService = RetrofitClient.eventApi
}
```

### Anotaciones explicadas:

| Anotación | Propósito |
|-----------|-----------|
| `@Module` | Indica que esta clase provee dependencias |
| `@InstallIn(SingletonComponent::class)` | Las dependencias viven mientras viva la app |
| `@Provides` | Indica que el método crea una dependencia |
| `@Singleton` | Una sola instancia en toda la app |
| `@Named("userApi")` | Identificador para distinguir múltiples ApiService |

### ¿Por qué múltiples ApiService con @Named?

Tenemos **4 microservicios** con URLs diferentes. Cada uno necesita su propia instancia de Retrofit:

```kotlin
@Named("userApi")  → https://msvc-user-xxx.run.app/
@Named("noteApi")  → https://msvc-note-xxx.run.app/
@Named("eventApi") → https://msvc-event-xxx.run.app/
@Named("taskApi")  → https://msvc-task-xxx.run.app/
```

---

## Patrón Repository

### ¿Qué es el Repository?

El **Repository** es una capa intermedia que:
1. Abstrae la fuente de datos (API, Base de Datos Local)
2. Implementa lógica de negocio (sincronización offline)
3. Transforma DTOs de red a entidades de dominio
4. Maneja errores de manera centralizada

### Ejemplo: UserRepository

**Ubicación:** `data/repository/UserRepository.kt`

```kotlin
class UserRepository @Inject constructor(
    private val userDao: UserDao,                    // Base de datos local
    @Named("userApi") private val apiService: ApiService  // API remota
) {

    sealed class AuthResult {
        data class Success(val response: LoginResponse) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val request = LoginRequest(usernameOrEmail = email, password = password)
            val response = apiService.loginUser(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                
                // Guardar en caché local
                saveUserToCache(loginResponse.user)
                
                AuthResult.Success(loginResponse)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                AuthResult.Error("Error (${response.code()}): $errorMsg")
            }
        } catch (e: Exception) {
            AuthResult.Error("Error de conexión: ${e.message}")
        }
    }
}
```

### Flujo del Repository:

```
ViewModel                Repository               ApiService              Backend
    │                        │                        │                      │
    │──── login() ──────────▶│                        │                      │
    │                        │── loginUser() ────────▶│                      │
    │                        │                        │──── POST /login ────▶│
    │                        │                        │◀── LoginResponse ────│
    │                        │◀── Response<> ─────────│                      │
    │                        │                        │                      │
    │                        │── saveToCache() ──────▶ [Room DB]             │
    │                        │                        │                      │
    │◀── AuthResult ─────────│                        │                      │
```

---

## Ejemplo Práctico: Login de Usuario

### 1. Usuario toca "Iniciar Sesión" en la UI

```kotlin
// LoginScreen.kt (Composable)
Button(onClick = { viewModel.onLoginClick() }) {
    Text("Iniciar Sesión")
}
```

### 2. ViewModel procesa la acción

```kotlin
// LoginViewModel.kt
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    fun onLoginClick() {
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = repository.login(state.email, state.password)) {
                is UserRepository.AuthResult.Success -> {
                    sessionManager.saveSession(
                        userId = result.response.user.id,
                        authToken = result.response.token
                    )
                    state = state.copy(loginSuccess = true, isLoading = false)
                }
                is UserRepository.AuthResult.Error -> {
                    state = state.copy(generalError = result.message, isLoading = false)
                }
            }
        }
    }
}
```

### 3. Repository hace la llamada HTTP

```kotlin
// UserRepository.kt
suspend fun login(email: String, password: String): AuthResult {
    val request = LoginRequest(usernameOrEmail = email, password = password)
    val response = apiService.loginUser(request)  // ← Aquí se usa Retrofit
    // ...
}
```

### 4. ApiService define el endpoint

```kotlin
// ApiService.kt
@POST("api/auth/login")
suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
```

### 5. RetrofitClient envía la petición HTTP

```
POST https://msvc-user-xxx.run.app/api/auth/login
Content-Type: application/json

{
    "usernameOrEmail": "usuario@email.com",
    "password": "contraseña123"
}
```

### 6. Backend responde

```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "username": "usuario",
        "email": "usuario@email.com",
        "firstName": "Juan",
        "lastName": "Pérez"
    }
}
```

### 7. Gson deserializa a LoginResponse

```kotlin
LoginResponse(
    token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    user = UserResponse(
        id = "550e8400-e29b-41d4-a716-446655440000",
        username = "usuario",
        email = "usuario@email.com",
        firstName = "Juan",
        lastName = "Pérez"
    )
)
```

---

## Manejo de Errores

### Tipos de errores comunes:

```kotlin
suspend fun login(email: String, password: String): AuthResult {
    return try {
        val response = apiService.loginUser(request)
        
        if (response.isSuccessful && response.body() != null) {
            // ✅ Éxito HTTP 2xx
            AuthResult.Success(response.body()!!)
        } else {
            // ❌ Error HTTP (4xx, 5xx)
            val errorMsg = response.errorBody()?.string()
            AuthResult.Error("Error ${response.code()}: $errorMsg")
        }
        
    } catch (e: JsonSyntaxException) {
        // ❌ Error de parseo JSON
        AuthResult.Error("Error de formato: ${e.message}")
        
    } catch (e: UnknownHostException) {
        // ❌ Sin conexión / servidor no encontrado
        AuthResult.Error("Sin conexión a internet")
        
    } catch (e: SocketTimeoutException) {
        // ❌ Timeout
        AuthResult.Error("Tiempo de espera agotado")
        
    } catch (e: Exception) {
        // ❌ Otros errores
        AuthResult.Error("Error inesperado: ${e.message}")
    }
}
```

---

## Buenas Prácticas

### ✅ Recomendaciones:

1. **Usar `Response<T>`** para manejar códigos HTTP correctamente
2. **Capturar excepciones específicas** (SocketTimeout, UnknownHost)
3. **Logs solo en Debug** - Desactivar en producción
4. **Timeouts razonables** - 30 segundos es un buen balance
5. **Singleton para Retrofit** - Una sola instancia por base URL
6. **Repository Pattern** - No llamar ApiService directamente desde ViewModel
7. **DTOs separados** - Request y Response en paquetes distintos
8. **Sealed Classes** - Para resultados tipados (Success/Error)

### ❌ Evitar:

1. **Crear múltiples instancias** de Retrofit (ineficiente)
2. **Llamar API en el hilo principal** (usar `suspend fun`)
3. **Logs en producción** (expone datos sensibles)
4. **Ignorar errores** (siempre mostrar feedback al usuario)
5. **Hardcodear URLs** (usar BuildConfig o constantes)

---

## Diagrama de Arquitectura Completa

```
┌────────────────────────────────────────────────────────────────────────────┐
│                              CAPA DE UI                                    │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐                 │
│  │ LoginScreen  │    │ NotesScreen  │    │ EventsScreen │                 │
│  └──────┬───────┘    └──────┬───────┘    └──────┬───────┘                 │
└─────────┼──────────────────┼──────────────────┼───────────────────────────┘
          │                   │                   │
          ▼                   ▼                   ▼
┌────────────────────────────────────────────────────────────────────────────┐
│                           CAPA DE VIEWMODEL                                │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐                 │
│  │LoginViewModel│    │NoteViewModel │    │EventViewModel│                 │
│  │ @HiltViewModel│    │ @HiltViewModel│    │ @HiltViewModel│                │
│  └──────┬───────┘    └──────┬───────┘    └──────┬───────┘                 │
└─────────┼──────────────────┼──────────────────┼───────────────────────────┘
          │                   │                   │
          ▼                   ▼                   ▼
┌────────────────────────────────────────────────────────────────────────────┐
│                          CAPA DE REPOSITORY                                │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐                 │
│  │UserRepository│    │NoteRepository│    │EventRepository│                │
│  └──────┬───────┘    └──────┬───────┘    └──────┬───────┘                 │
│         │                   │                   │                          │
│         │ Sincronización    │ Offline-First     │ Store-and-Forward       │
│         │ Caché Local       │                   │                          │
└─────────┼──────────────────┼──────────────────┼───────────────────────────┘
          │                   │                   │
          ▼                   ▼                   ▼
┌────────────────────────────────────────────────────────────────────────────┐
│                            CAPA DE DATOS                                   │
│  ┌────────────────────────┐        ┌────────────────────────┐             │
│  │      LOCAL (Room)       │        │     REMOTE (Retrofit)   │             │
│  │                         │        │                         │             │
│  │  ┌─────────┐            │        │  ┌─────────────────┐    │             │
│  │  │ UserDao │            │        │  │   ApiService    │    │             │
│  │  │ NoteDao │            │        │  │  (Interfaz)     │    │             │
│  │  │ EventDao│            │        │  └────────┬────────┘    │             │
│  │  └─────────┘            │        │           │             │             │
│  │                         │        │           ▼             │             │
│  │  ┌─────────────────┐    │        │  ┌─────────────────┐    │             │
│  │  │ AppDatabase.db  │    │        │  │ RetrofitClient  │    │             │
│  │  │   (SQLite)      │    │        │  │   (OkHttp)      │    │             │
│  │  └─────────────────┘    │        │  └────────┬────────┘    │             │
│  └─────────────────────────┘        └───────────┼─────────────┘             │
└─────────────────────────────────────────────────┼──────────────────────────┘
                                                  │
                                                  ▼
                                    ┌─────────────────────────┐
                                    │      BACKEND            │
                                    │   (Microservicios)      │
                                    │                         │
                                    │  ┌─────────────────┐    │
                                    │  │ msvc-user       │    │
                                    │  │ msvc-note       │    │
                                    │  │ msvc-event      │    │
                                    │  │ msvc-task       │    │
                                    │  └─────────────────┘    │
                                    └─────────────────────────┘
```

---

## Resumen

| Archivo | Responsabilidad |
|---------|-----------------|
| `build.gradle.kts` | Dependencias de Retrofit, OkHttp, Gson |
| `AndroidManifest.xml` | Permiso `INTERNET` |
| `*Request.kt` | DTOs de datos enviados al servidor |
| `*Response.kt` | DTOs de datos recibidos del servidor |
| `ApiService.kt` | Interfaz con todos los endpoints HTTP |
| `RetrofitClient.kt` | Configuración de Retrofit y OkHttp |
| `NetworkModule.kt` | Inyección de dependencias con Hilt |
| `*Repository.kt` | Lógica de negocio y sincronización |

---

## Recursos Adicionales

- [Documentación oficial de Retrofit](https://square.github.io/retrofit/)
- [OkHttp Interceptors](https://square.github.io/okhttp/features/interceptors/)
- [Gson User Guide](https://github.com/google/gson/blob/master/UserGuide.md)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

*Documentación creada para el proyecto AppAgendita - Grupo 1* 📚
