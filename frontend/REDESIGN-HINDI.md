# UMS Angular 19 — Complete ERP Redesign Notes

## Version fixed

Frontend ko Angular 19.2.15 par hi rakha gaya hai.

- Angular CLI: 19.2.15
- Node: 22.22.2
- npm: 10.9.7

Angular 21 upgrade nahi kiya gaya.

## Main redesign

Existing business modules ko delete/recreate nahi kiya gaya. Unka API/service/business logic preserve karke common visual system, shell aur role navigation redesign ki gayi hai.

### Reusable design

- Global ERP design tokens
- Light theme
- Dark Classic theme
- Small / Normal / Large font scale
- Responsive mobile/tablet/desktop layout
- Common card, table, form, filter and header language
- Tables overflow horizontally only when required
- Main application content has controlled vertical scrolling
- Reusable navigation filtering by role

## Role-wise navigation

### SUPER_ADMIN
Management + system governance + audit + finance + academic operations.

### ADMIN
Management + reports + audit + attendance reporting (read/management context), but attendance marking menu nahi hai.

### FACULTY
Students, academics, attendance marking/history/reports, examinations/results.

### STUDENT
Own academics, own attendance, own results, own fee/payment/profile/notifications/settings.

### ACCOUNTANT
Finance dashboard, payments, finance reports and related finance operations.

### STAFF
Only role-safe generic workspace/system items are exposed until a dedicated staff backend domain is defined. Fake CRUD intentionally nahi banaya gaya.

## Attendance

Frontend me existing attendance module preserve kiya gaya hai. New session detail screen:

`/dashboard/attendance/view?sessionId=<id>`

shows:

- Session ID
- Date
- Started / completed time
- Faculty
- Subject
- Semester
- Session status
- Total / Present / Absent / Leave / Half Day
- Student ID
- Roll number
- Student name
- Status
- Marked time
- Source
- Remarks

Per-session PDF/Excel ke liye backend optional patch diya gaya hai.

## Examination

New Angular workspace added:

`/dashboard/examination`

It contains:

- Exam schedule
- Exam metadata
- Result entry for non-student roles
- Result listing
- Server-side percentage/grade/status calculation remains backend responsibility

## Finance

New finance dashboard uses the existing backend endpoint:

`GET /api/v1/fee-dashboard`

It displays existing backend fields for:

- Total fee collection
- Today collection
- Monthly collection
- Pending due
- Collection percentage
- Average collection
- Payment status
- Payment modes
- Scholarship
- Refund summary
- Installments
- Student fee summary
- Highest due students
- Recent activity
- Alerts already exposed by backend

## Reports

Central report catalogue added. Existing backend report APIs and fee export endpoint remain the source of truth.

Existing backend already supports PDF/Excel for these fee export types:

- PAYMENTS
- PENDING_DUE
- RECEIPTS
- REFUNDS
- INSTALLMENTS
- SCHOLARSHIPS

The backend patch does not pretend unsupported export types are already implemented.

## Audit

Existing backend audit API is used:

`GET /api/audit-logs`

Frontend audit page provides a dedicated governance workspace.

## Notifications

Backend currently exposes send-only notification API. The frontend notification center therefore derives role-aware live workspace events from available dashboard/session data instead of inventing a persistent notification database.

A future persistent notification inbox needs a Notification entity/repository/API in the backend.

## Settings

New settings page:

- Light
- Dark Classic
- Small font
- Normal font
- Large font

Preferences are saved in browser localStorage.

## Global search

Top navbar search filters the role-aware navigation catalogue and navigates directly to matching modules.

## Important backend patch

The current backend `GET /api/attendance-sessions/{id}` checks that the session belongs to the current faculty. That is correct for faculty ownership but wrong for an ADMIN management view.

The optional patch adds:

`GET /api/attendance-sessions/{id}/details`

Rules:

- ADMIN / SUPER_ADMIN: read any active session
- FACULTY: only own assigned session
- STUDENT: denied

It also adds session-specific PDF/Excel export.

## Database safety

No DB migration, seed, reset, drop, truncate or data deletion is introduced by the frontend.
