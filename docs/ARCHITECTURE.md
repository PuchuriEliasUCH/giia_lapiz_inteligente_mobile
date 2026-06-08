# ARCHITECTURE.md

# Architecture Overview

The Android application follows MVVM combined with Clean Architecture.

```text
UI
 ↓
ViewModel
 ↓
UseCase
 ↓
Repository
 ↓
DataSource
 ↓
Backend / Local Storage
```

---

# Layers

## Presentation Layer

Contains:

* Screens
* Navigation
* UI State
* ViewModels

Responsibilities:

* User interaction
* State rendering
* Event dispatching

No business logic is allowed.

---

## Domain Layer

Contains:

* Entities
* UseCases
* Repository contracts

Responsibilities:

* Business rules
* Validation
* Application workflows

The domain layer must remain independent from Android SDK classes.

---

## Data Layer

Contains:

* Repository implementations
* Remote data sources
* Local data sources

Responsibilities:

* API communication
* Local persistence
* Data mapping

---

# Feature Structure

Each feature follows:

```text
feature/

data/
domain/
presentation/
```

Example:

```text
auth/

data/
domain/
presentation/
```

---

# Core Modules

## network

* Retrofit
* OkHttp
* JWT Interceptor
* Error Mapping

## storage

* DataStore
* Local Preferences

## websocket

* Session Feedback
* Reconnection Logic

## ble

* Device Discovery
* Device Connection
* Characteristic Communication

## mqtt

* Session Data Publishing

---

# State Management

Every ViewModel exposes:

```kotlin
StateFlow<UiState>
```

No LiveData for new implementations.

---

# Error Handling

Repositories never throw exceptions to upper layers.

All failures must be mapped to Result<T> or a sealed class.

---

# Testing Strategy

* Unit Tests
* ViewModel Tests
* Repository Tests
* Integration Tests

BLE and MQTT must be abstracted to allow mocking.

