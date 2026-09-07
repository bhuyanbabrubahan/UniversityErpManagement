# Interview & Portfolio Blueprint

## Resume bullets

- Designed a role-based University ERP using Java 17, Spring Boot, Spring Security JWT and MySQL.
- Implemented student, faculty, academics, attendance, examination, result and finance modules.
- Built fee payment, receipt, refund, scholarship and installment workflows with operational dashboards.
- Applied Hibernate LAZY loading, EntityGraph, pagination, dynamic search and soft-delete patterns.
- Added PDF/Excel report exports, Docker, Nginx, Actuator, Redis-ready caching and GitHub Actions.

## Architecture questions

1. Why JWT access + refresh tokens?
2. How do you revoke a JWT?
3. What causes Hibernate N+1?
4. When do you use EntityGraph vs fetch join?
5. Why should list APIs avoid EAGER relationships?
6. How do database indexes change query plans?
7. What belongs inside a transaction?
8. How do you design an idempotent payment callback?
9. How would you migrate this monolith to microservices?
10. How do you monitor a production Spring Boot service?

## Microservices migration path

Do not split everything immediately. First establish stable bounded contexts:

1. Identity & Access
2. Student/Academic
3. Attendance/Examination
4. Finance
5. Notification
6. Reporting

Introduce events through Kafka only after the modular monolith boundaries are stable. Finance and identity should be split last only after data ownership and transaction boundaries are explicit.
