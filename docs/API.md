# API del Backend — App Mobile

Documentación para el cliente móvil (Android/Kotlin). Endpoints REST + WebSocket + MQTT.

---

## Base URL

```
http://{host}:8001
```

---

## Autenticación

Todas las rutas excepto `/health` y `/auth/*` requieren un token JWT en el header:

```
Authorization: Bearer <token>
```

El token se obtiene en login y expira en **30 minutos**. Al expirar, el servidor responde `401` y la app debe redirigir al login.

**Tokens de prueba:** Usar `POST /auth/login` con credenciales registradas.

---

## Resumen de endpoints

| Método | Ruta | Auth | Propósito |
|---|---|---|---|
| `GET` | `/health` | No | Verificar servidor |
| `POST` | `/auth/register` | No | Registrar tutor |
| `POST` | `/auth/login` | No | Iniciar sesión |
| `POST` | `/children/` | JWT | Crear niño |
| `GET` | `/children/` | JWT | Listar niños |
| `GET` | `/children/{id}` | JWT | Detalle niño |
| `PUT` | `/children/{id}` | JWT | Actualizar niño |
| `PATCH` | `/children/{id}/deactivate` | JWT | Desactivar niño |
| `GET` | `/stroke-types` | JWT | Tipos de trazo |
| `GET` | `/exercises` | JWT | Ejercicios activos |
| `GET` | `/exercises/{id}` | JWT | Detalle ejercicio |
| `POST` | `/exercises` | JWT | Crear ejercicio |
| `PUT` | `/exercises/{id}` | JWT | Actualizar ejercicio |
| `PATCH` | `/exercises/{id}/deactivate` | JWT | Desactivar ejercicio |
| `POST` | `/sessions` | JWT | Crear sesión |
| `GET` | `/sessions/{id}` | JWT | Detalle sesión |
| `GET` | `/children/{id}/sessions` | JWT | Historial sesiones |
| `PATCH` | `/sessions/{id}/end` | JWT | Finalizar sesión |
| `WS` | `/ws/sessions/{id}?token=` | JWT (query) | Feedback tiempo real |
| MQTT | `session/{id}/data` | No (interno) | Enviar datos IMU |

---

## Health

### `GET /health`

Verificar que el servidor esté operativo.

**Respuesta:**
```json
{
  "status": "ok"
}
```

**Códigos:** 200

---

## Auth

### `POST /auth/register`

Registrar un nuevo tutor.

**Request:**
```json
{
  "name": "Carlos",
  "lastname": "Pérez",
  "email": "carlos@example.com",
  "password": "miClave123",
  "phone": "999888777"
}
```

`phone` es opcional.

**Respuesta 201:**
```json
{
  "user_id": 1,
  "name": "Carlos",
  "lastname": "Pérez",
  "email": "carlos@example.com",
  "is_active": true,
  "created_at": "2026-06-08T12:00:00"
}
```

**Códigos:** 201, 400 (email ya registrado)

---

### `POST /auth/login`

Iniciar sesión y obtener JWT.

**Request:**
```json
{
  "email": "carlos@example.com",
  "password": "miClave123"
}
```

