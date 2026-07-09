# Plan de Trabajo: Actualización de Rutas y Nuevas Características

**Fuente principal:** `docs/requerimientos_lapiz_inteligente_rf_rnf.md`
**Fuente secundaria:** `docs/rutas.md`

**Objetivo:** alinear la aplicación Android con los requerimientos funcionales, no funcionales y validaciones del sistema Lápiz Inteligente, priorizando el flujo principal de sesiones con lápiz.

---

## 1. Mapa de requerimientos M vs plan

| Requerimiento M | Prioridad | Cubierto por plan | Nota |
|---|---|---|---|
| RF-01 | M | Parcial | Registro funciona, falta consulta y actualización de perfil |
| RF-02 | M | Sí | Login implementado |
| RF-03 | M | Sí | Token existe, pero interceptor OkHttp pospuesto en análisis previo |
| RF-04 | M | Sí | CRUD niños implementado |
| RF-05 | M | Sí | Formulario completo con DOB, mano, grado, notas |
| RF-06 | M | Sí | App solo para tutor |
| RF-07 | M | Sí | Catálogo con tipo de trazo |
| RF-08 | M | Sí | CRUD ejercicios implementado |
| RF-09 | M | Sí | Nombre, descripción, tipo trazo, estado, patrón |
| RF-11 | M | Sí | Nuevo: Pencil CRUD agregado al plan |
| RF-12 | M | Sí | Nuevo: device_uid |
| RF-13 | M | Sí | Nuevo: estados operativos |
| RF-14 | M | Sí | Nuevo: última actividad |
| RF-15 | M | Sí | Nuevo: recurso compartido (sin dueño fijo) |
| RF-16 | M | Sí | Sesión con alumno + ejercicio + lápiz |
| RF-17 | M | Sí | Backend mantiene asociación |
| RF-18 | M | Parcial | Historial existe, paginación no expuesta en UI |
| RF-19 | M | Sí | Cierre manual implementado |
| RF-20 | M | Backend | Cierre automático por inactividad (backend) |
| RF-21 | M | Sí | close_reason en EndSessionRequest |
| RF-22 | S | Nuevo | Sesión activa por child_id / pencil_id |
| RF-23 | M | Plan B | MQTT preferido, alternativa documentada necesaria |
| RF-24 | M | Pendiente | App como puente BLE (depende de arquitectura) |
| RF-25 | M | Backend | Payload IoT definido por backend/firmware |
| RF-26 | M | Backend | Asociación lectura-sesión |
| RF-27 | M | Backend | Buffer temporal en backend |
| RF-28 | M | Backend | Dataset al cierre |
| RF-29 | M | Backend | Fuera de BD relacional |
| RF-30 | S | Backend | Eventos de conectividad |
| RF-31 | M | Sí | Reglas en tiempo real (backend) |
| RF-32 | M | Sí | Alertas vía WebSocket planificado |
| RF-33 | S | Sí | Tipo, severidad, mensaje, timestamp (plan Fase 6) |
| RF-34 | S | Backend | Reglas configurables |
| RF-35 | M | Backend | Feature extraction |
| RF-36 | M | Backend | Random Forest |
| RF-37 | M | Backend | Modelo versionado |
| RF-38 | M | Sí | Resultado ML en sesión (consumo backend) |
| RF-39 | M | Sí | Diferenciar alertas de reglas vs ML (UI) |
| RF-40 | S | Backend | Gestión de versiones de modelo |
| RF-41 | S | Backend | Reproceso |
| RF-42 | M | Backend | Métricas al finalizar |
| RF-43 | M | Backend | Presión, estabilidad, temblor, eventos |
| RF-44 | S | Backend | Postura |
| RF-45 | M | Backend | Trazabilidad |
| RF-46 | M | Sí | Flujo completo en Android |
| RF-47 | M | Sí | Estado de conexión lápiz y sesión (planificado) |
| RF-48 | M | Sí | Alertas en tiempo real (planificado Fase 6) |
| RF-49 | M | Sí | Métricas visuales + historial por alumno |
| RF-50 | S | No | Gráficos comparativos (futuro) |
| RF-55 | S | No | Admin centralizado (futuro, no Android) |
| RF-56 | M | Sí | Documentación de API |
| RF-57 | M | No | Eventos de auditoría (backend, no Android) |

