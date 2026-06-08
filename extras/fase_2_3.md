# FASE_2_2_API.md

# Fase 2.2 — API de Autenticación

## Objetivo

Implementar los endpoints de autenticación dentro de `ApiService.kt` para permitir la comunicación entre la aplicación Android y el backend FastAPI.

Esta fase NO incluye:

* Repository
* ViewModels
* Pantallas
* Navegación
* DataStore

Solamente la capa de comunicación HTTP.

---

# Dependencias requeridas

Verificar que ya existan:

```kotlin
implementation("com.squareup.retrofit2:retrofit")
implementation("com.squareup.retrofit2:converter-gson")
implementation("com.squareup.okhttp3:logging-interceptor")
```

---

# Backend utilizado

## Registro

```http
POST /auth/register
```

Body:

```json
{
  "name": "Hector",
  "lastname": "Flores",
  "email": "hector@gmail.com",
  "password": "123456"
}
```

Respuesta:

```json
{
  "user_id": 1,
  "name": "Hector",
  "email": "hector@gmail.com"
}
```

---

## Login

```http
POST /auth/login
```

Body:

```json
{
  "email": "hector@gmail.com",
  "password": "123456"
}
```

Respuesta:

```json
{
  "access_token": "jwt_token"
}
```

---

# Estructura esperada

```text
data/
└── remote/
    └── ApiService.kt

di/
└── NetworkModule.kt
```

---

# Tarea 1 — Crear ApiService

## Objetivo

Definir todos los endpoints HTTP.

### Archivo

```text
data/remote/ApiService.kt
```

### Checklist

* [x] Crear interfaz ApiService

### Resultado esperado

```kotlin
interface ApiService
```

---

# Tarea 2 — Implementar endpoint Register

## Objetivo

Consumir:

```http
POST /auth/register
```

### Checklist

* [x] Agregar anotación @POST
* [x] Agregar body RegisterRequest
* [x] Retornar RegisterResponse

### Resultado esperado

```kotlin
@POST("auth/register")
suspend fun register(
    @Body request: RegisterRequest
): RegisterResponse
```

---

# Tarea 3 — Implementar endpoint Login

## Objetivo

Consumir:

```http
POST /auth/login
```

### Checklist

* [x] Agregar anotación @POST
* [x] Agregar body LoginRequest
* [x] Retornar LoginResponse

### Resultado esperado

```kotlin
@POST("auth/login")
suspend fun login(
    @Body request: LoginRequest
): LoginResponse
```

---

# Tarea 4 — Configurar Logging Interceptor

## Objetivo

Visualizar requests y responses durante desarrollo.

### Archivo

```text
di/NetworkModule.kt
```

### Checklist

* [x] Crear HttpLoggingInterceptor
* [x] Configurar BODY level

### Resultado esperado

```kotlin
HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
```

---

# Tarea 5 — Configurar OkHttpClient

## Objetivo

Centralizar configuración HTTP.

### Checklist

* [x] Crear OkHttpClient
* [x] Agregar LoggingInterceptor

### Resultado esperado

```kotlin
OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()
```

---

# Tarea 6 — Configurar Retrofit

## Objetivo

Crear instancia principal de Retrofit.

### Checklist

* [x] Definir BASE_URL
* [x] Agregar GsonConverterFactory
* [x] Agregar OkHttpClient

### Resultado esperado

```kotlin
Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(
        GsonConverterFactory.create()
    )
    .build()
```

---

# Tarea 7 — Crear instancia ApiService

## Objetivo

Permitir inyección mediante Hilt.

### Checklist

* [x] Crear provideApiService()

### Resultado esperado

```kotlin
retrofit.create(ApiService::class.java)
```

---

# Tarea 8 — Configurar BASE_URL

## Objetivo

Apuntar al backend FastAPI.

### Desarrollo local

Emulador Android Studio:

```text
http://10.0.2.2:8001/
```

Dispositivo físico:

```text
http://192.168.X.X:8001/
```

### Checklist

* [x] Definir BASE_URL correcta

---

# Tarea 9 — Permitir tráfico HTTP

## Objetivo

Evitar errores ClearText HTTP.

### Archivo

```text
AndroidManifest.xml
```

### Checklist

* [x] Agregar usesCleartextTraffic
* [x] Agregar INTERNET permission

### Resultado esperado

```xml
<uses-permission android:name="android.permission.INTERNET" />

<application
    android:usesCleartextTraffic="true"
```

---

# Verificación Final

## ApiService

* [x] register()
* [x] login()

## Retrofit

* [x] Retrofit configurado

## OkHttp

* [x] Logging configurado

## Hilt

* [x] ApiService inyectable

## Android

* [x] HTTP permitido

---

# Entregables

```text
ApiService.kt

NetworkModule.kt
```

---

# Código esperado al finalizar

## ApiService

```kotlin
interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}
```

---

## Funcionalidades disponibles

* Registro de usuarios.
* Login de usuarios.
* Comunicación HTTP con FastAPI.
* Logs completos en Logcat.

---

# Próxima subfase

```text
FASE_2_4_REPOSITORY.md
```

En la siguiente fase implementaremos:

* AuthRepository
* register()
* login()
* manejo de excepciones
* Result<T>
* separación de lógica de red y UI
