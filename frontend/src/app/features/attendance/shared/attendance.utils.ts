import { AttendanceStatus } from '../../../shared/enums/attendance-status.enum';

export function todayIso(): string {
  const now = new Date();
  const offset = now.getTimezoneOffset();
  return new Date(now.getTime() - offset * 60000).toISOString().slice(0, 10);
}

export function statusLabel(status: AttendanceStatus | string): string {
  return String(status)
    .toLowerCase()
    .split('_')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
}

export function isPresent(status: AttendanceStatus): boolean {
  return String(status).toUpperCase() === 'PRESENT';
}

export function isAbsent(status: AttendanceStatus): boolean {
  return String(status).toUpperCase() === 'ABSENT';
}

export function getCurrentAcademicYear(): string {

  const today = new Date();

  const year = today.getFullYear();

  const month = today.getMonth() + 1;

  const startYear =
    month >= 4
      ? year
      : year - 1;

  const endYear =
    startYear + 1;

  return `${startYear}-${endYear}`;
}