### Conclusiones del mapa

- **24 requerimientos M directos a Android**: cubiertos o planificados.
- **1 requerimiento M parcial**: RF-18 (paginación no expuesta en UI).
- **1 requerimiento M necesita plan B documentado**: RF-23 (MQTT alternativo).
- **1 requerimiento M pendiente de definición**: RF-24 (app como puente BLE).
- **Requerimientos S futuros**: RF-50, RF-55, RF-57 (post-MVP).

---

## 2. Plan por fases

### Fase 0: Seguridad inmediata y validaciones

**Prioridad: MUY ALTA**

| Tarea | Requerimiento asociado | Estado |
|---|---|---|
| Implementar interceptor OkHttp para `Authorization` header en lugar de pasar token manualmente en cada endpoint | RF-03, RNF-02 | ✅ |
| Validar que el token existe antes de navegar tras registro; no navegar a dashboard si RegisterRequest no contiene JWT | RF-03, VAL-AUT-04 | ✅ |
| Validar `confirmPassword` coincida con `password` en formulario de registro | RF-01, VAL-AUT-02 | ✅ |
| Validar `termsAccepted == true` antes de enviar registro | RF-01, VAL-AUT-02 | ✅ |
| Configurar logging de OkHttp en modo HEADERS (no BODY) para no exponer tokens ni datos sensibles en repositorios | RNF-04, RNF-07 | ✅ |
| Cerrar explícitamente Response en healthchecks (NetworkScanner, SplashNetwork) | Buenas prácticas | ✅ |
| Agregar validación de campos obligatorios y formato de email antes de enviar login/registro | VAL-AUT-02 | ✅ |

**Criterios de aceptación:**
- Ningún endpoint construye `Bearer null`.
- El formulario de registro rechaza envío si `confirmPassword != password` o `termsAccepted == false`.
- Los logs no contienen tokens JWT ni cuerpos de petición de autenticación.

---

### Fase 1: Modelos y API

**Prioridad: ALTA**

Esta fase detalla los campos **exactos** de cada request/response según `docs/rutas.md`. Se listan primero los modelos existentes (ya implementados y correctos) y luego los nuevos o con cambios.

#### Modelos existentes — correctos (sin cambios)

| Archivo | Campos | Contrato en rutas.md |
|---|---|---|
| `LoginRequest.kt` | `email`, `password` | L46-L50 |
| `LoginResponse.kt` | `access_token`, `token_type` | L73-L78 |
| `RegisterRequest.kt` | `name`, `lastname`, `email`, `password`, `phone?` | L35-L43 |
| `RegisterResponse.kt` | `user_id`, `name`, `lastname`, `email`, `is_active`, `created_at` | L46-L54 |
| `UserProfileResponse.kt` | `user_id`, `name`, `lastname`, `email`, `phone?`, `is_active`, `created_at` | L92-L101 |
| `CreateChildRequest.kt` | `name`, `birth_date?`, `dominant_hand?`, `school_grade?`, `notes?` | L115-L122 |
| `UpdateChildRequest.kt` | `name?`, `birth_date?`, `dominant_hand?`, `school_grade?`, `notes?` | L176-L184 |
| `ChildResponse.kt` | `child_id`, `user_id`, `name`, `birth_date?`, `dominant_hand?`, `school_grade?`, `notes?`, `is_active`, `created_at` | L125-L137 |
| `StrokeTypeResponse.kt` | `stroke_type_id` → `id`, `name`, `created_at` | L220-L224 |
| `CreateExerciseRequest.kt` | `name`, `description?`, `stroke_type_id` | L275-L281 |
| `UpdateExerciseRequest.kt` | `name?`, `description?`, `stroke_type_id?` | L294-L303 |
| `ExerciseResponse.kt` | `exercise_id` → `id`, `name`, `description?`, `stroke_type_id`, `is_active`, `created_at`, `updated_at` | L243-L251 |
| `ExerciseDetailResponse.kt` | Ídem ExerciseResponse | L264 |
| `EndSessionRequest.kt` | `close_reason` | L525-L529 |

