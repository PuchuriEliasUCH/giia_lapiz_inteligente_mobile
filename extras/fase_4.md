# FASE_4_EJERCICIOS.md

# Fase 4 — Gestión de Ejercicios

## Objetivo

Permitir que el tutor consulte el catálogo de ejercicios disponibles para los niños.

Los ejercicios son administrados por el backend y la aplicación móvil solo los consume.

Al finalizar esta fase el usuario podrá:

* Consultar todos los ejercicios.
* Filtrar ejercicios por tipo de trazo.
* Ver el detalle de un ejercicio.
* Consultar información pedagógica del ejercicio.
* Seleccionar ejercicios posteriormente para crear sesiones.

---

# Requisitos Previos

## Fase 2 completada

* [ ] Login funcional
* [ ] JWT funcional
* [ ] Navigation funcional

## Fase 3 completada

* [ ] Gestión de niños funcional

---

# Backend Utilizado

## Obtener tipos de trazo

```http
GET /stroke-types
Authorization: Bearer {token}
```

---

## Obtener ejercicios

```http
GET /exercises
Authorization: Bearer {token}
```

---

## Obtener ejercicios por tipo

```http
GET /exercises?stroke_type_id={id}
Authorization: Bearer {token}
```

---

## Obtener detalle de ejercicio

```http
GET /exercises/{id}
Authorization: Bearer {token}
```

---

# Estructura Esperada

```text
models/
└── exercise/
    ├── StrokeTypeResponse.kt
    ├── ExerciseResponse.kt
    └── ExerciseDetailResponse.kt

data/
└── repository/
    └── ExerciseRepository.kt

ui/
└── exercises/
    ├── ExercisesScreen.kt
    ├── ExerciseDetailScreen.kt
    ├── ExercisesViewModel.kt
    ├── ExerciseUiState.kt
    ├── ExerciseCard.kt
    └── FilterSection.kt
```

---

# FASE 4.1 — Modelos

## Objetivo

Representar los datos recibidos desde la API.

---

## Tarea 4.1.1 Crear paquete exercise

### Ruta

```text
models/exercise
```

### Checklist

* [ ] Crear carpeta exercise

---

## Tarea 4.1.2 StrokeTypeResponse

### Archivo

```text
models/exercise/StrokeTypeResponse.kt
```

### Campos

* [ ] id
* [ ] name
* [ ] description

### Validación

* [ ] Compila correctamente

---

## Tarea 4.1.3 ExerciseResponse

### Archivo

```text
models/exercise/ExerciseResponse.kt
```

### Campos

* [ ] id
* [ ] name
* [ ] description
* [ ] stroke_type_id

### Validación

* [ ] Compila correctamente

---

## Tarea 4.1.4 ExerciseDetailResponse

### Archivo

```text
models/exercise/ExerciseDetailResponse.kt
```

### Campos

* [ ] id
* [ ] name
* [ ] description
* [ ] stroke_type
* [ ] instructions

### Validación

* [ ] Coincide con backend
* [ ] Compila correctamente

---

# FASE 4.2 — API

## Objetivo

Agregar endpoints relacionados con ejercicios.

---

## Tarea 4.2.1 Obtener tipos de trazo

### Endpoint

```http
GET /stroke-types
```

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Authorization

---

## Tarea 4.2.2 Obtener ejercicios

### Endpoint

```http
GET /exercises
```

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Authorization

---

## Tarea 4.2.3 Obtener ejercicios filtrados

### Endpoint

```http
GET /exercises?stroke_type_id={id}
```

### Checklist

* [ ] Agregar Query Parameter
* [ ] Agregar Authorization

---

## Tarea 4.2.4 Obtener detalle

### Endpoint

```http
GET /exercises/{id}
```

### Checklist

* [ ] Agregar Path id
* [ ] Agregar Authorization

---

## Validación API

* [ ] Todos los endpoints responden
* [ ] JWT enviado correctamente

---

# FASE 4.3 — Repository

## Objetivo

Centralizar la lógica de ejercicios.

---

## Tarea 4.3.1 Crear ExerciseRepository

### Archivo

```text
data/repository/ExerciseRepository.kt
```

### Checklist

* [ ] Crear archivo
* [ ] Inyectar ApiService

---

## Tarea 4.3.2 Obtener tipos

### Función

```kotlin
getStrokeTypes()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Tarea 4.3.3 Obtener ejercicios

### Función

```kotlin
getExercises()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Tarea 4.3.4 Obtener ejercicios filtrados

### Función

```kotlin
getExercisesByStrokeType()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Tarea 4.3.5 Obtener detalle

### Función

```kotlin
getExerciseDetail()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Validación Repository

* [ ] Funciones responden correctamente
* [ ] Errores controlados

---

# FASE 4.4 — ViewModel

## Objetivo

Administrar el estado de la pantalla de ejercicios.

---

## Tarea 4.4.1 ExerciseUiState

### Estados

