# UMS ERP V2 — Production UI & Architecture

## Design direction

This version deliberately does not preserve the original visual language. The UI uses an enterprise operations aesthetic: compact information density, neutral surfaces, indigo/cyan semantic accents, strong hierarchy, responsive navigation and predictable Material interaction patterns.

## Rules

- Feature code owns business behavior; shared code owns reusable UI primitives and contracts.
- API URLs come from environments and API constants; components never hard-code backend hosts.
- Shared visual rules live in `src/styles.scss`; component SCSS is intentionally minimal to avoid cascade conflicts.
- Tables use horizontal scrolling only inside their table region on narrow screens; the page itself never overflows horizontally.
- Forms collapse from four/three/two columns to one column at mobile widths.
- Desktop navigation uses a side rail; mobile uses the same Material sidenav in overlay mode.
- Existing services, models, routes and CRUD bindings are retained; redesign is applied around them rather than replacing business logic.

## Existing implemented features retained

- Authentication flows
- Dashboard
- Student CRUD
- Faculty CRUD
- Profile and profile dialogs
- Department service/models
- Location selectors/services/models
- Shared enums/models/validators
- Auth/refresh/error interceptors

## Next implementation rule

New ERP domains (admissions, finance, examinations, library, hostel, placement, reports, etc.) should only be wired when their API/business contract exists. Do not create fake production CRUD behavior just to fill navigation.