#### Modelos existentes — cambios necesarios

| Archivo | Campo faltante | Contrato | Acción |
|---|---|---|---|---|
| `SessionResponse.kt` | `pencil_id: Int` | rutas.md L446 | Agregar campo |
| `SessionHistoryResponse.kt` | Modelo completo incorrecto — el backend NO retorna `SessionHistoryResponse`, retorna `List<SessionResponse>` (ver GET `/children/{child_id}/sessions` L512) | L512 | Eliminar archivo o reemplazar por `List<SessionResponse>` |
| `RegisterResponse.kt` | `created_at` no documentado originalmente (sí en rutas.md L46-L54) | L54 | Ya existe, correcto |

#### Nuevos modelos de lápiz

| Archivo | Campos exactos | Request/Response en rutas.md |
|---|---|---|
| `models/pencil/PencilResponse.kt` | `pencil_id: Int`, `device_uid: String`, `name: String?`, `status: String` (valores: `available`, `in_use`, `inactive`, `maintenance`, `lost`), `firmware_version: String?`, `last_seen_at: String?`, `created_at: String`, `updated_at: String` | L341-L351 |
| `models/pencil/CreatePencilRequest.kt` | `device_uid: String`, `name: String? = null`, `firmware_version: String? = null` | L333-L337 |
| `models/pencil/UpdatePencilRequest.kt` | `name: String? = null`, `firmware_version: String? = null` | L391-L395 |
| `models/pencil/UpdatePencilStatusRequest.kt` | `status: String` (uno de: `available`, `in_use`, `inactive`, `maintenance`, `lost`) | L411-L414 |

#### Nuevos modelos de feedback (WebSocket)

| Archivo | Campos exactos | Nota | Estado |
|---|---|---|---|
| `models/session/AlertType.kt` | Enum: `PRESSURE_HIGH`, `PRESSURE_LOW`, `MOVEMENT_UNSTABLE`, `TREMOR_DETECTED`, `POSTURE_ISSUE` | Definir según backend | ✅ |
| `models/session/FeedbackSeverity.kt` | Enum: `LOW`, `MEDIUM`, `HIGH` | | ✅ |
| `models/session/SessionFeedbackMessage.kt` | `type: AlertType`, `severity: FeedbackSeverity`, `message: String`, `timestamp: String`, `session_id: Int` y opcionalmente `value: Double?` | | ✅ |

#### Endpoints en ApiService

| Endpoint | Método HTTP | Firma Retrofit | Contrato rutas.md |
|---|---|---|---|---|
|---|---|---|---|
| `/pencils/` | POST | `suspend fun createPencil(@Body request: CreatePencilRequest): PencilResponse` | L325-L338 |
| `/pencils/` | GET | `suspend fun getPencils(@Query("status") status: String? = null): List<PencilResponse>` | L358-L366 |
| `/pencils/{pencil_id}` | GET | `suspend fun getPencil(@Path("pencil_id") pencilId: Int): PencilResponse` | L370-L378 |
| `/pencils/{pencil_id}` | PUT | `suspend fun updatePencil(@Path("pencil_id") pencilId: Int, @Body request: UpdatePencilRequest): PencilResponse` | L382-L398 |
| `/pencils/{pencil_id}/status` | PATCH | `suspend fun updatePencilStatus(@Path("pencil_id") pencilId: Int, @Body request: UpdatePencilStatusRequest): PencilResponse` | L402-L417 |
| `/sessions/active` | GET | `suspend fun getActiveSession(@Query("child_id") childId: Int? = null, @Query("pencil_id") pencilId: Int? = null): SessionResponse` | L472-L484 |
| `/sessions` | POST | Actualizar firma existente: `CreateSessionRequest` ahora incluye `pencil_id` | L425-L438 |

