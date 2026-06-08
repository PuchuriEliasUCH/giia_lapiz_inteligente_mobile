# FASE_5_SESIONES.md

# Fase 5 — Gestión de Sesiones

## Objetivo

Implementar el flujo principal de la aplicación.

Una sesión representa una actividad realizada por un niño sobre un ejercicio específico.

Esta fase conecta:

```text
Niño
    +
Ejercicio
    +
Métricas
    =
Sesión
```

Al finalizar esta fase el usuario podrá:

* Seleccionar un niño.
* Seleccionar un ejercicio.
* Crear una sesión.
* Consultar una sesión.
* Finalizar una sesión.
* Consultar historial de sesiones.

---

# Requisitos Previos

## Fase 2

* [ ] Login funcional
* [ ] JWT funcional

## Fase 3

* [ ] Gestión de niños funcional

## Fase 4

* [ ] Catálogo de ejercicios funcional

---

# Backend Utilizado

## Crear sesión

```http
POST /sessions
Authorization: Bearer {token}
```

Body:

```json
{
  "child_id": 1,
  "exercise_id": 5
}
```

---

## Obtener sesión

```http
GET /sessions/{id}
Authorization: Bearer {token}
```

---

## Historial del niño

```http
GET /children/{id}/sessions
Authorization: Bearer {token}
```

---

## Finalizar sesión

```http
PATCH /sessions/{id}/end
Authorization: Bearer {token}
```

Body:

```json
{
  "close_reason": "manual"
}
```

---

# Estructura Esperada

```text
models/
└── session/
    ├── CreateSessionRequest.kt
    ├── SessionResponse.kt
    ├── SessionHistoryResponse.kt
    └── EndSessionRequest.kt

data/
└── repository/
    └── SessionRepository.kt

ui/
└── sessions/
    ├── SessionsScreen.kt
    ├── SessionDetailScreen.kt
    ├── SessionHistoryScreen.kt
    ├── SessionsViewModel.kt
    ├── SessionUiState.kt
    └── SessionCard.kt
```

---

# FASE 5.1 — Modelos

## Objetivo

Representar los datos enviados y recibidos por la API.

---

## Checklist

### CreateSessionRequest

* [ ] child_id
* [ ] exercise_id

### EndSessionRequest

* [ ] close_reason

### SessionResponse

* [ ] session_id
* [ ] child_id
* [ ] exercise_id
* [ ] started_at
* [ ] ended_at
* [ ] status

### SessionHistoryResponse

* [ ] session_id
* [ ] exercise_name
* [ ] started_at
* [ ] ended_at

---

## Validación

* [ ] Todos los modelos compilan
* [ ] Coinciden con el backend

---

# FASE 5.2 — API

## Objetivo

Agregar endpoints de sesiones.

---

## POST /sessions

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Authorization
* [ ] Agregar CreateSessionRequest

---

## GET /sessions/{id}

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Path id
* [ ] Agregar Authorization

---

## GET /children/{id}/sessions

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Path childId
* [ ] Agregar Authorization

---

## PATCH /sessions/{id}/end

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Path sessionId
* [ ] Agregar EndSessionRequest

---

## Validación

* [ ] Todos los endpoints responden

---

# FASE 5.3 — Repository

## Objetivo

Centralizar lógica de sesiones.

---

## Crear SessionRepository

### Checklist

* [ ] Crear archivo
* [ ] Inyectar ApiService

---

## Funciones

### Crear sesión

* [ ] createSession()

### Obtener sesión

* [ ] getSession()

### Historial

* [ ] getChildSessions()

### Finalizar

* [ ] endSession()

---

## Manejo de errores

* [ ] IOException
* [ ] HttpException
* [ ] Result<T>

---

## Validación

* [ ] Todas las funciones operativas

---

# FASE 5.4 — ViewModel

## Objetivo

Administrar estado de sesiones.

---

## SessionUiState

### Estados

* [ ] Loading
* [ ] Success
* [ ] Error
* [ ] Empty

---

## SessionsViewModel

### Funciones

