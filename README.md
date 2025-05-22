English | [中文说明 (Chinese)](README-zh.md)

---

# Android MITM Attack Detection App

**An Application for Detecting Man-In-The-Middle Attacks and Providing Real-Time Warnings on Android**

Author: Mengjing Cao

---

## Overview

This project is an Android application designed to detect and warn users about Man-In-The-Middle (MITM) attacks, especially when using public Wi-Fi networks. The app leverages various detection techniques, such as ping delay analysis, URL error detection, and network packet capture, to identify potential threats and alert users in real time through an intuitive interface.

---

## Motivation

With the widespread adoption of mobile devices and public Wi-Fi, users are increasingly exposed to network security risks, particularly MITM attacks. Most existing security tools are expensive or have limited functionality, making them inaccessible to ordinary users and small businesses. This project aims to provide a low-cost, practical solution to enhance public awareness and the ability to protect against network threats.

---

## Key Features

- **Wi-Fi Information Display**: Shows detailed information about the currently connected Wi-Fi network (IP, MAC, gateway, speed, etc.).
- **Network Packet Capture & Analysis**: Captures and analyzes current network traffic to detect abnormal activities.
- **Ping Delay Detection**: Monitors ping latency to target hosts for signs of communication hijacking or disruption.
- **URL Error Detection**: Uses domain resolution and URL verification to identify suspicious websites and phishing attempts.
- **Whitelist & Blacklist Mechanism**: Built-in trusted and risky host lists for automatic comparison and interception.
- **Real-Time Security Alerts**: Provides instant warnings and guidance when suspicious activity is detected.
- **User-Friendly Interface**: Designed for easy understanding and quick response to security alerts.

---

## Technology Stack

- **Platform**: Android
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Main Modules**:
  - UI Design (Wi-Fi info, packet capture, alerts)
  - Network Traffic Interceptor & Analyzer
  - Ping/URL Detection Algorithms
  - Local Database (for logs and host lists)

---

## Project Structure

```
.
├── .idea/                 # IDE configuration files (can be ignored in version control)
├── app/                   # Main Android application source code (Java/Kotlin, layouts, resources)
├── gradle/wrapper/        # Gradle wrapper files
├── monitor/               # (Optional) Additional modules or monitoring logic
├── .gitignore             # Git ignore rules
├── README.md              # Project documentation (this file)
├── build.gradle.kts       # Project-level Gradle build script (Kotlin DSL)
├── gradle.properties      # Gradle properties configuration
├── gradlew                # Unix Gradle wrapper
├── gradlew.bat            # Windows Gradle wrapper
├── settings.gradle        # Gradle settings (Groovy DSL)
├── settings.gradle.kts    # Gradle settings (Kotlin DSL)
├── upload.gradle          # (Optional) Custom Gradle upload script
```

### Directory Details

- **app/**  
  Contains all main Android application code:
  - `src/` (Java/Kotlin source files, activities, services, etc.)
  - `res/` (UI layouts, images, strings, etc.)
  - `AndroidManifest.xml` (App manifest)

- **monitor/**  
  (If used) For monitoring modules, services, or additional features not in the main app.

- **gradle/wrapper/**  
  Gradle wrapper JAR and properties for consistent builds.

- **.idea/**  
  IDE-specific settings (not essential for building/running the app).

- **build.gradle.kts**, **settings.gradle(.kts)**  
  Project and module build configuration.

---

### How to Use

- Open the project root directory in Android Studio.
- Main development happens in the `app/` folder.
- For multi-module projects, `monitor/` may contain additional features or services.
- Configuration and build scripts are at the root level.

---

## Getting Started

1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Connect your Android device or start an emulator.
4. Build and run the application.

---

## Author Contribution

- Independently completed requirement analysis, system design, implementation, and testing.
- Designed and implemented multi-dimensional MITM detection algorithms.
- Developed and optimized the full user interface.
- Wrote complete project documentation and technical report.

---

## Acknowledgments

Special thanks to supervisor Mehran Abolhasan for guidance and support.

---

## Appendices

- Code Help from GitHub: https://github.com/lygttpod/AndroidMonitor 
- Android theory knowledge help: https://developer.android.com/

---
