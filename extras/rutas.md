# Referencia de Endpoints — API Lápiz Inteligente

---

## `GET /health`

Sin autenticación.

**Respuesta:**
```json
{
  "status": "ok"
}
```
**Códigos:** 200

---

## `POST /auth/register`

Registro de tutor. Sin autenticación.

**Body (JSON):**
| Campo | Tipo | Requerido | Defecto |
|---|---|---|---|
| `name` | string | sí | — |
| `lastname` | string | sí | — |
| `email` | string (email) | sí | — |
| `password` | string | sí | — |
| `phone` | string \| null | no | `null` |

**Respuesta 201:**
| Campo | Tipo | Notas |
|---|---|---|
| `user_id` | int | PK |
| `name` | string | |
| `lastname` | string | |
| `email` | string | |
| `is_active` | bool | Siempre `true` en creación |
| `created_at` | datetime | |

**Códigos:** 201, 400 (email duplicado)

---

## `POST /auth/login`

Inicio de sesión. Sin autenticación.

**Body (JSON):**
| Campo | Tipo | Requerido |
|---|---|---|
| `email` | string (email) | sí |
| `password` | string | sí |

**Respuesta 200:**
| Campo | Tipo | Notas |
|---|---|---|
| `access_token` | string | JWT (HS256), expira en 30 min |
| `token_type` | string | `"bearer"` |

**Códigos:** 200, 401 (credenciales incorrectas)

---

## `POST /children/`

Crear un niño asociado al tutor autenticado.

**Auth:** `Authorization: Bearer <token>` (obligatorio)

**Body (JSON):**
| Campo | Tipo | Requerido | Defecto |
|---|---|---|---|
| `name` | string | sí | — |
| `birth_date` | date \| null | no | `null` |
| `dominant_hand` | `"derecha"` \| `"izquierda"` \| `"ambidiestro"` | no | `"derecha"` |
| `school_grade` | string \| null | no | `null` |
| `notes` | string \| null | no | `null` |

**Respuesta 201:**
| Campo | Tipo | Notas |
|---|---|---|
| `child_id` | int | PK |
| `user_id` | int | FK → users (propietario del token) |
| `name` | string | |
| `birth_date` | date \| null | |
| `dominant_hand` | string | |
| `school_grade` | string \| null | |
| `notes` | string \| null | |
| `is_active` | bool | |
| `created_at` | datetime | |

**Códigos:** 201, 401, 403

---

## `GET /children/`

Listar todos los niños del tutor autenticado.

**Auth:** `Authorization: Bearer <token>`

**Query params opcionales:** `?is_active=true`

**Respuesta 200:** Array de `ChildResponse`
```json
[
  {
    "child_id": 1,
    "user_id": 1,
    "name": "string",
    "birth_date": "2020-01-15",
    "dominant_hand": "derecha",
    "school_grade": "string|null",
    "notes": "string|null",
    "is_active": true,
    "created_at": "datetime"
  }
]
```

**Códigos:** 200, 401

---

## `GET /children/{child_id}`

Obtener un niño por ID. Valida que pertenezca al tutor autenticado.

**Auth:** `Authorization: Bearer <token>`

**Respuesta 200:** `ChildResponse` (mismos campos que `POST /children/`)

**Códigos:** 200, 404, 401

---

## `PUT /children/{child_id}`

Actualizar datos de un niño. Valida que pertenezca al tutor.

**Auth:** `Authorization: Bearer <token>`

**Body (JSON):** Todos los campos opcionales
| Campo | Tipo | Requerido |
|---|---|---|
| `name` | string | no |
| `birth_date` | date \| null | no |
| `dominant_hand` | string | no |
| `school_grade` | string \| null | no |
| `notes` | string \| null | no |

**Respuesta 200:** `ChildResponse`

**Códigos:** 200, 404, 401

---

## `PATCH /children/{child_id}/deactivate`

Desactivar un niño (borrado lógico). Valida que pertenezca al tutor.

**Auth:** `Authorization: Bearer <token>`

**Body:** Vacío

**Respuesta 200:** `ChildResponse` con `is_active: false`

**Códigos:** 200, 400 (ya desactivado), 404, 401

---

## `GET /stroke-types`

Listar tipos de trazo del catálogo.

**Auth:** `Authorization: Bearer <token>`

**Respuesta 200:**
| Campo | Tipo |
|---|---|
| `stroke_type_id` | int |
| `name` | string |
| `created_at` | datetime |

