# Production Runbook

## Health

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`

## Logging

Use structured application logs and centralize them at the infrastructure layer. Never log passwords, JWTs, OTPs or sensitive identity numbers.

## Monitoring

Minimum production dashboards:

- JVM heap/non-heap usage
- CPU and process uptime
- HTTP request rate/latency/error rate
- Database connection-pool active/idle/pending
- Hibernate query latency and slow query count
- Redis hit/miss and memory usage
- Disk usage for uploads/logs
- 4xx/5xx rate
- Authentication failure rate

## Backup strategy

1. Daily RDS/MySQL automated backup.
2. Weekly independent snapshot.
3. Monthly restore test.
4. Keep backup retention according to institutional policy.
5. Never test destructive recovery on the production database.

## Rollback

- Keep immutable release artifacts.
- Deploy by versioned release directory.
- Keep the previous working artifact available.
- Roll back application first for application-only regressions.
- Database changes require a separately approved backward-compatible plan.