* [ ] Loading
* [ ] Success
* [ ] Error
* [ ] Empty

---

## Tarea 4.4.2 ExercisesViewModel

### Funciones

* [ ] loadExercises()
* [ ] loadStrokeTypes()
* [ ] filterExercises()
* [ ] loadExerciseDetail()

---

### StateFlow

* [ ] Configurado correctamente

---

## Validación

* [ ] Actualiza UI correctamente
* [ ] Maneja errores

---

# FASE 4.5 — ExercisesScreen

## Objetivo

Mostrar catálogo de ejercicios.

---

## Tarea 4.5.1 Crear pantalla

### Archivo

```text
ExercisesScreen.kt
```

### Checklist

* [ ] Crear pantalla

---

## Tarea 4.5.2 Mostrar ejercicios

### Componentes

* [ ] LazyColumn
* [ ] ExerciseCard

---

## Información visible

### Mostrar

* [ ] Nombre
* [ ] Descripción resumida
* [ ] Tipo de trazo

---

## Estados

### Loading

* [ ] ProgressIndicator

### Empty

* [ ] Sin ejercicios

### Error

* [ ] Error visible

---

## Validación

* [ ] Lista visible
* [ ] Datos correctos

---

# FASE 4.6 — Filtro por Tipo de Trazo

## Objetivo

Permitir filtrar ejercicios.

---

## Tarea 4.6.1 Obtener tipos

### Checklist

* [ ] Cargar tipos desde backend

---

## Tarea 4.6.2 Crear selector

### Componentes

* [ ] DropdownMenu
* [ ] ExposedDropdownMenuBox

---

## Tarea 4.6.3 Aplicar filtro

### Checklist

* [ ] Filtrar lista localmente
  o
* [ ] Consultar backend nuevamente

---

## Tarea 4.6.4 Limpiar filtro

### Checklist

* [ ] Mostrar todos nuevamente

---

## Validación

* [ ] Filtro funcional

---

# FASE 4.7 — ExerciseCard

## Objetivo

Representar visualmente un ejercicio.

---

## Archivo

```text
ExerciseCard.kt
```

### Mostrar

* [ ] Nombre
* [ ] Tipo de trazo
* [ ] Descripción resumida

---

## Acción

* [ ] Abrir detalle

---

## Validación

* [ ] Card reutilizable

---

# FASE 4.8 — Detalle de Ejercicio

## Objetivo

Mostrar información completa.

---

## Archivo

```text
ExerciseDetailScreen.kt
```

---

## Información visible

### Mostrar

* [ ] Nombre
* [ ] Descripción completa
* [ ] Tipo de trazo
* [ ] Instrucciones

---

## Backend

* [ ] Consumir getExerciseDetail()

---

## Estados

### Loading

* [ ] Visible

### Error

* [ ] Visible

---

## Validación

* [ ] Información correcta

---

# FASE 4.9 — Navegación

## Objetivo

Integrar módulo de ejercicios.

---

## Routes

### Agregar

* [ ] EXERCISES
* [ ] EXERCISE_DETAIL

---

## NavGraph

### Registrar

* [ ] ExercisesScreen
* [ ] ExerciseDetailScreen

---

## Navegación

### Exercises → Detail

* [ ] Funciona

### Detail → Exercises

* [ ] Funciona

---

## Validación

* [ ] Flujo completo

---

# FASE 4.10 — Preparación para Sesiones

## Objetivo

Dejar los ejercicios listos para ser usados en la Fase 5.

---

## Checklist

* [ ] Cada ejercicio posee ID
* [ ] ID accesible desde UI
* [ ] ID disponible para crear sesiones
* [ ] ViewModel expone ejercicio seleccionado

---

## Validación

* [ ] Listo para integración con sesiones

---

# FASE 4.11 — Validación Final

## Catálogo

* [ ] Lista completa visible

## Filtros

* [ ] Filtrar por tipo

## Detalle

* [ ] Abrir detalle

## Backend

* [ ] JWT enviado correctamente

## Navegación

* [ ] Flujo funcional

## Estados

* [ ] Loading
* [ ] Empty
* [ ] Error

---

# Entregables Finales

```text
models/
└── exercise/
    ├── StrokeTypeResponse.kt
    ├── ExerciseResponse.kt
    └── ExerciseDetailResponse.kt

data/
└── repository/
    └── ExerciseRepository.kt

ui/
└── exercises/
    ├── ExercisesScreen.kt
    ├── ExerciseDetailScreen.kt
    ├── ExercisesViewModel.kt
    ├── ExerciseUiState.kt
    ├── ExerciseCard.kt
    └── FilterSection.kt
```

---

# Estado esperado al finalizar

* [ ] Usuario puede consultar ejercicios.
* [ ] Usuario puede filtrar ejercicios.
* [ ] Usuario puede ver detalles.
* [ ] Ejercicios listos para crear sesiones.
* [ ] Fase 4 completada al 100%.