**Códigos:** 200, 401

---

## `GET /exercises`

Listar ejercicios activos.

**Auth:** `Authorization: Bearer <token>`

**Query params opcionales:** `?stroke_type_id=1`

**Respuesta 200:**
| Campo | Tipo | Notas |
|---|---|---|
| `exercise_id` | int | PK |
| `name` | string | |
| `description` | string \| null | |
| `stroke_type_id` | int | FK → stroke_types |
| `is_active` | bool | |
| `created_at` | datetime | |
| `updated_at` | datetime | |

**Códigos:** 200, 401

---

## `GET /exercises/{exercise_id}`

Obtener un ejercicio por ID.

**Auth:** `Authorization: Bearer <token>`

**Respuesta 200:** `ExerciseResponse` (mismos campos que `GET /exercises`)

**Códigos:** 200, 404, 401

---

## `POST /exercises`

Crear un ejercicio.

**Auth:** `Authorization: Bearer <token>`

**Body (JSON):**
| Campo | Tipo | Requerido | Defecto |
|---|---|---|---|
| `name` | string | sí | — |
| `description` | string \| null | no | `null` |
| `stroke_type_id` | int | sí | — |

**Respuesta 201:** `ExerciseResponse`

**Códigos:** 201, 400 (stroke_type inválido), 401

---

## `PUT /exercises/{exercise_id}`

Actualizar un ejercicio.

**Auth:** `Authorization: Bearer <token>`

**Body (JSON):** Todos los campos opcionales
| Campo | Tipo |
|---|---|
| `name` | string \| null |
| `description` | string \| null |
| `stroke_type_id` | int \| null |
| `is_active` | bool \| null |

**Respuesta 200:** `ExerciseResponse`

**Códigos:** 200, 404, 401

---

## `PATCH /exercises/{exercise_id}/deactivate`

Desactivar un ejercicio (borrado lógico).

**Auth:** `Authorization: Bearer <token>`

**Body:** Vacío

**Respuesta 200:** `ExerciseResponse` con `is_active: false`

**Códigos:** 200, 400 (ya desactivado), 404, 401

---

## `POST /sessions`

Crear una sesión. Valida que el `child_id` pertenezca al tutor autenticado.

**Auth:** `Authorization: Bearer <token>`

**Body (JSON):**
| Campo | Tipo | Requerido |
|---|---|---|
| `child_id` | int | sí |
| `exercise_id` | int | sí |

**Respuesta 201:**
| Campo | Tipo | Notas |
|---|---|---|
| `session_id` | int | PK |
| `child_id` | int | FK → children |
| `exercise_id` | int | FK → exercises |
| `started_at` | datetime | Timestamp de creación |
| `ended_at` | datetime \| null | `null` hasta cerrar |
| `close_reason` | string \| null | `null` hasta cerrar |
| `avg_pressure` | float \| null | |
| `max_pressure` | float \| null | |
| `pressure_stability` | float \| null | |
| `movement_stability` | float \| null | |
| `tremor_level` | float \| null | |
| `posture_score` | float \| null | |
| `total_errors` | int | 0 inicial |
| `feedback_count` | int | 0 inicial |
| `ai_score` | float \| null | |
| `result_summary` | string \| null | |
| `created_at` | datetime | |

**Códigos:** 201, 404 (child no encontrado o no es propio), 401

---

## `GET /sessions/{session_id}`

Obtener una sesión por ID. Valida que pertenezca al tutor (join `Session → Child.user_id`).

**Auth:** `Authorization: Bearer <token>`

**Respuesta 200:** `SessionResponse` (mismos campos que `POST /sessions`, con métricas pobladas si ya terminó)

**Códigos:** 200, 404, 401

---

## `GET /children/{child_id}/sessions`

Historial de sesiones de un niño. Valida que el niño pertenezca al tutor.

**Auth:** `Authorization: Bearer <token>`

**Query params:**
| Parámetro | Tipo | Defecto | Límite |
|---|---|---|---|
| `skip` | int | 0 | ≥ 0 |
| `limit` | int | 50 | 1–100 |

**Respuesta 200:** Array de `SessionResponse`

**Códigos:** 200, 401

---

## `PATCH /sessions/{session_id}/end`

Finalizar una sesión. Flushea el buffer RAM a CSV, calcula métricas y persiste en BD. Valida que la sesión pertenezca al tutor.

**Auth:** `Authorization: Bearer <token>`

