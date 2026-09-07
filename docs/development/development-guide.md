# Development Guide

## Rules before changing code

1. Inspect the existing entity, repository, service and mapper first.
2. Reuse existing DTOs/services when the responsibility already exists.
3. Do not introduce a second implementation of the same business rule.
4. Keep relationships LAZY and solve fetch requirements at the query boundary.
5. Keep business operations transactional.
6. Prefer soft-delete conventions already used by the project.
7. Add a focused test for a new business rule.

## Backend testing

Use JUnit 5 + Mockito for service/unit tests and Spring Boot integration tests for API/security/persistence flows.

## Frontend testing

Keep route guards, auth services and data services independently testable. Feature pages should remain thin and reuse shared UI primitives.

## Report exports

Do not place report generation inside controllers. Controllers delegate to `FeeExportService`; new report types should add a new row strategy without duplicating response plumbing.
