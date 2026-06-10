# GraphiCare — Flujo de Pantallas (Android / Navigation Compose)

**Proyecto:** Lápiz Inteligente — UCH / Grupo GIIA
**App:** GraphiCare (Kotlin, Single Activity + Jetpack Navigation Compose)
**Backend:** FastAPI + MySQL 8.0 + MQTT + WebSocket

---

## 1. Arquitectura general

Single Activity (`MainActivity`) con `NavHost` como único punto de entrada. Cada pantalla es un `@Composable` en su propio archivo dentro de un directorio dedicado.

```
MainActivity
  └── NavHost(startDestination = "splash")
        ├── SplashScreen
        ├── LoginScreen
        ├── RegisterScreen
        ├── MainMenuScreen        ← hub con BottomNav
        ├── ChildrenListScreen
        ├── ChildCreateScreen
        ├── ChildEditScreen
        ├── ExercisesScreen
        ├── ExerciseDetailScreen
        ├── SessionCreateScreen
        ├── SessionDetailScreen
        ├── SessionHistoryScreen
        ├── DashboardScreen
        └── ProfileScreen
```

**Bottom Navigation** visible en: `MainMenuScreen`, `ChildrenListScreen`, `DashboardScreen`, `ProfileScreen`.

---

## 2. Mapeo directorios → Screens

| Directorio | Screen @Composable | Layout principal | ViewModel |
|---|---|---|---|
| `splash_screen/` | `SplashScreen` | Logo + loading indicator | `SplashViewModel` |
| `login_screen/` | `LoginScreen` | Email + password + botón + link registro | `LoginViewModel` |
| `register_screen/` | `RegisterScreen` | Nombre + apellido + email + password + confirmar + términos | `RegisterViewModel` |
| `main_menu/` | `MainMenuScreen` | BottomNav host + Dashboard global | `MainMenuViewModel` |
| `children_list/` | `ChildrenListScreen` | Search bar + RecyclerView grid 2 cols + FAB | `ChildrenListViewModel` |
| `child_create/` | `ChildCreateScreen` | Formulario: nombre, fecha nac., mano, grado, notas | `ChildCreateViewModel` |
| `child_edit/` | `ChildEditScreen` | Mismo formulario precargado | `ChildEditViewModel` |
| `exercises/` | `ExercisesScreen` | Search + filtros por tipo de trazo + lista de tarjetas | `ExercisesViewModel` |
| `exercise_detail/` | `ExerciseDetailScreen` | Nombre, descripción, tipo de trazo, preview, botón "Usar" | `ExerciseDetailViewModel` |
| `session_create/` | `SessionCreateScreen` | Selector de niño + selector de ejercicio + botón "Iniciar" | `SessionCreateViewModel` |
| `session_detail/` | `SessionDetailScreen` | Cronómetro, canvas interactivo, métricas en vivo, botones Iniciar/Terminar | `SessionDetailViewModel` |
| `session_history/` | `SessionHistoryScreen` | Lista de sesiones por niño con badges + botones Ver/Repetir | `SessionHistoryViewModel` |
| `dashboard_screen/` | `DashboardScreen` | Tarjetas métricas + gráfico semanal + actividad reciente | `DashboardViewModel` |
| `profile_screen/` | `ProfileScreen` | Avatar + nombre + email + opciones + cerrar sesión | `ProfileViewModel` |

---

## 3. NavGraph completo