**Nota importante con `GET /sessions/active`:** El backend requiere al menos uno de los dos query params (`child_id` o `pencil_id`). El error `400` si no se envía ninguno. El error `404` si no hay sesión activa.

**Checklist explícito de `pencil_id` en cadena de sesión:**
- [x] `CreateSessionRequest.pencil_id: Int`
- [x] `SessionResponse.pencil_id: Int?`
- [x] `SessionRepository.createSession(childId, exerciseId, pencilId)`
- [x] `SessionUseCase.createSession(childId, exerciseId, pencilId)`
- [x] `SessionsViewModel.createSession(childId, exerciseId, pencilId, onSuccess)`
- [x] `SessionsScreen` dropdown de lápices disponibles
- [x] `SessionCreateScreen` confirmación de lápiz seleccionado
- [x] `SessionUiState.Ready` incluye lista de `PencilResponse`

**Criterios de aceptación:**
- Retrofit representa todas las rutas de `docs/rutas.md` que usa la app.
- No se puede compilar `createSession` sin `pencilId`.
- `SessionUiState.Ready` contiene `children`, `exercises` y `pencils`.
- `SessionResponse` incluye `pencil_id: Int?`.

---

---

### Fase 2: Repositorios y casos de uso

**Prioridad: ALTA**

| Archivo | Acción | Firmas exactas | Estado |
|---|---|---|---|
| `data/repository/PencilRepository.kt` | Nuevo: listar, detalle, registrar, actualizar, cambiar estado | `getPencils(status: String? = null): Result<List<PencilResponse>>`, `getPencil(pencilId: Int): Result<PencilResponse>`, `createPencil(request: CreatePencilRequest): Result<PencilResponse>`, `updatePencil(pencilId: Int, request: UpdatePencilRequest): Result<PencilResponse>`, `updatePencilStatus(pencilId: Int, request: UpdatePencilStatusRequest): Result<PencilResponse>` | ✅ |
| `domain/pencil/PencilUseCase.kt` | Nuevo: wrapper de PencilRepository | Mismas firmas, delega a `PencilRepository` | ✅ |
| `data/repository/SessionRepository.kt` | Actualizar `createSession(childId, exerciseId, pencilId)`. Agregar `getActiveSessionByChild(childId)` y `getActiveSessionByPencil(pencilId)`. Marcar `SessionHistoryResponse` como deprecado y migrar a `List<SessionResponse>` | `createSession(childId: Int, exerciseId: Int, pencilId: Int): Result<SessionResponse>`, `getActiveSessionByChild(childId: Int): Result<SessionResponse?>`, `getActiveSessionByPencil(pencilId: Int): Result<SessionResponse?>` | ✅ |
| `domain/session/SessionUseCase.kt` | Actualizar `createSession`. Agregar `getActiveSessionByChild` / `getActiveSessionByPencil` | Mismas firmas que repositorio | ✅ |

> **Importante `SessionHistoryResponse`:** El contrato de `GET /children/{child_id}/sessions` retorna `List<SessionResponse>`, no `List<SessionHistoryResponse>`. El modelo `SessionHistoryResponse` es innecesario. Al actualizar `ApiService`, migrar el endpoint para retornar `List<SessionResponse>` y eliminar o deprecar `SessionHistoryResponse`.

**Manejo de errores esperados:**

| Código | Significado | Acción Android |
|---|---|---|
| 401 | Token expirado | Redirigir a login, limpiar DataStore |
| 404 | Recurso no encontrado (niño, ejercicio, lápiz, sesión) | Mostrar mensaje específico |
| 409 | Conflicto (lápiz ya en uso, sesión activa duplicada, email duplicado) | Mensaje claro al tutor |
| 422 | Datos inválidos | Mostrar errores de validación del backend |
| 423 | Recurso bloqueado (lápiz inactivo/perdido/mantenimiento) | Informar que el lápiz no está disponible |

