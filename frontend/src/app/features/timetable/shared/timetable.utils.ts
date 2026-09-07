export function timetableDisplayValue(value: unknown): string {
  return value === null || value === undefined || value === ""
    ? "—"
    : String(value);
}