**Body (JSON):**
| Campo | Tipo | Requerido | Defecto | Valores |
|---|---|---|---|---|
| `close_reason` | string | no | `"manual"` | `"manual"`, `"timeout"`, `"ble_disconnect"` |

**Respuesta 200:** `SessionResponse` con `ended_at`, `close_reason` y métricas calculadas.

**Códigos:** 200, 400 (ya terminada), 404, 401

---

## `ws://{host}:{port}/ws/sessions/{session_id}?token={jwt}`

Conexión WebSocket para recibir feedback en tiempo real durante una sesión.

**Auth:** `?token=` query param (JWT del tutor). No disponible `Authorization: Bearer` en WebSocket.

**Query params:**
| Parámetro | Tipo | Obligatorio |
|---|---|---|
| `token` | string | sí |

**Mensajes del servidor al cliente (JSON):**
| Campo | Tipo | Valores |
|---|---|---|
| `id` | int | ID de la alerta |
| `feedback` | string | Texto descriptivo |
| `severity` | string | `"info"` \| `"warning"` |

**Alertas posibles:**
| id | feedback | severity |
|---|---|---|
| 1 | Presión demasiado fuerte, intenta con más suavidad | warning |
| 2 | Presión muy baja, asegúrate de apoyar el lápiz | info |
| 3 | Temblor detectado, intenta mantener la mano firme | warning |

**Cierre:** Código `1008` si el token es inválido.

---

## `session/{session_id}/data` (MQTT — no HTTP)

Publicación continua desde el ESP32 hacia el backend. El backend se suscribe a `session/+/data`.

**Payload CSV (8 campos por línea):**
```
ts,ax,ay,az,gx,gy,gz,fsr
```

**Payload JSON (equivalente):**
```json
{
  "ts": "int",
  "ax": "float",
  "ay": "float",
  "az": "float",
  "gx": "float",
  "gy": "float",
  "gz": "float",
  "fsr": "int"
}
```

| Campo | Tipo | Unidad | Rango | Descripción |
|---|---|---|---|---|
| `ts` | int | ms | — | Timestamp desde boot del ESP32 |
| `ax` | float | m/s² | ±19.6 | Aceleración eje X |
| `ay` | float | m/s² | ±19.6 | Aceleración eje Y |
| `az` | float | m/s² | ±19.6 | Aceleración eje Z |
| `gx` | float | rad/s | ±250 | Giroscopio eje X |
| `gy` | float | rad/s | ±250 | Giroscopio eje Y |
| `gz` | float | rad/s | ±250 | Giroscopio eje Z |
| `fsr` | int | ADC raw | 0–4095 | Presión del trazo |

**Pipeline interno:** MQTT → buffer RAM → `evaluate_realtime()` → WebSocket → CSV → métricas → MySQL

---

## Errores comunes

| Código | Causa |
|---|---|
| 401 | Token faltante, inválido o expirado |
| 403 | Usuario inactivo |
| 404 | Recurso no encontrado o no pertenece al usuario |
| 400 | Validación de datos fallida, recurso ya en estado final, o violación de unicidad |
| 500 | Error interno del servidor |

---

## Resumen de autenticación por sección

| Sección | Auth | Método |
|---|---|---|
| Health | No | — |
| Auth | No | — |
| Children | Sí | `Authorization: Bearer <token>` |
| Exercises | Sí | `Authorization: Bearer <token>` |
| Sessions | Sí | `Authorization: Bearer <token>` |
| WebSocket | Sí | `?token={jwt}` vía query param |
| MQTT | No (broker interno) | — |

---

## Flujo típico completo

```
 1. POST /auth/register                         → crea tutor
 2. POST /auth/login                            → obtiene JWT
 3. POST /children/         (Bearer JWT)        → crea niño
 4. POST /exercises         (Bearer JWT)        → crea ejercicio
 5. POST /sessions          (Bearer JWT)        → inicia sesión

    === DURANTE LA SESIÓN ===
    ESP32 → MQTT session/{id}/data → buffer RAM
    evaluate_realtime() → WebSocket → feedback al cliente
    Watchdog: si 10 min sin datos → cierra con timeout

 6. PATCH /sessions/{id}/end (Bearer JWT)       → cierra sesión
    ├─ flush RAM → CSV (/raw_sessions/session_{id}.csv)
    ├─ calculate_metrics()
    └─ UPDATE MySQL con métricas + close_reason
```