**Respuesta 200:**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIs...",
  "token_type": "bearer"
}
```

Guardar el `access_token`. Enviar en todas las llamadas posteriores como `Authorization: Bearer eyJhbG...`.

**Códigos:** 200, 401 (credenciales incorrectas)

---

## Children

Todas las rutas requieren `Authorization: Bearer <token>`.

### `POST /children/`

Crear un niño asociado al tutor.

**Request:**
```json
{
  "name": "Mateo",
  "birth_date": "2020-03-15",
  "dominant_hand": "derecha",
  "school_grade": "Inicial 5 años",
  "notes": "Tiene dificultad con curvas"
}
```

| Campo | Requerido | Valores |
|---|---|---|
| `name` | sí | — |
| `birth_date` | no | ISO date, null |
| `dominant_hand` | no | `"derecha"`, `"izquierda"`, `"ambidiestro"` (default: `"derecha"`) |
| `school_grade` | no | string, null |
| `notes` | no | string, null |

**Respuesta 201:**
```json
{
  "child_id": 1,
  "user_id": 1,
  "name": "Mateo",
  "birth_date": "2020-03-15",
  "dominant_hand": "derecha",
  "school_grade": "Inicial 5 años",
  "notes": "Tiene dificultad con curvas",
  "is_active": true,
  "created_at": "2026-06-08T12:00:00"
}
```

**Códigos:** 201, 401

---

### `GET /children/`

Listar niños del tutor.

**Query opcional:** `?is_active=true`

**Respuesta 200:** Array de objetos con la misma estructura que `POST /children/`.

**Códigos:** 200, 401

---

### `GET /children/{child_id}`

Detalle de un niño.

**Respuesta 200:** Ídem `POST /children/`.

**Códigos:** 200, 404, 401

---

### `PUT /children/{child_id}`

Actualizar datos de un niño. Todos los campos opcionales.

**Request:**
```json
{
  "name": "Mateo G.",
  "school_grade": "Primer grado"
}
```

**Respuesta 200:** `ChildResponse` actualizado.

**Códigos:** 200, 404, 401

---

### `PATCH /children/{child_id}/deactivate`

Desactivar niño (borrado lógico). Sin body.

**Respuesta 200:** `ChildResponse` con `is_active: false`.

**Códigos:** 200, 400 (ya desactivado), 404, 401

---

## Exercises

Todas las rutas requieren `Authorization: Bearer <token>`.

### `GET /stroke-types`

Lista fija de 10 tipos de trazo.

**Respuesta 200:**
```json
[
  {
    "stroke_type_id": 1,
    "name": "recto_vertical",
    "created_at": "2026-01-01T00:00:00"
  },
  {
    "stroke_type_id": 2,
    "name": "recto_horizontal",
    "created_at": "2026-01-01T00:00:00"
  }
]
```

**Códigos:** 200, 401

---

### `GET /exercises`

Listar ejercicios activos.

**Query opcional:** `?stroke_type_id=3`

**Respuesta 200:**
```json
[
  {
    "exercise_id": 1,
    "name": "Líneas verticales",
    "description": null,
    "stroke_type_id": 1,
    "is_active": true,
    "created_at": "2026-01-01T00:00:00",
    "updated_at": "2026-01-01T00:00:00"
  }
]
```

**Códigos:** 200, 401

---

### `GET /exercises/{exercise_id}`

Detalle de un ejercicio.

**Respuesta 200:** Ídem `GET /exercises`.

**Códigos:** 200, 404, 401

---

### `POST /exercises`

Crear nuevo ejercicio (para tutores que quieran personalizar).

**Request:**
```json
{
  "name": "Trazos en zigzag",
  "description": "Ejercicio para mejorar control de cambios de dirección",
  "stroke_type_id": 3
}
```

**Respuesta 201:** `ExerciseResponse`.

**Códigos:** 201, 400, 401

---

### `PUT /exercises/{exercise_id}`

Actualizar ejercicio. Todos los campos opcionales.

**Códigos:** 200, 404, 401

---

### `PATCH /exercises/{exercise_id}/deactivate`

Desactivar ejercicio. Sin body.

**Códigos:** 200, 400 (ya desactivado), 404, 401

---

## Sessions

Todas las rutas requieren `Authorization: Bearer <token>`. El servidor valida que la sesión pertenezca a un niño del tutor.

### `POST /sessions`

Iniciar una sesión de práctica. El `session_id` devuelto se usará en MQTT y WebSocket.

**Request:**
```json
{
  "child_id": 1,
  "exercise_id": 1
}
```

**Respuesta 201:**
```json
{
  "session_id": 10,
  "child_id": 1,
  "exercise_id": 1,
  "started_at": "2026-06-08T12:00:00",
  "ended_at": null,
  "close_reason": null,
  "avg_pressure": null,
  "max_pressure": null,
  "pressure_stability": null,
  "movement_stability": null,
  "tremor_level": null,
  "posture_score": null,
  "total_errors": 0,
  "feedback_count": 0,
  "ai_score": null,
  "result_summary": null,
  "created_at": "2026-06-08T12:00:00"
}
```

Todos los campos de métricas son `null` hasta que la sesión termine.

**Códigos:** 201, 404 (child no encontrado), 401

---

### `GET /sessions/{session_id}`

Obtener detalle de una sesión.

**Respuesta 200:** Misma estructura que `POST /sessions`. Si la sesión ya terminó, los campos de métricas estarán poblados.

**Códigos:** 200, 404, 401

---

### `GET /children/{child_id}/sessions`

Historial de sesiones de un niño.

**Query params:** `?skip=0&limit=50` (máx 100)

**Respuesta 200:** Array de `SessionResponse`.

**Códigos:** 200, 401

---

### `PATCH /sessions/{session_id}/end`

Finalizar una sesión. El servidor flushea los datos a CSV, calcula métricas y guarda en BD.

**Request:**
```json
{
  "close_reason": "manual"
}
```

| `close_reason` | Cuándo usarlo |
|---|---|
| `"manual"` | Tutor presiona "Terminar" en la app |
| `"timeout"` | (Asignado automáticamente por el watchdog) |
| `"ble_disconnect"` | Pérdida de conexión BLE con el lápiz |

**Respuesta 200:** `SessionResponse` con todos los campos de métricas poblados y `ended_at` con timestamp.

**Ejemplo de respuesta:**
```json
{
  "session_id": 10,
  "child_id": 1,
  "exercise_id": 1,
  "started_at": "2026-06-08T12:00:00",
  "ended_at": "2026-06-08T12:05:30",
  "close_reason": "manual",
  "avg_pressure": 1520.5,
  "max_pressure": 3800.0,
  "pressure_stability": 0.72,
  "movement_stability": 0.85,
  "tremor_level": 0.35,
  "posture_score": 0.92,
  "total_errors": 3,
  "feedback_count": 5,
  "ai_score": null,
  "result_summary": null,
  "created_at": "2026-06-08T12:00:00"
}
```

**Códigos:** 200, 400 (ya terminada), 404, 401

---

## WebSocket — Feedback en Tiempo Real

Conectar durante la sesión para recibir alertas del backend.

### Conexión

```
ws://{host}:8001/ws/sessions/{session_id}?token={jwt}
```

### Consideraciones

| Aspecto | Detalle |
|---|---|
| Auth | Token JWT vía query param `?token=` |
| Cierre auth inválida | Código `1008` |
| Formato mensajes | JSON |
| Dirección | Servidor → Cliente (solo lectura para el cliente) |

### Formato de mensajes

```json
{
  "id": 1,
  "feedback": "Presión demasiado fuerte, intenta con más suavidad",
  "severity": "warning"
}
```

### Alertas posibles

| id | feedback | severity |
|---|---|---|
| 1 | Presión demasiado fuerte, intenta con más suavidad | `warning` |
| 2 | Presión muy baja, asegúrate de apoyar el lápiz | `info` |
| 3 | Temblor detectado, intenta mantener la mano firme | `warning` |

### Implementación recomendada (Android)

```kotlin
// Ejemplo con OkHttp
val client = OkHttpClient()
val request = Request.Builder()
    .url("ws://{host}:8001/ws/sessions/$sessionId?token=$jwt")
    .build()
