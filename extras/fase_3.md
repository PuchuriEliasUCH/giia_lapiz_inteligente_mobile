# FASE_3_GESTION_NINOS.md

# Fase 3 — Gestión de Niños

## Objetivo

Permitir que el tutor gestione los niños asociados a su cuenta.

Al finalizar esta fase el usuario podrá:

* Crear niños.
* Consultar niños registrados.
* Editar información de un niño.
* Desactivar niños.
* Visualizar el estado de cada niño.

---

# Requisitos Previos

## Fase 2 completada

* [ ] Login funcional
* [ ] JWT almacenado
* [ ] SessionManager funcional
* [ ] Retrofit funcional
* [ ] Hilt funcional
* [ ] Navigation funcional

---

# Backend Utilizado

## Obtener niños

```http
GET /children
Authorization: Bearer {token}
```

---

## Crear niño

```http
POST /children
Authorization: Bearer {token}
```

Body:

```json
{
  "name": "Juan"
}
```

---

## Actualizar niño

```http
PUT /children/{id}
Authorization: Bearer {token}
```

Body:

```json
{
  "name": "Juan Pérez"
}
```

---

## Desactivar niño

```http
PATCH /children/{id}/deactivate
Authorization: Bearer {token}
```

---

# Estructura Esperada

```text
models/
└── child/
    ├── ChildResponse.kt
    ├── CreateChildRequest.kt
    └── UpdateChildRequest.kt

data/
└── repository/
    └── ChildRepository.kt

ui/
└── children/
    ├── ChildrenScreen.kt
    ├── AddChildScreen.kt
    ├── EditChildScreen.kt
    ├── ChildrenViewModel.kt
    ├── ChildUiState.kt
    └── Components.kt
```

---

# FASE 3.1 — Modelos

## Objetivo

Representar requests y responses del backend.

---

## Tarea 3.1.1 Crear paquete child

### Ruta

```text
models/child
```

### Checklist

* [ ] Crear carpeta child

---

## Tarea 3.1.2 CreateChildRequest

### Archivo

```text
models/child/CreateChildRequest.kt
```

### Campos

* [ ] name

### Validación

* [ ] Compila correctamente

---

## Tarea 3.1.3 UpdateChildRequest

### Archivo

```text
models/child/UpdateChildRequest.kt
```

### Campos

* [ ] name

### Validación

* [ ] Compila correctamente

---

## Tarea 3.1.4 ChildResponse

### Archivo

```text
models/child/ChildResponse.kt
```

### Campos esperados

* [ ] id
* [ ] name
* [ ] is_active
* [ ] created_at

### Validación

* [ ] Coincide con backend
* [ ] Compila correctamente

---

# FASE 3.2 — API

## Objetivo

Agregar endpoints al ApiService.

---

## Tarea 3.2.1 GET Children

### Endpoint

```http
GET /children
```

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Header Authorization

---

## Tarea 3.2.2 POST Child

### Endpoint

```http
POST /children
```

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Body
* [ ] Agregar Header Authorization

---

## Tarea 3.2.3 PUT Child

### Endpoint

```http
PUT /children/{id}
```

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Path id
* [ ] Agregar Body
* [ ] Agregar Authorization

---

## Tarea 3.2.4 PATCH Deactivate

### Endpoint

```http
PATCH /children/{id}/deactivate
```

### Checklist

* [ ] Crear endpoint
* [ ] Agregar Path id
* [ ] Agregar Authorization

---

## Validación API

* [ ] Compila correctamente
* [ ] Todos los endpoints responden

---

# FASE 3.3 — Repository

## Objetivo

Centralizar toda la lógica relacionada a niños.

---

## Tarea 3.3.1 Crear ChildRepository

### Archivo

```text
data/repository/ChildRepository.kt
```

### Checklist

* [ ] Crear archivo
* [ ] Inyectar ApiService

---

## Tarea 3.3.2 Obtener niños

### Función

```kotlin
getChildren()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Tarea 3.3.3 Crear niño

### Función

```kotlin
createChild()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Tarea 3.3.4 Actualizar niño

### Función