```kotlin
NavHost(navController, startDestination = "splash") {

    // === Auth flow ===
    composable("splash") {
        SplashScreen(
            onNavigateToLogin = { navController.navigate("login") { popUpTo("splash") { inclusive = true } } },
            onNavigateToMain = { navController.navigate("main_menu") { popUpTo("splash") { inclusive = true } } }
        )
    }

    composable("login") {
        LoginScreen(
            onNavigateToRegister = { navController.navigate("register") },
            onLoginSuccess = { navController.navigate("main_menu") { popUpTo("login") { inclusive = true } } }
        )
    }

    composable("register") {
        RegisterScreen(
            onRegisterSuccess = { navController.navigate("main_menu") { popUpTo("login") { inclusive = true } } },
            onNavigateToLogin = { navController.popBackStack() }
        )
    }

    // === Main hub (Bottom Nav) ===
    composable("main_menu") {
        MainMenuScreen(
            onNavigateToChildren = { navController.navigate("children") },
            onNavigateToExercises = { navController.navigate("exercises") },
            onNavigateToDashboard = { navController.navigate("dashboard") },
            onNavigateToProfile = { navController.navigate("profile") }
        )
    }

    // === Children CRUD ===
    composable("children") {
        ChildrenListScreen(
            onChildClick = { childId -> navController.navigate("children/$childId/history") },
            onCreateChild = { navController.navigate("children/create") },
            onNavigateToDashboard = { navController.navigate("dashboard") },
            onNavigateToProfile = { navController.navigate("profile") },
            onNavigateToMain = { navController.navigate("main_menu") }
        )
    }

    composable("children/create") {
        ChildCreateScreen(
            onCreated = { navController.popBackStack() }
        )
    }

    composable("children/{childId}/edit") { backStackEntry ->
        val childId = backStackEntry.arguments?.getString("childId")?.toIntOrNull() ?: return@composable
        ChildEditScreen(
            childId = childId,
            onUpdated = { navController.popBackStack() }
        )
    }

    composable("children/{childId}/history") { backStackEntry ->
        val childId = backStackEntry.arguments?.getString("childId")?.toIntOrNull() ?: return@composable
        SessionHistoryScreen(
            childId = childId,
            onStartSession = { childId, exerciseId -> navController.navigate("sessions/create/$childId/$exerciseId") },
            onSessionClick = { sessionId -> navController.navigate("sessions/$sessionId") },
            onEditChild = { navController.navigate("children/$childId/edit") }
        )
    }

    // === Exercises ===
    composable("exercises") {
        ExercisesScreen(
            onExerciseClick = { exerciseId -> navController.navigate("exercises/$exerciseId") }
        )
    }

    composable("exercises/{exerciseId}") { backStackEntry ->
        val exerciseId = backStackEntry.arguments?.getString("exerciseId")?.toIntOrNull() ?: return@composable
        ExerciseDetailScreen(
            exerciseId = exerciseId,
            onUseExercise = { childId -> navController.navigate("sessions/create/$childId/$exerciseId") }
        )
    }

    // === Sessions ===
    composable("sessions/create/{childId}/{exerciseId}") { backStackEntry ->
        val childId = backStackEntry.arguments?.getString("childId")?.toIntOrNull() ?: return@composable
        val exerciseId = backStackEntry.arguments?.getString("exerciseId")?.toIntOrNull() ?: return@composable
        SessionCreateScreen(
            childId = childId,
            exerciseId = exerciseId,
            onSessionStarted = { sessionId ->
                navController.navigate("sessions/$sessionId") {
                    popUpTo("sessions/create/$childId/$exerciseId") { inclusive = true }
                }
            }
        )
    }

    composable("sessions/{sessionId}") { backStackEntry ->
        val sessionId = backStackEntry.arguments?.getString("sessionId")?.toIntOrNull() ?: return@composable
        SessionDetailScreen(
            sessionId = sessionId,
            onSessionEnded = { childId ->
                navController.navigate("children/$childId/history") {
                    popUpTo("main_menu")
                }
            },
            onBack = { navController.popBackStack() }
        )
    }

    // === Dashboard ===
    composable("dashboard") {
        DashboardScreen(
            onSessionClick = { sessionId -> navController.navigate("sessions/$sessionId") }
        )
    }

    // === Profile ===
    composable("profile") {
        ProfileScreen(
            onLogout = {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}
```

---

## 4. Detalle de cada Screen

### 4.1 SplashScreen (`splash_screen/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Pantalla de carga inicial. Verifica conectividad y sesión JWT vigente. |
| **Layout** | Logo de GraphiCare centrado + `CircularProgressIndicator` + texto de estado |
| **ViewModel** | `SplashViewModel` — checkea `DataStore` por token JWT |
| **Datos** | `GET /health` (opcional) para verificar conectividad |
| **Navegación saliente** | Token válido → `main_menu`; sin token → `login` |
| **Referencia HTML** | No existía en el prototipo. Diseño nuevo. |

---

### 4.2 LoginScreen (`login_screen/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Autenticación del tutor con email + password |
| **Layout** | Logo + título + campo email + campo password con toggle visibility + botón "Iniciar Sesión" + link "Crear una cuenta" |
| **ViewModel** | `LoginViewModel` — valida campos, llama `POST /auth/login` |
| **API** | `POST /auth/login` → `{ email, password }` → `{ access_token, token_type }` |
| **Persistencia** | Guarda JWT en `DataStore` |
| **Navegación** | Éxito → `main_menu` (popUpTo login inclusive); link → `register` |
| **Referencia HTML** | `login_screen/code.html` + `index.html` líneas ~1-250 |

---

