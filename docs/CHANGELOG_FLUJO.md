# GraphiCare — Historial de Cambios (Refactorización de Flujo y Diseño)

**Fecha:** 10/06/2026
**Objetivo:** Alinear la aplicación con el nuevo sistema de diseño (`DESIGN.md`), el flujo actualizado (`flujo.md`) y la guía visual (`index.html`).

---

## 1. Sistema de Diseño (Theme)

### `ui/theme/Color.kt`
- **Añadidos** colores del sistema de contenedores de superficie:
  - `SurfaceContainerLowest` (#FFFFFF), `SurfaceContainerLow` (#EFF4FF), `SurfaceContainer` (#E6EEFF), `SurfaceContainerHigh` (#DEE9FC), `SurfaceContainerHighest` (#D9E3F6)
- **Añadidos** colores de contorno:
  - `Outline` (#737686), `OutlineVariant` (#C3C6D7)
- **Añadido** `OnSurfaceVariant` (#434655) para texto secundario
- **Consolidados** colores semánticos: `Success`, `Warning`, `Info`, `Error`

### `ui/theme/Theme.kt`
- **Actualizado** `LightColorScheme` para incluir los nuevos tokens de contenedores de superficie, contorno y `onSurfaceVariant`
- Se mantiene el esquema monocolor (light) con Atkinson Hyperlegible como tipografía principal

### `ui/theme/Shape.kt`
- Sin cambios (ya coincide con DESIGN.md: small=4dp, medium=8dp, large=16dp)

### `ui/theme/Type.kt`
- Sin cambios estructurales (Atkinson Hyperlegible ya estaba configurado)

---

## 2. Navegación

### `navigation/Routes.kt`
- **Eliminadas** rutas antiguas:
  - `HOME`, `ADD_CHILD`, `EDIT_CHILD` (con nombre), `CREATE_SESSION`, `SESSION_HISTORY`, `CHILD_DASHBOARD`, `SESSION_METRICS`
- **Añadidas** nuevas rutas según `flujo.md`:
  - `MAIN` → hub principal (punto de entrada post-login)
  - `CHILD_CREATE` → `children/create`
  - `CHILD_EDIT` → `children/{childId}/edit`
  - `CHILD_DETAIL` → `children/{childId}` (con tabs Datos/Sesiones/Métricas)
  - `SESSION_CREATE` → `sessions/create/{childId}/{exerciseId}`
  - `SESSION_DETAIL` → `sessions/{sessionId}`
  - `EXERCISE_DETAIL` → `exercises/{exerciseId}`
  - `PROFILE` → `profile`
- **Añadidas** funciones helper para construir rutas con parámetros:
  - `childDetail()`, `childEdit()`, `sessionCreate()`, `sessionDetail()`, `exerciseDetail()`

### `navigation/NavGraph.kt` (Reescrita completamente)
- **Arquitectura**: Scaffold único en NavGraph con BottomNav condicional
- **Bottom Navigation** visible solo en rutas: `dashboard`, `children`, `profile`
- **Flujo de autenticación**:
  - `splash` → `login` o `dashboard` (según token JWT)
  - `login` ↔ `register`
  - `login`/`register` exitoso → `dashboard`
- **Hub principal (BottomNav)**:
  - `dashboard` → DashboardScreen (Inicio) — métricas globales
  - `children` → ChildrenListScreen (Alumnos) — grid de niños
  - `profile` → ProfileScreen (Perfil)
- **Flujo de niños**:
  - `children` → `children/create` (ChildCreateScreen)
  - `children` → `children/{childId}` (ChildDetailScreen — 3 tabs)
  - `children/{childId}` → `children/{childId}/edit` (ChildEditScreen)
  - `children/{childId}` → `sessions/create/{childId}/{exerciseId}` (SessionCreateScreen)
  - `children/{childId}` → `sessions/{sessionId}` (SessionDetailScreen)
- **Flujo de ejercicios**:
  - `exercises` → `exercises/{exerciseId}` (ExerciseDetailScreen)
- **Flujo de sesiones**:
  - `sessions/create/{childId}/{exerciseId}` → `sessions/{sessionId}` (popUpTo create)
  - `sessions/{sessionId}` → popBackStack al finalizar
- **Logout**: desde ProfileScreen → `authRepository.logout()` → navega a `login` (popUpTo 0)

---

## 3. Nuevas Pantallas

### `ui/components/BottomNavBar.kt` (NUEVO)
- Barra de navegación inferior con 3 ítems: Inicio, Alumnos, Perfil
- Usa `NavigationBar` de Material 3 con indicador de selección

### `ui/profile/ProfileScreen.kt` (NUEVO)
- Perfil del tutor con avatar, nombre, email
- Opciones: Editar Perfil, Notificaciones, Acerca de
- Botón "Cerrar Sesión" (color error)
- Diseño basado en `index.html` sección "Perfil"

### `ui/children/ChildDetailScreen.kt` (NUEVO)
- Pantalla de detalle de niño con 3 tabs usando `PrimaryTabRow`:
  - **Datos**: nombre, ID, edad, mano dominante, grado, sesiones, notas, botón editar
  - **Sesiones**: historial de sesiones del niño (esqueleto listo para integrar)
  - **Métricas**: precisión, estabilidad, presión, temblor, evolución (esqueleto listo)
- TopAppBar con botón de retroceso
- Basado en `index.html` sección "Alumno Detail" con tabs

---

## 4. Pantallas Actualizadas

### `ui/splash/SplashScreen.kt`
- Renombrado callback: `onNavigateToDashboard` → `onNavigateToMain`
- Actualizado texto de marca: "GraphiCare" (antes "Lápiz Inteligente")
- Colores ahora usan `MaterialTheme.colorScheme`

### `ui/auth/LoginScreen.kt` (Rediseñada)
- Nuevo layout con branding "GraphiCare" y eslogan
- Inputs con etiquetas persistentes, bordes según DESIGN.md
- Botón "Iniciar Sesión" en color primary
- Fondo y espaciado según sistema de DESIGN.md
- Snackbar para errores

### `ui/auth/RegisterScreen.kt` (Rediseñada)
- Nuevo layout con título "Crear Cuenta" y descripción
- Campos: nombre, email, contraseña, confirmar contraseña
- Checkbox de términos y condiciones
- Botón "Registrarse" en color secondary (CTA)
- Validaciones de campos

### `ui/children/ChildrenListScreen.kt` (Rediseñada)
- **Nuevo layout**: `LazyVerticalGrid` con 2 columnas (`GridCells.Fixed(2)`)
- Barra de búsqueda con `OutlinedTextField`
- Empty state: "No hay alumnos registrados."
- FAB "+" flotante en posición inferior derecha
- Cada child es una `Card` clickeable que navega a ChildDetailScreen
- Eliminado `Scaffold` interno (ahora usa el del NavGraph)

### `ui/children/ChildCreateScreen.kt`
- TopAppBar con colores de superficie (no primary)
- Botón de retroceso (ArrowBack)
- Mismo formulario: nombre, fecha, mano, grado, notas

### `ui/children/ChildEditScreen.kt`
- TopAppBar con colores de superficie
- Botón de retroceso
- Mismo formulario precargado

### `ui/dashboard/DashboardScreen.kt` (Rediseñada)
- Eliminados TopAppBar con primary color (ahora es el tab "Inicio" del BottomNav)
- Nuevo layout con saludo "Hola, Juan"
- Cards de métricas: Sesiones, Rendimiento
- Sección "Actividad Reciente" en Card
- Diseño responsivo con `LazyColumn` y espaciado de 20dp
- Eliminados cards específicos (PressureCard, StabilityCard, etc.) — ahora se muestran globalmente

### `ui/sessions/SessionCreateScreen.kt` (NUEVO — antes SessionsScreen)
- Renombrado de `SessionsScreen` a `SessionCreateScreen`
- Ahora recibe `childId` y `exerciseId` como parámetros (no selectores)
- Layout de confirmación: muestra tarjeta del alumno y tarjeta del ejercicio
- Botón "Iniciar Sesión" que llama `viewModel.createSession()`
- TopAppBar con superficie y retroceso

### `ui/sessions/SessionDetailScreen.kt` (Rediseñada)
- **Nuevo**: Timer circular con Canvas y arco de progreso
- **Nuevo**: Badge de estado ("No iniciada" / "En progreso")
- **Nuevo**: Botones "Iniciar Sesión" y "Terminar Sesión" con estados disabled
- Timer se controla con estado local `isRunning` y `elapsedSeconds`
- Al hacer clic en "Terminar" → muestra diálogo de confirmación
- Mantiene visualización de métricas (PressureCard, StabilityCard, TremorCard, PostureCard)
- TopAppBar con superficie y retroceso

### `ui/exercises/ExercisesScreen.kt`
- Añadido callback `onNavigateBack` para retroceso
- TopAppBar con colores de superficie y botón de retroceso
- Misma funcionalidad de filtros y lista

### `ui/exercises/ExerciseDetailScreen.kt`
- TopAppBar con colores de superficie y botón de retroceso

---

## 5. Pantallas Eliminadas/Obsoletas

| Archivo | Estado | Motivo |
|---------|--------|--------|
| `ui/dashboard/MainMenuScreen.kt` | Obsoleto | Reemplazado por BottomNav en NavGraph |
| `ui/sessions/SessionHistoryScreen.kt` | Obsoleto | Funcionalidad integrada en ChildDetailScreen (tab Sesiones) |
| Ruta `SESSION_HISTORY` | Eliminada | Reemplazada por `children/{childId}` con tabs |
| Ruta `CHILD_DASHBOARD` | Eliminada | Reemplazada por tab Métricas en ChildDetailScreen |
| Ruta `SESSION_METRICS` | Eliminada | Reemplazada por SessionDetailScreen directo |
| Ruta `HOME` | Eliminada | Reemplazada por `dashboard` como inicio del BottomNav |

---

## 6. Dependencias y Configuración

- Sin cambios en `build.gradle.kts` ni `libs.versions.toml`
- Sin cambios en `AndroidManifest.xml`
- Sin cambios en `MainActivity.kt` (sigue siendo Single Activity con `@AndroidEntryPoint`)

---

## 7. Migración de Rutas (Antiguas → Nuevas)

| Ruta Antigua | Ruta Nueva | Notas |
|---|---|---|
| `splash` | `splash` | Sin cambios |
| `login` | `login` | Sin cambios |
| `register` | `register` | Sin cambios |
| `home` | `dashboard` | Ahora es tab "Inicio" del BottomNav |
| `children` | `children` | Ahora es tab "Alumnos" del BottomNav |
| `add_child` | `children/create` | Ruta simplificada |
| `edit_child/{childId}/{childName}` | `children/{childId}/edit` | Sin nombre en ruta |
| — | `children/{childId}` | **Nueva**: detalle con tabs |
| `child_dashboard/{childId}` | — | Integrado en ChildDetailScreen tab Métricas |
| `exercises` | `exercises` | Sin cambios |
| `exercise_detail/{exerciseId}` | `exercises/{exerciseId}` | Sin cambios |
| `create_session` | `sessions/create/{childId}/{exerciseId}` | Ahora recibe parámetros de ruta |
| `session_detail/{sessionId}` | `sessions/{sessionId}` | Sin cambios |
| `session_history` | `children/{childId}/history` | Integrado en ChildDetailScreen |
| `session_metrics/{sessionId}` | — | Reemplazado por SessionDetailScreen |
| `dashboard` | `dashboard` | Ahora es tab "Inicio" |
| — | `profile` | **Nueva**: perfil del tutor |

---

## 8. Pendientes / Próximos Pasos

- [ ] Integrar datos reales en ChildDetailScreen tabs (Sesiones y Métricas)
- [ ] Implementar selector de ejercicios como modal (según `index.html`)
- [ ] Completar gráfico semanal en DashboardScreen
- [ ] Integrar WebSocket para feedback en tiempo real en SessionDetailScreen
- [ ] Reemplazar `menuAnchor()` deprecated por `menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)`
- [ ] Agregar estado de carga/error en ProfileScreen

---

*Documento generado automáticamente tras la refactorización del flujo de navegación y sistema de diseño.*
