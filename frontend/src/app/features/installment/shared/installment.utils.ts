export function installmentDisplayValue(value: unknown): string { return value === null || value === undefined || value === '' ? '—' : String(value); }
