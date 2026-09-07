export const NOTIFICATION_STATUSES = [
  'DRAFT',
  'SCHEDULED',
  'PUBLISHED',
  'ARCHIVED',
] as const;

export const NOTIFICATION_CHANNELS = [
  'EMAIL',
  'SMS',
  'PUSH',
  'IN_APP',
] as const;

export const NOTIFICATION_TARGET_ROLES = [
  'ALL',
  'SUPER_ADMIN',
  'ADMIN',
  'REGISTRAR',
  'ACCOUNTANT',
  'STAFF',
  'FACULTY',
  'STUDENT',
] as const;

export const NOTIFICATION_PAGE_SIZES = [10, 20, 50, 100];
