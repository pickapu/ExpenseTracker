
***

# 📱 Smart Daily Expense Tracker

## Overview

Smart Daily Expense Tracker is a modern Android app built with **Kotlin** and **Jetpack Compose** to help you record, review, and visualize your daily expenses. The app is designed with a clean MVVM architecture, uses **Room** for local storage, and supports **Hilt** for dependency injection. It offers thematic customization, powerful reporting, and a user-friendly, animated Compose UI.

***

## ✨ Features

- **Add Expenses:** Quick entry with category, amount, notes, receipt toggle \& validation.
- **Expense List:** Filter, group by date/category, search, and view totals with smooth item animations.
- **Reports:** Visualize your habits with pie/bar charts, summary stats, and export simulation.
- **Settings:** Toggle between light/dark themes, access info, and placeholders for backup/restore.
- **UX:** Material 3 styling, animated feedback for actions, error/success toasts.

***

## 🛠️ Tech \& Architecture

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Persistence:** Room Database
- **Architecture:** MVVM + StateFlow/Coroutines
- **Dependency Injection:** Hilt
- **Preferences:** DataStore (theme)
- **Navigation:** Navigation Compose
- **Custom Charts:** Compose Canvas for bar/pie
- **Animations:** AnimatedVisibility, slideIn/fadeIn, etc.

***

## 📂 Project Structure

```
├── data/           # Room entities, DAO, database, repository implementation
├── domain/         # Models, repository interface, use cases
├── di/             # Hilt modules
├── presentation/
│   ├── ui/         # Screens, components, navigation, theming
│   └── viewmodel/  # All ViewModels
├── ExpenseTrackerApplication.kt
├── MainActivity.kt
└── README.md
```


***

## 🚀 Getting Started

1. **Clone/Download** this repository or copy the code into an Android Studio project.
2. Make sure your `build.gradle.kts` matches the included one and has all dependencies.
3. Rename/com.picka.expenseTracker` package as needed.
4. Sync and **Run on an emulator/device** (Android 9+, SDK 28+).

***

## 🤖 AI Usage Summary

AI was actively utilized to:

- Scaffold the MVVM + DI architecture and generate base code for Room, Repository, and ViewModels.
- Automatically design Jetpack Compose UIs with Material 3, navigation logic, and custom empty/report state components.
- Generate robust form validation, search/filter logic, chart rendering, and animated interactions.
- Review, refactor, and optimize code structure for best practices and modularity.
- Prepare this README and ensure all major assignment specs are documented and fulfilled.

***

## 📝 AI Prompt Logs (Sample)

Here are a few example prompts used during development (summarized for brevity):

- `"Generate a Kotlin MVVM architecture for a daily expense tracker with Jetpack Compose screens and Room Repository"`
- `"Implement a Compose ExpenseEntryScreen with input validation, category dropdown, and animated success toast"`
- `"Provide a Room DAO interface for expense retrieval by date, category and range with Flow"`
- `"Refactor my add expense button so the label is always centered with loading spinner"`
- `"Give me a full README for my smart expense tracker with technical and AI usage summary"`

Some prompt/response iterations were repeated for code polish and bug resolution, especially for Compose UI alignment and gradle configuration.

***

## 📄 License

This project is for educational/demo purposes.
For custom licensing, please edit this section.

***

Feel free to further tailor this README with your name, screenshots, or repository URL before distribution!
Let me know if you'd like any part expanded or additional guidance for final hand-in.