**Criterios de aceptación:**
- Ningún ViewModel nuevo llama directamente a `PencilRepository`.
- Sesiones nuevas se crean siempre con lápiz seleccionado.
- `SessionRepository` maneja 401, 404, 409, y 423 con mensajes en español claro.

---

### Fase 3: Flujo UI de sesión con lápiz

**Prioridad: ALTA**

| Archivo | Acción | Estado |
|---|---|---|
| `ui/sessions/SessionUiState.kt` | Agregar `pencils: List<PencilResponse>` a `Ready` | ✅ |
| `ui/sessions/SessionsViewModel.kt` | Cargar pencils disponibles, filtrar por `available`, exponer `createSession(childId, exerciseId, pencilId)` | ✅ |
| `ui/sessions/SessionsScreen.kt` | Agregar dropdown de lápices disponibles, bloquear botón hasta tener child + exercise + pencil | ✅ |
| `ui/sessions/SessionCreateScreen.kt` | Mostrar confirmación de niño, ejercicio y lápiz seleccionados. Recibir `pencilId` como parámetro | ✅ |

**Estado de lápices:**
- Mostrar solo lápices con estado `available`.
- Si no hay lápices disponibles, mostrar estado vacío con mensaje: "No hay lápices disponibles. Consulta con el administrador técnico."
- Al seleccionar un lápiz, mostrar su `device_uid` y estado para confirmación.

**Criterios de aceptación:**
- El botón "Iniciar sesión" solo se habilita con niño, ejercicio y lápiz seleccionados (VAL-AND-01).
- El body enviado contiene `child_id`, `exercise_id` y `pencil_id`.
- `SessionDetailScreen` recibe el `pencilId` para mostrar estado del lápiz.

---

### Fase 4: Timer funcional y navegación de cierre

**Prioridad: ALTA**

| Archivo | Acción |
|---|---|
| `ui/sessions/SessionDetailScreen.kt` | Implementar timer real con `LaunchedEffect` + `delay(1000)` que incremente `elapsedSeconds`. Estado visible de tiempo transcurrido |
| `navigation/NavGraph.kt` | Al finalizar sesión, navegar a ChildDetailScreen o Dashboard según origen. No usar `popBackStack()` genérico |
| `data/repository/SessionRepository.kt` | Asegurar que `endSession` siempre envía `close_reason` (actualmente "manual") |

**Criterios de aceptación:**
- El timer de sesión incrementa cada segundo mientras `isRunning == true`.
- Al finalizar sesión, la navegación retorna a la pantalla correcta (ChildDetail o Dashboard).
- Toda sesión cerrada tiene `close_reason` obligatorio (VAL-SES-06).

---

### Fase 5: Sesiones activas y recuperación

**Prioridad: ALTA antes de WebSocket**

| Archivo | Acción |
|---|---|
| `ui/children/ChildDetailScreen.kt` | Al cargar, consultar `GET /sessions/active?child_id={child_id}` y si existe, mostrar botón "Continuar sesión activa" |
| `ui/sessions/SessionsViewModel.kt` | Agregar método `checkActiveSession(childId)` |
| `ui/sessions/SessionsScreen.kt` | Al seleccionar lápiz, verificar si tiene sesión activa por pencil_id |
| `ui/sessions/SessionDetailScreen.kt` | Cargar sesión activa existente, mostrar estado actual del lápiz (conectado/desconectado) |
| `data/datastore/SessionManager.kt` | Opcional: cachear `activeSessionId` y `activePencilId` para recuperación offline |

**RF-22:** La app puede recuperar el estado de una sesión activa si se reconecta.

**Criterios de aceptación:**
- No se permiten dos sesiones activas para el mismo niño o lápiz.
- Una sesión interrumpida puede reanudarse desde ChildDetailScreen o SessionsScreen.
- Si no hay red, mostrar "No se puede verificar si hay sesiones activas. Verifica tu conexión."

---

### Fase 6: WebSocket y feedback en tiempo real

**Prioridad: Media-Alta**