val ws = client.newWebSocket(request, object : WebSocketListener() {
    override fun onMessage(webSocket: WebSocket, text: String) {
        // text: {"id":1,"feedback":"...","severity":"warning"}
        // Parsear JSON y mostrar feedback al niño
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        // Reintentar o mostrar error
    }
})
```

---

## MQTT — Envío de Datos del Lápiz

La app Android recibe datos del ESP32 por BLE y los publica en MQTT para que el backend los procese.

### Broker

| Propiedad | Valor |
|---|---|
| Host | `{host}` (mismo servidor) |
| Puerto | `1883` (TCP) |
| Protocolo | MQTT v3.1.1 |
| TLS | No (red interna Docker) |

### Topic

```
session/{session_id}/data
```

Sustituir `{session_id}` por el ID devuelto en `POST /sessions`.

### Formato (recomendado: CSV)

Cada lectura IMU se publica como una línea CSV:

```
{ts},{ax},{ay},{az},{gx},{gy},{gz},{fsr}
```

Ejemplo:

```
12345,0.12,-0.34,9.81,0.01,-0.02,0.05,1800
```

Alternativamente JSON:

```json
{"ts":12345,"ax":0.12,"ay":-0.34,"az":9.81,"gx":0.01,"gy":-0.02,"gz":0.05,"fsr":1800}
```

### Parámetros de cada lectura

| Parámetro | Tipo | Unidad | Rango | Fuente |
|---|---|---|---|---|
| `ts` | int | ms desde boot | — | ESP32 millis() |
| `ax`, `ay`, `az` | float | m/s² | ±19.6 | MPU6050 acelerómetro |
| `gx`, `gy`, `gz` | float | rad/s | ±250 | MPU6050 giroscopio |
| `fsr` | int | ADC raw | 0–4095 | FSR402 + RFP602 |

### Frecuencia de envío

El ESP32 envía datos continuamente mientras la sesión esté activa. La frecuencia esperada es **~10 lecturas/segundo** (100ms entre lecturas).

### Implementación recomendada (Android)

```kotlin
// Ejemplo con Eclipse Paho
val mqttClient = MqttClient("tcp://{host}:1883", MqttAsyncClient.generateClientId())
mqttClient.connect()

