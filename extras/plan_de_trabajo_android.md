# Plan de Desarrollo Android — Lápiz Inteligente

## Convención de seguimiento

Este documento se utilizará como guía y control de avance del proyecto.

### Estados

```text
[ ] Tarea no iniciada
[x] Tarea completada
```

Ejemplo:

```text
[x] Crear proyecto Android
[x] Configurar Compose
[ ] Configurar Retrofit
[ ] Implementar Login
```

---

# FASE 1 — Infraestructura

## Objetivo

Preparar la arquitectura base del proyecto Android.

### Proyecto

* [ ] Crear proyecto Empty Activity
* [ ] Verificar compilación inicial
* [ ] Configurar namespace del proyecto
* [ ] Configurar nombre de la aplicación

### Compose

* [ ] Verificar Compose habilitado
* [ ] Verificar Material 3
* [ ] Ejecutar pantalla de prueba
* [ ] Confirmar compilación sin errores

### Hilt

* [ ] Agregar dependencias Hilt
* [ ] Configurar plugin Hilt
* [ ] Crear clase App.kt
* [ ] Agregar @HiltAndroidApp
* [ ] Registrar Application en Manifest
* [ ] Agregar @AndroidEntryPoint a MainActivity
* [ ] Verificar compilación

### Navigation

* [ ] Agregar Navigation Compose
* [ ] Crear Routes.kt
* [ ] Crear NavGraph.kt
* [ ] Configurar NavHost
* [ ] Crear navegación Login → Register
* [ ] Verificar navegación funcional

### Retrofit

* [ ] Agregar Retrofit
* [ ] Agregar Gson Converter
* [ ] Agregar Logging Interceptor
* [ ] Crear ApiService
* [ ] Crear RetrofitModule
* [ ] Configurar BASE_URL
* [ ] Probar conexión con backend

### DataStore

* [ ] Agregar dependencia DataStore
* [ ] Crear SessionManager
* [ ] Implementar saveToken()
* [ ] Implementar getToken()
* [ ] Implementar clearToken()
* [ ] Verificar persistencia

### Arquitectura

* [ ] Crear carpeta data
* [ ] Crear carpeta domain
* [ ] Crear carpeta ui
* [ ] Crear carpeta navigation
* [ ] Crear carpeta datastore
* [ ] Crear carpeta di
* [ ] Crear estructura base MVVM

### Validación Fase 1

* [ ] Proyecto compila
* [ ] Navigation funciona
* [ ] Hilt funciona
* [ ] Retrofit funciona
* [ ] DataStore funciona

---

# FASE 2 — Autenticación

## Backend utilizado

```http
POST /auth/register
POST /auth/login
```

### Modelos

* [ ] Crear LoginRequest
* [ ] Crear LoginResponse
* [ ] Crear RegisterRequest
* [ ] Crear RegisterResponse

### API

* [ ] Implementar endpoint register()
* [ ] Implementar endpoint login()

### Repository

* [ ] Crear AuthRepository
* [ ] Implementar register()
* [ ] Implementar login()

### Login Screen

* [ ] Crear pantalla Login
* [ ] Campo Email
* [ ] Campo Password
* [ ] Validación de campos vacíos
* [ ] Mostrar errores del backend
* [ ] Navegar al Dashboard

### Register Screen

* [ ] Crear pantalla Registro
* [ ] Campo Nombre
* [ ] Campo Apellido
* [ ] Campo Email
* [ ] Campo Password
* [ ] Validaciones
* [ ] Registrar usuario

### Persistencia JWT

* [ ] Guardar token después del login
* [ ] Recuperar token al abrir la app
* [ ] Eliminar token al cerrar sesión

### Splash Screen

* [ ] Crear SplashScreen
* [ ] Verificar token existente
* [ ] Navegar a Login si no existe token
* [ ] Navegar a Dashboard si existe token

### Logout

* [ ] Crear botón logout
* [ ] Limpiar DataStore
* [ ] Redirigir al Login

### Validación Fase 2

* [ ] Registro exitoso
* [ ] Login exitoso
* [ ] JWT persistido
* [ ] Logout funcional
* [ ] Splash funcional

---