| Archivo | Acción |
|---|---|
| `data/realtime/SessionWebSocketDataSource.kt` | Nuevo: conexión vía OkHttp WebSocket, URL `ws://{base_url}/ws/sessions/{session_id}?token={jwt}`, ciclo de vida (conectar, reconectar, cerrar) |
| `models/session/SessionFeedbackMessage.kt` | Nuevo: tipo, severidad, mensaje_educativo, timestamp, sesión_id |
| `models/session/AlertType.kt` | Nuevo: enum de tipos de alerta (pressure_high, pressure_low, unstable, tremor, posture) |
| `models/session/FeedbackSeverity.kt` | Nuevo: enum low, medium, high |
| `domain/session/ObserveSessionFeedbackUseCase.kt` | Nuevo: caso de uso que expone Flow de mensajes de feedback |
| `ui/sessions/SessionDetailScreen.kt` | Mostrar estado de conexión WebSocket (conectando, conectado, reconectando, desconectado). Renderizar alertas entrantes en tarjetas con color según severidad |
| `ui/sessions/SessionsViewModel.kt` | Iniciar/cerrar conexión WebSocket al entrar/salir de SessionDetailScreen |

**Diferenciación visual (RF-39, RNF-23):**
- Alertas de reglas educativas: tarjeta azul con ícono de información.
- Resultados ML: tarjeta violeta con ícono de estrella y etiqueta "Análisis IA".
- Métricas de sesión: sección separada con fondo neutro.

**Estado de conexión del lápiz (RF-47):**
- Mostrar indicador visual: verde (conectado), amarillo (reconectando), rojo (desconectado).
- Mensaje educativo: "El lápiz está conectado", "Buscando lápiz...", "Lápiz desconectado. La sesión continúa registrando."

**Criterios de aceptación:**
- Las alertas aparecen en la pantalla sin recargar manualmente.
- La conexión WebSocket se cierra al salir de la pantalla o finalizar sesión.
- No se persiste el token en logs ni en almacenamiento adicional.

---

### Fase 7: Historial y métricas de sesión

**Prioridad: Media**

| Archivo | Acción |
|---|---|
| `navigation/Routes.kt` | Ya existe `CHILD_SESSION_HISTORY` |
| `navigation/NavGraph.kt` | Agregar `composable(Routes.CHILD_SESSION_HISTORY)` |
| `ui/sessions/SessionHistoryScreen.kt` | Conectar ruta y pasar `childId`. Mostrar lista paginada con métricas visuales: presión media, estabilidad, tremor, fecha, ejercicio |
| `ui/sessions/SessionCard.kt` | Agregar indicadores visuales de métricas (barras de progreso, colores) |
| `models/session/SessionHistoryResponse.kt` | Revisar si debe incluir `avg_pressure`, `stability`, `tremor_level`, `ai_score`, `pencil_id` |
| `ui/children/ChildDetailScreen.kt` | Agregar botón a historial completo (navegar a CHILD_SESSION_HISTORY) |

**RF-49:** El tutor visualiza fecha, ejercicio, presión, estabilidad, movimiento, eventos y resultado IA.
**RF-50:** Futuro (gráficos comparativos).

**Criterios de aceptación:**
- CHILD_SESSION_HISTORY es accesible desde ChildDetailScreen y dashboard.
- Cada tarjeta de sesión muestra métricas clave de un vistazo.
- El historial soporta scroll/paginación con skip/limit.

---

### Fase 8: Correcciones arquitectónicas

**Prioridad: Media**

| Tarea | Requerimiento |
|---|---|
| Agregar use cases faltantes: AuthUseCase, ChildrenUseCase, DashboardUseCase, UserUseCase | AGENTS.md |
| Mover cálculos de métricas y recomendaciones de DashboardRepository a DashboardUseCase | RNF-24 |
| Eliminar dependencias directas de NavGraph a AuthRepository/SessionManager; mover lógica a un ViewModel compartido o UseCase | AGENTS.md |
| Agregar interfaces de repositorio en dominio (Repository contracts) | AGENTS.md |
| Evaluar separación de DTOs vs modelos de dominio | Arquitectura |

