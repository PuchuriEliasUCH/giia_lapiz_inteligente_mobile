# DESIGN.md

# Smart Pencil Android Application Design System

This document defines the complete design system, visual language, accessibility rules, navigation structure, screen inventory, and UI implementation constraints for the Smart Pencil Android application.

This document is the single source of truth for all UI and UX decisions.

---

# Product Context

## Purpose

The Smart Pencil application allows parents and tutors to monitor handwriting practice sessions performed by children using a custom ESP32-based smart pencil.

The application focuses on:

* Session monitoring
* Progress tracking
* Exercise management
* Real-time feedback
* Historical analysis

The application does NOT:

* Perform medical diagnosis
* Replace educational professionals
* Display medical information

---

# Target Users

Primary users:

* Parents
* Tutors
* Educational professionals

Secondary users:

* Researchers
* Project evaluators

The application is NOT designed to be directly operated by children.

---

# Design Principles

## Clarity First

Every screen must prioritize readability and understanding over visual complexity.

## Accessibility First

The interface must remain usable by users with different visual and motor abilities.

## Predictability

Navigation and interactions must remain consistent throughout the application.

## Low Cognitive Load

Users should never need to memorize workflows or hidden actions.

## Professional Educational Environment

The visual identity should feel educational, trustworthy, and modern.

Avoid:

* Gamification
* Childish illustrations
* Excessive animations
* Visual clutter

---

# Brand Personality

The product should be perceived as:

* Empathetic
* Professional
* Reliable
* Educational
* Supportive

---

# Color System

## Primary Colors

```yaml
Primary: "#2563EB"
OnPrimary: "#FFFFFF"

Secondary: "#F97316"
OnSecondary: "#FFFFFF"

Surface: "#F8F9FF"
OnSurface: "#121C2A"

Background: "#FFFFFF"
OnBackground: "#121C2A"

Error: "#BA1A1A"
OnError: "#FFFFFF"
```

---

## Semantic Colors

### Success

```yaml
Success: "#16A34A"
OnSuccess: "#FFFFFF"
```

### Warning

```yaml
Warning: "#F97316"
OnWarning: "#FFFFFF"
```

### Information

```yaml
Info: "#60A5FA"
OnInfo: "#FFFFFF"
```

### Error

```yaml
Error: "#BA1A1A"
OnError: "#FFFFFF"
```

---

# Typography

## Font Family

Primary font:

```text
Atkinson Hyperlegible Next
```

This font is mandatory across the entire application.

---

## Type Scale

### Headline Large

```yaml
Size: 32sp
Weight: Bold
LineHeight: 40sp
```

### Headline Medium

```yaml
Size: 24sp
Weight: SemiBold
LineHeight: 32sp
```

### Body Large

```yaml
Size: 18sp
Weight: Regular
LineHeight: 28sp
```

### Body Medium

```yaml
Size: 16sp
Weight: Regular
LineHeight: 24sp
```

### Label Large

```yaml
Size: 14sp
Weight: SemiBold
LineHeight: 20sp
```

### Button

```yaml
Size: 18sp
Weight: SemiBold
LineHeight: 24sp
```

---

# Spacing System

Base Unit:

```yaml
4dp
```

Scale:

```yaml
4dp
8dp
12dp
16dp
24dp
32dp
40dp
48dp
64dp
```

---

## Touch Targets

Minimum interactive size:

```yaml
48dp x 48dp
```

Mandatory for:

* Buttons
* Cards
* Inputs
* Navigation Items
* Icons

---

# Shape System

## Small

```yaml
4dp
```

## Medium

```yaml
8dp
```

## Large

```yaml
16dp
```

## Pill

```yaml
9999dp
```

Usage:

| Component | Radius |
| --------- | ------ |
| Input     | 8dp    |
| Button    | 8dp    |
| Card      | 16dp   |
| Chip      | Pill   |

---

# Elevation

Use Material 3 elevation levels.

## Card

```yaml
Elevation: 2dp
```

## Elevated Card

```yaml
Elevation: 4dp
```

## Dialog

```yaml
Elevation: 6dp
```

Avoid excessive shadow usage.

---

# Navigation Structure

```text
Splash
 ↓
Authentication

Authentication
 ├── Login
 └── Register

Home
 ├── Children
 ├── Exercises
 ├── Sessions
 └── History
```

---

# Screen Inventory

## Authentication

* SplashScreen
* LoginScreen
* RegisterScreen

---

## Children

* ChildrenListScreen
* ChildCreateScreen
* ChildEditScreen
* ChildDetailScreen

---

## Exercises

* ExerciseListScreen
* ExerciseDetailScreen

---

## Sessions

* SessionCreateScreen
* SessionActiveScreen
* SessionResultScreen

---

## History

* SessionHistoryScreen
* SessionHistoryDetailScreen

---

# Component Guidelines

## Primary Button

Purpose:

Main user action.

Properties:

* Filled
* Primary color
* White text
* Minimum height 48dp

---

## Secondary Button

Purpose:

Alternative actions.

Properties:

* Outlined
* Primary color border
* Transparent background

---

## Cards

Purpose:

Present grouped information.

Properties:

* White background
* 16dp padding
* 16dp radius
* Elevation 2dp

---

## Text Fields

Requirements:

* Persistent label
* Helper text support
* Error state support

Never rely only on placeholders.

---

# Session Feedback Components

The backend can send real-time feedback messages through WebSocket.

Feedback levels:

## Warning

Appearance:

* Orange background
* Warning icon

Duration:

* 3 seconds

---

## Information

Appearance:

* Blue background
* Info icon

Duration:

* 3 seconds

---

## Error

Appearance:

* Red background
* Error icon

Duration:

* Manual dismissal

---

# Loading States

Every screen must provide a loading state.

Allowed components:

* CircularProgressIndicator
* Skeleton Loading

Never display blank screens.

---

# Empty States

## Children

Message:

"No children registered yet."

Action:

"Add Child"

---

## Exercises

Message:

"No exercises available."

---

## History

Message:

"No sessions available."

---

# Error States

## Network Error

Message:

"Unable to connect to the server."

Action:

"Try Again"

---

## Authentication Error

Message:

"Invalid email or password."

---

## Generic Error

Message:

"Something went wrong."

Action:

"Retry"

---

# Accessibility

## Text

Minimum size:

```yaml
14sp
```

Preferred size:

```yaml
16sp+
```

---

## Contrast

All text must meet:

```text
WCAG AA
```

---

## Touch Targets

Minimum:

```yaml
48dp
```

---

## Screen Readers

Support:

* TalkBack
* Android Accessibility Services

---

## Dynamic Font Scaling

Must support Android font scaling settings.

---

# Responsive Design

Primary target:

* Android phones

Secondary target:

* Android tablets

Supported:

* Portrait
* Landscape

Minimum supported width:

```yaml
360dp
```

---

# Motion

Animations should be subtle.

Allowed:

* Fade
* Slide
* Scale

Duration:

```yaml
150ms - 300ms
```

Avoid:

* Bouncing
* Flashing
* Continuous animations

---

# AI Implementation Constraints

When generating UI:

* Use Material 3 components only.
* Follow this document strictly.
* Do not invent colors.
* Do not invent typography scales.
* Do not invent screens.
* Do not invent navigation flows.
* Use the screen inventory defined above.
* Respect accessibility requirements.
* Respect spacing tokens.
* Respect semantic colors.

This document overrides any design assumptions made by AI coding assistants.

