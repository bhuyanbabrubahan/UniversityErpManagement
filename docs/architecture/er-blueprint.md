# Database ER Blueprint

Existing core tables are retained as the source of truth.

```text
users
  |
  +---- students ---- departments
  |        |             |
  |        +---- courses +---- subjects
  |        +---- semesters
  |        +---- addresses ---- village ---- tehsil ---- district ---- state ---- country
  |        +---- student_documents
  |        +---- attendance
  |        +---- results ---- exams
  |
  +---- faculties ---- faculty_subject_mapping ---- subjects
  |
  +---- admins
  +---- super_admins

students/admissions
  |
  +---- student_fee_payments
          |
          +---- student_fee_receipts
          +---- student_fee_refunds
          +---- student_fee_installments
          +---- student_scholarships

all important operations ---- audit_logs

users ---- refresh_tokens
users ---- password_reset_tokens
users ---- otp_verifications
users ---- email_verification_tokens
```

## Finance relationship rules

- A payment belongs to a student and admission.
- A receipt is generated from a payment and is unique per payment.
- Refunds reference the payment/receipt and carry their own lifecycle.
- Installments reference the payment and carry due/paid/remaining snapshots.
- Scholarships are approved independently and their approved amount is captured on the payment snapshot.

No migration files are included in this redesign. Use an approved database backup/change-control process for schema changes in production.
