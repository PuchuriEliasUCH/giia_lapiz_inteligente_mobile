# FASE_6_DASHBOARD.md

# Fase 6 — Dashboard y Métricas

## Objetivo

Visualizar los resultados obtenidos durante las sesiones.

Esta fase transforma los datos generados por el backend en información comprensible para padres, tutores y especialistas.

---

# Requisitos Previos

## Fase 5 completada

* [ ] Sesiones funcionales
* [ ] Historial funcional
* [ ] Detalle de sesión funcional

---

# Métricas Disponibles

## Presión

* [ ] avg_pressure
* [ ] max_pressure

## Estabilidad

* [ ] pressure_stability
* [ ] movement_stability

## Temblor

* [ ] tremor_level

## Postura

* [ ] posture_score

---

# Estructura Esperada

```text
models/
└── dashboard/
    ├── MetricsResponse.kt
    └── DashboardSummary.kt

data/
└── repository/
    └── DashboardRepository.kt

ui/
└── dashboard/
    ├── DashboardScreen.kt
    ├── DashboardViewModel.kt
    ├── DashboardUiState.kt
    ├── MetricCard.kt
    ├── PressureCard.kt
    ├── StabilityCard.kt
    ├── TremorCard.kt
    └── PostureCard.kt
```

---

# FASE 6.1 — Modelos

## MetricsResponse

### Checklist

* [ ] avg_pressure
* [ ] max_pressure
* [ ] pressure_stability
* [ ] movement_stability
* [ ] tremor_level
* [ ] posture_score

---

## DashboardSummary

### Checklist

* [ ] score
* [ ] recommendation
* [ ] performance_level

---

## Validación

* [ ] Modelos compilan

---

# FASE 6.2 — API

## Objetivo

Consumir métricas desde backend.

---

## Obtener Dashboard

### Checklist

* [ ] Crear endpoint dashboard

---

## Obtener métricas de sesión

### Checklist

* [ ] Crear endpoint detalle métricas

---

## Validación

* [ ] Datos recibidos correctamente

---

# FASE 6.3 — Repository

## DashboardRepository

### Checklist

* [ ] Crear DashboardRepository

---

## Funciones

### Dashboard

* [ ] getDashboard()

### Métricas

* [ ] getMetrics()

---

## Manejo de errores

* [ ] IOException
* [ ] HttpException
* [ ] Result<T>

---

## Validación

* [ ] Datos obtenidos correctamente

---

# FASE 6.4 — ViewModel

## DashboardUiState

### Estados

* [ ] Loading
* [ ] Success
* [ ] Error
* [ ] Empty

---

## DashboardViewModel

### Funciones

* [ ] loadDashboard()
* [ ] loadMetrics()

---

### StateFlow

* [ ] Configurado

---

## Validación

* [ ] Actualiza UI correctamente

---

# FASE 6.5 — Tarjeta Presión

## PressureCard

### Mostrar

* [ ] avg_pressure
* [ ] max_pressure

---

### Interpretación

* [ ] Baja
* [ ] Normal
* [ ] Alta

---

## Validación

* [ ] Valores correctos

---

# FASE 6.6 — Tarjeta Estabilidad

## StabilityCard

### Mostrar

* [ ] pressure_stability
* [ ] movement_stability

---

### Interpretación

* [ ] Buena
* [ ] Regular
* [ ] Baja

---

## Validación

* [ ] Información correcta

---

# FASE 6.7 — Tarjeta Temblor

## TremorCard

### Mostrar

* [ ] tremor_level

---

### Interpretación

* [ ] Bajo
* [ ] Medio
* [ ] Alto

---

## Validación

* [ ] Información correcta

---

# FASE 6.8 — Tarjeta Postura

## PostureCard

### Mostrar

* [ ] posture_score

---

### Interpretación

* [ ] Excelente
* [ ] Buena
* [ ] Mejorable

---

## Validación

* [ ] Información correcta

---

# FASE 6.9 — Dashboard Principal

## Archivo

```text
DashboardScreen.kt
```

---

## Componentes

### Tarjetas

* [ ] PressureCard
* [ ] StabilityCard
* [ ] TremorCard
* [ ] PostureCard

---

### Resumen

* [ ] Nivel de desempeño
* [ ] Recomendación
* [ ] Puntaje general

---

### Historial reciente

* [ ] Últimas sesiones
* [ ] Últimos ejercicios

---

## Estados

### Loading

* [ ] Visible

### Empty

* [ ] Visible

### Error

* [ ] Visible

---

## Validación

* [ ] Dashboard visible

---

# FASE 6.10 — Dashboard por Sesión

## Objetivo

Consultar métricas específicas.

---

## Mostrar

### Presión

* [ ] Visible

### Estabilidad

* [ ] Visible

### Temblor

* [ ] Visible

### Postura

* [ ] Visible

---

## Navegación

### Historial → Dashboard

* [ ] Funciona

---

## Validación

* [ ] Métricas correctas

---

# FASE 6.11 — Navegación

## Routes

### Agregar

* [ ] DASHBOARD
* [ ] SESSION_METRICS

---

## NavGraph

### Registrar

* [ ] DashboardScreen
* [ ] SessionMetricsScreen

---

## Navegación

### Dashboard → Métricas

* [ ] Funciona

### Historial → Métricas

* [ ] Funciona

---

# FASE 6.12 — Validación Final

## Métricas

* [ ] avg_pressure
* [ ] max_pressure
* [ ] pressure_stability
* [ ] movement_stability
* [ ] tremor_level
* [ ] posture_score

---

## Dashboard

* [ ] Tarjeta presión
* [ ] Tarjeta estabilidad
* [ ] Tarjeta temblor
* [ ] Tarjeta postura

---

## Resumen

* [ ] Desempeño visible
* [ ] Recomendaciones visibles

---

## Historial

* [ ] Últimas sesiones visibles

---

## Navegación

* [ ] Dashboard funcional

---

## Backend

* [ ] Datos reales desde API

---

# Entregables

```text
models/dashboard/
├── MetricsResponse.kt
└── DashboardSummary.kt

data/repository/
└── DashboardRepository.kt

ui/dashboard/
├── DashboardScreen.kt
├── DashboardViewModel.kt
├── DashboardUiState.kt
├── MetricCard.kt
├── PressureCard.kt
├── StabilityCard.kt
├── TremorCard.kt
└── PostureCard.kt
```

Estado esperado:

* [ ] Dashboard completo.
* [ ] Métricas reales visibles.
* [ ] Historial integrado.
* [ ] Fase 6 completada al 100%.
