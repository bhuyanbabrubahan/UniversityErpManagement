export function formatAdmissionDate(value: string | null | undefined): string {
  if (!value) {
    return '—';
  }

  const date = new Date(`${value}T00:00:00`);

  if (Number.isNaN(date.getTime())) {
    return '—';
  }

  return date.toLocaleDateString();
}

export function toDateInputValue(
  value: string | null | undefined
): string {
  if (!value) {
    return '';
  }

  return value.slice(0, 10);
}

export function normalizeNullableText(
  value: string | null | undefined
): string | null {
  const normalized = value?.trim() ?? '';
  return normalized || null;
}
