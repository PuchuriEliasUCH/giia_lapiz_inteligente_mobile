# FASE_2_AUTENTICACION.md

# Fase 2 — Autenticación

## Objetivo

Permitir que un tutor pueda:

* Registrarse.
* Iniciar sesión.
* Mantener su sesión activa mediante JWT.
* Recuperar automáticamente la sesión al abrir la aplicación.
* Cerrar sesión.

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
├── auth/
│   ├── LoginRequest.kt
│   ├── LoginResponse.kt
│   ├── RegisterRequest.kt
│   └── RegisterResponse.kt

data/
├── repository/
│   └── AuthRepository.kt

ui/
├── auth/
│   ├── LoginScreen.kt
│   ├── RegisterScreen.kt
│   ├── LoginViewModel.kt
│   ├── RegisterViewModel.kt
│   └── AuthUiState.kt

ui/
├── splash/
│   ├── SplashScreen.kt
│   └── SplashViewModel.kt
```

---

# BLOQUE 1 — Modelos

## Objetivo

Representar requests y responses del backend.

### Checklist

#### Login

* [x] Crear LoginRequest
* [x] Crear LoginResponse

#### Register

* [x] Crear RegisterRequest
* [x] Crear RegisterResponse

### Validación

* [x] Todos los modelos compilan
* [x] Los nombres coinciden con el backend

---

# BLOQUE 2 — API

## Objetivo

Implementar endpoints de autenticación.

### Checklist

#### Register

* [x] Crear endpoint register()

#### Login

* [x] Crear endpoint login()

### Validación

* [x] ApiService compila
* [x] Retrofit reconoce ambos endpoints

---

# BLOQUE 3 — Repository

## Objetivo

Centralizar llamadas de autenticación.

### Checklist

#### AuthRepository

* [x] Crear AuthRepository

#### Funciones

* [x] register()
* [x] login()

### Validación

* [x] Repository retorna datos correctamente
* [x] Manejo básico de errores

---

# BLOQUE 4 — Login

## Objetivo

Permitir autenticación.

### UI

* [x] Crear LoginScreen

### Campos

* [x] Email
* [x] Password

### Botones

* [x] Iniciar sesión
* [x] Ir a Registro

### Validaciones

* [x] Email vacío
* [x] Password vacío
* [x] Formato de email

### Backend

* [x] Consumir login()

### Errores

* [x] Mostrar credenciales inválidas
* [x] Mostrar errores de red

### Navegación

* [x] Navegar a Dashboard tras login exitoso

### Validación

* [x] Login exitoso
* [x] Login fallido
* [x] Error de red controlado

---

# BLOQUE 5 — Registro

## Objetivo

Permitir creación de cuenta.

### UI

* [x] Crear RegisterScreen

### Campos

* [x] Nombre
* [x] Apellido
* [x] Email
* [x] Password

### Botones

* [x] Registrarse
* [x] Volver a Login

### Validaciones

* [x] Nombre vacío
* [x] Apellido vacío
* [x] Email vacío
* [x] Email inválido
* [x] Password vacía
* [x] Longitud mínima password

### Backend

* [x] Consumir register()

### Errores

* [x] Mostrar email duplicado
* [x] Mostrar error de red

### Validación

* [x] Registro exitoso
* [x] Registro rechazado
* [x] Error de red controlado

---

# BLOQUE 6 — Persistencia JWT

## Objetivo

Mantener la sesión del usuario.

### SessionManager

* [x] Guardar token
* [x] Recuperar token
* [x] Eliminar token

### Login

* [x] Guardar JWT después del login

### Validación

* [x] Token guardado
* [x] Token recuperado correctamente

---

# BLOQUE 7 — Splash

## Objetivo

Determinar pantalla inicial.

### UI

* [x] Crear SplashScreen

### ViewModel

* [x] Crear SplashViewModel

### Flujo

#### Token existe

* [x] Navegar Dashboard

#### Token no existe

* [x] Navegar Login

### Validación

* [x] Flujo correcto con token
* [x] Flujo correcto sin token

---

# BLOQUE 8 — Logout

## Objetivo

Cerrar sesión.

### Dashboard

* [x] Crear botón Logout

### SessionManager

* [x] Limpiar JWT

### Navegación

* [x] Volver a Login

### Validación

* [x] Token eliminado
* [x] Usuario redirigido

---

# Validación Final Fase 2

## Registro

* [x] Usuario registrado correctamente

## Login

* [x] Usuario autenticado correctamente

## JWT

* [x] JWT almacenado
* [x] Recuperación automática de sesión via Splash

## Logout

* [x] Cierre de sesión funcional

---

# Entregables

```text
LoginRequest.kt
LoginResponse.kt
RegisterRequest.kt
RegisterResponse.kt

AuthRepository.kt

LoginViewModel.kt
RegisterViewModel.kt

LoginScreen.kt
RegisterScreen.kt

SplashScreen.kt
SplashViewModel.kt
```

Estado esperado:

* Usuario puede registrarse.
* Usuario puede iniciar sesión.
* JWT persiste entre reinicios.
* Splash decide correctamente la pantalla inicial.
* Logout funcional.
