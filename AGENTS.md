# AGENTS.md

# Smart Pencil Android Application

Read this document completely before making any architectural, implementation, testing, or refactoring decision.

This document is the source of truth for Android development.

---

# Project Overview

The Smart Pencil project is an Android application used by tutors and parents to monitor handwriting practice sessions performed by children using a custom ESP32-based smart pencil.

The application:

* Authenticates users using JWT.
* Manages children profiles.
* Manages handwriting exercises.
* Creates and closes writing sessions.
* Displays real-time feedback from the backend.
* Shows historical metrics and session summaries.

The application does NOT:

* Perform medical diagnosis.
* Execute machine learning models locally.
* Process raw IMU signals beyond transportation.
* Make healthcare decisions.

---

# Architecture Principles

The project follows:

* MVVM
* Clean Architecture
* Single Responsibility Principle
* Dependency Injection
* Feature-based modular organization

Mandatory flow:

UI → ViewModel → UseCase → Repository → DataSource

Never bypass layers.

---

# Mandatory Technology Stack

* Kotlin
* Jetpack Compose
* Material 3
* Hilt
* Retrofit
* OkHttp
* Coroutines
* StateFlow
* Navigation Compose
* DataStore
* Room
* Bluetooth LE
* OkHttp WebSocket

---

# Code Rules

## Forbidden

* Business logic inside Composables
* Retrofit calls inside ViewModels
* Direct DataStore access from UI
* GlobalScope
* Hardcoded strings
* Singleton objects outside Hilt modules

## Required

* Error handling
* Loading states
* Empty states
* Success states
* Kotlin documentation on public APIs

---

# Security

All application functionality requires authentication.

Only:

* POST /auth/login
* POST /auth/register

are public endpoints.

JWT must be stored using DataStore.

Never persist passwords.

---

# Development Priorities

1. Authentication
2. Children Management
3. Exercise Catalog
4. Session Management
5. Session History
6. WebSocket Integration
7. BLE Integration
8. MQTT Integration
9. ML Features

Never start with BLE or Machine Learning.

---

# Decision Rule

When multiple solutions exist:

Prefer the simplest implementation that satisfies the requirements.
