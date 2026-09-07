export function staffDisplayValue(value: unknown): string {
  return value === null || value === undefined || value === ""
    ? "—"
    : String(value);
}
