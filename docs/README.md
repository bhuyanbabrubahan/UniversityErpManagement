# University ERP Production Blueprint

This repository is the cleaned continuation of the existing University ERP / Student Management System.

## Source-of-truth rules

- Existing MySQL data is preserved; there is no Flyway/Liquibase migration chain in this redesign.
- The old one-time student/admission legacy repair controllers, services and DTOs were removed from the runtime application.
- The old default-admin `CommandLineRunner` was removed so application startup does not create or mutate business data.
- Email verification and password flows remain on the existing Mailtrap-backed implementation.
- Mobile OTP remains on the existing mock SMS implementation; a provider can be added behind `SmsService` later.
- Fee payment options remain `CASH`, `ONLINE`, `UPI`, and `CHEQUE`; the frontend keeps the payment screen extensible for a future real gateway.

## Runtime stack

- Backend: Java 17, Spring Boot 3.3.x, Spring Data JPA, Hibernate, Spring Security, JWT, MapStruct, Lombok, OpenPDF, Apache POI.
- Frontend: Angular 21 standalone architecture.
- Database: MySQL.
- Cache: Spring Cache with Redis profile support.
- Production: Docker, Docker Compose, Nginx, Actuator.
- CI/CD: GitHub Actions.

## Run locally

### Backend

1. Keep the existing database `student_management_system` available.
2. Set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` only if the defaults do not match the current environment.
3. Set `MAIL_USERNAME`, `MAIL_PASSWORD`, and `MAIL_FROM` when Mailtrap is required.
4. Start the Spring Boot application on port `8089`.
5. Open Swagger at `/swagger-ui.html`.

### Frontend

```bash
cd frontend
npm install
npm start
```

Development API target: `http://localhost:8089/api`.

## Docker

```bash
cd frontend
npm install
npm run build:prod
cd ..
docker compose up -d --build
```

The Docker Compose MySQL volume is intentionally empty. Do not expect demo data to be generated automatically. For the real environment, point the backend at the existing database or restore an approved backup before enabling production traffic.

## Major ERP modules

### Authentication

Login, refresh token, logout, forgot password, forgot-password OTP, reset password, email verification and mobile verification.

### Student

Dashboard, profile, academics, attendance, fees, examinations, documents, certificates, notifications, requests, support and settings.

### Faculty

Dashboard, profile, academics, students, attendance, examinations, assignments, announcements and settings.

### Administration

Admissions, students, faculty, staff, departments, academics, attendance, examinations, finance, library, hostel, placements, documents, certificates, reports, users, roles/permissions, audit logs and settings.

### Super Admin

Dashboard, users, roles/permissions, system settings and audit logs.

### Finance

Fee structure, payment, receipt, refund, scholarship, installment, ledger, daily collection, pending dues, payment status and dashboard/reporting.

## Finance dashboard blueprint

1. Collection: total, today, monthly, pending dues.
2. Payments: total, paid, partial, pending, cancelled.
3. Payment modes: cash, online, UPI, cheque.
4. Scholarship: amount and student count.
5. Receipts: total, today, monthly, generated today, printed/download activity today.
6. Refund: amount, today/monthly counts, completed, pending, rejected, cancelled.
7. Installments: total, paid, pending, overdue.
8. Student fee summary: students with fee records, paid, partial and pending.
9. Charts: monthly collection, payment-mode, department-wise and course-wise collection.
10. Recent activity: payments, receipts, refunds and scholarships.
11. Alerts: pending dues, overdue installments, pending refund approvals and payment exceptions.
12. Highest due students: top ten operational follow-up list.

## Report export

The backend provides a reusable export endpoint for major finance datasets. Supported report types include:

- PAYMENTS
- PENDING_DUE
- RECEIPTS
- REFUNDS
- INSTALLMENTS
- SCHOLARSHIPS

Supported formats: `xlsx` and `pdf`.

## Production engineering

- Dockerfile for the backend.
- Docker Compose for MySQL, Redis, backend and Nginx.
- Nginx SPA fallback and `/api/` reverse proxy.
- Environment-variable based database, JWT and mail configuration.
- Actuator health, info, metrics and loggers endpoints.
- Redis profile available through `application-docker.properties`.
- GitHub Actions CI for backend and frontend.
- Manual production deployment workflow with SSH release packaging.

## Data safety

No automatic business-data seed is executed on application startup. The application must be treated as a stateless service over the existing database. Database backups remain an infrastructure responsibility.
