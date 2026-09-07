# UniversityErpManagement – ERP Completion Modules

This change adds independent backend feature boundaries and Angular feature boundaries for Staff, HOD, Curriculum, Timetable, Marks, Installments, Scholarship, Library, Hostel, Transport, Placement, Certificates, Notifications, Research and Grievance.

## Backend
Each module follows `Entity -> Repository -> Service -> ServiceImpl -> Controller` under `com.sms.erp.<module>`. Controllers expose CRUD endpoints under `/api/erp/<module>`.

## Frontend
Each module has a dedicated `features/<module>` boundary and a standalone wrapper component. CRUD UI is reusable through `shared/components/erp-crud`.

## Database
`database/UniversityErpManagement-completion-modules.sql` creates only ERP completion tables with `IF NOT EXISTS`; location master tables are not touched.

## Attendance
The existing AttendanceSession implementation remains the source of truth for academic year, semester, course, section, timetable entry, subject, faculty, room, date, start/end time, period, source, marking user, timestamp and student status.

## Reports
Existing report packages remain intact. The completion modules are intentionally separated so report/query implementations can be added without coupling controllers to persistence.
