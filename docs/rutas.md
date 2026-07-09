# Rutas del Backend

Fuente de verdad funcional: `docs/requerimientos_lapiz_inteligente.md`.

Todas las rutas son privadas salvo `GET /health` y `POST /auth/*`.

Autenticacion: `Authorization: Bearer <jwt>` (excepto rutas publicas).

---

## Salud

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/health` | Publica | Healthcheck del backend. |

**Request:** Sin body, sin parametros.

**Response (200):**
```json
{ "status": "ok" }
```

---

## Autenticacion

### `POST /auth/register`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| POST | `/auth/register` | Publica | Registro de tutor. |

**Request body:**
```json
{
  "name": "string (requerido)",
  "lastname": "string (requerido)",
  "email": "email (requerido)",
  "password": "string (requerido)",
  "phone": "string | null (opcional)"
}
```

**Response (201):**
```json
{
  "user_id": 1,
  "name": "string",
  "lastname": "string",
  "email": "string",
  "is_active": true
}
```

---

### `POST /auth/login`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| POST | `/auth/login` | Publica | Inicio de sesion y emision de JWT. |

**Request body:**
```json
{
  "email": "email (requerido)",
  "password": "string (requerido)"
}
```

**Response (200):**
```json
{
  "access_token": "string (jwt)",
  "token_type": "bearer"
}
```

---

### `GET /users/me`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/users/me` | Privada | Perfil del tutor autenticado. |

**Request:** Sin body, sin parametros. Header `Authorization: Bearer <jwt>`.

**Response (200):**
```json
{
  "user_id": 1,
  "name": "string",
  "lastname": "string",
  "email": "string",
  "phone": "string | null",
  "is_active": true,
  "created_at": "datetime"
}
```

---

## Alumnos

### `POST /children/`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| POST | `/children/` | Privada | Registra alumno del tutor autenticado. |

**Request body:**
```json
{
  "name": "string (requerido)",
  "birth_date": "date (opcional, ej: 2020-01-15)",
  "dominant_hand": "'derecha' | 'izquierda' | 'ambidiestro' (opcional, defecto: 'derecha')",
  "school_grade": "string | null (opcional)",
  "notes": "string | null (opcional)"
}
```

**Response (201):**
```json
{
  "child_id": 1,
  "user_id": 1,
  "name": "string",
  "birth_date": "date | null",
  "dominant_hand": "'derecha' | 'izquierda' | 'ambidiestro'",
  "school_grade": "string | null",
  "notes": "string | null",
  "is_active": true,
  "created_at": "datetime"
}
```

---

### `GET /children/`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/children/` | Privada | Lista alumnos activos del tutor autenticado. |

**Request:** Sin body, sin parametros.

**Response (200):** Array de `ChildResponse` (misma estructura que la respuesta de create).

---

### `GET /children/{child_id}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/children/{child_id}` | Privada | Detalle de alumno propio. |

**Path params:** `child_id` (integer)

**Response (200):** `ChildResponse`

**Error:** `404` si no existe o no pertenece al tutor.

---

### `PUT /children/{child_id}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| PUT | `/children/{child_id}` | Privada | Actualiza alumno propio. |

**Path params:** `child_id` (integer)

**Request body (todos opcionales):**
```json
{
  "name": "string | null (opcional)",
  "birth_date": "date | null (opcional)",
  "dominant_hand": "'derecha' | 'izquierda' | 'ambidiestro' (opcional)",
  "school_grade": "string | null (opcional)",
  "notes": "string | null (opcional)"
}
```

**Response (200):** `ChildResponse`

---

### `PATCH /children/{child_id}/deactivate`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| PATCH | `/children/{child_id}/deactivate` | Privada | Desactiva alumno sin borrar historial. |

**Path params:** `child_id` (integer)

**Request:** Sin body.

**Response (200):** `ChildResponse` con `is_active: false`

**Error:** `400` si ya esta desactivado.

---

## Tipos de Trazo

### `GET /stroke-types/`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/stroke-types/` | Privada | Lista tipos de trazo. |

**Request:** Sin body, sin parametros.

**Response (200):**
```json
[
  {
    "stroke_type_id": 1,
    "name": "string",
    "created_at": "datetime"
  }
]
```

---

## Ejercicios

### `GET /exercises/`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/exercises/` | Privada | Lista ejercicios activos. |

**Query params opcionales:** `?stroke_type_id=1`

**Response (200):**
```json
[
  {
    "exercise_id": 1,
    "name": "string",
    "description": "string | null",
    "stroke_type_id": 1,
    "is_active": true,
    "created_at": "datetime",
    "updated_at": "datetime"
  }
]
```

---

### `GET /exercises/{exercise_id}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/exercises/{exercise_id}` | Privada | Detalle de ejercicio. |

**Path params:** `exercise_id` (integer)

**Response (200):** `ExerciseResponse`

---

### `POST /exercises/`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| POST | `/exercises/` | Privada | Crea ejercicio. Pendiente restringir a administrador tecnico. |

**Request body:**
```json
{
  "name": "string (requerido)",
  "description": "string | null (opcional)",
  "stroke_type_id": 1
}
```

**Response (201):** `ExerciseResponse`

---

### `PUT /exercises/{exercise_id}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| PUT | `/exercises/{exercise_id}` | Privada | Actualiza ejercicio. |

**Path params:** `exercise_id` (integer)

**Request body (todos opcionales):**
```json
{
  "name": "string | null (opcional)",
  "description": "string | null (opcional)",
  "stroke_type_id": 1,
  "is_active": true
}
```