# FASE 3 — Gestión de Niños

### API

* [ ] GET /children
* [ ] POST /children
* [ ] PUT /children/{id}
* [ ] PATCH /children/{id}/deactivate

### Modelos

* [ ] ChildResponse
* [ ] CreateChildRequest
* [ ] UpdateChildRequest

### Repository

* [ ] getChildren()
* [ ] createChild()
* [ ] updateChild()
* [ ] deactivateChild()

### Pantallas

#### ChildrenScreen

* [ ] Mostrar lista
* [ ] Mostrar estado activo
* [ ] Mostrar botón agregar

#### AddChildScreen

* [ ] Campo nombre
* [ ] Guardar niño

#### EditChildScreen

* [ ] Cargar información
* [ ] Actualizar información

#### Deactivate Child

* [ ] Mostrar diálogo
* [ ] Confirmar acción
* [ ] Refrescar lista

### Validación Fase 3

* [ ] Crear niño
* [ ] Editar niño
* [ ] Desactivar niño
* [ ] Listar niños

---

# FASE 4 — Ejercicios

### API

* [ ] GET /stroke-types
* [ ] GET /exercises
* [ ] GET /exercises/{id}

### Funcionalidades

* [ ] Listar ejercicios
* [ ] Filtrar por tipo de trazo
* [ ] Ver detalle
* [ ] Mostrar descripción
* [ ] Mostrar tipo de trazo

### Validación Fase 4

* [ ] Catálogo visible
* [ ] Filtros funcionales
* [ ] Detalle funcional

---

# FASE 5 — Sesiones

### API

* [ ] POST /sessions
* [ ] GET /sessions/{id}
* [ ] GET /children/{id}/sessions
* [ ] PATCH /sessions/{id}/end

### Funcionalidades

* [ ] Seleccionar niño
* [ ] Seleccionar ejercicio
* [ ] Crear sesión
* [ ] Guardar session_id
* [ ] Mostrar sesión activa
* [ ] Cerrar sesión
* [ ] Mostrar historial

### Validación Fase 5

* [ ] Crear sesión
* [ ] Ver detalle
* [ ] Cerrar sesión
* [ ] Consultar historial

---

# FASE 6 — Dashboard

### Métricas

* [ ] Mostrar avg_pressure
* [ ] Mostrar max_pressure
* [ ] Mostrar pressure_stability
* [ ] Mostrar movement_stability
* [ ] Mostrar tremor_level
* [ ] Mostrar posture_score

### Dashboard

* [ ] Tarjeta Presión
* [ ] Tarjeta Estabilidad
* [ ] Tarjeta Temblor
* [ ] Tarjeta Postura
* [ ] Resumen de desempeño
* [ ] Historial reciente

### Validación Fase 6

* [ ] Dashboard completo
* [ ] Datos reales desde API

---

# FASE 7 — WebSocket

### Infraestructura

* [ ] Agregar OkHttp WebSocket
* [ ] Crear WebSocketManager
* [ ] Implementar connect()
* [ ] Implementar disconnect()

### Sesión

* [ ] Conectar al iniciar sesión activa
* [ ] Escuchar mensajes
* [ ] Parsear JSON recibido
* [ ] Actualizar UI

### Feedback

* [ ] Mostrar Snackbar
* [ ] Mostrar alertas warning
* [ ] Mostrar alertas info

### Validación Fase 7

* [ ] WebSocket conectado
* [ ] Alertas recibidas
* [ ] Feedback visible

---

# FASE 8 — Testing

### Repository Tests

* [ ] AuthRepository
* [ ] ChildRepository
* [ ] ExerciseRepository
* [ ] SessionRepository

### ViewModel Tests

* [ ] LoginViewModel
* [ ] RegisterViewModel
* [ ] ChildrenViewModel
* [ ] SessionsViewModel

### Casos críticos

* [ ] Token inválido
* [ ] Token expirado
* [ ] Backend apagado
* [ ] Sin internet
* [ ] Error 500

### Validación Final

* [ ] MVP Android compilando
* [ ] Todas las pantallas funcionales
* [ ] Integración completa con FastAPI
* [ ] Listo para integración BLE

