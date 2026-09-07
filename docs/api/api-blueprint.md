# API Blueprint

## Authentication

- `POST /api/auth/login`
- `POST /api/auth/refresh-token`
- `POST /api/auth/logout`
- `POST /api/auth/register`
- `POST /api/auth/forgot-password`
- `POST /api/auth/verify-forgot-password-otp`
- `POST /api/auth/reset-password`
- `GET /api/auth/verify-email`
- `POST /api/auth/mobile/send`
- `POST /api/auth/mobile/verify`

## Academic

- `/api/students`
- `/api/faculties`
- `/api/departments`
- `/api/courses`
- `/api/semesters`
- `/api/subjects`
- `/api/faculty-subjects`
- `/api/admissions`
- `/api/attendances`
- `/api/attendance-sessions`
- `/api/exams`
- `/api/results`
- `/api/student-documents`

## Finance

- `/api/v1/fee-structures`
- `/api/v1/student-fee-payments`
- `/api/v1/fee-receipts`
- `/api/v1/fee-refunds`
- `/api/v1/fee-installments`
- `/api/v1/scholarships`
- `/api/v1/fee-dashboard`
- `/api/v1/fee-reports/**`
- `/api/v1/fee-reports/export?report=PAYMENTS&format=xlsx`

## Notification

- `POST /api/v1/notifications/send`

The notification endpoint is an internal admin/staff abstraction. Email uses the existing JavaMail/Mailtrap configuration. SMS calls the existing `SmsService`, currently backed by the mock provider.

## API documentation

Swagger/OpenAPI remains available at `/swagger-ui.html` and `/v3/api-docs`.
