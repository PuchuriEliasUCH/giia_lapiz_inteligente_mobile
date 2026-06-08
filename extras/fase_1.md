# FASE_1_INFRAESTRUCTURA.md

# Fase 1 — Infraestructura

## Objetivo

Preparar toda la infraestructura base del proyecto Android para que las siguientes fases (Autenticación, Niños, Ejercicios, Sesiones, Dashboard y WebSocket) puedan desarrollarse sin modificar la arquitectura.

---

# Convención

```text
[ ] Tarea no iniciada
[x] Tarea completada
```

---

# Criterio de Finalización

La Fase 1 se considera terminada cuando:

* [x] El proyecto compila correctamente.
* [x] Hilt funciona.
* [x] Navigation Compose funciona.
* [x] Retrofit está configurado.
* [x] DataStore está configurado.
* [x] La estructura MVVM existe.
* [x] Se puede navegar entre Login y Register.

---

# BLOQUE 1 — Preparación del Proyecto

## Tarea 1.1 — Verificar proyecto inicial

### Objetivo

Confirmar que el proyecto Empty Activity funciona correctamente.

### Checklist

* [x] Abrir proyecto Android Studio
* [x] Sincronizar Gradle
* [x] Ejecutar aplicación
* [x] Verificar pantalla inicial Compose
* [x] Verificar ausencia de errores

### Resultado esperado

Aplicación funcionando correctamente.

---

## Tarea 1.2 — Verificar configuración base

### Revisar

```text
settings.gradle.kts
```

```text
app/build.gradle.kts
```

```text
AndroidManifest.xml
```

### Checklist

* [x] Namespace correcto
* [x] applicationId correcto
* [x] minSdk correcto
* [x] targetSdk correcto

---

# BLOQUE 2 — Dependencias Base

## Tarea 2.1 — Navigation Compose

### Archivo

```text
app/build.gradle.kts
```

### Checklist

* [x] Agregar dependencia Navigation Compose
* [x] Sincronizar Gradle
* [x] Verificar compilación

---

## Tarea 2.2 — Lifecycle ViewModel

### Checklist

* [x] Agregar dependencia ViewModel Compose
* [x] Sincronizar Gradle
* [x] Verificar compilación

---

## Tarea 2.3 — Coroutines

### Checklist

* [x] Agregar dependencia Coroutines
* [x] Sincronizar Gradle
* [x] Verificar compilación

---

## Tarea 2.4 — Retrofit

### Checklist

* [x] Agregar Retrofit
* [x] Agregar Gson Converter
* [x] Sincronizar Gradle
* [x] Verificar compilación

---

## Tarea 2.5 — Logging Interceptor

### Checklist

* [x] Agregar Logging Interceptor
* [x] Sincronizar Gradle
* [x] Verificar compilación

---

## Tarea 2.6 — DataStore

### Checklist

* [x] Agregar dependencia DataStore
* [x] Sincronizar Gradle
* [x] Verificar compilación

---

# BLOQUE 3 — Configuración de Hilt

## Tarea 3.1 — Configurar Plugins

### Checklist

* [x] Agregar plugin Hilt
* [x] Agregar plugin KSP
* [x] Sincronizar Gradle

---

## Tarea 3.2 — Dependencias Hilt

### Checklist

* [x] Agregar dependencia hilt-android
* [x] Agregar compilador Hilt
* [x] Verificar compilación

---

## Tarea 3.3 — Clase Application

### Crear

```text
App.kt
```

### Checklist

* [x] Crear clase App
* [x] Agregar @HiltAndroidApp

---

## Tarea 3.4 — AndroidManifest

### Checklist

* [x] Registrar clase App

---

## Tarea 3.5 — MainActivity

### Checklist

* [x] Agregar @AndroidEntryPoint
* [x] Verificar compilación

---

# BLOQUE 4 — Arquitectura MVVM

## Tarea 4.1 — Crear estructura principal

### Crear

```text
com.lapizinteligente

├── data
├── domain
├── ui
├── navigation
├── di
├── models
```

### Checklist

* [x] data
* [x] domain
* [x] ui
* [x] navigation
* [x] di
* [x] models

---

## Tarea 4.2 — Estructura Data

### Crear

```text
data
├── remote
├── repository
├── datastore
```

### Checklist

* [x] remote
* [x] repository
* [x] datastore

---

## Tarea 4.3 — Estructura UI

### Crear

```text
ui
├── auth
├── children
├── exercises
├── sessions
├── dashboard
```

### Checklist

* [x] auth
* [x] children
* [x] exercises
* [x] sessions
* [x] dashboard

---

# BLOQUE 5 — Navigation

## Tarea 5.1 — Routes

### Crear

```text
navigation/Routes.kt
```

### Checklist

* [x] Crear archivo
* [x] Definir LOGIN
* [x] Definir REGISTER
* [x] Definir HOME

---

## Tarea 5.2 — Pantallas temporales

### Crear

```text
ui/auth/LoginScreen.kt
ui/auth/RegisterScreen.kt
```

### Checklist

* [x] LoginScreen
* [x] RegisterScreen

---

## Tarea 5.3 — NavGraph

### Crear

```text
navigation/NavGraph.kt
```

### Checklist

* [x] Crear NavHost
* [x] Registrar LoginScreen
* [x] Registrar RegisterScreen

---

## Tarea 5.4 — Navegación de prueba

### Checklist

* [x] Login → Register
* [x] Register → Login

---

# BLOQUE 6 — Retrofit

## Tarea 6.1 — ApiService

### Crear

```text
data/remote/ApiService.kt
```

### Checklist

* [x] Crear archivo

---

## Tarea 6.2 — Network Module

### Crear

```text
di/NetworkModule.kt
```

### Checklist

* [x] Configurar OkHttp
* [x] Configurar Logging Interceptor
* [x] Configurar Retrofit
* [x] Configurar Gson

---

## Tarea 6.3 — Base URL

### Checklist

* [x] Definir BASE_URL backend

---

# BLOQUE 7 — DataStore

## Tarea 7.1 — SessionManager

### Crear

```text
data/datastore/SessionManager.kt
```

### Checklist

* [x] Crear archivo

---

## Tarea 7.2 — Keys

### Checklist

* [x] Crear TOKEN_KEY

---

## Tarea 7.3 — Funciones

### Checklist

* [ ] saveToken()
* [ ] getToken()
* [ ] clearToken()

---

# BLOQUE 8 — Verificación Final

## Arquitectura

* [ ] Estructura MVVM creada

## Hilt

* [ ] Inyección funcionando

## Navigation

* [ ] Navegación funcionando

## Retrofit

* [ ] Retrofit inicializado

## DataStore

* [ ] SessionManager implementado

## Compilación

* [ ] Proyecto compila sin errores

---

# Entregables

Al finalizar la Fase 1 deberán existir:

```text
App.kt

navigation/
├── Routes.kt
├── NavGraph.kt

data/
├── remote/
│   └── ApiService.kt
├── repository/
├── datastore/
│   └── SessionManager.kt

ui/
├── auth/
│   ├── LoginScreen.kt
│   └── RegisterScreen.kt

di/
└── NetworkModule.kt
```

Estado final esperado:

* Proyecto Android estable.
* Arquitectura MVVM preparada.
* Dependencias configuradas.
* Base lista para iniciar la Fase 2 (Autenticación).