### 4.3 RegisterScreen (`register_screen/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Registro de nuevo tutor |
| **Layout** | Logo + título + nombre + apellido + email + password + confirmar + checkbox términos + botón "Registrarse" + link "Iniciar Sesión" |
| **ViewModel** | `RegisterViewModel` — valida coincidencia passwords, llama `POST /auth/register` |
| **API** | `POST /auth/register` → `{ name, lastname, email, password, phone? }` → `{ access_token, token_type }` |
| **Persistencia** | Guarda JWT en `DataStore` |
| **Navegación** | Éxito → `main_menu` (popUpTo login inclusive); link → popBackStack |
| **Referencia HTML** | `register_screen/code.html` + `index.html` líneas ~250-450 |

---

### 4.4 MainMenuScreen (`main_menu/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Hub central post-autenticación. Contiene BottomNav y enrutamiento a las secciones principales. |
| **Layout** | Scaffold con BottomNavBar (Dashboard, Children, Profile) + top bar con nombre de usuario |
| **ViewModel** | `MainMenuViewModel` — carga nombre del usuario desde perfil |
| **Bottom Nav** | 3 ítems: Dashboard (activo default), Children, Profile |
| **API** | `GET /profile` (nombre + email) |
| **Navegación** | Las pestañas cambian el contenido del `NavHost` anidado o navegan a rutas independientes |
| **Nota** | Puede implementarse como un `Scaffold` contenedor que renderiza los hijos según la ruta activa (BottomNav + nested NavHost), o como ruteo plano donde cada screen tiene su propio BottomNav. Recomendado: nested NavHost. |
| **Referencia HTML** | `main_menu/code.html` + `index.html` líneas ~450-600 |

---

### 4.5 ChildrenListScreen (`children_list/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Listar, buscar y crear niños |
| **Layout** | SearchBar + `LazyVerticalGrid` 2 columnas de tarjetas (avatar, nombre, edad, mano, sesiones) + FAB `+` + estado vacío |
| **ViewModel** | `ChildrenListViewModel` — carga `GET /children/`, filtra en vivo por nombre |
| **API** | `GET /children/` → `[ChildResponse]` |
| **Navegación** | Click tarjeta → `children/{id}/history`; FAB → `child_create`; BottomNav Dashboard/Profile |
| **Referencia HTML** | `children_list/code.html` (antes dashboard/) + `index.html` líneas ~600-850 |

---

### 4.6 ChildCreateScreen (`child_create/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Formulario para registrar un nuevo niño |
| **Layout** | Formulario scrollable: nombre completo, fecha nacimiento (DatePicker), mano dominante (RadioGroup), grado escolar, notas (textarea) + botón Guardar + Cancelar |
| **ViewModel** | `ChildCreateViewModel` — valida campos, llama `POST /children/` |
| **API** | `POST /children/` → `{ name, birth_date?, dominant_hand, school_grade?, notes? }` → `ChildResponse` |
| **Navegación** | Éxito → popBackStack() a children list |
| **Referencia HTML** | Modal "Nuevo Alumno" en `index.html` líneas ~970-1050 |

---

### 4.7 ChildEditScreen (`child_edit/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Editar datos de un niño existente |
| **Layout** | Mismo formulario que ChildCreate pero precargado con `GET /children/{id}` + botón "Desactivar Niño" (rojo) |
| **ViewModel** | `ChildEditViewModel` — carga datos existentes, llama `PUT /children/{id}` |
| **API** | `GET /children/{id}` → `ChildResponse`; `PUT /children/{id}` → `{ ...campos }` → `ChildResponse` |
| **Navegación** | Éxito → popBackStack() |
| **Referencia HTML** | Botón "Editar Datos" + modal en `index.html` líneas ~700-800 |

---

### 4.8 ExercisesScreen (`exercises/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Catálogo de ejercicios con filtro por tipo de trazo |
| **Layout** | SearchBar + chips de filtro horizontal (`All`, `Recto`, `Curva`, `Círculo`, `Letra`, etc.) + `LazyColumn` de tarjetas de ejercicio (icono, nombre, descripción corta, badge dificultad) |
| **ViewModel** | `ExercisesViewModel` — carga `GET /exercises?stroke_type_id=`, `GET /stroke-types` para filtros |
| **API** | `GET /stroke-types` → `[StrokeType]`; `GET /exercises` con filtro opcional |
| **Navegación** | Click tarjeta → `exercises/{id}` |
| **Referencia HTML** | `exercises/code.html` (antes activity_selection/) + modal selector en `index.html` líneas ~1050-1150 |

---

