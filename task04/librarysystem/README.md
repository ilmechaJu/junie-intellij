# Library Management System (Task04)

This is a minimal implementation following `./task04/guidelines.md` to scaffold a Library Management System using the MVC pattern with JDBC and an in-memory H2 database for local run and tests. It includes:

- Gradle build with dependencies (H2, MySQL driver, Lombok, JUnit 5)
- Package structure: model, dao, service, controller, view, util
- Schema file (schema.sql)
- Basic CRUD via JDBC for Books and Members
- Simple Swing UI to add/list Books and Members
- JUnit tests for DAO and Service

## Requirements
- JDK 11+
- Gradle (wrapper not included; use your local Gradle or import into IntelliJ)

## How to Run
Option A) From repository root (recommended, uses root Gradle wrapper):
- Windows (PowerShell/CMD):
  1) `cd C:\Users\hjoong\junie-test2\junie-projects`
  2) `.\gradlew.bat -p task04\librarysystem run`
- macOS/Linux:
  1) `cd /path/to/junie-projects`
  2) `./gradlew -p task04/librarysystem run`

Option B) From IntelliJ IDEA:
- Open folder `task04/librarysystem` as a project.
- Ensure Lombok plugin is enabled and annotation processing is on.
- Run configuration: Main class `com.example.librarysystem.MainApp`.

What happens:
- The app starts an in-memory H2 DB (no external DB needed) and opens a Swing UI.

## How to Test
Option A) From repository root (using wrapper):
- Windows: `.\gradlew.bat -p task04\librarysystem test`
- macOS/Linux: `./gradlew -p task04/librarysystem test`

Option B) From IntelliJ:
- Right-click the `test` directory and choose Run Tests.

## Structure
```
com.example.librarysystem/
├── model/             # Book, Member, Loan, Reservation, Category
├── dao/               # DAO interfaces (+ JDBC impl under dao.impl)
├── service/           # Service interfaces/impl
├── controller/        # Controllers
├── view/              # Swing UI (MainFrame)
├── util/              # DBUtil (schema loader, connection)
└── MainApp.java       # Entry point
```

## Database
- Default is H2 in-memory DB for simplicity.
- Schema is in `src/main/resources/schema.sql`.
- For MySQL, adjust JDBC URL and credentials in a future config (not required for this task).

## Notes
- Only core flows (Books, Members) are wired end-to-end to keep the example concise.
- Extend DAOs/Services/Controllers for Loans and Reservations as needed.
