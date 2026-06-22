# Student Progress Tracker

A native Android application built in Java for tracking student academic progress across courses and assessments. Designed with a custom XML-based UI and a local Room persistence database, the app provides students with an organized view of their terms, courses, and assessment deadlines — all stored and retrieved locally on-device without requiring a network connection.

---

## Features

- **Term Management** — Create and manage academic terms with start and end dates.
- **Course Tracking** — Add courses within a term, assign instructors and contact information, and track course status (In Progress, Completed, Dropped, Plan to Take).
- **Assessment Scheduling** — Attach objective and performance assessments to courses, with start and end dates for each.
- **Notification Alerts** — Set optional notifications for course and assessment start/end dates. Alerts fire on the scheduled date to remind the user of upcoming deadlines.
- **Notes** — Attach free-text notes to individual courses. Notes can be shared to other apps via Android's share intent.
- **Local Persistence** — All data is stored locally using the Room persistence library (SQLite under the hood). No internet connection or account required.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | XML layouts (Android View system) |
| Local Database | SQLite via Room Persistence Library |
| Architecture | Repository pattern with ViewModel |
| Async Operations | Multi-threading (background threads for all database I/O) |
| Build System | Gradle |
| IDE | Android Studio |
| Version Control | Git / GitHub |

---

## Project Structure

```
app/src/main/
├── java/
│   ├── database/         # Room database class, DAOs (Data Access Objects)
│   ├── entities/         # Room entity classes (Term, Course, Assessment)
│   ├── repository/       # Repository classes abstracting database access
│   ├── ui/               # Activity and Adapter classes for each screen
│   └── utilities/        # Notification scheduling helpers
└── res/
    ├── layout/           # XML layout files for all screens
    ├── drawable/         # Icons and background resources
    └── values/           # Strings, colors, dimensions
```

---

## Getting Started

### Prerequisites

- Android Studio (Arctic Fox or newer recommended)
- Android SDK 21+ (Android 5.0 Lollipop minimum)
- A physical Android device or an Android Virtual Device (AVD)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/ggmallow/Student_Progress_Tracker.git
   cd Student_Progress_Tracker
   ```

2. **Open in Android Studio**  
   Select **File → Open** and choose the project folder. Android Studio will automatically sync Gradle dependencies.

3. **Build and Run**  
   Connect a physical device (USB debugging enabled) or start an AVD via the Device Manager, then click **Run** or press `Shift + F10`.

### Running on an Emulator

The app has been validated across 200+ Android device configurations using Android Studio's built-in emulator. Any AVD running API level 21 or higher should work without modification.

---

## Key Engineering Decisions

- **Room over raw SQLite** — The Room persistence library was chosen to leverage compile-time SQL verification, strongly-typed DAOs, and LiveData integration, while still producing a SQLite database under the hood.
- **Background threading** — All database reads and writes run on background threads to keep the main (UI) thread responsive. This was achieved by refactoring the original codebase to use Java `ExecutorService`, which reduced load times by 70%.
- **Repository pattern** — Database access is abstracted behind repository classes so that Activity and Adapter classes never interact with the database directly. This keeps the codebase modular and easier to test or extend.
- **Notification system** — Assessment and course deadline notifications are scheduled using Android's `AlarmManager`, which fires a `BroadcastReceiver` on the target date regardless of whether the app is open.

---

## Compatibility

Validated against 200+ Android device configurations via Android Studio's emulator suite.

| Target SDK | Minimum SDK |
|---|---|
| Android 13 (API 33) | Android 5.0 (API 21) |

---

## Author

**Jacob Dyke**  
[linkedin.com/in/jake-dyke](https://www.linkedin.com/in/jake-dyke) · [github.com/ggmallow](https://github.com/ggmallow)

B.S. Software Development — Western Governors University, 2022