### 4.9 ExerciseDetailScreen (`exercise_detail/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Ver detalle de un ejercicio individual |
| **Layout** | Header con nombre + tipo de trazo (chip) + descripción completa + preview visual del trazo + botón "Usar con un niño" que abre selector de niño |
| **ViewModel** | `ExerciseDetailViewModel` — carga `GET /exercises/{id}` |
| **API** | `GET /exercises/{id}` → `ExerciseResponse` |
| **Navegación** | Botón "Usar" → navega a `session_create/{childId}/{exerciseId}` (requiere seleccionar child primero, o navegar a `children` con `exerciseId` extra) |
| **Nota** | Esta pantalla no tenía referente en el prototipo HTML (se creó en el nuevo flujo). |

---

### 4.10 SessionCreateScreen (`session_create/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Confirmar y crear una nueva sesión antes de iniciar |
| **Layout** | "Nueva Sesión" + tarjeta del niño seleccionado + tarjeta del ejercicio seleccionado + botón "Iniciar Sesión" |
| **ViewModel** | `SessionCreateViewModel` — llama `POST /sessions` |
| **API** | `POST /sessions` → `{ child_id, exercise_id }` → `{ session_id, started_at }` |
| **Navegación** | Éxito → `sessions/{sessionId}` |
| **Nota** | Screen nueva. En el prototipo HTML esto se hacía en 2 pasos: seleccionar ejercicio (modal) → ir directo a sesión activa. |

---

### 4.11 SessionDetailScreen (`session_detail/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Sesión activa con cronómetro, canvas de dibujo y métricas en tiempo real |
| **Layout** | Top bar con nombre niño + ejercicio + badge lápiz; badge de estado (No iniciada/En progreso/Finalizada); cronómetro circular `MM:SS`; canvas interactivo con guías de trazo; panel de feedback en vivo; botones Iniciar / Terminar |
| **ViewModel** | `SessionDetailViewModel` — maneja timer, conexión WebSocket, llama `PATCH /sessions/{id}/end` |
| **API** | `GET /sessions/{id}` (carga datos); `PATCH /sessions/{id}/end` → `{ close_reason }` → métricas; WebSocket `ws://host/ws/sessions/{id}?token=JWT` para feedback |
| **Estados** | Tres estados: `Idle` (No iniciada), `Running` (En progreso), `Finished` (Finalizada) |
| **Transiciones** | Click "Iniciar" → `POST /sessions` si no existe, arranca timer, habilita "Terminar"; Click "Terminar" → `PATCH /sessions/{id}/end`, detiene timer, espera 1.2s y navega a `children/{childId}/history` |
| **Navegación** | Al finalizar → `children/{childId}/history` (popUpTo main_menu); flecha atrás → popBackStack (detiene timer) |
| **Referencia HTML** | `session_detail/code.html` (antes active_exercise/) + `index.html` líneas ~1150-1350 |

---

### 4.12 SessionHistoryScreen (`session_history/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Historial de sesiones de un niño específico |
| **Layout** | Header con avatar + nombre del niño + ID + botón editar + `LazyColumn` de sesiones. Cada item: nombre ejercicio, badge (Completada/En Progreso), fecha, duración, score, botones (Ver Detalle, Repetir, Continuar/Terminar) + FAB "Nueva Sesión" |
| **ViewModel** | `SessionHistoryViewModel` — carga `GET /children/{id}/sessions` |
| **API** | `GET /children/{id}/sessions?skip=0&limit=50` → `[SessionResponse]` |
| **Navegación** | Click sesión → `sessions/{sessionId}` si está en progreso, o detalle si completada; FAB → selector de ejercicio → `session_create/{childId}/{exerciseId}`; botón editar → `children/{childId}/edit` |
| **Referencia HTML** | Tab "Sesiones" en `index.html` líneas ~700-900 |

---

### 4.13 DashboardScreen (`dashboard_screen/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Dashboard de métricas globales del tutor |
| **Layout** | Saludo "Hola, {nombre}" + 4 tarjetas métricas (Total alumnos, Total sesiones, Precisión promedio, Alertas) + actividad reciente + gráfico semanal de sesiones + lista de últimas sesiones (clickeables) |
| **ViewModel** | `DashboardViewModel` — carga `GET /dashboard/metrics`, `GET /dashboard/recent-activity` o endpoints equivalentes |
| **API** | Depende de implementación backend: puede ser `GET /sessions?skip=0&limit=10` + cálculos locales, o endpoints dedicados |
| **Navegación** | Click en sesión → `sessions/{sessionId}`; BottomNav |
| **Referencia HTML** | `main_menu/code.html` (antes homepage/) sección de tarjetas + `index.html` líneas ~450-600 (homepage) + tab Métricas ~900-970 |