**Criterios de aceptación:**
- El flujo `UI -> ViewModel -> UseCase -> Repository -> DataSource` se cumple para funcionalidades nuevas y existentes.
- Los ViewModels no contienen lógica de negocio.
- Los repositorios no contienen reglas de presentación.

---

### Fase 9: Tests

**Prioridad: ALTA para cambios de contrato**

| Test | Archivo esperado |
|---|---|
| `CreateSessionRequest` serializa `pencil_id` | `CreateSessionRequestTest.kt` |
| `SessionsViewModel` no crea sesión sin lápiz | `SessionsViewModelTest.kt` |
| `SessionsViewModel` maneja lista vacía de lápices disponibles | `SessionsViewModelTest.kt` |
| `PencilRepository` mapea errores 401, 404, 409, 423 | `PencilRepositoryTest.kt` |
| `SessionRepository` consulta sesión activa por child y por pencil | `SessionRepositoryTest.kt` |
| Registro valida confirmPassword y termsAccepted | `RegisterViewModelTest.kt` |
| `SessionDetailScreen` timer incrementa correctamente | `SessionDetailScreenTest.kt` |
| Navegación a sesión activa desde ChildDetail | `NavGraphTest.kt` |

**Comandos:**
```bash
./gradlew test
./gradlew assembleDebug
```

---

## 3. Canal IoT: plan B documentado

Dado que **RF-23** (M) exige un canal IoT para recibir lecturas del lápiz y el plan excluye MQTT inicialmente, se establece lo siguiente:

| Elemento | Decisión |
|---|---|
| Canal IoT MVP | WebSocket desde ESP32 al backend (o HTTP POST si WebSocket no está disponible en firmware) |
| MQTT | Preferido para producción, se integrará en fase posterior |
| App Android como puente | No se implementa en MVP (RF-24 condicionado a arquitectura aprobada) |
| Documentación requerida | Contrato IoT provisional (tópico, payload, frecuencia, credenciales) debe documentarse antes de integración (RNF-27) |
| Rol de Android | Mostrar estado de conexión del lápiz vía WebSocket de sesión, no vía BLE directo |

**Criterio:** El backend debe aceptar lecturas por el canal documentado antes de que la app pueda mostrar estado de conexión del lápiz en tiempo real.

---

## 4. Orden recomendado de implementación

```
Fase 0 (Seguridad) ──────────────────────────── Inicio inmediato
       │
Fase 1 (Modelos + API) ──────────────────────── Semana 1
       │
Fase 2 (Repositorios + UseCases) ───────────── Semana 1-2
       │
Fase 3 (UI lápiz) ────────────────────────────── Semana 2
       │
Fase 4 (Timer + navegación cierre) ──────────── Semana 2
       │
Fase 5 (Sesiones activas) ───────────────────── Semana 3
       │
Fase 6 (WebSocket feedback) ─────────────────── Semana 3-4
       │
Fase 7 (Historial + métricas) ───────────────── Semana 4
       │
Fase 8 (Arquitectura) ───────────────────────── Semana 4-5
       │
Fase 9 (Tests) ───────────────────────────────── Continuo
```

Las fases **0 a 5** son prerrequisito para WebSocket. No iniciar Fase 6 sin completar Fase 5.

---

## 5. Criterio de preparado para WebSocket

- `POST /sessions` funciona con `pencil_id` (Fase 1-2).
- La UI evita duplicar sesiones activas (Fase 5).
- `SessionDetailScreen` tiene timer funcional (Fase 4).
- El token JWT se inyecta por interceptor OkHttp y no se registra en logs (Fase 0).
- Los modelos de feedback (`AlertType`, `FeedbackSeverity`, `SessionFeedbackMessage`) están definidos (Fase 6).
<br>
- El lápiz puede publicar lecturas por el canal IoT documentado (Plan B, sección 3).

---

## 6. Inconsistencias detectadas

### Contrato API vs código

