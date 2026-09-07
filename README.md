# University ERP / Student Management System

Production-oriented modular monolith for a University ERP.

## What was cleaned

- Removed historical student/admission legacy repair, mapping-repair and migration-era runtime tooling.
- Removed automatic default-admin data initialization.
- Removed temporary Java source files and IDE build artifacts from the deliverable.
- Removed `System.out`/debug prints from production services and JWT processing.
- Consolidated production concerns around reusable service/repository patterns.
- Added a clean Angular 21 frontend shell because the supplied archive contained backend source only.

## What was added

- Finance dashboard expansion: refunds, installments, receipts, student fee summary, alerts, recent activity and highest due students.
- PDF/XLSX export for major finance reports.
- Attendance PDF/XLSX export while retaining online and bulk attendance APIs.
- Notification abstraction: email + SMS channel selection; existing Mailtrap and mock SMS behavior remain.
- Dummy payment gateway endpoint; no real money movement.
- Actuator monitoring and production logging.
- Redis-ready cache configuration.
- Dockerfile, Docker Compose, Nginx and environment profiles.
- GitHub Actions CI/CD workflows.
- AWS deployment, architecture, ER, API, operations and interview documentation.
- Angular 21 role-based route structure for authentication, student, faculty, staff, admin and super-admin modules.

## Important data rule

The current MySQL database is the source of truth. No Flyway/Liquibase migration files or automatic business-data seeders are part of this redesign. Existing records are not intentionally deleted or reset by application startup.

## Start

### Backend

```bash
./mvnw spring-boot:run
```

If Maven dependencies cannot be downloaded, configure Maven/network access first.

### Frontend

```bash
cd frontend
npm install
npm start
```

### Production build

```bash
cd frontend
npm install
npm run build:prod
cd ..
./mvnw -DskipTests package
docker compose up -d --build
```

## Documentation

See `docs/README.md` for the complete production blueprint.