* [ ] createSession()
* [ ] getSession()
* [ ] getChildSessions()
* [ ] endSession()

### StateFlow

* [ ] Configurado

---

## Validación

* [ ] Estado actualizado correctamente

---

# FASE 5.5 — Crear Sesión

## Objetivo

Iniciar una nueva sesión.

---

## Archivo

```text
SessionsScreen.kt
```

---

## Seleccionar Niño

### Checklist

* [ ] Cargar niños
* [ ] Dropdown de niños
* [ ] Mostrar nombre

---

## Seleccionar Ejercicio

### Checklist

* [ ] Cargar ejercicios
* [ ] Dropdown de ejercicios
* [ ] Mostrar nombre

---

## Crear Sesión

### Botón

* [ ] Iniciar sesión

### Acción

* [ ] Consumir POST /sessions

---

## Guardar session_id

### Checklist

* [ ] Obtener session_id respuesta
* [ ] Guardar en ViewModel
* [ ] Guardar sesión activa

---

## Validación

* [ ] Sesión creada correctamente

---

# FASE 5.6 — Sesión Activa

## Objetivo

Mostrar información de la sesión en curso.

---

## Archivo

```text
SessionDetailScreen.kt
```

---

## Información visible

### Niño

* [ ] Nombre

### Ejercicio

* [ ] Nombre

### Estado

* [ ] Activa

### Inicio

* [ ] Fecha inicio

---

## Acciones

### Finalizar

* [ ] Botón finalizar sesión

---

## Validación

* [ ] Información correcta

---

# FASE 5.7 — Cerrar Sesión

## Objetivo

Finalizar sesión manualmente.

---

## Acción

### Consumir

```http
PATCH /sessions/{id}/end
```

---

## Request

* [ ] close_reason

---

## UI

### Confirmación

* [ ] AlertDialog

---

## Resultado

* [ ] Estado finalizado
* [ ] Volver a historial

---

## Validación

* [ ] Sesión cerrada

---

# FASE 5.8 — Historial

## Objetivo

Consultar sesiones realizadas.

---

## Archivo

```text
SessionHistoryScreen.kt
```

---

## Obtener historial

### Endpoint

```http
GET /children/{id}/sessions
```

---

## Mostrar

### Información

* [ ] Fecha
* [ ] Ejercicio
* [ ] Duración
* [ ] Estado

---

## Componentes

* [ ] LazyColumn
* [ ] SessionCard

---

## Estados

### Loading

* [ ] Visible

### Empty

* [ ] Visible

### Error

* [ ] Visible

---

## Validación

* [ ] Historial visible

---

# FASE 5.9 — Navegación

## Routes

### Agregar

* [ ] SESSIONS
* [ ] SESSION_DETAIL
* [ ] SESSION_HISTORY

---

## NavGraph

### Registrar

* [ ] SessionsScreen
* [ ] SessionDetailScreen
* [ ] SessionHistoryScreen

---

## Navegaciones

### Crear → Detalle

* [ ] Funciona

### Detalle → Historial

* [ ] Funciona

### Historial → Detalle

* [ ] Funciona

---

# FASE 5.10 — Validación Final

## Crear

* [ ] Crear sesión

## Consultar

* [ ] Obtener detalle

## Finalizar

* [ ] Cerrar sesión

## Historial

* [ ] Consultar historial

## Navegación

* [ ] Flujo completo funcional

## Seguridad

* [ ] JWT enviado correctamente

---

# Entregables

```text
models/session/
├── CreateSessionRequest.kt
├── SessionResponse.kt
├── SessionHistoryResponse.kt
└── EndSessionRequest.kt

data/repository/
└── SessionRepository.kt

ui/sessions/
├── SessionsScreen.kt
├── SessionDetailScreen.kt
├── SessionHistoryScreen.kt
├── SessionsViewModel.kt
├── SessionUiState.kt
└── SessionCard.kt
```

Estado esperado:

* [ ] Usuario puede iniciar sesiones.
* [ ] Usuario puede finalizar sesiones.
* [ ] Usuario puede consultar historial.
* [ ] Fase 5 completada al 100%.