| Problema | Referencia | Estado |
|---|---|---|---|
| `POST /sessions` requiere `pencil_id`, pero Android envía solo alumno y ejercicio | `models/session/CreateSessionRequest.kt`, `SessionRepository.kt`, `SessionUseCase.kt`, `SessionsViewModel.kt` | ✅ |
| No existen endpoints `/pencils/` en Retrofit | `data/remote/ApiService.kt` | ✅ |
| No existen modelos ni repositorio de lápices | Falta `models/pencil/`, `PencilRepository`, `PencilUseCase` | ✅ |
| No existe `GET /sessions/active` | `data/remote/ApiService.kt`, `SessionRepository.kt`, `SessionUseCase.kt` | ✅ |
| WebSocket documentado pero no implementado | Falta `data/realtime/` y modelos de feedback | |
| `docs/flujo.md` documenta crear sesión sin `pencil_id` | `docs/flujo.md` | |
| `SessionResponse` no tiene campo `pencil_id` | `models/session/SessionResponse.kt` (rutas.md L446 exige `pencil_id`) | ✅ |
| `SessionHistoryResponse` es modelo innecesario — el endpoint retorna `List<SessionResponse>` | `models/session/SessionHistoryResponse.kt` vs rutas.md L512 | |
| `UpdateExerciseRequest` no incluye campo `is_active` | `models/exercise/UpdateExerciseRequest.kt` vs rutas.md L298 (PUT exercises retorna `is_active: true`) | ✅ |

### Arquitectura

| Problema | Referencia |
|---|---|
| ViewModels llaman repositorios directamente, saltando use cases | Auth, Register, Children, Dashboard, Profile, parte de Sessions |
| `NavGraph` recibe `AuthRepository` y `SessionManager` | `navigation/NavGraph.kt` |
| `SplashViewModel` hace healthcheck directo con OkHttp | `ui/splash/SplashViewModel.kt` |
| `DashboardRepository` calcula métricas y niveles de rendimiento | `data/repository/DashboardRepository.kt` |
| No hay interfaces de repositorio en dominio | Paquete `domain/` incompleto |

### UI y navegación

| Problema | Riesgo | Fase de corrección | Estado |
|---|---|---|---|
| Registro navega al dashboard aunque `RegisterResponse` no tiene JWT | Pantallas privadas pueden llamar API con token nulo | Fase 0 | ✅ |
| Formulario de registro tiene campos pero no valida confirmación ni términos | Registro inválido o incompleto | Fase 0 | ✅ |
| `SessionDetailScreen` tiene timer local que no incrementa | Experiencia engañosa durante sesión | Fase 4 | |
| `SessionHistoryScreen` y ruta existen pero no conectadas en NavGraph | Funcionalidad inaccesible | Fase 7 | |
| Al finalizar sesión se hace `popBackStack()` genérico | Puede volver a pantalla incorrecta | Fase 4 | |
| Dashboard recibe callback a detalle de sesión pero filas no son clickeables | Callback sin uso real | Fase 7 | |

### Datos y seguridad

| Problema | Riesgo | Fase | Estado |
|---|---|---|---|
| Repositorios construyen `Bearer null` si no hay token | Errores 401 tardíos y difíciles de diagnosticar | Fase 0 | ✅ |
| Token se pasa manualmente en cada llamada privada | Repetición y riesgo de omisión | Fase 0 | ✅ |
| Interceptor de logging usa `BODY` globalmente | Puede exponer tokens y datos sensibles | Fase 0 | ✅ |
| Healthchecks con OkHttp no cierran explícitamente `Response` | Riesgo de fugas de recursos | Fase 0 | ✅ |
| Escalas de métricas no uniformes entre dashboard y detalle de alumno | Datos visualmente inconsistentes | Fase 7 | |

### Build

| Problema | Riesgo |
|---|---|
| `app/build.gradle.kts` aplica Compose compiler plugin pero no `org.jetbrains.kotlin.android` | Posible fallo de compilación o configuración incompleta |
| Hay imports mixtos de `hiltViewModel` | Riesgo de incompatibilidad de APIs |