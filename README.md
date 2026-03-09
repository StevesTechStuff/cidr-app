# CIDR Pro – Fast Subnet Calculator

CIDR Pro is a clean, offline Android application built with Kotlin and Jetpack Compose. It's designed for network engineers, students, and IT professionals who need instant, accurate subnet calculations without ads, analytics, or unnecessary permissions.

## Features

*   **Calculator Tab:** Instantly calculate network address, broadcast address, first/last usable IP, and total host count from any valid IP and CIDR notation (e.g., `192.168.1.1/24`).
*   **Subnet Split Tab:** Divide a given network into smaller subnets based on required host counts or a specific prefix length.
*   **Cheat Sheet Tab:** A quick reference guide for common CIDR block sizes, identifying the number of addresses and typical use cases.
*   **Offline First:** All calculations are performed instantly using an optimized pure Kotlin custom utility. No internet connection required.
*   **Privacy Focused:** Zero ads, zero analytics, zero trackers. No internet permission is requested.
*   **Persisted State:** The last calculation entered in the Calculator tab is saved automatically and restored upon app restart.
*   **Modern UI:** Built fully with Jetpack Compose Material 3, featuring a minimalist dark theme by default.

## Architecture

*   **100% Kotlin:** Core logic and UI are written entirely in Kotlin.
*   **Jetpack Compose:** Declarative UI toolkit for native Android development.
*   **ViewModel & StateFlow:** Robust state management for a reactive UI architecture.
*   **DataStore Preferences:** Used for persisting the last entered CIDR input.
*   **Single-Activity Architecture:** A modern, streamlined approach utilizing Compose navigation.

## Building and Running

1.  Clone the repository:
    ```bash
    git clone https://github.com/YOUR-USERNAME/cidr-app.git
    ```
2.  Open the project in **Android Studio**.
3.  Let Gradle sync the project dependencies.
4.  Build and run the project on an Android emulator or physical device.

## Screenshots

*(Add screenshots of the Calculator, Subnet Split, and Cheat Sheet tabs here after uploading them to the repository)*

## License

This project is intended for personal use and portfolio demonstration. All rights reserved.
