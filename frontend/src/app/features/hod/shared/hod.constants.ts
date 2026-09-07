export const HOD_FIELDS = [
  "code",
  "employeeCode",
  "fullName",
  "department",
  "email",
  "phone",
  "designation",
  "status",
  "description",
] as const;

export const HOD_TITLE = "HOD Management";

export function hodDisplayValue(value: unknown): string {
  return value === null || value === undefined || value === ""
    ? "—"
    : String(value);
}
