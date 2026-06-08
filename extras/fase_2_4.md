# SUBFASES RESTANTES — FASE 2 AUTENTICACIÓN

```text
FASE 2.1 — Modelos ✅
FASE 2.2 — API ✅
FASE 2.3 — Repository
FASE 2.4 — Login
FASE 2.5 — Registro
FASE 2.6 — Persistencia JWT
FASE 2.7 — Splash
FASE 2.8 — Logout
FASE 2.9 — Validación Final
```

---

# FASE 2.3 — Repository

## Objetivo

Crear una capa intermedia entre la UI y Retrofit.

---

### Archivos

```text
data/
└── repository/
    └── AuthRepository.kt
```

---

### Checklist

#### Crear Repository

* [x] Crear AuthRepository
* [x] Inyectar ApiService

#### Login

* [x] Implementar login()

#### Registro

* [x] Implementar register()

#### Manejo de errores

* [x] Capturar IOException
* [x] Capturar HttpException
* [x] Retornar Result<T>

#### Validación

* [x] Login responde correctamente
* [x] Register responde correctamente
* [x] Manejo de errores funcional

---

# FASE 2.4 — Login

## Objetivo

Permitir autenticación de usuarios.

---

### Archivos

```text
ui/
└── auth/
    ├── LoginScreen.kt
    ├── LoginViewModel.kt
    └── AuthUiState.kt
```

---

### LoginViewModel

#### Checklist

* [x] Crear LoginViewModel
* [x] Inyectar AuthRepository
* [x] Crear StateFlow
* [x] Implementar login()

---

### LoginScreen

#### Campos

* [x] Email
* [x] Password

#### Botones

* [x] Iniciar sesión
* [x] Ir a Registro

#### Validaciones

* [x] Email vacío
* [x] Password vacía
* [x] Formato email válido

#### Estados

* [x] Loading
* [x] Success
* [x] Error

#### Backend

* [x] Consumir login()

#### Navegación

* [x] Navegar Dashboard

---

### Validación

* [x] Login exitoso
* [x] Login incorrecto
* [x] Error de red controlado

---

# FASE 2.5 — Registro

## Objetivo

Permitir registrar nuevos tutores.

---

### Archivos

```text
ui/
└── auth/
    ├── RegisterScreen.kt
    └── RegisterViewModel.kt
```

---

### RegisterViewModel

#### Checklist

* [x] Crear RegisterViewModel
* [x] Inyectar AuthRepository
* [x] Implementar register()

---

### RegisterScreen

#### Campos

* [x] Nombre
* [x] Apellido
* [x] Email
* [x] Password

#### Botones

* [x] Registrarse
* [x] Volver a Login

#### Validaciones

* [x] Nombre vacío
* [x] Apellido vacío
* [x] Email vacío
* [x] Email inválido
* [x] Password vacía
* [x] Password mínima

#### Backend

* [x] Consumir register()

#### Navegación

* [x] Volver a Login tras registro exitoso

---

### Validación

* [x] Registro exitoso
* [x] Email duplicado
* [x] Error de red controlado

---

# FASE 2.6 — Persistencia JWT

## Objetivo

Mantener la sesión iniciada.

---

### Archivos

```text
data/
└── datastore/
    └── SessionManager.kt
```

---

### Checklist

#### Claves

* [x] TOKEN_KEY

#### Funciones

* [x] saveToken()
* [x] getToken()
* [x] clearToken()

#### Login

* [x] Guardar token tras login

#### Lectura

* [x] Recuperar token

#### Eliminación

* [x] Eliminar token

---

### Validación

* [x] Token guardado
* [x] Token recuperado
* [x] Token eliminado

---

# FASE 2.7 — Splash

## Objetivo

Determinar automáticamente la pantalla inicial.

---

### Archivos

```text
ui/
└── splash/
    ├── SplashScreen.kt
    └── SplashViewModel.kt
```

---

### SplashViewModel

#### Checklist

* [x] Inyectar SessionManager
* [x] Verificar token

---

### Lógica

#### Si existe token

* [x] Navegar Dashboard

#### Si no existe token

* [x] Navegar Login

---

### UI

* [x] Crear SplashScreen
* [x] Mostrar logo
* [x] Mostrar loading

---

### Validación

* [x] Token encontrado
* [x] Token inexistente
* [x] Navegación correcta

---

# FASE 2.8 — Logout

## Objetivo

Cerrar sesión correctamente.

---

### Dashboard

#### Checklist

* [x] Agregar botón Logout

---

### SessionManager

#### Checklist

* [x] Ejecutar clearToken()

---

### Navegación

#### Checklist

* [x] Regresar a Login
* [x] Limpiar backstack

---

### Validación

* [x] JWT eliminado
* [x] Usuario redirigido
* [x] No puede volver con botón atrás

---

# FASE 2.9 — Validación Final

## Registro

* [x] Usuario registrado correctamente

## Login

* [x] Usuario autenticado correctamente

## JWT

* [x] JWT almacenado
* [x] JWT recuperado

## Splash

* [x] Recuperación automática de sesión

## Logout

* [x] Logout funcional

## Navegación

* [x] Login → Register
* [x] Register → Login
* [x] Login → Dashboard
* [x] Splash → Dashboard

## Manejo de errores

* [x] Error 401
* [x] Error 400
* [x] Error 500
* [x] Sin internet

---

# Entregables Finales Fase 2

```text
models/
└── auth/
    ├── LoginRequest.kt
    ├── LoginResponse.kt
    ├── RegisterRequest.kt
    └── RegisterResponse.kt

data/
├── repository/
│   └── AuthRepository.kt
│
└── datastore/
    └── SessionManager.kt

ui/
├── auth/
│   ├── LoginScreen.kt
│   ├── RegisterScreen.kt
│   ├── LoginViewModel.kt
│   ├── RegisterViewModel.kt
│   └── AuthUiState.kt
│
└── splash/
    ├── SplashScreen.kt
    └── SplashViewModel.kt
```

Estado esperado:

* [x] Usuario puede registrarse
* [x] Usuario puede iniciar sesión
* [x] JWT persiste entre reinicios
* [x] Splash detecta sesión existente
* [x] Logout funciona correctamente
* [x] Fase 2 completada al 100%
