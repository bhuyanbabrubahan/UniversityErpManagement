# AWS Deployment Blueprint

## Target architecture

```text
Route 53
   |
ACM SSL
   |
ALB / Nginx
   |
EC2 / ECS application
   |---- RDS MySQL
   |---- ElastiCache Redis
   |---- S3 student documents/backups
   |
CloudWatch / Prometheus-compatible monitoring
```

## Services

- EC2: application host when using the Docker Compose deployment model.
- RDS: managed MySQL for production database.
- S3: documents, exports and backup artifacts.
- IAM: least-privilege roles for EC2/application and CI/CD.
- ACM: TLS certificates.
- Route 53: domain mapping.
- CloudWatch: logs, metrics and alarms.

## Security groups

Only expose 80/443 publicly. MySQL and Redis should be private and reachable only from the application security group.

## Environment variables

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`
- `MAIL_FROM`

Never commit production secrets.
