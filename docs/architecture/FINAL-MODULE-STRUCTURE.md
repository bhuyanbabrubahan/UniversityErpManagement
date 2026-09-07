# Final ERP Module Structure

## Backend

Each completion ERP module uses the same package boundary:

```text
module/
├── entity/
├── dto/
├── repository/
├── mapper/
├── specification/
├── service/
├── serviceImpl/
└── controller/
```

### Responsibilities

- `entity`: JPA persistence model only.
- `dto`: request, response and search API contracts.
- `repository`: Spring Data persistence and specification execution.
- `mapper`: MapStruct entity/API conversion.
- `specification`: composable dynamic search predicates.
- `service`: business-facing contract; controllers do not access repositories.
- `serviceImpl`: transaction/business orchestration and persistence calls.
- `controller`: HTTP contract, validation boundary and role authorization.

## Frontend

Each completion feature uses:

```text
module/
├── components/
│   ├── module-form/
│   ├── module-table/
│   └── module-search-filter/
├── models/
├── pages/
│   ├── list/
│   ├── add/
│   ├── edit/
│   └── view/
├── services/
├── shared/
└── module.routes.ts
```

All pages have separate HTML/TypeScript/SCSS files. Shared visual conventions stay in shared components and feature-level utilities/constants.
