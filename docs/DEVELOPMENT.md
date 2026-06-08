# DEVELOPMENT.md

# Development Guidelines

## Git Flow

Branches:

```text
main
develop
feature/*
bugfix/*
hotfix/*
```

Examples:

```text
feature/authentication
feature/session-management
feature/websocket-feedback
```

---

# Commit Convention

Format:

```text
type(scope): description
```

Examples:

```text
feat(auth): implement login screen
fix(session): resolve websocket reconnect issue
refactor(network): simplify token interceptor
```

---

# Code Style

Follow:

* Kotlin Coding Conventions
* Official Android Guidelines

Use:

* ktlint
* detekt

---

# Dependency Injection

All dependencies must be provided using Hilt.

Manual singleton implementations are not allowed.

---

# Coroutines

ViewModels:

```kotlin
viewModelScope.launch { }
```

Repositories:

```kotlin
withContext(Dispatchers.IO)
```

Continuous streams:

```kotlin
Flow<T>
```

---

# UI Guidelines

* Material 3
* Accessibility first
* Minimum touch target: 48dp
* Responsive layouts
* No hardcoded strings

---

# Environment Variables

Required:

```properties
BASE_URL=
MQTT_BROKER_URL=
```

Never commit local environment values.

---

# Pull Requests

Every PR must:

* Build successfully
* Pass all tests
* Respect architecture rules
* Include a description of changes