while (sesionActiva) {
    val lectura = receiveFromESP32viaBLE() // BLE callback
    val payload = "${lectura.ts},${lectura.ax},${lectura.ay},${lectura.az}," +
                   "${lectura.gx},${lectura.gy},${lectura.gz},${lectura.fsr}"
    val topic = "session/${sessionId}/data"
    mqttClient.publish(topic, MqttMessage(payload.toByteArray()))
}
```

---

## Flujo completo (app mobile)

```
Pantalla de Login
  │
  ├─ POST /auth/login ───────────────► guardar JWT
  │
  ▼
Pantalla de inicio (lista de niños)
  │
  ├─ GET /children/ (Bearer JWT) ────► mostrar lista
  │
  ▼
Seleccionar niño → elegir ejercicio
  │
  ├─ POST /sessions (Bearer JWT) ────► obtener session_id
  │
  ▼
Conectar WebSocket
  │
  ├─ ws://host:8001/ws/sessions/{id}?token={jwt}
  │
  ▼
Iniciar sesión BLE con ESP32
  │
  ├─ ESP32 → BLE → App → MQTT session/{id}/data (CSV)
  ├─ Backend evalúa reglas → WS → App recibe feedback
  │
  ▼
Tutor presiona "Terminar"
  │
  ├─ PATCH /sessions/{id}/end (close_reason="manual")
  │
  ▼
Pantalla de resultados (métricas de la sesión)
  │
  ├─ GET /sessions/{id} ────────────► mostrar avg_pressure, posture_score, etc.
```

---

## Códigos de estado

| Código | Qué hacer en la app |
|---|---|
| 200 | Éxito |
| 201 | Recurso creado |
| 400 | Error del cliente (mostrar mensaje `detail`) |
| 401 | Token inválido/expirado → redirigir a login |
| 404 | Recurso no encontrado |
| 500 | Error del servidor (reintentar más tarde) |

---

## URLs de desarrollo

| Servicio | URL |
|---|---|
| API REST + WebSocket | `http://localhost:8001` |
| Broker MQTT (Mosquitto) | `tcp://localhost:1883` |
| Base de datos (MySQL) | `mysql://localhost:3306` (solo admin) |