**Response (200):** `ExerciseResponse`

---

### `PATCH /exercises/{exercise_id}/deactivate`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| PATCH | `/exercises/{exercise_id}/deactivate` | Privada | Desactiva ejercicio. |

**Path params:** `exercise_id` (integer)

**Request:** Sin body.

**Response (200):** `ExerciseResponse` con `is_active: false`

---

## Lapices

### `POST /pencils/`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| POST | `/pencils/` | Privada | Registra lapiz inteligente con `device_uid` unico. |

**Request body:**
```json
{
  "device_uid": "string (requerido, unico)",
  "name": "string | null (opcional)",
  "firmware_version": "string | null (opcional)"
}
```

**Response (201):**
```json
{
  "pencil_id": 1,
  "device_uid": "string",
  "name": "string | null",
  "status": "available | in_use | inactive | maintenance | lost",
  "firmware_version": "string | null",
  "last_seen_at": "datetime | null",
  "created_at": "datetime",
  "updated_at": "datetime"
}
```

**Error:** `400` si `device_uid` ya existe.

---

### `GET /pencils/`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/pencils/` | Privada | Lista lapices. |

**Query params opcionales:** `?status=available` (valores: `available`, `in_use`, `inactive`, `maintenance`, `lost`)

**Response (200):** Array de `PencilResponse`.

---

### `GET /pencils/{pencil_id}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/pencils/{pencil_id}` | Privada | Consulta detalle y estado operativo del lapiz. |

**Path params:** `pencil_id` (integer)

**Response (200):** `PencilResponse`

---

### `PUT /pencils/{pencil_id}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| PUT | `/pencils/{pencil_id}` | Privada | Actualiza datos operativos del lapiz. |

**Path params:** `pencil_id` (integer)

**Request body:**
```json
{
  "name": "string | null (opcional)",
  "firmware_version": "string | null (opcional)"
}
```

**Response (200):** `PencilResponse`

---

### `PATCH /pencils/{pencil_id}/status`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| PATCH | `/pencils/{pencil_id}/status` | Privada | Cambia estado operativo del lapiz. |

**Path params:** `pencil_id` (integer)

**Request body:**
```json
{
  "status": "'available' | 'in_use' | 'inactive' | 'maintenance' | 'lost'"
}
```

**Response (200):** `PencilResponse`

Estados validos de lapiz: `available`, `in_use`, `inactive`, `maintenance`, `lost`.

---

## Sesiones

### `POST /sessions`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| POST | `/sessions` | Privada | Crea sesion, asocia alumno-ejercicio-lapiz e inicia simulacion MQTT. |

**Request body:**
```json
{
  "child_id": 1,
  "exercise_id": 1,
  "pencil_id": 1
}
```

**Response (201):**
```json
{
  "session_id": 1,
  "child_id": 1,
  "exercise_id": 1,
  "pencil_id": 1,
  "started_at": "datetime",
  "ended_at": null,
  "close_reason": null,
  "avg_pressure": null,
  "max_pressure": null,
  "pressure_stability": null,
  "movement_stability": null,
  "tremor_level": null,
  "posture_score": null,
  "total_errors": null,
  "feedback_count": null,
  "ai_score": null,
  "result_summary": null,
  "created_at": "datetime"
}
```

**Reglas:**
- El alumno debe pertenecer al tutor autenticado.
- El ejercicio debe existir y estar activo.
- El lapiz debe existir y estar `available`.
- Al crear la sesion, el lapiz pasa a `in_use`.

---

### `GET /sessions/active`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/sessions/active` | Privada | Recupera sesion activa por `child_id` o `pencil_id`. |

**Query params (obligatorio al menos uno):**
- `?child_id=1`
- `?pencil_id=1`

**Response (200):** `SessionResponse`

**Error:** `400` si no se envia `child_id` ni `pencil_id`. `404` si no hay sesion activa.

---

### `GET /sessions/{session_id}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/sessions/{session_id}` | Privada | Detalle de sesion propia. |

**Path params:** `session_id` (integer)

**Response (200):** `SessionResponse`

---

### `GET /children/{child_id}/sessions`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| GET | `/children/{child_id}/sessions` | Privada | Historial paginado por alumno propio. |

**Path params:** `child_id` (integer)

**Query params opcionales:**
- `?skip=0` (default: 0)
- `?limit=50` (default: 50, max: 100)

**Response (200):** Array de `SessionResponse`

---

### `PATCH /sessions/{session_id}/end`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| PATCH | `/sessions/{session_id}/end` | Privada | Cierra sesion, detiene simulacion, calcula metricas. |

**Path params:** `session_id` (integer)

**Request body:**
```json
{
  "close_reason": "'manual' (opcional, defecto: 'manual')"
}
```

**Response (200):** `SessionResponse` con metricas pobladas.

**Regla:** Al cerrar, si el lapiz sigue `in_use`, vuelve a `available`.

---

## WebSocket

### `WS /ws/sessions/{session_id}?token={jwt}`

| Metodo | Ruta | Estado | Descripcion |
|---|---|---|---|
| WS | `/ws/sessions/{session_id}` | Privada | Feedback de sesion en tiempo real. |

**Path params:** `session_id` (integer)

**Query params:** `?token=<jwt>` (obligatorio)

**Validacion antes de aceptar:**
- JWT valido y no expirado.
- Usuario activo.
- Sesion existe y pertenece al usuario.

**Conexion:** Bidireccional. El servidor envia datos de trazo/metricas en tiempo real.
