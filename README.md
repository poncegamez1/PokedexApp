# 🐉 PokedexApp

A modern, high-performance Android application built with **Jetpack Compose** that allows users to browse and explore the world of Pokémon. This project is a demonstration of **Clean Architecture** principles, reactive programming, and modern Android development standards.

---

## ✨ Key Features
- **Infinite Scrolling:** Seamlessly browse the entire Pokémon collection using **Paging 3**.
- **Real-time Search:** Filter Pokémon by name with a dynamic search bar that reacts instantly to user input.
- **Detailed Statistics:** View high-quality sprites, types, abilities, and custom-designed base stat bars.
- **Modern UI/UX:** 100% Jetpack Compose with smooth slide and fade transitions between screens, following Material 3 guidelines.
- **Reliable Networking:** Robust data fetching with Retrofit, OkHttp logging, and comprehensive error handling with retry logic.
- **Reactive State Management:** One-way data flow (UDF) managed via `StateFlow` and ViewModels.

---

## 🚀 API Choice: Why PokeAPI?
For this project, I chose the **[PokeAPI](https://pokeapi.co/)**.

**Why?**
*   **Completeness:** It provides comprehensive data, from basic types to complex move sets and evolution chains.
*   **RESTful Design:** The API's hierarchical structure is ideal for demonstrating data mapping and clean repository patterns.
*   **Developer Friendly:** It is well-documented and has a consistent structure, making it perfect for demonstrating how to handle real-world data at scale.
*   **Open Source:** Being a free, community-driven resource, it aligns with the spirit of open-source development.

---

## 🏗 Project Structure & Clean Architecture
The project is strictly organized into three layers to ensure a **Separation of Concerns**. This makes the codebase easier to maintain, scale, and test.

### 🧩 Architecture Patterns
- **Clean Architecture:** Ensures business logic is independent of UI and Frameworks.
- **MVVM (Model-ViewModel-ViewModel):** Decouples UI from business logic using reactive streams.
- **Repository Pattern:** Centralizes data access and provides a clean API to the rest of the app.

### 📂 Directory Walkthrough
```text
com.example.pokedexapp/
├── data/           # Infrastructure & Data Procurement
│   ├── mapper/     # DTO to Domain transformations (ensures UI doesn't see raw JSON)
│   ├── paging/     # PagingSource implementation for infinite scrolling
│   ├── remote/     # Retrofit Services and Data Transfer Objects (DTOs)
│   └── repository/ # Concrete implementations of Domain Repository interfaces
├── domain/         # The Business Heart (Pure Kotlin)
│   ├── models/     # Domain entities used by the UI (e.g., PokemonDetail)
│   └── PokemonRepository.kt # Repository Interface (The Contract)
├── ui/             # The Presentation Layer
│   ├── screens/    # ViewModels, UI State definitions, and Composable Screens
│   ├── components/ # Reusable UI atoms (Stat bars, Type chips, Loaders)
│   └── theme/      # Centralized design system (Colors, Typography, Shapes)
├── di/             # Koin Dependency Injection modules
└── utils/          # Extensions, Constants, and Navigation Routes
```

---

## 🛠 Tech Stack
*   **UI:** Jetpack Compose (Declarative UI)
*   **DI:** Koin (Lightweight and Kotlin-idiomatic dependency injection)
*   **Networking:** Retrofit & OkHttp (with Logging Interceptor)
*   **Pagination:** Paging 3 (For efficient memory and data handling)
*   **Image Loading:** Coil (Memory-efficient image loading for Compose)
*   **Concurrency:** Kotlin Coroutines & Flow (For asynchronous data streams)
*   **Testing:** MockK, JUnit 4, and Coroutine Test utilities

---

## 🧪 Testing & Quality Assurance
The project includes a robust suite of **Unit Tests** for the presentation layer, specifically targeting the logic within ViewModels to ensure a reliable user experience.

### 🔍 Featured Tests: `PokemonDetailsScreenViewModelTest`
This test suite covers critical scenarios to ensure the state machine behaves correctly:
- **Initial State:** Verifies the screen starts in a `Loading` state.
- **Data Success:** Ensures that successful repository calls transition the UI to `Success` with the correct Pokémon data.
- **Error Handling:** Validates that network failures or exceptions correctly transition the UI to an `Error` state with a descriptive message.
- **State Resilience:** Tests that the UI resets to `Loading` when a new Pokémon is requested, even if a previous one was already loaded.
- **Edge Cases:** Handles empty names, special characters, and rapid-fire API calls to ensure no crashes occur.

**Tools used:**
- **MockK:** For mocking the Repository layer and controlling API responses.
- **MainDispatcherRule:** A custom JUnit rule to handle Coroutine dispatchers in a testing environment.
- **runTest & advanceUntilIdle:** For deterministic testing of asynchronous flows.

---

## 🏗 Development Roadmap: Steps to Build
1.  **Project Scaffolding:** Initialized with Version Catalogs and Koin for DI.
2.  **Domain Blueprint:** Defined `PokemonDetail` models and the `Repository` interface first (Domain-First approach).
3.  **Data Implementation:** Built Retrofit services and Mappers to ensure the UI works with clean domain objects.
4.  **Pagination Engine:** Implemented `PagingSource` for seamless infinite scrolling.
5.  **Reactive ViewModels:** Created ViewModels that expose a lifecycle-aware `UiState`.
6.  **Compose Assembly:** Built reusable UI atoms and assembled them into screens.
7.  **Navigation Flow:** Configured `NavHost` with premium animations.
8.  **Validation:** Wrote comprehensive unit tests to ensure stability.

---

## 📝 Technical Decisions
*   **Single-Module:** Chosen for speed and simplicity at this scale, while maintaining a "module-ready" package structure.
*   **Git Strategy:** Worked on a **single branch** (`main`) for a streamlined individual workflow.
*   **Paging over Local Storage:** Focused on real-time API performance using Paging 3 to manage memory and data streaming.

---

## ⚙️ How to Run
1.  **Clone:** `git clone https://github.com/your-username/PokedexApp.git`
2.  **Open:** Android Studio (Ladybug or newer).
3.  **Sync:** Let Gradle download dependencies.
4.  **Run:** Select a device (API 24+) and hit **Run**.
