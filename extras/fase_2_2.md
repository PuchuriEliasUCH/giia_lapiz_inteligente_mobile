# FASE_2_1_MODELOS.md

# Fase 2.1 — Modelos de Autenticación

## Objetivo

Crear todos los DTOs (Data Transfer Objects) necesarios para comunicarse con los endpoints de autenticación del backend.

Esta fase NO incluye:

* Retrofit
* Repository
* ViewModels
* Pantallas
* Navegación

Solamente los modelos.

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
models/
└── auth/
    ├── LoginRequest.kt
    ├── LoginResponse.kt
    ├── RegisterRequest.kt
    └── RegisterResponse.kt
```

---

# Tarea 1 — Crear paquete auth

### Ruta

```text
models/auth
```

### Checklist

* [x] Crear carpeta auth dentro de models

### Resultado esperado

```text
models
└── auth
```

---

# Tarea 2 — LoginRequest

## Objetivo

Representar el body enviado al endpoint login.

### Archivo

```text
models/auth/LoginRequest.kt
```

### Propiedades

| Campo    | Tipo   |
| -------- | ------ |
| email    | String |
| password | String |

### Checklist

* [x] Crear LoginRequest
* [x] Agregar email
* [x] Agregar password

### Resultado esperado

```kotlin
data class LoginRequest(
    val email: String,
    val password: String
)
```

---

# Tarea 3 — LoginResponse

## Objetivo

Representar la respuesta del backend después del login.

### Archivo

```text
models/auth/LoginResponse.kt
```

### Propiedades

| Campo        | Tipo   |
| ------------ | ------ |
| access_token | String |

### Checklist

* [x] Crear LoginResponse
* [x] Agregar access_token

### Resultado esperado

```kotlin
data class LoginResponse(
    val access_token: String
)
```

---

# Tarea 4 — RegisterRequest

## Objetivo

Representar el body enviado al endpoint de registro.

### Archivo

```text
models/auth/RegisterRequest.kt
```

### Propiedades

| Campo    | Tipo   |
| -------- | ------ |
| name     | String |
| lastname | String |
| email    | String |
| password | String |

### Checklist

* [x] Crear RegisterRequest
* [x] Agregar name
* [x] Agregar lastname
* [x] Agregar email
* [x] Agregar password

### Resultado esperado

```kotlin
data class RegisterRequest(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String
)
```

---

# Tarea 5 — RegisterResponse

## Objetivo

Representar la respuesta del backend después del registro.

### Archivo

```text
models/auth/RegisterResponse.kt
```

### Propiedades

| Campo   | Tipo   |
| ------- | ------ |
| user_id | Int    |
| name    | String |
| email   | String |

### Checklist

* [x] Crear RegisterResponse
* [x] Agregar user_id
* [x] Agregar name
* [x] Agregar email

### Resultado esperado

```kotlin
data class RegisterResponse(
    val user_id: Int,
    val name: String,
    val email: String
)
```

---

# Verificación Final

## Estructura

```text
models/
└── auth/
    ├── LoginRequest.kt
    ├── LoginResponse.kt
    ├── RegisterRequest.kt
    └── RegisterResponse.kt
```

---

## Checklist Final

* [x] LoginRequest creado
* [x] LoginResponse creado
* [x] RegisterRequest creado
* [x] RegisterResponse creado
* [x] Proyecto compila

---

# Entregables

```text
LoginRequest.kt
LoginResponse.kt
RegisterRequest.kt
RegisterResponse.kt
```

Estado esperado:

* Todos los DTOs de autenticación creados.
* Sin errores de compilación.
* Listo para iniciar Fase 2.2 — API.