```kotlin
updateChild()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Tarea 3.3.5 Desactivar niño

### Función

```kotlin
deactivateChild()
```

### Checklist

* [ ] Implementada
* [ ] Manejo de errores

---

## Validación Repository

* [ ] CRUD funcional
* [ ] Manejo de errores correcto

---

# FASE 3.4 — ViewModel

## Objetivo

Administrar estado de la UI.

---

## Tarea 3.4.1 Crear ChildUiState

### Estados

* [ ] Loading
* [ ] Success
* [ ] Error
* [ ] Empty

---

## Tarea 3.4.2 Crear ChildrenViewModel

### Funciones

* [ ] loadChildren()
* [ ] createChild()
* [ ] updateChild()
* [ ] deactivateChild()

### Estado

* [ ] StateFlow configurado

---

## Validación

* [ ] Actualiza UI correctamente
* [ ] Maneja errores

---

# FASE 3.5 — ChildrenScreen

## Objetivo

Mostrar lista de niños.

---

## Tarea 3.5.1 Crear Screen

### Archivo

```text
ChildrenScreen.kt
```

### Checklist

* [ ] Crear pantalla

---

## Tarea 3.5.2 Mostrar lista

### Componentes

* [ ] LazyColumn
* [ ] Card por niño

---

## Tarea 3.5.3 Información visible

### Mostrar

* [ ] Nombre
* [ ] Estado
* [ ] Fecha de creación

---

## Tarea 3.5.4 Estados

### Loading

* [ ] CircularProgressIndicator

### Empty

* [ ] Mensaje sin niños

### Error

* [ ] Mensaje error

---

## Tarea 3.5.5 Acciones

### Botones

* [ ] Agregar niño
* [ ] Editar niño
* [ ] Desactivar niño

---

## Validación

* [ ] Lista carga correctamente

---

# FASE 3.6 — AddChildScreen

## Objetivo

Registrar un nuevo niño.

---

## Tarea 3.6.1 Crear pantalla

### Archivo

```text
AddChildScreen.kt
```

### Checklist

* [ ] Crear pantalla

---

## Tarea 3.6.2 Campo nombre

### Campo

* [ ] TextField nombre

---

## Tarea 3.6.3 Validaciones

### Nombre

* [ ] Vacío
* [ ] Longitud mínima

---

## Tarea 3.6.4 Guardar

### Acción

* [ ] Consumir createChild()

---

## Navegación

* [ ] Regresar a lista

---

## Validación

* [ ] Niño creado correctamente

---

# FASE 3.7 — EditChildScreen

## Objetivo

Modificar información del niño.

---

## Tarea 3.7.1 Crear pantalla

### Archivo

```text
EditChildScreen.kt
```

### Checklist

* [ ] Crear pantalla

---

## Tarea 3.7.2 Cargar información

### Checklist

* [ ] Mostrar nombre actual

---

## Tarea 3.7.3 Actualizar

### Acción

* [ ] Consumir updateChild()

---

## Navegación

* [ ] Volver a listado

---

## Validación

* [ ] Actualización exitosa

---

# FASE 3.8 — Desactivar Niño

## Objetivo

Permitir desactivar un niño sin eliminarlo.

---

## Tarea 3.8.1 Mostrar diálogo

### Checklist

* [ ] AlertDialog

---

## Tarea 3.8.2 Confirmación

### Botones

* [ ] Confirmar
* [ ] Cancelar

---

## Tarea 3.8.3 Backend

### Acción

* [ ] Consumir deactivateChild()

---

## Tarea 3.8.4 Refrescar lista

### Checklist

* [ ] Recargar ChildrenScreen

---

## Validación

* [ ] Estado actualizado

---

# FASE 3.9 — Navegación

## Objetivo

Integrar módulo Children al flujo principal.

---

## Routes

### Agregar

* [ ] CHILDREN
* [ ] ADD_CHILD
* [ ] EDIT_CHILD

---

## NavGraph

### Registrar

* [ ] ChildrenScreen
* [ ] AddChildScreen
* [ ] EditChildScreen

---

## Navegaciones

### Children → Add

* [ ] Funciona

### Children → Edit

* [ ] Funciona

### Add → Children

* [ ] Funciona

### Edit → Children

* [ ] Funciona

---

# FASE 3.10 — Validación Final

## CRUD

### Crear

* [ ] Crear niño

### Leer

* [ ] Obtener niños

### Actualizar

* [ ] Editar niño

### Desactivar

* [ ] Desactivar niño

---

## UI

* [ ] Lista visible
* [ ] Loading visible
* [ ] Empty state visible
* [ ] Error state visible

---

## Seguridad

* [ ] JWT enviado correctamente
* [ ] Endpoints protegidos funcionan

---

## Navegación

* [ ] Flujo completo funcional

---

# Entregables Finales

```text
models/
└── child/
    ├── ChildResponse.kt
    ├── CreateChildRequest.kt
    └── UpdateChildRequest.kt

data/
└── repository/
    └── ChildRepository.kt

ui/
└── children/
    ├── ChildrenScreen.kt
    ├── AddChildScreen.kt
    ├── EditChildScreen.kt
    ├── ChildrenViewModel.kt
    ├── ChildUiState.kt
    └── Components.kt
```

Estado esperado:

* [ ] Usuario puede crear niños.
* [ ] Usuario puede listar niños.
* [ ] Usuario puede editar niños.
* [ ] Usuario puede desactivar niños.
* [ ] Gestión de niños completada al 100%.