---

### 4.14 ProfileScreen (`profile_screen/`)

| Aspecto | Detalle |
|---|---|
| **Propósito** | Perfil del tutor con opciones y cierre de sesión |
| **Layout** | Avatar + nombre completo + email + opciones (Editar Perfil, Notificaciones, Acerca de) + botón "Cerrar Sesión" (borde rojo) |
| **ViewModel** | `ProfileViewModel` — carga `GET /profile`, limpia DataStore al cerrar sesión |
| **API** | `GET /profile` → `{ name, lastname, email, phone }` |
| **Navegación** | Cerrar sesión → limpia JWT → `login` (popUpTo 0 inclusive) |
| **Referencia HTML** | Sección "Perfil" en `index.html` líneas ~1200-1350 |

---

## 5. Mapeo DB → Screens

| Tabla | Screens que la consumen | Endpoints |
|---|---|---|
| `users` | LoginScreen, RegisterScreen, ProfileScreen | `POST /auth/login`, `POST /auth/register`, `GET /profile` |
| `children` | ChildrenListScreen, ChildCreateScreen, ChildEditScreen, SessionHistoryScreen, SessionCreateScreen | `GET /children/`, `POST /children/`, `GET /children/{id}`, `PUT /children/{id}`, `PATCH /children/{id}/deactivate` |
| `stroke_types` | ExercisesScreen | `GET /stroke-types` |
| `exercises` | ExercisesScreen, ExerciseDetailScreen, SessionCreateScreen, SessionDetailScreen | `GET /exercises`, `GET /exercises/{id}` |
| `sessions` | SessionHistoryScreen, SessionCreateScreen, SessionDetailScreen, DashboardScreen | `POST /sessions`, `GET /sessions/{id}`, `GET /children/{id}/sessions`, `PATCH /sessions/{id}/end` |

---

## 6. Diagrama de navegación

```
splash
  │
  ├── (sin token) ──→ login ──→ register
  │                     │
  │                     └────────┐
  │                              ▼
  └── (con token) ──→ main_menu (hub + BottomNav)
                        │
           ┌────────────┼────────────┐
           ▼            ▼            ▼
    children_list   exercises    dashboard
           │            │            │
           ▼            ▼            ▼
    child_create   exercise_     sessions/
    child_edit     detail        {sessionId}
           │
           ▼
    session_history (por childId)
           │
           ├──→ session_create/{childId}/{exerciseId}
           │         │
           │         ▼
           │    session_detail/{sessionId}
           │         │
           │         └──→ session_history (al terminar)
           │
           └──→ child_edit

profile (accesible desde BottomNav)
    │
    └──→ login (cerrar sesión)
```

---

## 7. Consideraciones técnicas

| Aspecto | Decisión |
|---|---|
| **Navegación** | Navigation Compose con rutas tipo `"children/{childId}/history"` |
| **Estado** | ViewModel + StateFlow |
| **HTTP** | Retrofit + OkHttp + interceptor JWT (lee token de DataStore) |
| **Persistencia local** | DataStore Preferences (token JWT, preferencias de usuario) |
| **Tiempo real** | OkHttp WebSocket para feedback durante sesión activa |
| **Canvas/dibujo** | `Canvas` Composable + `PointerInputScope` para detectar trazos |
| **Gráficos** | MPAndroidChart o Vico (charting library para Compose) |
| **Diálogos** | `Dialog` Composable o `AlertDialog` para confirmaciones |
| **DatePicker** | `DatePickerDialog` Composable o Material3 DatePicker |
| **Inyección** | Hilt (recomendado) |
| **Tests** | JUnit + Mockk + Compose Test |

---

## 8. Seed data (stroke_types + exercises)

La base de datos ya contiene 10 tipos de trazo y 9 ejercicios. Para la app, los ejercicios visibles son los 8 activos:

| Ejercicio | stroke_type | Nivel |
|---|---|---|
| Líneas verticales | recto_vertical | 1 |
| Líneas horizontales | recto_horizontal | 1 |
| Curvas simples | curva | 2 |
| Ondas | onda | 2 |
| Círculos | circulo | 3 |
| Espirales | espiral | 3 |
| Arcos | arco | 4 |
| Letras individuales | letra | 5 |

El ejercicio "Trazo libre" (`libre`, `is_active = false`) no se muestra en la app.
