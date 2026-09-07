export function placementDisplayValue(value: unknown): string { return value === null || value === undefined || value === '' ? '—' : String(value); }
