# Architecture Blueprint

## High-level flow

```text
Angular 21
   |
   | HTTPS / JSON
   v
Nginx
   |
   v
Spring Boot API
   |---- Spring Security + JWT
   |---- Service layer + DTO + MapStruct
   |---- Spring Data JPA / Hibernate
   |---- Actuator / Metrics
   |---- Notification abstraction
   |
   +---- MySQL
   +---- Redis (optional production cache)
   +---- Mailtrap (email testing)
   +---- Mock SMS (provider abstraction)
```

## Package principles

- `com.sms.security`: authentication, authorization, JWT, user accounts.
- `com.sms`: academic/student domain.
- `com.sms.fee`: finance domain and finance reports.
- `com.sms.scholarship`: scholarship domain.
- `com.sms.audit`: application audit logging.
- `com.location`: reusable geographic master data.
- `com.admission`: admission domain.
- `com.sms.notification`: channel-independent notification entry point.

## Reuse rules

- Controllers only translate HTTP requests/responses.
- Services own business rules and transactions.
- Repositories own persistence queries.
- DTOs cross API boundaries; entities do not become public API contracts.
- MapStruct handles normal entity/DTO mapping.
- Existing `BaseEntity` remains the common audit/timestamp base.
- Relationships stay LAZY by default; detail APIs use `@EntityGraph` or fetch joins when needed.

## Hibernate advanced design

### Entity lifecycle

Transient -> Managed -> Dirty checking -> Flush -> Detached/Removed.

Business services should work inside transaction boundaries and should not expose lazy entities directly to controllers.

### Lazy vs Eager

Use LAZY for `@ManyToOne`, `@OneToMany` and `@OneToOne` relationships unless there is a strong invariant that requires eager loading.

### N+1 prevention

Use `@EntityGraph`, JPQL `join fetch`, projections or batch fetching for known read paths. The existing Student detail repository already demonstrates `@EntityGraph` for department/course/semester/user/address hierarchy.

### Fetch join vs EntityGraph

- Fetch join: best when the query shape itself defines the exact relationship graph.
- EntityGraph: best when the same repository method needs a reusable fetch plan without changing JPQL.

## Database performance

- Index foreign keys and frequently filtered status/date fields.
- Keep pagination at the repository boundary.
- Avoid loading fee/attendance/exam collections on student list screens.
- Use summary queries for dashboards instead of loading all transactions.
- Use `EXPLAIN` before changing a slow query.
- Monitor connection-pool utilization before increasing pool size.

## Caching

Default profile uses Spring's simple cache so local development does not require Redis. Docker profile switches to Redis. Cache only stable reference data and short-lived dashboard aggregates; never cache mutable authorization state without an explicit invalidation strategy.
