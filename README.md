# HiltTestActivity

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/waffiqaziz/HiltTestActivity/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/waffiqaziz/HiltTestActivity/tree/main)
[![codecov](https://codecov.io/gh/waffiqaziz/HiltTestActivity/graph/badge.svg?token=OKBP27L0UB)](https://codecov.io/gh/waffiqaziz/HiltTestActivity)
![GitHub License](https://img.shields.io/github/license/waffiqaziz/HiltTestActivity)

This repository contains a simple Android project demonstrating the use of **Jetpack Compose** for
building UI, **Hilt** for dependency injection, and **CircleCI** for continuous integration and
testing. The project focuses on **instrumented testing (UI testing)** using **Jetpack Compose Test
APIs**.

## 🛠️ Tech Stack

- **Kotlin**: 100% Kotlin codebase.
- **Jetpack Compose**: For declarative UI development.
- **Hilt**: For managing dependencies.
- **Jetpack Compose Test APIs**: For UI and instrumented testing.
- **CircleCI**: For CI/CD pipeline.

---

## 📁 Project Structure

``` bash
├── app/                    # Main application module 
│ ├── src/main/             # Source files for the app 
│ ├── src/androidTest/      # Instrumentation (UI) tests 
├── .circleci/              # CircleCI configuration 
│ └── config.yml            # CircleCI pipeline setup 
└── build.gradle            # Project-level Gradle file
```

## 🖥️ Setup Instructions

1. Clone the repository:

   ```bash
   git clone https://github.com/waffiqaziz/HiltTestActivity.git
   ```

2. Open the project in Android Studio.

3. Sync Gradle dependencies:

   Go to File > Sync Project with Gradle Files.

4. Run the application:

   Select a device/emulator and click on Run ▶️.
   Run UI tests:

5. Execute the instrumented tests with:

   ```bash
   ./gradlew connectedAndroidTest
   ```

## 🔗 CI/CD with CircleCI

This project includes a `.circleci/config.yml` file for automating builds and tests. To use
CircleCI:

1. Push the repository to GitHub or another supported VCS.
2. Set up the project on [CircleCI](https://circleci.com/).
3. Customize the `config.yml` file if needed.
4. Monitor builds and test results on the CircleCI dashboard.

## 🤝 Contributing

Contributions are welcome! Please read this [page](CONTRIBUTING.md).
